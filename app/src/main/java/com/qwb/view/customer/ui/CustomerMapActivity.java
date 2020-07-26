package com.qwb.view.customer.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.customer.model.CustomerBean;
import com.qwb.view.customer.parsent.PCustomerMap;
import com.qwb.widget.MyCustomerMapPopup;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.KhtypeAndKhlevellBean;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.qiweibao.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 客户分布图
 */
public class CustomerMapActivity extends XActivity<PCustomerMap> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_customer_map;
    }

    @Override
    public PCustomerMap newP() {
        return new PCustomerMap();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        initMap();
        createPopupScreening();
        getSpData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSpData();
    }

    /**
     * 查询数据
     */
    private String mMemIds = SPUtils.getID(), mMemberNames = SPUtils.getSValues(ConstantUtils.Sp.USER_NAME);
    private String mProvince, mCity, mArea, mCustomerType;
    private boolean mCbNormal;
    private void queryData() {
        String customerType = "";
        if (MyStringUtil.isNotEmpty(mCustomerType)){
            customerType = JSON.toJSONString(mCustomerType);
        }
        getP().queryData(context, mMemIds, mProvince, mCity, mArea, customerType);
    }

    @BindView(R.id.parent)
    View parent;
    @BindView(R.id.tv_screening)
    TextView mTvScreening;
    private void initUI() {
        initHead();
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
        mTvHeadTitle.setText("客户分布图");
        mTvHeadRight.setText("筛选");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupScreening();
            }
        });
    }

    /**
     * 地图
     */
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private void initMap() {
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        //缩放比例
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15);
        mBaiduMap.setMapStatus(msu);

        //点击标记物监听：开启弹窗
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                doMarkerClickListener(marker);
                return true;
            }
        });
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


    //TODO*******************************************************************************************************************************************************************
    /**
     * 筛选窗体：省市区，客户类型，人员
     */
    private MyCustomerMapPopup mPopup;
    private boolean mTempCbNormal;
    public void createPopupScreening() {
        mPopup = new MyCustomerMapPopup(context);
        mPopup.createPopup();
        mPopup.setOnClickListener(new MyCustomerMapPopup.OnClickListener() {
            @Override
            public void setOnClickListener() {
                mProvince = mTempProvince;
                mCity = mTempCity;
                mArea = mTempArea;
                mCustomerType = mTempCustomerType;
                mMemIds = mTempMemIds;
                mMemberNames = mTempMemberNames;
                mCbNormal = mTempCbNormal;
                mMemberCheckMap.clear();
                queryData();
                setScreeningTip();
            }

            @Override
            public void setOnShowCity(TextView tvShowCity) {
                showDialogCity();
            }

            @Override
            public void setOnShowCustomerType(TextView tvShowCustomerType) {
                if (MyCollectionUtil.isNotEmpty(mCustomerTypeList)) {
                    showDialogCustomerType(null);
                } else {
                    getP().queryDataCustomerType(null);
                }
            }

            @Override
            public void setOnShowMember(TextView tvShowMember) {
                if (MyCollectionUtil.isNotEmpty(mMemberTree)) {
                    showDialogMember(null);
                } else {
                    getP().queryDataMember(context);
                }
            }

            @Override
            public void setCbNormal(CheckBox cbNormal) {
                mTempCbNormal = cbNormal.isChecked();
            }

            @Override
            public void setReset() {
                mTempProvince = "";
                mTempCity = "";
                mTempArea = "";
                mTempCustomerType = "";
                mTempMemIds = "";
                mTempMemberNames = "";
                mPopup.setCityText(mTempProvince, mTempCity, mTempArea);
                mPopup.setCustomerTypeText(mTempCustomerType);
                mPopup.setMemberText(mTempMemberNames);
            }
        });

    }

    /**
     * 显示窗体：筛选
     */
    private void showPopupScreening() {
        mTempProvince = mProvince;
        mTempCity = mCity;
        mTempArea = mArea;
        mTempCustomerType = mCustomerType;
        mTempMemIds = mMemIds;
        mTempMemberNames = mMemberNames;
        mPopup.setCityText(mProvince, mCity, mArea);
        mPopup.setCustomerTypeText(mCustomerType);
        mPopup.setMemberText(mMemberNames);
        mPopup.setCbNormal(mCbNormal);
        mPopup.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    /**
     * 省市区
     */
    private String mTempProvince, mTempCity, mTempArea;
    private JDCityPicker mCityPicker = new JDCityPicker();
    public void showDialogCity() {
        mCityPicker.init(this);
        JDCityConfig jdCityConfig = new JDCityConfig
                .Builder()
                .setProvince(mTempProvince)
                .setCity(mTempCity)
                .setArea(mTempArea)
                .build();
        mCityPicker.setConfig(jdCityConfig);
        mCityPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                if (province != null){
                    mTempProvince = province.getName();
                    if (city != null){
                        mTempCity = city.getName();
                        if (district != null){
                            mTempArea = district.getName();
                        }else{
                            mTempArea = "";
                        }
                    }else{
                        mTempCity = "";
                        mTempArea = "";
                    }
                }else{
                    mTempProvince = "";
                    mTempCity = "";
                    mTempArea = "";
                }
                mPopup.setCityText(mTempProvince, mTempCity, mTempArea);
            }

            @Override
            public void onCancel() {
            }
        });

        mCityPicker.showCityPicker();
    }

    /**
     * 客户类型
     */
    private List<KhtypeAndKhlevellBean.Qdtypels> mCustomerTypeList = new ArrayList<>();
    private String mTempCustomerType;
    public void showDialogCustomerType(List<KhtypeAndKhlevellBean.Qdtypels> list) {
        if (MyCollectionUtil.isNotEmpty(list)) {
            this.mCustomerTypeList.addAll(list);
        }
        if (MyCollectionUtil.isNotEmpty(mCustomerTypeList)) {
            final ArrayList<DialogMenuItem> items = new ArrayList<>();
            DialogMenuItem item0 = new DialogMenuItem("全部", 0);
            items.add(item0);
            for (KhtypeAndKhlevellBean.Qdtypels customerType : mCustomerTypeList) {
                DialogMenuItem item = new DialogMenuItem(customerType.getQdtpNm(), 0);
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("选择客户类型").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DialogMenuItem item = items.get(position);
                    if (position == 0) {
                        mTempCustomerType = null;
                    } else {
                        mTempCustomerType = item.mOperName;
                    }
                    mPopup.setCustomerTypeText(mTempCustomerType);

                }
            });
        }
    }

    /**
     * 选择员工
     */
    private List<TreeBean> mMemberTree = new ArrayList<>();
    private String mTempMemIds, mTempMemberNames;
    private HashMap<Integer, Integer> mMemberCheckMap = new HashMap<>();
    public void showDialogMember(List<TreeBean> dataList) {
        if (MyCollectionUtil.isNotEmpty(dataList)) {
            mMemberTree.clear();
            mMemberTree.addAll(dataList);
        }
        //默认自己(不用)
//        if (MyCollectionUtil.isEmpty(mMemberCheckMap)){
//            mMemberCheckMap.put(Integer.valueOf(SPUtils.getID()) + ConstantUtils.TREE_ID, 1);
//        }
        MyTreeDialog mTreeDialog = new MyTreeDialog(context, mMemberTree, mMemberCheckMap, true);
        mTreeDialog.title("选择员工").show();
        mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
            @Override
            public void onOkListener(String containPIds, String noContainPIds, Map<Integer, Integer> checkMap) {
                mTempMemIds = containPIds;
                mMemberCheckMap.clear();
                mMemberCheckMap.putAll(checkMap);
                String memberNames = "";
                for (TreeBean treeBean : mMemberTree) {
                    if (treeBean.get_id() > ConstantUtils.TREE_ID) {
                        Integer value = checkMap.get(treeBean.get_id());
                        if (value != null && "1".equals(String.valueOf(value))) {
                            if (MyStringUtil.isEmpty(memberNames)) {
                                memberNames += "" + treeBean.getName();
                            } else {
                                memberNames += "," + treeBean.getName();
                            }
                        }
                    }
                }
                mTempMemberNames = memberNames;
                mPopup.setMemberText(mTempMemberNames);
            }
        });
    }

    /**
     * 筛选结果提示
     */
    private void setScreeningTip() {
        StringBuffer sb = new StringBuffer();
        String c = "/";
        sb.append(MyStringUtil.append(mProvince, c))
                .append(MyStringUtil.append(mCity, c))
                .append(MyStringUtil.append(mArea, c))
                .append(MyStringUtil.append(mCustomerType, c))
                .append(MyStringUtil.append(mMemberNames, c));
        if (MyStringUtil.isNotEmpty(sb.toString())) {
            mTvScreening.setText(MyStringUtil.clearEndChar(sb.toString(), c));
            mTvScreening.setVisibility(View.VISIBLE);
        }else{
            mTvScreening.setText("");
            mTvScreening.setVisibility(View.GONE);
        }
    }



    /**
     * 保存：省市区，客户类型，人员
     */
    private void saveSpData() {
        SPUtils.setValues(ConstantUtils.Sp.CUSTOMER_MAP_PROVINCE, mProvince);
        SPUtils.setValues(ConstantUtils.Sp.CUSTOMER_MAP_CITY, mCity);
        SPUtils.setValues(ConstantUtils.Sp.CUSTOMER_MAP_AREA, mArea);
        SPUtils.setValues(ConstantUtils.Sp.CUSTOMER_MAP_CUSTOMER_TYPE, mCustomerType);
        SPUtils.setValues(ConstantUtils.Sp.CUSTOMER_MAP_MEMBER_IDS, mMemIds);
        SPUtils.setValues(ConstantUtils.Sp.CUSTOMER_MAP_MEMBER_NAMES, mMemberNames);
        SPUtils.setBoolean(ConstantUtils.Sp.CUSTOMER_MAP_CB_NORMAL, mCbNormal);
    }

    /**
     * 获取缓存的：省市区，客户类型，人员
     */
    private void getSpData() {
        try {
            Boolean isFirst = SPUtils.getBoolean_true(ConstantUtils.Sp.CUSTOMER_MAP_INIT);
            if (isFirst!= null && isFirst){
                mTvScreening.setVisibility(View.GONE);
                mViewLeft.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showPopupScreening();
                        SPUtils.setBoolean(ConstantUtils.Sp.CUSTOMER_MAP_INIT, false);
                    }
                }, 500);
            }else{
                mProvince = SPUtils.getSValues(ConstantUtils.Sp.CUSTOMER_MAP_PROVINCE);
                mCity = SPUtils.getSValues(ConstantUtils.Sp.CUSTOMER_MAP_CITY);
                mArea = SPUtils.getSValues(ConstantUtils.Sp.CUSTOMER_MAP_AREA);
                mCustomerType = SPUtils.getSValues(ConstantUtils.Sp.CUSTOMER_MAP_CUSTOMER_TYPE);
                mMemIds = SPUtils.getSValues(ConstantUtils.Sp.CUSTOMER_MAP_MEMBER_IDS);
                mMemberNames = SPUtils.getSValues(ConstantUtils.Sp.CUSTOMER_MAP_MEMBER_NAMES);
                mCbNormal = SPUtils.getBoolean(ConstantUtils.Sp.CUSTOMER_MAP_CB_NORMAL);
                if (MyStringUtil.isNotEmpty(mMemIds)) {
                    String[] items = mMemIds.split(",");
                    for (String id: items){
                        mMemberCheckMap.put(Integer.valueOf(id) + ConstantUtils.TREE_ID, 1);
                    }
                }
                setScreeningTip();

                mViewLeft.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCbNormal){
                            queryData();
                        }else{
                            showPopupScreening();
                        }
                    }
                }, 500);
            }

        }catch (Exception e){
        }
    }


    /**
     * 客户在地图上显示
     */
    private List<CustomerBean> mCustomerList = new ArrayList<>();
    public void doShowCustomerMap(List<CustomerBean> list) {
        mBaiduMap.clear();//地图清空

        this.mCustomerList.clear();
        if (MyCollectionUtil.isNotEmpty(list)) {
            this.mCustomerList.addAll(list);

            //中心点
            CustomerBean centerCustomer = list.get(0);
            setCenterLatLng(new LatLng(Double.valueOf(centerCustomer.getLatitude()), Double.valueOf(centerCustomer.getLongitude())));
            XLog.e("中心点:" + MyTimeUtils.getDateYmdhms(0));

            //创建OverlayOptions的集合
            List<OverlayOptions> options = new ArrayList<>();

            XLog.e("start:" + MyTimeUtils.getDateYmdhms(0));
            List<LatLng> latLngList = new ArrayList<>();
            for (int z = 0; z < list.size(); z++) {
                CustomerBean bean = list.get(z);
                if (bean != null) {
                    String latitude = bean.getLatitude();
                    String longitude = bean.getLongitude();
                    if (MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude) && MyStringUtil.isNotEmpty(longitude) && !"0".equals(longitude)) {
                        LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                        latLngList.add(latLng);

                        options.add(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblue)).title("-1"));
                    }
                }
            }
            XLog.e("标记物前:" + MyTimeUtils.getDateYmdhms(0));
            mBaiduMap.addOverlays(options);
            XLog.e("标记物后:" + MyTimeUtils.getDateYmdhms(0));
        }else{
            ToastUtils.showCustomToast("暂无数据");
        }
    }

    //设置中心点
    private void setCenterLatLng(LatLng latLng) {
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(u);
    }

    /**
     * 处理标记物点击监听：弹窗
     * 客户名称;联系人；电话，手机；地址；上次拜访；业务员
     */
    View contentView;
    TextView mTvKhNm, mTvLxr,mTvTel, mTvPhone, mTvAddress,mTvCallDate, mTvMember;
    private void doMarkerClickListener(Marker marker) {
        LatLng latLng = marker.getPosition();//标记物的经纬度
        if (MyCollectionUtil.isNotEmpty(mCustomerList)) {
            for (int i = 0; i < mCustomerList.size(); i++) {
                CustomerBean bean = mCustomerList.get(i);
                String latitude = bean.getLatitude();
                String longitude = bean.getLongitude();
                //备注：String转double,如果尾号是0会去掉，导致equals()为false
                if (MyStringUtil.isNotEmpty(latitude) && !"0".equals(latitude) && MyStringUtil.isNotEmpty(longitude) && !"0".equals(longitude)) {
                    if (String.valueOf(latLng.latitude).equals("" + Double.valueOf(latitude)) && String.valueOf(latLng.longitude).equals("" + Double.valueOf(longitude))) {

                        //弹窗
                        if (contentView == null) {
                            contentView = LayoutInflater.from(context).inflate(R.layout.x_popup_customer_map, null);
                            mTvKhNm = contentView.findViewById(R.id.tv_khNm);
                            mTvLxr = contentView.findViewById(R.id.tv_lxr);
                            mTvTel = contentView.findViewById(R.id.tv_tel);
                            mTvPhone = contentView.findViewById(R.id.tv_phone);
                            mTvAddress = contentView.findViewById(R.id.tv_address);
                            mTvCallDate = contentView.findViewById(R.id.tv_call_date);
                            mTvMember = contentView.findViewById(R.id.tv_member_name);
                        }
                        mTvKhNm.setText(bean.getKhNm());
                        mTvLxr.setText(bean.getLinkman());
                        mTvTel.setText(bean.getTel());
                        mTvPhone.setText(bean.getMobile());
                        mTvAddress.setText(bean.getAddress());
                        mTvCallDate.setText(bean.getScbfDate());
                        mTvMember.setText(bean.getMemberNm());

                        InfoWindow infoWindow = new InfoWindow(contentView, latLng, -50);
                        mBaiduMap.showInfoWindow(infoWindow);
                        break;
                    }
                }
            }
        }
    }


}
