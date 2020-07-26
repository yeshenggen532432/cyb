package com.qwb.view.object.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.object.model.ProviderListBean;
import com.qwb.view.object.ui.ObjectProviderFragment;
import com.qwb.utils.MyHttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 对象：供货商
 */
public class PObjectProvider extends XPresent<ObjectProviderFragment>{

    /**
     */
    public void queryProviderPage(Activity activity, String proName, int pageNo, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("proName", proName);
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryProviderPage)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            ProviderListBean bean = JSON.parseObject(response, ProviderListBean.class);
            if (bean != null && bean.isState()) {
                List<ProviderListBean.ProviderBean> dataList = bean.getList();
                getV().refreshAdapter(dataList);
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
