package com.qwb.view.car.parsent;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyRequestUtil;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.car.ui.CarActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.car.model.CarRecMastPageBean;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.order.model.QueryDhorderBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：车销单
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCar extends XPresent<CarActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.CXGL_NEW, ConstantUtils.Apply.CXXDGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.CXGL_NEW, ConstantUtils.Apply.CXXDGL_OLD);

    /**
     * 获取车销单列表
     */
    public void queryCarOrder(Activity activity, EditText etSearch, int pageNo, int pageSize, String startDate, String endDate,String stkId) {
        String searchStr=etSearch.getText().toString().trim();//搜索
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("kmNm", String.valueOf(searchStr));// 业务员或客户名称
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        params.put("dataTp", String.valueOf(dataTp));//角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);//角色
        }
        if (!MyStringUtil.isEmpty(stkId)) {
            params.put("stkId", stkId);//仓库id
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryCarOrder)
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
     * 检查车销订单是否有空
     */
    public void queryStkCarRectMastStatus(Activity activity, String orderIds) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderIds", String.valueOf(orderIds));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarRecMast_checkOrderHasRec)
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
     * 获取仓库
     */
    public void queryStorageList() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryWebStkCarStorageList)
                .id(26)
                .build()
                .execute(new MyHttpCallback(null) {
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
     * 暂存收款记录
     */
    public void updateStatusZc(Activity activity, int orderId, int status, int accType, String money) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderId", String.valueOf(orderId));
        params.put("status", String.valueOf(status));
        params.put(" accType", String.valueOf(accType));
        params.put("money", String.valueOf(money));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.stkCarRecMast_drageRec)
                .id(3)
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

    /**
     * 作废订单
     */
    public void cancelOrder(Activity activity, int orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderId", String.valueOf(orderId));
        String url = Constans.cancelOrderAndOutByOrderId;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(33)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson33(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            QueryDhorderBean parseObject = JSON.parseObject(response,QueryDhorderBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<QueryDhorderBean.Rows> dataList = parseObject.getRows();
                    getV().refreshAdapterOrder(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-仓库列表
    private void parseJson26(String response) {
        try {
            StorageBean parseObject = JSON.parseObject(response, StorageBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<StorageBean.Storage> datas = parseObject.getList();
                if(getV()!=null){
                    getV().refreshAdapterStorage(datas);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson3(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if(bean.isState()){
                    ToastUtils.showCustomToast("收款成功");
                    getV().updateStatusSuccess();
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
            CarRecMastPageBean bean = JSON.parseObject(response, CarRecMastPageBean.class);
            if (bean != null) {
                if(bean.isState()){
                    getV().refreshAdapterStatus(bean.getRows());
                }else{
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    private void parseJson33(String response) {
        try {
            BaseBean result = JSON.parseObject(response,BaseBean.class);
            if (MyRequestUtil.isSuccess(result)) {
                getV().doCanCelOrderSuccess();
            }else {
                ToastUtils.showCustomToast(result.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
