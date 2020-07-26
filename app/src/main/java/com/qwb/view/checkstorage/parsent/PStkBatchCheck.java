package com.qwb.view.checkstorage.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.checkstorage.model.WareProduceDateListResult;
import com.qwb.view.checkstorage.ui.XStkBatchCheckActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.checkstorage.model.StkCheckDetailBean;
import com.qwb.view.stk.StorageBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 *  批次盘点
 */
public class PStkBatchCheck extends XPresent<XStkBatchCheckActivity>{
    private boolean multipleScan;

    /**
     * 模糊查询
     */
    public void getDataKeyWordGoodsList(Activity context, String keyWord,int stkId) {
        String url=Constans.queryStkWare1;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
        params.put("stkId", String.valueOf(stkId));//仓库id
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }

    public void getWareByScan(Activity context, String keyWord, int stkId, boolean multipleScan) {
        this.multipleScan = multipleScan;
        String url=Constans.queryStkWare1;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("keyWord", keyWord);
        params.put("noCompany", "1");
        params.put("stkId", String.valueOf(stkId));//仓库id
        OkHttpUtils.post()
                .params(params)
                .url(url)
                .id(5)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson5(response);
                    }
                });
    }

    /**
     * 获取仓库
     */
    public void queryDataStorageList(Activity activity) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryWebStkStorageList)
                .id(2)
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
     * 添加盘点单
     */
    public void addStkCheck(Activity activity, String staff, int stkId, String checkTimeStr, String wareStr, int billId, String queryToken){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", String.valueOf(stkId));
        params.put("staff", staff);//操作者
        params.put("isPc", "1");//批次1
        params.put("id", String.valueOf(billId));
        params.put("checkTimeStr", checkTimeStr);
        params.put("wareStr", wareStr);
        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        String url = Constans.drageBatchStkCheck;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
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

    /**
     * 查询盘点单
     */
    public void queryStkCheck(Activity activity, int billId){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        String url = Constans.queryStkCheck;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
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
     * 商品ids查询商品生产日期列表
     */
    public void getWareProduceDateList(Activity activity, int stkId , String wareIds){
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", String.valueOf(stkId));
        params.put("wareIds", wareIds);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.getWareProduceDateList)
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

    //解析数据--搜索
    private void parseJson1(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        getV().refreshSearchAdapter(list);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-仓库列表
    private void parseJson2(String response) {
        try {
            StorageBean parseObject = JSON.parseObject(response, StorageBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<StorageBean.Storage> datas = parseObject.getList();
                if(getV()!=null){
                    getV().showDialogStorage(datas);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson3(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (null != bean && bean.isState()) {
                if(getV()!=null){
                    getV().doAddSuccess();
                }
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-详情
    private void parseJson4(String response) {
        try {
            StkCheckDetailBean bean = JSON.parseObject(response, StkCheckDetailBean.class);
            if (null != bean && bean.isState()) {
                if(getV()!=null){
                    getV().doStkCheckDetail(bean);
                }
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-单个扫描和多次扫描
    private void parseJson5(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        getV().doScanSuccess(list,multipleScan);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastUtils.showCustomToast(e.getMessage());
        }
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

    //解析数据-商品生产日期
    private void parseJson29(String response) {
        try {
            WareProduceDateListResult bean = JSON.parseObject(response, WareProduceDateListResult.class);
            if (null != bean && bean.isState()) {
                 getV().doWareProduceDatList(bean.getRows());
            }else{
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }




}
