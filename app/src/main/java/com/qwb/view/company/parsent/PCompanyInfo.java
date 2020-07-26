package com.qwb.view.company.parsent;


import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.view.base.model.BaseBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.company.model.CompanyIndustryBean;
import com.qwb.view.company.model.CompanyIndustryCategoryBean;
import com.qwb.view.company.model.CompanyInfoBean;
import com.qwb.view.company.ui.CompanyInfoActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：公司信息
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PCompanyInfo extends XPresent<CompanyInfoActivity>{

    private Activity mActivity;

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
                        parseJson11(response);
                    }
                });
    }

    /**
     * 所属行业分类
     */
    public void queryCompanyCategory(Activity activity, String industryId, final boolean show) {
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
                        parseJson13(response, show);
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
                        parseJson12(response);
                    }
                });
    }
    /**
     * 所属行业
     */
    public void addCompanySave(Activity activity, String name, String industryId, String categoryId, String brand,
                               String leader,String tel, String email, String employeeCount, String salesmanCount,
                               String bizLicenseNumber, List<String> picList) {
        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);}
        });
        if (null != picList && !picList.isEmpty()) {
            for (int i = 0; i < picList.size(); i++) {
                File oldFile=new File(picList.get(i));
                File newFile = new CompressHelper.Builder(activity)
                        .setMaxWidth(720)  // 默认最大宽度为720
                        .setMaxHeight(1280) // 默认最大高度为960
                        .setQuality(80)    // 默认压缩质量为80
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                        .setFileName(String.valueOf(System.currentTimeMillis()+"_"+i)) // 设置你的文件名
                        .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                        .build()
                        .compressToFile(oldFile);
                //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                String absolutePath=newFile.getAbsolutePath();
                String j=absolutePath.substring(absolutePath.length()-6,absolutePath.length()-5);
                files.put("file" + j, newFile);
            }
        }
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
                .files(files)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson14(response);
                    }
                });
    }


    //解析数据
    private void parseJson11(String response) {
        try {
            CompanyInfoBean bean = JSON.parseObject(response, CompanyInfoBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().showData(bean.getData());
                    }
                }else{
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
    //解析数据
    private void parseJson12(String response) {
        try {
            CompanyIndustryBean bean = JSON.parseObject(response, CompanyIndustryBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().refreshAdapterIndustry(bean.getData());
                    }
                }else{
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
    //解析数据
    private void parseJson13(String response, boolean show) {
        try {
            CompanyIndustryCategoryBean bean = JSON.parseObject(response, CompanyIndustryCategoryBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().refreshAdapterCategory(bean.getData(), show);
                    }
                }else{
                    ToastUtils.showCustomToast(bean.getMsg());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
    //解析数据
    private void parseJson14(String response) {
        try {
            BaseBean bean = JSON.parseObject(response, BaseBean.class);
            if (bean != null) {
                if (bean.isState()) {
                    if(getV()!=null){
                        getV().addDataSuccess();
                    }
                }
                ToastUtils.showCustomToast(bean.getMsg());
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
}
