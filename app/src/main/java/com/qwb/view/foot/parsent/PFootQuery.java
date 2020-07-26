package com.qwb.view.foot.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.foot.model.FootPageResult;
import com.qwb.view.foot.ui.FootQueryActivity;
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
 * 我的足迹（查询）
 */
public class PFootQuery extends XPresent<FootQueryActivity>{
    private Activity activity;

    public void queryFlow(String sdate, String edate, int pageNo, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("startDate", sdate);
        params.put("edate", MyStringUtil.appendHMS(edate));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.querySignInPageMS)
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

    //TODO ------------------------接口回調----------------------
    //解析数据
    private void parseJson1(String response) {
        try {
            FootPageResult bean = JSON.parseObject(response,FootPageResult.class);
            if(MyRequestUtil.isSuccess(bean)){
                getV().refreshAdapter(bean.getRows());
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }



}
