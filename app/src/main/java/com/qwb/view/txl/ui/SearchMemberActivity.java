package com.qwb.view.txl.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.txl.model.SearchResultBean;
import com.qwb.view.txl.model.SearchResultBean.ResultItems;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
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
import java.util.List;
import java.util.Map;
import okhttp3.Call;


/**
 * 搜索 1成员2公司3部门4员工圈
 * */
public class SearchMemberActivity extends BaseNoTitleActivity implements OnClickListener {

	private EditText et_content;
	private ImageView msec_show_whennodata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_search_member);
		initUI();
	}

	private boolean nameFocus;
	private void initUI() {
		findViewById(R.id.comm_back).setOnClickListener(this);
		findViewById(R.id.ima_caogaosuosou).setOnClickListener(this);
		iv_clean = findViewById(R.id.iv_clean);
		iv_clean.setOnClickListener(this);
		et_content = (EditText) findViewById(R.id.et_content);
		et_content.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		et_content.setOnEditorActionListener(enterListener);
		et_content.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				EditText edit = (EditText) v ;
				String str = edit.getText().toString().trim();
				if (hasFocus) {
					et_content.setHint("");
					if (!TextUtils.isEmpty(str)) {
						iv_clean.setVisibility(View.VISIBLE);
					}
					
				}else{
					et_content.setHint(et_content.getHint().toString());
					iv_clean.setVisibility(View.INVISIBLE);
				}
				nameFocus = hasFocus ;
					
			}
		});
		et_content.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (nameFocus) {
					if (TextUtils.isEmpty(s)) {
						iv_clean.setVisibility(View.INVISIBLE);
						if (lists != null) {
							lists.clear();
							refreshAdapter();
						}
						
					}else{
						iv_clean.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		
		TextView comm_title = (TextView) findViewById(R.id.tv_head_title);
		comm_title.setText("搜索");

		msearch_resultsLt = (PullToRefreshListView) findViewById(R.id.search_resultsLt);
		msearch_resultsLt.setMode(Mode.DISABLED);
//		msearch_resultsLt.setOnRefreshListener(new OnRefreshListener2() {
//
//			
//
//			
//
//			@SuppressWarnings("rawtypes")
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
//				pageNo = 1;
//				isRefresh = true;
//				getJson();
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
//				pageNo++;
//				isRefresh = false;
//				getJson();
//			}
//		});
		
		
		msearch_resultsLt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
				final ResultItems resultItems = lists.get(arg2-1);
				String tp = resultItems.getTp();
				/**
				 * whetherIn：
				 * 如果是圈 ：0不在该圈1创建者 2管理员3普通成员
				 * 用户/部门：1是好友或成员 2不是*/
				int whetherIn = resultItems.getWhetherIn();
				
				/**
				 * datasource：
				 * 1 是公开圈
				 * 2 非公开圈
				 * 
				 * */
				String datasource = resultItems.getDatasource();
				
				/**
				 * tp :
				 * 1成员2公司3部门4员工圈*/
				if ("4".equals(tp)) {
					
					if (0 == whetherIn) {
					}else{
					}
				}
				//用户
				else if("1".equals(tp)){
					if (1 == whetherIn) {
					}else{
						//不是好友 是否同公司 
					String mydatasource = SPUtils.getSValues("datasource");
					//其中一方没有公司 或者2个人所在公司不一样  就不在同个公司 跳申请好友页面
					if (MyUtils.isEmptyString(mydatasource) || MyUtils.isEmptyString(datasource) || !mydatasource.equals(datasource)) {
//						Intent intent = new Intent(SearchMemberActivity.this, SearchResult_UserActivity.class);
//						intent.putExtra("memid", String.valueOf(resultItems.getBelongId()));
//						intent.putExtra("name", resultItems.getNm());
//						startActivity(intent);
					}else {
						//在同个公司
						//自己跳主页
						if (SPUtils.getSValues("memId").equals(String.valueOf(resultItems.getBelongId()))) {
						}else{
						}
					}
					}
				}
				
				//公司
				else if("2".equals(tp)){
					MyDialogManager.getI().showWithClickDialog(SearchMemberActivity.this, "是否申请加入["+resultItems.getNm() + "]公司？", new OnCancle() {
						
						@Override
						public void sure() {
							joinCompany(resultItems.getBelongId());
						}
						
						@Override
						public void cancle() {
						}
					});
				}
				//部门
				else if("3".equals(tp)){
					if (1 == whetherIn ) {
					}else{
						//不在该部门
						MyDialogManager.getI().showWithClickDialog(SearchMemberActivity.this, "是否申请加入["+resultItems.getNm() + "]部门？", new OnCancle() {
							
							@Override
							public void sure() {
								joinCompany(resultItems.getBelongId());
							}
							
							@Override
							public void cancle() {
							}
						});
					}
				}
			}
		});
		msec_show_whennodata = (ImageView) findViewById(R.id.sec_show_whennodata);

	}

	//发送公司加入请求
		private void joinCompany(int id) {
			Map<String, String> params = new HashMap<>();
			params.put("token", SPUtils.getTK());
			params.put("companyId", String.valueOf(id));
			OkHttpUtils
					.post()
					.params(params)
					.url(Constans.applyInCompanyURL)
					.id(2)
					.build()
					.execute(new MyStringCallback(),this);
		}
	
	private OnEditorActionListener enterListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

			if (actionId == EditorInfo.IME_ACTION_SEARCH
					|| actionId == EditorInfo.IME_ACTION_SEND
					|| actionId == EditorInfo.IME_ACTION_DONE
					|| (event != null
							&& KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event
							.getAction())) {
				String content = et_content.getText().toString().trim();
				if (TextUtils.isEmpty(content)) {
					ToastUtils.showCustomToast(
							"搜索内容不能为空！");
				} else {
					search(content);
				}

			}

			return true;
		}

	};
	private PullToRefreshListView msearch_resultsLt;
	private MySeachLtAd mySeachLtAd;
	private List<ResultItems> lists;
	private View iv_clean;

	private void search(String content) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("searchContent", content);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.searchURL)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comm_back:
			finish();
			break;
		case R.id.ima_caogaosuosou:
			String trim = et_content.getText().toString().trim();
			if (TextUtils.isEmpty(trim)) {
				ToastUtils.showCustomToast( "搜索内容不能为空！");
			}else{
				search(trim);
			}
			break;
		case R.id.iv_clean:
			 et_content.setText("");
			 break;
		default:
			break;
		}
	}
	
	private void refreshAdapter() {
		
		if (lists != null && lists.size() <= 0) {
			msec_show_whennodata.setVisibility(View.VISIBLE);
		}else{
			msec_show_whennodata.setVisibility(View.INVISIBLE);
		}
		
		if (lists != null && msearch_resultsLt != null) {
			if (null == mySeachLtAd) {
				mySeachLtAd = new MySeachLtAd();
				msearch_resultsLt.setAdapter(mySeachLtAd);
			} else {
				mySeachLtAd.notifyDataSetChanged();
			}
		}

	}


	class MySeachLtAd extends BaseAdapter {

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				LayoutInflater layoutInflater = getLayoutInflater();
				convertView = layoutInflater.inflate(
						R.layout.x_adapter_seaechresult, null);
			}
			TextView mXingM = MyUtils.getViewFromVH(convertView,
					R.id.sec_add_personname);
			CircleImageView memberHead = MyUtils.getViewFromVH(convertView,
					R.id.sec_memberHead);

			ResultItems resultItems = lists.get(position);
			MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + resultItems.getHeadUrl(), memberHead);
			String nm = resultItems.getNm();
			mXingM.setText(nm);
			return convertView;
		}
	}


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
			// 1 搜索内容2发送加入公司申请
			switch (tag) {
				case 1:
					SearchResultBean secBean = JSON.parseObject(json,
							SearchResultBean.class);
					if (secBean.isState()) {
						lists = secBean.getLists();
						if (0 < lists.size()) {
							msec_show_whennodata.setVisibility(View.GONE);
							msearch_resultsLt.setVisibility(View.VISIBLE);
							refreshAdapter();
						} else {
							ToastUtils.showCustomToast( "没找到相关结果！");
							msec_show_whennodata.setVisibility(View.VISIBLE);
						}
					} else {
						ToastUtils.showCustomToast( secBean.getMsg());
					}

					break;
				case 2 :
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							ToastUtils.showCustomToast( "公司申请发送成功！");
						}else{
							ToastUtils.showCustomToast( ""+jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtils.showCustomToast( "操作失败!");
					}
					break;
			}
		}
	}

}
