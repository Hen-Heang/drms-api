package com.heang.drms_api.auth.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor

public class Otp {

    private Integer id;
    private Integer accountId;
    private Integer otpCode;
    private String email;
    private Date createdDate;


}
