package com.bazl.alims.service.impl;

import com.bazl.alims.dao.SerialNumberMapper;
import com.bazl.alims.model.po.SerialNumber;
import com.bazl.alims.service.SeqNoGenerateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by Administrator on 2017/1/7.
 */
@Service
public class SeqNoGenerateServiceImpl extends BaseService implements SeqNoGenerateService {

    @Autowired
    SerialNumberMapper serialNumberMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String getNextNoVal(String year, String code, String text, String orgId) {
        SerialNumber serialNumber = serialNumberMapper.selectNextVal(code, orgId);
        String nextVal = "";
        try {
            if (serialNumber != null && StringUtils.isNotBlank(serialNumber.getTypeValue())) {
                int val = Integer.parseInt(serialNumber.getTypeValue());
                val = val + 1;
                nextVal = getFullCharString(String.valueOf(val), 4);
                serialNumber.setTypeValue(nextVal);
                serialNumberMapper.update(serialNumber);
            } else {
                int val = 1;
                nextVal = getFullCharString(String.valueOf(val), 4);
                serialNumber = new SerialNumber();
                serialNumber.setId(UUID.randomUUID().toString());
                serialNumber.setTypeCode(code);
                serialNumber.setYear(year);
                serialNumber.setTypeValue(nextVal);
                serialNumber.setOrgId(orgId);
                serialNumberMapper.insert(serialNumber);
            }
        } catch (Exception ex) {
            logger.info("获取流水号报错：" + ex);
            throw ex;
        }
        return year + text + nextVal;
    }

    @Override
    public String getNextVal(String year, String typeCode, String code, String orgId) {
        SerialNumber serialNumber = new SerialNumber();
        String nextVal = "";
        try {
            serialNumber = serialNumberMapper.selectNextVal(typeCode, orgId);

            if(StringUtils.isNotBlank(serialNumber.getYear()) && serialNumber.getYear() != year){
                serialNumberMapper.updateYear(year,typeCode,orgId);
            }

            int val = Integer.parseInt(serialNumber.getTypeValue());
            val = val + 1;
            nextVal = getFullCharString(String.valueOf(val), serialNumber.getDigit() == 0 ? 4 : serialNumber.getDigit());
        } catch (Exception ex) {
            logger.info("获取流水号出错：" + ex);
            return "";
        }
        String yearVal = serialNumber.getYear() == null ? "" : serialNumber.getYear();
        String prefixVal = serialNumber.getPrefix() == null ? "" : serialNumber.getPrefix();
        return yearVal + prefixVal + nextVal;

    }

    @Override
    public int updateTypeVal(String year, String typeCode, String code, String orgId) {
        SerialNumber serialNumber = new SerialNumber();
        String nextVal = "";
        try {
            serialNumber = serialNumberMapper.selectNextVal(typeCode, orgId);
            int val = Integer.parseInt(serialNumber.getTypeValue());
            val = val + 1;

            nextVal = getFullCharString(String.valueOf(val), serialNumber.getDigit() == 0 ? 4 : serialNumber.getDigit());
            serialNumber.setTypeValue(nextVal);
            serialNumberMapper.update(serialNumber);

        } catch (Exception ex) {
            logger.info("修改流水号出错：" + ex);
            return 0;
        }

        return 1;
    }

    private String getFullCharString(String src, int len) {
        int srcLen = src.length();
        if (srcLen >= len) {
            return src;
        }

        int needlen = len - srcLen;
        for (int i = 0; i < needlen; i++) {
            src = "0" + src;
        }

        return src;
    }
}
