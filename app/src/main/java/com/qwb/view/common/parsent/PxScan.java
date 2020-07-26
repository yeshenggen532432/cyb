package com.qwb.view.common.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.ui.XScanActivity;
import com.qwb.view.step.model.ShopInfoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：扫描
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PxScan extends XPresent<XScanActivity>{

    public void getWareByScan(Activity context, String keyWord, int stkId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
        params.put("noCompany", "1");
        params.put("stkId", String.valueOf(stkId));//仓库id
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryStkWare1)
                .id(5)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        if(null != getV()){
                            getV().restartScan();
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson5(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------

    //解析数据-单个扫描和多次扫描
    private void parseJson5(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        getV().doScanSuccess(list);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                    if(null != getV()){
                        getV().restartScan();
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
            if(null != getV()){
                getV().restartScan();
            }
        }
    }

}
