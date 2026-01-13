package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.dto.AppUserRequest;
import com.heang.drms_api.auth.model.AppUser;
import com.heang.drms_api.auth.model.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserMapper {

    AppUser insertPartnerUser(@Param("user")AppUserRequest appUserRequest);
    AppUser insertMerchantUser(@Param("user")AppUserRequest user);

    AppUser findPartnerByEmail(@Param("email") String email);

    AppUser findMerchantByEmail(@Param("email") String email);

    Boolean partnerEmailExists(@Param("email") String email);
    Boolean merchantEmailExists(@Param("email") String email);

    Integer getPartnerRoleIdByEmail(@Param("email") String email);
    Integer getMerchantRoleIdByEmail(@Param("email") String email);

    Integer getUserIdByPartnerEmail(@Param("email") String email);
    Integer getUserIdByMerchantEmail(@Param("email") String email);





}
