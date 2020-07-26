package com.qwb.view.work.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.work.ui.WorkClassAddressActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 班次地址
 */
public class PWorkClassAddress extends XPresent<WorkClassAddressActivity>{

    /**
     * 修改班次
     */
    public void updateWorkAddress(Activity activity, String bcId, String latitude, String longitude, String address, String areaLong, String outOf) {
        String url=Constans.updateBcPosition;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", bcId);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("address", address);
        params.put("areaLong", areaLong);
        params.put("outOf", outOf);
        OkHttpUtils.post().params(params).url(url).id(1).build()
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


    //解析数据-解析json:商品信息
    private void parseJson1(String response) {
        try {
            BaseBean parseObject = JSON.parseObject(response, BaseBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        getV().updateSuccess();
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }

        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
