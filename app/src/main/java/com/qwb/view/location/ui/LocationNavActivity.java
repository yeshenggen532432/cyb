package com.qwb.view.location.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.baidu.location.BDLocation;
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
import com.qwb.common.LocationEnum;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import java.io.File;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 位置：1.查看 3.导航
 */
public class LocationNavActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_location_nav;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initMap();
        initUI();
        doIntent();
    }

    /**
     * 初始化Intent
     */
    private String latitude;
    private String longitude;
    private String address;
    private String province;
    private String city;
    private String area;
    private String mType;
    private boolean mIsRefreshLocation = false;
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra(ConstantUtils.Intent.TYPE);
            latitude = intent.getStringExtra(ConstantUtils.Intent.LATITUDE);
            longitude = intent.getStringExtra(ConstantUtils.Intent.LONGITUDE);
            address = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            province = intent.getStringExtra(ConstantUtils.Intent.PROVINCE);
            city = intent.getStringExtra(ConstantUtils.Intent.CITY);
            area = intent.getStringExtra(ConstantUtils.Intent.AREA);
        }
    }

    private void doIntent(){
        if (LocationEnum.LOOK.getType().equals(mType)) {
            mTvHeadRight.setText("");
            mViewRight.setClickable(false);
        }else if (LocationEnum.NAV.getType().equals(mType)) {
            initLocation();
        }
        initOverlay();
    }

    private void initUI() {
        initHead();
        //定位
        findViewById(R.id.iv_location).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsRefreshLocation = true;
                initLocation();
            }
        });
    }

    /**
     * 初始化头部
     */
    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("我的位置");
        mTvHeadRight.setText("导航");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNavClickListener();
            }
        });
    }

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private void initMap() {
        mMapView = findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
    }

    /**
     * 初始化定位
     */

    private BDLocation mCurrentLocation;
    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        mCurrentLocation = bdLocation;
                        LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());

                        //蓝色的定位图标
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(bdLocation.getRadius())
                                .direction(0)
                                .latitude(bdLocation.getLatitude())
                                .longitude(bdLocation.getLongitude()).build();
                        mBaiduMap.setMyLocationData(locData);

                        //重新定位时，中心点-当前位置，否则终点坐标
                        if(mIsRefreshLocation){
                            MapStatus.Builder builder = new MapStatus.Builder();
                            builder.target(latLng).zoom(18.0f);
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                        }

                    }

                    @Override
                    public void setErrorListener() {
                    }

                    @Override
                    public void setAddressListener(String addressStr) {
                    }
                });
    }



    /**
     * 添加覆盖物
     */
    private BitmapDescriptor bdA;
    private void initOverlay() {
        // 中心点
        if (MyStringUtil.isNotEmpty(latitude) && MyStringUtil.isNotEmpty(longitude)) {
            bdA = BitmapDescriptorFactory.fromResource(R.drawable.map_center_tag2);
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            //标记物
            OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bdA).zIndex(9).draggable(true);
            mBaiduMap.addOverlay(ooA);
            //中心点
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude)));
            mBaiduMap.setMapStatus(u);
            //地址弹窗
            TextView button = new TextView(getApplicationContext());
            button.setBackgroundResource(R.drawable.map_hint);
            button.setTextColor(Color.BLACK);
            button.setText(address);
            InfoWindow mInfoWindow = new InfoWindow(button, latLng, (int) (-35 * MyApp.getI().getBiLi()));
            mBaiduMap.showInfoWindow(mInfoWindow);
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);// 开启定位图层
        mMapView.onDestroy();
        mMapView = null;
        if (bdA != null) {
            bdA.recycle();
        }
    }

    /**
     * 导航监听
     */
    public void doNavClickListener() {
        if (mCurrentLocation == null) {
            ToastUtils.showCustomToast("当前位置获取失败");
            return;
        }
        if (MyStringUtil.isEmpty(latitude) || MyStringUtil.isEmpty(longitude) || "0".equals(latitude) || "0".equals(longitude)) {
            ToastUtils.showCustomToast("没有终点位置");
            return;
        }
        LatLng pt1 = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        LatLng pt2 = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        // 构建 导航参数
        if (new File("/data/data/com.baidu.BaiduMap").exists()) {
            NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2).startName("起始").endName("结束");
            BaiduMapNavigation.openBaiduMapNavi(para, context);
        } else {
            NaviParaOption para = new NaviParaOption().startPoint(pt1).endPoint(pt2);
            BaiduMapNavigation.openWebBaiduMapNavi(para, context);
        }
    }

}
