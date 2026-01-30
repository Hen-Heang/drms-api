package com.heang.drms_api.partner.mapper;

import com.heang.drms_api.partner.dto.CategoryRequest;
import com.heang.drms_api.partner.model.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Category getDuplicateCategory(@Param("name") String name);

    Category insertCategory(@Param("category") CategoryRequest category);

    List<Category> getAllCategory(@Param("storeId") Integer storeId,
                                  @Param("pageNumber") Integer pageNumber,
                                  @Param("pageSize") Integer pageSize);

    Category getCategoryById(@Param("id") Integer id,
                             @Param("storeId") Integer storeId);

    Category editCategory(@Param("categoryId") Integer categoryId,
                          @Param("id") Integer id,
                          @Param("storeId") Integer storeId);

    String deleteCategory(@Param("id") Integer id,
                          @Param("storeId") Integer storeId);

    boolean checkIfExist(@Param("name") String name);

    Integer getStoreIdByCurrentUserId(@Param("userId") Integer userId);

    void addCategoryToStore(@Param("categoryId") Integer categoryId,
                            @Param("storeId") Integer storeId);

    Integer getCategoryInCurrentStoreId(@Param("categoryId") Integer categoryId,
                                        @Param("storeId") Integer storeId);

    List<Category> getCategoryByCurrentUserId(@Param("storeId") Integer storeId);

    boolean checkDuplicateCategory(@Param("name") String name);

    Integer createNewCategory(@Param("name") String name);

    Category createNewStoreCategory(@Param("storeId") Integer storeId,
                                    @Param("newCategoryId") Integer newCategoryId);

    String getCategoryNameById(@Param("id") Integer id);

    String getCategoryCreatedDateById(@Param("id") Integer id);

    String getCategoryUpdatedById(@Param("id") Integer id);

    Integer getCategoryIdByName(@Param("name") String name);

    boolean checkIfStoreCategoryDuplicate(@Param("storeId") Integer storeId,
                                          @Param("categoryId") Integer categoryId);

    Integer findTotalCategory(@Param("storeId") Integer storeId);

    List<Category> searchCategoryByName(@Param("name") String name,
                                        @Param("storeId") Integer storeId,
                                        @Param("pageNumber") Integer pageNumber,
                                        @Param("pageSize") Integer pageSize);

    boolean storeIsExist(@Param("currentUserId") Integer currentUserId);

    String moveProductCategory(@Param("storeId") Integer storeId,
                               @Param("id") Integer id);

    boolean checkIfCategoryHaveProduct(@Param("storeId") Integer storeId,
                                       @Param("id") Integer id);

    void replaceProductCategory(@Param("oldId") Integer oldId,
                                @Param("newId") Integer newId,
                                @Param("storeId") Integer storeId);

    Integer getCategoryIdByProductId(@Param("id") Integer id);
}
