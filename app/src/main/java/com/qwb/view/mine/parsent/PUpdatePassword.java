package com.qwb.view.mine.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.mine.ui.UpdatePasswordActivity;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 设置-修改密码
 */
public class PUpdatePassword extends XPresent<UpdatePasswordActivity>{

    private Activity activity;

    public void getCode(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
        params.put("type", "3");
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.getCodeURL)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson(response, 1);
                    }
                });
    }

    public void addData(Activity activity, String newPassword, String oldPassword, String code, String sessionId){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("newpwd", MD5.hex_md5(newPassword));
        params.put("oldpwd", MD5.hex_md5(oldPassword));
        params.put("sendCode", code);

        if (!code.equals(Constans.SYSCODE) && MyStringUtil.isNotEmpty(sessionId)) {
            params.put("sessionId", sessionId);
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.changepwdURL)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson(response, 2);
                    }
                });
    }



    //1 修改密码0获取验证码
    public void parseJson(String json, int tag) {
        switch (tag) {
            case 1:
                try {
                    JSONObject jsonObject = JSON.parseObject(json);
                    if (jsonObject.getBoolean("state")) {
                        ToastUtils.showCustomToast("获取成功,注意查收短信");
                        String sessionId = jsonObject.getString("sessionId");
                        getV().doGetCodeResult(true, sessionId);
                    } else {
                        ToastUtils.showCustomToast(jsonObject.getString("msg"));
                        getV().doGetCodeResult(false, null);
                    }
                } catch (Exception e) {
                    getV().doGetCodeResult(false, null);
                    ToastUtils.showCustomToast("获取数据失败");
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = JSON.parseObject(json);
                    if (jsonObject.getBoolean("state")) {
                        ToastUtils.showCustomToast("修改成功！");
                       getV().doUpdatePasswordSuccess();
                    } else {
                        ToastUtils.showCustomToast(jsonObject.getString("msg"));
                    }
                } catch (Exception e) {
                    ToastUtils.showCustomToast("操作失败！");
                }
                break;

        }
    }





}
