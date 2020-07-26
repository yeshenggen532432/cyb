package com.qwb.view.audit.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.audit.model.ShenpiModel;
import com.qwb.view.audit.model.ShenpiModelBean;
import com.qwb.view.audit.ui.AuditActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 *  审批
 */
public class PAudit extends XPresent<AuditActivity>{

    private Activity context;

    /**
     * 查询审批自定义
     */
    public void queryData(Activity activity) {
        this.context = activity;
        OkHttpUtils
                .post()
                .url(Constans.queryAuditZdy)
                .addParams("token", SPUtils.getTK())
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
     * 删除审批自定义
     */
    public void deleteData(Activity context,int id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(id));// 自定义id
        OkHttpUtils.post()
                .params(params)
                .url(Constans.deleteAuditZdy)
                .id(2)
                .build()
                .execute(new MyHttpCallback(context) {
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

    //解析数据
    private void parseJson1(String response) {
        try {
            ShenpiModelBean bean = JSON.parseObject(response, ShenpiModelBean.class);
            if (null != bean && bean.isState()) {
                List<ShenpiModel> list = bean.getList();
                if(list == null || list.isEmpty()){
                    return;
                }
//                getV().doZdyModelData(list);
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean baseBean = JSON.parseObject(response,BaseBean.class);
            if (null != baseBean && baseBean.isState() ) {
//                    getV().doDelData();
                ToastUtils.showCustomToast(baseBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }




}
