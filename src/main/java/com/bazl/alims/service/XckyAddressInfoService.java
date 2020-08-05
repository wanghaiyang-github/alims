package com.bazl.alims.service;

import com.bazl.alims.model.po.XckyAddressInfo;

/**
 * Created by Administrator on 2019/1/27.
 */
public interface XckyAddressInfoService {

    XckyAddressInfo selectByOrgId(String orgId);

    XckyAddressInfo selectDefault();

}
