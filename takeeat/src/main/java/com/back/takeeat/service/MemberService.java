package com.back.takeeat.service;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.common.exception.ErrorPageException;
import com.back.takeeat.domain.cart.Cart;
import com.back.takeeat.domain.order.OrderStatus;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.member.SignupRequest;
import com.back.takeeat.dto.member.SocialSignupRequest;
import com.back.takeeat.repository.CartRepository;
import com.back.takeeat.repository.MemberRepository;
import com.back.takeeat.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final S3Service s3Service;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void registerMember(SignupRequest signupRequest) {

        validatePassword(signupRequest.getPassword(), signupRequest.getPasswordCheck());
        validateDuplicateMember(signupRequest.getEmail());

        Member member = signupRequest.toMember();
        member.setEncryptPassword(encoder.encode(signupRequest.getPassword()));
        memberProfileUpload(signupRequest.getProfile(), member);
        cartRepository.save(Cart.builder().member(member).build());

        memberRepository.save(member);
    }

    @Transactional
    public void registerSocialMember(SocialSignupRequest signupRequest, Member member) {
        validateDuplicateMember(member.getEmail());
        member.socialMemberSignup(signupRequest.getName(), signupRequest.getNickname(), signupRequest.getPhone());
        memberProfileUpload(signupRequest.getProfile(), member);
        cartRepository.save(Cart.builder().member(member).build());
        memberRepository.save(member);
    }

    /**
     * @return 중복되는 로그인 아이디가 있다면 false, 없다면 true
     */
    @Transactional(readOnly = true)
    public Boolean duplicateMemberLoginId(String memberLoginId) {
        return !memberRepository.existsByMemberLoginId(memberLoginId);
    }

    /**
     * @return 중복되는 이메일이 있다면 false, 없다면 true
     */
    @Transactional(readOnly = true)
    public Boolean duplicateEmail(String email) {
        return !memberRepository.existsByEmail(email);
    }

    private void validatePassword(String password, String passwordCheck) {
        if(!password.equals(passwordCheck)) {
            log.error("validatePassword - Password not equals PasswordCheck");
            throw new AuthException(ErrorCode.MEMBER_PASSWORD_MISMATCH);
        }
    }

    @Transactional(readOnly = true)
    public String findMemberLoginId(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new ErrorPageException(ErrorCode.MEMBER_NOT_FOUND)).getMemberLoginId();
    }

    @Transactional
    public String findMemberPassword(String memberLoginId, BindingResult bindingResult) {

        Member findMember = memberRepository.findByMemberLoginId(memberLoginId)
                .orElse(null);

        if(findMember == null) {
            bindingResult.rejectValue("memberLoginId", "required", "아이디를 다시 확인해 주세요");
            return null;
        }

        String newPassword = this.getRandomPassword(8);
        findMember.setEncryptPassword(encoder.encode(newPassword));

        return newPassword;
    }

    @Transactional
    public void socialMemberDelete(Member member) {
        List<OrderStatus> statuses = List.of(OrderStatus.WAIT, OrderStatus.ACCEPT);
        //대기중이거나, 수락상태인 주문이 존재할 경우 탈퇴 불가
        validateExistsOrders(member.getId(), statuses);

        String accessToken = this.getSocialMemberAccessToken(member.getEmail());

//        googleOAuthUnlink(accessToken);
        kakaoOAuthUnlink(accessToken);

        log.info("test");
    }

    /**
     * @param email 소셜 회원 이메일
     * @return Access Token
     */
    private String getSocialMemberAccessToken(String email) {
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oAuth2AuthorizedClientService.loadAuthorizedClient("google", email);

        if(oAuth2AuthorizedClient == null) {
            throw new AuthException(ErrorCode.SOCIAL_NOT_TOKEN);
        }
        return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    }

    private void kakaoOAuthUnlink(String accessToken) {
        try {
            URI uri = new URI("https://kapi.kakao.com/v1/user/unlink");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, Object.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("카카오 연동 해제에 성공했습니다.");
            } else {
                log.error("카카오 연동 해제 실패. 상태 코드: {}", response.getStatusCode());
                throw new BaseException(ErrorCode.DELETE_MEMBER_ERROR);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();

            // 1 -> 카카오 서버 문제, 2 -> URL, 인자 정보에 문제, 401 -> 유효하지 않은 토큰인 경우
            if (errorMessage != null && errorMessage.contains("401")) {
                throw new AuthException(ErrorCode.SOCIAL_NOT_TOKEN);
            } else {
                throw new BaseException(ErrorCode.DELETE_MEMBER_ERROR);
            }
        }
    }

    private void googleOAuthUnlink(String accessToken) {
        try{
            URI uri = new URI("https://oauth2.googleapis.com/revoke?token="+accessToken);
            ResponseEntity<Object> response = restTemplate.exchange(uri, HttpMethod.POST, null, Object.class);

            // 상태 코드 확인
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("구글 연동 해제에 성공했습니다.");
            }

        } catch (Exception e) {
            String errorMessage = e.getMessage();

            if(errorMessage != null && errorMessage.contains("invalid_token")) {
                throw new AuthException(ErrorCode.SOCIAL_NOT_TOKEN);
            } else {
                throw new BaseException(ErrorCode.DELETE_MEMBER_ERROR);
            }
        }
    }

    private void validateExistsOrders(Long memberId, List<OrderStatus> statuses) {
        boolean existsOrders = orderRepository.existsByMemberIdAndOrderStatusIn(memberId, statuses);

        if(existsOrders) throw new BaseException(ErrorCode.MEMBER_ORDER_EXISTS);
    }

    /**
     * 임시 비밀번호 생성 메서드
     */
    private String getRandomPassword(int len){
        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

        int idx = 0; StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            idx = (int) (charSet.length * Math.random());
            //36 * 생성된 난수를 Int로 추출 (소숫점제거)
            sb.append(charSet[idx]);
        }
        return sb.toString();

    }

    private void validateDuplicateMember(String email) {
        if(memberRepository.existsByEmail(email)) {
            log.error("validateDuplicateMember - Exists Member Email");
            throw new AuthException(ErrorCode.MEMBER_EXISTS);
        }
    }

    private void memberProfileUpload(MultipartFile profile, Member member) {
        if(profile != null && !profile.isEmpty()) {
            String profilePath = s3Service.uploadSingleFile(profile);
            member.profileUpload(profilePath);
        }
    }

}
