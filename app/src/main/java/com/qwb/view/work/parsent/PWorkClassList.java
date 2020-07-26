package com.qwb.view.work.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.view.work.model.WorkClassListResult;
import com.qwb.view.work.ui.WorkClassListActivity;
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
 * 班次列表
 */
public class PWorkClassList extends XPresent<WorkClassListActivity> {

    /**
     * 获得班次列表
     */
    public void queryWorkList(Activity activity) {
        String url = Constans.queryBcList;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils.post().params(params).url(url).id(1).build()
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
            WorkClassListResult parseObject = JSON.parseObject(response, WorkClassListResult.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if (getV() != null) {
                        getV().refreshAdapter(parseObject);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }

        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
