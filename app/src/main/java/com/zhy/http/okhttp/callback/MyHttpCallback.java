package com.zhy.http.okhttp.callback;

import android.app.Activity;
import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ToastUtils;
import com.xmsx.cnlife.view.widget.MyLoadingDialog;

import cn.droidlover.xdroidmvp.log.XLog;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 自己根据OkHttpUtil,再次封装回调函数
 */

public abstract class MyHttpCallback extends StringCallback {

    private MyLoadingDialog dialog;
    private Context context;

    public MyHttpCallback(Activity activity) {
        setActivity(activity);
        if (null != activity && activity instanceof Activity && !activity.isFinishing()) {
            dialog = new MyLoadingDialog(activity);
        }
    }

    public MyHttpCallback(Activity activity, String message) {
        setActivity(activity);
        if (null != activity && activity instanceof Activity && !activity.isFinishing()) {
            dialog = new MyLoadingDialog(activity, message);
        }
    }

    public MyHttpCallback(Activity activity, String message, String indicatror) {
        setActivity(activity);
        if (null != activity && activity instanceof Activity && !activity.isFinishing()) {
            dialog = new MyLoadingDialog(activity, message);
        }
    }

    public MyHttpCallback(Activity activity, String message, String indicatror, boolean mCancelable) {
        setActivity(activity);
        if (null != activity && activity instanceof Activity && !activity.isFinishing()) {
            dialog = new MyLoadingDialog(activity, message);
        }
    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            this.context = null;
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        XLog.e("onError：" + e.getMessage());
        ToastUtils.showCustomToast(e.getMessage());
        //前面自己的业务逻辑
        myOnError(call, e, id);
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            XLog.e("onResponse：" + response);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (MyStringUtil.isNotEmpty(response) && response.startsWith("{")) {
                JSONObject json = JSON.parseObject(response);
                String msg = json.getString("msg");
                if (MyStringUtil.isNotEmpty(msg)) {
                    if ("此账号在异地登陆".equals(msg) || "请先登录系统".equals(msg)) {
                        ActivityManager.getInstance().jumpToLoginActivity(context, 1);
                        return;
                    }
                }
            }

            //前面自己的业务逻辑wan
            myOnResponse(response, id);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //自己的
    public abstract void myOnError(Call call, Exception e, int id);

    //自己的
    public abstract void myOnResponse(String response, int id);

    public void setActivity(Context context) {
        if (context != null) {
            this.context = context;
        }
    }


}
