package com.heang.drms_api.partner.controller;

import com.heang.drms_api.auth.dto.ApiResponse;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.controller.BaseController;
import com.heang.drms_api.partner.dto.PartnerRequest;
import com.heang.drms_api.partner.model.Partner;
import com.heang.drms_api.partner.service.PartnerInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Tag(name = "Partner Profile Controller")
@SecurityRequirement(name = "bearer")
@RestController
@RequiredArgsConstructor
@RequestMapping("${base.partner.v1}profiles")

public class PartnerInfoController extends BaseController {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;

    private final PartnerInfoService userProfileService;


    @Operation(summary = "Get user profile")
    @GetMapping("/")
    public ResponseEntity<?> getUserProfileById() throws ParseException, NotFoundException {
        AppUser appUser=(AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId= appUser.getId();
        return ResponseEntity.ok( new ApiResponse<>(
                HttpStatus.OK.value(),
                "Fetched successfully.",
                userProfileService.getUserProfile(currentUserId),
                formatter.format(date= new Date())
        ));

    }
    @Operation(summary = "Create profile")
    @PostMapping("/")
    public ResponseEntity<?> addUserProfile(@RequestBody PartnerRequest distributorRequest) throws ParseException {
        AppUser appUser= (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId= appUser.getId();
        ApiResponse<Partner> response = ApiResponse.<Partner>builder()
                .status(HttpStatus.CREATED.value())
                .message("User profile added")
                .data(userProfileService.addUserProfile(currentUserId, distributorRequest))
                .date(formatter.format(date=new Date()))
                .build();
        return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @Operation(summary = "Update profile")
    @PutMapping("/")
    public ResponseEntity<?> updateUserProfile(@RequestBody PartnerRequest distributorRequest) throws ParseException {
        AppUser appUser= (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId= appUser.getId();
        ApiResponse<Partner> response = ApiResponse.<Partner>builder()
                .status(HttpStatus.OK.value())
                .message("successfully updated")
                .data(userProfileService.updateUserProfile(currentUserId, distributorRequest))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }
}
