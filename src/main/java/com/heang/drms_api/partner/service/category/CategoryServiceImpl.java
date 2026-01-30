package com.heang.drms_api.partner.service.category;


import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.exception.ConflictException;
import com.heang.drms_api.exception.InternalServerErrorException;
import com.heang.drms_api.partner.mapper.CategoryMapper;
import com.heang.drms_api.partner.model.Category;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date;


    private final CategoryMapper categoryMapper;


    @Override
    public Category createCategories(String categoryName) throws NotFoundException, ParseException {
        AppUser appUser = (AppUser) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
//  Get user id
        assert appUser != null;
        Integer currentUserId = appUser.getId();
        if (categoryName.isBlank()) {
            throw new IllegalArgumentException("Category categoryName cannot be empty");
        }
        if (!categoryMapper.storeIsExist(currentUserId)) {
            throw new NotFoundException("User has not created store yet, Please create store first");
        }
        Integer storeId = categoryMapper.getStoreIdByCurrentUserId(currentUserId);
        // trim white space
        categoryName = categoryName.trim().toLowerCase();
        Category category;
        // if category is already created or haven't been created
        if (!categoryMapper.checkDuplicateCategory(categoryName)) { // if not yet create, create new
            Integer newCategoryId = categoryMapper.createNewCategory(categoryName);
            category = categoryMapper.createNewStoreCategory(storeId, newCategoryId);
        } else { // if already created, just get the id and insert
            Integer categoryId = categoryMapper.getCategoryIdByName(categoryName);
            if (categoryMapper.checkIfStoreCategoryDuplicate(storeId, categoryId)) {
                throw new ConflictException("Fail to create category because store already created this category.");
            }
            category = categoryMapper.createNewStoreCategory(storeId, categoryId);
        }
        // if insert fail
        if (category == null) {
            throw new InternalServerErrorException("Fail to create category. Something went during the process.");
        }
        category.setCreatedDate(formatter.format(formatter.parse(category.getCreatedDate())));
        category.setUpdatedDate(formatter.format(formatter.parse(category.getUpdatedDate())));
        return category;
    }
}
