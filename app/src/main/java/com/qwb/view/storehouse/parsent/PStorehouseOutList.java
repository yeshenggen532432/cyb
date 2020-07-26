package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.storehouse.model.StorehouseOutListResult;
import com.qwb.view.storehouse.ui.StorehouseOutListActivity;
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
 * 库位：出仓单列表
 */
public class PStorehouseOutList extends XPresent<StorehouseOutListActivity> {

    private Activity context;

    /**
     *
     */
    public void queryData(Activity activity, String  searchStr, int pageNo, String startDate, String endDate, String status) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(10));
        params.put("status", String.valueOf(status));
        params.put("billNo", String.valueOf(searchStr));
        params.put("sdate", startDate);// 开始时间
        params.put("edate", endDate);// 结束时间
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkCrewOutPage)
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


    /**
     *
     */
    public void updateStatus(Activity activity, Integer billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateStatusAuditStkCrewOut)
                .id(1)
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
            StorehouseOutListResult bean = JSON.parseObject(response, StorehouseOutListResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().refreshAdapter(bean.getList());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()){
                    getV().auditSuccess();
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
