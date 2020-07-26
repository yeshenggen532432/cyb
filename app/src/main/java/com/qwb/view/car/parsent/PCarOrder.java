package com.qwb.view.car.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.car.ui.CarOrderActivity;
import com.qwb.view.common.model.OrderSuccessBean;
import com.qwb.view.step.model.OrderConfigResult;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.db.DStep5Bean;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.XiaJi;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：车削下单
 */
public class PCarOrder extends XPresent<CarOrderActivity>{
    private boolean mShowPopup = true;// 是否弹出搜索商品窗体（语音，窗体中的搜索框）
    private int mSubmitType;//2:提交并打印
    /**
     * 获取销售类型
     */
    public void queryXsTp(Activity activity) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryXstypels)
                .id(4)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }

    /**
     * 获取拜访订单信息：订货下单列表
     */
    public void queryDhOrder(Activity activity, int orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(orderId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryDhorderWeb)
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
    public void addData(Activity activity,String clientId,int xdType, int ddId,String jsonStr,
                        String shrStr,String phoneStr,String addressStr,String remoStr,String zjeStr,String zdzkStr,String cjjeStr,
                        String shTimeStr,String pszdStr,String stkId, String queryToken, final int submitType) {
        this.mSubmitType = submitType;
//        XLog.e("shrStr", shrStr);
//        XLog.e("phoneStr", phoneStr);
//        XLog.e("addressStr", addressStr);
//        XLog.e("remoStr", remoStr);
//        XLog.e("zdzkStr", zdzkStr);
//        XLog.e("zjeStr", zjeStr);
//        XLog.e("cjjeStr", cjjeStr);
//        XLog.e("shTimeStr", shTimeStr);
//        XLog.e("pszdStr", pszdStr);
//        XLog.e("token", SPUtils.getTK());
//        XLog.e("cid", clientId);
//        XLog.e("orderxx", jsonStr);
//        XLog.e("ddId", ""+ddId);
//        XLog.e("stkId", ""+stkId);

        //-------------------------------------------
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if (!MyStringUtil.isEmpty(shrStr)) {
            params.put("shr", shrStr);
        }
        if (!MyStringUtil.isEmpty(phoneStr)) {
            params.put("tel", phoneStr);
        }
        if (!MyStringUtil.isEmpty(addressStr)) {
            params.put("address", addressStr);
        }
        if (!MyStringUtil.isEmpty(remoStr)) {
            params.put("remo", remoStr);
        }
        if (!MyStringUtil.isEmpty(zjeStr)) {
            params.put("zje", zjeStr);
        }
        if (!MyStringUtil.isEmpty(zdzkStr)) {
            params.put("zdzk", zdzkStr);
        }
        if (!MyStringUtil.isEmpty(cjjeStr)) {
            params.put("cjje", cjjeStr);
        }
        if (!MyStringUtil.isEmpty(jsonStr)) {
            params.put("orderxx", jsonStr);
        }
        if (!MyStringUtil.isEmpty(shTimeStr)) {
            params.put("shTime", shTimeStr);
        }
        if (!MyStringUtil.isEmpty(pszdStr)) {
            params.put("pszd", pszdStr);
        }
        if (!MyStringUtil.isEmpty(stkId)) {
            params.put("stkId", stkId);
        }
        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        String url = null;
        if(xdType == 8){
            params.put("id", String.valueOf(ddId));
            url = Constans.updateCarOrderWeb;
        }else{
            url = Constans.addCarOorderWeb;
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        if(null != getV()){
                            getV().doSubmitError();
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
    public void queryStorage(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryWebStkCarStorageList)
                .id(26)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson26(response);
                    }
                });
    }

    /**
     * 模糊查询商品列表：语音
     */
    public void queryWareListByKeyWord(Activity context, String keyWord, boolean showPopup) {
        this.mShowPopup = showPopup;
        String url=Constans.queryStkWare1;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
//        params.put("customerId", cid);
//        params.put("noCompany", noCompany);
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

    /**
     * 获取手机端下单配置（拜访下单，订货下单）
     * 1.是历史价还是执行价
     * 2.价格是否可以编辑
     */
    public void queryOrderConfigWeb(Activity activity) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .url(Constans.queryOrderConfigWeb)
                .id(29)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson29(response);
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
                queryStorage(null);
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
            getV().doUI(bfBean);
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }

    //-------------------------------------------------------------------------------------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            QueryBforderBean parseObject = JSON.parseObject(response,QueryBforderBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        if (2 == mSubmitType){
                            getV().doPrint(parseObject);
                        }else{
                            getV().doUI(parseObject);//显示商品信息
                        }
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            OrderSuccessBean bean = JSON.parseObject(response, OrderSuccessBean.class);
            if (MyRequestUtil.isSuccess(bean)) {
                if (2==mSubmitType){
                    queryDhOrder(null, bean.getData());
                }else{
                    getV().doSubmitSuccess();//提交数据成功
                }
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
                if(null != getV()){
                    getV().doSubmitError();
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-销售类型
    private void parseJson4(String response) {
        try {
            QueryXstypeBean parseObject = JSON.parseObject(response, QueryXstypeBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<QueryXstypeBean.QueryXstype> datas = parseObject.getXstypels();
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
    private void parseJson26(String response) {
        try {
            StorageBean parseObject = JSON.parseObject(response, StorageBean.class);
            if ( null != parseObject && parseObject.isState()) {
                List<StorageBean.Storage> datas = parseObject.getList();
                if(getV()!=null){
                    getV().showDialogStorage(datas);
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

    //解析数据
    private void parseJson29(String response) {
        try {
            OrderConfigResult result = JSON.parseObject(response, OrderConfigResult.class);
            if (MyRequestUtil.isSuccess(result)) {
                getV().doOrderConfig(result.getData());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


}
