package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.common.exception.ErrorPageException;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.ProviderType;
import com.back.takeeat.dto.member.SignupRequest;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    //@TODO 테스트 후 지울 것
    @GetMapping("/test")
    public String test(@RequestParam("errorType") String errorType) {

        if(errorType.equals("api")) {
            throw new BaseException(ErrorCode.MEMBER_EXISTS);
        }else if(errorType.equals("page")) {
            throw new ErrorPageException(ErrorCode.ORDER_UNAUTHORIZED);
        }

        return "member/loginForm";
    }

    @GetMapping("/login")
    public String memberLoginForm(
            @LoginMember Member member,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "exception", required = false) String exception,
            Model model
    ) {
        if(member != null &&  member.getId() != null) {
            //@TODO 메인페이지 경로로 변경할 것
            return "redirect:/market/info";
        }

        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        return "member/loginForm";
    }

    @GetMapping("/policy-agreement")
    public String memberPolicyAgreement(@LoginMember Member member, @RequestParam(value = "loginType", required = false) String loginType, HttpServletRequest request) {
        if(member != null &&  member.getId() != null) {
            //@TODO 메인페이지 경로로 변경할 것
            return "redirect:/market/info";
        }

        if(loginType == null || !loginType.equals("social")) {
            request.getSession().removeAttribute("email");
            request.getSession().removeAttribute("providerType");
        }
        return "member/policyAgreement";
    }

    @GetMapping("/social-signup")
    public String socialSignupForm(@LoginMember Member member) {
        if(member != null &&  member.getId() != null) {
            //@TODO 메인페이지 경로로 변경할 것
            return "redirect:/market/info";
        }

        return "member/socialSignup";
    }

    @GetMapping("/default-signup")
    public String signupForm(@LoginMember Member member) {
        if(member != null &&  member.getId() != null) {
            //@TODO 메인페이지 경로로 변경할 것
            return "redirect:/market/info";
        }

        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@LoginMember Member member, @ModelAttribute SignupRequest signupRequest, HttpServletRequest request) {
        if((signupRequest.getProviderType() != null) && (signupRequest.getProviderType() != ProviderType.DEFAULT)) {
            validateEmailAndProviderMatch(request, signupRequest.getEmail(), signupRequest.getProviderType());
            memberService.socialSignup(signupRequest, member);
        }else {
            memberService.signup(signupRequest);
        }
        //@TODO 메인페이지로 리다이렉트 할 것
        return "redirect:/member/login";
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
