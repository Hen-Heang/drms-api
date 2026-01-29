package com.heang.drms_api.partner.mapper;
import com.heang.drms_api.partner.dto.PartnerRequest;
import com.heang.drms_api.partner.model.Partner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PartnerInfoMapper {

    List<String> getAdditionalPhoneNumberByPartnerInfoId(@Param("id") Integer id);

    Partner getUserProfile(@Param("currentUserId") Integer currentUserId);

    Partner insertPartnerInfo(
            @Param("currentUserId") Integer currentUserId,
            @Param("p") PartnerRequest partnerRequest
    );

    Partner updateUserProfile(
            @Param("currentUserId") Integer currentUserId,
            @Param("p") PartnerRequest partnerRequest
    );

    void addAdditionalPhoneNumber(
            @Param("infoId") Integer infoId,
            @Param("additionalPhoneNumber") String additionalPhoneNumber
    );

    boolean checkIfUserProfileIsCreated(@Param("currentUserId") Integer currentUserId);

    void deleteAdditionalPhoneNumber(@Param("infoId") Integer infoId);

    Integer getPartnerInfoId(@Param("currentUserId") Integer currentUserId);

    boolean checkIfAdditionalPhoneNumberExist(@Param("additionalPhoneNumber") String additionalPhoneNumber);

    Integer getPartnerIdByStoreId(@Param("storeId") Integer storeId);
}