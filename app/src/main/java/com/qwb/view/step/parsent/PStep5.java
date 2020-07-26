package com.qwb.view.step.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.common.OrderTypeEnum;
import com.qwb.utils.MyRequestUtil;
import com.qwb.view.step.model.OrderConfigResult;
import com.qwb.view.step.ui.Step5Activity;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.base.model.XbaseBean;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStep5Bean;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.step.model.QueryXstypeBean;
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
 * 创建描述：拜访5
 */
public class PStep5 extends XPresent<Step5Activity>{
    private boolean mShowPopup = true;// 是否弹出搜索商品窗体（语音，窗体中的搜索框）
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
     * 获取拜访订单信息:拜访步骤；补拜访
     */
    public void queryBfOrder(Activity activity, String clientId, String pdateStr) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if(!MyStringUtil.isEmpty(pdateStr)){// 传补拜访日期
            params.put("date", pdateStr);
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBforderWeb)
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
     * 获取拜访订单信息：订货下单列表
     */
    public void queryRetreat(Activity activity, int orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(orderId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryThorderWeb)
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
    public void addData(Activity activity,String clientId,int xdType,String pdateStr,String count5,int ddId,String jsonStr,
                        String shrStr,String phoneStr,String addressStr,String remoStr,String zjeStr,String zdzkStr,String cjjeStr,
                        String shTimeStr,String pszdStr,String stkId, String queryToken, int redMark) {
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
        if (Constans.ISDEBUG){
            params.put("redMark", String.valueOf(redMark));
        }


        String url = null;
        int tag = 0;

        if (xdType == 1) {//拜访步骤；拜访计划的补拜访
            if (!MyStringUtil.isEmpty(pdateStr)) {//补拜访
                params.put("date", pdateStr);
            }
            switch (count5) {
                case "0":
                    url = Constans.addBforderWeb;
                    tag = 2;
                    break;
                case "1":
                    params.put("id", String.valueOf(ddId));// 订单id
                    url = Constans.updateBforderWeb;
                    tag = 2;
                    break;
            }
        } else if (xdType == 2) {//单独下单
            url = Constans.addDhorderWeb;
            tag = 2;
        } else if (xdType == 3) {// 订货下单列表--修改
            params.put("id", String.valueOf(ddId));// 订单id
            url = Constans.updateDhorderWeb;
            tag = 2;
        } else if (xdType == 4) {//退货--添加
            url = Constans.addThorderWeb;
            tag = 3;
        } else if (xdType == 5) {// 退货列表--修改
            params.put("id", String.valueOf(ddId));// 订单id
            url = Constans.updateThorderWeb;
            tag = 3;
        }else if (xdType == OrderTypeEnum.ORDER_RED_ADD.getType()) {//红字单
            params.put("redMark", "1");// redMark=1 红字单据
            url = Constans.addDhorderWeb;
            tag = 2;
        }else if (xdType == OrderTypeEnum.ORDER_RED_LIST.getType()) {// 红字单--修改
            params.put("id", String.valueOf(ddId));// 订单id
            params.put("redMark", "1");// redMark=1 红字单据
            url = Constans.updateDhorderWeb;
            tag = 2;
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
                        switch (id) {
                            case 2:// 添加--拜访流程
                                parseJson2(response);
                                break;
                            case 3:// 添加--退货
                                parseJson3(response);
                                break;
                        }
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
     * 商城订单
     */
    public void queryScOrder(Activity activity,int orderId,String companyId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("orderId", String.valueOf(orderId));
        params.put("companyId", String.valueOf(companyId));
        String url = Constans.shopBforderWeb_queryShopBforderWeb;
        OkHttpUtils
                .post()
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
                        parseJson1(response);
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
            getV().doUI(bfBean, bean);
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

    //TODO ------------------------接口回調----------------------

    //解析数据
    private void parseJson1(String response) {
        try {
            QueryBforderBean bean = JSON.parseObject(response,QueryBforderBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().doUI(bean, null);//显示商品信息
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
            XbaseBean bean = JSON.parseObject(response, XbaseBean.class);
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
