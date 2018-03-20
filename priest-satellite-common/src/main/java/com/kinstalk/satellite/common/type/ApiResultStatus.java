package com.kinstalk.satellite.common.type;

/**
 * 接口是否调用成功
 * User: liuling
 * Date: 16/4/14
 * Time: 下午4:40
 */
public enum ApiResultStatus {

    OK(1),//成功
    NO(0);//失败

    public int value;

    ApiResultStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
