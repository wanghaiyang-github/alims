package com.bazl.alims.model.po;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/1/27.
 */
public class XckyAddressInfo implements Serializable {

    private String id;
    private String orgId;
    private String xckyAddress;
    private String xckySysName;
    private String defaultWhenNull;
    private String invokerIpaddr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getXckyAddress() {
        return xckyAddress;
    }

    public void setXckyAddress(String xckyAddress) {
        this.xckyAddress = xckyAddress;
    }

    public String getXckySysName() {
        return xckySysName;
    }

    public void setXckySysName(String xckySysName) {
        this.xckySysName = xckySysName;
    }

    public String getDefaultWhenNull() {
        return defaultWhenNull;
    }

    public void setDefaultWhenNull(String defaultWhenNull) {
        this.defaultWhenNull = defaultWhenNull;
    }

    public String getInvokerIpaddr() {
        return invokerIpaddr;
    }

    public void setInvokerIpaddr(String invokerIpaddr) {
        this.invokerIpaddr = invokerIpaddr;
    }
}
