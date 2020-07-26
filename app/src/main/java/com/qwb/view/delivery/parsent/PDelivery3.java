package com.qwb.view.delivery.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.model.DeliveryListBean;
import com.qwb.view.delivery.ui.Delivery3Fragment;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：我的客户
 * 创建作者：yeshenggen
 * 创建时间：2018/05/14
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PDelivery3 extends XPresent<Delivery3Fragment>{

    public void loadData(Activity activity, String psState, int page, int pageSize, String sDate, String eDate, String search){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("psState", psState);
        params.put("page", String.valueOf(page));
        params.put("rows", String.valueOf(pageSize));
        if(!MyStringUtil.isEmpty(sDate)){
            params.put("sdate", String.valueOf(sDate + " 00:00"));
        }
        if(!MyStringUtil.isEmpty(eDate)){
            params.put("edate", String.valueOf(eDate + " 59:59"));
        }
        if(!MyStringUtil.isEmpty(search)){
            params.put("khNm", String.valueOf(search));
        }
        String url = Constans.queryDeliverBillPage;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
                            //关闭刷新
                            getV().closeRefresh();
                        }catch (Exception e1){
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    public void updateDeliveryState(Activity activity,String psState,Integer billId){
//  web/updateDeliveryState(psState=2配送中 psState=3已完成 psState=4配送完成;token;billId:配送单ID)
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("psState", psState);
        params.put("billId", String.valueOf(billId));
        String url = Constans.updateDeliveryState;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }


    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showCustomToast(e.getMessage());
            try {
                //关闭刷新
                getV().closeRefresh();
            }catch (Exception e1){
            }
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1:
                    parseJson1(response);
                    break;
                case 2:
                    parseJson2(response);
                    break;
            }
        }
    }

    //解析数据-订货
    private void parseJson1(String response) {
        try {
            DeliveryListBean parseObject = JSON.parseObject(response,DeliveryListBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<DeliveryBean> dataList = parseObject.getRows();
                    if(dataList!=null && dataList.size()>0){
                        //去掉最后一个：disAmt；1ddNum；driverName；合计相关的
                        dataList.remove(dataList.size()-1);
                    }
                    getV().refreshAdapterOrder(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean parseObject = JSON.parseObject(response,BaseBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    getV().updatePsState(true);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
