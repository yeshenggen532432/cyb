package com.qwb.common;

/**
 * 盘点订单
 * 1.单人盘点-添加
 * 2.单人盘点-添加
 * 3.多人盘点-添加
 * 4.多人盘点-添加
 * 5.批次盘点-添加
 * 6.批次盘点-添加
 */
public enum StkOrderTypeEnum {
    ORDER_SINGLE_ADD(1),
    ORDER_SINGLE_UPDATE(2),
    ORDER_MULTIPLE_ADD(3),
    ORDER_MULTIPLE_UPDATE(4),
    ORDER_BATCH_ADD(5),
    ORDER_BATCH_UPDATE(6),
    ;
    ;

    private final int type;

    StkOrderTypeEnum(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public static StkOrderTypeEnum getByType(int type) {
        for (StkOrderTypeEnum typeEnum : StkOrderTypeEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
