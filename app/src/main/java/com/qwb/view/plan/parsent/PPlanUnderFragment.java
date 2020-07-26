package com.qwb.view.plan.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.plan.model.PlanUnderListResult;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.model.PlanLineBean;
//import com.qwb.view.plan.model.PlanUnderListResult;
import com.qwb.view.plan.ui.PlanUnderFragment;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.plan.model.PlanLineListResult;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.plan.model.PlanMineResult;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 计划拜访--下属拜访
 */
public class PPlanUnderFragment extends XPresent<PlanUnderFragment>{

    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);

    public void queryDataNewUnderlingPlan(Activity activity, int pageNo, int pageSize, String date, String mids){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        params.put("pdate", date);// 计划时间
        params.put("mid", SPUtils.getID());// 过滤自己id
        params.put("dataTp", dataTp);// 角色
        if(MyStringUtil.isEmpty(mids)){
            if ("4".equals(dataTp)) {
                params.put("mids", dataTpMids);// 角色
            }
        }else{
            params.put("mids", mids);// 角色
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBscPlanNewUnderlingWeb)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            PlanUnderListResult bean = JSON.parseObject(response, PlanUnderListResult.class);
                            if(bean == null){
                                return;
                            }
                            if(bean.isState()){
                                getV().refreshAdapterUnderling(bean.getRows());
                            }else {
                                ToastUtils.showCustomToast(bean.getMsg());
                            }
                        } catch (Exception e) {
                            ToastUtils.showError(e);
                        }
                    }
                });

    }


    /**
     * 获取结构图：部门，员工
     */
    public void loadDataFrame(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dataTp", dataTp);//角色
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryDepartMemLs)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    //查询线路
    public void queryDataPlanLine(Activity activity, int mid){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", String.valueOf(1000));
        params.put("mid", String.valueOf(mid));
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
//                getV().refreshAdapter(parseObject);
            } else {
                ToastUtils.showCustomToast("" + parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-结构图：部门，员工
    private void parseJson2(String response) {
        try {
            List<MemberBean> memberBeans = new ArrayList<>();
            memberBeans.clear();

            BranchListBean2 bean = JSON.parseObject(response, BranchListBean2.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<BranchBean> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            // 第一层
                            BranchBean branchBean = list.get(i);
                            Integer branchId = branchBean.getBranchId();
                            String branchName = branchBean.getBranchName();
                            if (branchId != null && !MyStringUtil.isEmpty(branchName)) {

                                // 第二层
                                List<MemberBean> members = branchBean.getMemls2();
                                if (members != null && members.size() > 0) {
                                    for (int j = 0; j < members.size(); j++) {
                                        MemberBean memberBean = members.get(j);
                                        memberBeans.add(memberBean);
                                    }
                                }
                            }
                        }
                    }

                    if(getV()!=null){
                        getV().refreshAdapterLeft(memberBeans);
                    }
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //查询线路
    private void parseJson3(String response) {
        try {
            PlanLineListResult parseObject = JSON.parseObject(response, PlanLineListResult.class);
            if (parseObject != null && parseObject.isState()) {
                List<PlanLineBean> rows = parseObject.getRows();
                getV().refreshAdapterRight(rows);
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
//                getV().addSuccess();
            } else {
                ToastUtils.showCustomToast("" + parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

}
