package com.qwb.view.object.parsent;

import android.app.Activity;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.KhtypeAndKhlevellBean;
import com.qwb.view.customer.model.MineClientBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.customer.model.RegionBean;
import com.qwb.view.member.model.BranchBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.member.model.BranchListBean2;
import com.qwb.view.object.ui.ObjectCustomerFragment;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 对象客户
 */
public class PObjectCustomer extends XPresent<ObjectCustomerFragment>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);

    /**
     */
    public void queryData(Activity activity, int pageNo, double latitude, double longitude, String pxtp, EditText etSearch, String entityStr,
                          String clientType, String clientLevel, String mRegionIds) {
        String searchStr=etSearch.getText().toString().trim();
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        params.put("pxtp", pxtp);// 排序类型1距离，2拜访时间
        params.put("dataTp", dataTp);
        if(!MyStringUtil.isEmpty(entityStr)){
            params.put("mids", entityStr);//员工ids
        }else {
            if("4".equals(dataTp)){
                params.put("mids", dataTpMids);//员工ids
            }
        }
        params.put("khNm", searchStr);
        if (!MyUtils.isEmptyString(clientType)) {// 客户类型:"零售","连锁","餐饮"
            params.put("qdtpNms", clientType);
        }
        if (!MyUtils.isEmptyString(clientLevel)) {// 客户级别:"A","C","B"
            params.put("khdjNms", clientLevel);
        }
        if (!MyUtils.isEmptyString(mRegionIds)) {// 客户级别:"A","C","B"
            params.put("regionIds", mRegionIds);
        }

        MyHttpUtil.getInstance()
                .post(activity, params, Constans.mineClient)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        closeRefresh();
                    }

                    @Override
                    public void onSuccess(String response, int id) {
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
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response);
                    }
                });
    }
    /**
     * 客户所属区域
     */
    public void queryRegionTree(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryRegionTree)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson6(response);
                    }
                });
    }
    /**
     * 获取结构图：客户等级，客户类型
     */
    public void loadDataFrameClient(Activity activity) {
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
     * 删除客户
     */
    public void delDataClient(Activity activity,String cid) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", cid);// 客户id

        MyHttpUtil.getInstance()
                .post(activity, params, Constans.deleteCustomerWeb)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson4(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------


    //解析数据-我的客户
    private void parseJson1(String response) {
        try {
            MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> dataList = parseObject.getRows();
                getV().refreshAdapter(dataList,parseObject.getTotal());//列表数量，我的客户数量
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
                        getV().showDialogMember(mDatas);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-结构图：客户等级，客户类型
    private void parseJson3(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<TreeBean>();
            HashMap<Integer, String> map = new HashMap<Integer, String>();
            mDatas.clear();
            map.clear();

            KhtypeAndKhlevellBean parseObject2 = JSON.parseObject(response,KhtypeAndKhlevellBean.class);
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
                        TreeBean fileBean3 = new TreeBean(qdtypels2.getId(),-111111, qdtypels2.getQdtpNm());
                        mDatas.add(fileBean3);
                        map.put(qdtypels2.getId(), qdtypels2.getQdtpNm());
                    }
                }

                if (khlevells != null && khlevells.size() > 0) {// 客户级别（子）
                    for (int j = 0; j < khlevells.size(); j++) {
                        KhtypeAndKhlevellBean.Khlevells khlevells2 = khlevells.get(j);
                        TreeBean fileBean3 = new TreeBean(khlevells2.getId() + ConstantUtils.TREE_ID, -222222,khlevells2.getKhdjNm());
                        mDatas.add(fileBean3);
                        map.put(khlevells2.getId() + ConstantUtils.TREE_ID,khlevells2.getKhdjNm());
                    }
                }
                if(getV()!=null){
                    getV().refreshAdapterFrameClient(mDatas,map,true);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-删除客户
    private void parseJson4(String response) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject != null) {
                Boolean state = jsonObject.getBoolean("state");
                if (state != null && state) {
                    if(getV()!=null){
                        getV().refreshAdapterDelSuccess();
                    }
                }
                ToastUtils.showCustomToast(jsonObject.getString("msg"));
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-我的客户(全部用于缓存)
    private void parseJson5(String response) {
        try {
            MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> dataList = parseObject.getRows();
                if(dataList!=null && !dataList.isEmpty()){
                    MyDataUtils.getInstance().saveMineClient(dataList);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
        }
    }

    //客户所属区域
    private void parseJson6(String response) {
        try {
            List<TreeBean> mDatas = new ArrayList<TreeBean>();
            mDatas.clear();

            RegionBean bean = JSON.parseObject(response, RegionBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    //第一层
                    List<RegionBean.Region> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            RegionBean.Region region = list.get(i);
                            Integer id = region.getRegionId();
                            Integer pid = region.getRegionPid();
                            String name = region.getRegionNm();
                            TreeBean treeBean = new TreeBean(id, pid, name);
                            mDatas.add(treeBean);

                            //第二层
                            List<RegionBean.Region> list2 = region.getList2();
                            if (list2 != null && list2.size() > 0) {
                                for (int j = 0; j < list2.size(); j++) {
                                    RegionBean.Region region2 = list.get(j);
                                    Integer id2 = region2.getRegionId();
                                    Integer pid2 = region2.getRegionPid();
                                    String name2 = region2.getRegionNm();
                                    TreeBean treeBean2 = new TreeBean(id2, pid2, name2);
                                    mDatas.add(treeBean2);
                                }
                            }
                        }
                    }

                    if(getV() != null){
                        getV().showDialogRegion(mDatas);
                    }
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    private void closeRefresh(){
        if(null != getV()){
            //关闭刷新
            getV().closeRefresh();
        }
    }
}
