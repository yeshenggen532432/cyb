package com.qwb.view.purchase.parsent;


import android.app.Activity;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.company.model.PurchaseOrderBean;
import com.qwb.view.company.model.PurchaseOrderListBean;
import com.qwb.view.purchase.ui.PurchaseOrderListActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 采购单列表
 */
public class PPurchaseOrderList extends XPresent<PurchaseOrderListActivity>{
//    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.DHXD_NEW, ConstantUtils.Apply.DHXD_NEW);
//    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.DHXD_NEW, ConstantUtils.Apply.DHXD_NEW);

    /**
     * 获取订货下单列表
     */
    public void queryOrderPage(Activity activity, EditText etSearch, int pageNo, String startDate, String endDate) {
        String searchStr=etSearch.getText().toString().trim();//搜索
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(20));
        params.put("proName", String.valueOf(searchStr));//
        params.put("sdate", String.valueOf(startDate));//
        params.put("edate", String.valueOf(endDate));//
        params.put("inType", String.valueOf("采购入库"));
//        if (!TextUtils.isEmpty(startDate)) {
//            params.put("sdate", startDate);// 开始时间
//        }
//        if (!TextUtils.isEmpty(endDate)) {
//            params.put("edate", endDate);// 结束时间
//        }
//        params.put("dataTp", String.valueOf(dataTp));//角色
//        if ("4".equals(dataTp)) {
//            params.put("mids", dataTpMids);//角色
//        }
        String url = Constans.stkInHisPage;
        OkHttpUtils
                .post()
                .params(params)
                .url(url)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
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



    //TODO ------------------------接口回調----------------------

    //解析数据-订货
    private void parseJson1(String response) {
        try {
            PurchaseOrderListBean bean = JSON.parseObject(response,PurchaseOrderListBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<PurchaseOrderBean> dataList = bean.getRows();
                    getV().refreshAdapterOrder(dataList);
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
