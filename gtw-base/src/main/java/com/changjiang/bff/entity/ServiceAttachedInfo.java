package com.changjiang.bff.entity;

import java.util.HashMap;
import java.util.Map;

public class ServiceAttachedInfo {
    private String url;
    private String protocol;
    private String address;
    private String referField;
    private Map<String, Class> specClassReferMap = new HashMap<>();
    private boolean pageInitMethod;
    private boolean ftoMethod;
    private int timeoutMills;

    public ServiceAttachedInfo() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReferField() {
        return referField;
    }

    public void setReferField(String referField) {
        this.referField = referField;
    }

    public Map<String, Class> getSpecClassReferMap() {
        return specClassReferMap;
    }

    public void setSpecClassReferMap(Map<String, Class> specClassReferMap) {
        this.specClassReferMap = specClassReferMap;
    }

    public boolean isPageInitMethod() {
        return pageInitMethod;
    }

    public void setPageInitMethod(boolean pageInitMethod) {
        this.pageInitMethod = pageInitMethod;
    }

    public boolean isFtoMethod() {
        return ftoMethod;
    }

    public void setFtoMethod(boolean ftoMethod) {
        this.ftoMethod = ftoMethod;
    }

    public int getTimeoutMills() {
        return timeoutMills;
    }

    public void setTimeoutMills(int timeoutMills) {
        this.timeoutMills = timeoutMills;
    }
}
