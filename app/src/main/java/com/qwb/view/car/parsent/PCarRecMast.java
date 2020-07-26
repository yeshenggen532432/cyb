package com.qwb.view.car.parsent;


import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.model.CarRecMastBean;
import com.qwb.view.car.model.CarRecMastPageBean;
import com.qwb.view.car.ui.CarRecMastActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：车销收款
 */
public class PCarRecMast extends XPresent<CarRecMastActivity>{

    /**
     * 车销收款列表
     */
    public void queryData(Activity activity, int pageNo, int pageSize, String startDate, String endDate, int status) {
        String url=Constans.stkCarRecMast_pages;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        if(0 != status){
            params.put("status", String.valueOf(status));
        }
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
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

    /**
     * 作废收款
     */
    public void updateStatusZf(Activity activity, int billId) {
        String url=Constans.stkCarRecMast_cancelStatus;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils.post()
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

    /**
     * 确认收款
     */
    public void updateStatusQrsk(Activity activity, int billId) {
        String url=Constans.stkCarRecMast_confirmSk;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .id(5)
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

    //解析数据
    private void parseJson1(String response) {
        try {
            CarRecMastPageBean bean = JSON.parseObject(response,CarRecMastPageBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<CarRecMastBean> list = bean.getRows();
                    getV().refreshAdapter(list, bean.getSumAmt());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().updateStatusSuccess();
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
