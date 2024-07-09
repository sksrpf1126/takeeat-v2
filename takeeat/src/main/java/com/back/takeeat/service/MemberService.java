package com.back.takeeat.service;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.dto.member.SignupRequest;
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
    public void signup(SignupRequest signupRequest) {
        validatePassword(signupRequest.getPassword(), signupRequest.getPasswordCheck());
        validateDuplicateMember(signupRequest.getEmail());

        Member member = signupRequest.toMember();
        member.setEncryptPassword(encoder.encode(signupRequest.getPassword()));
        memberRepository.save(member);
    }

    @Transactional
    public void socialSignup(SignupRequest signupRequest) {
        validateDuplicateMember(signupRequest.getEmail());
        Member member = signupRequest.toMemberFromSocial();
        memberRepository.save(member);
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
