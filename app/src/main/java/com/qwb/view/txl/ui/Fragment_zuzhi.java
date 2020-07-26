package com.qwb.view.txl.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qwb.utils.Constans;
import com.qwb.utils.MyDialogManager;
import com.qwb.utils.MyDialogManager.OnCancle;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class Fragment_zuzhi extends Fragment {
	private View view;
	private View rl_company;
	private String company;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.x_fragment_zuzhi, null);

		tv_bumen = (TextView) view.findViewById(R.id.tv_bumen);
		rl_company = view.findViewById(R.id.rl_company);
		rl_company.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), BuMenListActivity.class);
				intent.putExtra("title", company);
				startActivity(intent);
			}
		});
		
		rl_company.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (MyUtils.isCreater()) {
					showMoreDialog();
				}else{
					ToastUtils.showCustomToast( "您无操作权限！");
				}
				return true;
			}
		});
		return view;
	}
	
	
	/**
	 *更多操作窗口
	 * */
	private Dialog moreDialog ;
	private void showMoreDialog() {
		moreDialog = new Dialog(getActivity(),R.style.Translucent_NoTitle);
		moreDialog.setContentView(R.layout.x_dialog_zhishiku_more);
		//吧类别名字设置为标题
		TextView tv_del = (TextView) moreDialog.findViewById(R.id.tv_deltype);
		tv_del.setText("删除公司");
		tv_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//删除公司
				moreDialog.dismiss();
				MyDialogManager.getI().showWithClickDialog(getActivity(), "确定删除公司？", new OnCancle() {
					
					@Override
					public void sure() {
						// TODO Auto-generated method stub
						if (companyId != 0) {
							delCompany();
						}
					}
					
					@Override
					public void cancle() {
					}
				});
			}
		});
		moreDialog.findViewById(R.id.tv_upname).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跟新名字
				upDateCompanyDialog();
				moreDialog.dismiss();
			}
		});
		moreDialog.show();
	}
	
	
	private Dialog upDl;
	/**
	 * 修改公司名字
	 * */
	private void upDateCompanyDialog() {
			upDl = new Dialog(getActivity(),R.style.Translucent_NoTitle);
			upDl.setContentView(R.layout.x_dialog_zhishiku);
			final EditText et_conten = (EditText) upDl.findViewById(R.id.et_conten);
			et_conten.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean hasFocus) {
					if (hasFocus) {
						et_conten.setHint("");
					}
				}
			});
			 TextView tv_title = (TextView) upDl.findViewById(R.id.tv_title);
			 tv_title.setText("重命名");
			 if (!MyUtils.isEmptyString(company)) {
				 et_conten.setHint(company);
			}
//			et_conten.setText(typeItemBean.getSortNm());
			upDl.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					upDl.dismiss();
				}
			});
			upDl.findViewById(R.id.bt_upName).setOnClickListener(new OnClickListener() {
				

				@Override
				public void onClick(View v) {
					String name = et_conten.getText().toString().trim();
					if (!TextUtils.isEmpty(name)) {
						upCompanyName(name);
					}else{
						ToastUtils.showCustomToast( "内容不能为空！");
					}
					upDl.dismiss();
				}

				

				
			});
		upDl.show();
	}
	
	private void upCompanyName(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("companyNm",name);
		params.put("companyId", String.valueOf(companyId));
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.updateCompanyNmURL)
				.id(3)
				.build()
				.execute(new MyStringCallback(),getActivity());
	}

	private int companyId;
	private TextView tv_bumen;
	
	private void delCompany() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("companyId", String.valueOf(companyId));
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.delCompanyURL)
				.id(2)
				.build()
				.execute(new MyStringCallback(),getActivity());
	}
	
	public void refreshData(){
		getCompanyInfo();
	}
	
	/**
	 * 获取公司信息
	 * */
	private void getCompanyInfo(){
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.queryCorporationURL)
				.id(1)
				.build()
				.execute(new MyStringCallback(),getActivity());
	};	
	


	// ------------------------------TODO OKHttp回调数据--------------------------
	public class MyStringCallback extends StringCallback {
		@Override
		public void onError(Call call, Exception e, int id) {
			e.printStackTrace();
		}

		@Override
		public void onResponse(String response, int id) {
			resultData(response, id);
		}
	}
	/**
	 * 回调数据
	 */
	private void resultData(String json, int tag) {
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			//1查询公司信息 2删除公司3修改公司名字
			switch (tag) {
				case 1:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							rl_company.setVisibility(View.VISIBLE);
							company = jsonObject.getString("deptNm");
							String datasource = jsonObject.getString("datasource");
							SPUtils.setValues("datasource", datasource);
							companyId = jsonObject.getInt("deptId");
							tv_bumen.setText(company);
						}else{
							SPUtils.setValues("datasource", "");
							rl_company.setVisibility(View.GONE);
						}
					} catch (JSONException e) {
						SPUtils.setValues("datasource", "");
						rl_company.setVisibility(View.GONE);
					}
//					TongXunLuActivity activity = (TongXunLuActivity) getActivity();
//					activity.refrehsMenu();
					break;
				case 2 :

					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							SPUtils.setValues("datasource", "");
							SPUtils.setValues("iscreat", "0");
							getCompanyInfo();
						}else{
							ToastUtils.showCustomToast( jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtils.showCustomToast("操作失败");
					}
					break;
				case 3 :
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							getCompanyInfo();
						}else{
							ToastUtils.showCustomToast( jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtils.showCustomToast("操作失败");
					}
					break;
			}
		}
	}
}
