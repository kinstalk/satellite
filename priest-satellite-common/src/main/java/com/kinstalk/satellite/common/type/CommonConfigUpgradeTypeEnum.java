package com.kinstalk.satellite.common.type;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 表情包类别枚举类
 * User: erin
 * Date: 14-11-7
 * Time: 下午7:37
 */
public enum CommonConfigUpgradeTypeEnum {

    FORCE(1, "强制升级"),
    GENERAL(2, "普通升级");

    //数字与字符串映射字典表
    public static Map<Integer, CommonConfigUpgradeTypeEnum> UPGRADE_TYPE_MAPPING_DICT = new LinkedHashMap<Integer, CommonConfigUpgradeTypeEnum>();

    static {
        UPGRADE_TYPE_MAPPING_DICT.put(FORCE.getCode(), FORCE);
        UPGRADE_TYPE_MAPPING_DICT.put(GENERAL.getCode(), GENERAL);
    }

    public static String getString(Integer code) {
        return UPGRADE_TYPE_MAPPING_DICT.get(code).getValue();
    }

    public static boolean checkKeyIsExist(Integer code) {
        if (UPGRADE_TYPE_MAPPING_DICT.containsKey(code)) {
            return true;
        }
        return false;
    }

    private Integer code;

    private String value;

    private CommonConfigUpgradeTypeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
