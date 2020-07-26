package com.qwb.view.step.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.step.ui.ChooseWareActivity3;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DWareBean;
import com.qwb.db.DWareTypeBean;
import com.qwb.utils.MyHttpUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.HistoryOrgPriceBean;
import com.qwb.view.ware.model.QueryStkWareType;
import com.qwb.view.step.model.ShopInfoBean;
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
 * 创建描述：选择商品
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PChooseWare3 extends XPresent<ChooseWareActivity3>{
    /**
     * * 获得商品分类列表（不包括非公司商品）
     */
    public void queryDataCompanyWareTypes(Activity activity, String cid, String noCompany, String stkId, final boolean isContainSale,final String type) {
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
                        parseJson1(response, isContainSale,type);
                    }
                });
    }


    /**
     * * 获得商品分类列表（包括非公司的商品分类）
     */
    public void queryDataWareTypes(Activity activity, String cid, String noCompany, String stkId) {
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
                .post(activity, params, Constans.queryWareTypeList, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response, true, null);
                    }
                });
    }


    /**
     * 获得常卖商品列表
     */
    public void getDataOftenGoodsList(Activity context, String cid, String noCompany, String stkId, int pageNo, int pageSize, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", cid);
        params.put("customerId", cid);
        if(!MyStringUtil.isEmpty(noCompany)){
            params.put("noCompany", noCompany);
        }
        if(!MyStringUtil.isEmpty(stkId)){
            params.put("stkId", stkId);
        }

//        if(!"7".equals(type)){
            params.put("pageNo", String.valueOf(pageNo));
            params.put("pageSize", String.valueOf(pageSize));
//        }

        MyHttpUtil.getInstance()
                .post(context, params, Constans.querySaleWare, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2345(response, null);
                    }
                });

    }
    /**
     * 获得收藏商品列表
     */
    public void getDataSaveGoodsList(Activity context,String cid, String noCompany, String stkId, int pageNo, int pageSize, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("customerId", cid);
        if(!MyStringUtil.isEmpty(noCompany)){
            params.put("noCompany", noCompany);
        }
        if(!MyStringUtil.isEmpty(stkId)){
            params.put("stkId", stkId);
        }

//        if(!"7".equals(type)){
            params.put("pageNo", String.valueOf(pageNo));
            params.put("pageSize", String.valueOf(pageSize));
//        }

        MyHttpUtil.getInstance()
                .post(context, params, Constans.querySaveWare, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2345(response, null);
                    }
                });
    }

    /**
     * 根据类型查询商品:wareType
     */
    public void getDataTypeGoodsList(Activity context, String cid, final String wareType, String noCompany, String stkId, int pageNo, int pageSize, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("wareType", wareType);
        params.put("customerId", cid);
        if(!MyStringUtil.isEmpty(noCompany)){
            params.put("noCompany", noCompany);
        }
        if(!MyStringUtil.isEmpty(stkId)){
            params.put("stkId", stkId);
        }
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));
        //车削并“车载商品”：过滤0库存
        if("7".equals(type) && MyStringUtil.isEmpty(wareType)){
            params.put("filterZero", "0");
        }

        MyHttpUtil.getInstance()
                .post(context, params, Constans.queryStkWare, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        if("7".equals(type) && MyStringUtil.isEmpty(wareType)){
                            parseJson2345(response, type);
                        }else{
                            parseJson2345(response, null);
                        }
                    }
                });
    }

    /**
     * 车销仓库商品列表
     */
    public void queryDataStkWareCarList(Activity activity, String stkId, String customerId) {
        String url=Constans.queryStorageWareCarList;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", stkId);//仓库id
        params.put("customerId", customerId);
        params.put("type", "2");
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2345(response, "");
                    }
                });
    }

    /**
     * 模糊查询
     */
    public void getDataKeyWordGoodsList(Activity context,String cid,String keyWord,String noCompany, String stkId, int pageNo, int pageSize, final String type) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
        params.put("customerId", cid);
        if(!MyStringUtil.isEmpty(noCompany)){
            params.put("noCompany", noCompany);
        }
        if(!MyStringUtil.isEmpty(stkId)){
            params.put("stkId", stkId);
        }

        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(pageSize));

        MyHttpUtil.getInstance()
                .post(context, params, Constans.queryStkWare1,new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2345(response, type);
                    }
                });
    }

    /**
     * 收藏商品
     */
    public void getDataFavoritesGoodsList(Activity context,String wareId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("wareId", wareId);

        MyHttpUtil.getInstance()
                .post(context, params, Constans.addEmpWare, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson67(response);
                    }
                });
    }

    /**
     * 取消收藏
     */
    public void getDataUnFavoritesGoodsList(Activity context,String wareId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("wareId", wareId);

        MyHttpUtil.getInstance()
                .post(context, params, Constans.deleteEmpWare, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson67(response);
                    }
                });
    }

    /**
     * 获取历史价格，原价
     */
    public void getDataHistoryOrgPrice(Activity context,String cid,String wareId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", cid);//客户ID
        params.put("wareId", wareId);//商品ID

        MyHttpUtil.getInstance()
                .post(context, params, Constans.queryCustomerHisWarePrice, new MyHttpUtil.ResultListener2() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        if(null != getV()){
                            getV().setZyPirce5(null);
                        }
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson8(response);
                    }

                    @Override
                    public void onNoNetWork() {
                        if(null != getV()){
                            getV().setZyPirce5(null);
                        }
                    }
                });
    }

//    /**
//     * 获取是执行价还是历史价
//     */
//    public void queryAutoPrice(Activity context) {
//        Map<String, String> params = new HashMap<>();
//        params.put("token", SPUtils.getTK());
//        MyHttpUtil.getInstance()
//                .post(context, params, Constans.queryAutoPrice, new MyHttpUtil.ResultListener2() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                    }
//
//                    @Override
//                    public void onSuccess(String response, int id) {
//                        parseJson9(response);
//                    }
//
//                    @Override
//                    public void onNoNetWork() {
//                    }
//                });
//    }

    /**
     *
     */
    public void queryCacheWareType(Activity activity) {
        try {
            List<DWareTypeBean> wareTypeBeans =  MyDataUtils.getInstance().queryWareType();
            if(null == wareTypeBeans || wareTypeBeans.isEmpty()){
                return;
            }

            List<TreeBean> mData = new ArrayList();
            for (DWareTypeBean bean: wareTypeBeans) {
                mData.add(new TreeBean(bean.getWareTypeId(), bean.getPid(), bean.getWareTypeNm()));
            }

            if(null != getV()){
                // 刷新Tree
                getV().refreshAdapterTree(mData);
            }
        }catch (Exception e){
        }

    }
    /**
     *
     */
    public void queryCacheWare(Activity activity, String search, String wareType, int pageNo, int pageSize) {
        try {
            List<DWareBean> wareBeans =  MyDataUtils.getInstance().queryWare(search, wareType, pageNo, pageSize);
            if(null != wareBeans){
                List<ShopInfoBean.Data> dataList = new ArrayList<>();
                for (DWareBean bean: wareBeans) {
                    ShopInfoBean.Data data = new ShopInfoBean.Data();
                    data.setWareId(bean.getWareId());
                    data.setWareNm(bean.getWareNm());
                    data.setHsNum(bean.getHsNum());
                    data.setWareGg(bean.getWareGg());
                    data.setWareDw(bean.getWareDw());
                    data.setMinUnit(bean.getMinUnit());
                    data.setWareDj(bean.getWareDj());
                    data.setMaxUnitCode(bean.getMaxUnitCode());
                    data.setMinUnitCode(bean.getMinUnitCode());
                    data.setSunitFront(bean.getSunitFront());
                    dataList.add(data);
                }
                getV().refreshAdapterRight(dataList,true);
            }
        }catch (Exception e){
        }
    }

    //------------------------------------------------------------------------------------------------------------

    //解析数据-商品类型
    private void parseJson1(String response, boolean isContainSale, String type) {
        try {
            QueryStkWareType bean = JSON.parseObject(response, QueryStkWareType.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<TreeBean> mDatas = new ArrayList<TreeBean>();
                    mDatas.clear();
                    List<QueryStkWareType.List1> list = bean.getList();
                    if (list != null && list.size() > 0) {
                        //车销下单：根目录
                        if("7".equals(type)){
                            mDatas.add(new TreeBean(-3, 0, "车载商品"));
                        }
                        // 默认添加“常售商品”，“收藏商品”
                        if(isContainSale){
                            mDatas.add(new TreeBean(-1, 0, "常售商品"));
                            mDatas.add(new TreeBean(-2, 0, "收藏商品"));
                        }
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

    private void parseJson2345(String response, String type) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        //零库存过滤
                        String storage0=SPUtils.getSValues(ConstantUtils.Sp.STORAGE_ZERO);
                        if("7".equals(type) || (!MyUtils.isEmptyString(storage0) && storage0.equals("1"))){
                            List<ShopInfoBean.Data> removeList = new ArrayList<>();
                            //零库存过滤
                            for (ShopInfoBean.Data data: list) {
                                String stkQty=data.getStkQty();
                                try {
                                    if(MyUtils.isEmptyString(stkQty)){
                                        removeList.add(data);
                                    }else{
                                        if (Double.valueOf(stkQty)<=0){
                                            removeList.add(data);
                                        }
                                    }
                                }catch (Exception e){
                                }
                            }
                            if(removeList!=null && !removeList.isEmpty()){
                                list.removeAll(removeList);
                            }
                        }
                        getV().refreshAdapterRight(list,parseObject.isEditPrice());
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }  //解析数据

    //解析数据-收藏商品,取消收藏
    private void parseJson67(String response) {
        try {
            JSONObject parseObject = JSON.parseObject(response);
            if (parseObject != null) {
                if (parseObject.getBoolean("state")) {
                    if(getV()!=null){
                        getV().setSuccessFavorites();
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-解析json:历史价格，原价
    private void parseJson8(String response) {
        try {
            HistoryOrgPriceBean bean = JSON.parseObject(response, HistoryOrgPriceBean.class);
            if (bean != null) {
                if(getV()!=null){
                    getV().setZyPirce5(bean);
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
