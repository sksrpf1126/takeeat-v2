package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.domain.user.ProviderType;
import com.back.takeeat.dto.member.SignupRequest;
import com.back.takeeat.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String memberLoginForm() {
        return "member/loginForm";
    }

    @GetMapping("/policy-agreement")
    public String memberPolicyAgreement(@RequestParam("loginType") String loginType, HttpServletRequest request) {
        if(loginType == null || !loginType.equals("social")) {
            request.getSession().removeAttribute("email");
            request.getSession().removeAttribute("providerType");
        }
        return "member/policyAgreement";
    }

    @GetMapping("/social-signup")
    public String socialSignupForm() {
        return "member/socialSignup";
    }

    @GetMapping("/default-signup")
    public String signupForm() {
        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequest signupRequest, HttpServletRequest request) {
        if((signupRequest.getProviderType() != null) && (signupRequest.getProviderType() != ProviderType.DEFAULT)) {
            validateEmailAndProviderMatch(request, signupRequest.getEmail(), signupRequest.getProviderType());
            memberService.socialSignup(signupRequest);
        }else {
            memberService.signup(signupRequest);
        }
        //@TODO 메인페이지로 리다이렉트 할 것
        return "/";
    }

    private void validateEmailAndProviderMatch(HttpServletRequest request, String inputEmail, ProviderType inputProvider) {
        String sessionEmail = (String) request.getSession().getAttribute("email");
        ProviderType sessionProvider = (ProviderType) request.getSession().getAttribute("providerType");

        if (!sessionEmail.equals(inputEmail) || !sessionProvider.equals(inputProvider)) {
            throw new AuthException(ErrorCode.EMAIL_PROVIDER_MISMATCH_ERROR);
        }

        request.getSession().removeAttribute("email");
        request.getSession().removeAttribute("providerType");
    }

}
