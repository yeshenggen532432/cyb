package com.qwb.view.checkstorage.parsent;


import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.checkstorage.model.StkCheckBean;
import com.qwb.view.checkstorage.model.StkCheckListBean;
import com.qwb.view.checkstorage.ui.XStkCheckListTempActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：盘点单列表
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PxStkCheckListTemp extends XPresent<XStkCheckListTempActivity>{

    /**
     * 盘点单列表
     */
    public void queryData(Activity activity,int pageNo,int pageSize, String startDate, String endDate, String status) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("page", String.valueOf(pageNo));
        params.put("rows", String.valueOf(pageSize));//每页几条
        params.put("status", String.valueOf(status));//每页几条
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkCheckTempPages)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        ToastUtils.showCustomToast(e.getMessage());
                        try {
                            getV().closeRefresh();//关闭刷新
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
     * 合并单
     */
    public void addData(Activity activity, String billIds, Integer stkId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billIds", billIds);
        params.put("stkId", String.valueOf(stkId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.mergeStkcheck)
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
            }
        }
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            StkCheckListBean parseObject = JSON.parseObject(response,StkCheckListBean.class);
            if (parseObject != null) {
                List<StkCheckBean> dataList = parseObject.getRows();
                getV().refreshAdapter(dataList);
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if(bean.isState()){
                    ToastUtils.showCustomToast(bean.getMsg());
                    getV().addDataSuccess();
                }else{
                    getV().addDataError(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
