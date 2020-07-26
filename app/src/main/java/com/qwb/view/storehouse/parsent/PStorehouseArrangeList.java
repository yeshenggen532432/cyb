package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.storehouse.model.StorehouseArrangeListResult;
import com.qwb.view.storehouse.ui.StorehouseArrangeListActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 库位整理
 */
public class PStorehouseArrangeList extends XPresent<StorehouseArrangeListActivity>{

    /**
     *
     */
    public void queryData(Activity activity, int pageNo, int pageSize, String startDate, String endDate, Integer status, String search) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        if (MyStringUtil.isNotEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (MyStringUtil.isNotEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        if (MyStringUtil.isNotEmpty(search)) {
            params.put("billNo", search);
        }
        if (MyStringUtil.isNotEmpty("" + status)) {
            params.put("status", String.valueOf(status));//状态
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkCrewPage)
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
            StorehouseArrangeListResult parseObject = JSON.parseObject(response,StorehouseArrangeListResult.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<StorehouseInBean> dataList = parseObject.getRows();
                    getV().refreshAdapter(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }



}
