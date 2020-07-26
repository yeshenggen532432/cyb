package com.qwb.view.checkstorage.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.checkstorage.model.StkWareBean;
import com.qwb.view.checkstorage.model.StkWareListBean;
import com.qwb.view.checkstorage.ui.XStkWareActivity;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.ware.model.QueryStkWareType;
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
 * 创建描述：仓库商品
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PxStkWare extends XPresent<XStkWareActivity>{

    public void queryStorageWarePage(Activity activity, int stkId, int wareType, int pageNo, int pageSize){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", String.valueOf(stkId));
        params.put("waretype", String.valueOf(wareType));
        params.put("isType", String.valueOf(0));
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStorageWarePage)
                .id(4)
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
     * * 获得商品分类列表（不包括非公司商品）
     */
    public void queryDataCompanyWareTypes(Activity activity, String cid, String noCompany, String stkId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("customerId", cid);
        if(!MyStringUtil.isEmpty(noCompany)){
            params.put("noCompany", noCompany);
        }
        if(!MyStringUtil.isEmpty(stkId)){
            params.put("stkId", stkId);
        }


        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryStkWareType, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response);
                    }
                });
    }



    //TODO ------------------------接口回調----------------------

    //解析数据--搜索
    private void parseJson1(String response) {
        try {
            StkWareListBean parseObject = JSON.parseObject(response, StkWareListBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<StkWareBean> list = parseObject.getList();
                        getV().refreshAdapter(list);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-商品类型
    private void parseJson2(String response) {
        try {
            QueryStkWareType bean = JSON.parseObject(response, QueryStkWareType.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<TreeBean> mDatas = new ArrayList<TreeBean>();
                    mDatas.clear();
                    List<QueryStkWareType.List1> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        // 默认添加“常售商品”，“收藏商品”
                        // 第一层
                        for (int i = 0; i < list.size(); i++) {
                            QueryStkWareType.List1 list1 = list.get(i);
                            int id_1 = list1.getWaretypeId();
                            mDatas.add(new TreeBean(id_1, 0, list1.getWaretypeNm()));
                            //这边只加载第一层

//                            // 第二层
//                            List<QueryStkWareType.List2> list2 = list1.getList2();
//                            if (list2 != null && list2.size() > 0) {
//                                for (int z = 0; z < list2.size(); z++) {
//                                    QueryStkWareType.List2 list2_2 = list2.get(z);
//                                    int id_2 = list2_2.getWaretypeId();
//                                    mDatas.add(new TreeBean(id_2, id_1, list2_2.getWaretypeNm()));
//                                }
//                            }
                        }
                        if(getV()!=null){
                            // 刷新Tree
                            getV().refreshAdapterTree(mDatas);
                        }
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }




}
