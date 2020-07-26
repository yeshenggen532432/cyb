package com.qwb.view.company.parsent;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.company.model.SearchCompanyBean;
import com.qwb.view.company.ui.MyCompanyActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：我的单位
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PMyCompany extends XPresent<MyCompanyActivity>{

    /**
     * 搜索公司
     */
    public void queryCompany(Activity activity, String searchStr) {
        OkHttpUtils
                .post()
                .url(Constans.likeCompanyNmURL)
                .addParams("token", SPUtils.getTK())
                .addParams("searchNm", searchStr)
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
                        parseJson2(response);
                    }
                });
    }
    /**
     * 公司信息
     */
    public void queryCompanyInfo(Activity activity) {
        OkHttpUtils
                .post()
                .url(Constans.company_info)
                .addParams("token", SPUtils.getTK())
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
    /**
     * 所属行业分类
     */
    public void queryCompanyCategory(Activity activity, String industryId) {
        OkHttpUtils
                .post()
                .url(Constans.company_category)
                .addParams("industryId", industryId)
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
    /**
     * 所属行业
     */
    public void queryCompanyIndustry(Activity activity) {
        OkHttpUtils
                .post()
                .url(Constans.company_industry)
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
    /**
     * 所属行业
     */
    public void addCompanySave(Activity activity, String name, String industryId, String categoryId, String brand,
                                 String leader,String tel, String email, String employeeCount, String salesmanCount,
                                 String bizLicenseNumber, String bizLicensePic) {
        OkHttpUtils
                .post()
                .url(Constans.company_save)
                .addParams("token", SPUtils.getTK())
                .addParams("name", name)
                .addParams("industryId", industryId)
                .addParams("categoryId", categoryId)
                .addParams("brand", brand)
                .addParams("leader", leader)
                .addParams("tel", tel)
                .addParams("email", email)
                .addParams("employeeCount", employeeCount)
                .addParams("salesmanCount", salesmanCount)
                .addParams("bizLicenseNumber", bizLicenseNumber)
                .addParams("bizLicensePic", bizLicensePic)
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
    //解析数据
    private void parseJson1(String response) {
        try {
            SearchCompanyBean bean = JSON.parseObject(response, SearchCompanyBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    List<SearchCompanyBean.Company> dataList = bean.getCompanys();
                    getV().refreshAdapterPop(dataList);
                } else {
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
    //解析数据
    private void parseJson2(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    getV().addSuccess();
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }



}


