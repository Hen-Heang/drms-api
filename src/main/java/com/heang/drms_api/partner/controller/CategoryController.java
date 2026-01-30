package com.heang.drms_api.partner.controller;


import com.heang.drms_api.auth.dto.ApiResponse;
import com.heang.drms_api.partner.model.Category;
import com.heang.drms_api.partner.service.category.CategoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@Tag(name = "Partner Category Controller")
@RequestMapping("${base.partner.v1}categories")
@SecurityRequirement(name = "bearer")
@RequiredArgsConstructor

public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<?> createCategoriesStore(
        @RequestParam String categoryName
    ) throws NotFoundException, ParseException {
        ApiResponse<Category> response = ApiResponse.<Category>builder()
                .status(200)
                .message("Categories created successfully.")
                .data(categoryService.createCategories(categoryName))
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
