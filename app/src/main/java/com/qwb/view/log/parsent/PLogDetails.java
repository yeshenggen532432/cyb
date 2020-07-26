package com.qwb.view.log.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.log.ui.LogDetailActivity;
import com.qwb.view.log.model.RizhiDetialBean;
import com.qwb.view.log.model.RizhiPinlunBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：日报；周报；月报(详情)
 */
public class PLogDetails extends XPresent<LogDetailActivity>{

    /**
     * 获取详情
     */
    public void loadData(Activity activity, int id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(id));
        OkHttpUtils.post().params(params).url(Constans.queryReportWeb).id(1).build().execute(new MyHttpCallback(activity) {
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
     * 获取评论
     */
    public void loadDataComment(Activity activity,int id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bid", String.valueOf(id));
        params.put("pageNo", "1");
        OkHttpUtils.post().params(params).url(Constans.queryReportPlWebPage).id(2).build().execute(new MyHttpCallback(null) {
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
     * 添加评论
     */
    public void addDataComment(Activity activity,int id,String content) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bid", String.valueOf(id));
        params.put("content", content);
        OkHttpUtils.post().params(params).url(Constans.addReportPl).id(3).build().execute(new MyHttpCallback(activity) {
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
            ToastUtils.showCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            XLog.e("response",response);
            switch (id) {
                case 1://详情
                    parseJson1(response);
                    break;
                case 2://评论
                    parseJson2(response);
                    break;
                case 3://添加评论
                    parseJson3(response);
                    break;
            }
        }
    }

    //解析数据-详情
    private void parseJson1(String response) {
        try {
            RizhiDetialBean parseObject = JSON.parseObject(response,RizhiDetialBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        getV().showData(parseObject);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }

        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据二-评论
    private void parseJson2(String response) {
        try {
            RizhiPinlunBean parseObject = JSON.parseObject(response,RizhiPinlunBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        getV().refreshAdapterComment(parseObject);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }

        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-添加报评论
    private void parseJson3(String response) {
        try {
            JSONObject parseObject3 = JSON.parseObject(response);
            if (parseObject3 != null) {
                if (parseObject3.getBoolean("state")) {
                    if(getV()!=null){
                        getV().addDataSuccess();
                    }
                }
                ToastUtils.showCustomToast(parseObject3.getString("msg"));
            }

        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
}
