package com.qwb.view.car.parsent;


import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.car.model.CarStkMovePageResult;
import com.qwb.view.car.model.StkMoveBean;
import com.qwb.view.car.ui.CarStkMoveListActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：移库：1.车销配货单列表 2.车销回库单
 */
public class PCarStkMoveList extends XPresent<CarStkMoveListActivity>{
    /**
     * 获取车销单列表
     */
    public void page(Activity activity,int pageNo, int pageSize, String startDate, String endDate, int billType, String status) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("billType", String.valueOf(billType));
        params.put("status", String.valueOf(status));
        params.put("mid", SPUtils.getID());
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarMove_page)
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

    /**
     * 审批
     */
    public void audit(Activity activity, int billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarMove_audit)
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

    /**
     * 取消
     */
    public void cancel(Activity activity, int billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarMove_cancel)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            CarStkMovePageResult bean = JSON.parseObject(response, CarStkMovePageResult.class);
            if (MyRequestUtil.isSuccess(bean)) {
                List<StkMoveBean> dataList = bean.getRows();
                getV().refreshAdapter(dataList);
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (MyRequestUtil.isSuccess(bean)) {
                getV().doAuditSuccess();
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据
    private void parseJson3(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (MyRequestUtil.isSuccess(bean)) {
                getV().doCancelSuccess();
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
