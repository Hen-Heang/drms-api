package com.heang.drms_api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Accessors

public class AuthUser {
    private Long id;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private String role;
    private String status;
    private LocalDateTime createdDate;

}
