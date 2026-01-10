package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.model.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserMapper {
//    Email mapper service
    AuthUser findByEmail(@Param("email")String email);

    int insertUser(AuthUser authUser);



}
