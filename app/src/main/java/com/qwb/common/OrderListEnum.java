package com.qwb.common;

/**
 * 1：订货下单， 退货下单
 * 2：历史订单
 */
public enum OrderListEnum {
    ORDER(1),
    HISTORY_ORDER(2);

    private final int type;

    OrderListEnum(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public static OrderListEnum getByType(int type) {
        for (OrderListEnum typeEnum : OrderListEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
