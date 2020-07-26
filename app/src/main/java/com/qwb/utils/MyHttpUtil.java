package com.qwb.utils;

import android.app.Activity;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;

import java.util.Map;

import cn.droidlover.xdroidmvp.log.XLog;
import okhttp3.Call;

/**
 * 网络请求在封装一层
 */

public class MyHttpUtil {

    private static MyHttpUtil mInstance;

    /**
     * 单实例
     */
    public static MyHttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (MyHttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new MyHttpUtil();
                }
            }
        }
        return mInstance;
    }

    public MyHttpUtil post(Activity activity, Map<String, String> params, String url){
        if(!MyNetWorkUtil.isNetworkConnected()){
            ToastUtils.showCustomToast("网络不流通，请检查网络");
        }else{

            OkHttpUtils.post()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new MyHttpCallback(activity) {
                        @Override
                        public void myOnError(Call call, Exception e, int id) {
                            if(null != listener){
                                listener.onError(call, e, id);
                            }
                        }

                        @Override
                        public void myOnResponse(String response, int id) {
                            if(null != listener){
                                listener.onSuccess(response, id);
                            }
                        }
                    });
        }
        return mInstance;
    }

    /**
     * 同时调用多个请求
     * @param activity
     * @param params
     * @param url
     * @param resultListener
     * @return
     */
    public void post(Activity activity, Map<String, String> params, String url,final ResultListener resultListener){
        if(!MyNetWorkUtil.isNetworkConnected()){
            ToastUtils.showCustomToast("网络不流通，请检查网络");
        }else{
            OkHttpUtils.post()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new MyHttpCallback(activity) {
                        @Override
                        public void myOnError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            XLog.e("e:"+e);
                            if(null != resultListener){
                                resultListener.onError(call, e, id);
                            }
                        }

                        @Override
                        public void myOnResponse(String response, int id) {
                            XLog.e("response:"+response);
                            if(null != resultListener){
                                resultListener.onSuccess(response, id);
                            }
                        }
                    });
        }
    }

    /**
     * 同时调用多个请求
     * @param activity
     * @param params
     * @param url
     * @param resultListener
     * @return
     */
    public void post(Activity activity, Map<String, String> params, String url,final ResultListener2 resultListener){
        if(!MyNetWorkUtil.isNetworkConnected()){
            ToastUtils.showCustomToast("网络不流通，请检查网络");
            if(null != resultListener){
                resultListener.onNoNetWork();
            }
        }else{
            OkHttpUtils.post()
                    .params(params)
                    .url(url)
                    .build()
                    .execute(new MyHttpCallback(activity) {
                        @Override
                        public void myOnError(Call call, Exception e, int id) {
                            XLog.e("e:"+e);
                            e.printStackTrace();
                            if(null != resultListener){
                                resultListener.onError(call, e, id);
                            }
                        }

                        @Override
                        public void myOnResponse(String response, int id) {
                            XLog.e("response:"+response);
                            if(null != resultListener){
                                resultListener.onSuccess(response, id);
                            }
                        }
                    });
        }
    }

    public ResultListener listener;
    public void setResultListener(ResultListener listener){
        this.listener = listener;
    }

    public interface ResultListener{
        void onError(Call call, Exception e, int id);
        void onSuccess(String response, int id);
    }

    public interface ResultListener2{
        void onError(Call call, Exception e, int id);
        void onSuccess(String response, int id);
        void onNoNetWork();
    }



}
