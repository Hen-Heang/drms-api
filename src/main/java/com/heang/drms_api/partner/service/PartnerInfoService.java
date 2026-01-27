package com.heang.drms_api.partner.service;


import com.heang.drms_api.partner.dto.PartnerRequest;

public interface PartnerInfoService {

    Object createPartnerInfo(Integer userId, PartnerRequest partnerRequest);
}
