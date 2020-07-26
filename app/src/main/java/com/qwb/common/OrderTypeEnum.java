package com.qwb.common;

/**
 * 1：拜访下单（我的客户）
 * 2：订货下单-下单
 * 3：订货下单-列表
 * 4：退货下单-下单
 * 5：退货下单-列表
 * 7：车销单-添加
 * 8：车销单-列表
 * 9：商城
 * 10：历史订单
 */
public enum OrderTypeEnum {
    // 1：拜访下单(添加或修改) 2:订货下单(添加) 3：订货下单列表（查看或修改）4：退货下单(添加或修改) 5：退货下单列表（查看或修改）
    // 7：车销单（添加）8：车销单（修改，详情）9.商城详情 10.历史订单 11.红字单 12.红字单（列表）
    ORDER_CUSTOMER(1),
    ORDER_DHXD_ADD(2),
    ORDER_DHXD_LIST(3),
    ORDER_THXD_ADD(4),
    ORDER_THXD_LIST(5),
    ORDER_CAR_ADD(7),
    ORDER_CAR_LIST(8),
    ORDER_SC(9),
    ORDER_HISTORY(10),
    ORDER_RED_ADD(11),
    ORDER_RED_LIST(12),
    ;

    public static final int  O_CAR_ADD = 7;
    public static final int  O_CAR_UPDATE = 8;
    public static final int  O_SC = 9;
    ;

    private final int type;

    OrderTypeEnum(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public static OrderTypeEnum getByType(int type) {
        for (OrderTypeEnum typeEnum : OrderTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
