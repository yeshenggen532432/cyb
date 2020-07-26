package com.qwb.view.tab.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DMessageBean;
import com.qwb.view.tab.ui.XMessageFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;


import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 消息
 */
public class PxMessage extends XPresent<XMessageFragment>{

    private Activity context;

    /**
     * 转让客户 是否同意：1：同意；2：不同意
     */
    public void doAgree(Activity context, DMessageBean item, String isAgree) {
        this.context = context;
        OkHttpUtils
                .post()
                .url(Constans.zrCustomerCzWeb)
                .addParams("token", SPUtils.getTK())
                .addParams("belongId", String.valueOf(item.getBelongId()))
                .addParams("memberId", String.valueOf(item.getMemberId()))
                .addParams("msgId", String.valueOf(item.getMsgId()))
                .addParams("tp", isAgree)
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
    /**
     * 同意或不同意加入公司申请 是否同意：1：同意；2：不同意
     */
    public void doAgreeJoinCompany(Activity context, DMessageBean item, String isAgree) {
        this.context = context;
        OkHttpUtils
                .post()
                .url(Constans.inCompanyAgreeURL)
                .addParams("token", SPUtils.getTK())
                .addParams("agree", isAgree)
                .addParams("memId", String.valueOf(item.getMemberId()))
                .addParams("deptId", String.valueOf(0))
                .addParams("id", String.valueOf(item.getBelongId()))
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
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean.isState()) {
                getV().doAgreeSuccess();
            }else{
                ToastUtils.showCustomToast( bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


}
