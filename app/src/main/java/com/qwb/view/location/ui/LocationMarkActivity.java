package com.qwb.view.location.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 位置(标注)
 */
public class LocationMarkActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_location_mark;
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
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            latitude = intent.getStringExtra(ConstantUtils.Intent.LATITUDE);
            longitude = intent.getStringExtra(ConstantUtils.Intent.LONGITUDE);
            address = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            province = intent.getStringExtra(ConstantUtils.Intent.PROVINCE);
            city = intent.getStringExtra(ConstantUtils.Intent.CITY);
            area = intent.getStringExtra(ConstantUtils.Intent.AREA);
        }
    }

    private void doIntent() {
        if ((MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude)) && MyStringUtil.isNotEmpty(longitude)  && !"0".equals(longitude)) {
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.animateMapStatus(u);// 中心点
            if(MyStringUtil.isNotEmpty(address)){
                mTvAddress.setText(address);
            }else{
                //经纬度转地址
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            }
        } else {
            initLocation();
        }
    }

    private void initUI() {
        initHead();
        initOther();
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
        mTvHeadRight.setText("标注");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMarkClickListener();
            }
        });
    }


    /**
     * 其他ui
     */
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.et_city)
    EditText mEtCity;
    @BindView(R.id.et_address)
    EditText mEtAddress;
    private void initOther() {
        //定位
        findViewById(R.id.iv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
            }
        });
        //地址搜索
        findViewById(R.id.iv_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearchClickListener();
            }
        });
    }

    /**
     * 地图
     */
    @BindView(R.id.bmapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private void initMap() {
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomBy(18);//缩放等级
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
        initMapChangeListener();
        initGeoCoder();
    }

    /**
     * 地图改变监听
     */
    private void initMapChangeListener(){
        // 地图移动自带定位功能
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                mTvAddress.setText("定位中...");
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                // 回调成功后用 反Geo变成位置信息
                LatLng latLng = mBaiduMap.getMapStatus().target;
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus arg0, int arg1) {
            }
        });
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
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
     * 初始化-地理编码
     */
    private GeoCoder mGeoCoder;
    private void initGeoCoder() {
        mGeoCoder = GeoCoder.newInstance();// 地理经纬度反编码
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtils.showCustomToast("没有找到检索结果");
                    return;
                }
                latitude = "" + result.getLocation().latitude;
                longitude = "" + result.getLocation().longitude;
                address = result.getAddress();
                province = result.getAddressDetail().province;
                city = result.getAddressDetail().city;
                mTvAddress.setText(address);
                // 中心点
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(result.getLocation());
                mBaiduMap.setMapStatus(u);
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    ToastUtils.showCustomToast("没有找到检索结果");
                    return;
                }
                //如果要获取：省份；市；区
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(result.getLocation()));
            }
        });
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
        mGeoCoder.destroy();
        mBaiduMap.setMyLocationEnabled(false);// 关闭定位图层
        mMapView.onDestroy();
        mMapView = null;
    }

    /**
     * 处理地址搜索监听
     */
    private void doSearchClickListener(){
        String city = mEtCity.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
        if (MyStringUtil.isEmpty(city)) {
            ToastUtils.showCustomToast("请输入要搜索的城市");
            return;
        }
        if (MyStringUtil.isEmpty(address)) {
            ToastUtils.showCustomToast("请输入要搜索的具体地址");
            return;
        }
        mGeoCoder.geocode(new GeoCodeOption()
                .city(city)
                .address(address));
    }

    /**
     * 处理点击标注按钮
     */
    private void doMarkClickListener(){
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.Intent.LATITUDE, String.valueOf(latitude));
        intent.putExtra(ConstantUtils.Intent.LONGITUDE, String.valueOf(longitude));
        intent.putExtra(ConstantUtils.Intent.ADDRESS, address);
        intent.putExtra(ConstantUtils.Intent.PROVINCE, province);
        intent.putExtra(ConstantUtils.Intent.CITY, city);
        intent.putExtra(ConstantUtils.Intent.AREA, area);
        setResult(0, intent);
        ActivityManager.getInstance().closeActivity(context);
    }


}
