package com.qwb.view.object.parsent;

import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.object.model.FinUnitListBean;
import com.qwb.view.object.ui.ObjectFinUnitFragment;
import com.qwb.utils.MyHttpUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 其他往来单位
 */
public class PObjectFinUnit extends XPresent<ObjectFinUnitFragment>{

    /**
     */
    public void queryFinUnitPage(Activity activity, String search, Integer pageNo, Integer pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("proName", String.valueOf(search));
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryFinUnitPage)
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
            FinUnitListBean bean = JSON.parseObject(response, FinUnitListBean.class);
            if (bean != null && bean.isState()) {
                List<FinUnitListBean.FinUnitBean> dataList = bean.getList();
                getV().refreshAdapter(dataList);
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
