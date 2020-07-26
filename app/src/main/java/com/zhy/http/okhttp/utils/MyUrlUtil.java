package com.zhy.http.okhttp.utils;

import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;

/**
 * 请求url判断更换基本独立域名（由登录接口返回）
 */

public class MyUrlUtil {

    public static String getUrl(String url) {
        String loginBaseUrl = SPUtils.getSValues(ConstantUtils.Sp.LOGIN_BASE_URL);
        if (MyStringUtil.isNotEmpty(loginBaseUrl) && url != null && url.startsWith(Constans.ROOT)) {
            if (!(
                    (url.contains("/web/login") && !url.contains("/web/login/standalone")) ||
                            url.contains("/web/reg") ||
                           url.contains("/web/regnew")
// ||
//                            url.contains("/web/likeCompanyNm") || //搜索公司
//                            url.contains("/web/applyInCompany")  //加入公司
            )) {
                url = url.replace(Constans.ROOT, loginBaseUrl);
            }
        }
//        XLog.e("url:" + url);
//        XLog.e("loginBaseUrl:" + loginBaseUrl);
        return url;
    }

    public static void clearUrl(){
        SPUtils.setValues(ConstantUtils.Sp.LOGIN_BASE_URL, "");
    }
}
