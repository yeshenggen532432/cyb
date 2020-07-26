package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseListResult;
import com.qwb.view.storehouse.model.StorehouseOutBean;
import com.qwb.view.storehouse.model.StorehouseOutResult;
import com.qwb.view.storehouse.model.StorehouseOutSubBean;
import com.qwb.view.storehouse.model.StorehouseStayOutBean;
import com.qwb.view.storehouse.model.StorehouseStayOutResult;
import com.qwb.view.storehouse.model.StorehouseStayOutSubBean;
import com.qwb.view.storehouse.ui.StorehouseOutActivity;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
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
 * 库位：出仓单
 */
public class PStorehouseOut extends XPresent<StorehouseOutActivity> {

    private Activity context;


    /**
     * 待出发票单的
     */
    public void queryStorehouseByBillId(Activity activity, Integer billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkOutByBillId)
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
     * 出仓单的
     */
    public void queryStorehouseOutByBillId(Activity activity, Integer billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkCrewOut)
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
     * 提交数据或修改数据
     */
    public void addData(Activity activity,int id,  int stkId, String time, int sourceId, String sourceNo, String remark, String json, Integer billId) {
        this.context = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bizType", "KWCK");
        params.put("remarks", remark);
        params.put("stkId", String.valueOf(stkId));
        params.put("sourceId", String.valueOf(sourceId));
        params.put("sourceNo", sourceNo);
        params.put("inDate", time);
        params.put("json", json);
        params.put("id", String.valueOf(id));
        params.put("billId", String.valueOf(billId));


        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.saveStkCreOut)
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
            StorehouseStayOutResult bean = JSON.parseObject(response, StorehouseStayOutResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    //把StorehouseStayOutBean转为StorehouseOutBean
                    StorehouseStayOutBean stayOutBean = bean.getData();
                    StorehouseOutBean outBean = new StorehouseOutBean();
//                    outBean.setId(stayOutBean.getId());
//                    outBean.setBillNo(stayOutBean.getBillNo());
//                    outBean.setStatus(stayOutBean.getStatus());
//                    outBean.setInTime(stayOutBea);
//                    outBean.setBizType(stayOutBean.getbi);
//                    outBean.setRemarks(stayOutBean.getRemarks());
//                    outBean.setSubmitUser(stayOutBean.getSubmitUser());
//                    outBean.setSubmitTime(stayOutBean.getSubmitTime());
//                    outBean.setCancelUser(stayOutBean.getCancelUser());
//                    outBean.setCancelTime(stayOutBean.getCancelTime());
//                    outBean.setProId(stayOutBean.getp);
//                    outBean.setProType(stayOutBean.getProType());
//                    outBean.setNewTime(stayOutBean.getNewTime());
//                    outBean.setHouseId(stayOutBean.geth);
                    outBean.setSourceId(stayOutBean.getId());
                    outBean.setSourceNo(stayOutBean.getBillNo());
//                    outBean.setSdate(stayOutBean.getSdate());
//                    outBean.setEdate(stayOutBean.getEdate());
//                    outBean.setInDate(stayOutBean.getInDate());
//                    outBean.setHouseName(stayOutBean.geth);
                    outBean.setStkId(stayOutBean.getStkId());
                    outBean.setStkName(stayOutBean.getStkName());

                    List<StorehouseStayOutSubBean> stayOutSubList =  stayOutBean.getList();
                    if(MyCollectionUtil.isNotEmpty(stayOutSubList)){
                        List<StorehouseOutSubBean> outSubList = new ArrayList<>();
                        for (StorehouseStayOutSubBean stayOutSubBean:stayOutSubList ) {
                            StorehouseOutSubBean outSubBean = new StorehouseOutSubBean();
                            outSubBean.setBeUnit(stayOutSubBean.getBeUnit());
                            outSubBean.setHsNum(stayOutSubBean.getHsNum());
                            outSubBean.setId(stayOutSubBean.getId());
                            outSubBean.setMastId(outBean.getId());
                            outSubBean.setWareId(stayOutSubBean.getWareId());
                            outSubBean.setQty(stayOutSubBean.getQty());
                            outSubBean.setPrice(stayOutSubBean.getPrice());
                            outSubBean.setAmt(stayOutSubBean.getAmt());
                            outSubBean.setUnitName(stayOutSubBean.getUnitName());
                            outSubBean.setProductDate(stayOutSubBean.getProductDate());
//                            outSubBean.setProId(stayOutSubBean.getpro);
//                            outSubBean.setProName(stayOutSubBean.getpro);
//                            outSubBean.setProType(stayOutSubBean.getprot);
                            outSubBean.setOutStkId(stayOutSubBean.getOutStkId());
                            outSubBean.setOutStkName(stayOutSubBean.getOutStkName());
                            outSubBean.setOutQty(stayOutSubBean.getOutQty());
                            outSubBean.setOutQty1(stayOutSubBean.getOutQty1());
                            outSubBean.setWareNm(stayOutSubBean.getWareNm());
                            outSubBean.setWareGg(stayOutSubBean.getWareGg());
                            outSubBean.setWareCode(stayOutSubBean.getWareCode());
                            outSubBean.setWareDw(stayOutSubBean.getWareDw());
                            outSubBean.setMaxUnitCode(stayOutSubBean.getMaxUnitCode());
                            outSubBean.setMinUnitCode(stayOutSubBean.getMinUnitCode());
                            outSubBean.setMinUnit(stayOutSubBean.getMinUnit());

                            outSubList.add(outSubBean);
                        }
                        outBean.setList(outSubList);
                    }

                    getV().doUI(outBean);
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
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

    //解析数据
    private void parseJson3(String response) {
        try {
            StorehouseOutResult bean = JSON.parseObject(response, StorehouseOutResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().doUI(bean.getData());
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
