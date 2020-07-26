package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.storehouse.model.StorehouseStayOutListResult;
import com.qwb.view.storehouse.ui.StorehouseOutFragment;
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
 * 库位：待出仓发票
 */
public class PStorehouseOutFragment extends XPresent<StorehouseOutFragment> {

    private Activity context;

    /**
     *
     */
    public void queryData(Activity activity, String  searchStr, int pageNo, String startDate, String endDate, String outType) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(10));
        params.put("jz", String.valueOf(1));
        params.put("isPay", String.valueOf(-1));
        params.put("khNm", String.valueOf(searchStr));
        params.put("sdate", startDate);// 开始时间
        params.put("edate", endDate);// 结束时间
        params.put("outType", outType);// 出库类型
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryOutPageForCrewOut)
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
            StorehouseStayOutListResult bean = JSON.parseObject(response, StorehouseStayOutListResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().refreshAdapter(bean.getList());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
