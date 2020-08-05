package com.bazl.alims.model.vo;

import com.bazl.alims.model.po.FugitivesInfo;
import com.bazl.alims.utils.JsonDatetimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wanghaiyang
 * @date 2020/6/15
 */
public class FugitivesInfoVo extends AbstractBaseVo<FugitivesInfo> implements Serializable {

    public FugitivesInfoVo() {
        super();
        this.entity = new FugitivesInfo();
    }

    public FugitivesInfoVo(FugitivesInfo entity) {
        super();
        this.entity = entity;
    }

    private String personTypeName;

    private String personGenderName;

    public String getPersonTypeName() {
        return personTypeName;
    }

    public void setPersonTypeName(String personTypeName) {
        this.personTypeName = personTypeName;
    }

    public String getPersonGenderName() {
        return personGenderName;
    }

    public void setPersonGenderName(String personGenderName) {
        this.personGenderName = personGenderName;
    }
}
