package com.qwb.view.plan.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.plan.model.PlanLineDetailBean;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.ui.PlanEditLineActivity;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.plan.model.PlanLineResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：计划模块-编辑线路
 */
public class PPlanEditLine extends XPresent<PlanEditLineActivity> {

    /**
     * 添加线路
     */
    public void addData(Activity activity, String name, String cids) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("xlNm", name);//线路名称
        params.put("cids", cids);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.addBscPlanxlWeb)
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

    // 查询线路详情
    public void queryData(Activity activity, int pageNo, int pageSize, int id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("xlId", String.valueOf(id));// 线路id
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBscPlanxlDetailWeb)
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

    /**
     * 修改线路
     */
    public void updateData(Activity activity, int id, String cids, String xlNm) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(id));
        params.put("xlNm", String.valueOf(xlNm));
        params.put("cids", cids);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateBscPlanxlWeb2)
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
            if(bean != null){
                if(bean.isState()){
                    getV().addSuccess();
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //解析数据
    private void parseJson2(String response) {
        try {
            PlanLineResult bean = JSON.parseObject(response, PlanLineResult.class);
            if (bean != null) {
                if(bean.isState()){
                    ArrayList<MineClientInfo> mineClientList = new ArrayList<>();
                    List<PlanLineDetailBean> rows = bean.getRows();
                    for (PlanLineDetailBean detail : rows) {
                        MineClientInfo clientInfo = new MineClientInfo();
                        clientInfo.setId(Integer.valueOf(detail.getCid()));
                        clientInfo.setKhNm(detail.getKhNm());
                        clientInfo.setLatitude(detail.getLatitude());
                        clientInfo.setLongitude(detail.getLongitude());
                        mineClientList.add(clientInfo);
                    }
                    getV().refreshAdapter(mineClientList);
                }else{
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}



