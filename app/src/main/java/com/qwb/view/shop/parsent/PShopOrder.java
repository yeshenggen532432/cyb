package com.qwb.view.shop.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.shop.ui.ShopOrderActivity;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.shop.model.ShopAddressInfo;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.model.QueryBforderBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：商城订单-下单，查看
 */
public class PShopOrder extends XPresent<ShopOrderActivity>{

    /**
     * 提交数据或修改数据
     */
    public void addData(Activity context, String companyId, String jsonStr, String shrStr, String phoneStr, String addressStr, String remoStr, String zjeStr, String zdzkStr, String cjjeStr) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("companyId", companyId);
        if (!MyUtils.isEmptyString(shrStr)) {
            params.put("shr", shrStr);
        }
        if (!MyUtils.isEmptyString(phoneStr)) {
            params.put("tel", phoneStr);
        }
        if (!MyUtils.isEmptyString(addressStr)) {
            params.put("address", addressStr);
        }
        if (!MyUtils.isEmptyString(remoStr)) {
            params.put("remo", remoStr);
        }
        if (!MyUtils.isEmptyString(zjeStr)) {
            params.put("zje", zjeStr);
        }
        if (!MyUtils.isEmptyString(zdzkStr)) {
            params.put("zdzk", zdzkStr);
        }
        if (!MyUtils.isEmptyString(cjjeStr)) {
            params.put("cjje", cjjeStr);
        }
        if (!MyUtils.isEmptyString(jsonStr)) {
            params.put("orderxx", jsonStr);
        }

        String url = Constans.shopBforderWeb_addAppOrderWeb;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
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
     * 获取客户下单详情
     */
    public void queryOrderDetail(Activity context,String orderId,String companyId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderId", orderId);
        params.put("companyId", String.valueOf(companyId));
        String url = Constans.shopBforderWeb_queryShopBforderWeb;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(context) {
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
     * 获取客户下单详情
     */
    public void deleteOrder(Activity context,String orderId,String companyId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderId", orderId);
        params.put("companyId", String.valueOf(companyId));
        String url = Constans.shopBforderWeb_deleteBforderWeb;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(3)
                .build()
                .execute(new MyHttpCallback(context) {
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
     * 获取默认地址
     */
    public void queryDefaultAddress(Activity context,String companyId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("companyId", String.valueOf(companyId));
        String url = Constans.queryMemberDefaultAddressWeb;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(4)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            JSONObject parseObject = JSON.parseObject(response);
            if (parseObject != null) {
                if (parseObject.getBoolean("state")) {
                    if(getV()!=null){
                        getV().resultDataSuccessOrder();//下单提交数据成功
                    }
                }
                ToastUtils.showCustomToast(parseObject.getString("msg"));
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    //解析数据-查看
    private void parseJson2(String response) {
        try {
//            SupplierOrderDetailBean parseObject = JSON.parseObject(response, SupplierOrderDetailBean.class);
            QueryBforderBean parseObject = JSON.parseObject(response, QueryBforderBean.class);
            if (parseObject != null && parseObject.isState()) {
                if(getV()!=null){
                    getV().resultDataOrderInfo(parseObject);//显示商品信息
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-删除
    private void parseJson3(String response) {
        try {
            BaseBean parseObject = JSON.parseObject(response, BaseBean.class);
            if (parseObject != null && parseObject.isState()) {
                if(getV()!=null){
                    getV().resultDataDelSuccess();//显示商品信息
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-默认地址
    private void parseJson4(String response) {
        try {
            ShopAddressInfo data=JSON.parseObject(response, ShopAddressInfo.class);
            if (data != null){
                if (data.isState()) {
                    getV().resultShowAddressInfo(data);
                } else {
                    ToastUtils.showCustomToast(data.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
