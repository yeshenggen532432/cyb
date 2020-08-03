package com.qwb.view.tab.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qwb.utils.MyRequestUtil;
import com.qwb.view.tab.model.HotShopBean;
import com.qwb.view.tab.model.ShopMyFollowResult;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.model.HotShopListBean;
import com.qwb.view.tab.ui.XShopFragment;
import com.qwb.utils.MyStringUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 商城
 */
public class PXShop extends XPresent<XShopFragment> {

    /**
     * 我的供货商
     */
    public void queryShopMember(Activity context, final boolean isJump) {
        OkHttpUtils
                .post()
                .addParams("token", SPUtils.getTK())
                .url(Constans.shopMember_mySuppliers)
//                .url(Constans.myFollow)
                .id(1)
                .build()
                .execute(new MyHttpCallback(context) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
                            //关闭刷新
                            getV().closeRefresh();
                        } catch (Exception e1) {
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
    public void delShopMember(Activity context, String companyId, String fdId) {
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
    public void AddShopMember(Activity context, String companyId) {
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
    public void queryDataHotShop(Activity activity, int pageNo, String mSearchStr, int pageSize) {
        OkHttpUtils
                .post()
                .url(Constans.hotShop)
                .addParams("token", SPUtils.getTK())
                .addParams("page", String.valueOf(pageNo))
                .addParams("size", String.valueOf(pageSize))
                .addParams("name", mSearchStr)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                        try {
                            getV().closeRefresh();
                        } catch (Exception e1) {
                        }
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson5(response);
                    }
                });
    }


    //TODO ------------------------接口回調----------------------
    //解析数据 我的供货商列表
    private void parseJson1(String response, boolean isJump) {
        try {
            ShopMyFollowResult parseObject = JSON.parseObject(response, ShopMyFollowResult.class);
            if (parseObject != null) {
                if (MyRequestUtil.isSuccess(parseObject)) {
                    //TODO 因为缓存旧数据的原因，先清空
                    if (SPUtils.getBoolean_true(ConstantUtils.Sp.CLEAR_NORMAL_SHOP_COMPANY)) {
                        SPUtils.setValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_ID, null);
                        SPUtils.setValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_NAME, null);
                        SPUtils.setBoolean(ConstantUtils.Sp.CLEAR_NORMAL_SHOP_COMPANY, false);
                    }

                    List<HotShopBean> dataList = parseObject.getObj();
                    String normalShopCompanyId = SPUtils.getSValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_ID);
                    String normalShopCompanyName = SPUtils.getSValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_NAME);
                    //默认商城没有时：
                    if (MyStringUtil.isEmpty(normalShopCompanyId)) {
                        boolean flag = true;
                        //TODO 默认遍历本公司的
                        normalShopCompanyId = SPUtils.getCompanyId();
                        for (HotShopBean bean : dataList) {
                            int companyId = bean.getId();
                            if (normalShopCompanyId.equals(String.valueOf(companyId))) {
                                normalShopCompanyName = bean.getName();
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            normalShopCompanyId = "285";
                            for (HotShopBean bean : dataList) {
                                int companyId = bean.getId();
                                if (normalShopCompanyId.equals(String.valueOf(companyId))) {
                                    normalShopCompanyName = bean.getName();
                                    break;
                                }
                            }
                        }
                    }
                    getV().refreshAdapter(dataList);
                    if (!MyStringUtil.isEmpty(normalShopCompanyId) && !MyStringUtil.isEmpty(normalShopCompanyName) && isJump) {
                        toShoppingMall(null, normalShopCompanyId, SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE), normalShopCompanyName, true);
                    }
                } else {
                    ToastUtils.showCustomToast(parseObject.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }


    //解析数据-取消关注
    private void parseJson2(String response) {
        try {
            BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
            if (baseBean != null) {
                if (baseBean.isState()) {
                    ToastUtils.showCustomToast(baseBean.getMsg());
                    getV().delSuccess();
                } else {
                    ToastUtils.showCustomToast(baseBean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

    //解析数据-扫描关注公司
    private void parseJson3(String response) {
        try {
            BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
            if (baseBean != null) {
                if (baseBean.isState()) {
                    ToastUtils.showCustomToast("关注成功");
                    getV().addSuccess();
                } else {
                    ToastUtils.showCustomToast(baseBean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据-进入商城
    private void parseJson4(String response, String title, boolean isJump) {
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if (null != jsonObject && jsonObject.getBoolean("state")) {
                if (isJump) {
                    getV().jumpMall(jsonObject.getString("url"), title);
                }
            }
        } catch (Exception e) {
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
                    if (obj != null && obj.getRows() != null) {
                        getV().refreshAdapterHotShop(obj.getRows());
                    }
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        } catch (Exception e) {
            ToastUtils.showCustomToast(e.getMessage());
        }
    }

}


