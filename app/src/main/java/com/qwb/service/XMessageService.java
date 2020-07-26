package com.qwb.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.alibaba.fastjson.JSON;
import com.qwb.view.tab.model.MsgBean;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.MyPublicSaveMessageUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * 下载未读消息和推送消息的服务
 */
public class XMessageService extends IntentService {
	public XMessageService() {
		super("XMessageService");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		if (Constans.UnRreadMsg.equals(action)) {
			Map<String, String> params = new HashMap<>();
			params.put("token", SPUtils.getTK());
			OkHttpUtils.post().params(params).url(Constans.unReadURL).id(1).build().execute(new StringCallback() {
				@Override
				public void onError(Call call, Exception e, int id) {
				}

				@Override
				public void onResponse(String response, int id) {
					parseJson(response);
				}
			});
//			OkHttpUtils.post().params(params).url(Constans.unReadURL).id(1).build()
//					.execute(new MyHttpCallback(null) {
//						@Override
//						public void myOnError(Call call, Exception e, int id) {
//
//						}
//
//						@Override
//						public void myOnResponse(String response, int id) {
//							parseJson(response);
//						}
//					});
		}
	}


	public void parseJson(String response){
		try {
			MsgBean msgBean = JSON.parseObject(response, MsgBean.class);
			if (msgBean !=null && msgBean.isState()) {
				//新旧数据库放同一处理
				MyPublicSaveMessageUtil.getInstance().doPublicSaveMessage(msgBean, XMessageService.this);
			}
		} catch (Exception e) {
		}
	}



}
