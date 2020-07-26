package com.qwb.view.car.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.view.car.ui.CarStkWareActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：仓库管理
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCarStkWare extends XPresent<CarStkWareActivity>{


    /**
     * 车销仓库商品列表
     */
    public void queryDataStkWareCarList(Activity activity, String stkId) {
        String url=Constans.queryStorageWareCarList;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", stkId);//仓库id
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
                        parseJson2345(response);
                    }
                });
    }

    private void parseJson2345(String response) {
        try {
            ShopInfoBean parseObject = JSON.parseObject(response, ShopInfoBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if(getV()!=null){
                        List<ShopInfoBean.Data> list = parseObject.getList();
                        getV().refreshAdapter(list);
                    }
                }else{
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }  //解析数据





}
