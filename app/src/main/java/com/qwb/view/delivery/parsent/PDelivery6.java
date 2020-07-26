package com.qwb.view.delivery.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.model.DeliveryListBean;
import com.qwb.view.delivery.ui.Delivery6Fragment;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 *
 */
public class PDelivery6 extends XPresent<Delivery6Fragment>{

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
}
