package com.qwb.view.base.parsent;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.ui.XRegisterNextActivity;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.model.LoginBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.XbaseBean;
import com.qwb.view.tab.ui.XTabActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.callback.StringCallback;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 注册(下一步)
 */
public class PXRegisterNext extends XPresent<XRegisterNextActivity> {

    private String pwd;
    private String name;
    private Activity context;
    /**
     *
     */
    public void submit(Activity context,String name,String pwd) {
        this.pwd = pwd;
        this.name = name;
        this.context = context;
        OkHttpUtils
                .post()
                .url(Constans.UPDATE_MEMBER)
                .addParams("id", SPUtils.getID())
                .addParams("name", name)
                .addParams("password", MD5.hex_md5(pwd))
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

    //TODO ------------------------接口回調----------------------
    public class MyStringCallback extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            switch (id) {
                case 1://
                    parseJson1(response);
                    break;
            }
        }
    }

    //解析数据
    private void parseJson1(String response) {
        try {
            XbaseBean xbaseBean = JSON.parseObject(response, XbaseBean.class);
            if (xbaseBean != null && xbaseBean.isState()) {
                LoginBean loginBean = new LoginBean();
                loginBean.setCompanyId(Integer.valueOf(SPUtils.getCompanyId()));
                loginBean.setMemberNm(name);
                loginBean.setMemberMobile(SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE));
                loginBean.setMemId(Integer.valueOf(SPUtils.getSValues(ConstantUtils.Sp.USER_ID)));
                MyLoginUtil.updateNameAndPwd(loginBean,pwd,true);
                ActivityManager.getInstance().jumpMainActivity((Activity)context,XTabActivity.class);
            }
            ToastUtils.showLongCustomToast(xbaseBean.getMessage());
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


}



