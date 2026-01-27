package com.heang.drms_api.partner.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor


public class Partner {
    private Integer id;
    private Integer partnerAccountId;
    private String firstName;
    private String lastName;
    private String gender;
    private String profileImage;
    private String createdDate;
    private String updatedDate;


}
