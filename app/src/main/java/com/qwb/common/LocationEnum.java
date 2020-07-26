package com.qwb.common;

/**
 * 地址位置：1.查看 2：标注; 3.导航
 */
public enum LocationEnum {
    LOOK("1"),
    MARK("2"),
    NAV("3");

    private final String type;

    LocationEnum(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static LocationEnum getByType(String type) {
        for (LocationEnum typeEnum : LocationEnum.values()) {
            if (typeEnum.getType().equals(type)) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
