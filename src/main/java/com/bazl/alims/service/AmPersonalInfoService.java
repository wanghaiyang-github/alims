package com.bazl.alims.service;

import com.bazl.alims.model.po.AmPersonalInfo;
import com.bazl.alims.model.vo.AmPersonalInfoVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */
@Repository
public interface AmPersonalInfoService {
    List<AmPersonalInfo> queryAmPersonalInfoLIst(String orgId);

    List<AmPersonalInfoVo> queryAmPersonalInfoVoList(String orgId);

    void addAmPersonalInfo(AmPersonalInfo amPersonalInfo);

    void deleteAmPersonalInfo(AmPersonalInfo amPersonalInfo);

    void updateAmPersonalInfo(AmPersonalInfo amPersonalInfo);

    AmPersonalInfo queryAmPersonalInfo(String personalId);

    void updateAmPersonalInfoById(AmPersonalInfo amPersonalInfo);
}
