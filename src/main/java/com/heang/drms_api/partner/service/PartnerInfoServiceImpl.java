package com.heang.drms_api.partner.service;


import com.heang.drms_api.common.utils.DateTimeUtils;
import com.heang.drms_api.partner.dto.PartnerRequest;
import com.heang.drms_api.partner.mapper.PartnerInfoMapper;
import com.heang.drms_api.partner.model.Partner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PartnerInfoServiceImpl implements PartnerInfoService {

    private final PartnerInfoMapper partnerInfoMapper;

    @Override
    public Object createPartnerInfo(Integer userId, PartnerRequest partnerRequest) {

//        insert partner info into database
        Partner partner = partnerInfoMapper.insertPartnerInfo(userId,partnerRequest);
        partner.setCreatedDate(DateTimeUtils.format(LocalDateTime.parse(partner.getCreatedDate())));
        partner.setUpdatedDate(DateTimeUtils.format(LocalDateTime.parse(partner.getUpdatedDate())));
        return partner;
    }
}
