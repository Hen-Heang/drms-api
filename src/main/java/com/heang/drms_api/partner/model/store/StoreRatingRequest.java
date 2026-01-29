package com.heang.drms_api.partner.model.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreRatingRequest {
    private Integer ratedStar;
//    private String comment;
}
