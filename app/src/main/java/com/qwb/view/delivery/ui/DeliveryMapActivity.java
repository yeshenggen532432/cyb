package com.qwb.view.delivery.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DDeliveryCustomerBean;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 物流配送中心：地图
 */
public class DeliveryMapActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_delivery_map;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doUI();
        createPopup();
    }

    private void initIntent() {
        
    }

    private void initUI() {
        initHead();
        initMap();
        intOther();
    }

    // 头部
    private TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);//设置状态栏颜色；透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle = findViewById(R.id.tv_head_title);
        mTvHeadTitle.setText("配送中心地图");
    }

    private Button btn_up;
    private Button btn_down;
    private DDeliveryCustomerBean mCurrentBean;
    private int mCurrentPosition = -1;
    private void intOther() {
        btn_up = findViewById(R.id.btn_up);
        btn_down = findViewById(R.id.btn_down);
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentBean = mList.get(mCurrentPosition - 1);
                mCurrentPosition = mCurrentPosition - 1;
                showPopup();

            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentBean = mList.get(mCurrentPosition + 1);
                mCurrentPosition = mCurrentPosition + 1;
                showPopup();
            }
        });
    }

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private void initMap() {
        mMapView = findViewById(R.id.bmapView1);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);// 地图缩放初始化
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMarkerClickListener(new MarkerListener());// 标记物点击监听
        mBaiduMap.setOnMapClickListener(new MapListener());// 地图点击监听

        initLocation();
        findViewById(R.id.iv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocation = true;
                initLocation();
            }
        });
    }

    private boolean isLocation = false;
    private BDLocation mCurrentLocation;
    private void initLocation(){
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        mCurrentLocation = bdLocation;
                        mBaiduMap.clear();// 要清空，不然图标会重复
                        addPoint();
                        LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                        if(isLocation){
                            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                            mBaiduMap.setMapStatus(u);// 中心点
                        }
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(bdLocation.getRadius())
                                .direction(0).latitude(bdLocation.getLatitude())
                                .longitude(bdLocation.getLongitude()).build();
                        mBaiduMap.setMyLocationData(locData);

                    }

                    @Override
                    public void setAddressListener(String addressStr) {
                    }

                    @Override
                    public void setErrorListener() {
                    }
                });
    }

    // 标记物监听--marker
    private final class MarkerListener implements BaiduMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng latlng = marker.getPosition();
            for (int i = 0; i < mList.size(); i++) {
                DDeliveryCustomerBean bean = mList.get(i);
                if (bean.getLatitude().equals(String.valueOf(latlng.latitude))&& bean.getLongitude().equals(String.valueOf(latlng.longitude))) {
                    mCurrentBean = bean;
                    mCurrentPosition = i;
                    showPopup();
                    break;
                }
            }
            return true;
        }
    }

    //地图点击监听：隐藏窗体
    private final class MapListener implements BaiduMap.OnMapClickListener {
        @Override
        public boolean onMapPoiClick(MapPoi arg0) {
            return false;
        }
        @Override
        public void onMapClick(LatLng arg0) {
            mBaiduMap.hideInfoWindow();
        }
    }

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
        if(mMapView != null){
            mMapView.onDestroy();
		    mMapView = null;
        }
    }

    //doUI
    private List<DDeliveryCustomerBean> mList = new ArrayList<>();
    public void doUI(){
        mList.clear();
        for (DDeliveryCustomerBean bean :ConstantUtils.selectDeliveryCustomerList){
            String lat = bean.getLatitude();
            if(!MyStringUtil.isEmpty(lat) && !"0".equals(lat)){
                mList.add(bean);
            }
        }
        if (mList != null && mList.size() > 0) {
            if (mList.size() == 1) {
                // 当只有一个时：向上和向下都不可点击
                btn_up.setEnabled(false);
                btn_down.setEnabled(false);
            } else {
                // 默认“向上”不能点，“向下”可以点
                btn_up.setEnabled(false);
                btn_down.setEnabled(true);
            }

            addPoint();
        } else {
            btn_up.setEnabled(false);
            btn_down.setEnabled(false);
            ToastUtils.showCustomToast("没有数据");
        }
    }

    /**
     * 添加点
     */
    private void addPoint() {
        try {
            if(mList == null || mList.isEmpty()){
                return;
            }
            List<LatLng> pointList = new ArrayList<>();// 所有的点
            for (int i = 0; i < mList.size(); i++) {
                DDeliveryCustomerBean bean = mList.get(i);
                LatLng latLng2 = new LatLng(Double.valueOf(bean.getLatitude()),Double.valueOf(bean.getLongitude()));
                addText(latLng2, " " + (i+1) + " ");// 文本左右加空格
                Boolean isNav = bean.getNav();
                if(isNav != null && isNav){
                    mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblack)));
                }else {
                    String psState = bean.getPsState();
                    if("1".equals(psState)){
                        mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblue)));
                    }else if("6".equals(psState)){
                        mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbgreen)));
                    }else {
                        mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbred)));
                    }
                }
                pointList.add(latLng2);
            }
            LatLng llC = pointList.get(0);
            LatLng llD = pointList.get(pointList.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder().include(llC).include(llD).build();
            MapStatusUpdate msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.setMapStatus(msUpdate);
        }catch (Exception e){}
    }

    // 添加文字
    private void addText(LatLng latlng, String context) {
        // 添加文字 位置，背景颜色，字体大小，字体颜色，内容，旋转,字体加粗（Typeface.DEFAULT_BOLD）
        OverlayOptions ooText = new TextOptions().bgColor(0xFF000000).fontSize(30).fontColor(0xFFFFFFFF).text(context)
                .rotate(0).position(latlng).align(20, 20).typeface(Typeface.DEFAULT_BOLD);
        mBaiduMap.addOverlay(ooText);
    }

    private void showPopup() {
        //"向上按钮"：处于第一个时（不可点击），其他可点击；"向下按钮"：处于最后一个（不可点击），其他可点击
        if (mCurrentPosition == 0) {
            btn_up.setEnabled(false);
        } else {
            btn_up.setEnabled(true);
        }
        if (mCurrentPosition == mList.size() - 1) {
            btn_down.setEnabled(false);
        }else{
            btn_down.setEnabled(true);
        }
        //中心点
        LatLng latLngCenter = new LatLng(Double.valueOf(mCurrentBean.getLatitude()), Double.valueOf(mCurrentBean.getLongitude()));
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLngCenter);
        mBaiduMap.setMapStatus(u);
        //显示数据
        mPopTvKhNm.setText(mCurrentBean.getKhNm());
        mPopTvAddress.setText(mCurrentBean.getAddress());
        //地图弹出窗体
        InfoWindow infoWindow = new InfoWindow(mContentView, latLngCenter, -50);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    private View mContentView;
    private TextView mPopTvKhNm;
    private TextView mPopTvAddress;
    public void createPopup() {
        EasyPopup mEasyPop = new EasyPopup(context)
                .setContentView(R.layout.x_popup_delivery_map)
                .createPopup();
        mContentView = mEasyPop.getContentView();
        mPopTvKhNm = mEasyPop.getView(R.id.tv_khNm);
        mPopTvAddress = mEasyPop.getView(R.id.tv_address);
        //导航
        mEasyPop.getView(R.id.view_nav).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCurrentBean.setNav(true);
//                MyDataUtils.getInstance().updateDeliveryCustomer(mCurrentBean);
                String lat = mCurrentBean.getLatitude();
                String lng = mCurrentBean.getLongitude();
                if (mCurrentLocation != null){
                    MyMapUtil.getInstance().toNav(context,"" + mCurrentLocation.getLatitude(), "" + mCurrentLocation.getLongitude(), lat, lng);
                }

            }
        });

    }


}
