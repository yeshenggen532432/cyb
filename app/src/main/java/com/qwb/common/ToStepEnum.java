package com.qwb.common;

/**
 * 跳转到拜访六步骤
 * 1：我的客户
 * 2：周边客户
 * 3：计划拜访
 */
public enum ToStepEnum {
    STEP_MINE_CUSTOMER(1),
    STEP_NEAR_CUSTOMER(2),
    STEP_JFBF(3);

    private final int type;

    ToStepEnum(int type){
        this.type = type;
    }

    public int getType(){
        return type;
    }

    public static ToStepEnum getByType(int type) {
        for (ToStepEnum typeEnum : ToStepEnum.values()) {
            if (typeEnum.getType() == type) {
                return typeEnum;
            }
        }
        throw new IllegalArgumentException("unknown type:" + type);
    }


}
