package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.JwtChangePasswordRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserMapper {

    AppUser insertDistributorUser(@Param("user") AppUserRequest user);

    AppUser insertRetailerUser(@Param("user") AppUserRequest user);

    AppUser findDistributorUserByEmail(@Param("email") String email);

    AppUser findDistributorUserById(@Param("id") int id);

    AppUser findRetailerUserByEmail(@Param("email") String email);

    boolean checkPhoneNumberFromDistributorPhone(@Param("phone") String phone);

    boolean checkPhoneNumberFromDistributorDetail(@Param("phone") String phone);

    boolean checkPhoneNumberFromRetailerPhone(@Param("phone") String phone);

    boolean checkPhoneNumberFromRetailerDetail(@Param("phone") String phone);

    int getRoleIdByMail(@Param("email") String email);

    int getRoleIdByMailRetailer(@Param("email") String email);

    boolean getVerifyDistributorEmail(@Param("email") String email);

    boolean getVerifyRetailerEmail(@Param("email") String email);

    AppUser updateDistributorUser(@Param("req") JwtChangePasswordRequest req);

    AppUser updateRetailerUser(@Param("req") JwtChangePasswordRequest req);

    String updateForgetDistributorUser(@Param("email") String email, @Param("newPassword") String newPassword);

    String updateForgetRetailerUser(@Param("email") String email, @Param("newPassword") String newPassword);

    int getUserIdByMailDistributor(@Param("email") String email);

    int getUserIdByMailRetailer(@Param("email") String email);
}
