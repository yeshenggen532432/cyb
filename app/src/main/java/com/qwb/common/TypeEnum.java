package com.qwb.common;

/**
 * 添加：1
 * 修改：2
 * 详情：3
 * 删除：4
 */
public enum TypeEnum {
    ADD("1"),
    UPDATE("2"),
    DETAIL("3"),
    REMOVE("4");

    private final String type;

    TypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static TypeEnum getByType(String type) {
        for (TypeEnum typeEnum : TypeEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
