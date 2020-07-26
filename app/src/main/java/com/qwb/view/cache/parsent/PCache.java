package com.qwb.view.cache.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.cache.ui.CacheActivity;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DWareBean;
import com.qwb.db.DWareTypeBean;
import com.qwb.view.customer.model.MineClientBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.ware.model.QueryStkWareType;
import com.qwb.view.step.model.ShopInfoBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：缓存
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCache extends XPresent<CacheActivity>{

    // 请求个人全部客户--用于缓存
    public void queryAllCustomer(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", "1");
        params.put("pageSize", "100000");
//        params.put("latitude", "" + latitude);
//        params.put("longitude", "" + longitude);
        params.put("dataTp", "1");
//        params.put("mids", SPUtils.getID());// 角色
        OkHttpUtils.post()
                .params(params)
                .url(Constans.mineClient)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }

    // 缓存商品类型
    public void queryAllCacheWareType(Activity activity) {
        String url=Constans.queryStkWareType;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils.post()
                .params(params)
                .url(url)
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

    // 缓存商品
    public void queryAllCacheWare(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryAllCacheWare)
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

    // 获取所有商品信息
    public void queryCompanyStockWareList(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils.post()
                .params(params)
                .url(Constans.queryCompanyStockWareList)
                .id(3)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                    }
                });
    }

    //TODO ------------------------接口回調----------------------

    //解析数据-商品类型
    private void parseJson1(String response) {
        try {
            QueryStkWareType bean = JSON.parseObject(response, QueryStkWareType.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<DWareTypeBean> mData = new ArrayList<>();
                    mData.clear();

                    List<QueryStkWareType.List1> list = bean.getList();
                    if (null != list && !list.isEmpty()) {
                        // 第一层
                        for (int i = 0; i < list.size(); i++) {
                            QueryStkWareType.List1 data1 = list.get(i);

                            DWareTypeBean wareTypeBean = new DWareTypeBean();
                            wareTypeBean.setUserId(SPUtils.getID());
                            wareTypeBean.setCompanyId(SPUtils.getCompanyId());
                            wareTypeBean.setPid(0);
                            wareTypeBean.setWareTypeId(data1.getWaretypeId());
                            wareTypeBean.setWareTypeLeaf(data1.getWaretypeLeaf());
                            wareTypeBean.setWareTypeNm(data1.getWaretypeNm());
                            mData.add(wareTypeBean);

                            // 第二层
                            List<QueryStkWareType.List2> list2 = data1.getList2();
                            if (list2 != null && list2.size() > 0) {
                                for (int z = 0; z < list2.size(); z++) {
                                    QueryStkWareType.List2 list2_2 = list2.get(z);

                                    DWareTypeBean wareTypeBean2 = new DWareTypeBean();
                                    wareTypeBean2.setUserId(SPUtils.getID());
                                    wareTypeBean2.setCompanyId(SPUtils.getCompanyId());
                                    wareTypeBean2.setPid(data1.getWaretypeId());
                                    wareTypeBean2.setWareTypeId(list2_2.getWaretypeId());
                                    wareTypeBean2.setWareTypeNm(list2_2.getWaretypeNm());
                                    wareTypeBean2.setWareTypeLeaf(list2_2.getWaretypeLeaf());
                                    mData.add(wareTypeBean2);
                                }
                            }
                        }
                        MyDataUtils.getInstance().saveWareType(mData);
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-缓存商品
    private void parseJson2(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (null != parseObject && parseObject.isState()){
                List<ShopInfoBean.Data> dataList = parseObject.getList();
                if(null != dataList){
                    List<DWareBean> wareBeans = new ArrayList<>();
                    for (ShopInfoBean.Data data: dataList) {
                        DWareBean bean = new DWareBean();
                        bean.setUserId(SPUtils.getID());
                        bean.setCompanyId(SPUtils.getCompanyId());
                        bean.setWareId(data.getWareId());
                        bean.setWareNm(data.getWareNm());
                        bean.setHsNum(data.getHsNum());
                        bean.setWareGg(data.getWareGg());
                        bean.setWareDw(data.getWareDw());
                        bean.setMinUnit(data.getMinUnit());
                        bean.setWareDj(data.getWareDj());
                        bean.setMaxUnitCode(data.getMaxUnitCode());
                        bean.setMinUnitCode(data.getMinUnitCode());
                        bean.setSunitFront(data.getSunitFront());
                        bean.setWareType(data.getWareType());
                        bean.setPy(data.getPy());
                        wareBeans.add(bean);
                    }
                    MyDataUtils.getInstance().saveWare(wareBeans);
                    ToastUtils.showCustomToast("保存成功");
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-我的客户(全部用于缓存)
    private void parseJson3(String response) {
        try {
            MineClientBean parseObject = JSON.parseObject(response, MineClientBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<MineClientInfo> dataList = parseObject.getRows();
                if(dataList!=null && !dataList.isEmpty()){
                    MyDataUtils.getInstance().saveMineClient(dataList);
                    ToastUtils.showCustomToast("保存成功");
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
}
