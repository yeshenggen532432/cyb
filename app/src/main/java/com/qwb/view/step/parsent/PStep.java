package com.qwb.view.step.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.step.ui.StepActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyHttpUtil;
import com.qwb.view.customer.model.CustomerBfBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 拜访6步骤
 */
public class PStep extends XPresent<StepActivity> {
    private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);
    private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.KHGL_NEW, ConstantUtils.Apply.KHGL_OLD);

    /**
     * 获取拜访信息：1）周边客户 2）我的客户 3）计划拜访
     * date:计划拜访的补拜访日期
     */
    private int cid;
    private String clientName;
    public void queryCustomerBf(Activity activity, int type, int cid, String date, String clientName) {
        this.cid = cid;
        this.clientName = clientName;

        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("cid", String.valueOf(cid));
        if (3 == type) {
            params.put("date", String.valueOf(date));// 如果不是今天，传补拜访日期
        }

        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryBfkhsWeb, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson1(response);
                    }
                });


    }

    // 获取月的销售金额
    public void queryDataMoney(Activity activity, String monthFirst, String monthLast, final int tag) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stime", monthFirst);
        params.put("etime", monthLast);
        params.put("dataTp", dataTp);//角色
        if ("4".equals(dataTp)) {
            params.put("mids", dataTpMids);//角色
        }
        params.put("khNm", clientName);
        params.put("cid", String.valueOf(cid));// 客户id

        MyHttpUtil.getInstance()
                .post(activity, params, Constans.queryTolpricexs, new MyHttpUtil.ResultListener() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onSuccess(String response, int id) {
                        parseJson2(response, tag);
                    }
                });
    }


//    /**
//     * 获取客户信息或经销商信息（没有经纬度，添加客户经纬度）
//     */
//    public void queryCustomerById(Activity activity, String cid) {
//        Map<String, String> params = new HashMap<>();
//        params.put("token", SPUtils.getTK());
//        params.put("Id", cid);
//
//        MyHttpUtil.getInstance()
//                .post(activity, params, Constans.clientDetail, new MyHttpUtil.ResultListener() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                    }
//
//                    @Override
//                    public void onSuccess(String response, int id) {
//                        parseJson8(response);
//                    }
//                });
//    }

    /**
     * 修改客户
     */
    public void updateCustomerByAddress(Activity activity,int cid, String address, String longitude, String latitude, String province, String city, String area) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("id", String.valueOf(cid));
        params.put("address", address);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("province", String.valueOf(province));
        params.put("city", String.valueOf(city));
        params.put("area", String.valueOf(area));
        OkHttpUtils.post()
                .params(params)
                .url(Constans.updateCustomerByAddress)
                .id(7)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson7(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            CustomerBfBean parseObject = JSONObject.parseObject(response, CustomerBfBean.class);
            if (parseObject != null) {
                if (parseObject.isState()) {
                    if (getV() != null) {
                        getV().doUICustomerBf(parseObject);
                    }
                    queryDataMoney(null, getV().shangFirst, getV().shangLast, 1);//上月的销售金额
                    queryDataMoney(null, getV().benFirst, getV().benLast, 2);//本月的销售金额
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-上月,本月的销售金额
    private void parseJson2(String response, int tag) {
        try {
            JSONObject parseObject = JSONObject.parseObject(response);
            if (parseObject != null) {
                if (parseObject.getBoolean("state")) {
                    double money = parseObject.getDoubleValue("tolprice");
                    if (getV() != null) {
                        //tag:1上月；2：本月
                        getV().doUIMoney(money, tag);
                    }
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    //解析数据-修改客户(地址信息)
    private void parseJson7(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (MyRequestUtil.isSuccess(bean)) {
                if (getV() != null) {
                    getV().doUpdateSuccess();
                }
            }
            ToastUtils.showCustomToast(bean.getMsg());
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

//    //解析数据-客户信息
//    private void parseJson8(String response) {
//        try {
//            CustomerResult bean = JSON.parseObject(response, CustomerResult.class);
//            if (MyRequestUtil.isSuccess(bean)) {
//                    CustomerBean customer = bean.getCustomer();
//                    if (customer != null) {
//                        if (getV() != null) {
//                            getV().doUI(customer);
//                        }
//                    }
//            }else{
//                ToastUtils.showCustomToast(bean.getMsg());
//            }
//        } catch (Exception e) {
//           ToastUtils.showError(e);
//        }
//    }

}
