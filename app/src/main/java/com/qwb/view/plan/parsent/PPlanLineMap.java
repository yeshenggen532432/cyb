package com.qwb.view.plan.parsent;

import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.KhtypeAndKhlevellBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.plan.model.PlanNearCustomerResult;
import com.qwb.view.plan.ui.PlanLineMapActivity;
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
 * 线路地图
 */
public class PPlanLineMap extends XPresent<PlanLineMapActivity> {
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);

    //查询线路的周边客户
    public void queryDataNearCustomer(Activity activity, String customerJson, String customerType){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("customerJson", String.valueOf(customerJson));
        params.put("dataTp", dataTp);
        if (MyStringUtil.isNotEmpty(customerType)) {// 客户类型:"零售","连锁","餐饮"
            params.put("customerType", customerType);
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryNearCustomerListByLatLng)
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



    //解析数据
    private void parseJson1(String response) {
        try {
            PlanNearCustomerResult parseObject = JSON.parseObject(response, PlanNearCustomerResult.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> list = parseObject.getList();
                getV().doNearCustomer(list, true);
            } else {
                ToastUtils.showCustomToast("" + parseObject.getMsg());
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
                    getV().doCustomerType(qdtypels);
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }




}



