package com.qwb.view.tab.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.common.ScanTypeEnum;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.Constans;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.BaseBean;
import com.qwb.view.base.model.JwtBean;
import com.qwb.view.base.model.JwtResult;
import com.qwb.view.base.model.LoginBean;
import com.qwb.view.base.model.RoleBean;
import com.qwb.view.base.model.ScanBean;
import com.qwb.view.tab.model.MainResult;
import com.qwb.view.tab.model.ShopInfoResult;
import com.qwb.view.tab.ui.XMainFragment;
import com.qwb.view.tab.ui.XMainFragment2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 首页
 */
public class PXMain2 extends XPresent<XMainFragment2>{

    private Activity context;

    public void query(Activity context,String shopNo,String sdate,String edate) {
        OkHttpUtils
                .post()
                .url(Constans.sumPageStat)
                .addParams("token", SPUtils.getTK())
                .addParams("sdate", sdate)
                .addParams("edate", edate)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                             @Override
                             public void myOnError(Call call, Exception e, int id) {

                             }

                             @Override
                             public void myOnResponse(String response, int id) {
                                 parseJson1(response);
                             }
                });
    }

    public void queryShop(Activity context) {
        OkHttpUtils
                .post()
                .url(Constans.queryShopinfo)
                .addParams("token", SPUtils.getTK())
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }





    //TODO ------------------------接口回調----------------------
    private void parseJson1(String response) {
        try {
            MainResult bean = JSON.parseObject(response, MainResult.class);
            if (MyRequestUtil.isSuccess(bean)) {
                getV().doUI(bean);
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    private void parseJson2(String response) {
        try {
            ShopInfoResult bean = JSON.parseObject(response, ShopInfoResult.class);
            if (MyRequestUtil.isSuccess(bean)) {
                getV().showDialogShop(bean.getRows());
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }



}
