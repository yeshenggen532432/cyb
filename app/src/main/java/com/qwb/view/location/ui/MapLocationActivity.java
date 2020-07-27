package com.qwb.view.location.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.qwb.application.MyApp;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.location.parsent.PMapLocation;
import com.qwb.view.map.ui.MapTrackActivity;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.LocationBean;
import com.qwb.utils.Constans;
import com.qwb.view.call.model.CallonRecordBean;
import com.qwb.view.call.model.QueryCallon;
import com.qwb.view.map.model.TrackListBean.TrackList;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 员工在线-地图
 * 功能: 所在位置界面:添加标记物，添加文字
 * 1: 员工在线-列表-item点击跳转
 * 2：拜访记录模块-（签到地址，签退地址，客户地址）
 * 3：轨迹回放-列表-item点击跳转
 * 4：拜访查询-地址(客户地址，签到地址，签退地址)
 */
public class MapLocationActivity extends XActivity<PMapLocation> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_location;
    }

    @Override
    public PMapLocation newP() {
        return new PMapLocation();
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
    private int mMapType;
    private TrackList mTrackBean;
    private CallonRecordBean mRecordBean;//拜访记录
    private QueryCallon mCallBean;// 拜访查询
    private LocationBean mLocationBean;// 轨迹列表
    private int mAddressType;// 1:签到地址 2:客户地址 3:签退地址
    private String mLat;
    private String mLng;
    private String mAddress;
    private String mUserId;
    private String mUserName;
    private String mDate;
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mMapType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 0);
//            mLat = intent.getStringExtra(ConstantUtils.Intent.LATITUDE);
//            mLng = intent.getStringExtra(ConstantUtils.Intent.LONGITUDE);
//            mLng = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
//            //
//            mUserId = intent.getStringExtra(ConstantUtils.Intent.USER_ID);
//            mUserName = intent.getStringExtra(ConstantUtils.Intent.USER_NAME);
//            mDate = intent.getStringExtra(ConstantUtils.Intent.DATE);
            if (mMapType == 1) {
                mTrackBean = (TrackList) intent.getSerializableExtra(Constans.track);
            } else if (mMapType == 2) {
                mRecordBean = (CallonRecordBean) intent.getSerializableExtra(Constans.callOnRecord);
                mAddressType = intent.getIntExtra("address_type", 2);
            } else if (mMapType == 3) {
                mLocationBean = (LocationBean) intent.getSerializableExtra("locationList");
            } else if (mMapType == 4) {
                mCallBean = (QueryCallon) intent.getSerializableExtra("CallQueryActivity");
            }
        }
    }


    /**
     * 初始化地图
     */
    @BindView(R.id.mapView)
    MapView mMapView;
    BaiduMap mBaiduMap;
    private void initMap() {
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initOther();
    }

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
        mTvHeadTitle.setText("位置");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrackBean != null) {
                    int mid = mTrackBean.getUserId();
                    String name = mTrackBean.getUserNm();
                    String date = mTrackBean.getTimes().substring(0, 10);// 截取日期：0000-00-00
                    Router.newIntent(context)
                            .putInt(ConstantUtils.Intent.TYPE, 3)
                            .putInt(ConstantUtils.Intent.USER_ID, mid)
                            .putString(ConstantUtils.Intent.USER_NAME, name)
                            .putString(ConstantUtils.Intent.DATE, date)
                            .to(MapTrackActivity.class)
                            .launch();
                }
            }
        });
    }

    @BindView(R.id.btn_refresh)
    Button mBtnRefresh;

    private void initOther() {
        mBtnRefresh.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                getP().queryDataGetLocation(context, mTrackBean.getUserId(), mTrackBean.getUserNm());
            }
        });
    }

    //TODO **********************************设置type的状态*********************************************
    private void doIntent() {
        switch (mMapType) {
            case 1:
                setType1();
                break;
            case 2:
                setType2();
                break;
            case 3:
                setType3();
                break;
            case 4:
                setType4();
                break;
        }
    }
    private void setOtherVisiable(int btnVisiable, int rightVisiable, String rightText){
        mTvHeadRight.setVisibility(rightVisiable);
        mBtnRefresh.setVisibility(btnVisiable);
        if(MyStringUtil.isEmpty(rightText)){
            mTvHeadRight.setText("");
            mViewRight.setClickable(false);
        }else{
            mTvHeadRight.setText(rightText);
            mViewRight.setClickable(true);
        }
    }

    //	1:实时查岗模块--item点击跳转
    private void setType1() {
        if (mTrackBean != null) {
            String location = mTrackBean.getLocation();
            // [00000000,00000000]去掉[],解析“,”
            String[] split = location.substring(1, location.length() - 1).split(",");
            String lat = split[1];
            String lng = split[0];
            // 中心点
            setData(lat, lng, mTrackBean.getAddress(), mTrackBean.getUserNm());
            setOtherVisiable(View.VISIBLE, View.VISIBLE, "轨迹\n回放");
        }
    }

    //	2：拜访记录模块--签到地址，签退地址，客户地址点击跳转
    private void setType2() {
        LatLng latLng1 = null;
        LatLng latLng2 = null;
        LatLng latLng3 = null;
        MapStatusUpdate u = null;
        String jd1 = mRecordBean.getJd1();// 签到地址--经度
        String jd2 = mRecordBean.getJd2();// 客户地址--经度
        String jd3 = mRecordBean.getJd3();// 签退地址--经度
        String wd1 = mRecordBean.getWd1();
        String wd2 = mRecordBean.getWd2();
        String wd3 = mRecordBean.getWd3();
        // 客户地址
        if (MyStringUtil.isNotEmpty(jd2) && MyStringUtil.isNotEmpty(wd2)) {
            latLng2 = new LatLng(Double.valueOf(wd2), Double.valueOf(jd2));
            addText(latLng2, " 客户地址 ", 0xFF666666);
            addOverlay(latLng2, R.drawable.qwbgray);
        }
        // 签到地址
        if (MyStringUtil.isNotEmpty(jd1) && MyStringUtil.isNotEmpty(wd1)) {
            latLng1 = new LatLng(Double.valueOf(wd1), Double.valueOf(jd1));
            addText(latLng1, " 签到 ", 0xFF26DAEE);
            addOverlay(latLng1, R.drawable.qwbblue);
        }
        // 签退地址
        if (MyStringUtil.isNotEmpty(jd3) && MyStringUtil.isNotEmpty(wd3)) {
            latLng3 = new LatLng(Double.valueOf(wd3), Double.valueOf(jd3));
            addText(latLng3, " 签退 ", 0xFFE12419);
            addOverlay(latLng3, R.drawable.qwbred);
        }

        switch (mAddressType) {
            case 1:
                if (latLng1 != null) {
                    u = MapStatusUpdateFactory.newLatLng(latLng1);
                    initPopup(latLng1, mRecordBean.getDaddress());
                }
                break;
            case 2:
                if (latLng2 != null) {
                    u = MapStatusUpdateFactory.newLatLng(latLng2);
                    initPopup(latLng2, mRecordBean.getKhaddress());
                }
                break;
            case 3:
                if (latLng3 != null) {
                    u = MapStatusUpdateFactory.newLatLng(latLng3);
                    initPopup(latLng3, mRecordBean.getQtaddress());
                }
                break;
        }
        if (u != null) {
            mBaiduMap.setMapStatus(u);
        }

        setOtherVisiable(View.GONE, View.GONE, null);
    }

    //	3：轨迹列表--item点击事件
    private void setType3() {
        if (mLocationBean != null) {
            String address = mLocationBean.getAddress();
            String lat = String.valueOf(mLocationBean.getLatitude());
            String lng = String.valueOf(mLocationBean.getLongitude());
            setData(lat, lng, address, null);
        }
        setOtherVisiable(View.GONE, View.GONE, null);
    }

    //	4：拜访查询--地址
    private void setType4() {
        LatLng latLng1 = null;
        LatLng latLng2;
        LatLng latLng3;
        MapStatusUpdate u;
        String jd1 = mCallBean.getLongitude();// 签到地址--经度
        String jd2 = mCallBean.getLongitude2();// 签退地址--经度
        String jd3 = mCallBean.getLongitude3();// 客户地址--经度
        String wd1 = mCallBean.getLatitude();//
        String wd2 = mCallBean.getLatitude2();//
        String wd3 = mCallBean.getLatitude3();//
        // 客户地址
        if (MyStringUtil.isNotEmpty(jd3) && MyStringUtil.isNotEmpty(wd3)) {
            latLng2 = new LatLng(Double.valueOf(wd3), Double.valueOf(jd3));
            addText(latLng2, "客户地址", 0xFF666666);
            addOverlay(latLng2, R.drawable.qwbgray);
        }
        // 签到地址
        if (MyStringUtil.isNotEmpty(jd1) && MyStringUtil.isNotEmpty(wd1)) {
            latLng1 = new LatLng(Double.valueOf(wd1), Double.valueOf(jd1));
            addText(latLng1, " 签到 ", 0xFF26DAEE);
            addOverlay(latLng1, R.drawable.qwbblue);
        }
        // 签退地址
        if (MyStringUtil.isNotEmpty(jd2) && MyStringUtil.isNotEmpty(wd2)) {
            latLng3 = new LatLng(Double.valueOf(wd2), Double.valueOf(jd2));
            addText(latLng3, " 签退 ", 0xFFE12419);
            addOverlay(latLng3, R.drawable.qwbred);
        }
        if (latLng1 != null) {
            u = MapStatusUpdateFactory.newLatLng(latLng1);
            initPopup(latLng1, mCallBean.getAddress());
            mBaiduMap.setMapStatus(u);
        }

        setOtherVisiable(View.GONE, View.GONE, null);
    }









    //TODO ******************************生命周期相关*****************************************
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
    }


    //TODO ******************************接口相关*****************************************

    /**
     * 设置显示数据
     */
    public void setData(String lat, String lng, String address, String name) {
        mBaiduMap.clear();
        if (MyStringUtil.isNotEmpty(lat) && MyStringUtil.isNotEmpty(lng)) {
            LatLng latLng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.setMapStatus(u);
            if (MyStringUtil.isNotEmpty(name)) {
                addText(latLng, name, 0xFFE12419);
            }
            addOverlay(latLng, R.drawable.map_center_tag2);
            initPopup(latLng, address);
        }
    }

    // 添加文字
    private void addText(LatLng latlng, String context, int bgcolor) {
        // 添加文字 位置，背景颜色，字体大小，字体颜色，内容，旋转
        OverlayOptions ooText = new TextOptions().bgColor(bgcolor).fontSize(25).fontColor(0xFF000000)
                .text("　" + context + "　").rotate(0).position(latlng).typeface(Typeface.DEFAULT_BOLD);
        mBaiduMap.addOverlay(ooText);
    }

    private void initPopup(final LatLng latLng, String address) {
        if (latLng == null){
            return;
        }
        if (MyStringUtil.isNotEmpty(address)){
            TextView button = new TextView(getApplicationContext());
            button.setBackgroundResource(R.drawable.map_hint);
            button.setTextColor(Color.BLACK);
            button.setText(address);
            InfoWindow mInfoWindow = new InfoWindow(button, latLng, (int) (-20 * MyApp.getI().getBiLi()));
            mBaiduMap.showInfoWindow(mInfoWindow);
        }else{
            MyMapUtil.getInstance()
                    .reverseGeoCode(latLng)
                    .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                        @Override
                        public void setOnSuccessListener(BDLocation bdLocation) {
                        }
                        @Override
                        public void setErrorListener() {
                        }
                        @Override
                        public void setAddressListener(String addressStr) {
                            TextView button = new TextView(getApplicationContext());
                            button.setBackgroundResource(R.drawable.map_hint);
                            button.setTextColor(Color.BLACK);
                            button.setText(addressStr);
                            InfoWindow mInfoWindow = new InfoWindow(button, latLng, (int) (-20 * MyApp.getI().getBiLi()));
                            mBaiduMap.showInfoWindow(mInfoWindow);
                        }
                    });
        }
    }

    // 添加标记物
    private void addOverlay(LatLng latlng, int imgRes) {
        MarkerOptions markerOptions = new MarkerOptions().position(latlng)
                .icon(BitmapDescriptorFactory.fromResource(imgRes));
        mBaiduMap.addOverlay(markerOptions);
    }
}
