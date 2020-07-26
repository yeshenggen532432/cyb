package com.qwb.common;


/**
 * 年月日：yyyy-MM-dd
 * 年月日 时分：yyyy-MM-dd HH:mm
 */
public enum DateFormatEnum {
    Y_M_D("yyyy-MM-dd"),
    Y_M_D_H_M("yyyy-MM-dd HH:mm");

    private final String type;

    DateFormatEnum(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

}
