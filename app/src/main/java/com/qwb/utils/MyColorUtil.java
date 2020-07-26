package com.qwb.utils;

import android.support.v4.content.ContextCompat;

import com.qwb.application.MyApp;

/**
 * Created by yeshenggen on 2019/5/14.
 */

public class MyColorUtil {

    /**
        获取颜色id
     */
    public static int getColorResId(int colorResId){
        int id = 0;
        try{
           id =  ContextCompat.getColor(MyApp.getI(), colorResId);
        }catch (Exception e){
        }finally {
            return id;
        }
    }

}
