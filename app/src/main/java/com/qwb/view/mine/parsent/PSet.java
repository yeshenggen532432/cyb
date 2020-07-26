package com.qwb.view.mine.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.mine.ui.SetActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.mine.model.AddressUploadBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 设置
 */
public class PSet extends XPresent<SetActivity>{

    private Activity activity;

    /**
     * 从后台设置轨迹采集周期和打包周期
     */
    public void queryAddressUpload() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryAddressUplaodWeb)
                .id(3)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        resultData(response, 3);
                    }
                });
    }
    /**
     * 从后台设置轨迹采集周期和打包周期
     */
    public void updateAddressUpload(Activity activity, String memUpload) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("memUpload", memUpload);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateAddressUpload)
                .id(4)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        resultData(response, 4);
                    }
                });
    }


    private void resultData(String response, int id) {
        if (!MyUtils.isEmptyString(response) && response.startsWith("{")) {
            switch (id) {
                // 设置后台轨迹采集周期和打包周期
                case 3:
                    try {
                        AddressUploadBean data = JSON.parseObject(response,AddressUploadBean.class);
                        if (data != null) {
                            if (data.isState()) {
                                getV().doAddressUpload(data);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                //上传与不上传的切换
                case 4:
                    try {
                        BaseBean data = JSON.parseObject(response, BaseBean.class);
                        if (data != null) {
                            ToastUtils.showCustomToast(data.getMsg());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }





}
