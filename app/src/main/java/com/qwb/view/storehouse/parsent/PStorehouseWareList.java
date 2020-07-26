package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseListResult;
import com.qwb.view.storehouse.model.StorehouseWareListResult;
import com.qwb.view.storehouse.ui.StorehouseWareListActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.stk.StorageBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 库位：库位商品列表
 */
public class PStorehouseWareList extends XPresent<StorehouseWareListActivity> {

    private Activity context;

    /**
     *
     */
    public void queryData(Activity activity, int pageNo, String stkId, String houseId, String search) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("pageNo", String.valueOf(pageNo));
        params.put("pageSize", String.valueOf(10));
        params.put("stkId", String.valueOf(stkId));// 仓库
        params.put("houseId", String.valueOf(houseId));//库位
        params.put("wareNm", String.valueOf(search));//
//        params.put("isType", String.valueOf(isType));//
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkHouseWarePage)
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
     * 获取仓库
     */
    public void queryStorage(Activity activity) {
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
                        parseJson26(response);
                    }
                });
    }

    /**
     * 获取库位
     */
    public void queryStorehouseList(Activity activity, String stkId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", String.valueOf(stkId));
        params.put("status", "1");
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStorehouseList)
                .id(27)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson27(response);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response) {
        try {
            StorehouseWareListResult bean = JSON.parseObject(response, StorehouseWareListResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().refreshAdapter(bean.getList());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-仓库列表
    private void parseJson26(String response) {
        try {
            StorageBean parseObject = JSON.parseObject(response, StorageBean.class);
            if (null != parseObject && parseObject.isState()) {
                List<StorageBean.Storage> datas = parseObject.getList();
                if (getV() != null) {
                    getV().showDialogStorage(datas);
                }
            } else {
                ToastUtils.showCustomToast(parseObject.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson27(String response) {
        try {
            StorehouseListResult bean = JSON.parseObject(response, StorehouseListResult.class);
            if (null != bean) {
                List<StorehouseBean> datas = bean.getList();
                getV().showDialogStorehouse(datas);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }





}
