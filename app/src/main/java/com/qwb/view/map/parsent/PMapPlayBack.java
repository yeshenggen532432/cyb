package com.qwb.view.map.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSONObject;
import com.qwb.view.map.ui.MapPlaybackActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.flow.model.FlowBfListBean;
import com.qwb.view.map.model.QueryBflsmwebBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 员工在线-拜访回放
 */
public class PMapPlayBack extends XPresent<MapPlaybackActivity>{

    /**
     * 获取拜访回放
     */
    public void loadData(Activity activity,int mid ,String date) {
        OkHttpUtils
                .post()
                .url(Constans.queryBflsmweb)
                .addParams("token", SPUtils.getTK())
                .addParams("mid", String.valueOf(mid))
                .addParams("date", date)
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
     * 获取拜访回放(流动打卡 + 上下班)
     */
    public void querySignInBfhf(Activity activity, int mid , String date) {
        OkHttpUtils
                .post()
                .url(Constans.querySignInBfhf)
                .addParams("token", SPUtils.getTK())
                .addParams("mid", String.valueOf(mid))
                .addParams("date", date)
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

    //TODO ------------------------接口回調----------------------

    //解析数据-普通查询
    private void parseJson1(String response) {
        try {
            QueryBflsmwebBean parseObject = JSONObject.parseObject(response, QueryBflsmwebBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<QueryBflsmwebBean.QueryBfHf> list = parseObject.getList();
                    if(getV()!=null){
                        getV().showData(list);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-普通查询
    private void parseJson2(String response) {
        try {
            FlowBfListBean parseObject = JSONObject.parseObject(response, FlowBfListBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<FlowBfListBean.FlowBfBean> list = parseObject.getList();
                    if(getV()!=null){
                        getV().showDataFlow(list);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
