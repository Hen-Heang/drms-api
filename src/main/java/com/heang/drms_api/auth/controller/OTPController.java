package com.heang.drms_api.auth.controller;


import com.heang.drms_api.auth.service.OtpService;
import com.heang.drms_api.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OTPController extends BaseController {

    private final OtpService otpService;
    @PostMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestParam String email) {
        String otp = otpService.generateOtp(email);
        return ok(otp);

    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestParam Integer otp, @RequestParam String email){
        String result = otpService.verifyOtp(otp, email);
        return ok(result);
    }
}
