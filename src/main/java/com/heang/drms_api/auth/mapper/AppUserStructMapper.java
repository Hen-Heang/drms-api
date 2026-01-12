package com.heang.drms_api.auth.mapper;

import com.heang.drms_api.auth.dto.AppUserDto;
import com.heang.drms_api.auth.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface AppUserStructMapper {

    // If field names match, MapStruct maps automatically.
    // Keep this if you have different names (example below):
    // @Mapping(source="roleId", target="roleId")

    AppUserDto toDto(AppUser user);


}
