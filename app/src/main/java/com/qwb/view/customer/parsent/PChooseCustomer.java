package com.qwb.view.customer.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.KhtypeAndKhlevellBean;
import com.qwb.view.customer.model.RegionBean;
import com.qwb.view.customer.ui.ChooseCustomerActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyHttpUtil;
import com.qwb.view.customer.model.MineClientBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：选择客户
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PChooseCustomer extends XPresent<ChooseCustomerActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);

    /**
     */
    public void queryDataMine(Activity activity, int pageNo, double latitude, double longitude, String searchStr, String clientType, String clientLevel, String mRegionIds) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        params.put("pxtp", "1");// 排序类型1距离，2拜访时间
        params.put("dataTp", dataTp);
        if("4".equals(dataTp)){
            params.put("mids", dataTpMids);//员工ids
        }
        params.put("khNm", searchStr);
        if (!MyUtils.isEmptyString(clientType)) {// 客户类型:"零售","连锁","餐饮"
            params.put("qdtpNms", clientType);
        }
        if (!MyUtils.isEmptyString(clientLevel)) {// 客户级别:"A","C","B"
            params.put("khdjNms", clientLevel);
        }
        if (!MyUtils.isEmptyString(mRegionIds)) {//区域
            params.put("regionIds", mRegionIds);
        }


        MyHttpUtil.getInstance()
                .post(activity, params, Constans.mineClient)
                .setResultListener(new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        closeRefresh();
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    /**
     * 加载周边客户
     */
    public void queryDataNear(Activity activity, int pageNo, double latitude, double longitude, String searchStr, String clientType, String clientLevel, String mRegionIds) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("latitude", String.valueOf(latitude));
        params.put("longitude", String.valueOf(longitude));
        params.put("pxtp", "1");// 排序类型1距离，2拜访时间
        params.put("dataTp", dataTp);
        params.put("mids", "");//员工ids
        params.put("khNm", searchStr);
        if (!MyUtils.isEmptyString(clientType)) {// 客户类型:"零售","连锁","餐饮"
            params.put("qdtpNms", clientType);
        }
        if (!MyUtils.isEmptyString(clientLevel)) {// 客户级别:"A","C","B"
            params.put("khdjNms", clientLevel);
        }
        if (!MyUtils.isEmptyString(mRegionIds)) {//区域
            params.put("regionIds", mRegionIds);
        }

        OkHttpUtils.post()
                .params(params)
                .url(Constans.nearClient)
                .id(1)
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
//                        closeRefresh();
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson3(response);
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


    //解析数据-我的客户
    private void parseJson1(String response) {
        try {
            MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> dataList = parseObject.getRows();
                getV().refreshAdapterMine(dataList);//列表数量，我的客户数量
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-周边客户
    private void parseJson2(String response) {
        try {
            MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> dataList = parseObject.getRows();
                getV().refreshAdapterNear(dataList);//列表数量，我的客户数量
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
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







}
