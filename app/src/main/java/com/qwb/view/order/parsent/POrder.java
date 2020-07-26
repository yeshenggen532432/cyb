package com.qwb.view.order.parsent;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.order.ui.OrderActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.order.model.QueryDhorderBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：订货下单，退货下单
 */
public class POrder extends XPresent<OrderActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.DHXD_NEW, ConstantUtils.Apply.DHXD_NEW);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.DHXD_NEW, ConstantUtils.Apply.DHXD_NEW);

    /**
     * 获取订货下单列表
     */
    public void loadDataOrder(Activity activity, EditText etSearch, int pageNo, String startDate, String endDate) {
        String searchStr=etSearch.getText().toString().trim();//搜索
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
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
        //根据公司类型：快消，卖场--跳到不同的页面
        String tpNmStr = SPUtils.getSValues("tpNm");
        String url = Constans.queryDhorder;
        if("卖场".equals(tpNmStr)) {
            url = Constans.queryOrderlsPage;
        }
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
     * 获取退货下单列表
     */
    public void loadDataRetreat(Activity activity, EditText etSearch, int pageNo, String startDate, String endDate) {
        String searchStr=etSearch.getText().toString().trim();//搜索
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("kmNm", String.valueOf(searchStr));// 业务员或客户名称
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        params.put("dataTp", dataTp);//角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);//角色
        }
        //根据公司类型：快消，卖场--跳到不同的页面
        String tpNmStr = SPUtils.getSValues("tpNm");
        String url = Constans.queryThorder;
        if ("卖场".equals(tpNmStr)) {
            url = Constans.queryOrderlsPage;
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(2)
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
                        parseJson2(response);
                    }
                });
    }


    //TODO ------------------------接口回調----------------------

    //解析数据-订货
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

    //解析数据二-退货
    private void parseJson2(String response) {
        try {
            QueryDhorderBean parseObject = JSON.parseObject(response,QueryDhorderBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<QueryDhorderBean.Rows> dataList = parseObject.getRows();
                    getV().refreshAdapterRetreat(dataList);
                } else {
                    ToastUtils.showCustomToast( parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
}
