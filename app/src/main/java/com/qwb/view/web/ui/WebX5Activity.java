package com.qwb.view.web.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qwb.utils.MyStringUtil;
import com.qwb.widget.WebViewProgressBar;
import com.qwb.widget.web.JsApi;
import com.qwb.widget.web.MyWebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 与原生交互的X5-WebView
 */
public class WebX5Activity extends XActivity {
	@Override
	public int getLayoutId() {
		return R.layout.x_activity_web_x5;
	}
	@Override
	public Object newP() {
		return null;
	}
	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
	}

	private String mUrl;
	private String mTitle;
	public void initIntent(){
		Intent intent = getIntent();
		if(intent!=null){
			mTitle = intent.getStringExtra(ConstantUtils.Intent.TITLE);
			mUrl = intent.getStringExtra(ConstantUtils.Intent.URL);
		}
	}

	private void initUI() {
		MyStatusBarUtil.getInstance().setColorBlue(context);
		initHead();
		initProgressBar();
		initWebViewClose();
		initWebView();
	}

	/**
	 * 有标题的头部
	 */
	private void initHead() {
		View head = findViewById(R.id.head);
		TextView tvHeadTitle = findViewById(R.id.tv_head_title);
		View headClose = findViewById(R.id.head_close);
		View head2 = findViewById(R.id.web_view_close);

		if (MyStringUtil.isEmpty(mTitle)){
			head.setVisibility(View.GONE);
			head2.setVisibility(View.VISIBLE);
		}else{
			head.setVisibility(View.VISIBLE);
			head2.setVisibility(View.GONE);
			tvHeadTitle.setText(mTitle);
		}
		headClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		head2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
	}

	/**
	 * 进度条
	 */
	private WebViewProgressBar mProgressBar;
	private void initProgressBar(){
		mProgressBar =  new WebViewProgressBar((ProgressBar) findViewById(R.id.ProgressBar));
	}

	/**
	 * 没有标题的关闭按钮
	 */
	private void initWebViewClose() {

	}

	/**
	 * webView
	 */
	private MyWebView mWebView;
	private void initWebView() {
		mWebView = findViewById(R.id.web_view);
		WebSettings mWebSettings = mWebView.getSettings();
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setSupportZoom(true);
		mWebSettings.setBuiltInZoomControls(false);
		mWebSettings.setSavePassword(false);
		mWebSettings.setTextZoom(100);
		mWebSettings.setDatabaseEnabled(true);
		mWebSettings.setAppCacheEnabled(true);
		mWebSettings.setLoadsImagesAutomatically(true);
		mWebSettings.setSupportMultipleWindows(false);
		mWebSettings.setBlockNetworkImage(false);
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebSettings.setLoadWithOverviewMode(false);
		mWebSettings.setUseWideViewPort(false);
		mWebSettings.setDomStorageEnabled(true);
		mWebSettings.setNeedInitialFocus(true);
		mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
		mWebSettings.setDefaultFontSize(16);
		mWebSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
		mWebSettings.setGeolocationEnabled(true);
		mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.onProgressStart();
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mProgressBar.onProgressChange(newProgress);
			}
		});

//		if (android.os.Build.VERSION.SDK_INT >= 21) {
//			CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
//		}else {
//			CookieManager.getInstance().setAcceptCookie(true);
//		}
//		CookieManager.getInstance().setAcceptCookie(true);
		mWebView.addJavascriptObject(new JsApi(context), null);
		mWebView.loadUrl(mUrl);

	}

	@Override
	protected void onPause() {
		if (mWebView != null) {
			mWebView.onPause();
		}
		super.onPause();

	}

	@Override
	protected void onResume() {
		if (mWebView != null) {
			mWebView.onResume();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mProgressBar != null) {
			mProgressBar.destroy();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebView != null && mWebView.canGoBack()){
				mWebView.goBack();// 返回前一个页面
				return true;
			}else{
				ActivityManager.getInstance().closeActivity(context);
			}
		}
		return super.onKeyDown(keyCode, event);
	}



}
