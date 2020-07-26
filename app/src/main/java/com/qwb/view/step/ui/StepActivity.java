package com.qwb.view.step.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.qwb.common.ToStepEnum;
import com.qwb.utils.MyCallUtil;
import com.qwb.utils.MyMenuUtil;
import com.qwb.utils.MyStepDateUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.customer.model.CustomerBean;
import com.qwb.event.MineClientEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.step.parsent.PStep;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyMapUtil;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.view.customer.model.CustomerBfBean;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 拜访6步骤
 */
public class StepActivity extends XActivity<PStep>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_step;
    }

    @Override
    public PStep newP() {
        return new PStep();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        MyStepDateUtil.initDate(StepActivity.this);//获取上月；本月的销售量的日期
        initIntent();
        initUI();
        doIntent();
        createPopupByAddress();
        initLocation();
        getP().queryCustomerBf(context, type, cid, mDate, clientName);//获取客户拜访信息：已上传和未上传信息
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        //修改客户
        BusProvider.getBus().toFlowable(MineClientEvent.class)
                .subscribe(new Consumer<MineClientEvent>() {
                    @Override
                    public void accept(MineClientEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_MINE_CLIENT) {
                            CustomerBean customer = event.getCustomerBean();
                            if (customer != null){
                                mLocationTag = 1;
                                clientName = customer.getKhNm();
                                address = customer.getAddress();
                                tel = customer.getTel();
                                mobile = customer.getMobile();
                                linkman = customer.getLinkman();
                                longitude = customer.getLongitude();
                                latitude = customer.getLatitude();
                                showCustomerUI();
                            }
                        }
                    }
                });
    }

    /**
     * 初始化Intent
     */
    private int type;//1:周边客户；2：我的客户；3：计划拜访
    private int cid;// 客户id
    private String clientName;// 客户名称
    private String address;// 客户地址
    private String tel;//// 手机
    private String mobile;// 固定电话
    private String linkman;// 联系人
    private String longitude;
    private String latitude;
    private Integer mLocationTag = 1;//0为初始化
    private String mDate;// 计划拜访的补拜访日期
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(ConstantUtils.Intent.TYPE, ToStepEnum.STEP_MINE_CUSTOMER.getType());
            cid = intent.getIntExtra(ConstantUtils.Intent.CLIENT_ID, 0);
            mLocationTag = intent.getIntExtra(ConstantUtils.Intent.TAG, 1);
            clientName = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
            address = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            tel = intent.getStringExtra(ConstantUtils.Intent.TEL);
            mobile = intent.getStringExtra(ConstantUtils.Intent.MOBILE);
            linkman = intent.getStringExtra(ConstantUtils.Intent.LINKMAN);
            longitude = intent.getStringExtra(ConstantUtils.Intent.LONGITUDE);
            latitude = intent.getStringExtra(ConstantUtils.Intent.LATITUDE);
            mDate = intent.getStringExtra(ConstantUtils.Intent.DATE);
        }
    }

    private void doIntent() {
        showCustomerUI();
        tv_shangFirst.setText(shangFirst + " 至 " + shangLast);
        tv_benFirst.setText(benFirst + " 至 " + benLast);
    }

    @BindView(R.id.parent)
    View mParent;
    private void initUI() {
        initHead();
        initOtherUI();
    }

    /**
     * 初始化UI
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
        mTvHeadRight.setText("修改");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().jumpToAddClientActivity(context, 1, String.valueOf(cid));
            }
        });
    }

    /**
     * 初始化UI
     */
    public String shangFirst;
    public String shangLast;
    public String benFirst;
    public String benLast;
    private TextView tv_shangFirst;
    private TextView tv_shangJine;
    private TextView tv_benFirst;
    private TextView tv_benJine;
    //客户信息
    private TextView mTvLinkman;
    private TextView mTvPhone;
    private TextView tv_qddate;
    private TextView tv_bcbfzj;
    private TextView tv_dbsx;
    private TextView tv_xxd;
    //拜访信息
    private TextView tv_count1;
    private TextView tv_count2;
    private TextView tv_count3;
    private TextView tv_count4;
    private TextView tv_count5;
    private TextView tv_count6;
    private void initOtherUI() {
        mTvLinkman = findViewById(R.id.tv_linkman);
        mTvPhone = findViewById(R.id.tv_phone);
        tv_qddate = findViewById(R.id.tv_qddate);
        tv_bcbfzj = findViewById(R.id.tv_bcbfzj);
        tv_dbsx = findViewById(R.id.tv_dbsx);
        tv_xxd = findViewById(R.id.tv_xxd);// 新鲜度（临期，正常）

        tv_count1 = findViewById(R.id.tv_count1);
        tv_count2 = findViewById(R.id.tv_count2);
        tv_count3 = findViewById(R.id.tv_count3);
        tv_count4 = findViewById(R.id.tv_count4);
        tv_count5 = findViewById(R.id.tv_count5);
        tv_count6 = findViewById(R.id.tv_count6);

        tv_shangFirst = findViewById(R.id.tv_shangFirst);
        tv_shangJine = findViewById(R.id.tv_shangJine);
        tv_benFirst = findViewById(R.id.tv_benFirst);
        tv_benJine = findViewById(R.id.tv_benJine);
    }

    @OnClick({R.id.tv_phone, R.id.tv_shangFirst, R.id.tv_benFirst, R.id.ll_1, R.id.ll_2, R.id.ll_3, R.id.ll_4, R.id.ll_5, R.id.ll_6,  R.id.ll_9, R.id.ll_10, R.id.ll_11})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:// 打电话
                MyCallUtil.call(context, mTvPhone.getText().toString().trim());
                break;
            case R.id.tv_shangFirst:// 上月第一天
                showDialogDoubleDate(1);
                break;
            case R.id.tv_benFirst:// 本月第一天
                showDialogDoubleDate(2);
                break;
            case R.id.ll_1:// 1.拜访签到拍照
                jumpToStep(Step1Activity.class,mCurrentBean.getCount1(),ConstantUtils.Intent.REQUEST_STEP_ONE);
                break;
            case R.id.ll_2:// 2.生活化检查
                jumpToStep(Step2Activity.class,mCurrentBean.getCount2(),ConstantUtils.Intent.REQUEST_STEP_TWO);
                break;
            case R.id.ll_3:// 3.陈列检查采集
                jumpToStep(Step3Activity.class,mCurrentBean.getCount3(),ConstantUtils.Intent.REQUEST_STEP_THREE);
                break;
            case R.id.ll_4:// 4.销售小结
                jumpToStep(Step4Activity.class,mCurrentBean.getCount4(),ConstantUtils.Intent.REQUEST_STEP_FOUR);
                break;
            case R.id.ll_5:// 5.订货下单
                jumpToStep(Step5Activity.class,mCurrentBean.getCount5(),ConstantUtils.Intent.REQUEST_STEP_FIVE);
                break;
            case R.id.ll_6:// 6.道谢并告知下次拜访日期
                jumpToStep(Step6Activity.class,mCurrentBean.getCount6(),ConstantUtils.Intent.REQUEST_STEP_SIX);
                break;
            case R.id.ll_9:// 9.拜访查询
                ActivityManager.getInstance().jumpToCallQueryActivity(context, "1", String.valueOf(cid));
                break;
            case R.id.ll_10://上月销售量
                ActivityManager.getInstance().jumpToCallQueryActivity(context, 2, clientName, String.valueOf(cid), shangFirst, shangLast);
                break;
            case R.id.ll_11://本月销售量
                ActivityManager.getInstance().jumpToCallQueryActivity(context, 2, clientName, String.valueOf(cid), benFirst, benLast);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int arg1, Intent data) {
        super.onActivityResult(requestCode, arg1, data);
        if (data != null) {
            boolean state = data.getBooleanExtra(ConstantUtils.Intent.SUCCESS, false);
            int count = data.getIntExtra(ConstantUtils.Intent.COUNT, 1);
            if (state) {
                if (requestCode == ConstantUtils.Intent.REQUEST_STEP_ONE) {
                    setTextLoad(count, tv_count1, 1);
                } else if (requestCode == ConstantUtils.Intent.REQUEST_STEP_TWO) {
                    setTextLoad(count, tv_count2, 2);
                } else if (requestCode == ConstantUtils.Intent.REQUEST_STEP_THREE) {
                    setTextLoad(count, tv_count3, 3);
                } else if (requestCode == ConstantUtils.Intent.REQUEST_STEP_FOUR) {
                    setTextLoad(count, tv_count4, 4);
                } else if (requestCode == ConstantUtils.Intent.REQUEST_STEP_FIVE) {
                    setTextLoad(count, tv_count5, 5);
                } else if (requestCode == ConstantUtils.Intent.REQUEST_STEP_SIX) {
                    setTextLoad(count, tv_count6, 6);
                }
            }
        }
    }






    //TODO**************************************************************************************************************************
    //TODO**************************************************************************************************************************
    /**
     * 弹出修改客户信息
     * 1.客户没有经纬度
     * 2.初始化导入的客户
     * 3.经纬度超过1000米
     */
    private EasyPopup mPopup;
    private LinearLayout mPViewOld;
    private RadioButton mPRbOld;
    private RadioButton mPRbNew;
    private EditText mPEtOldAddress;
    private EditText mPEtNewAddress;
    private TextView mPTvContent;
    public void createPopupByAddress() {
        mPopup = new EasyPopup(this)
                .setContentView(R.layout.x_popup_step_no_address)
                .createPopup();
        mPViewOld = mPopup.getView(R.id.ll_old);
        mPRbOld = mPopup.getView(R.id.rb_old);
        mPRbNew = mPopup.getView(R.id.rb_new);
        mPEtOldAddress = mPopup.getView(R.id.edit_oldaddress);
        mPEtNewAddress = mPopup.getView(R.id.edit_address);
        mPTvContent = mPopup.getView(R.id.tv_content);
        mPopup.getView(R.id.rb_old).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPRbNew.setChecked(false);
            }
        });
        mPopup.getView(R.id.rb_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPRbOld.setChecked(false);
            }
        });
        mPopup.getView(R.id.layout_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopup.dismiss();
            }
        });
        mPopup.getView(R.id.layout_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPRbNew.isChecked()) {
                    address = mPEtNewAddress.getText().toString().trim();
                } else {
                    address = mPEtOldAddress.getText().toString().trim();
                }
                if (MyStringUtil.isEmpty(address)) {
                    ToastUtils.showCustomToast("请输入客户地址");
                    return;
                }
                getP().updateCustomerByAddress(context, cid, address, addLongitude, addLatitude, addProvince, addCity, addArea);
                mPopup.dismiss();
            }
        });
    }

    /**
     * 初始化定位
     */
    private String addAddress;// 要添加客户地址
    private String addLongitude;// 要添加客户地址
    private String addLatitude;// 要添加客户地址
    private String addProvince;// 要添加客户地址
    private String addCity;// 要添加客户地址
    private String addArea;// 要添加客户地址
    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        addAddress = bdLocation.getAddrStr();// 地址
                        addLongitude = String.valueOf(bdLocation.getLongitude());
                        addLatitude = String.valueOf(bdLocation.getLatitude());
                        addProvince = String.valueOf(bdLocation.getProvince());
                        addCity = String.valueOf(bdLocation.getCity());
                        addArea = String.valueOf(bdLocation.getDistrict());

                        //周边客户；我的客户：没有经纬度-弹框提醒修改客户信息
                        if (type == ToStepEnum.STEP_MINE_CUSTOMER.getType() || type == ToStepEnum.STEP_NEAR_CUSTOMER.getType()) {
                            if (MyStringUtil.isEmpty(latitude) || MyStringUtil.isEmpty(longitude) || MyStringUtil.eq("0", latitude) || MyStringUtil.eq("0", longitude)) {
                                showPopupAddress("该客户没有经纬度，是否使用当前定位经纬度");
                            }else{
                                if (0 == mLocationTag){
                                    showPopupAddress("该客户为初始化导入客户，是否纠正该客户的经纬度为当前定位的经纬度");
                                }else{
                                    LatLng latLng1 = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                                    LatLng latLng2 = new LatLng(Double.valueOf(addLatitude), Double.valueOf(addLongitude));
                                    if (DistanceUtil.getDistance(latLng1, latLng2) > 1000){
                                        showPopupAddress("该客户的经纬度与当前定位的经纬度超过1公里，是否纠正该客户的经纬度为当前定位的经纬度");
                                    }
                                }
                            }
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
     * 显示修改客户：地址；经纬度
     */
    public void showPopupAddress(String content){
        if (MyStringUtil.isEmpty(address)) {
            mPViewOld.setVisibility(View.GONE);
            mPRbOld.setChecked(false);
            mPRbNew.setChecked(true);
        } else {
            mPViewOld.setVisibility(View.VISIBLE);
            mPRbOld.setChecked(true);
            mPRbNew.setChecked(false);
            mPEtOldAddress.setText(address);
        }
        mPTvContent.setText(content);
        mPEtNewAddress.setText(addAddress);
        mPopup.showAtLocation(mParent, Gravity.CENTER, 0, 0);
    }


    /**
     * 选择时间：上月销售量；本月销售量
     */
    private void showDialogDoubleDate(final int timeType) {
        String startDate = null, endDate = null;
        switch (timeType) {
            case 1:
                startDate = shangFirst;
                endDate = shangLast;
                break;
            case 2:
                startDate = benFirst;
                endDate = benLast;
                break;
        }

        new MyDoubleDatePickerDialog(context, "筛选时间", startDate, endDate, new MyDoubleDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                switch (timeType) {
                    case 1:
                        shangFirst = startDate;
                        shangLast = endDate;
                        tv_shangFirst.setText(startDate + " 至 " + endDate);
                        getP().queryDataMoney(context, startDate, endDate, 1);
                        break;
                    case 2:
                        benFirst = startDate;
                        benLast = endDate;
                        tv_benFirst.setText(startDate + " 至 " + endDate);
                        getP().queryDataMoney(context, startDate, endDate, 2);
                        break;
                }
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    /**
     * 上月，本月的销售金额
     */
    public void doUIMoney(double money, int tag) {
        switch (tag) {
            case 1://上月
                tv_shangJine.setText(String.valueOf(money));
                break;
            case 2://本月
                tv_benJine.setText(String.valueOf(money));
                break;
        }
    }


    //TODO***********************************************************************************************************************************
    //TODO***********************************************************************************************************************************
    /**
     * 修改客户信息
     */
    public void doUpdateSuccess() {
        address = addAddress;
        latitude = addLatitude;
        longitude = addLongitude;
        BusProvider.getBus().post(new MineClientEvent());
    }

    /**
     * 显示客户拜访信息
     */
    private CustomerBfBean mCurrentBean = new CustomerBfBean();
    public void doUICustomerBf(CustomerBfBean data) {
        if (null == data) {
            return;
        }
        mCurrentBean = data;

        //显示：上次拜访；拜访总结；代办事项；新鲜度
        if (MyStringUtil.isNotEmpty(data.getQddate())) {
            tv_qddate.setText(data.getQddate());
        }
        if (MyStringUtil.isNotEmpty(data.getBcbfzj())) {
            tv_bcbfzj.setText(data.getQddate());
        }
        if (MyStringUtil.isNotEmpty(data.getDbsx())) {
            tv_dbsx.setText(data.getDbsx());
        }
        if (MyStringUtil.isNotEmpty(data.getXxzt())) {
            tv_xxd.setText("新鲜度:" + data.getXxzt());
        } else {
            tv_xxd.setText("");
        }
        setTextLoad(data.getCount1(), tv_count1, 1);
        setTextLoad(data.getCount2(), tv_count2, 2);
        setTextLoad(data.getCount3(), tv_count3, 3);
        setTextLoad(data.getCount4(), tv_count4, 4);
        setTextLoad(data.getCount5(), tv_count5, 5);
        setTextLoad(data.getCount6(), tv_count6, 6);
    }

    //六步骤：上传情况
    public void setTextLoad(int count, TextView tvCount, int step){
        if (MyStringUtil.eq("2", String.valueOf(count))) {
            tvCount.setText("已缓存");
        } else if (MyStringUtil.eq("1", String.valueOf(count))) {
            tvCount.setText("已上传");
        }else {
            tvCount.setText("未上传");
            count = 0;
        }
        if (1 == step){
            mCurrentBean.setCount1(count);
        }else if(2 == step){
            mCurrentBean.setCount2(count);
        }else if(3 == step){
            mCurrentBean.setCount3(count);
        }else if(4 == step){
            mCurrentBean.setCount4(count);
        }else if(5 == step){
            mCurrentBean.setCount5(count);
        }else if(6 == step){
            mCurrentBean.setCount6(count);
        }
    }

    /**
     * 设置客户名称，手机/电话，联系人
     */
    private void showCustomerUI() {
        mTvHeadTitle.setText(clientName);
        if (MyStringUtil.isNotEmpty(tel)) {
            mTvPhone.setText(tel);
        }
        if (MyStringUtil.isNotEmpty(mobile)) {
            mTvPhone.setText(mobile);
        }
        if (MyStringUtil.isNotEmpty(linkman)) {
            mTvLinkman.setText(linkman);
        } else {
            mTvLinkman.setText("");
        }
    }

    /**
     * 六步骤：跳转到各自界面
     */
    public void jumpToStep(Class c, Integer count,int requestCode){
        int tempCount = 0;
        if (count != null){
            tempCount = count;
        }
        try {
            MyMenuUtil.hasMenuCustomerBf();
            Router.newIntent(context)
                    .putString(ConstantUtils.Intent.CLIENT_ID, String.valueOf(cid))
                    .putString(ConstantUtils.Intent.CLIENT_NAME, clientName)
                    .putString(ConstantUtils.Intent.ADDRESS, address)
                    .putString(ConstantUtils.Intent.TEL, tel)
                    .putString(ConstantUtils.Intent.MOBILE, mobile)
                    .putString(ConstantUtils.Intent.LINKMAN, linkman)
                    .putString(ConstantUtils.Intent.STEP, "" + tempCount)// 是否上传
                    .putString(ConstantUtils.Intent.SUPPLEMENT_TIME, mDate)// 补拜访时间
                    .requestCode(requestCode)
                    .to(c)
                    .launch();
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }




}
