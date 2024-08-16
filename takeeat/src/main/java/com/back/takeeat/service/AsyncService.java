package com.back.takeeat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

    private final JavaMailSender javaMailSender;

    @Async
    public void sendEmail(String email, String authCode, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText("인증 코드는 " + authCode + " 입니다.");

        log.info("{} 로 인증코드를 방송 했습니다. code : {}", email, authCode);

        javaMailSender.send(mailMessage);
    }

}
