package com.heang.drms_api.partner.service.store;

import com.heang.drms_api.partner.model.store.Store;
import com.heang.drms_api.partner.dto.store.StoreRequest;
import org.apache.ibatis.javassist.NotFoundException;
import java.text.ParseException;

public interface PartnerStoreService {

    Store createNewStore(StoreRequest storeRequest, Integer currentUserId) throws ParseException;

    Store getUserStore(Integer currentUserId) throws ParseException, NotFoundException;

    Store editAllFieldUserStore(Integer currentUserId, StoreRequest storeRequest) throws ParseException;

    String deleteUserStore(Integer currentUserId);

    String disableStore(Integer currentUserId);

    String enableStore(Integer currentUserId);

}
