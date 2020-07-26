package com.qwb.view.map.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.map.ui.VisitMapActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.map.model.TrackListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * (员工在线)拜访地图
 */
public class PVisitMap extends XPresent<VisitMapActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFDT_NEW, ConstantUtils.Apply.BFDT_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFDT_NEW, ConstantUtils.Apply.BFDT_OLD);
    /**
     * tag:1-普通查询，2：自动刷新查询
     */
    public void loadData(Activity activity, int pageNo, String entityStr, final int tag) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("entityNms", String.valueOf(entityStr));
        params.put("dataTp", dataTp);//角色
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryMapGjLsClj2)
                .id(tag)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        if(1 == tag){
                            parseJson1(response);
                        }else if(2 == tag){
                            parseJson2(response);
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
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryDepartMemLs)
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



    //解析数据-普通查询
    private void parseJson1(String response) {
        try {
            TrackListBean parseObject = JSONObject.parseObject(response,TrackListBean.class);
            if (parseObject != null && parseObject.isState()) {
                getV().setShowData(parseObject);
            } else {
                ToastUtils.showCustomToast( parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-自动查询
    private void parseJson2(String response) {
        try {
            TrackListBean parseObject = JSONObject.parseObject(response,TrackListBean.class);
            if (parseObject != null && parseObject.isState()) {
                getV().setShowDataAuto(parseObject);
            } else {
                ToastUtils.showCustomToast( parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    //解析数据-结构图：部门，员工
    private void parseJson3(String response) {
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
                        getV().refreshAdapterMemberTree(mDatas);
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
