package com.bazl.alims.model.po;


import com.bazl.alims.utils.JsonDatetimeSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * QUEUE_SAMPLE
 * 队列表
 *  Created by hj on 2019/05/06
 */
public class QueueSample {

    /**
     * 主键id
     */
    private String id;

    /**
     * 委托id
     */
    private String foreignId;

    /**
     * 上报状态
     */
    private BigDecimal status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date createDatetime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+08")
    @JsonSerialize(using = JsonDatetimeSerializer.class)
    private Date updateDatetime;

    /**
     * 队列类型
     */
    private String queueType;

    /**
     * xml路劲
     */
    private String filename;

    private String serverNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getForeignId() {
        return foreignId;
    }

    public void setForeignId(String foreignId) {
        this.foreignId = foreignId;
    }

    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(Date updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getServerNo() {
        return serverNo;
    }

    public void setServerNo(String serverNo) {
        this.serverNo = serverNo;
    }

}



