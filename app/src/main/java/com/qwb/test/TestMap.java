package com.qwb.test;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyMapUtil;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *  地图
 */

public class TestMap {

    private Activity context;

    private List<LatLng> mLagLngList = new ArrayList<>();// 所有的点
    private LatLng mCenterLatLng = new LatLng(28.333365, 118.303030);

    @BindView(R.id.mapView)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private void initMap() {
        mBaiduMap = mMapView.getMap();

        // 标记物点击监听
        initMarkerListener();

        //所有的经纬度在可见区域内
        LatLng llC = mLagLngList.get(0);
        LatLng llD = mLagLngList.get(mLagLngList.size() - 1);
        LatLngBounds bounds = new LatLngBounds.Builder().include(llC).include(llD).build();
        MapStatusUpdate msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
        mBaiduMap.setMapStatus(msUpdate);

        //缩放比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18);
        mBaiduMap.setMapStatus(msu);

        //中心点
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(mCenterLatLng);
        mBaiduMap.setMapStatus(u);

        ////地图点击监听:关闭弹窗
        initMapClickListener();
    }


    /**
     * 定位
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

                        //中心点
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                        mBaiduMap.setMapStatus(u);

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
     * 添加线:至少需要两个点
     */
    private void addLine() {
        if (MyCollectionUtil.isNotEmpty(mLagLngList) && mLagLngList.size() > 1) {
            PolylineOptions polylineOptions = new PolylineOptions().points(mLagLngList).width(6).color(Color.RED);
            mBaiduMap.addOverlay(polylineOptions);
        }
    }

    /**
     * 添加标记物
     */
    private void addMarker(){
        if (MyCollectionUtil.isNotEmpty(mLagLngList)) {
            for (int i = 0; i < mLagLngList.size(); i++) {
                LatLng latLng = mLagLngList.get(i);
                addText(latLng, "  " + (i + 1) + "  ");
                mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbred)));
            }
        }
    }

    /**
     * 添加文字
      */
    private void addText(LatLng latlng, String context) {
        // 添加文字 位置，背景颜色，字体大小，字体颜色，内容，旋转,字体加粗（Typeface.DEFAULT_BOLD）
        OverlayOptions ooText = new TextOptions().bgColor(0xFF000000).fontSize(20).fontColor(0xFFFFFFFF).text(context)
                .rotate(0).position(latlng).align(10, 10).typeface(Typeface.DEFAULT_BOLD);
        mBaiduMap.addOverlay(ooText);
    }

    /**
     * 标记物监听
     */
    private void initMarkerListener(){
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //标记物的经纬度
                LatLng latlng = marker.getPosition();
                //自己逻辑
               showPopup();
                return true;
            }
        });
    }

    /**
     * 弹窗
     */
    private void showPopup(){
        TextView tv = new TextView(context);
        tv.setTextColor(context.getResources().getColor(R.color.x_main_color));
        tv.setBackgroundColor(context.getResources().getColor(R.color.white));
        tv.setText("123456");

        LatLng latLng = new LatLng(28.323232,118.363636);

        InfoWindow infoWindow = new InfoWindow(tv, latLng, -50);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    /**
     * //地图点击监听:关闭弹窗
     */
    private void initMapClickListener() {
        //地图点击监听:关闭弹窗
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }




}
