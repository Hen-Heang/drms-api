package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.JwtChangePasswordRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserMapper {

    // ======================
    // INSERT
    // ======================
    AppUser insertPartnerUser(@Param("user") AppUserRequest user);

    AppUser insertMerchantUser(@Param("user") AppUserRequest user);

    // ======================
    // FIND USERS
    // ======================
    AppUser findPartnerUserByEmail(@Param("email") String email);

    AppUser findPartnerUserById(@Param("id") int id);

    AppUser findMerchantUserByEmail(@Param("email") String email);

    // ======================
    // PHONE CHECKS
    // ======================
    boolean checkPhoneNumberFromPartnerPhone(@Param("phone") String phone);

    boolean checkPhoneNumberFromPartnerDetail(@Param("phone") String phone);

    boolean checkPhoneNumberFromMerchantPhone(@Param("phone") String phone);

    boolean checkPhoneNumberFromMerchantDetail(@Param("phone") String phone);

    // ======================
    // ROLE ID
    // ======================
    int getRoleIdByMailPartner(@Param("email") String email);

    int getRoleIdByMailMerchant(@Param("email") String email);

    // ======================
    // VERIFIED
    // ======================
    Boolean getVerifyPartnerEmail(@Param("email") String email);

    boolean getVerifyMerchantEmail(@Param("email") String email);

    // ======================
    // UPDATE PASSWORD
    // ======================
    AppUser updatePartnerUser(@Param("req") JwtChangePasswordRequest req);

    AppUser updateMerchantUser(@Param("req") JwtChangePasswordRequest req);

    // ======================
    // FORGET PASSWORD
    // ======================
    String updateForgetPartnerUser(@Param("email") String email,
                                   @Param("newPassword") String newPassword);

    String updateForgetMerchantUser(@Param("email") String email,
                                    @Param("newPassword") String newPassword);

    // ======================
    // GET USER ID
    // ======================
    int getUserIdByMailPartner(@Param("email") String email);

    int getUserIdByMailMerchant(@Param("email") String email);
}
