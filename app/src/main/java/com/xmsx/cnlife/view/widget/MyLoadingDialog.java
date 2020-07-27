package com.xmsx.cnlife.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.chiyong.t3.R;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * 自定义网络加载对话框
 * 备注：com.wang.avi.AVLoadingIndicatorView加载动画，网址：https://github.com/81813780/AVLoadingIndicatorView
 */
public class MyLoadingDialog extends Dialog {
    private Context mContext;
    private String mMessage;//提示语
    private String mIndicatror;//动画
    private boolean mCancelable = true;//按返回键是否关闭加载框

    public MyLoadingDialog(@NonNull Context context) {
        super(context, R.style.LoadingDialog);
        mContext = context;
        mMessage = "加载中...";
        mIndicatror = "LineSpinFadeLoaderIndicator";
        mCancelable = true;
    }
    public MyLoadingDialog(@NonNull Context context, String message) {
        super(context, R.style.LoadingDialog);
        mContext = context;
        mMessage = message;
        mIndicatror = "LineSpinFadeLoaderIndicator";
        mCancelable = true;
    }
    public MyLoadingDialog(@NonNull Context context, String message, String indicatror) {
        super(context, R.style.LoadingDialog);
        mContext = context;
        mMessage = message;
        mIndicatror = indicatror;
        mCancelable = true;
    }
    public MyLoadingDialog(@NonNull Context context, String message, String indicatror, boolean cancelable) {
        super(context, R.style.LoadingDialog);
        mContext = context;
        mMessage = message;
        mIndicatror = indicatror;
        mCancelable = cancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.x_dialog_loading);
        try {
            // 设置窗口大小
            if(null != mContext){
                WindowManager windowManager = getWindow().getWindowManager();
                int screenWidth = windowManager.getDefaultDisplay().getWidth();
                WindowManager.LayoutParams attributes = getWindow().getAttributes();
                attributes.alpha = 0.5f;
                attributes.width = screenWidth/3;
                attributes.height = screenWidth/3;
                getWindow().setAttributes(attributes);
                setCanceledOnTouchOutside(false);
            }

            TextView tv_loading = findViewById(R.id.tv_loading);
            AVLoadingIndicatorView iv_loading = findViewById(R.id.iv_loading);
            iv_loading.setIndicator(mIndicatror);
            tv_loading.setText(mMessage);
        }catch (Exception e){
            XLog.e("MyLoadingDialog");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //按返回键是否关闭加载框
            if(mCancelable){
                dismiss();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
