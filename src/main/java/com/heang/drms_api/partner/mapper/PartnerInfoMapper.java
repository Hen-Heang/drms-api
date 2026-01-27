package com.heang.drms_api.partner.mapper;
import com.heang.drms_api.partner.dto.PartnerRequest;
import com.heang.drms_api.partner.model.Partner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PartnerInfoMapper {

    Partner insertPartnerInfo(Integer currentUserId ,@Param("") PartnerRequest partnerRequest);

}
