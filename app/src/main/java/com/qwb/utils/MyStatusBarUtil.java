package com.qwb.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.xmsx.qiweibao.R;

/**
 * Created by yeshenggen on 2019/5/11.
 */

public class MyStatusBarUtil {

    private static MyStatusBarUtil MANAGER = null;
    public static MyStatusBarUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyStatusBarUtil();
        }
        return MANAGER;
    }

    /**
     * 状态栏白色，透明度100
     */
    public void setColorWhite(Activity context){
        StatusBarUtil.setColor(context, context.getResources().getColor(R.color.white), ConstantUtils.STATUSBAR_ALPHA);
    }

    /**
     * 状态栏蓝色，透明度100
     */
    public void setColorBlue(Activity context){
//        StatusBarUtil.setColor(context, context.getResources().getColor(R.color.blue), ConstantUtils.STATUSBAR_ALPHA);
        StatusBarUtil.setColor(context, context.getResources().getColor(R.color.green), 0);
    }

    /**
     * 状态栏蓝色，透明度100
     */
    public void setColorGreen(Activity context){
        StatusBarUtil.setColor(context, context.getResources().getColor(R.color.green),0);
    }

    /**
     * 状态栏为图片
     */
    public void setTranslucentForImageView(Activity context,View viewNeedOffset){
        //mViewNeedOffset:需要向下移动的view
//        StatusBarUtil.setTranslucentForImageView(context, ConstantUtils.STATUSBAR_ALPHA, viewNeedOffset);
        StatusBarUtil.setTranslucentForImageView(context, 0, viewNeedOffset);
    }

    /**
     * 全屏，透明度100
     */
    public void setStatusBarTranslucent(Context context){
        StatusBarUtil.setTranslucent((Activity) context,ConstantUtils.STATUSBAR_ALPHA);
    }


}
