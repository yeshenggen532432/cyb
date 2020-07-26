package com.qwb.view.customer.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.RegionBean;
import com.qwb.view.customer.ui.AddClientActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.customer.model.BfCountBean;
import com.qwb.view.customer.model.ClientDetailBean;
import com.qwb.view.customer.model.ClientLevelBean;
import com.qwb.view.customer.model.QdTypeBean;
import com.qwb.view.customer.model.XsjdBean;
import com.qwb.view.customer.model.queryHzfsBean;
import com.qwb.utils.MyHttpUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：添加，修改客户
 */
public class PAddClient extends XPresent<AddClientActivity>{
    /**
     * 获取：1）渠道类型；客户类型 2）客户等级 3）销售阶段 4）拜访次数 5）合作方式
     */
    public void loadData(Activity activity, int popType, Integer qdId) {
        String url=null;
        int tag=0;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        switch (popType) {
            case 1://渠道类型；客户类型
                url= Constans.qdTpye;
                tag=1;
                break;
            case 2://客户等级
                if(qdId != null && qdId != 0){
                    params.put("qdId", qdId.toString());
                }
                url= Constans.Khlevells;
                tag=2;
                break;
            case 3://销售阶段
                url= Constans.Xsphasels;
                tag=3;
                break;
            case 4://拜访次数
                url= Constans.Bfpcls;
                tag=4;
                break;
            case 5:// 合作方式
                url= Constans.queryHzfsls;
                tag=5;
                break;
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(tag)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        if(1 == id){
                            parseJson1(response);
                        }else if(2 == id){
                            parseJson2(response);
                        }else if(3 == id){
                            parseJson3(response);
                        }else if(4 == id){
                            parseJson4(response);
                        }else if(5 == id){
                            parseJson5(response);
                        }

                    }
                });
    }
    /**
     * 获取客户信息（修改）
     */
    public void loadDataClientInfo(Activity activity, String cid) {
        OkHttpUtils
                .post()
                .addParams("token",SPUtils.getTK())
                .addParams("Id", String.valueOf(cid))
                .url(Constans.clientDetail)
                .id(8)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson8(response);
                    }
                });
    }

    /**
     * 添加
     */
    public void addData(Activity activity,int type,String cid,
                        String clientName,String phone,String address,String lat,String lng,
                        String qdType,String clientLevel,Integer clientTypeId, Integer clientLevelId,String xsjd,String bfCount,String hzfs,
                        String lxr,String tel,String date,String remo,int providerId,
                        String province,String city,String area,String fgqy,String regionId) {

        Map<String, String> params = new HashMap<>();
        //必传
        params.put("token", SPUtils.getTK());
        params.put("khNm", clientName);
        params.put("address", address);
        params.put("longitude", lng);
        params.put("latitude", lat);
        // 可以不传
        params.put("mobile", phone);
        params.put("qdtpNm", qdType);// 客户类型
        params.put("khdjNm", clientLevel);// 客户等级
        if(clientTypeId != null && clientTypeId != 0){
            params.put("qdtypeId", clientTypeId + "");// 客户类型
        }else{
            params.put("qdtypeId", "");// 客户类型
        }
        if(clientLevelId != null && clientLevelId != 0){
            params.put("khlevelId", clientLevelId + "");// 客户等级
        }else{
            params.put("khlevelId", "");// 客户等级
        }
        params.put("bfpcNm", bfCount);// 拜访频次
        params.put("xsjdNm", xsjd);// 销售阶段
        params.put("hzfsNm", hzfs);// 合作方式
        params.put("linkman", lxr);// 联系人
        params.put("tel", tel);// 电话
        params.put("openDate", date);// 开户时间
        params.put("remo", remo);// 备注
        params.put("province", String.valueOf(province));
        params.put("city", String.valueOf(city));
        params.put("area", String.valueOf(area));
        params.put("regionId", String.valueOf(regionId));
        params.put("fgqy", fgqy);// 覆盖区域
        if (0!=providerId) {// 供货商id
            params.put("khPid", String.valueOf(providerId));
        }

        // 1：修改客户； 默认（添加客户）
        if (type == 1) {
            params.put("id", String.valueOf(cid));
            OkHttpUtils.post().params(params).url(Constans.updateClient).id(7).build().execute(new MyHttpCallback(activity) {
                @Override
                public void myOnError(Call call, Exception e, int id) {

                }

                @Override
                public void myOnResponse(String response, int id) {
                    parseJson7(response);
                }
            });
        } else {
            OkHttpUtils.post().params(params).url(Constans.addClient).id(6).build().execute(new MyHttpCallback(activity) {
                @Override
                public void myOnError(Call call, Exception e, int tagId) {

                }

                @Override
                public void myOnResponse(String response, int id) {
                    parseJson6(response);
                }
            });
        }
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
                    public void onSuccess(String response, int tagId) {
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
                case 3://销售阶段
                    parseJson3(response);
                    break;
                case 4://拜访次数
                    parseJson4(response);
                    break;
                case 5://合作方式
                    parseJson5(response);
                    break;
                case 6://添加客户
                    parseJson6(response);
                case 7://修改客户
                    parseJson7(response);
                    break;
                case 8://客户信息
                    parseJson8(response);
                    break;
            }
        }
    }

    //解析数据-渠道类型;客户类型
    private void parseJson1(String response) {
        try {
            QdTypeBean parseObject = JSON.parseObject(response, QdTypeBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<QdTypeBean.Qdtypels> qdtypels = parseObject.getQdtypels();
                if(getV()!=null){
                    getV().refreshAdapterQdType(qdtypels);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-客户等级
    private void parseJson2(String response) {
        try {
            ClientLevelBean parseObject = JSON.parseObject(response, ClientLevelBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<ClientLevelBean.ClientLevel> khlevells = parseObject.getKhlevells();
                if(getV()!=null){
                    getV().refreshAdapterClientLevel(khlevells);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-销售阶段
    private void parseJson3(String response) {
        try {
            XsjdBean parseObject = JSON.parseObject(response, XsjdBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<XsjdBean.Xsphasels> datas = parseObject.getXsphasels();
                if(getV()!=null){
                    getV().refreshAdapterXsjd(datas);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-拜访次数
    private void parseJson4(String response) {
        try {
            BfCountBean parseObject = JSON.parseObject(response, BfCountBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<BfCountBean.BfCount> datas = parseObject.getBfpcls();
                if(getV()!=null){
                    getV().refreshAdapterBfCount(datas);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-合作方式
    private void parseJson5(String response) {
        try {
            queryHzfsBean parseObject = JSON.parseObject(response, queryHzfsBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<queryHzfsBean.queryHzfs> datas = parseObject.getHzfsls();
                if(getV()!=null){
                    getV().refreshAdapterHzfs(datas);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-添加客户
    private void parseJson6(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("state")) {
                if(getV()!=null){
                    getV().addSuccess();
                }
            }
            ToastUtils.showCustomToast(jsonObject.getString("msg"));
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-修改客户
    private void parseJson7(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("state")) {
                if(getV()!=null){
                    getV().updateSuccess();
                }
            }
            ToastUtils.showCustomToast(jsonObject.getString("msg"));
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据-客户信息
    private void parseJson8(String response) {
        try {
            ClientDetailBean parseObject = JSON.parseObject(response, ClientDetailBean.class);
            if (parseObject != null && parseObject.isState()) {
                if (parseObject.isState()) {
                    ClientDetailBean.ClientDetail customer = parseObject.getCustomer();
                    if(customer!=null){
                        if(getV()!=null){
                            getV().setClientInfo(customer);
                        }
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
