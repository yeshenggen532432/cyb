package com.qwb.view.map.parsent;


import android.app.Activity;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.map.ui.MapTrackActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.map.model.TrackBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.map.model.TrackListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 员工在线-轨迹回放
 */
public class PMapTrack extends XPresent<MapTrackActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFDT_NEW, ConstantUtils.Apply.BFDT_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFDT_NEW, ConstantUtils.Apply.BFDT_OLD);

    /**
     * 地图的
     */
    public void loadDataMap(Activity activity, int mid, String date, RadioButton rbCustom, TextView tvStratTime, TextView tvEndTime) {
        Map<String, String> params = new HashMap<>();
        params.put("company_id", SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID));
        params.put("user_id", String.valueOf(mid));
            params.put("cleanup", String.valueOf("y"));// 纠偏
        if (rbCustom.isChecked()) {
            String startTime = tvStratTime.getText().toString().trim();
            String endTime = tvEndTime.getText().toString().trim();
            params.put("time_begin", startTime + ":00");
            params.put("time_end", endTime + ":00");
        } else {
            params.put("date", date);
        }
        OkHttpUtils.post().params(params).url(Constans.getDailyLocation).id(1).build().execute(new MyHttpCallback(activity) {
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
     * 列表的
     */
    public void loadDataList(Activity activity, int mid,int pageNo, String date, RadioButton rbCustom, TextView tvStratTime,TextView tvEndTime) {
        Map<String, String> params = new HashMap<>();
        params.put("company_id", SPUtils.getSValues("companyId"));
        params.put("user_id", String.valueOf(mid));
        if (rbCustom.isChecked()) {
            String startTime = tvStratTime.getText().toString().trim();
            String endTime = tvEndTime.getText().toString().trim();
            params.put("time_begin", startTime + ":00");
            params.put("time_end", endTime + ":00");
        } else {
            params.put("date", date);
        }
        params.put("page", String.valueOf(pageNo));
        params.put("page_size", String.valueOf(20));
        params.put("field", "longitude,latitude,location_time,address,os,location_from");//// 如需要指定获取的字段信息，请在URL中添加查询项
        OkHttpUtils.post().params(params).url(Constans.getDailyLocation).id(2).build().execute(new MyHttpCallback(null) {
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
     * 里程的
     */
    public void loadDataLC(Context context, int mid) {
        Map<String, String> params=new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("entityNms", String.valueOf(mid));
        params.put("dataTp", dataTp);
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryMapGjLsdtClj)
                .id(3)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            try {
                if(getV()!=null){
                    getV().closeRefresh();
                }
            }catch (Exception e2){
            }
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://
                    parseJson1(response);
                    break;
                case 2://
                    parseJson2(response);
                    break;
                case 3://
                    parseJson3(response);
                    break;
            }
        }
    }

    //解析数据-地图
    private void parseJson1(String response) {
        try {
            TrackBean mTrackBean = JSON.parseObject(response, TrackBean.class);
            if (mTrackBean != null) {
                if(getV()!=null){
                    getV().showDataMap(mTrackBean);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-列表
    private void parseJson2(String response) {
        try {
            TrackBean mTrackBean2 = JSON.parseObject(response, TrackBean.class);
            if (mTrackBean2 != null) {
                if (0 == mTrackBean2.getCode()) {
                    if(getV()!=null){
                        getV().showDataList(mTrackBean2);
                    }
                } else {
                    ToastUtils.showCustomToast("稍后再试");
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-里程数
    private void parseJson3(String response) {
        try {
            TrackListBean parseObject2 = JSONObject.parseObject(response, TrackListBean.class);
            if (parseObject2 != null) {
                if (parseObject2.isState()) {
                    if(getV()!=null){
                        List<TrackListBean.TrackList> rows = parseObject2.getRows();
                        getV().showDataLc(rows);
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
}
