package com.qwb.view.delivery.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DDeliveryCustomerBean;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.model.DeliveryListBean;
import com.qwb.view.delivery.ui.DeliveryNavListFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 物流配送中心:配送单导航
 */
public class PDeliveryNavList extends XPresent<DeliveryNavListFragment>{

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
                    List<DDeliveryCustomerBean> list = new ArrayList<>();
                    List<DeliveryBean> dataList = parseObject.getRows();
                    if(dataList != null && dataList.size() > 0){
                        for (DeliveryBean bean: dataList) {
                            DDeliveryCustomerBean dDeliveryCustomerBean = new DDeliveryCustomerBean();
                            dDeliveryCustomerBean.setPsState("" + bean.getPsState());
                            dDeliveryCustomerBean.setCid("" + bean.getCstId());
                            dDeliveryCustomerBean.setKhNm("" + bean.getKhNm());
                            dDeliveryCustomerBean.setAddress(bean.getAddress());
                            dDeliveryCustomerBean.setLongitude(bean.getLongitude());
                            dDeliveryCustomerBean.setLatitude(bean.getLatitude());
                            dDeliveryCustomerBean.setBillNo(bean.getBillNo());
                            dDeliveryCustomerBean.setTel(bean.getTel());
                            list.add(dDeliveryCustomerBean);
                        }
                    }
                    //查询缓存数据
                    List<DDeliveryCustomerBean> cacheList = MyDataUtils.getInstance().queryDeliveryCustomer();
                    if (cacheList != null && cacheList.size() > 0) {
                        list.addAll(cacheList);
                    }
                    getV().refreshAdapter(list);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
}

