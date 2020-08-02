package com.qwb.view.tab.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.common.ScanTypeEnum;
import com.qwb.utils.MyRequestUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.base.model.JwtBean;
import com.qwb.view.base.model.JwtResult;
import com.qwb.view.base.model.BaseBean;
import com.qwb.event.ChangeCompanyEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.model.LoginBean;
import com.qwb.view.base.model.RoleBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.ScanBean;
import com.qwb.view.tab.model.BannerResult;
import com.qwb.view.tab.ui.XMainFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import com.zhy.http.okhttp.utils.MyUrlUtil;


import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 首页
 */
public class PXMain extends XPresent<XMainFragment>{

    private Activity context;
    private String phone;
    /**
     * 切换公司
     */
    public void queryBanner(Activity context) {
        OkHttpUtils
                .post()
                .url(Constans.getBanners)
                .addParams("token", SPUtils.getTK())
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson10(response);
                    }
                });
    }

    /**
     * 切换公司
     */
    public void queryDataChangeCompany(Activity context,String companyId) {
        if (context != null){
            this.context = context;
        }
        OkHttpUtils
                .post()
                .url(Constans.changeCompany)
                .addParams("token", SPUtils.getTK())
                .addParams("companyId", companyId)
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

    /**
     * 切换公司(独立版)
     */
    public void switchCompany(Activity context, String baseUrl, String jwt, String fromCompanyId, String toCompanyId) {
        if (MyStringUtil.isEmpty(baseUrl)) {
            baseUrl = Constans.ROOT;
        }
        OkHttpUtils
                .post()
                .url(baseUrl + Constans.switchCompany)
                .addParams("token", SPUtils.getTK())
                .addParams("jwt", jwt)
                .addParams("from", fromCompanyId)
                .addParams("to", toCompanyId)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson8(response);
                    }
                });
    }


    /**
     * 登录
     */
    public void queryDataLogin(Activity context, String phone, final String  pwd) {
        this.phone = phone;
        OkHttpUtils
                .post()
                .url(Constans.loginURL)
                .addParams("mobile", phone)
                .addParams("pwd", MD5.hex_md5(pwd))
                .addParams("unId", MyUtils.getImei(context))
                .id(2)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response, pwd);
                    }
                });
    }


    /**
     * 获取应用列表（角色）
     */
    public void loadDataRoleNew(Activity activity, String token) {
        OkHttpUtils
                .post()
                .addParams("token", token)
                .url(Constans.menus)
                .id(33)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson33(response);
                    }
                });
    }



    //申请成为会员
    public void AddShopMember(Activity context,String companyId) {
        OkHttpUtils
                .post()
                .url(Constans.shopMember_apply)
                .addParams("token", SPUtils.getTK())
                .addParams("companyId", companyId)
                .id(4)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response);
                    }
                });
    }

    /**
     * 独立服务器登录
     */
    public void queryLogin2(Activity activity, String jwt) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .addParams("jwt", jwt)
                .url(Constans.loginURL2)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson6(response);
                    }
                });
    }

    /**
     *
     */
    public void queryJwt(Activity activity, String baseUrl, final String companyId) {
        if (activity != null){
            this.context = activity;
        }
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .addParams("to", companyId)
                .url(Constans.jwt)
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


    /**
     * 处理扫描
     * 1.关注商城
     * 2.扫描登录
     */
    public void doScan(Activity activity, ScanBean scanBean){
        if (MyStringUtil.eq(ScanTypeEnum.FOLLOW.getType(), scanBean.getType())){
            AddShopMember(activity, String.valueOf(scanBean.getCompanyId()));
        } else if (MyStringUtil.eq(ScanTypeEnum.LOGIN.getType(), scanBean.getType())){
            doScanLogin(activity, scanBean.getTicket(), 2);
        }
    }

    /**
     * type: 1.取消，2.同意
     */
    public void doScanLogin(Activity activity, String ticket, final int type) {
        String url = Constans.qrcodeReject;
        if (2 == type){
            url = Constans.qrcodeAccept;
        }
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .addParams("ticket", ticket)
                .url(url)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson9(response, type);
                    }
                });
    }

    /**
     * 已扫描
     */
    public void doScaned(Activity activity, String ticket) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .addParams("ticket", ticket)
                .url(Constans.qrcodeScanned)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                    }
                });
    }


    //TODO ------------------------接口回調----------------------

    //解析数据-切换公司
    private void parseJson1(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {
                String domain = loginBean.getDomain();
                if(MyStringUtil.isEmpty(domain)){
                    queryDataLogin(null, SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE), SPUtils.getSValues(ConstantUtils.Sp.PASSWORD));
                }else{
                    MyLoginUtil.login(loginBean.getJwt(), loginBean.getDomain());
                    queryLogin2(null, loginBean.getJwt());
                }
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-登录
    private void parseJson2(String response, String pwd) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {
                String domain = loginBean.getDomain();
                if (MyStringUtil.isEmpty(domain)) {
                    loginBean.setMemberMobile(phone);
                    MyLoginUtil.login(loginBean, pwd, true);
                    MyLoginUtil.login(null, null);
                    //获取应用列表
                    loadDataRoleNew(null, loginBean.getToken());
                } else {
                    MyLoginUtil.login(loginBean.getJwt(), loginBean.getDomain());
                    queryLogin2(null, loginBean.getJwt());
                }
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-应用列表
    private void parseJson33(String response) {
        try {
            RoleBean bean = JSON.parseObject(response, RoleBean.class);//角色
            if (bean != null || bean.isState()) {
                MyLoginUtil.doApplyListNew(bean);
                //跳到首页
                BusProvider.getBus().post(new ChangeCompanyEvent());
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-扫描关注公司
    private void parseJson4(String response) {
        try {
            BaseBean baseBean = JSON.parseObject(response,BaseBean.class);
            if(baseBean!=null){
                ToastUtils.showCustomToast(baseBean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    private void parseJson6(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);//角色
            if (loginBean != null || loginBean.isState()) {
                loginBean.setRzState(1);
                MyLoginUtil.login(loginBean, SPUtils.getSValues(ConstantUtils.Sp.PASSWORD), true);
                loadDataRoleNew(null, loginBean.getToken());
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }

    private void parseJson7(String response) {
        try {
            JwtResult bean = JSON.parseObject(response, JwtResult.class);
            if (bean != null && (200 == bean.getCode() || bean.isState())) {
                MyUrlUtil.clearUrl();
                JwtBean jwtBean = bean.getData();
                if (MyStringUtil.isNotEmpty(jwtBean.getDomain())){
                    MyLoginUtil.login(jwtBean.getJwt(), jwtBean.getDomain());
                }else{
                    MyLoginUtil.login(null, null);
                }
                switchCompany(null,jwtBean.getDomain(), jwtBean.getJwt(), jwtBean.getFrom(), jwtBean.getTo());
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }


    //解析数据-切换公司(独立版)
    private void parseJson8(String response) {
        try {
            LoginBean loginBean = JSON.parseObject(response, LoginBean.class);
            if (loginBean != null && loginBean.isState()) {
                loginBean.setRzState(1);
                MyLoginUtil.login(loginBean, SPUtils.getSValues(ConstantUtils.Sp.PASSWORD), true);
                loadDataRoleNew(null, loginBean.getToken());
            } else {
                ToastUtils.showLongCustomToast(loginBean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-扫描授权登录
    private void parseJson9(String response, int type) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (MyRequestUtil.isSuccess(bean)) {
                ToastUtils.showLongCustomToast(bean.getMsg());
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-扫描授权登录
    private void parseJson10(String response) {
        try {
            BannerResult bean = JSON.parseObject(response, BannerResult.class);
            if (MyRequestUtil.isSuccess(bean)) {
                getV().doBanner(bean.getRows());
                ToastUtils.showLongCustomToast(bean.getMsg());
            } else {
                ToastUtils.showLongCustomToast(bean.getMsg());
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


}
