package com.qwb.view.location.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.qwb.application.MyApp;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;

import java.io.File;

/**
 * 实现显示某个位置的功能
 */
public class MapNavActivity extends BaseNoTitleActivity {
	private boolean isLocation=false;//是否重新定位
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_map_nav);
		Intent intent = getIntent();
		if(intent!=null){
			neednav = intent.getBooleanExtra("neednav", false);
			latitude = intent.getStringExtra("latitude");
			longitude = intent.getStringExtra("longitude");
			location = intent.getStringExtra("location");
		}
		initUI();
	}

	private void initUI() {
		//返回键
		findViewById(R.id.comm_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView comm_title = (TextView) findViewById(R.id.tv_head_title);
		comm_title.setText("查看位置");
		TextView tv_top_right = (TextView) findViewById(R.id.tv_top_right);
		tv_top_right.setOnClickListener(new OnClickListener() {// 导航
			@Override
			public void onClick(View v) {
				if (currentLocation == null) {
					ToastUtils.showCustomToast("当前位置获取失败");
					return;
				}

				if (MyUtils.isEmptyString(latitude) || MyUtils.isEmptyString(longitude)) {
					ToastUtils.showCustomToast("终点位置获取失败");
					return;
				}

				LatLng pt1 = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
				LatLng pt2 = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
				// 构建 导航参数
				if (isInstallByread("com.baidu.BaiduMap")) {
					NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("起始")
							.endName("结束");
					BaiduMapNavigation.openBaiduMapNavi(para, MapNavActivity.this);
				} else {
					NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2);
					BaiduMapNavigation.openWebBaiduMapNavi(para, MapNavActivity.this);
				}
			}
		});
		if (neednav) {
			tv_top_right.setVisibility(View.VISIBLE);
			tv_top_right.setText("导航");
		} else {
			tv_top_right.setVisibility(View.GONE);
		}
		initMap();
		
		//定位当前的位置
		findViewById(R.id.iv_location).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isFirstLoc=true;
				isLocation = true;
				mLocClient.start();
			}
		});
	}

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private void initMap() {
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 地图缩放初始化
		if(!MyUtils.isEmptyString(latitude) && !MyUtils.isEmptyString(longitude)){
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
			mBaiduMap.setMapStatus(msu);
			// 中心点--要导航的终点坐标点（客户的地址坐标）
			MapStatusUpdate u = MapStatusUpdateFactory
					.newLatLng(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
			mBaiduMap.setMapStatus(u);
		}
		//定位--现在的位置
		initLocation();
		initOverlay();
	}

	/**
	 * 初始化定位
	 */
	private LocationClient mLocClient;

	private void initLocation() {
		mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
		mLocClient = new LocationClient(this);// 定位初始化
		mLocClient.registerLocationListener(new MyLocationListenner());
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setCoorType("bd09ll"); // 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	private boolean isFirstLoc = true;
	private String latitude;
	private String location;
	private String longitude;
	private BDLocation currentLocation;//当前定位的对象
	private boolean neednav;
	private BitmapDescriptor bdA;

	/**
	 * 定位SDK监听函数
	 */
	private MyLocationData locData;
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
				locData = new MyLocationData.Builder()
						.accuracy(location.getRadius())
						// 此处设置开发者获取到的方向信息，顺时针0-360
						.direction(0).latitude(location.getLatitude())
						.longitude(location.getLongitude()).build();
				mBaiduMap.setMyLocationData(locData);
				//重新定位时，中心点-当前位置，否则终点坐标
				if(isLocation){
					isLocation=false;
	                MapStatus.Builder builder = new MapStatus.Builder();
	                builder.target(ll).zoom(18.0f);
	                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
				}
			}
			currentLocation = location;
			mLocClient.stop();
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 添加覆盖物
	 */
	private void initOverlay() {
		if (MyUtils.isEmptyString(latitude) || MyUtils.isEmptyString(longitude)) {
			ToastUtils.showCustomToast("终点位置获取失败");
			return;
		}

		if (bdA == null) {
			bdA = BitmapDescriptorFactory.fromResource(R.drawable.map_center_tag2);
			LatLng llA = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
			OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA).zIndex(9).draggable(true);
			mBaiduMap.addOverlay(ooA);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llA);
			mBaiduMap.animateMapStatus(u);
			//布局
			TextView button = new TextView(getApplicationContext());
			button.setBackgroundResource(R.drawable.map_hint);
			button.setTextColor(Color.BLACK);
			button.setText(location);
			InfoWindow mInfoWindow = new InfoWindow(button, llA, (int) (-35 * MyApp.getI().getBiLi()));
			mBaiduMap.showInfoWindow(mInfoWindow);
		}
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void onPause() {
		// MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mBaiduMap.setMyLocationEnabled(false);// 开启定位图层
        mLocClient.stop(); // 退出时销毁定位
        mMapView.onDestroy();
        mMapView = null;
		super.onDestroy();
		// 回收 bitmap 资源
		if (bdA != null) {
			bdA.recycle();
		}
	}
}
