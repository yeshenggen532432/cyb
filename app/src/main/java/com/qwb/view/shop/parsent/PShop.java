package com.qwb.view.shop.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.MyRequestUtil;
import com.qwb.view.tab.model.HotShopBean;
import com.qwb.view.tab.model.ShopMyFollowResult;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.shop.ui.ShopActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.model.HotShopListBean;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：我的供货商
 */
public class PShop extends XPresent<ShopActivity>{

    /**
     * 我的供货商
     */
    public void queryShopMember(Activity context, final boolean isJump) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
//                .url(Constans.shopMember_mySuppliers)
                .url(Constans.myFollow)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
                            //关闭刷新
                            getV().closeRefresh();
                        }catch (Exception e1){
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response, isJump);
                    }
                });
    }

    /**
     */
    public void delShopMember(Activity context,String companyId,String fdId) {
//        token,fdId:会员关联企业主键，companyId
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .addParams("fdId", fdId)
                .addParams("companyId", companyId)
                .url(Constans.shopMember_cancel)
                .id(2)
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

    //申请成为会员
    public void AddShopMember(Activity context,String companyId) {
        OkHttpUtils
                .post()
                .url(Constans.shopMember_apply)
                .addParams("token", SPUtils.getTK())
                .addParams("companyId", companyId)
                .id(3)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson3(response);
                    }
                });
    }

    public void toShoppingMall(Activity context, String companyId, String mobile, final String title, final boolean isJump) {
        OkHttpUtils
                .post()
                .url(Constans.toShoppingMall)
                .addParams("token", SPUtils.getTK())
                .addParams("companyId", companyId)
                .addParams("mobile", mobile)
                .id(4)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson4(response, title, isJump);
                    }
                });
    }

    /**
     * 热门商城
     */
    public void queryDataHotShop(Activity activity, int pageNo, String mSearchStr) {
        OkHttpUtils
                .post()
                .url(Constans.hotShop)
                .addParams("token", SPUtils.getTK())
                .addParams("page", String.valueOf(pageNo))
                .addParams("name", mSearchStr)
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
                        parseJson5(response);
                    }
                });
    }


    /**
     * 申请加入公司
     */
    public void addJoinCompany(Activity activity, int companyId) {
        OkHttpUtils
                .post()
                .url(Constans.applyInCompanyURL)
                .addParams("token", SPUtils.getTK())
                .addParams("companyId", String.valueOf(companyId))
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
    //解析数据 我的供货商列表
    private void parseJson1(String response, boolean isJump) {
        try {
            ShopMyFollowResult parseObject = JSON.parseObject(response,ShopMyFollowResult.class);
            if (parseObject != null) {
                if (MyRequestUtil.isSuccess(parseObject)) {
                    List<HotShopBean> dataList = parseObject.getObj();
                    getV().refreshAdapter(dataList);
                    String normalShopCompanyId = SPUtils.getSValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_ID);
                    String normalShopCompanyName = SPUtils.getSValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_NAME);
                    String normalShopCompanyUrl = SPUtils.getSValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_URL);
                    if(!MyStringUtil.isEmpty(normalShopCompanyId) && !MyStringUtil.isEmpty(normalShopCompanyName) && !MyStringUtil.isEmpty(normalShopCompanyUrl) && isJump){
                        toShoppingMall(null,normalShopCompanyId,SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE),normalShopCompanyName,true);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-取消关注
    private void parseJson2(String response) {
        try {
            BaseBean baseBean=JSON.parseObject(response, BaseBean.class);
            if (baseBean != null){
                if (baseBean.isState()) {
                    ToastUtils.showCustomToast(baseBean.getMsg());
                    getV().delSuccess();
                } else {
                    ToastUtils.showCustomToast(baseBean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-扫描关注公司
    private void parseJson3(String response) {
        try {
            BaseBean baseBean = JSON.parseObject(response,BaseBean.class);
            if(baseBean!=null){
                if(baseBean.isState()){
                    ToastUtils.showCustomToast("关注成功");
                    getV().addSuccess();
                }else{
                    ToastUtils.showCustomToast(baseBean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据-进入商城
    private void parseJson4(String response, String title, boolean isJump) {
        try {
            JSONObject jsonObject=JSON.parseObject(response);
            if(null != jsonObject && jsonObject.getBoolean("state")){
                if(isJump){
                    getV().jumpMall(jsonObject.getString("url"), title);
                }else{
                    SPUtils.setValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_NAME, title);
                    SPUtils.setValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_URL, jsonObject.getString("url"));
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //热门商家
    private void parseJson5(String response) {
        try {
            HotShopListBean bean = JSON.parseObject(response, HotShopListBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    HotShopListBean.Obj obj = bean.getObj();
                    if(obj != null && obj.getRows() != null){
                        getV().refreshAdapterHotShop(obj.getRows());
                    }
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}


