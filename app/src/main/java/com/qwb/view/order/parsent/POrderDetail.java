package com.qwb.view.order.parsent;


import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.qwb.view.order.ui.OrderDetailActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.model.QueryBforderBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：订单详情
 */
public class POrderDetail extends XPresent<OrderDetailActivity> {

    /**
     * 获取订单信息
     */
    public void loadDataDhOrderInfo(Context context, int orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(orderId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryDhorderWeb)
                .id(1)
                .build()
                .execute(new MyStringCallback(),context);
    }

    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
//            ToastUtils.showCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://获取拜访-订单信息
                    parseJson1(response);
                    break;
            }
        }
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            QueryBforderBean parseObject = JSON.parseObject(response,QueryBforderBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        getV().resultOrderInfo(parseObject);//显示商品信息
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }



}
