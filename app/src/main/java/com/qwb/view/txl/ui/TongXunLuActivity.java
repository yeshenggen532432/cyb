package com.qwb.view.txl.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qwb.application.MyApp;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.company.model.CompanyInfoOldBean;
import com.qwb.view.company.model.CompanyInfoOldBean.CompanyInforItemBean;
import com.qwb.event.CreateCompanyEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;

public class TongXunLuActivity extends BaseNoTitleActivity implements OnClickListener {

	private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TXL_NEW, ConstantUtils.Apply.TXL_OLD);
	private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TXL_NEW, ConstantUtils.Apply.TXL_OLD);

	private int screenWidth;
	private ImageView mImageView;
	private int item_width;
	private HorizontalScrollView mHorizontalScrollView;
	private ViewPager pager;
	private MyViewPagerAdapter adapter;
	private PopupWindow mPopupWindow;
	private List<CompanyInforItemBean> companyList = new ArrayList<CompanyInforItemBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        showdl = getIntent().getStringExtra("showdl");
        registerReceiver(myReceive, new IntentFilter(Constans.Action_getcompanyInfo));
        creatPop();
		setContentView(R.layout.x_activity_tong_xun_lu);
		initPopMenu();
		initUI();
	}
	
	  private BroadcastReceiver myReceive = new BroadcastReceiver()
	    {
	        
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	            if (Constans.Action_getcompanyInfo.equals(intent.getAction()))
	            {
	            	if (fragment_zuzhi != null ) {
	            		fragment_zuzhi.refreshData();
					}
	            }
	        }
	    };
	
	private void initPopMenu() {
	}
	
	private void creatBuMen(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("branchName", name);
		params.put("dataTp", dataTp);
		if("4".equals(dataTp)){
			params.put("mids", dataTpMids);//角色
		}
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.addDepartURL)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);
	}
	
	
	private Dialog dlg;
	private void creatBuMenDialog(){
		dlg = new Dialog(this,R.style.Translucent_NoTitle);
		dlg.setContentView(R.layout.x_dialog_creat_bumen);
		TextView tv_yaoqing = (TextView) dlg.findViewById(R.id.tv_biaoti);
		tv_yaoqing.setText("创建部门");
		 final EditText et_name = (EditText) dlg.findViewById(R.id.et_name);
		dlg.findViewById(R.id.bt_sure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String name = et_name.getText().toString().trim();
						if (TextUtils.isEmpty(name)) {
							ToastUtils.showCustomToast( "名称不能为空！");
						}else{
							dlg.dismiss();
							creatBuMen(name);
						}
					}
				});
		dlg.findViewById(R.id.bt_cancle).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dlg.dismiss();
					}
				});
		dlg.show();
	}
	private Dialog yaoqingDL;
	private void creatYaoQingDialog(){
		yaoqingDL = new Dialog(this,R.style.Translucent_NoTitle);
		yaoqingDL.setContentView(R.layout.x_dialog_creat_bumen);
		TextView tv_yaoqing = (TextView) yaoqingDL.findViewById(R.id.tv_biaoti);
		tv_yaoqing.setText("邀请人");
		final EditText et_name = (EditText) yaoqingDL.findViewById(R.id.et_name);
		et_name.setInputType(InputType.TYPE_CLASS_PHONE);
		yaoqingDL.findViewById(R.id.bt_sure).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String name = et_name.getText().toString().trim();
						if (TextUtils.isEmpty(name)) {
							ToastUtils.showCustomToast( "号码不能为空");
						}else{
							yaoqingDL.dismiss();
							sendYaoQing(name);
						}
					}
				});
		yaoqingDL.findViewById(R.id.bt_cancle).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						yaoqingDL.dismiss();
					}
				});
		yaoqingDL.show();
	}
	
	private void sendYaoQing(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("tel", name);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.yqworkmateURL)
				.id(3)
				.build()
				.execute(new MyStringCallback(),this);
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return companyList.size();
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
				convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
			}
			
			CompanyInforItemBean companyInforItemBean = companyList.get(position);
			TextView viewFromVH = MyUtils.getViewFromVH(convertView,android.R.id.text1);
			viewFromVH.setText(companyInforItemBean.getDeptNm());
			
			return convertView;
		}
		
	}
	private ListView lv_compute;
	private Dialog addCompanyDL ;
	private CompanyInforItemBean currentBean;

	private void searchCompany(String name) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("searchNm", name);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.likeCompanyNmURL)
				.id(4)
				.build()
				.execute(new MyStringCallback(),this);
	}
	
	//发送公司加入请求
	private void sendRequest() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("companyId", String.valueOf(currentBean.getDeptId()));
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.applyInCompanyURL)
				.id(5)
				.build()
				.execute(new MyStringCallback(),this);

		currentBean = null ;
	}
	
//	
//	
	private Dialog companydlg;
	private String companyname;
	private void creatCompanyDialog(){
		companydlg = new Dialog(this,R.style.Translucent_NoTitle);
		companydlg.setContentView(R.layout.x_dialog_creat_company);
		final EditText com_name = (EditText) companydlg.findViewById(R.id.et_name);
//		final EditText et_maxmember = (EditText) companydlg.findViewById(R.id.et_maxmember);
//		final EditText et_hanye = (EditText) companydlg.findViewById(R.id.et_hanye);
		companydlg.findViewById(R.id.bt_sure).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						companyname = com_name.getText().toString().trim();
						if (TextUtils.isEmpty(companyname)) {
							ToastUtils.showCustomToast( "名称不能为空！");
						}else{
							companydlg.dismiss();
//							String commax = et_maxmember.getText().toString().trim();
//							String hangye = et_hanye.getText().toString().trim();
//							
							creatCompany(companyname);
						}
					}
					
					
				});
		companydlg.findViewById(R.id.bt_cancle).setOnClickListener(
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						companydlg.dismiss();
					}
				});
		companydlg.show();
	}
	
    private void creatPop()
    {
        View view = getLayoutInflater().inflate(R.layout.x_popup_zuzhi, null);
        View tv_creat = view.findViewById(R.id.tv_creat);
        View tv_sao = view.findViewById(R.id.tv_sao);
        View tv_yaoqing = view.findViewById(R.id.tv_yaoqing);
        View tv_addcompany = view.findViewById(R.id.tv_addcompany);
        tv_addcompany.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
//				startActivity(new Intent(TongXunLuActivity.this, SearchCompanyActivity.class));
			}
		});
        
        View tv_add_line = view.findViewById(R.id.tv_add_line);
        View tv_bumen_line = view.findViewById(R.id.tv_bumen_line);
        tv_bumen_line.setVisibility(View.GONE);
        tv_add_line.setVisibility(View.GONE);
        tv_yaoqing.setVisibility(View.GONE);
        tv_sao.setVisibility(View.GONE);
        tv_creat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				creatCompanyDialog();
			}

		});
        tv_sao.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		mPopupWindow.dismiss();
        		if (dlg == null) {

        			if (!TextUtils.isEmpty(SPUtils.getSValues("datasource"))) {
        				creatBuMenDialog();
					}else{
						ToastUtils.showCustomToast("您没有所属公司！请先创建公司！");
					}
				}else{
					dlg.show();
				}
        		
        	}
        });
        tv_yaoqing.setOnClickListener(new OnClickListener() {
        	
        	@Override
        	public void onClick(View v) {
        		mPopupWindow.dismiss();
        		if (yaoqingDL == null) {
        			if (!TextUtils.isEmpty(SPUtils.getSValues("datasource"))) {
        				creatYaoQingDialog();
					}else{
						ToastUtils.showCustomToast("您没有所属公司！请先创建公司！");
					}
				}else{
					yaoqingDL.show();
				}
        	}

		
        });
        
        mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT,true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setAnimationStyle(R.style.MyPopAnimation3);
    }

    private String companyName;//公司名称
    private void creatCompany(String deptNm) {
		companyName=deptNm;
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("deptNm", deptNm);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.addNewCompany)
				.id(2)
				.build()
				.execute(new MyStringCallback(),this);
	}
	
	private void initUI() {
		findViewById(R.id.comm_back).setOnClickListener(this);
		TextView comm_title = (TextView) findViewById(R.id.tv_head_title);
    	comm_title.setText("通讯录");
    	
    	iv_top_right = (ImageView) findViewById(R.id.iv_head_right);
		iv_top_right.setImageResource(R.drawable.search_icon);
		iv_top_right.setOnClickListener(this);
    	
    	CheckBox cb_2 = (CheckBox) findViewById(R.id.cb_2);
    	cb_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					pager.setCurrentItem(1);
				}
			}
		});
    	CheckBox cb_1 = (CheckBox) findViewById(R.id.cb_1);
    	cb_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					pager.setCurrentItem(0);
				}
				
			}
		});
    	CheckBox cb_3 = (CheckBox) findViewById(R.id.cb_3);
    	cb_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
    		
    		@Override
    		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    			if (isChecked) {
    				pager.setCurrentItem(2);
    			}
    			
    		}
    	});
    	
    	screenWidth = MyApp.getScreenWidth();
    	
    	View houseres_tab_layout = findViewById(R.id.houseres_tab_layout);
    	View hsv_content = findViewById(R.id.hsv_content);
    	houseres_tab_layout.getLayoutParams().width = screenWidth ;
    	hsv_content.getLayoutParams().width = screenWidth ;
    	
    	mImageView = (ImageView) findViewById(R.id.iv_line);
    	item_width = screenWidth /3;
		mImageView.getLayoutParams().width = item_width ;
		mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.hsv_view);
		initViewPager();
//		getCompanyInfor();
		if (!MyUtils.isEmptyString(showdl)) {
			creatCompanyDialog();
		}
	}
	
	

	private void initViewPager()
	{
		pager = (ViewPager) findViewById(R.id.houseres_viewpager);
		pager.setOffscreenPageLimit(8);
		adapter = new MyViewPagerAdapter(
				getSupportFragmentManager());
		
		pager.setOnPageChangeListener(new MyOnPageChangeListener());
		pager.setAdapter(adapter);
		if (!MyUtils.isEmptyString(showdl)) {
			pager.setCurrentItem(1);
		}
	}

    private int endPosition;
    private int beginPosition;
    private int currentFragmentIndex;
	private boolean isEnd ;
	private ImageView iv_top_right;
	private int currentPosition;
	
	public class MyOnPageChangeListener implements OnPageChangeListener
	{

		@Override
		public void onPageSelected(final int position)
		{
			currentPosition = position;
			if (iv_top_right != null) {
				switch (position) {
				case 0:
					iv_top_right.setVisibility(View.VISIBLE);
					iv_top_right.setImageResource(R.drawable.search_icon);
					if (fragment_changYong != null) {
						fragment_changYong.refershData();
					}
					break;
				case 1:
					if (!TextUtils.isEmpty(SPUtils.getSValues("datasource"))) {
						iv_top_right.setVisibility(View.INVISIBLE);
					}else{
						iv_top_right.setVisibility(View.VISIBLE);
						iv_top_right.setImageResource(R.drawable.add_caozuo);
					}
					if (fragment_zuzhi != null) {
						fragment_zuzhi.refreshData();
					}
					break;
				case 2 :
					iv_top_right.setVisibility(View.VISIBLE);
					iv_top_right.setImageResource(R.drawable.search_icon);
					if (fragment_friends != null) {
						fragment_friends.refershData();
					}
					break;
				default:
					break;
				}
			}
			Animation animation = new TranslateAnimation(endPosition, position
					* item_width, 0, 0);

			beginPosition = position * item_width;

			currentFragmentIndex = position;
			if (animation != null)
			{
				animation.setFillAfter(true);
				animation.setDuration(0);
				mImageView.startAnimation(animation);
				mHorizontalScrollView.smoothScrollTo((currentFragmentIndex - 1)
						* item_width, 0);
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels)
		{
			if (!isEnd)
			{
				if (currentFragmentIndex == position)
				{
					endPosition = item_width * currentFragmentIndex
							+ (int) (item_width * positionOffset);
				}
				if (currentFragmentIndex == position + 1)
				{
					endPosition = item_width * currentFragmentIndex
							- (int) (item_width * (1 - positionOffset));
				}

				Animation mAnimation = new TranslateAnimation(beginPosition,
						endPosition, 0, 0);
				mAnimation.setFillAfter(true);
				mAnimation.setDuration(0);
				mImageView.startAnimation(mAnimation);
				mHorizontalScrollView.invalidate();
				beginPosition = endPosition;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state)
		{
			if (state == ViewPager.SCROLL_STATE_DRAGGING)
			{
				isEnd = false;
			} else if (state == ViewPager.SCROLL_STATE_SETTLING)
			{
				isEnd = true;
				beginPosition = currentFragmentIndex * item_width;
				if (pager.getCurrentItem() == currentFragmentIndex)
				{
					// 未跳入下一个页面
					mImageView.clearAnimation();
					Animation animation = null;
					// 恢复位置
					animation = new TranslateAnimation(endPosition,
							currentFragmentIndex * item_width, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(1);
					mImageView.startAnimation(animation);
					mHorizontalScrollView.invalidate();
					endPosition = currentFragmentIndex * item_width;
				}
			}
		}

	}
	
	
	private Fragment_zuzhi fragment_zuzhi;
	private Fragment_changYong fragment_changYong;
	private Fragment_friends fragment_friends;
	private boolean isFirst;
	
	
	/**
	 * viewpager适配器
	 * */
	class MyViewPagerAdapter extends FragmentStatePagerAdapter
	{

		public MyViewPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}
		
		@Override
		public Object instantiateItem(ViewGroup arg0, int arg1)
		{
			return super.instantiateItem(arg0, arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			super.destroyItem(container, position, object);
		}

		@Override
		public Fragment getItem(int position)
		{
			if (position == 0 )
			{
				fragment_changYong = new Fragment_changYong();
				return fragment_changYong ;
			}
			if (position == 1)
			{
				fragment_zuzhi = new Fragment_zuzhi();
				return fragment_zuzhi  ;
			}
			if (position == 2)
			{
				fragment_friends = new Fragment_friends();
				return fragment_friends;
			
			}

			return null;
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return 3;
		}

	}
	
	public  void refreshChangYong(){
		if (fragment_changYong != null) {
    		fragment_changYong.refershData();
    	}
	}
	
	 @Override
	    protected void onResume()
	    {
	        super.onResume();
	        JPushInterface.onResume(this);
	        switch (currentPosition) {
			case 0:
				if (fragment_changYong != null) {
					fragment_changYong.refershData();
				}
				break;
			case 1:
				if (fragment_zuzhi != null) {
					fragment_zuzhi.refreshData();
				}
			case 2:
				if (fragment_friends != null) {
					fragment_friends.refershData();
				}
				break;
			}
	    }
	    
	    @Override
	    protected void onPause()
	    {
	        super.onPause();
	        JPushInterface.onPause(this);
	    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comm_back:
			finish();
			break;
		case R.id.iv_head_right:
			if (1 == currentPosition) {
				mPopupWindow.showAsDropDown(iv_top_right,0,-20);
			}else{
				startActivity(new Intent(this, SearchMemberActivity.class));
			}
			break;
		}
	}
	

	private MyAdapter myAdapter;
	private String showdl;
	private View menu_layout;
	private void refrehsAdapter() {
		if (lv_compute != null) {
			if (myAdapter == null ) {
				myAdapter = new MyAdapter();
				lv_compute.setAdapter(myAdapter);
			}else{
				myAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(myReceive);
		super.onDestroy();
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
			//1 创建部门 2创建公司3邀请同事进公司4搜索公司5发送加入公司申请
			switch (tag) {
				case 1:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
						}
					} catch (JSONException e) {
					}
					break;
				case 2:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							SPUtils.setValues("datasource", jsonObject.getString("database"));
							SPUtils.setValues("iscreat", "1");
							if (fragment_zuzhi != null) {
								fragment_zuzhi.refreshData();
							}
							//首页的公司名称更改
							CreateCompanyEvent event=new CreateCompanyEvent();
							event.setCompanyName(companyName);
							BusProvider.getBus().post(event);
							SPUtils.setValues(ConstantUtils.Sp.COMPANY_ID, String.valueOf(jsonObject.getString("companyId")));// 公司id
						}else{
							ToastUtils.showCustomToast( ""+jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
					}
					break;
				case 3 :
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state") && fragment_zuzhi != null) {
							ToastUtils.showCustomToast("发送成功！");
						}else{

							ToastUtils.showCustomToast( ""+jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
					}
					break;
				case 4:
					CompanyInfoOldBean parseObject = JSON.parseObject(json, CompanyInfoOldBean.class);
					if (parseObject != null && parseObject.isState()) {
						List<CompanyInforItemBean> companys = parseObject.getCompanys();
						if (companys != null) {
							companyList.clear();
							companyList.addAll(companys);
							refrehsAdapter();
						}

					}else{
						ToastUtils.showCustomToast( "无匹配结果！");
					}
					break;
				case 5:
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
