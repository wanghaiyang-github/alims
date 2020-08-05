package com.bazl.alims.dao;


import com.bazl.alims.model.DictItem;
import com.bazl.alims.model.po.XckyAddressInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XckyAddressInfoMapper {

    XckyAddressInfo selectByOrgId(String orgId);

    XckyAddressInfo selectDefault();

}