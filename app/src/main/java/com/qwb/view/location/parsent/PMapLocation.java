package com.qwb.view.location.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.location.ui.MapLocationActivity;
import com.qwb.utils.ConstantUtils;
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
 *  地图
 */
public class PMapLocation extends XPresent<MapLocationActivity>{

    /**
     */
    public void queryDataGetLocation(Activity activity, int userId, final String name) {
        Map<String, String> params = new HashMap<>();
        params.put("company_id", SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID));
        params.put("user_id", String.valueOf(userId));
        OkHttpUtils.post().params(params).url(Constans.getRealtimeLocation).id(1).build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response, name);
                    }
                });
    }


    //解析数据-普通查询
    private void parseJson1(String response, String name) {
        try {
            JSONObject parseObject = JSON.parseObject(response);
            if (parseObject.getIntValue("code") == 1) {
                ToastUtils.showCustomToast(parseObject.getString("error"));
            } else {
                String latitude = String.valueOf(parseObject.getDoubleValue("latitude"));
                String longitude = String.valueOf(parseObject.getDoubleValue("longitude"));
                String address = parseObject.getString("address");
                getV().setData(latitude, longitude, address, name);
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }




}
