package com.zhy.http.okhttp.builder;

import android.net.Uri;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.SPUtils;
import com.zhy.http.okhttp.request.GetRequest;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.MyUrlUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * Created by zhy on 15/12/14.
 */
public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable {
    @Override
    public RequestCall build() {
        if (params != null) {
            url = appendParams(url, params);
        }

        url = MyUrlUtil.getUrl(url);
        addToken();
        XLog.e("RequestCall-url", url);
        if (params != null){
            XLog.e("RequestCall-params", JSON.toJSONString(params));
        }
        return new GetRequest(url, tag, params, headers, id).build();
    }

    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }


    @Override
    public GetBuilder params(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val) {
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        if (MyStringUtil.isNotEmpty(val)) {
            params.put(key, val);
        }
        return this;
    }

    /**
     * 添加token
     */
    public void addToken(){
        if (this.params == null) {
            params = new LinkedHashMap<>();
        }
        if (!params.containsKey("token") && MyStringUtil.isNotEmpty(SPUtils.getTK())){
            params.put("token", SPUtils.getTK());
        }
    }


}
