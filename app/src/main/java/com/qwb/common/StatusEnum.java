package com.qwb.common;

/**
 * 状态
 * -2.暂存
 * 1.审批
 * 2.作废
 */
public enum StatusEnum {
    zc("-2", "暂存"),
    Audit("1", "已审批"),
    zf("2", "暂存"),
    ;

    private final String type;

    StatusEnum(String type, String name){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static StatusEnum getByType(String type) {
        for (StatusEnum typeEnum : StatusEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
