package com.back.takeeat.service;

import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.user.EmailAuth;
import com.back.takeeat.repository.EmailAuthRepository;
import com.back.takeeat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final MemberRepository memberRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final JavaMailSender javaMailSender;

    @Transactional
    public void authenticationEmail(String email) {

        if(memberRepository.existsByEmail(email)) {
            throw new BaseException(ErrorCode.MEMBER_EXISTS);
        }

        String authCode = this.createCode();

        emailAuthRepository.save(EmailAuth.builder()
                .email(email)
                .authCode(authCode)
                .build());

        this.sendEmail(email, authCode, "[TakeEat] 회원가입 이메일 인증");
    }

    @Transactional
    public void findMemberLoginIdSendEmail(String email) {

        if(!memberRepository.existsByEmail(email)) {
            throw new BaseException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String authCode = this.createCode();

        emailAuthRepository.save(EmailAuth.builder()
                .email(email)
                .authCode(authCode)
                .build());

        this.sendEmail(email, authCode, "[TakeEat] 아이디 찾기 인증");
    }

    @Transactional(readOnly = true)
    public void validateAuthCode(String email, String authCode, BindingResult bindingResult) {
        EmailAuth findEmailAuth = emailAuthRepository.findTop1ByEmailOrderByCreatedTimeDesc(email).orElse(null);

        if(findEmailAuth == null) {
            bindingResult.rejectValue("authCode", "required", "해당 이메일로 인증코드가 발송되지 않았습니다.");
        }else if(!findEmailAuth.getAuthCode().equals(authCode)) {
            bindingResult.rejectValue("authCode", "required", "인증코드를 다시 확인해 주세요.");
        }else if(findEmailAuth.expireCheck()) {
            bindingResult.rejectValue("authCode", "required", "인증코드 유효시간이 지났습니다.");
        }

    }

    @Async
    public void sendEmail(String email, String authCode, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText("인증 코드는 " + authCode + " 입니다.");

        log.info("{} 로 인증코드를 방송 했습니다. code : {}", email, authCode);

        javaMailSender.send(mailMessage);
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new BaseException(ErrorCode.NO_SUCH_AUTH_CODE);
        }
    }

}
