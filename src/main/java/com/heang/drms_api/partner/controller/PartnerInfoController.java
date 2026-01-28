package com.heang.drms_api.partner.controller;

import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.controller.BaseController;
import com.heang.drms_api.partner.dto.PartnerRequest;
import com.heang.drms_api.partner.service.PartnerInfoService;
import com.heang.drms_api.security.SecurityUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Tag(name = "Partner Profile Controller")
@SecurityRequirement(name = "bearer")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/partners")
public class PartnerInfoController extends BaseController {
    private final PartnerInfoService partnerInfoService;

    @PostMapping("/create")
    public ResponseEntity<?> createPartner(@RequestBody PartnerRequest partnerRequest) {
        AppUser appUser= (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        assert appUser != null;
        Integer currentUserId= appUser.getId();
        partnerInfoService.createPartnerInfo(currentUserId,partnerRequest);
        Integer userId= appUser.getId();
//        Integer userId = com.heang.drms_api.security.SecurityUtils.currentUserId();
//        Integer userId = SecurityUtils.currentUserId();
        return ok();
    }

}
