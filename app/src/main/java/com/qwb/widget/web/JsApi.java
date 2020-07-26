package com.qwb.widget.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.SPUtils;
import com.qwb.view.purchase.ui.PurchaseOrderActivity;

/**
 *
 */

public class JsApi {
    private Activity context;
    public JsApi(Activity activity){
        this.context = activity;
    }

    @JavascriptInterface
    public void addPurchaseOrderToNative(Object msg)  {
        //采购发票
        ActivityManager.getInstance().jumpActivity(context, PurchaseOrderActivity.class);
    }
    @JavascriptInterface
    public void queryShopOrderByIdToNative(Object id)  {
        //商城订单
        ActivityManager.getInstance().jumpToShopOrderActivity(context, (int)id, SPUtils.getCompanyId());
    }

//    @JavascriptInterface
//    public void testAsyn(Object msg, CompletionHandler<String> handler){
//        handler.complete(msg+" [ asyn call]");
//    }
//
//    @JavascriptInterface
//    public String testNoArgSyn(Object arg) throws JSONException {
//        return  "testNoArgSyn called [ syn call]";
//    }
//
//    @JavascriptInterface
//    public void testNoArgAsyn(Object arg, CompletionHandler<String> handler) {
//        handler.complete( "testNoArgAsyn   called [ asyn call]");
//    }
//
//
//    //@JavascriptInterface
//    //without @JavascriptInterface annotation can't be called
//    public String testNever(Object arg) throws JSONException {
//        JSONObject jsonObject= (JSONObject) arg;
//        return jsonObject.getString("msg") + "[ never call]";
//    }
//
//    @JavascriptInterface
//    public void callProgress(Object args, final CompletionHandler<Integer> handler) {
//
//        new CountDownTimer(11000, 1000) {
//            int i=10;
//            @Override
//            public void onTick(long millisUntilFinished) {
//                //setProgressData can be called many times util complete be called.
//                handler.setProgressData((i--));
//
//            }
//            @Override
//            public void onFinish() {
//                //complete the js invocation with data; handler will be invalid when complete is called
//                handler.complete(0);
//
//            }
//        }.start();
//    }
}