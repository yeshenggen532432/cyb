package com.qwb.view.audit.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.view.audit.ui.AuditDetailActivity;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.audit.model.ShenPiDetialBean;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.base.model.TreeBean;
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
 * 审批详情
 */
public class PAuditDetail extends XPresent<AuditDetailActivity>{

    private Activity mActivity;
    private String id;
    public void queryData(Activity activity, String id) {
        this.mActivity = activity;
        this.id = id;

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("auditNo", id);
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryAuditDetailURL)
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
     * 审批操作 checkTp（操作类型 2 同意 3 拒绝4 转发,6 退回）, int memId（转发的对象,当checkTp=4时必传）
     * */
    public void updateStatusShenpi(Activity activity, String id, String checkTp, String dsc, String memId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("auditNo", id);
        params.put("checkTp", checkTp);
        if (!MyStringUtil.isEmpty(memId)) {
            params.put("memId", memId);// 转发的时候用到
        }
        if (!MyStringUtil.isEmpty(dsc)) {
            params.put("dsc", dsc);
        }
        OkHttpUtils.post().params(params).url(Constans.toCheckURL).id(2).build()
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
     * 撤销审批
     * */
    public void upDateStatusCancel(Activity activity, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("auditNo", id);
        OkHttpUtils.post().params(params).url(Constans.cancelAuditURL).id(2).build()
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
     * 执行完成
     * */
    public void updateAuditExecStatus(Activity activity, String id) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("auditNo", id);
        OkHttpUtils.post().params(params).url(Constans.updateAuditExecStatus).id(3).build()
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
     * 查询部门，员工
     */
    public void queryMemberList(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("memberNm", "");
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryCompanyMemberList)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson30(response);
                    }
                });
    }



    //解析数据
    private void parseJson1(String response) {
        try {
            ShenPiDetialBean bean = JSON.parseObject(response ,ShenPiDetialBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().doUI(bean);
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
           BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    queryData(mActivity, id);
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-结构图：部门，员工
    private void parseJson30(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<>();
            mDatas.clear();
            MemberListBean bean = JSON.parseObject(response, MemberListBean.class);
            if (bean != null && bean.isState()) {
                List<MemberListBean.MemberBean> dataList = bean.getList();
                for (MemberListBean.MemberBean memberBean:dataList) {
                    Integer memberId = memberBean.getMemberId();// 默认+10万--防止父ID与子ID重复
                    String memberNm = memberBean.getMemberNm();
                    if (memberId != null && memberNm != null) {
                        mDatas.add(new TreeBean(memberId, 0, memberNm));
                    }
                }
                if(getV()!=null){
                    getV().showDialogMember(mDatas);
                }
            } else {
                ToastUtils.showCustomToast(bean.getMsg());
            }

        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


}
