package com.qwb.view.plan.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.deadline.statebutton.StateButton;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.event.PlanLineEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.customer.model.KhtypeAndKhlevellBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.plan.parsent.PPlanLineMap;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 计划拜访-线路地图
 */
public class PlanLineMapActivity extends XActivity<PPlanLineMap> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_plan_line_map;
    }

    @Override
    public PPlanLineMap newP() {
        return new PPlanLineMap();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        initMap();
        getP().queryDataCustomerType(null);
    }

    private int mType;//1.可以在地图上添加或移除客户（编辑线路）
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 0);
        }
    }

    private void initUI() {
        initHead();
        initButton();
    }

    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.head_right2)
    View mViewRight2;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("线路地图");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //只有一条线路才有“周边客户”
        if (MyCollectionUtil.isNotEmpty(ConstantUtils.selectCustomerLists) && ConstantUtils.selectCustomerLists.size() == 1){
            mTvHeadRight.setText("周边客户▼");
            mViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogCustomerType();
                }
            });
        }
    }

    @BindView(R.id.btn_up)
    Button mBtnUp;
    @BindView(R.id.btn_down)
    Button mBtnDown;
    private void initButton() {
        mBtnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doButton(1);
            }
        });
        mBtnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doButton(2);
            }
        });
    }

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private void initMap() {
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        //缩放比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18);
        mBaiduMap.setMapStatus(msu);
        //添加线路客户：折现，标记物，标记物监听等等
        addLineCustomer();
    }

    /**
     * 添加线路客户：折现，标记物，标记物监听等等
     */
    private void addLineCustomer(){
        addData();
        //添加线
        addLine();
        //添加标记物
        addMarker();
        //点击标记物监听
        initMarkerListener();
    }


    //TODO***************************************************************************************************************************************************
    /**
     * 上下键按钮
     */
    private int mCurrentPosition = 0;
    private int mIndex = 0;//第几条线路的序号
    private void doButton(int type) {
        try {
            if (1 == type) {
                mCurrentPosition--;
            } else {
                mCurrentPosition++;
            }
            //中心点
            LatLng latLng = mLatLngLists.get(mIndex).get(mCurrentPosition);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.setMapStatus(u);

            showPopupLineCustomer(latLng, String.valueOf(mIndex));
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 上下键按钮(是否可以按)
     */
    private void doButtonEnabled() {
        if (mCurrentPosition == 0) {
            mBtnUp.setEnabled(false);
            ToastUtils.showCustomToast("第一个");
        } else {
            mBtnUp.setEnabled(true);
        }
        if (mCurrentPosition == mLatLngLists.get(mIndex).size() - 1) {
            mBtnDown.setEnabled(false);
            ToastUtils.showCustomToast("最后一个");
        } else {
            mBtnDown.setEnabled(true);
        }
    }

    /**
     * 获取线路所有的客户的经纬度数据
     */
    private List<List<LatLng>> mLatLngLists = new ArrayList<>();// 所有的点
    private void addData() {
        mLatLngLists.clear();
        if (MyCollectionUtil.isNotEmpty(ConstantUtils.selectCustomerLists)) {
            for (int i = 0; i < ConstantUtils.selectCustomerLists.size(); i++) {
                List<MineClientInfo> customerList = ConstantUtils.selectCustomerLists.get(i);
                List<LatLng> latLngList = new ArrayList<>();
                for (int z = 0; z < customerList.size(); z++) {
                    MineClientInfo bean = customerList.get(z);
                    if (bean != null) {
                        String latitude = bean.getLatitude();
                        String longitude = bean.getLongitude();
                        if (MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude) && MyStringUtil.isNotEmpty(longitude) && !"0".equals(longitude)) {
                            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                            latLngList.add(latLng);
                        }
                    }
                }
                mLatLngLists.add(latLngList);
            }
        }
    }

    /**
     * 线路客户：折现图
     */
    private void addLine() {
        if (MyCollectionUtil.isNotEmpty(mLatLngLists)) {
            for (int i = 0; i < mLatLngLists.size(); i++) {
                List<LatLng> latLngList = mLatLngLists.get(i);
                if (MyCollectionUtil.isNotEmpty(latLngList)) {
                    if (i == 0) {
                        //第一个为中心点
                        LatLng cLatLng = latLngList.get(0);
                        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(cLatLng);
                        mBaiduMap.setMapStatus(u);
                    }

                    int color = Color.RED;
                    if (i % 4 == 0) {
                        color = Color.RED;
                    } else if (i % 4 == 1) {
                        color = getResources().getColor(R.color.x_c_green_179);
                    } else if (i % 4 == 2) {
                        color = getResources().getColor(R.color.yundong);
                    } else if (i % 4 == 3) {
                        color = getResources().getColor(R.color.gray_6);
                    }
                    // 线:至少两个点
                    if (latLngList.size() > 1) {
                        PolylineOptions polylineOptions = new PolylineOptions().points(latLngList).width(6).color(color);
                        mBaiduMap.addOverlay(polylineOptions);
                    }
                }
            }
        }
    }

    /**
     * 线路客户：标记物
     */
    private void addMarker() {
        if (MyCollectionUtil.isNotEmpty(mLatLngLists)) {
            for (int i = 0; i < mLatLngLists.size(); i++) {
                List<LatLng> latLngList = mLatLngLists.get(i);
                if (MyCollectionUtil.isNotEmpty(latLngList)) {
                    for (int z = 0; z < latLngList.size(); z++) {
                        LatLng latLng = latLngList.get(z);
                        addText(latLng, "  " + (z + 1) + "  ");// 文本左右加空格

                        int drawableId = R.drawable.qwbred;
                        if (i % 4 == 0) {
                            drawableId = R.drawable.qwbred;
                        } else if (i % 4 == 1) {
                            drawableId = R.drawable.qwbgreen;
                        } else if (i % 4 == 2) {
                            drawableId = R.drawable.qwbblue;
                        } else if (i % 4 == 3) {
                            drawableId = R.drawable.qwbgray;
                        }
                        //备注：标记物以title作为index(第几条线路的序号)
                        mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(drawableId)).title("" + i));
                    }
                }
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
    private void initMarkerListener() {
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                LatLng latLng = marker.getPosition();//标记物的经纬度
                if ("-1".equals(title)) {
                    showPopupNearCustomer(latLng);
                } else {
                    showPopupLineCustomer(latLng, title);
                }
                return true;
            }
        });
    }

    /**
     * 线路客户：弹窗
     */
    View contentView;
    TextView mTvKhNm;
    StateButton mSbAdd;
    private void showPopupLineCustomer(LatLng latLng, String index) {
        if (MyCollectionUtil.isNotEmpty(ConstantUtils.selectCustomerLists) && MyStringUtil.isNotEmpty(index)) {
            List<MineClientInfo> customerList = ConstantUtils.selectCustomerLists.get(Integer.valueOf(index));
            //自己逻辑:弹窗（客户信息）
            if (MyCollectionUtil.isNotEmpty(customerList)) {
                for (int i = 0; i < customerList.size(); i++) {
                    final MineClientInfo bean = customerList.get(i);
                    String latitude = bean.getLatitude();
                    String longitude = bean.getLongitude();
                    //备注：String转double,如果尾号是0会去掉，导致equals()为false
                    if (MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude) && MyStringUtil.isNotEmpty(longitude) && !"0".equals(longitude)) {
                        if (String.valueOf(latLng.latitude).equals("" + Double.valueOf(latitude)) && String.valueOf(latLng.longitude).equals("" + Double.valueOf(longitude))) {
                            if (contentView == null) {
                                contentView = LayoutInflater.from(context).inflate(R.layout.x_popup_plan_line_map, null);
                                mTvKhNm = contentView.findViewById(R.id.tv_khNm);
                                mSbAdd = contentView.findViewById(R.id.sb_add);
                            }
                            mTvKhNm.setText(bean.getKhNm());
                            //移除客户
                            if (1 == mType){
                                mSbAdd.setVisibility(View.VISIBLE);
                                mSbAdd.setText("移除");
                                mSbAdd.setOnClickListener(new OnNoMoreClickListener() {
                                    @Override
                                    protected void OnMoreClick(View view) {
                                        ConstantUtils.selectCustomerLists.get(0).remove(bean);
                                        mNearCustomerList.add(bean);
                                        doNearCustomer(mNearCustomerList, false);

                                        BusProvider.getBus().post(new PlanLineEvent());
                                    }
                                });
                            }else{
                                mSbAdd.setVisibility(View.GONE);
                            }

                            InfoWindow infoWindow = new InfoWindow(contentView, latLng, -50);
                            mBaiduMap.showInfoWindow(infoWindow);
                            //处理上下键按钮是否可以按
                            mIndex = Integer.valueOf(index);
                            mCurrentPosition = i;
                            doButtonEnabled();
                            break;
                        }
                    }
                }
            }

        }

    }

    /**
     * 处理周边客户
     */
    private List<MineClientInfo> mNearCustomerList = new ArrayList<>();
    public void doNearCustomer(List<MineClientInfo> list, boolean add) {
        mBaiduMap.clear();//地图清空
        addLineCustomer();//添加线路

        if (add){
            this.mNearCustomerList.clear();
            this.mNearCustomerList.addAll(list);
        }
        //经纬度数据
        if (MyCollectionUtil.isNotEmpty(mNearCustomerList)) {
            List<LatLng> latLngList = new ArrayList<>();
            for (int z = 0; z < mNearCustomerList.size(); z++) {
                MineClientInfo bean = mNearCustomerList.get(z);
                if (bean != null) {
                    String latitude = bean.getLatitude();
                    String longitude = bean.getLongitude();
                    if (MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude) && MyStringUtil.isNotEmpty(longitude) && !"0".equals(longitude)) {
                        LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                        latLngList.add(latLng);
                    }
                }
            }

            //标记物
            if (MyCollectionUtil.isNotEmpty(latLngList)) {
                for (int i = 0; i < latLngList.size(); i++) {
                    LatLng latLng = latLngList.get(i);
                    int drawableId = R.drawable.qwbblue;
                    mBaiduMap.addOverlay(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(drawableId)).title("-1"));
                }
            }

        }
    }

    /**
     * 点击周边客户的标记物弹出客户信息
     */
    private void showPopupNearCustomer(LatLng latLng) {
        if (MyCollectionUtil.isNotEmpty(mNearCustomerList)) {
            for (int i = 0; i < mNearCustomerList.size(); i++) {
                final MineClientInfo bean = mNearCustomerList.get(i);
                String latitude = bean.getLatitude();
                String longitude = bean.getLongitude();
                //备注：String转double,如果尾号是0会去掉，导致equals()为false
                if (MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude) && MyStringUtil.isNotEmpty(longitude) && !"0".equals(longitude)) {
                    if (String.valueOf(latLng.latitude).equals("" + Double.valueOf(latitude)) && String.valueOf(latLng.longitude).equals("" + Double.valueOf(longitude))) {
                        if (contentView == null) {
                            contentView = LayoutInflater.from(context).inflate(R.layout.x_popup_plan_line_map, null);
                            mTvKhNm = contentView.findViewById(R.id.tv_khNm);
                            mSbAdd = contentView.findViewById(R.id.sb_add);
                        }
                        mTvKhNm.setText(bean.getKhNm());
                        //添加客户
                        if (1 == mType){
                            mSbAdd.setVisibility(View.VISIBLE);
                            mSbAdd.setText("添加");
                            mSbAdd.setOnClickListener(new OnNoMoreClickListener() {
                                @Override
                                protected void OnMoreClick(View view) {
                                    ConstantUtils.selectCustomerLists.get(0).add(bean);
                                    mNearCustomerList.remove(bean);
                                    doNearCustomer(mNearCustomerList, false);

                                    BusProvider.getBus().post(new PlanLineEvent());
                                }
                            });
                        }else{
                            mSbAdd.setVisibility(View.GONE);
                        }

                        InfoWindow infoWindow = new InfoWindow(contentView, latLng, -50);
                        mBaiduMap.showInfoWindow(infoWindow);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 客户类型数据
     */
    private List<KhtypeAndKhlevellBean.Qdtypels> mCustomerTypeList = new ArrayList<>();
    public void doCustomerType(List<KhtypeAndKhlevellBean.Qdtypels> list){
        if (MyCollectionUtil.isNotEmpty(list)) {
            this.mCustomerTypeList.addAll(list);
        }
    }

    /**
     * 对话框：周边客户（客户类型）
     */
    private String mCustomerType;
    private void showDialogCustomerType(){
        if (MyCollectionUtil.isNotEmpty(mCustomerTypeList)){
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            DialogMenuItem item0 = new DialogMenuItem("全部", 0);
            items.add(item0);
            for (KhtypeAndKhlevellBean.Qdtypels customerType : mCustomerTypeList){
                DialogMenuItem item = new DialogMenuItem(customerType.getQdtpNm(), 0);
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择客户类型").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogMenuItem item = items.get(position);
                    if (position == 0){
                        mCustomerType = null;
                        mTvHeadRight.setText("周边客户"+"▼");
                    }else{
                        mCustomerType = item.mOperName;
                        mTvHeadRight.setText(item.mOperName+"▼");
                    }

                    if (MyCollectionUtil.isNotEmpty(ConstantUtils.selectCustomerLists)) {
                        String customerJson = JSON.toJSONString(ConstantUtils.selectCustomerLists.get(0));
                        getP().queryDataNearCustomer(context, customerJson, JSON.toJSONString(mCustomerType));
                    }
                }
            });
        }
    }


}
