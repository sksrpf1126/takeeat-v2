package com.back.takeeat.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Controller
public class MainController {

    @Value("${KAKAO_API_KEY}")
    String KAKAO_API_KEY;

    @GetMapping("/")
    public String mainPage(Model model) {

        model.addAttribute("KAKAO_API_KEY", KAKAO_API_KEY);
        return "/mainPage/home";
    }

    @PostMapping("/saveGPSInfo")
    public ResponseEntity<String> saveGPSInfo(@RequestBody Map<String, Object> gpsData, HttpSession session) {
        double latitude = (Double) gpsData.get("latitude");
        double longitude = (Double) gpsData.get("longitude");
        String addr = (String) gpsData.get("addr");

        session.setAttribute("latitude", latitude);
        session.setAttribute("longitude", longitude);
        session.setAttribute("addr", addr);

        return ResponseEntity.ok("위치 정보 저장 성공");
    }
}
