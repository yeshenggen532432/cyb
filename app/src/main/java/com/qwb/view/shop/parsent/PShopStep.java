package com.qwb.view.shop.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.shop.ui.ShopStepActivity;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：商城订单
 */
public class PShopStep extends XPresent<ShopStepActivity>{
    /**
     * 获取销售类型
     */
    public void queryXsTp(Activity activity) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryXstypels)
                .id(4)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }

    /**
     * 提交数据或修改数据
     */
    public void addData(Activity activity,String clientId,int xdType,int ddId,String jsonStr,
                        String shrStr,String phoneStr,String addressStr,String remoStr,String zjeStr,String zdzkStr,String cjjeStr,
                        String shTimeStr,String pszdStr,String stkId,String freight) {
        XLog.e("shrStr", shrStr);
        XLog.e("phoneStr", phoneStr);
        XLog.e("addressStr", addressStr);
        XLog.e("remoStr", remoStr);
        XLog.e("zdzkStr", zdzkStr);
        XLog.e("zjeStr", zjeStr);
        XLog.e("cjjeStr", cjjeStr);
        XLog.e("shTimeStr", shTimeStr);
        XLog.e("pszdStr", pszdStr);
        XLog.e("token", SPUtils.getTK());
        XLog.e("cid", clientId);
        XLog.e("orderxx", jsonStr);
        XLog.e("id", ""+ddId);
        XLog.e("stkId", ""+stkId);


        //-------------------------------------------
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if (!MyStringUtil.isEmpty(shrStr)) {
            params.put("shr", shrStr);
        }
        if (!MyStringUtil.isEmpty(phoneStr)) {
            params.put("tel", phoneStr);
        }
        if (!MyStringUtil.isEmpty(addressStr)) {
            params.put("address", addressStr);
        }
        if (!MyStringUtil.isEmpty(remoStr)) {
            params.put("remo", remoStr);
        }
        if (!MyStringUtil.isEmpty(zjeStr)) {
            params.put("zje", zjeStr);
        }
        if (!MyStringUtil.isEmpty(zdzkStr)) {
            params.put("zdzk", zdzkStr);
        }
        if (!MyStringUtil.isEmpty(cjjeStr)) {
            params.put("cjje", cjjeStr);
        }
        if (!MyStringUtil.isEmpty(jsonStr)) {
            params.put("orderxx", jsonStr);
        }
        if (!MyStringUtil.isEmpty(shTimeStr)) {
            params.put("shTime", shTimeStr);
        }
        if (!MyStringUtil.isEmpty(pszdStr)) {
            params.put("pszd", pszdStr);
        }
        if (!MyStringUtil.isEmpty(stkId)) {
            params.put("stkId", stkId);
        }
        params.put("id", "" + ddId);
        if (!MyStringUtil.isEmpty(freight)) {
            params.put("freight", freight);
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.shopBforderWeb_updateBforderWeb)
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
     * 商城订单
     */
    public void queryScOrder(Activity activity,int orderId,String companyId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderId", String.valueOf(orderId));
        params.put("companyId", String.valueOf(companyId));
        String url = Constans.shopBforderWeb_queryShopBforderWeb;
        OkHttpUtils
                .post()
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
                        parseJson1(response);
                    }
                });
    }


    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            QueryBforderBean bean = JSON.parseObject(response,QueryBforderBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().doUI(bean, null);//显示商品信息
                    }
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
            XLog.e(response);
            JSONObject parseObject = JSON.parseObject(response);
            if (null != parseObject && parseObject.getBoolean("state") && null != getV() ) {
                getV().submitSuccess(0);//提交数据成功
            }else{
                ToastUtils.showCustomToast(parseObject.getString("msg"));
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    //解析数据-销售类型
    private void parseJson4(String response) {
        try {
            QueryXstypeBean parseObject = JSON.parseObject(response, QueryXstypeBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<QueryXstypeBean.QueryXstype> datas = parseObject.getXstypels();
                if(getV()!=null){
                    getV().showDialogXstp(datas);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
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



}
