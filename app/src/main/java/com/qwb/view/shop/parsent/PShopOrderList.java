package com.qwb.view.shop.parsent;


import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.qwb.view.shop.ui.ShopOrderListActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.order.model.QueryDhorderBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：商城订单
 */
public class PShopOrderList extends XPresent<ShopOrderListActivity>{

    /**
     * 获取车销单列表
     */
    public void loadDataOrder(Activity context, EditText etSearch, int pageNo, String startDate, String endDate) {
        String searchStr=etSearch.getText().toString().trim();//搜索
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("kmNm", searchStr);// 业务员或客户名称
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryAllShopOrder)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
                            //关闭刷新
                            getV().closeRefresh();
                        }catch (Exception e1){
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response);
                    }
                });
    }


    private void parseJson1(String response) {
        try {
            QueryDhorderBean parseObject = JSON.parseObject(response,QueryDhorderBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    List<QueryDhorderBean.Rows> dataList = parseObject.getRows();
                    getV().refreshAdapterOrder(dataList);
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}
