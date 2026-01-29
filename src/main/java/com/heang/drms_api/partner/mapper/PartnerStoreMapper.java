package com.heang.drms_api.partner.mapper;


import com.heang.drms_api.partner.model.Category;
import com.heang.drms_api.partner.model.product.Product;
import com.heang.drms_api.partner.model.store.Store;
import com.heang.drms_api.merchant.model.StoreMerchant;
import com.heang.drms_api.partner.model.store.StoreRating;
import com.heang.drms_api.partner.model.store.StoreRatingRequest;
import com.heang.drms_api.partner.model.store.StoreRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartnerStoreMapper {

    Store createNewStore(@Param("store") StoreRequest storeRequest,
                         @Param("currentUserId") Integer currentUserId);

    Double getRating(@Param("id") Integer id);

    Integer getRatingCount(@Param("storeId") Integer storeId);

    List<Integer> getBookmarkStoreId(@Param("currentUserId") Integer currentUserId);

    Integer checkStoreIfCreated(@Param("currentUserId") Integer currentUserId);

    Store getUserStore(@Param("currentUserId") Integer currentUserId);

    List<StoreMerchant> getAllStore();

    List<String> getAdditionalPhone(@Param("storeId") Integer storeId);

    Store editAllFieldUserStore(@Param("storeId") Integer storeId,
                                @Param("store") StoreRequest storeRequest);

    Integer getStoreIdByUserId(@Param("currentUserId") Integer currentUserId);

    Boolean checkDuplicateStoreName(@Param("name") String name);

    String deleteUserStore(@Param("currentUserId") Integer currentUserId);

    String disableStore(@Param("currentUserId") Integer currentUserId);

    String enableStore(@Param("currentUserId") Integer currentUserId);

    Boolean checkIfStoreExist(@Param("storeId") Integer storeId);

    StoreMerchant getStoreById(@Param("id") Integer id);

    String bookmarkStoreById(@Param("storeId") Integer storeId,
                             @Param("currentUser") Integer currentUser);

    String removeBookmarkStoreById(@Param("storeId") Integer storeId,
                                   @Param("currentUser") Integer currentUser);

    Boolean checkAlreadyBookmarked(@Param("storeId") Integer storeId,
                                   @Param("currentUserId") Integer currentUserId);

    Boolean checkAlreadyRated(@Param("storeId") Integer storeId,
                              @Param("currentUserId") Integer currentUserId);

    StoreRating ratingStoreById(@Param("storeId") Integer storeId,
                                @Param("currentUser") Integer currentUser,
                                @Param("request") StoreRatingRequest storeRatingRequest);

    StoreRating getRatingByStoreId(@Param("storeId") Integer storeId,
                                   @Param("currentUser") Integer currentUser);

    StoreRating editRatingByStoreId(@Param("storeId") Integer storeId,
                                    @Param("currentUser") Integer currentUser,
                                    @Param("request") StoreRatingRequest ratingRequest);

    String deleteRatingByStoreId(@Param("storeId") Integer storeId,
                                 @Param("currentUser") Integer currentUser);

    List<Product> getProductListingByStoreIdASC(@Param("storeId") Integer storeId,
                                                @Param("by") String by);

    List<Product> getProductListingByStoreIdDESC(@Param("storeId") Integer storeId,
                                                 @Param("by") String by);

    List<Product> getProductListingByStoreIdAndCategoryId(@Param("storeId") Integer storeId,
                                                          @Param("categoryId") Integer categoryId);

    Category getCategoryByCategoryId(@Param("id") Integer id);

    List<StoreMerchant> getAllUserStoreSortByDateASC(@Param("pageNumber") Integer pageNumber,
                                                     @Param("pageSize") Integer pageSize);

    List<StoreMerchant> getAllUserStoreSortByDateDESC(@Param("pageNumber") Integer pageNumber,
                                                      @Param("pageSize") Integer pageSize);

    Integer getTotalStores();

    Integer getTotalRatedStores(@Param("merchantId") Integer merchantId);

    List<StoreMerchant> getAllUserStoreSortByCurrentUserFavoriteDESC(@Param("pageNumber") Integer pageNumber,
                                                                     @Param("pageSize") Integer pageSize,
                                                                     @Param("currentUser") Integer currentUser);

    List<StoreMerchant> getAllBookmarkedStore(@Param("pageNumber") Integer pageNumber,
                                              @Param("pageSize") Integer pageSize,
                                              @Param("currentUser") Integer currentUser);

    List<StoreMerchant> searchStoreByName(@Param("pageNumber") Integer pageNumber,
                                          @Param("pageSize") Integer pageSize,
                                          @Param("name") String name);

    List<StoreMerchant> getAllUserStoreSortByRatedStarASC(@Param("pageNumber") Integer pageNumber,
                                                          @Param("pageSize") Integer pageSize);

    List<StoreMerchant> getAllUserStoreSortByRatedStarDESC(@Param("pageNumber") Integer pageNumber,
                                                           @Param("pageSize") Integer pageSize);

    List<StoreMerchant> getAllUserStoreSortByNameASC(@Param("pageNumber") Integer pageNumber,
                                                     @Param("pageSize") Integer pageSize);

    List<StoreMerchant> getAllUserStoreSortByNameDESC(@Param("pageNumber") Integer pageNumber,
                                                      @Param("pageSize") Integer pageSize);

    Integer getStoreIdByProductId(@Param("productId") Integer productId);

    boolean checkIfStoreIsDisable(@Param("currentUserId") Integer currentUserId);

    void addAdditionalPhone(@Param("phone") String phone,
                            @Param("id") Integer id);

    void deleteAdditionalPhone(@Param("storeId") Integer storeId);

    Integer getTotalBookmarkedStores(@Param("merchantId") Integer merchantId);

    String getStoreImageByStoreId(@Param("id") Integer id);

    String getStoreNameById(@Param("id") Integer id);

    boolean checkDuplicatePhone(@Param("primaryPhone") String primaryPhone);

    List<Category> getCategoryListingByStoreId(@Param("storeId") Integer storeId);

    boolean checkIfCategoryExistInStore(@Param("storeId") Integer storeId,
                                        @Param("categoryId") Integer categoryId);

    String getStoreImageById(@Param("id") Integer id);

    List<Integer> checkStock(@Param("orderId") Integer orderId);

    List<StoreMerchant> getStoresByCategorySearchASC(@Param("name") String name,
                                                     @Param("by") String by);

    List<StoreMerchant> getStoresByCategorySearchDESC(@Param("name") String name,
                                                      @Param("by") String by);

    List<Integer> getStoreIdsByCategorySearchASC(@Param("name") String name,
                                                 @Param("by") String by);

    List<Integer> getStoreIdsByCategorySearchDESC(@Param("name") String name,
                                                  @Param("by") String by);

    List<StoreMerchant> getStoresByProductSearchASC(@Param("name") String name,
                                                    @Param("by") String by);

    List<StoreMerchant> getStoresByProductSearchDESC(@Param("name") String name,
                                                     @Param("by") String by);

    List<Integer> getStoreIdByProductSearchASC(@Param("name") String name,
                                               @Param("by") String by);

    List<Integer> getStoreIdByProductSearchDESC(@Param("name") String name,
                                                @Param("by") String by);

    List<StoreMerchant> getStoresByNameSearchASC(@Param("name") String name,
                                                 @Param("by") String by);

    List<StoreMerchant> getStoresByNameSearchDESC(@Param("name") String name,
                                                  @Param("by") String by);

    List<Integer> getStoresIdByNameSearchASC(@Param("name") String name,
                                             @Param("by") String by);

    List<Integer> getStoresIdByNameSearchDESC(@Param("name") String name,
                                              @Param("by") String by);

    String getStoreAddressById(@Param("id") Integer id);

    String getStorePrimaryPhoneById(@Param("id") Integer id);

    String getStoreEmailByStoreId(@Param("id") Integer id);

    List<StoreMerchant> getStoresByStoreIdsASC(@Param("combinedList") String combinedList);

    List<StoreMerchant> getStoresByStoreIdsDESC(@Param("combinedList") String combinedList);
    Integer getStoreIdByDraftId(@Param("id") Integer id);

}
