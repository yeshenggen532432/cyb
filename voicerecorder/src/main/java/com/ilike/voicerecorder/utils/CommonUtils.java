package com.ilike.voicerecorder.utils;

import android.content.Context;

/**
 * 公共方法类
 */
public class CommonUtils {

    /**
     * sd卡是否存在
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 根据时间长短计算语音条宽度:220dp
     */
    public synchronized static int getVoiceLineWight2(Context context, int seconds) {
        //1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        if (seconds <= 2) {
            return dip2px(context, 60);
        } else if (seconds > 2 && seconds <= 10) {
            //90~170
            return dip2px(context, 60 + 8 * seconds);
        } else {
            //170~220
            return dip2px(context, 140 + 10 * (seconds / 10));
        }
    }

    //根据时间长短计算语音条宽度:220dp
    public synchronized static int getVoiceLineWight(Context context, int seconds) {
        //1-2s是最短的。2-10s每秒增加一个单位。10-60s每10s增加一个单位。
        if (seconds <= 2) {
            return dip2px(context, 90);
        } else if (seconds <= 10) {
            //90~170
            return dip2px(context, 90 + 8 * seconds);
        } else {
            //170~220
            return dip2px(context, 170 + 10 * (seconds / 10));

        }
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @return px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @return dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }









}
