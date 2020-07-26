package com.qwb.view.purchase.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.common.model.TokenBean;
import com.qwb.db.DStep5Bean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.company.model.PurchaseOrderResultBean;
import com.qwb.view.company.model.PurchaseTypeBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.XiaJi;
import com.qwb.view.purchase.ui.PurchaseOrderActivity;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 采购单
 */
public class PPurchaseOrder extends XPresent<PurchaseOrderActivity>{
    private boolean mShowPopup = true;// 是否弹出搜索商品窗体（语音，窗体中的搜索框）
    /**
     * 获取采购类型
     */
    public void queryXsTp(Activity activity) {
//        OkHttpUtils
//                .post()
//                .addParams("token", SPUtils.getTK())
//                .url(Constans.queryXstypels)
//                .id(4)
//                .build()
//                .execute(new MyHttpCallback(activity) {
//                    @Override
//                    public void myOnError(Call call, Exception e, int id) {
//
//                    }
//
//                    @Override
//                    public void myOnResponse(String response, int id) {
//                        parseJson4(response);
//                    }
//                });
        String response = "{\"msg\":\"获取采购类型列表成功\",\"xstypels\":[{\"inTypeName\":\"正常采购\",\"inTypeCode\":10001},{\"inTypeName\":\"其他\",\"inTypeCode\":10005}],\"state\":true}";
        parseJson4(response);
    }

    /**
     * 查询采购单
     */
    public void queryOrder(Activity activity, String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", orderId);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.showstkin)
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
     * 提交数据或修改数据
     */
    public void addData(Activity activity,String orderId,String proId,String proName,String proType,String stkId,Integer checkAutoPrice,
                        String time,String zdzkStr,String remarks, String wareStr,String queryToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        if(!MyStringUtil.isEmpty(orderId)){
            params.put("id", orderId);
        }
        params.put("proId", proId);
        params.put("proName", proName);
        params.put("proType", proType);
        params.put("inType","采购入库");
        params.put("discount",zdzkStr);//折扣
        params.put("checkAutoPrice",""+checkAutoPrice);
        params.put("status","-2");
        params.put("remarks", remarks);
        params.put("inDate", time);
        params.put("stkId", stkId);
//        params.put("stkName", stkName);
        params.put("wareStr", wareStr);

        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        String url = Constans.dragSaveStkIn;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        switch (id) {
                            case 2:// 添加--拜访流程
                                if(null != getV()){
                                    getV().submitDataError();
                                }
                                break;
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    /**
     * 获取仓库
     */
    public void queryStorage(Activity activity, final boolean isShow) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryWebStkStorageList)
                .id(26)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson26(response, isShow);
                    }
                });
    }

    /**
     * 模糊查询：语音
     */
    public void getDataKeyWordGoodsList(Activity context, String keyWord, boolean showPopup) {
        this.mShowPopup = showPopup;
        String url=Constans.queryStkWare1;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
        OkHttpUtils.post().params(params).url(url).id(27).build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson27(response);
                    }
                });
    }



    /**
     * 查询缓存数据
     */
    public void queryCacheData(String type, String cid){
        try {
            DStep5Bean bean = MyDataUtils.getInstance().queryOrder(type, cid);
            if(bean == null){
                queryStorage(null,false);
                return;
            }

            //DStep5Bean转为QueryBforderBean（QueryBforderBean接口统一返回的数据：复用）
            QueryBforderBean bfBean = new QueryBforderBean();
            if(!MyStringUtil.isEmpty(bean.getCid())){
                bfBean.setCid(Integer.valueOf(bean.getCid()));
            }
            if(!MyStringUtil.isEmpty(bean.getOrderId())){
                bfBean.setId(Integer.valueOf(bean.getOrderId()));
            }
            bfBean.setShr(bean.getShr());
            bfBean.setTel(bean.getTel());
            bfBean.setAddress(bean.getAddress());
            bfBean.setRemo(bean.getRemo());
            bfBean.setZje(bean.getZje());
            bfBean.setZdzk(bean.getZdzk());
            bfBean.setCjje(bean.getCjje());
            bfBean.setShTime(bean.getShTime());
            bfBean.setPszd(bean.getPszd());
            if(!MyStringUtil.isEmpty(bean.getStkId())){
                bfBean.setStkId(Integer.valueOf(bean.getStkId()));
                bfBean.setStkName(bean.getStkName());
            }

            List<XiaJi> list = new ArrayList<>();
            //把json转List
            String orderxx = bean.getOrderxx();
            if(!MyStringUtil.isEmpty(orderxx)){
                List<XiaJi> jsonList = JSON.parseArray(orderxx, XiaJi.class);
                if(jsonList != null){
                    list.addAll(jsonList);
                }
            }

            bfBean.setList(list);
//            getV().doUI(bfBean, bean);
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }

    /**
     * 删除缓存数据
     */
    public void delCacheData(String type, String cid){
        DStep5Bean bean = MyDataUtils.getInstance().queryOrder(type, cid);
        if(bean == null){
            return;
        }
        MyDataUtils.getInstance().delStep5(bean);
    }

    /**
     * 获取token
     */
    public void queryToken(Activity activity) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryToken)
                .id(28)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson28(response);
                    }
                });
    }

    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            PurchaseOrderResultBean bean = JSON.parseObject(response,PurchaseOrderResultBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().doUI(bean.getStkIn(), null);//显示商品信息
                    }
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
            if (null != bean) {
                if(bean.isState()){
                 getV().submitSuccess(0);//提交数据成功
                }else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
                if(null != getV()){
                    getV().submitDataError();
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据
    private void parseJson3(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().submitSuccess(2);//下单提交数据成功
                    }
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


    //解析数据-销售类型
    private void parseJson4(String response) {
        try {
            PurchaseTypeBean parseObject = JSON.parseObject(response, PurchaseTypeBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<PurchaseTypeBean.TypeBean> datas = parseObject.getXstypels();
                if(getV()!=null){
                    getV().showDialogXstp(datas);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-仓库列表
    private void parseJson26(String response, boolean isShow) {
        try {
            StorageBean parseObject = JSON.parseObject(response, StorageBean.class);
            if ( null != parseObject && parseObject.isState()) {
                List<StorageBean.Storage> datas = parseObject.getList();
                if(getV()!=null){
                    if(isShow){
                        getV().showDialogStorage(datas);
                    }else{
                        getV().doNormalStorage(datas);
                    }
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据--关键字搜索（语音）
    private void parseJson27(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        getV().refreshAdapterSearch(list, mShowPopup);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }
    //解析数据--获取token
    private void parseJson28(String response) {
        try {
            TokenBean bean = JSON.parseObject(response, TokenBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    getV().doToken(bean.getData());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
