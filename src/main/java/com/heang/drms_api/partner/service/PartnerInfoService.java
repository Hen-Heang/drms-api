package com.heang.drms_api.partner.service;


import com.heang.drms_api.partner.dto.PartnerRequest;
import com.heang.drms_api.partner.model.Partner;
import org.apache.ibatis.javassist.NotFoundException;
import java.text.ParseException;

public interface PartnerInfoService {

    Partner addUserProfile(Integer currentUserId, PartnerRequest partnerRequest) throws ParseException;

    Object getUserProfile(Integer currentUserId) throws NotFoundException, ParseException;

    Partner updateUserProfile(Integer currentUserId, PartnerRequest distributorRequest) throws ParseException;
}
