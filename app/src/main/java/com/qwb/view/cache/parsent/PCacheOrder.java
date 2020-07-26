package com.qwb.view.cache.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DStep5Bean;
import com.qwb.view.cache.ui.CacheOrderActivity;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：缓存-拜访步骤
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCacheOrder extends XPresent<CacheOrderActivity>{
    private Activity activity;

    public void queryAllCache(Activity activity) {
        this.activity = activity;
        try {
            // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
            // 这边不查询（拜访下单--添加或修改）
            List<DStep5Bean> step5Beans = MyDataUtils.getInstance().queryAllStep5("1", false);
            if(null != step5Beans){
                getV().refreshAdapter5(step5Beans, false);
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


    //处理拜访5:下单
    public void doStep5(final DStep5Bean bean) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", bean.getCid());
        if (!MyStringUtil.isEmpty(bean.getShr())) {
            params.put("shr", bean.getShr());
        }
        if (!MyStringUtil.isEmpty(bean.getTel())) {
            params.put("tel", bean.getTel());
        }
        if (!MyStringUtil.isEmpty(bean.getAddress())) {
            params.put("address", bean.getAddress());
        }
        if (!MyStringUtil.isEmpty(bean.getRemo())) {
            params.put("remo", bean.getRemo());
        }
        if (!MyStringUtil.isEmpty(bean.getZje())) {
            params.put("zje", bean.getZje());
        }
        if (!MyStringUtil.isEmpty(bean.getZdzk())) {
            params.put("zdzk", bean.getZdzk());
        }
        if (!MyStringUtil.isEmpty(bean.getCjje())) {
            params.put("cjje", bean.getCjje());
        }
        if (!MyStringUtil.isEmpty(bean.getOrderId())) {
            params.put("orderxx", bean.getOrderxx());
        }
        if (!MyStringUtil.isEmpty(bean.getShTime())) {
            params.put("shTime", bean.getShTime());
        }
        if (!MyStringUtil.isEmpty(bean.getPszd())) {
            params.put("pszd", bean.getPszd());
        }
        if (!MyStringUtil.isEmpty(bean.getStkId())) {
            params.put("stkId", bean.getStkId());
        }

        String url = null;
        String type = bean.getType();
        // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
        switch (type){
            case "1":
                if("0".equals(bean.getCount())){
                    url = Constans.addBforderWeb;
                }else if("1".equals(bean.getCount())){
                    params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                    url = Constans.updateBforderWeb;
                }
                break;
            case "2":
                url = Constans.addDhorderWeb;
                break;
            case "3":
                params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                url = Constans.updateDhorderWeb;
                break;
            case "4":
                url = Constans.addThorderWeb;
                break;
            case "5":
                params.put("id", String.valueOf(bean.getOrderId()));// 订单id
                url = Constans.updateThorderWeb;
                break;
        }

        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        MyDataUtils.getInstance().updateStep5(bean);
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        try {
                            JSONObject parseObject = JSON.parseObject(response);
                            if (null != parseObject && parseObject.getBoolean("state")) {
                                MyDataUtils.getInstance().delStep5(bean);
                                getV().refreshAdapter5(null, true);
                                ToastUtils.showCustomToast("上传成功");
                            }else{
                                MyDataUtils.getInstance().updateStep5(bean);
                            }
                        }catch (Exception e){
                            MyDataUtils.getInstance().updateStep5(bean);
                        }
                    }
                });
    }



}
