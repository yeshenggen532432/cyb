package com.qwb.view.company.parsent;

import android.app.Activity;
import android.graphics.Bitmap;

import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.CodeBean;
import com.qwb.view.company.model.CompanyIndustryBean;
import com.qwb.view.company.model.CompanyIndustryCategoryBean;
import com.qwb.view.company.model.CompanyPicBean;
import com.qwb.view.company.model.CompanyRegisterBean;
import com.qwb.view.company.model.CompanySalesmanBean;
import com.qwb.view.company.ui.RegisterCompanyActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 创建描述：创建公司
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class PRegisterCompany extends XPresent<RegisterCompanyActivity>{

    private String smsToken;//短信token

    /**
     * 发送验证码
     */
    public void sendCode(Activity activity, String phone) {
        OkHttpUtils
                .post()
                .addParams("mobile", phone)
                .addParams("token", SPUtils.getTK())
                .url(Constans.COMPANY_REGISTER_CODE)
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
     * 上传图片
     */
    public void uploadPic(Activity activity, List<String> picList) {
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
                files.put("file", newFile);
            }
        }
        OkHttpUtils
                .post()
                .url(Constans.COMMON_UPLOAD)
                .addParams("token", SPUtils.getTK())
                .files(files)
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
     * 创建公司
     */
    public void addCompanySave(final Activity activity, String name, String industryId, String categoryId, String brand,
                               String leader,String tel, String email, String employeeCount, String salesmanCount,
                               String bizLicenseNumber, String picUrl, String code, String mobile, String salesman, String salesmanId) {
        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>(){
            @Override
            public int compare(String o1, String o2) {return o1.compareTo(o2);}
        });
        OkHttpUtils
                .post()
                .url(Constans.COMPANY_REGISTER)
                .addParams("token", SPUtils.getTK())
                .addParams("smsToken", smsToken)//短信token
                .addParams("code", code)
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
                .addParams("bizLicensePic", picUrl)
                .addParams("mobile", mobile)
                .addParams("salesman", salesman)
                .addParams("salesmanId", salesmanId)
                .files(files)
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
     * 所属行业
     */
    public void queryCompanyIndustry(Activity activity) {
        OkHttpUtils
                .post()
                .url(Constans.company_industry)
                .addParams("token", SPUtils.getTK())
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
     * 所属行业分类
     */
    public void queryCompanyCategory(Activity activity, String industryId, final boolean show) {
        OkHttpUtils
                .post()
                .url(Constans.company_category)
                .addParams("industryId", industryId)
                .addParams("token", SPUtils.getTK())
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
     * 业务员
     */
    public void querySalesman(Activity activity) {
        OkHttpUtils
                .get()
                .addParams("token", SPUtils.getTK())
                .url(Constans.company_register_salesman)
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
    private void parseJson1(String response) {
        try {
            CodeBean bean = JSON.parseObject(response, CodeBean.class);
            if (bean != null) {
                if(200 == bean.getCode()){
                    smsToken = bean.getData();
                    ToastUtils.showCustomToast("发送成功");
                }else{
                    ToastUtils.showCustomToast(bean.getMessage());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            CompanyRegisterBean bean = JSON.parseObject(response, CompanyRegisterBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    ToastUtils.showCustomToast("注册成功");
                    getV().addDataSuccess();
                }else{
                    ToastUtils.showCustomToast(bean.getMessage());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson3(String response) {
        try {
            CompanyPicBean bean = JSON.parseObject(response, CompanyPicBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    getV().addDataPicSuccess(bean.getData().getName());
                }else{
                    ToastUtils.showCustomToast(bean.getMessage());
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
            CompanySalesmanBean bean = JSON.parseObject(response, CompanySalesmanBean.class);
            if (bean != null) {
                if (200 == bean.getCode()) {
                    getV().doSalesman(bean.getData());
//                    getV().showDialogSalesman(bean.getData());
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

}
