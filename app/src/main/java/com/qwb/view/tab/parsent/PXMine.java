package com.qwb.view.tab.parsent;


import android.app.Activity;
import com.alibaba.fastjson.JSON;
import com.qwb.view.base.model.VersionBean;
import com.qwb.view.base.model.VersionResult;
import com.qwb.view.tab.ui.XMineFragment;
import com.qwb.utils.MyAppUtil;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import java.util.HashMap;
import java.util.Map;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 我的
 */
public class PXMine extends XPresent<XMineFragment> {

    /**
     * 服务器获取服务器的版本号
     */
    public void queryDataUpdateVersion(Activity activity) {
        // 0:测试版本 4：Android发布版本
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("verSion", "0");
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.updateVerSionURL)
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





    //TODO ------------------------接口回調----------------------
    //解析数据
    private void parseJson1(String response) {
            try {
                VersionResult result = JSON.parseObject(response, VersionResult.class);
                if (result != null && result.isState()){
                    VersionBean bean = result.getVersion();
                    String isQz = bean.getIsQz();// 是否强制更新
                    String versionUrl = bean.getVersionUrl();// 下载地址
                    String versionContent = bean.getVersionContent();// 更新内容
                    Integer serviceVersionNo = bean.getAppNo();
                    boolean isUpdate = false;
                    int appVersionNo = MyAppUtil.getAppVersionNo();// 获取app当前版本号
                    if (serviceVersionNo != null && serviceVersionNo > appVersionNo){
                        isUpdate = true;
                    }
                    getV().doVersionUpdate(isUpdate, isQz, versionUrl, versionContent);
                }



//                JSONObject jsonObject = JSON.parseObject(response);
//                if (jsonObject != null && jsonObject.getBoolean("state")) {
//                    // 获取app当前版本号
//                    String appVersion = MyAppUtil.getAppVersion();
//                    String replace_appVersion = appVersion.replace(".", ",");
//                    String[] split_appVersion = replace_appVersion.split(",");// String.split(".")不能转化为数组
//                    double appVersion_1 = Double.valueOf(split_appVersion[0]);
//                    double appVersion_2 = Double.valueOf(split_appVersion[1]);
//                    double appVersion_3 = Double.valueOf(split_appVersion[2]);
//
//                    JSONObject version = jsonObject.getJSONObject("version");
//
//                    // 服务器的更新版本号
//                    String serviceVersion = version.getString("versionName");
//                    String replace_serviceVersion = serviceVersion.replace(".", ",");
//                    String[] split_serviceVersion = replace_serviceVersion.split(",");
//                    double serviceVersion_1 = Double.valueOf(split_serviceVersion[0]);
//                    double serviceVersion_2 = Double.valueOf(split_serviceVersion[1]);
//                    double serviceVersion_3 = Double.valueOf(split_serviceVersion[2]);
//
//
//
//                    boolean isUpdate = false;
//                    // @注意：版本格式为例如：0.0.0（两个“.”）； “0.0.0”分解成：0，0，0；
//                    // 先判断第一个后台版本大于当前版本弹出窗体（这边要强制更新，具体看参数isQz），相等或不于判断第二个
//                    // 第二个后台版本大于当前版本弹出窗体（这边要强制更新，具体看参数isQz），相等或不于判断第三个（这边要不强制更新，具体看参数isQz）
//                    // 第一层
//                    if (serviceVersion_1 > appVersion_1) {// 强
//                        isUpdate = true;
//                    } else {// 一般相等
//                        // 第二层
//                        if (serviceVersion_2 > appVersion_2) {// 强
//                            isUpdate = true;
//                        } else if (serviceVersion_2 == appVersion_2) {
//                            // 第三层
//                            if (serviceVersion_3 > appVersion_3) {// 弹出窗体
//                                isUpdate = true;
//                            } else {
//                                isUpdate = false;
//                            }
//                        } else {
//                            isUpdate = false;
//                        }
//                    }
//
//                    String isQz = version.getString("isQz");// 是否强制更新
//                    String versionUrl = version.getString("versionUrl");//下载地址
//                    String versionContent = version.getString("versionContent");//更新内容
//
//                    getV().doVersionUpdate(isUpdate, isQz, versionUrl, versionContent);
//                }
            } catch (Exception e) {
                ToastUtils.showError(e);
            }
    }


}


