package com.heang.drms_api.auth.controller;


import com.heang.drms_api.auth.dto.ApiResponse;
import com.heang.drms_api.auth.service.OtpService;
import com.heang.drms_api.controller.BaseController;
import com.heang.drms_api.exception.BadRequestException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/authorization")
@Tag(name = "API authorization")
@RequiredArgsConstructor
public class OTPController extends BaseController {


    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;
    private final OtpService otpService;


    @PostMapping("/generate")
    public ResponseEntity<?> generateOtp(@RequestParam String email){
        String otpResponse = otpService.generateOtp(email);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.CREATED.value())
                .message("New OTP generated.")
                .data(otpResponse)
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> activateAccount(@RequestParam Integer otp, @RequestParam String email){
        if (otp > 2147483646){
            throw new BadRequestException("Integer value can not exceed 2147483646");
        }
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Activated successfully")
                .data(otpService.verifyOtp(otp, email))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }
}
