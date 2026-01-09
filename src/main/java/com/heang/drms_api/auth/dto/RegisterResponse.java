package com.heang.drms_api.auth.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class RegisterResponse {
    private Long id;
    private String email;
    private String fullName;
    private String role;


}
