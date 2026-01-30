package com.heang.drms_api.partner.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Category {

    private Integer id;
    private String name;
    private String categoryId;
    private String createdDate;
    private String updatedDate;

}
