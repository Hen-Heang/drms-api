package com.heang.drms_api.partner.service.partnerInfo;

import com.heang.drms_api.exception.BadRequestException;
import com.heang.drms_api.partner.dto.partnerInfo.PartnerRequest;
import com.heang.drms_api.partner.mapper.PartnerInfoMapper;
import com.heang.drms_api.partner.model.Partner;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
@RequiredArgsConstructor
public class PartnerInfoServiceImpl implements PartnerInfoService {

    private final PartnerInfoMapper partnerInfoMapper;

 
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Boolean checkUserProfileIfCreated(Integer currentUserId) {
        return partnerInfoMapper.checkIfUserProfileIsCreated(currentUserId);
    }

    @Override
    public Partner getUserProfile(Integer currentUserId) throws NotFoundException, ParseException {
        Partner userProfile = partnerInfoMapper.getUserProfile(currentUserId);
        if (userProfile == null) {
            throw new NotFoundException("Partner profile not found");
        }
        userProfile.setCreatedDate(formatter.format(formatter.parse(userProfile.getCreatedDate())));
        userProfile.setUpdatedDate(formatter.format(formatter.parse(userProfile.getUpdatedDate())));
        return userProfile;
    }

    @Override
    public Partner addUserProfile(Integer currentUserId, PartnerRequest partnerRequest) throws ParseException {
        // check if user profile is already created
        if (checkUserProfileIfCreated(currentUserId)) {
            throw new BadRequestException("User profile is already created.");
        }

        // prevent blank
        if (partnerRequest.getFirstName().isEmpty() || partnerRequest.getFirstName().isBlank()
                || partnerRequest.getLastName().isEmpty() || partnerRequest.getLastName().isBlank()
                || partnerRequest.getGender().isEmpty() || partnerRequest.getGender().isBlank()) {
            throw new BadRequestException("First name, Last name, or Gender can not be empty.");
        }

        if (!(partnerRequest.getGender().equalsIgnoreCase("male")
                || partnerRequest.getGender().equalsIgnoreCase("female")
                || partnerRequest.getGender().equalsIgnoreCase("other"))) {
            throw new BadRequestException("Please input valid gender. Available gender are 'male', 'female', or 'other'.");
        }

        // insert partner profile and return partner info id
        Partner partner = partnerInfoMapper.insertPartnerInfo(currentUserId, partnerRequest);
        if (partner == null) {
            throw new BadRequestException("Fail to create profile");
        }
        partner.setCreatedDate(formatter.format(formatter.parse(partner.getCreatedDate())));
        partner.setUpdatedDate(formatter.format(formatter.parse(partner.getUpdatedDate())));
        return partner;
    }


    @Override
    public Partner updateUserProfile(Integer currentUserId, PartnerRequest partnerRequest) throws ParseException {
        if (!checkUserProfileIfCreated(currentUserId)) {
            throw new BadRequestException("User profile is not created yet.");
        }

        Partner partner = partnerInfoMapper.updateUserProfile(currentUserId, partnerRequest);
        if (partner == null) {
            throw new BadRequestException("Fail to update profile");
        }

        partner.setCreatedDate(formatter.format(formatter.parse(partner.getCreatedDate())));
        partner.setUpdatedDate(formatter.format(formatter.parse(partner.getUpdatedDate())));
        return partner;
    }
}
