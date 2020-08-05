package com.bazl.alims.model;

import java.util.Date;
import java.util.List;

/**
 * 权限表
 * LOA_PERMISSION
 * Created by Administrator on 2018/12/21.
 */
public class LoaPermission {
    /**
     * 主键ID
     */
    private String permissionId;
    /**
     * 菜单名称
     */
    private String permissionName;
    /**
     * URL地址
     */
    private String permissionLink;
    /**
     * 是否为顶级节点标识
     */
    private String rootFlag;
    /**
     * 父菜单ID
     */
    private String parentId;
    /**
     * 是否有效标识
     */
    private String activeFlag;
    /**
     * 创建时间
     */
    private Date createDatetime;
    /**
     * 创建人
     */
    private String createPerson;

    private List<LoaPermission> permissionList;

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName == null ? null : permissionName.trim();
    }

    public String getPermissionLink() {
        return permissionLink;
    }

    public void setPermissionLink(String permissionLink) {
        this.permissionLink = permissionLink == null ? null : permissionLink.trim();
    }

    public String getRootFlag() {
        return rootFlag;
    }

    public void setRootFlag(String rootFlag) {
        this.rootFlag = rootFlag == null ? null : rootFlag.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag == null ? null : activeFlag.trim();
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson == null ? null : createPerson.trim();
    }

    public List<LoaPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<LoaPermission> permissionList) {
        this.permissionList = permissionList;
    }
}