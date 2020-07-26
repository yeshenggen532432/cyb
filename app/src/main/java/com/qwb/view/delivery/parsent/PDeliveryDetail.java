package com.qwb.view.delivery.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.delivery.model.DeliveryDetailBean;
import com.qwb.view.delivery.ui.DeliveryDetailActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：物流配送中心
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PDeliveryDetail extends XPresent<DeliveryDetailActivity>{

    public void queryDataDetail(Activity activity, String billId){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        String url = Constans.queryDeliveryDetail;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(3)
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


    //解析数据
    private void parseJson1(String response) {
        try {
            DeliveryDetailBean parseObject = JSON.parseObject(response, DeliveryDetailBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    getV().doUI(parseObject.getDelivery());
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
