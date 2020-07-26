package com.qwb.view.order.parsent;


import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.order.ui.HistoryOrderActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.order.model.QueryDhorderBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：历史订单
 */
public class PHistoryOrder extends XPresent<HistoryOrderActivity>{
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.DHXD_NEW, ConstantUtils.Apply.DHXD_NEW);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.DHXD_NEW, ConstantUtils.Apply.DHXD_NEW);

    /**
     * 获取历史订单列表
     * @param context
     * @param pageNo
     * @param startDate
     * @param endDate
     */
    public void loadDataOrder(Context context, EditText etSearch, int pageNo, String startDate, String endDate,String cid) {
        String searchStr=etSearch.getText().toString().trim();//搜索
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("kmNm", String.valueOf(searchStr));// 业务员或客户名称
        params.put("customerId", cid);// 客户id
        if (!TextUtils.isEmpty(startDate)) {
            params.put("sdate", startDate);// 开始时间
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("edate", endDate);// 结束时间
        }
        params.put("dataTp", String.valueOf(dataTp));//角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);//角色
        }
        String url = Constans.queryDhorder;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyStringCallback(), context);
    }




    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showCustomToast(e.getMessage());
            try {
                //关闭刷新
                getV().closeRefresh();
            }catch (Exception e1){
            }
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://历史订单
                    parseJson1(response);
                    break;
            }
        }
    }

    //解析数据-历史订单
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
