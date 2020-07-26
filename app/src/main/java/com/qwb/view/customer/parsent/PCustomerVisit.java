package com.qwb.view.customer.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.customer.model.CustomerVisitResult;
import com.qwb.view.customer.ui.CustomerVisitActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.KhtypeAndKhlevellBean;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.utils.MyHttpUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 客户拜访（我的拜访）
 */
public class PCustomerVisit extends XPresent<CustomerVisitActivity> {
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);


    public void queryData(Activity activity, String search, String startDate, String endDate, String customerId, String visitId, String memberIds) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(1));
        params.put("pageSize", "10");
        params.put("khNm", search);
        params.put("sdate", startDate);
        params.put("edate", endDate);
        params.put("dataTp", dataTp);
        params.put("cid", customerId);
        params.put("id", visitId);// 拜访id
        if (!MyUtils.isEmptyString(memberIds)) {
            params.put("mids", memberIds);// 角色
        }
        OkHttpUtils.post().params(params).url(Constans.queryBfkhLsWeb2).id(1).build().execute(new MyHttpCallback(activity) {
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
     * 获取结构图：客户类型
     */
    public void queryDataCustomerType(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryKhFlJbls)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson3(response);
                    }
                });
    }


    /**
     * 部门，员工
     */
    public void queryDataMember(Activity activity) {
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
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            CustomerVisitResult bean = JSON.parseObject(response, CustomerVisitResult.class);
            if (bean != null && bean.isState()) {
                getV().refreshAdapter(bean);
            } else {
                ToastUtils.showCustomToast("" + bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //解析数据-结构图：客户等级，客户类型
    private void parseJson3(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<TreeBean>();
            HashMap<Integer, String> map = new HashMap<Integer, String>();
            mDatas.clear();
            map.clear();

            KhtypeAndKhlevellBean parseObject2 = JSON.parseObject(response, KhtypeAndKhlevellBean.class);
            if (parseObject2 != null && parseObject2.isState()) {
                List<KhtypeAndKhlevellBean.Khlevells> khlevells = parseObject2.getKhlevells();
                List<KhtypeAndKhlevellBean.Qdtypels> qdtypels = parseObject2.getQdtypels();

                TreeBean fileBean = new TreeBean(-111111, -1, "客户类型");// //（父）
                mDatas.add(fileBean);
                TreeBean fileBean2 = new TreeBean(-222222, -1, "客户级别");// //（父）
                mDatas.add(fileBean2);

                if (qdtypels != null && qdtypels.size() > 0) {// 加1000避免上面重复：客户类型（子）
                    for (int j = 0; j < qdtypels.size(); j++) {
                        KhtypeAndKhlevellBean.Qdtypels qdtypels2 = qdtypels.get(j);
                        TreeBean fileBean3 = new TreeBean(qdtypels2.getId(), -111111, qdtypels2.getQdtpNm());
                        mDatas.add(fileBean3);
                        map.put(qdtypels2.getId(), qdtypels2.getQdtpNm());
                    }
                }

                if (khlevells != null && khlevells.size() > 0) {// 客户级别（子）
                    for (int j = 0; j < khlevells.size(); j++) {
                        KhtypeAndKhlevellBean.Khlevells khlevells2 = khlevells.get(j);
                        TreeBean fileBean3 = new TreeBean(khlevells2.getId() + ConstantUtils.TREE_ID, -222222, khlevells2.getKhdjNm());
                        mDatas.add(fileBean3);
                        map.put(khlevells2.getId() + ConstantUtils.TREE_ID, khlevells2.getKhdjNm());
                    }
                }
                if (getV() != null) {
//                    getV().showDialogCustomerType(qdtypels);
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
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

                    if (getV() != null) {
//                        getV().showDialogMember(mDatas);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}



