package com.qwb.view.plan.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.model.PlanLineBean;
import com.qwb.view.plan.ui.PlanLineActivity;
import com.qwb.view.plan.model.PlanLineListResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：计划模块-线路
 */
public class PPlanLine extends XPresent<PlanLineActivity> {

    //查询计划线路
    public void queryData(Activity activity, int pageNo, int pageSize){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("xlNm", "");//计划线路名称(False)
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBscPlanxlWeb)
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

    //添加拜访计划
    public void addData(Activity activity, String id, String date) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("xlId", id);//线路id
        params.put("pdate", date);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.addBscPlanWeb)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    //删除计划线路
    public void delData(Activity activity, String ids) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("ids", ids);//线路ids
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.deleteBscPlanxlWeb)
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


    //解析数据
    private void parseJson1(String response) {
        try {
            PlanLineListResult parseObject = JSON.parseObject(response, PlanLineListResult.class);
            if (parseObject != null && parseObject.isState()) {
                List<PlanLineBean> rows = parseObject.getRows();
                getV().refreshAdapter(rows);
            } else {
                ToastUtils.showCustomToast("" + parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if(bean != null){
                ToastUtils.showCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson3(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if(bean != null){
                if(bean.isState()){
                   getV().delSuccess();
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}



