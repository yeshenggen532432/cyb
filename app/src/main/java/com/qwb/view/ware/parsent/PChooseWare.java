package com.qwb.view.ware.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.ware.ui.ChooseWareActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.ware.model.QueryStkWareType;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.base.model.TreeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：选择商品
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PChooseWare extends XPresent<ChooseWareActivity> {
    /**
     * * 获得商品分类列表
     */
    public void queryDataWareTypes(Activity activity, String stkId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
//        params.put("customerId", cid);
//        if (!MyStringUtil.isEmpty(noCompany)) {
//            params.put("noCompany", noCompany);
//        }
        if (!MyStringUtil.isEmpty(stkId)) {
            params.put("stkId", stkId);
        }

        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryStkWareType, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    /**
     * 查询商品(根据商品分类)
     */
    public void queryDataByWareType(Activity context, String wareType, String stkId, int pageNo, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("wareType", wareType);
//        params.put("customerId", cid);
//        if (!MyStringUtil.isEmpty(noCompany)) {
//            params.put("noCompany", noCompany);
//        }
        if (!MyStringUtil.isEmpty(stkId)) {
            params.put("stkId", stkId);
        }

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));

        MyHttpUtil.getInstance()
                .post(context, params, Constans.queryStkWare, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2345(response);
                    }
                });
    }

    /**
     * 查询商品(根据关键字)
     */
    public void queryDataByKeyWord(Activity context, String keyWord, String stkId, int pageNo, int pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
//        params.put("customerId", cid);
//        if (!MyStringUtil.isEmpty(noCompany)) {
//            params.put("noCompany", noCompany);
//        }
        if (!MyStringUtil.isEmpty(stkId)) {
            params.put("stkId", stkId);
        }

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));

        MyHttpUtil.getInstance()
                .post(context, params, Constans.queryStkWare1, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2345(response);
                    }
                });
    }


    //------------------------------------------------------------------------------------------------------------

    //解析数据-商品类型
    private void parseJson1(String response) {
        try {
            QueryStkWareType bean = JSON.parseObject(response, QueryStkWareType.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<TreeBean> mDatas = new ArrayList<>();
                    mDatas.clear();
                    List<QueryStkWareType.List1> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        // 第一层
                        for (int i = 0; i < list.size(); i++) {
                            QueryStkWareType.List1 list1 = list.get(i);
                            int id_1 = list1.getWaretypeId();
                            mDatas.add(new TreeBean(id_1, 0, list1.getWaretypeNm()));
                            // 第二层
                            List<QueryStkWareType.List2> list2 = list1.getList2();
                            if (list2 != null && list2.size() > 0) {
                                for (int z = 0; z < list2.size(); z++) {
                                    QueryStkWareType.List2 list2_2 = list2.get(z);
                                    int id_2 = list2_2.getWaretypeId();
                                    mDatas.add(new TreeBean(id_2, id_1, list2_2.getWaretypeNm()));
                                }
                            }
                        }
                        if (getV() != null) {
                            // 刷新Tree
                            getV().doAdapterLeft(mDatas);
                        }
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2345(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if (getV() != null) {
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        getV().refreshAdapterRight(list);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }



}
