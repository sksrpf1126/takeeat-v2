package com.back.takeeat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    @GetMapping("/login")
    public String memberLoginForm() {
        return "member/loginForm";
    }

    @GetMapping("/policy-agreement")
    public String memberPolicyAgreement() {
        return "member/policyAgreement";
    }

}
