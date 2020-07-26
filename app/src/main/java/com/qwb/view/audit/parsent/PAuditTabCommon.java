package com.qwb.view.audit.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.audit.model.ShenPiIShenPiBean;
import com.qwb.view.audit.model.ShenPiIsendBean;
import com.qwb.view.audit.ui.AuditTabCommonFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 我发起的，我审批的，我执行的（共用）
 */
public class PAuditTabCommon extends XPresent<AuditTabCommonFragment>{

    private Activity activity;

    public void queryData(Activity activity, int pageNo, int pageSize, String isOver,String isCheck, String date, String search, final int type) {
        this.activity = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("isOver", isOver);//我发起，我执行
        params.put("isCheck", isCheck);//我审批
        if (!MyUtils.isEmptyString(date)) {
            params.put("addTime", date);
        }
        if (!MyUtils.isEmptyString(search)) {
            params.put("seach", search);
        }

        String url = Constans.queryMyAuditURL;
        if(1 == type){
            url = Constans.queryMyAuditURL;
        }else if(2 == type){
            url = Constans.queryMyCheckURL;
        }else if(3 == type){
            url = Constans.queryExecAudit;
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response, type);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response, int type) {
        try {
            if(2 == type){
                ShenPiIShenPiBean bean = JSON.parseObject(response, ShenPiIShenPiBean.class);
                if (bean != null) {
                    List<ShenPiIShenPiBean.ShenPiIShenPiItemBean> list = bean.getCheckAuditList();
                    if (bean.isState()) {
                        getV().refreshAdapter2(list, bean.getTotal());
                    } else {
                        ToastUtils.showCustomToast(bean.getMsg());
                    }
                }
            }else{
                ShenPiIsendBean bean = JSON.parseObject(response, ShenPiIsendBean.class);
                if (bean != null) {
                    List<ShenPiIsendBean.ShenPiIsendItemBean> list = bean.getAuditList();
                    if (bean.isState()) {
                        getV().refreshAdapter(list, bean.getTotal());
                    } else {
                        ToastUtils.showCustomToast(bean.getMsg());
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


}
