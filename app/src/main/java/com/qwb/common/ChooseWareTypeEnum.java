package com.qwb.common;

/**
 * 选择商品
 * 4：销售小结
 * 5：下单
 * 7：车销下单
 */
public enum ChooseWareTypeEnum {
    W_XSXJ("4"),
    W_ORDER("5"),
    W_CAR("7");

    private final String type;

    ChooseWareTypeEnum(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static ChooseWareTypeEnum getByType(String type) {
        for (ChooseWareTypeEnum typeEnum : ChooseWareTypeEnum.values()) {
            if (type.equals(typeEnum.getType())) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
