package com.heang.drms_api.partner.dto.partnerInfo;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PartnerRequest {

    private String firstName;
    private String lastName;
    private String gender;
    private String profileImage;

}
