package com.qwb.view.step.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.view.step.ui.Step4Activity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.model.TokenBean;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.step.model.QueryXsxj;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.step.model.queryXsxjBean;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：拜访4
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PStep4 extends XPresent<Step4Activity>{

    /**
     * 获取上传提交信息
     */
    public void queryData(Activity activity, String clientId, String pdateStr) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if(!MyUtils.isEmptyString(pdateStr)){// 如果不是今天，传补拜访日期
            params.put("date", pdateStr);
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryBfxsxjlsWeb)
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
     * 添加或修改（）
     */
    public void addData(Activity activity, String clientId,String pdateStr, String count4,String jsonStr, String queryToken) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", clientId);
        if(!MyUtils.isEmptyString(jsonStr)){
            params.put("xsxj", jsonStr);
        }
        if(!MyUtils.isEmptyString(pdateStr)){// 如果不是今天，传补拜访日期
            params.put("date", pdateStr);
        }
        if (!MyStringUtil.isEmpty(queryToken)) {
            params.put("page_token", queryToken);
        }

        String url = null;
        if("0".equals(count4)){// 0:销售小结（添加） 1:销售小结（修改）
            url = Constans.addBfxsxjWeb;
        }else if("1".equals(count4)){
            url = Constans.updateBfxsxjWeb;
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        if(null != getV()){
                            getV().submitDataError();
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

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


    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://上传提交的信息
                    parseJson1(response);
                    break;
                case 2:// 添加--修改
                    parseJson2(response);
                    break;
            }
        }
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            queryXsxjBean parseObject = JSON.parseObject(response,queryXsxjBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<ShopInfoBean.Data> datas = new ArrayList<>();

                    List<QueryXsxj> list = parseObject.getList();
                    if (list !=null && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            QueryXsxj queryXsxj = list.get(i);

                            ShopInfoBean.Data data = new ShopInfoBean.Data();
                            data.setWareId(Integer.valueOf(queryXsxj.getWid()));
                            data.setWareNm(queryXsxj.getWareNm());
                            data.setCurrentDhl(queryXsxj.getDhNum());
                            data.setCurrentSxl(queryXsxj.getSxNum());
                            data.setCurrentKcl(queryXsxj.getKcNum());
                            data.setCurrentDd(queryXsxj.getDdNum());
                            data.setCurrentXstp(queryXsxj.getXstp());
                            data.setWareGg(queryXsxj.getWareGg());
                            data.setCurrentBz(queryXsxj.getRemo());

                            //新鲜值
                            String xxd = queryXsxj.getXxd();
                            if (!MyUtils.isEmptyString(xxd)) {
                                if ("0".equals(String.valueOf(xxd))) {
                                    data.setCurrentXxz("正常");
                                } else {
                                    data.setCurrentXxz("临期" + xxd);
                                }
                            }

                            datas.add(data);
                        }
                        if(getV()!=null){
                            getV().showData(datas);
                        }
                    } else {
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            JSONObject parseObject = JSON.parseObject(response);
            if (null != parseObject && parseObject.getBoolean("state") && null != getV() ) {
                getV().submitSuccess();//提交数据成功
            }else{
                ToastUtils.showCustomToast(parseObject.getString("msg"));
                if(null != getV()){
//                    getV().saveCacheData();
                    getV().submitDataError();
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-销售类型
    private void parseJson4(String response) {
        try {
            QueryXstypeBean parseObject = JSON.parseObject(response, QueryXstypeBean.class);
            if (parseObject != null && parseObject.isState()) {
                List<QueryXstypeBean.QueryXstype> datas = parseObject.getXstypels();
                if(getV() != null){
                    getV().showDialogXstp(datas);
                }
            }else{
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
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



}
