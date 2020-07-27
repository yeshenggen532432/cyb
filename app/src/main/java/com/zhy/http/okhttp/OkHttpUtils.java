package com.zhy.http.okhttp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.qwb.utils.Constans;
import com.qwb.utils.MD5;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.HeadBuilder;
import com.zhy.http.okhttp.builder.OtherRequestBuilder;
import com.zhy.http.okhttp.builder.PostFileBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;
import com.zhy.http.okhttp.utils.Platform;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Executor;

import cn.droidlover.xdroidmvp.log.XLog;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;
    
    private Dialog creatDialog=null;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mPlatform = Platform.get();
    }

    /**
     * 单实例
     * @param okHttpClient
     * @return
     */
    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }
    
    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public Executor getDelivery() {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    //以下几个继承HttpRequestBuilder
    public static GetBuilder get() {
        return new GetBuilder();
    }
    public static PostStringBuilder postString() {
        return new PostStringBuilder();
    }
    public static PostFileBuilder postFile() {
        return new PostFileBuilder();
    }
    public static PostFormBuilder post() {
        return new PostFormBuilder();
    }
    public static HeadBuilder head() {
        return new HeadBuilder();
    }
    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }
    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }
    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }

    /**
     * 执行请求     
     * @param requestCall
     * @param callback
     */
    public void execute(final RequestCall requestCall, Callback callback,final Context context) {
    	
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        //创建对话框
        if(creatDialog!=null){
            creatDialog.dismiss();
            creatDialog=null;
        }
        final Activity activity = (Activity) context;
        if(activity!=null && !activity.isFinishing()){
        	creatDialog = creatDialog(activity);
        }
        
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            //回调失败
            @Override
            public void onFailure(Call call, final IOException e) {
            	//对话框消失
            	dismissDialog();
                sendFailResultCallback(call, e, finalCallback, id);
            }

           //回调成功
            @Override
            public void onResponse(final Call call, final Response response) {
            	//对话框消失
            	dismissDialog();
                try {
                    if (call.isCanceled()) {
                        sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                        return;
                    }

                    if (!finalCallback.validateReponse(response, id)) {
                        sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                        return;
                    }

                    Object o = finalCallback.parseNetworkResponse(response, id);
                    //发送成功回调
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e) {
                	//对话框消失
                	dismissDialog();
                    sendFailResultCallback(call, e, finalCallback, id);
                } finally {
                	//对话框消失
                	dismissDialog();
                    if (response.body() != null)
                        response.body().close();
                }
            }
        });
    }

    /**
     * 请求失败
     * @param call
     * @param e
     * @param callback
     * @param id --同一个界面区分不同的请求    */
    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id) {
        if (callback == null) return;

        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 成功回调
     * @param object
     * @param callback
     * @param id --同一个界面区分不同的请求    */
    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id) {
        if (callback == null) return;
        mPlatform.execute(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    /**
     * 取消
     */
    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
    }
    
    /**
     * 创建加载数据--进度条对话框
     * @param context
     * @return
     */
    public Dialog creatDialog(Context context) {
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		ProgressBar progressBar = new ProgressBar(context);
//		dialog.setContentView(progressBar);
		dialog.setContentView(R.layout.x_dialog_quest_warm);
		
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.setCanceledOnTouchOutside(false);
		if (dialog != null) {
			dialog.show();
		}
		return dialog;
	}
    /**
     * 加载框--消失
     */
    private void dismissDialog(){
    	if(creatDialog!=null && creatDialog.isShowing()){
    		creatDialog.dismiss();
    	}
    	creatDialog=null;
    }
    
    /**
     * 重新登录
     */
    private void initData_login(){
    	OkHttpClient okHttpClient = new OkHttpClient();
    	FormBody.Builder params=new FormBody.Builder();
        String phone=SPUtils.getSValues("memberMobile");
        String pwd=SPUtils.getSValues("psw");
    	params.add("mobile", phone);
    	params.add("pwd", MD5.hex_md5(pwd));
    	//创建一个请求对象
    	Request request = new Request.Builder()
    			.post(params.build())
    	        .url(Constans.loginURL)
    	        .build();
    	Call call=okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
			@Override
			public void onResponse(Call call, Response response) throws IOException {
				String json = response.body().string();
                XLog.e("重新登录-成功","重新登录："+json);
				try {
					if(!MyUtils.isEmptyString(json) && json.startsWith("{")){
						JSONObject jsonObject = new JSONObject(json);
						String token = jsonObject.getString("token");
						SPUtils.setValues("token", token);
                        SPUtils.setBoolean("islogin", true);
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(Call call, IOException response) {
                XLog.e("重新登录-失败","重新登录-失败");
            }
		});
    }
}

