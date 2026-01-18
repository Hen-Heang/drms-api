package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.model.AppUser;

public interface OtpMapper {

    AppUser getPartnerByEmail(String email);
    AppUser getMerchantByEmail(String email);
}
