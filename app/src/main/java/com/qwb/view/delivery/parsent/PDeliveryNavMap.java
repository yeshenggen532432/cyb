package com.qwb.view.delivery.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.model.DeliveryListBean;
import com.qwb.view.delivery.ui.DeliveryNavMapFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 物流配送中心:配送单导航
 */
public class PDeliveryNavMap extends XPresent<DeliveryNavMapFragment>{

    public void queryData(Activity activity, String psStates){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("psStates", psStates);
        String url = Constans.queryDeliverListByNavMap;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
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

    //解析数据
    private void parseJson1(String response) {
        try {
            DeliveryListBean parseObject = JSON.parseObject(response,DeliveryListBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<DeliveryBean> dataList = parseObject.getRows();
                    getV().doUI(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
}
