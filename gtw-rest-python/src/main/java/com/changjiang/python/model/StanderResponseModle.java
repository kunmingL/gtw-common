package com.changjiang.python.model;

import com.alibaba.fastjson2.JSONObject;

/**
 * 标准响应模型
 *
 * @Author changjiang
 * @Date 2022/12/12
 */
public class StanderResponseModle {
    private String code;
    private String msg;
    private JSONObject data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
