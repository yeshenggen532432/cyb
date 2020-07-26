package com.qwb.view.mine.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.mine.ui.UserUpdateActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 用户信息
 */
public class PUserUpdate extends XPresent<UserUpdateActivity>{

    private Activity activity;


    /**
     * 修改
     */
    public void updateUser(Activity activity, String key, String s) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put(key, s);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateinfoURL)
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


    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean.isState()) {
                getV().doUpdateSuccess();
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }



}
