package com.heang.drms_api.merchant.model;

import com.heang.drms_api.partner.model.Category;

import java.util.List;

public class StoreMerchant {
    private Integer id;
    private String name;
    private String bannerImage;
    private String address;
    private String primaryPhone;
    private List<String> additionalPhone;
    private List<Category> categories;
    private Integer partnerAccountId;
    private String description;
    private String createdDate;
    private String updatedDate;
    private Boolean isPublish;
    private Boolean isActive;
    private Double rating;
    private Integer ratingCount;
    private Boolean isBookmarked;
}
