package com.heang.drms_api.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AppUserRequest {

    private String email;
    private String password;
    private Integer roleId;


}
