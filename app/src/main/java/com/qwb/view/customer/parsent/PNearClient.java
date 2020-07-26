package com.qwb.view.customer.parsent;


import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.customer.ui.NearClientFragment;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.customer.model.NearClientBean;
import com.qwb.view.customer.model.NearClientInfo;
import com.qwb.view.member.model.BranchListBean2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：周边客户
 * 创建作者：yeshenggen
 * 创建时间：2018/05/14
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PNearClient extends XPresent<NearClientFragment>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);

    /**
     *
     */
    public void loadData(Activity context, int pageNo, double latitude, double longitude, String pxtp, EditText etSearch, String entityStr) {
        String searchStr=etSearch.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        params.put("pxtp", pxtp);// 排序类型1距离，2拜访时间
        params.put("khNm", searchStr);
        params.put("dataTp", dataTp);//角色
        params.put("mids", entityStr);//角色
        OkHttpUtils.post().params(params).url(Constans.nearClient).id(1).build().execute(new MyHttpCallback(context) {
            @Override
            public void myOnError(Call call, Exception e, int id) {
                if(getV()!=null){
                    //关闭刷新
                    getV().closeRefresh();
                }
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
    public void loadDataFrame(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("dataTp", dataTp);//角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);//角色
        }

        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryDepartMemLs)
                .id(2)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    //解析数据-我的客户
    private void parseJson1(String response) {
        try {
            NearClientBean parseObject = JSON.parseObject(response, NearClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<NearClientInfo> dataList = parseObject.getRows();
                getV().refreshAdapter(dataList);
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
