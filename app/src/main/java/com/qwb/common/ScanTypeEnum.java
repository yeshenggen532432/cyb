package com.qwb.common;

/**
 * 1:关注商城
 * 2.扫描登录
 */
public enum ScanTypeEnum {
    FOLLOW("1"),
    LOGIN("10001");

    private final String type;

    ScanTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static ScanTypeEnum getByType(String type) {
        for (ScanTypeEnum typeEnum : ScanTypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
