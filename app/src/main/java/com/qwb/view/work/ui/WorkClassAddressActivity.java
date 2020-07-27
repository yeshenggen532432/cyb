package com.qwb.view.work.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.event.BaseEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.work.parsent.PWorkClassAddress;
import com.chiyong.t3.R;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 班次地址
 */
public class WorkClassAddressActivity extends XActivity<PWorkClassAddress> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_work_class_address;
    }

    @Override
    public PWorkClassAddress newP() {
        return new PWorkClassAddress();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doUI();
    }

    private String latitude;
    private String longitude;
    private String address;
    private String bcId;
    private String areaLong;
    private String outOf;

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            latitude = intent.getStringExtra(ConstantUtils.Intent.LATITUDE);
            longitude = intent.getStringExtra(ConstantUtils.Intent.LONGITUDE);
            address = intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            bcId = intent.getStringExtra(ConstantUtils.Intent.ID);//班次id
            areaLong = intent.getStringExtra(ConstantUtils.Intent.AREA_LONG);//有效范围
            outOf = intent.getStringExtra(ConstantUtils.Intent.TYPE);
        }
    }

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
        mTvHeadTitle.setText("考勤位置");
        mTvHeadRight.setText("提交");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });
    }

    @BindView(R.id.tv_range)
    TextView mTvRange;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.rb_yes)
    RadioButton rb_yes;
    @BindView(R.id.rb_no)
    RadioButton rb_no;
    private void initOther() {

    }

    private void doUI() {
        if (MyStringUtil.isNotEmpty(address)) {
            et_address.setText(address);
        }
        if (MyStringUtil.isNotEmpty(areaLong)) {
            mTvRange.setText("有效范围" + areaLong + "米");
        }
        if ("0".equals(outOf)) {
            rb_no.setChecked(true);
        } else {
            rb_yes.setChecked(true);
        }
    }

    @OnClick({R.id.btn_location, R.id.iv_youxiaohanwei})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_location:
                ActivityManager.getInstance().jumpToLocationMarkActivity(context, latitude, longitude, address, null, null, null);
                break;
            case R.id.iv_youxiaohanwei:
                showDialogRange();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int arg1, Intent data) {
        super.onActivityResult(requestCode, arg1, data);
        if (data != null) {
            // 定位
            if (requestCode == ConstantUtils.Intent.REQUEST_CODE_LOCATION) {
                latitude = data.getStringExtra(ConstantUtils.Intent.LATITUDE);
                longitude = data.getStringExtra(ConstantUtils.Intent.LONGITUDE);
                address = data.getStringExtra(ConstantUtils.Intent.ADDRESS);
                et_address.setText(address);
            }
        }
    }

    /**
     * 选择有效范围
     */
    private void showDialogRange() {
        final String[] items = {"100", "200", "300", "400", "500", "600", "700", "800", "900", "1000"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("tv_name.setText(选择有效范围(单位米));").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = items[position];
                mTvRange.setText("有效范围:" + item + "米");
                areaLong = item;
            }
        });
    }

    //===========================================================================================
    /**
     * 提交数据
     */
    private void addData() {
        if (rb_no.isChecked()) {
            outOf = "0";
        } else {
            outOf = "1";
        }
        String address = et_address.getText().toString().trim();
        getP().updateWorkAddress(context, bcId, latitude, longitude, address, areaLong, outOf);
    }

    /**
     * 修改成功
     */
    public void updateSuccess() {
        BusProvider.getBus().post(new BaseEvent());
        ActivityManager.getInstance().closeActivity(context);
    }


}
