package com.qwb.view.map.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.map.ui.MapMemberFBTActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.map.model.TrackListBean;
import com.qwb.view.base.model.TreeBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 *  员工分布图
 */
public class PMapMemberFBT extends XPresent<MapMemberFBTActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFDT_NEW, ConstantUtils.Apply.BFDT_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.BFDT_NEW, ConstantUtils.Apply.BFDT_OLD);

    private Activity activity;
    private String mMemberIds;

    /**
     *
     */
    public void queryData(Activity activity,String memberIds) {
        this.mMemberIds = memberIds;
        Map<String, String> params=new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("entityNms", memberIds);
        params.put("dataTp", dataTp);
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryMapGjLsdtClj)
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
    public void queryMemberTree(Activity activity) {
        this.activity = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dataTp", dataTp);// 角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);// 角色
        }
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryDepartMemLs)
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


    //解析数据-普通查询
    private void parseJson1(String response) {
        try {
            TrackListBean parseObject2 = JSONObject.parseObject(response, TrackListBean.class);
            if (parseObject2 != null) {
                if (parseObject2.isState()) {
                    List<TrackListBean.TrackList> rows = parseObject2.getRows();
                    if(getV()!=null){
                        getV().showData(rows, mMemberIds, false);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject2.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-结构图：部门，员工
    private void parseJson2(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<>();
            mDatas.clear();

            BranchListBean2 parseObject = JSON.parseObject(response, BranchListBean2.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<BranchBean> list = parseObject.getList();
                    String mMemberIds = "";
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            // 第一层
                            BranchBean branchBean = list.get(i);
                            Integer branchId = branchBean.getBranchId();
                            String branchName = branchBean.getBranchName();
                            if (branchId != null && branchName != null) {
                                // 第二层
                                List<MemberBean> memls2 = branchBean.getMemls2();
                                if (memls2 != null && memls2.size() > 0) {
                                    for (int j = 0; j < memls2.size(); j++) {
                                        MemberBean memberBean = memls2.get(j);
                                        Integer memberId = memberBean.getMemberId();
                                        String memberNm = memberBean.getMemberNm();
                                        if (memberId != null && memberNm != null) {
                                            if(MyStringUtil.isEmpty(mMemberIds)){
                                                mMemberIds += memberId;
                                            }else{
                                                mMemberIds += "," + memberId;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    this.queryData(activity, mMemberIds);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
