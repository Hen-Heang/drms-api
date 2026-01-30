package com.heang.drms_api.partner.controller;

import com.heang.drms_api.auth.dto.ApiResponse;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.controller.BaseController;
import com.heang.drms_api.partner.model.store.Store;
import com.heang.drms_api.partner.dto.store.StoreRequest;
import com.heang.drms_api.partner.service.store.PartnerStoreService;
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

@RestController
@Tag(name = "Partner Store Controller")
@RequestMapping("${base.partner.v1}stores")
@SecurityRequirement(name = "bearer")
@RequiredArgsConstructor
public class PartnerStoreController extends BaseController {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;
    private final PartnerStoreService partnerStoreService;


    @Operation(summary = "Setup new store")
    @PostMapping("")
    public ResponseEntity<?> createStore(@RequestBody StoreRequest storeRequest) throws ParseException {
        AppUser appUser = (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId = appUser.getId();
        ApiResponse<Store> storeApiResponse = ApiResponse.<Store>builder()
                .status(HttpStatus.CREATED.value())
                .message("Created new Store.")
                .data(partnerStoreService.createNewStore(storeRequest, currentUserId))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(storeApiResponse);
    }

    @GetMapping("/user/")
    public ResponseEntity<?> getUserStore() throws ParseException, NotFoundException {
        AppUser appUser = (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId = appUser.getId();
        ApiResponse<Store> response = ApiResponse.<Store>builder()
                .status(HttpStatus.OK.value())
                .message("Fetched successfully.")
                .data(partnerStoreService.getUserStore(currentUserId))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("")
    public ResponseEntity<?> editAllFieldUserStore(@RequestBody StoreRequest storeRequest) throws ParseException {
        AppUser appUser = (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId = appUser.getId();
        ApiResponse<Store> response = ApiResponse.<Store>builder()
                .status(HttpStatus.OK.value())
                .message("Store updated.")
                .data(partnerStoreService.editAllFieldUserStore(currentUserId, storeRequest))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUserStore() {
        AppUser appUser = (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId = appUser.getId();
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Store deleted.")
                .data(partnerStoreService.deleteUserStore(currentUserId))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/disable")
    public ResponseEntity<?> disableStore() {
        AppUser appUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer currentUserId = appUser.getId();
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Store is deactivated.")
                .data(partnerStoreService.disableStore(currentUserId))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/enable")
    public ResponseEntity<?> enableStore() {
        AppUser appUser = (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
        assert appUser != null;
        Integer currentUserId = appUser.getId();
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Store is now active.")
                .data(partnerStoreService.enableStore(currentUserId))
                .date(formatter.format(date = new Date()))
                .build();
        return ResponseEntity.ok(response);
    }



}
