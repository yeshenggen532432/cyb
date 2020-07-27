package com.qwb.view.map.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.application.MyApp;
import com.qwb.utils.MyDividerUtil;
import com.qwb.view.map.adapter.MemberAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.map.parsent.PMapMemberFBT;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.map.model.TrackListBean;
import com.qwb.view.map.model.TrackListBean.TrackList;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 员工在线-员工分布图
 */
public class MapMemberFBTActivity extends XActivity<PMapMemberFBT>{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_map_member_fbt;
	}

	@Override
	public PMapMemberFBT newP() {
		return new PMapMemberFBT();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initMap();
		initUI();
		createPopup();
		doQueryData();
	}

	private String mMemberIds;// 用户ids（数据是由拜访地图的结构图（部门及员工）决定）-----现在改为可查看的全部员工
	public void doQueryData(){
//		mMemberIds = SPUtils.getSValues(ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS);
		//暂时这样处理：如果mMemberIds是空的，先查询员工列表(tree)再查询数据
		if(MyStringUtil.isEmpty(mMemberIds)){
			getP().queryMemberTree(context);
		}else{
			getP().queryData(context, mMemberIds);
		}
	}

	/**
	 * 初始化Intent
	 */
	private int mid;// 当前选中的用户id(默认自己)
	private String name;// 用户名称(默认自己)
	private String date;// 日期(默认今天)
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			mid = intent.getIntExtra(ConstantUtils.Intent.USER_ID, 0);
			name = intent.getStringExtra(ConstantUtils.Intent.USER_NAME);
			date = intent.getStringExtra(ConstantUtils.Intent.DATE);
		}
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		initHead();
		initOtherView();
		initAdapter();
		initAutoRefresh();//自动刷新
	}

	//头部
	@BindView(R.id.head_left)
	View mViewLeft;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorBlue(context);
		mViewLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadTitle.setText("员工分布图");
	}

	//基本View
	private Button btn_member;
	private CheckBox cb_weixingtu;// 卫星图
	private CheckBox cb_lukuangtu;// 交通图
	private void initOtherView() {
		btn_member = (Button) findViewById(R.id.btn_member);
		// 卫星图-交通图
		cb_weixingtu = (CheckBox) findViewById(R.id.cb_weixingtu);
		cb_lukuangtu = (CheckBox) findViewById(R.id.cb_lukuangtu);
		// 向上，向下
		findViewById(R.id.rb_bfhf).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpActivity(MapPlaybackActivity.class);// 跳到拜访回放
			}
		});
		findViewById(R.id.rb_gjhf).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpActivity(MapTrackActivity.class);// 跳到轨迹回放
			}
		});
	}

	/**
	 * 适配器--员工列表
	 */
	RecyclerView mRvMember;
	MemberAdapter mMemberAdapter;
	private int mCurrentPosition = 0;// 记录之前点击是哪个标记物(个数)
	private void initAdapter() {
		mRvMember = findViewById(R.id.rv_member);
		mRvMember.addItemDecoration(MyDividerUtil.getH1CGray(context));
		mRvMember.setLayoutManager(new LinearLayoutManager(this));
		mMemberAdapter = new MemberAdapter(mid);
		mRvMember.setAdapter(mMemberAdapter);
		mMemberAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
				try {
//					TrackList trackList = mTrackList.get(position);
//					String location = trackList.getLocation();
//					String[] split = location.substring(1, location.length() - 1).split(",");
//					LatLng latLng = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
//					latLng_center = latLng;
//					mCurrentPosition = position;
//					MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(latLng_center);//中心点
//					mBaiduMap.setMapStatus(u2);
//					//设置员工分布图--弹窗数据
//					showPopwin(trackList);
//					mid = trackList.getUserId();
//					name = trackList.getUserNm();
					TrackList track = mMemberAdapter.getData().get(position);
					mid = track.getUserId();
					name = track.getUserNm();
					isCenter = true;
					showData(mMemberTrackList, mMemberIds, true);

					mMemberAdapter.setMid(mid);
					mMemberAdapter.notifyDataSetChanged();
					doShowHideRvMember();
					SPUtils.setValues(ConstantUtils.Sp.MAP_MEMBER_USER_ID, "" + mid);
					SPUtils.setValues(ConstantUtils.Sp.MAP_MEMBER_USER_NAME, name);
				}catch (Exception e){
					ToastUtils.showError(e);
				}
			}
		});
	}

	/**
	 * 自动刷新
	 */
	public Disposable sDisposable;
	private boolean isScroll = true;// 是否自动刷新列表，1：界面运行，listview停止滚动--刷新2：界面暂停，listview滚动--不刷新
	private void initAutoRefresh() {
		//异步线程
		sDisposable= Observable.interval(60, TimeUnit.SECONDS)
				.doOnDispose(new Action() {
					@Override
					public void run(){
					}
				})
				.subscribe(new Consumer<Long>() {
					@Override
					public void accept(Long count) throws Exception {
						if (isScroll) {
							getP().queryData(null, mMemberIds);
						}
					}
				});
	}

	//TODO*********************************点击事件**********************************************
	@OnClick({R.id.btn_member,R.id.cb_weixingtu,R.id.cb_lukuangtu,R.id.btn_up,R.id.btn_down})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_member:// 成员列表
			doShowHideRvMember();
			break;

		case R.id.cb_weixingtu:// 卫星图
			boolean checked = cb_weixingtu.isChecked();
			if (checked) {
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			} else {
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}
			break;
		case R.id.cb_lukuangtu:// 交通图
			boolean checked2 = cb_lukuangtu.isChecked();
			mBaiduMap.setTrafficEnabled(checked2);
			break;

		case R.id.btn_up:// 向上
			try{
				if (mTrackList != null && mTrackList.size() > 0) {
					if (mCurrentPosition > 0) {
						TrackList track_ygfbt = mTrackList.get(mCurrentPosition - 1);
						//中心点
						String location = track_ygfbt.getLocation();
						String[] split = location.substring(1, location.length() - 1).split(",");
						LatLng latLng = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
						latLng_center = latLng;
						mCurrentPosition = mCurrentPosition - 1;
						MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(latLng_center);
						mBaiduMap.setMapStatus(u2);
						//设置员工分布图--弹窗数据
						showPopwin(track_ygfbt);
						mid = track_ygfbt.getUserId();
						name = track_ygfbt.getUserNm();
						mMemberAdapter.setMid(mid);
						mMemberAdapter.notifyDataSetChanged();
					} else {
						ToastUtils.showCustomToast("已经是第一位");
					}
				}
			}catch (Exception e){
				ToastUtils.showError(e);
			}
			break;
		case R.id.btn_down:// 向下
			try {
				if (mTrackList != null && mTrackList.size() > 0) {
					if (mCurrentPosition == mTrackList.size() - 1) {
						ToastUtils.showCustomToast("已经是最后一位");
					} else {
						TrackList track_ygfbt = mTrackList.get(mCurrentPosition + 1);
						mCurrentPosition = mCurrentPosition + 1;
						String location = track_ygfbt.getLocation();
						String[] split = location.substring(1, location.length() - 1).split(",");
						LatLng latLng = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
						latLng_center = latLng;
						MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLng(latLng_center);
						mBaiduMap.setMapStatus(u2);
						//设置员工分布图--弹窗数据
						showPopwin(track_ygfbt);
						mid = track_ygfbt.getUserId();
						name = track_ygfbt.getUserNm();
						mMemberAdapter.setMid(mid);
						mMemberAdapter.notifyDataSetChanged();
					}
				}
			}catch (Exception e){
				ToastUtils.showError(e);
			}
			break;
		}
	}

	//TODO**********************生命周期*********************************
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		isScroll=false;//暂停刷新
	}
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		isScroll=true;//重新刷新
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		bdA.recycle();// 回收 bitmap 资源
		bdB.recycle();
		bdC.recycle();
		if (sDisposable!=null) sDisposable.dispose();//关闭自动刷新
	}

	//TODO**********************地图相关*********************************
	//初始化地图
	private BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.qwbblue);
	private BitmapDescriptor bdB = BitmapDescriptorFactory.fromResource(R.drawable.qwbred);
	private BitmapDescriptor bdC = BitmapDescriptorFactory.fromResource(R.drawable.qwbgray);
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LatLng latLng_center;// 中心点
	private void initMap() {
		mMapView =  findViewById(R.id.bmapView2);
		mBaiduMap = mMapView.getMap();
		// 地图缩放初始化
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setOnMarkerClickListener(new MarkerListener());// 标记物点击监听
		mBaiduMap.setOnMapClickListener(new MapListener());// 地图点击监听
	}

	// 标记物监听--marker
	private final class MarkerListener implements OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {
			LatLng latlng2 = marker.getPosition();
			for (int i = 0; i < mTrackList.size(); i++) {
				TrackList track_ygfbt = mTrackList.get(i);
				if (marker.getTitle().equals(track_ygfbt.getUserNm())) {
					if (latlng2 != null) {
						latLng_center=latlng2;
						mCurrentPosition = i;
						mid = track_ygfbt.getUserId();
						name = track_ygfbt.getUserNm();
						mMemberAdapter.setMid(mid);
						mMemberAdapter.notifyDataSetChanged();
						//设置员工分布图--弹窗数据
						showPopwin(track_ygfbt);
					}
					return true;
				}
			}
			return true;
		}
	}

	//地图点击监听：隐藏窗体InfoWindow（原因：窗体覆盖标记物）
	private final class MapListener implements OnMapClickListener {
		@Override
		public boolean onMapPoiClick(MapPoi arg0) {
			return false;
		}

		@Override
		public void onMapClick(LatLng arg0) {
			mBaiduMap.hideInfoWindow();
		}
	}

	//TODO**********************窗体相关*********************************
	private TextView tv_khNm_ygfbt;
	private TextView tv_wangluo_type;
	private TextView tv_shudu_ygfbt;
	private TextView tv_licheng_ygfbt;
	private TextView tv_time_ygfbt;
	private TextView tv_address_ygfbt;
	private EasyPopup mEasyPop;
	private View mContentView;
	public void createPopup() {
		mEasyPop = new EasyPopup(context)
				.setContentView(R.layout.x_popup_yuangongfenbutu)
				.createPopup();
		// 设置tv_address的宽度为屏幕的0.7
		LinearLayout ll_address =mEasyPop.getView(R.id.ll_address);
		int screenWidth = MyApp.getScreenWidth();// 获取屏幕宽度
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_address.getLayoutParams(); // 取控件textView当前的布局参数
		params.width = (int) (screenWidth * 0.7);
		ll_address.setLayoutParams(params);
		mContentView=mEasyPop.getContentView();
		tv_khNm_ygfbt = mEasyPop.getView(R.id.tv_khNm);
		tv_wangluo_type = mEasyPop.getView(R.id.tv_wangluo_type);
		tv_shudu_ygfbt = mEasyPop.getView(R.id.tv_shudu);
		tv_licheng_ygfbt = mEasyPop.getView(R.id.tv_licheng);
		tv_time_ygfbt = mEasyPop.getView(R.id.tv_time_ygfbt);
		tv_address_ygfbt =mEasyPop.getView(R.id.tv_address_ygfbt);
	}

	//TODO**********************************接口处理*****************************************
	//TODO 参数：flag
	private List<TrackListBean.TrackList> mMemberTrackList = new ArrayList<>();
	public void showData(List<TrackListBean.TrackList> rows, String memberIds, boolean flag){
		this.mMemberIds = memberIds;
		if (!flag){
			mMemberTrackList.clear();
		}
		mTrackList.clear();
		latLngList.clear();
		ztList.clear();
		nameList.clear();
		if(rows != null && !rows.isEmpty()){
			for (int i = 0; i < rows.size(); i++) {
				TrackListBean.TrackList track = rows.get(i);
				String location = track.getLocation();
				if ("离职".equals(track.getZt()) || MyStringUtil.isEmpty(track.getUserNm())) {
				} else {
					if (!flag){
						mMemberTrackList.add(track);
					}
					String[] split = location.substring(1, location.length() - 1).split(",");
					LatLng latLng = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
					String zt = track.getZt();
					String name1 = track.getUserNm();
					int userId = track.getUserId();
					//过滤非中心点的“下班”状态的员工
					if(!(MyStringUtil.eq("下班",zt) && mid != userId)){
						if (mid == userId) {
							latLng_center = latLng;
							mCurrentPosition = mTrackList.size();// 列表的第几个（上下按钮用到）
							name = track.getUserNm();
						}
						mTrackList.add(track);
						latLngList.add(latLng);
						ztList.add(zt);
						nameList.add(name1);
					}
				}
			}
		}
		// 设置员工分布图的数据
		showDataFBT();
		if (!flag){
			mMemberAdapter.setMid(mid);
			mMemberAdapter.addData(mMemberTrackList);
			mMemberAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 设置员工分布图的数据
	 */
	private List<TrackList> mTrackList = new ArrayList<>();
	private List<LatLng> latLngList = new ArrayList<>();
	private List<String> ztList = new ArrayList<>();
	private List<String> nameList = new ArrayList<>();
	private boolean isCenter = true;// 是否跳到中心点，（刷新不跳到中心点，选中某人已他为中心点，刚进入页面已自己为中心点）
	private void showDataFBT() {
		mBaiduMap.clear();// 绘制新覆盖物前，清空之前的覆盖物
		// 添加标记物
		if (latLngList != null && latLngList.size() > 0) {
			if (latLng_center == null) {
				TrackList trackList = mTrackList.get(0);
				String location = trackList.getLocation();
				String[] split = location.substring(1, location.length() - 1).split(",");
				LatLng latLng = new LatLng(Double.valueOf(split[1]), Double.valueOf(split[0]));
				latLng_center = latLng;
				mCurrentPosition = 0;
				name = trackList.getUserNm();
				mid = trackList.getUserId();
			}
			if (isCenter) {
				if(latLng_center!=null && mBaiduMap!=null){
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng_center);
					mBaiduMap.setMapStatus(u);
					isCenter = false;
				}
			}
			if (latLng_center != null) {
				TrackList track_ygfbt = mTrackList.get(mCurrentPosition);
				if (track_ygfbt != null) {
					showPopwin(track_ygfbt);
				}
			}
			for (int i = 0; i < latLngList.size(); i++) {
				LatLng latLng = latLngList.get(i);
				String zt = ztList.get(i);
				String name = nameList.get(i);
				TrackList trackList = mTrackList.get(i);
				if (MyUtils.isEmptyString(trackList.getAzimuth())) {
					trackList.setAzimuth("0.0");
				}
				initOvlay(latLng, zt, name, Float.valueOf(trackList.getAzimuth()));
			}
		}
	}

	private void initOvlay(LatLng latlng, String type, String title, float rotation) {
		MarkerOptions oo = null;
		switch (type) {
			case "在线":// 运动
				bdA = setView_yundong(title, rotation);
				oo = new MarkerOptions().position(latlng).icon(bdA).zIndex(9).draggable(true).title(title);
				break;
			case "异常":// 离线
				bdC = setView_lixian(title, rotation);
				oo = new MarkerOptions().position(latlng).icon(bdC).zIndex(9).draggable(true).title(title);
				break;
			case "下班":// 下班
				bdB = setView_xiaban(title, rotation);
				oo = new MarkerOptions().position(latlng).icon(bdB).zIndex(9).draggable(true).title(title);
				break;
		}
		mBaiduMap.addOverlay(oo);
	}

	// --------------设置运动，下班，离线--3张图片----------
	private BitmapDescriptor setView_yundong(String title, float rotation) {
		View inflate = getLayoutInflater().inflate(R.layout.x_layout_yundong_fenbutu, null);
		ImageView iv = inflate.findViewById(R.id.iv);
		TextView time = inflate.findViewById(R.id.time);
		iv.setRotation(rotation);
		time.setText(title);
		BitmapDescriptor bd = BitmapDescriptorFactory.fromView(inflate);
		return bd;
	}
	// --------------设置运动，下班，离线--3张图片----------
	private BitmapDescriptor setView_xiaban(String title, float rotation) {
		View inflate = getLayoutInflater().inflate(R.layout.x_layout_xiaban_fenbutu, null);
		ImageView iv = inflate.findViewById(R.id.iv);
		TextView time = inflate.findViewById(R.id.time);
		iv.setRotation(rotation);
		time.setText(title);
		BitmapDescriptor bd = BitmapDescriptorFactory.fromView(inflate);
		return bd;
	}
	// --------------设置运动，下班，离线--3张图片----------
	private BitmapDescriptor setView_lixian(String title, float rotation) {
		View inflate = getLayoutInflater().inflate(R.layout.x_layout_lixian_fenbutu, null);
		ImageView iv = inflate.findViewById(R.id.iv);
		TextView time = inflate.findViewById(R.id.time);
		iv.setRotation(rotation);
		time.setText(title);
		BitmapDescriptor bd = BitmapDescriptorFactory.fromView(inflate);
		return bd;
	}

	/**
	 * 设置员工分布图-弹窗数据
	 */
	private void showPopwin(TrackList track_ygfbt) {
		String userNm = track_ygfbt.getUserNm();
		String times = track_ygfbt.getTimes();
		String address = track_ygfbt.getAddress();
		String locationFrom = track_ygfbt.getLocationFrom();
		tv_khNm_ygfbt.setText(userNm);// 用户名han
		tv_time_ygfbt.setText(times);// 时间
		tv_address_ygfbt.setText(address);// 地址
		if (!MyUtils.isEmptyString(locationFrom) && locationFrom.length()>4) {
			tv_wangluo_type.setText(track_ygfbt.getLocationFrom().substring(0, 4));// 定位类型--gps,wifi,lx
		}
		String stayTime = track_ygfbt.getStayTime();
		if (!MyUtils.isEmptyString(stayTime)) {
			tv_shudu_ygfbt.setText("静止：" + Double.valueOf(stayTime) / 60 + "分");
		} else {
			if(!MyUtils.isEmptyString(track_ygfbt.getSpeed())){
				if ("0.0".equals(track_ygfbt.getSpeed()) || "0".equals(track_ygfbt.getSpeed())) {
					tv_shudu_ygfbt.setText("行驶：" + 0 + "km/h");// 速度
				} else {
					// 保留两位小数
					double speed = Double.valueOf(track_ygfbt.getSpeed()) * 3.6;
					tv_shudu_ygfbt.setText("行驶：" + String.format("%.2f", speed) + "km/h");// 速度
				}
			}
		}
		String workingDistance = track_ygfbt.getWorkingDistance();
		if (!MyUtils.isEmptyString(workingDistance)) {
			double distance = Double.valueOf(workingDistance) / 1000;
			tv_licheng_ygfbt.setText("单日里程:" + String.format("%.2f", distance) + "公里");// 里程
		}
		InfoWindow mInfoWindow = new InfoWindow(mContentView, latLng_center, -50);
		mBaiduMap.showInfoWindow(mInfoWindow);
	}

    //TODO************************************************************************************************************************************
    //TODO************************************************************************************************************************************
	/**
	 * 显示隐藏“员工列表”
	 */
	private void doShowHideRvMember() {
		if (mRvMember.getVisibility() == View.VISIBLE) {// 展开
//				btn_member.setBackgroundResource(R.drawable.icon_member_up);
			mRvMember.setVisibility(View.GONE);
		} else {
//				btn_member.setBackgroundResource(R.drawable.icon_member_down);
			mRvMember.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 跳转界面-拜访回放；轨迹回放
	 */
	private void jumpActivity(Class class1) {
		Router.newIntent(context)
				.putInt(ConstantUtils.Intent.TYPE,2)
				.putInt(ConstantUtils.Intent.USER_ID,mid)//用户id
				.putString(ConstantUtils.Intent.USER_NAME,name)//用户名称
				.putString(ConstantUtils.Intent.DATE, date)//今天
				.to(class1)
				.launch();
		ActivityManager.getInstance().closeActivity(context);
	}



}
