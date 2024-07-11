package com.back.takeeat.controller;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.common.exception.ErrorPageException;
import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.ProviderType;
import com.back.takeeat.dto.member.SignupRequest;
import com.back.takeeat.dto.member.SocialSignupRequest;
import com.back.takeeat.security.LoginMember;
import com.back.takeeat.service.EmailService;
import com.back.takeeat.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;

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
    public String signupForm(@LoginMember Member member, Model model) {
        if(member != null &&  member.getId() != null) {
            //@TODO 메인페이지 경로로 변경할 것
            return "redirect:/market/info";
        }

        SignupRequest signupRequest = SignupRequest.builder().build();

        model.addAttribute("signupRequest", signupRequest);

        return "member/signup";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupRequest") SignupRequest signupRequest, BindingResult bindingResult) {

        //데이터 유효성 검증
        if(bindingResult.hasErrors()) {
            return "member/signup";
        }

        if(!memberService.duplicateMemberLoginId(signupRequest.getMemberLoginId())) {
            bindingResult.rejectValue("memberLoginId", "required", "사용할 수 없는 아이디입니다.");
        }

        if(!memberService.duplicateEmail(signupRequest.getEmail())) {
            bindingResult.rejectValue("email", "required", "사용할 수 없는 이메일입니다.");
        }

        if(!signupRequest.getPassword().equals(signupRequest.getPasswordCheck())) {
            bindingResult.rejectValue("passwordCheck", "required", "비밀번호와 비밀번호 확인 값이 일치하지 않습니다.");
        }

        emailService.validateAuthCode(signupRequest.getEmail(), signupRequest.getAuthCode(), bindingResult);

        //비즈니스 로직 검증
        if(bindingResult.hasErrors()) {
            return "member/signup";
        }

        memberService.registerMember(signupRequest);

        return "redirect:/member/login";
    }

    @PostMapping("/social-signup")
    public String socialSignup(@LoginMember Member member, @ModelAttribute SocialSignupRequest signupRequest, HttpServletRequest request) {
        validateEmailAndProviderMatch(request, member.getEmail(), signupRequest.getProviderType());
        memberService.registerSocialMember(signupRequest, member);

        //@TODO 메인페이지로 리다이렉트 할 것
        return "redirect:/market/info";
    }

    @GetMapping("/id-check")
    @ResponseBody
    public boolean idCheck(@RequestParam("memberLoginId") String memberLoginId) {
        return memberService.duplicateMemberLoginId(memberLoginId);
    }

    @GetMapping("/email-check")
    @ResponseBody
    public boolean emailCheck(@RequestParam("email") String email) {
        return memberService.duplicateEmail(email);
    }

    @PostMapping("/email-send")
    @ResponseBody
    public String sendAuthCode(@RequestParam("email") String email) {
        emailService.authenticationEmail(email);
        return "인증 코드를 발송했습니다.";
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
