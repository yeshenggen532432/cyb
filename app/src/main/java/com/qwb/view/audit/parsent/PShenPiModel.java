package com.qwb.view.audit.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.audit.ui.ShenpiModelActivity;
import com.qwb.view.base.model.BaseBean;
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
 * 自定义审批模板
 */
public class PShenPiModel extends XPresent<ShenpiModelActivity>{

    /**
     * type: 1.添加；2修改
     */
    public void addData(Activity activity, String name, String mIds, String tp, String isSy, final int type, int spId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("zdyNm", name);
        params.put("memIds", mIds);
        params.put("tp", tp);// 类型:1类型，2时间，3详情，4备注 5:金额
        params.put("isSy", isSy);// 1私用;2共用

        String url;
        if(2 == type){
            params.put("id", "" + spId);
            url = Constans.updateAuditZdy;
        }else{
            url = Constans.addBscAuditZdy;
        }

        OkHttpUtils.post()
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
                        parseJson1(response);
                    }
                });


    }



    //解析数据
    private void parseJson1(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().addSuccess();
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


}
