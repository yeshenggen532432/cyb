package com.qwb.view.customer.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.qwb.application.MyApp;
import com.qwb.view.customer.model.CustomerBean;
import com.qwb.event.MineClientEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.parsent.PAddClient;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.customer.model.BfCountBean.BfCount;
import com.qwb.view.customer.model.ClientDetailBean;
import com.qwb.view.customer.model.ClientLevelBean.ClientLevel;
import com.qwb.view.customer.model.QdTypeBean.Qdtypels;
import com.qwb.view.customer.model.XsjdBean.Xsphasels;
import com.qwb.view.customer.model.queryHzfsBean.queryHzfs;
import com.qwb.utils.MyChooseTimeUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：新增，修改客户
 */
public class AddClientActivity extends XActivity<PAddClient> implements View.OnClickListener{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_customer_edit;
	}

	@Override
	public PAddClient newP() {
		return new PAddClient();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		initIntent();
		createPopup();//5大类型：客户类型，客户等级，销售阶段，拜访次数，合作方式
	}

	private EditText edit_clientName;
	private EditText edit_lxr;
	private EditText edit_phone;
	private EditText edit_tel;
	private EditText edit_QQ;
	private EditText edit_weixin;
	private EditText edit_remo;
	private TextView tv_jwd;
	private TextView tv_date;
	private EditText edit_location;
	private TextView tv_QDType;
	private TextView tv_clientGrade;
	private TextView tv_salesStage;
	private TextView tv_callOnCount;
	private TextView tv_hzfs;
	private TextView tv_provider;

	int popType = 1;//1:渠道类型（客户类型）；2：客户等级；3：销售阶段；4：拜访次数；5：合作方式
	private Integer qdId;//渠道id,客户类型id
	private String qdtypeStr;//渠道类型
	private List<Qdtypels> mQdTypeList = new ArrayList<>();//渠道类型
	private List<ClientLevel> mClientLevelList = new ArrayList<>();//客户等级
	private List<Xsphasels> mXsjdList = new ArrayList<>();//销售阶段
	private List<BfCount> mBfCountlList = new ArrayList<>();//拜访次数
	private List<queryHzfs> mHzfsList = new ArrayList<>();//合作方式
	private MyAdapter mQdTypeAdapter;
	private MyAdapter mClientLevelAdapter;
	private MyAdapter mXsPhaselAdapter;
	private MyAdapter mBfcountAdapter;
	private MyAdapter mHzfsAdapter;

	private int type=1;//1:修改客户；2：添加客户
	private String cid;// 客户id
	private int providerId;//经销商id
	private String lat;
	private String lng;
	private String address;
	private String province;
	private String city;
	private String area;

	/**
	 * 初始化Intent
	 */
	private void initIntent() {
		Intent intent = getIntent();
		if(intent!=null){
			type = intent.getIntExtra(ConstantUtils.Intent.TYPE, 2);
			switch (type) {
				case 1:// 修改客户
					cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
					tv_headRight.setText("保存");
					getP().loadDataClientInfo(context,cid);
					break;
				case 2:// 新增客户
					tv_headTitle.setText("新增客户");
					tv_date.setText(MyUtils.getJintian());
					initLocation();// 初始化定位
					tv_headRight.setText("提交");
					try {
						//默认今天
						tv_date.setText(MyUtils.getDateToStr("yyyy-MM-dd"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 3:// 查看
					cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
					tv_headRight.setText("");
					tv_headRight.setClickable(false);
					getP().loadDataClientInfo(context,cid);
					break;
			}
		}
	}

	/**
	 * 初始化头部
	 */
	private TextView tv_headTitle;
	private TextView tv_headRight;
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//状态栏颜色，透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyApp.getImm().hideSoftInputFromWindow(tv_headTitle.getWindowToken(),0);//强制关闭软键盘
				Router.pop(context);
			}
		});
		tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		tv_headRight.setText("提交");
		tv_headRight.setVisibility(View.VISIBLE);
		tv_headRight.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				addData();
			}
		});
	}

	/**
	 * 初始化UI
	 */
	@BindView(R.id.tv_region)
	TextView mTvRegion;
	private void initUI() {
		initHead();
		edit_location = (EditText) findViewById(R.id.tv_location);// 定位
		tv_QDType = (TextView) findViewById(R.id.tv_QDType);// 渠道类型
		tv_clientGrade = (TextView) findViewById(R.id.tv_clientGrade);// 客户等级
		tv_salesStage = (TextView) findViewById(R.id.tv_salesStage);// 销售阶段
		tv_callOnCount = (TextView) findViewById(R.id.tv_callOnCount);// 拜访频数
		tv_hzfs = (TextView) findViewById(R.id.tv_hzfs);// 合作方式
		tv_provider = (TextView) findViewById(R.id.tv_provider);// 供应商

		tv_date = (TextView) findViewById(R.id.tv_date);// 日期
		edit_clientName = (EditText) findViewById(R.id.edit_clientName);
		edit_lxr = (EditText) findViewById(R.id.edit_LXR);
		edit_phone = (EditText) findViewById(R.id.edit_phone);
		edit_tel = (EditText) findViewById(R.id.edit_tel);
		edit_QQ = (EditText) findViewById(R.id.edit_QQ);
		edit_weixin = (EditText) findViewById(R.id.edit_weixin);
//		edit_area = (EditText) findViewById(R.id.edit_area);
		edit_remo = (EditText) findViewById(R.id.edit_bz);
		tv_jwd = (TextView) findViewById(R.id.tv_jwd);//经纬度

		// 点击事件
		findViewById(R.id.img_location).setOnClickListener(this);// 定位--定位界面
		findViewById(R.id.ll_QDType).setOnClickListener(this);// 渠道类型
		findViewById(R.id.ll_clientGrade).setOnClickListener(this);// 客户等级
		findViewById(R.id.ll_callOnCount).setOnClickListener(this);// 拜访频数
		findViewById(R.id.ll_salesStage).setOnClickListener(this);// 销售阶段
		findViewById(R.id.ll_hzfs).setOnClickListener(this);// 合作方式
		findViewById(R.id.ll_provider).setOnClickListener(this);// 供应商
		findViewById(R.id.ll_date).setOnClickListener(this);// 选择日期
		findViewById(R.id.iv_region).setOnClickListener(this);// 选择日期
	}

	/**
	 * 添加客户，修改客户
	 */
	private Integer mClientLevelId;
	private void addData() {
		String clientName = edit_clientName.getText().toString().trim();
		String phone = edit_phone.getText().toString().trim();
		String address = edit_location.getText().toString().trim();
		if (MyUtils.isEmptyString(clientName)) {
			ToastUtils.showCustomToast("请输入客户名称");
			return;
		}
		if (MyUtils.isEmptyString(address) || MyUtils.isEmptyString(lng) || MyUtils.isEmptyString(lat)) {
			ToastUtils.showCustomToast("请选择地址");
			return;
		}

		String qdtype = tv_QDType.getText().toString().trim();// 渠道类型
		String clientLevel = tv_clientGrade.getText().toString().trim();// 客户等级
		String xsjd = tv_salesStage.getText().toString().trim();// 销售阶段
		String bfcount = tv_callOnCount.getText().toString().trim();// 拜访频次
		String hzfs = tv_hzfs.getText().toString().trim();// 合作方式
		String lxr = edit_lxr.getText().toString().trim();// 联系人
		String tel = edit_tel.getText().toString().trim();// 电话
		String date = tv_date.getText().toString().trim();// 日期
		String remo = edit_remo.getText().toString().trim();// 备注
//		String fgqy = edit_area.getText().toString().trim();// 覆盖区域

		//1:修改客户，2：添加客户
		getP().addData(context,type,cid,clientName,phone,address,lat,lng,
				qdtype,clientLevel,qdId, mClientLevelId,xsjd,bfcount,hzfs,lxr,tel,date,remo,
				providerId,province,city,area,null,mRegionIds);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_QDType:// 渠道类型
			popType = 1;
			if(mQdTypeList !=null && mQdTypeList.size()>0){
				refreshAdapterQdType(null);
			}else{
				getP().loadData(context,popType,qdId);
			}
			break;
		case R.id.ll_clientGrade:// 客户等级
				qdtypeStr = tv_QDType.getText().toString();
				if (TextUtils.isEmpty(qdtypeStr)) {
					ToastUtils.showCustomToast("请先选择渠道类型");
					return;
				}
				popType = 2;
				getP().loadData(context,popType,qdId);
			break;
		case R.id.ll_salesStage:// 销售阶段
				popType = 3;
				if(mXsjdList !=null && mXsjdList.size()>0){
					refreshAdapterXsjd(null);
				}else{
					getP().loadData(context,popType,qdId);
				}
			break;
		case R.id.ll_callOnCount:// 拜访频数
				popType = 4;
				if(mBfCountlList !=null && mBfCountlList.size()>0){
					refreshAdapterBfCount(null);
				}else{
					getP().loadData(context,popType,qdId);
				}
			break;

		case R.id.ll_hzfs:// 合作方式
				popType = 5;
				if(mHzfsList !=null && mHzfsList.size()>0){
					refreshAdapterHzfs(null);
				}else{
					getP().loadData(context,popType,qdId);
				}
			break;

		case R.id.img_location:// 定位--跳到定位界面--返回定位数据
			ActivityManager.getInstance().jumpToLocationMarkActivity(context, lat, lng, address, province, city, area);
//			Router.newIntent(context)
//					.to(MapLocationActivity.class)
//					.putString(ConstantUtils.Intent.LATITUDE,lat)
//					.putString(ConstantUtils.Intent.LONGITUDE,lng)
//					.putString(ConstantUtils.Intent.ADDRESS,address)
//					.putString(ConstantUtils.Intent.PROVINCE,province)
//					.putString(ConstantUtils.Intent.CITY,city)
//					.requestCode(ConstantUtils.Intent.REQUEST_MINE_CLIENT_ADDRESS)
//					.launch();
			break;

		case R.id.ll_provider:// 选择供货商
			Router.newIntent(context)
					.to(ProviderActivity.class)
					.requestCode(ConstantUtils.Intent.REQUEST_MINE_CLIENT_PROVIDER)
					.launch();
			break;

		case R.id.ll_date://开户日期
			MyChooseTimeUtil.chooseDate(context, "开户日期", tv_date);
			break;
		case R.id.iv_region://客户区域
			if(null == mTreeRegion || mTreeRegion.isEmpty()){
				getP().queryRegionTree(context);
			}else{
				showDialogRegion(mTreeRegion);
			}
			break;
		}
	}

	// 界面ABA
	@Override
	protected void onActivityResult(int request, int arg1, Intent data) {
		super.onActivityResult(request, arg1, data);
		if (data != null) {
			// 选择供应商
			if (request == ConstantUtils.Intent.REQUEST_MINE_CLIENT_PROVIDER) {
				String providerName = data.getStringExtra(ConstantUtils.Intent.PROVIDER_NAME);
				providerId = data.getIntExtra(ConstantUtils.Intent.PROVIDER_ID, 0);
				tv_provider.setText(providerName);
				XLog.e("providerId",""+providerId);
			}
			// 定位
			if (request == ConstantUtils.Intent.REQUEST_CODE_LOCATION) {
				lat = data.getStringExtra(ConstantUtils.Intent.LATITUDE);
				lng = data.getStringExtra(ConstantUtils.Intent.LONGITUDE);
				address = data.getStringExtra(ConstantUtils.Intent.ADDRESS);
				province = data.getStringExtra(ConstantUtils.Intent.PROVINCE);
				city = data.getStringExtra(ConstantUtils.Intent.CITY);
				area = data.getStringExtra(ConstantUtils.Intent.AREA);
				edit_location.setText(address);
				tv_jwd.setText(lat+","+lng);//经纬度
			}
		}
	}

	/**
	 */
	private EasyPopup mEasyPop;
	private ListView mListView;
	public void createPopup() {
		mEasyPop = new EasyPopup(context)
				.setContentView(R.layout.x_popup_add_client_type)
				//允许背景变暗
				.setBackgroundDimEnable(true)
				//变暗的透明度(0-1)，0为完全透明
				.setDimValue(0.4f)
				//变暗的背景颜色
				.setDimColor(Color.BLACK)
				//是否允许点击PopupWindow之外的地方消失
				.setFocusAndOutsideEnable(true)
				.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
				.createPopup();
		mListView=mEasyPop.getView(R.id.listView_popwinClient);
		mListView.setOnItemClickListener(new PopItemListener());
	}

	// 窗体item监听
	private final class PopItemListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			switch (popType) {
			case 1:
				tv_QDType.setText("" + mQdTypeList.get(position).getQdtpNm());
				tv_clientGrade.setText("");
				qdId = mQdTypeList.get(position).getId();//渠道id;客户类型id
				mClientLevelId = null;
				break;
			case 2:
				mClientLevelId = mClientLevelList.get(position).getId();
				tv_clientGrade.setText("" + mClientLevelList.get(position).getKhdjNm());
				break;
			case 3:
				tv_salesStage.setText("" + mXsjdList.get(position).getPhaseNm());
				break;
			case 4:
				tv_callOnCount.setText("" + mBfCountlList.get(position).getPcNm());
				break;
			case 5:
				tv_hzfs.setText("" + mHzfsList.get(position).getHzfsNm());
				break;
			}
			mEasyPop.dismiss();// 关闭窗体
		}
	}

	private final class MyAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View layout = getLayoutInflater().inflate(R.layout.x_adapter_popwin_client, null);
			TextView tv = (TextView) layout.findViewById(R.id.tv_listitem_popwin_client);
			switch (popType) {
			case 1:
				tv.setText("" + mQdTypeList.get(position).getQdtpNm());
				break;
			case 2:
				tv.setText("" + mClientLevelList.get(position).getKhdjNm());
				break;
			case 3:
				tv.setText("" + mXsjdList.get(position).getPhaseNm());
				break;
			case 4:
				tv.setText("" + mBfCountlList.get(position).getPcNm());
				break;
			case 5:
				tv.setText("" + mHzfsList.get(position).getHzfsNm());
				break;
			}
			return layout;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			switch (popType) {
			case 1:
				return mQdTypeList.size();
			case 2:
				return mClientLevelList.size();
			case 3:
				return mXsjdList.size();
			case 4:
				return mBfCountlList.size();
			case 5:
				return mHzfsList.size();
			}
			return mQdTypeList.size();
		}
	}

	/**
	 * 初始化定位
	 */
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private boolean isFirst=true;
	private void initLocation() {
		mLocClient = new LocationClient(this); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null) {
				return;
			}
			if (isFirst) {// 只定位一次
				isFirst = false;
				edit_location.setText(location.getAddrStr());// 地址
				tv_jwd.setText(location.getLatitude()+","+location.getLongitude());//经纬度
				lng = String.valueOf(location.getLongitude());
				lat = String.valueOf(location.getLatitude());
				province = location.getProvince();// 省份
				city = location.getCity();//市
				area = location.getDistrict();// 地区
				mLocClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//释放资源
		if(mLocClient!=null){
			mLocClient.stop();
			mLocClient=null;
		}
	}

	/**
	 * 刷新：渠道类型
	 */
	public void refreshAdapterQdType(List<Qdtypels> datas){
		if(datas!=null){
			this.mQdTypeList.addAll(datas);
		}
		if (mQdTypeAdapter == null) {
			mQdTypeAdapter = new MyAdapter();
			mListView.setAdapter(mQdTypeAdapter);
		} else {
			mQdTypeAdapter.notifyDataSetChanged();
		}
		mEasyPop.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
	}
	/**
	 * 刷新：客户等级
	 */
	public void refreshAdapterClientLevel(List<ClientLevel> datas){
		if(datas!=null){
			this.mClientLevelList.clear();
			this.mClientLevelList.addAll(datas);
		}
		if(mClientLevelList!=null && mClientLevelList.size()>0){
			if (mClientLevelAdapter == null) {
				mClientLevelAdapter = new MyAdapter();
				mListView.setAdapter(mClientLevelAdapter);
			} else {
				mClientLevelAdapter.notifyDataSetChanged();
			}
			mEasyPop.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
		}else{
			ToastUtils.showCustomToast("暂无可选");
		}
	}
	/**
	 * 刷新：销售阶段
	 */
	public void refreshAdapterXsjd(List<Xsphasels> datas){
		if(datas!=null){
			this.mXsjdList.addAll(datas);
		}
		if (mXsPhaselAdapter == null) {
			mXsPhaselAdapter = new MyAdapter();
			mListView.setAdapter(mXsPhaselAdapter);
		} else {
			mXsPhaselAdapter.notifyDataSetChanged();
		}
		mEasyPop.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
	}
	/**
	 * 刷新：拜访次数
	 */
	public void refreshAdapterBfCount(List<BfCount> datas){
		if(datas!=null){
			this.mBfCountlList.addAll(datas);
		}
		if (mBfcountAdapter == null) {
			mBfcountAdapter = new MyAdapter();
			mListView.setAdapter(mBfcountAdapter);
		} else {
			mBfcountAdapter.notifyDataSetChanged();
		}
		mEasyPop.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
	}
	/**
	 * 刷新：合作方式
	 */
	public void refreshAdapterHzfs(List<queryHzfs> datas){
		if(datas!=null){
			this.mHzfsList.addAll(datas);
		}
		if (mHzfsAdapter == null) {
			mHzfsAdapter = new MyAdapter();
			mListView.setAdapter(mHzfsAdapter);
		} else {
			mHzfsAdapter.notifyDataSetChanged();
		}
		mEasyPop.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
	}

	/**
	 * 添加客户，修改客户成功
	 */
	public void addSuccess(){
		BusProvider.getBus().post(new MineClientEvent());
		Router.pop(context);
	}
	/**
	 * 添加客户，修改客户成功
	 */
	public void updateSuccess(){
		String KhNm = edit_clientName.getText().toString().trim();
		String phone = edit_phone.getText().toString().trim();
		String address = edit_location.getText().toString().trim();
		String tel = edit_tel.getText().toString().trim();
		String linkman = edit_lxr.getText().toString().trim();
		Intent data = new Intent();
		data.putExtra(ConstantUtils.Intent.SUCCESS, true);
		data.putExtra(ConstantUtils.Intent.ADDRESS, address);
		data.putExtra(ConstantUtils.Intent.CLIENT_NAME2, KhNm);
		data.putExtra(ConstantUtils.Intent.TEL, tel);
		data.putExtra(ConstantUtils.Intent.MOBILE, phone);
		data.putExtra(ConstantUtils.Intent.LINKMAN, linkman);
		setResult(0, data);
		MineClientEvent event = new MineClientEvent();
		CustomerBean customer = new CustomerBean();
		customer.setKhNm(KhNm);
		customer.setAddress(address);
		customer.setLinkman(linkman);
		customer.setTel(tel);
		customer.setMobile(phone);
		customer.setLongitude(lng);
		customer.setLatitude(lat);
		event.setCustomerBean(customer);
		BusProvider.getBus().post(event);
		ActivityManager.getInstance().closeActivity(context);
	}

	/**
	 * 设置客户信息
	 */
	public void setClientInfo(ClientDetailBean.ClientDetail clientInfo){
		qdId = clientInfo.getQdtypeId();
		mClientLevelId = clientInfo.getKhlevelId();
		//处于"修改客户"状态
		tv_headTitle.setText(clientInfo.getKhNm());
		edit_clientName.setText(clientInfo.getKhNm());
		edit_phone.setText(clientInfo.getMobile());
		lng = String.valueOf(clientInfo.getLongitude());
		lat = String.valueOf(clientInfo.getLatitude());
		tv_date.setText(clientInfo.getOpenDate());
		edit_location.setText(clientInfo.getAddress());
		if(!MyUtils.isEmptyString(clientInfo.getLatitude())  && !MyUtils.isEmptyString(clientInfo.getLongitude())){
			tv_jwd.setText(clientInfo.getLatitude()+","+clientInfo.getLongitude());//经纬度
		}
		if (!MyUtils.isEmptyString(clientInfo.getLinkman())) {
			edit_lxr.setText(clientInfo.getLinkman());
		}
		if (!MyUtils.isEmptyString(clientInfo.getTel())) {
			edit_tel.setText(clientInfo.getTel());
		}
		if (!MyUtils.isEmptyString(clientInfo.getQq())) {
			edit_QQ.setText(clientInfo.getQq());
		}
		if (!MyUtils.isEmptyString(clientInfo.getWxCode())) {
			edit_weixin.setText(clientInfo.getWxCode());
		}
		if (!MyUtils.isEmptyString(clientInfo.getRegionNm())) {
			mTvRegion.setText(clientInfo.getRegionNm());
			mRegionIds = "" + clientInfo.getRegionId();
		}
		if (!MyUtils.isEmptyString(clientInfo.getRemo())) {
			edit_remo.setText(clientInfo.getRemo());
		}
		if (!MyUtils.isEmptyString(clientInfo.getPkhNm())) {
			tv_provider.setText(clientInfo.getPkhNm());
			tv_provider.setVisibility(View.VISIBLE);
		}
		if (!MyUtils.isEmptyString(clientInfo.getQdtpNm())) {
			tv_QDType.setText(clientInfo.getQdtpNm());
		}
		if (!MyUtils.isEmptyString(clientInfo.getKhdjNm())) {
			tv_clientGrade.setText(clientInfo.getKhdjNm());
		}
		if (!MyUtils.isEmptyString(clientInfo.getXsjdNm())) {
			tv_salesStage.setText(clientInfo.getXsjdNm());
		}
		if (!MyUtils.isEmptyString(clientInfo.getBfpcNm())) {
			tv_callOnCount.setText(clientInfo.getBfpcNm());
		}
		if (!MyUtils.isEmptyString(clientInfo.getHzfsNm())) {
			tv_hzfs.setText(clientInfo.getHzfsNm());
		}
		province = String.valueOf(clientInfo.getProvince());
		city = String.valueOf(clientInfo.getCity());
		area = String.valueOf(clientInfo.getArea());
		//客户审核通过不让修改  审核不通过；待审核；审核通过
		if("审核通过".equals(clientInfo.getShZt())){
			tv_headRight.setVisibility(View.GONE);
		}else{
			tv_headRight.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 客户所属区域(单选)
	 */
	private List<TreeBean> mTreeRegion = new ArrayList<>();
	private MyTreeDialog mTreeRegionDialog;
	private String mRegionIds;
	public void showDialogRegion(List<TreeBean> datas){
		if(mTreeRegion != null && mTreeRegion.isEmpty()){
			mTreeRegion.addAll(datas);
		}
		if(null == mTreeRegionDialog){
			mTreeRegionDialog = new MyTreeDialog(context, mTreeRegion, false);
		}
		mTreeRegionDialog.title("客户所属区域").show();

		mTreeRegionDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
			@Override
			public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
				mRegionIds = clientTypeIds;
				for (TreeBean bean : mTreeRegion){
					if(clientTypeIds.equals(""+ bean.get_id())){
						mTvRegion.setText(bean.getName());
						break;
					}
				}
			}
		});
	}

}
