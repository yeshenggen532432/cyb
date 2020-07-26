package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.view.storehouse.model.StorehouseArrangeResult;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseListResult;
import com.qwb.view.storehouse.model.StorehouseWareBean;
import com.qwb.view.storehouse.model.StorehouseWareListResult;
import com.qwb.view.storehouse.ui.StorehouseArrangeActivity;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.stk.StorageBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 库位：库位整理
 */
public class PStorehouseArrange extends XPresent<StorehouseArrangeActivity> {

    private Activity context;


    /**
     *
     */
    public void queryStorehouseById(Activity activity, Integer billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkCrewById)
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
    public void queryStorehouseList(Activity activity, Integer stkId) {
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

    /**
     * 查询库位商品
     */
    public void queryStkHouseWareList(Activity activity, Integer stkId, String storehouseIds, String wareIds) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("stkId", String.valueOf(stkId));
        params.put("houseIds", storehouseIds);
        params.put("wareIds", wareIds);
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkHouseWareList)
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
     * 提交数据或修改数据
     */
    public void addData(Activity activity, int stkId, String time, int sourceId, String sourceNo, String remark, String json, int billId) {
        this.context = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bizType", "KWLH");
        params.put("remarks", remark);
        params.put("stkId", String.valueOf(stkId));
//        params.put("sourceId", String.valueOf(sourceId));
//        params.put("sourceNo", sourceNo);
        params.put("inDate", time);
        params.put("json", json);
        params.put("id", String.valueOf(billId));

        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.saveStkCrew)
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


    //解析数据
    private void parseJson1(String response) {
        try {
            StorehouseArrangeResult bean = JSON.parseObject(response, StorehouseArrangeResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().doUI(bean.getStkCrew());
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    ToastUtils.showCustomToast("保存成功");
                    ActivityManager.getInstance().closeActivity(context);
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
                List<TreeBean> tree = new ArrayList<>();
                tree.clear();

                if(MyCollectionUtil.isNotEmpty(datas)){
                    for (StorehouseBean storehouse: datas) {
                        TreeBean fileBean = new TreeBean(storehouse.getId(), -1, storehouse.getHouseName());
                        tree.add(fileBean);
                    }
                }
                getV().doStorehouseTree(tree, datas);

            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson28(String response) {
        try {
            StorehouseWareListResult bean = JSON.parseObject(response, StorehouseWareListResult.class);
            if (null != bean && bean.isState()) {
                List<StorehouseWareBean> datas = bean.getList();
                getV().doTable(datas);
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


}
