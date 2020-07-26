package com.qwb.view.car.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.model.StkMoveDetailBean;
import com.qwb.view.car.ui.CarStkOutOrderActivity;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 车销配货单
 */
public class PCarStkOutOrder extends XPresent<CarStkOutOrderActivity>{
    /**
     * 提交数据或修改数据
     */
    public void addData(Activity activity,String orderId, String stkId,String stkName,String stkInId,String stkInName,String remarks,
                        String totalAmt,String jsonStr,int billType, String queryToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", orderId);
        params.put("stkId", stkId);
        params.put("stkName", stkName);
        params.put("stkInId", stkInId);
        params.put("stkInName", stkInName);
        params.put("remarks", remarks);
        params.put("totalAmt", totalAmt);
        params.put("billType", String.valueOf(billType));
        params.put("orderxx", jsonStr);
        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarMove_save)
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
     * 获取仓库
     */
    public void queryStorage(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryWebStkStorageList)
                .id(26)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson26(response);
                    }
                });
    }

    /**
     * 详情
     */
    public void queryData(Activity activity, String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(orderId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarMove_show)
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
     * 获取token
     */
    public void queryToken(Activity activity) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryToken)
                .id(28)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson28(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (null != bean) {
                if(bean.isState()){
                    getV().submitSuccess();//提交数据成功
                }else{
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
            StkMoveDetailBean bean = JSON.parseObject(response, StkMoveDetailBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().doUI(bean.getStkMove());//显示商品信息
                    }
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-仓库列表
    private void parseJson26(String response) {
        try {
            StorageBean parseObject = JSON.parseObject(response, StorageBean.class);
            if ( null != parseObject && parseObject.isState()) {
                List<StorageBean.Storage> datas = parseObject.getList();
                if(getV()!=null){
                    getV().showDialogStorage(datas);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


    //解析数据--获取token
    private void parseJson28(String response) {
        try {
            TokenBean bean = JSON.parseObject(response, TokenBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    getV().doToken(bean.getData());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }




}
