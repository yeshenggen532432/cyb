package com.qwb.view.log.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.member.model.BuMenListBean;
import com.qwb.view.log.ui.LookLogFragment;
import com.qwb.view.audit.ui.AddCurrentIds;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.log.model.RizhiListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：看日报；周报；月报
 */
public class PLogLook extends XPresent<LookLogFragment>{

    /**
     * 获取发送的列表
     */
    public void loadDataSend(Activity activity, int pageNo) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        OkHttpUtils.post().params(params).url(Constans.queryReportWebPage2).id(2).build().execute(new MyHttpCallback(activity) {
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
     * 获取接收的列表
     */
    public void loadDataReceive(Activity activity, int pageNo, String sdate, String edate) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        if (!MyUtils.isEmptyString(sdate)) {// 开始时间
            params.put("sdate", sdate);
        }
        if (!MyUtils.isEmptyString(edate)) {// 结束时间
            params.put("edate", edate);
        }
        if (!MyUtils.isEmptyString(getIds())) {// 发送人ids
            params.put("fsMids", getIds());
        }
        OkHttpUtils.post().params(params).url(Constans.queryReportWebPage3).id(1).build().execute(new MyHttpCallback(activity) {
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
     * 获取成员ids
     */
    private String getIds() {
        StringBuffer sb = new StringBuffer();
        List<BuMenListBean.MemberBean> choiseIds = AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG, false);
        if (choiseIds != null) {
            if (choiseIds.size() == 1) {
                sb.append(choiseIds.get(0).getMemberId());
            } else {
                for (int i = 0; i < choiseIds.size(); i++) {
                    if (i == choiseIds.size() - 1) {// 最后一个不加“,”
                        sb.append(choiseIds.get(i).getMemberId());
                    } else {
                        sb.append(choiseIds.get(i).getMemberId() + ",");
                    }
                }
            }
        }
        return sb.toString();
    }



    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showCustomToast(e.getMessage());
            if(getV()!=null){
                //关闭刷新
                getV().closeRefresh();
            }
        }

        @Override
        public void onResponse(String response, int id) {
            XLog.e("response",response);
            switch (id) {
                case 1://接收
                    parseJson1(response);
                    break;
                case 2://发送
                    parseJson2(response);
                    break;
            }
        }
    }

    //解析数据-接收
    private void parseJson1(String response) {
        try {
            RizhiListBean parseObject = JSON.parseObject(response,RizhiListBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<RizhiListBean.RizhiList> dataList = parseObject.getRows();
                    getV().refreshAdapterReceive(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据二-发送
    private void parseJson2(String response) {
        try {
            RizhiListBean parseObject = JSON.parseObject(response,RizhiListBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<RizhiListBean.RizhiList> dataList = parseObject.getRows();
                    getV().refreshAdapterSend(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
}
