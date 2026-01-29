package com.heang.drms_api.partner.model.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRating {
    private Integer id;
    private Integer storeId;
    private Integer retailerId;
    private Integer ratedStar;
    //    private String comment;
    private String createdDate;
    private String updatedDate;
}

