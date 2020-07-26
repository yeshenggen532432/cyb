package com.qwb.view.storehouse.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.view.storehouse.model.StorehouseBean;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.view.storehouse.model.StorehouseInResult;
import com.qwb.view.storehouse.model.StorehouseInSubBean;
import com.qwb.view.storehouse.model.StorehouseStayInBean;
import com.qwb.view.storehouse.model.StorehouseStayInResult;
import com.qwb.view.storehouse.model.StorehouseListResult;
import com.qwb.view.storehouse.model.StorehouseStayInSubBean;
import com.qwb.view.storehouse.ui.StorehouseInActivity;
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
 * 库位：入仓单
 */
public class PStorehouseIn extends XPresent<StorehouseInActivity> {

    private Activity context;


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
                .url(Constans.queryStkCrewIn)
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
     *
     */
    public void queryStorehouseByBillId(Activity activity, Integer billId) {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("billId", String.valueOf(billId));
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryStkInByBillId)
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
     * 提交数据或修改数据
     */
    public void addData(Activity activity, int id, int stkId, String time, int sourceId, String sourceNo, String remark, String json, Integer billId) {
        this.context = activity;
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("bizType", "KWRK");
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
                .url(Constans.saveStkCreIn)
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
            StorehouseStayInResult bean = JSON.parseObject(response, StorehouseStayInResult.class);
            if (bean != null) {
                if (bean.isState()) {
                    //把StorehouseStayInBean转为StorehouseInBean
                    StorehouseStayInBean stayInBean = bean.getData();
                    StorehouseInBean inBean = new StorehouseInBean();
//                    inBean.setId(stayInBean.getId());
//                    inBean.setBillNo(stayInBean.getBillNo());
//                    inBean.setStatus(stayInBean.getStatus());
//                    outBean.setInTime(stayOutBea);
//                    outBean.setBizType(stayOutBean.getbi);
//                    inBean.setRemarks(stayInBean.getRemarks());
//                    inBean.setSubmitUser(stayInBean.getSubmitUser());
//                    inBean.setSubmitTime(stayInBean.getSubmitTime());
//                    inBean.setCancelUser(stayInBean.getCancelUser());
//                    inBean.setCancelTime(stayInBean.getCancelTime());
//                    outBean.setProId(stayOutBean.getp);
//                    inBean.setProType(stayInBean.getProType());
//                    inBean.setNewTime(stayInBean.getNewTime());
//                    outBean.setHouseId(stayOutBean.geth);
                    inBean.setSourceId(stayInBean.getId());
                    inBean.setSourceNo(stayInBean.getBillNo());
//                    inBean.setSdate(stayInBean.getSdate());
//                    inBean.setEdate(stayInBean.getEdate());
//                    inBean.setInDate(stayInBean.getInDate());
//                    outBean.setHouseName(stayOutBean.geth);
                    inBean.setStkId(stayInBean.getStkId());
                    inBean.setStkName(stayInBean.getStkName());

                    List<StorehouseStayInSubBean> stayInSubList=  stayInBean.getList();
                    if(MyCollectionUtil.isNotEmpty(stayInSubList)){
                        List<StorehouseInSubBean> inSubList = new ArrayList<>();
                        for (StorehouseStayInSubBean stayInSubBean : stayInSubList ) {
                            StorehouseInSubBean inSubBean = new StorehouseInSubBean();
                            inSubBean.setBeUnit(stayInSubBean.getBeUnit());
                            inSubBean.setHsNum(stayInSubBean.getHsNum());
                            inSubBean.setId(stayInSubBean.getId());
                            inSubBean.setMastId(inBean.getId());
                            inSubBean.setWareId(stayInSubBean.getWareId());
                            inSubBean.setQty(stayInSubBean.getQty());
                            inSubBean.setPrice(stayInSubBean.getPrice());
                            inSubBean.setAmt(stayInSubBean.getAmt());
                            inSubBean.setUnitName(stayInSubBean.getUnitName());
                            inSubBean.setProductDate(stayInSubBean.getProductDate());
//                            outSubBean.setProId(stayOutSubBean.getpro);
//                            outSubBean.setProName(stayOutSubBean.getpro);
//                            outSubBean.setProType(stayOutSubBean.getprot);
                            inSubBean.setInStkId(stayInSubBean.getInStkId());
                            inSubBean.setInStkName(stayInSubBean.getInStkName());
                            inSubBean.setMinQty(stayInSubBean.getMinQty());
                            inSubBean.setInQty(stayInSubBean.getInQty());
                            inSubBean.setInQty1(stayInSubBean.getInQty1());
                            inSubBean.setWareNm(stayInSubBean.getWareNm());
                            inSubBean.setWareGg(stayInSubBean.getWareGg());
                            inSubBean.setWareCode(stayInSubBean.getWareCode());
                            inSubBean.setWareDw(stayInSubBean.getWareDw());
                            inSubBean.setMaxUnitCode(stayInSubBean.getMaxUnitCode());
                            inSubBean.setMinUnitCode(stayInSubBean.getMinUnitCode());
                            inSubBean.setMinUnit(stayInSubBean.getMinUnit());

                            inSubList.add(inSubBean);
                        }
                        inBean.setList(inSubList);
                    }

                    getV().doUI(inBean);
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
            StorehouseInResult bean = JSON.parseObject(response, StorehouseInResult.class);
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
