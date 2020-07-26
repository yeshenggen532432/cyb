package com.qwb.view.plan.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.model.PlanLineBean;
import com.qwb.view.plan.ui.PlanMineFragment;
import com.qwb.view.plan.model.PlanLineListResult;
import com.qwb.view.plan.model.PlanMineResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 计划拜访
 */
public class PPlanMineFragment extends XPresent<PlanMineFragment>{

    public void queryDataNewPlan(Activity activity, int pageNo, int pageSize, String date, String mid){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("pdate", date);// 计划时间
        params.put("mid", mid);//成员ids
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBscPlanNewWeb)
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

    //查询线路
    public void queryDataPlanLine(Activity activity){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", String.valueOf(1000));
//        params.put("mid", String.valueOf(mid));
//        params.put("xlNm", "");//计划线路名称(False)
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBscPlanxlWeb)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }

    //添加拜访计划
    public void addDataPlan(Activity activity, String id, String date) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("xlId", id);//线路id
        params.put("pdate", date);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.addBscPlanNewWeb)
                .id(4)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }



    //解析数据
    private void parseJson1(String response) {
        try {
            PlanMineResult parseObject = JSON.parseObject(response, PlanMineResult.class);
            if (parseObject != null && parseObject.isState()) {
                getV().refreshAdapter(parseObject);
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //查询线路
    private void parseJson3(String response) {
        try {
            PlanLineListResult parseObject = JSON.parseObject(response, PlanLineListResult.class);
            if (parseObject != null && parseObject.isState()) {
                List<PlanLineBean> rows = parseObject.getRows();
                getV().showDialogChooseLine(rows);
            } else {
                ToastUtils.showCustomToast("" + parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson4(String response) {
        try {
            BaseBean parseObject = JSON.parseObject(response, BaseBean.class);
            if (parseObject != null && parseObject.isState()) {
                getV().addSuccess();
            } else {
                ToastUtils.showCustomToast("" + parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}
