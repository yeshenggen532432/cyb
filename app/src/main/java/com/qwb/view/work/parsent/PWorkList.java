package com.qwb.view.work.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.work.ui.WorkListActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyHttpUtil;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.work.model.WorkListResult;
import com.qwb.view.work.model.WorkSubBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 考勤列表
 */
public class PWorkList extends XPresent<WorkListActivity>{

    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KQ_NEW, ConstantUtils.Apply.KQ_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KQ_NEW, ConstantUtils.Apply.KQ_OLD);

    /**
     * */
    public void queryData(Activity activity, int pageNo, int pageSize, String mids, String sdate, String edate){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("sdate", sdate);
        params.put("edate", edate);
        params.put("dataTp", dataTp);
        if(!MyUtils.isEmptyString(mids)){
            params.put("mids", mids);//用户ids 如：2,3,4
        }else{
            if("4".equals(dataTp)){
                params.put("mids", dataTpMids);//角色
            }
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.newCheckinlist3)
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

    /**
     * 获取结构图：部门，员工
     */
    public void loadDataFrame(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dataTp", dataTp);// 角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);// 角色
        }

        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryDepartMemLs)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        closeRefresh();
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response);
                    }
                });
    }


    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            WorkListResult parseObject = JSON.parseObject(response, WorkListResult.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<WorkSubBean> kaoqianItemList=new ArrayList<>();

                    List<WorkListResult.KaoqianListBean> rows = parseObject.getRows();
                    if (rows != null && rows.size() > 0) {
                        for (int i = 0; i < rows.size(); i++) {
                            WorkListResult.KaoqianListBean kaoqianListBean = rows.get(i);
                            String memberName = kaoqianListBean.getMemberName();//员工名称--下级没有，赋值给下级
                            List<WorkSubBean> list = kaoqianListBean.getList();
                            for (int j = 0; j < list.size(); j++) {
                                WorkSubBean kaoqianItemBean = list.get(j);
                                kaoqianItemBean.setMemberName(memberName);
                                kaoqianItemList.add(kaoqianItemBean);
                            }
                        }
                    }
                    getV().refreshAdapter(kaoqianItemList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-结构图：部门，员工
    private void parseJson2(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<TreeBean>();
            mDatas.clear();

            BranchListBean2 parseObject = JSON.parseObject(response, BranchListBean2.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<BranchBean> list = parseObject.getList();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            // 第一层
                            BranchBean branchBean = list.get(i);
                            Integer branchId = branchBean.getBranchId();
                            String branchName = branchBean.getBranchName();
                            if (branchId != null && branchName != null) {
                                TreeBean fileBean = new TreeBean(branchId, -1, branchName);
                                if (fileBean != null) {
                                    mDatas.add(fileBean);
                                }
                                Constans.branchMap.put(branchId, branchBean);// 父

                                // 第二层
                                List<MemberBean> memls2 = branchBean.getMemls2();
                                if (memls2 != null && memls2.size() > 0) {
                                    for (int j = 0; j < memls2.size(); j++) {
                                        MemberBean memberBean = memls2.get(j);
                                        Integer memberId = memberBean.getMemberId() + ConstantUtils.TREE_ID;// 默认+10万--防止父ID与子ID重复
                                        String memberNm = memberBean.getMemberNm();
                                        if (memberId != null && memberNm != null) {
                                            mDatas.add(new TreeBean(memberId, branchId, memberNm));
                                            Constans.memberMap.put(memberId, memberBean);// 子
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if(getV()!=null){
                        getV().refreshAdapterFrame(mDatas);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
