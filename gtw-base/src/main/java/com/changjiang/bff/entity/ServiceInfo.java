package com.changjiang.bff.entity;

public class ServiceInfo extends ServiceAttachedInfo{
    private Class interfaceName;
    private String methodName;
    private Class[] argsType;
    private Class[] parameterizedTypes;
    private boolean directCall;
    private boolean dataMask;

    public ServiceInfo() {
    }

    public Class getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(Class interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getArgsType() {
        return argsType;
    }

    public void setArgsType(Class[] argsType) {
        this.argsType = argsType;
    }

    public Class[] getParameterizedTypes() {
        return parameterizedTypes;
    }

    public void setParameterizedTypes(Class[] parameterizedTypes) {
        this.parameterizedTypes = parameterizedTypes;
    }

    public boolean isDirectCall() {
        return directCall;
    }

    public void setDirectCall(boolean directCall) {
        this.directCall = directCall;
    }

    public boolean isDataMask() {
        return dataMask;
    }

    public void setDataMask(boolean dataMask) {
        this.dataMask = dataMask;
    }
}
