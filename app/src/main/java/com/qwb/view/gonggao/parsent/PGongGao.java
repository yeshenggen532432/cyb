package com.qwb.view.gonggao.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyRequestUtil;
import com.qwb.view.gonggao.model.GongGaoBean;
import com.qwb.view.gonggao.model.GongGaoListResult;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.gonggao.ui.GongGaoActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 公告
 */
public class PGongGao extends XPresent<GongGaoActivity>{

    public void queryData(Activity context, int pageNo, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        OkHttpUtils.post()
                .params(params)
                .url(Constans.noticeLists)
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


    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            GongGaoListResult result = JSON.parseObject(response, GongGaoListResult.class);
            if (MyRequestUtil.isSuccess(result)) {
                List<GongGaoBean> rows = result.getRows();
                if(null == rows){
                    rows = new ArrayList<>();
                }
                if(null != getV()){
                    getV().refreshAdapter(rows);
                }
            }else{
                ToastUtils.showCustomToast(result.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}
