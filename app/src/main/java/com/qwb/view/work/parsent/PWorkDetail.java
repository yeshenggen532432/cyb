package com.qwb.view.work.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.view.work.model.WorkDetailBean;
import com.qwb.view.work.ui.WorkDetailActivity;
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
 * 考勤明细
 */
public class PWorkDetail extends XPresent<WorkDetailActivity>{

    public void queryData(Activity activity, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(id));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.checkinDetailsURL)
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
            WorkDetailBean bean = JSON.parseObject(response, WorkDetailBean.class);
            if (bean != null && bean.isState()) {
                getV().doUI(bean);
            } else {
                ToastUtils.showCustomToast( bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }




}
