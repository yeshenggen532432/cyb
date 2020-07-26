package com.qwb.view.log.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.log.ui.LogTableActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.log.model.RizhiListBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：工作汇报-报表
 * 创建作者：yeshenggen
 * 创建时间：2018/05/25
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PLogTable extends XPresent<LogTableActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.GZHB_NEW, ConstantUtils.Apply.GZHB_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.GZHB_NEW, ConstantUtils.Apply.GZHB_OLD);


    /**
     * 获取：工作汇报-报表
     */
    public void loadData(Activity activity, int pageNo, int type, String startDate, String endDate, String mIds, String isWork) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("tp", String.valueOf(type));
        if(!MyUtils.isEmptyString(startDate)){
            params.put("sdate", startDate);
        }
        if(!MyUtils.isEmptyString(endDate)){
            params.put("edate", endDate);
        }
        if(!MyUtils.isEmptyString(mIds)){
            params.put("fsMids", mIds);//选择汇报人ids
        }
        params.put("yw", isWork);//1:有 2：无--是否有写“日报”，“周报”，“月报”

        XLog.e("pageNo",String.valueOf(pageNo));
        XLog.e("tp",String.valueOf(type));
        XLog.e("sdate",String.valueOf(startDate));
        XLog.e("edate",String.valueOf(endDate));
        XLog.e("fsMids",String.valueOf(mIds));
        XLog.e("yw",String.valueOf(isWork));

        params.put("dataTp", dataTp);//角色
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryReportWebPage1)
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
        params.put("dataTp", dataTp);//角色
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//角色
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


    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://渠道类型
                    parseJson1(response);
                    break;
                case 2://客户等级
                    parseJson2(response);
                    break;
            }
        }
    }

    //解析数据-渠道类型;客户类型
    private void parseJson1(String response) {
        try {
            RizhiListBean parseObject = JSON.parseObject(response,RizhiListBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<RizhiListBean.RizhiList> rows = parseObject.getRows();
                if(getV()!=null){
                    getV().showData(rows);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }

        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
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
                        getV().refreshAdapterFrame(mDatas,true);
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
