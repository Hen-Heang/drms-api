package com.heang.drms_api.mapper;

import com.heang.drms_api.model.AuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthUserMapper {
//    Email mapper service
    AuthUser findByEmail(@Param("email")String email);

    int insertUser(AuthUser authUser);


}
