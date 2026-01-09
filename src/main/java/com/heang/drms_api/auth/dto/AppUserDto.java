package com.heang.drms_api.auth.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AppUserDto {
    private Integer id;
    private String email;
    private Integer roleId;
    private Boolean isVerified;
    private Boolean isActive;

}
