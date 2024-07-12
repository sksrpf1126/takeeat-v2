package com.back.takeeat.service;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.member.SignupRequest;
import com.back.takeeat.dto.member.SocialSignupRequest;
import com.back.takeeat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void registerMember(SignupRequest signupRequest) {

        validatePassword(signupRequest.getPassword(), signupRequest.getPasswordCheck());
        validateDuplicateMember(signupRequest.getEmail());

        Member member = signupRequest.toMember();
        member.setEncryptPassword(encoder.encode(signupRequest.getPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public void registerSocialMember(SocialSignupRequest signupRequest, Member member) {
        validateDuplicateMember(member.getEmail());
        member.socialMemberSignup(signupRequest.getName(), signupRequest.getNickname(), signupRequest.getPhone());
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

    private void validateDuplicateMember(String email) {
        if(memberRepository.existsByEmail(email)) {
            log.error("validateDuplicateMember - Exists Member Email");
            throw new AuthException(ErrorCode.MEMBER_EXISTS);
        }
    }

}
