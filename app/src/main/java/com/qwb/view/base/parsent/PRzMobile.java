package com.qwb.view.base.parsent;

import android.app.Activity;
import android.content.Context;

import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.ui.RzMobileActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.ui.XTabActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 认证手机号
 */
public class PRzMobile extends XPresent<RzMobileActivity>{
    private  Context context;

    /**
     * 验证手机号
     */
    public void rzMobile(Activity activity,String code) {
        this.context = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("code", code);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.rzMobile)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    /**
     * 获取验证码
     */
    public void getCode(Activity context) {
        Map<String, String> params = new HashMap<>();
        params.put("token",SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.getCodeRzMobile)
                .id(2)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("state")) {
                //跳到首页
                ActivityManager.getInstance().jumpMainActivity((Activity) context, XTabActivity.class);
                SPUtils.setBoolean(ConstantUtils.Sp.IS_LOGIN, true);
            }
            ToastUtils.showCustomToast(jsonObject.getString("msg"));
        } catch (Exception e) {
            ToastUtils.showCustomToast("验证错误:"+e);
        }
    }

    private void parseJson2(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("state")) {
                ToastUtils.showCustomToast("获取成功,注意查收短信");
            } else {
                ToastUtils.showCustomToast(jsonObject.getString("msg"));
            }
            getV().resetCode();
        } catch (Exception e) {
            ToastUtils.showCustomToast("获取验证码错误:"+e);
            getV().resetCode();
        }
    }

}
