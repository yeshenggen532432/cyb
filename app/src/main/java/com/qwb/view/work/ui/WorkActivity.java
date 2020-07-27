package com.qwb.view.work.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.qwb.widget.MyMenuPopup;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyPicUtil;
import com.qwb.view.longconnection.GpsUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.longconnection.TraceServiceImpl;
import com.qwb.view.work.parsent.PWork;
import com.qwb.view.mine.model.AddressUploadBean;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

import java.util.ArrayList;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 考勤:上下班
 */
public class WorkActivity extends XActivity<PWork> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_work;
    }

    @Override
    public PWork newP() {
        return new PWork();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        initPopup();
        getP().queryAddressUpload();//位置上传方式
    }

    /**
     * 上下班
     */
    public void addData() {
        String remark = mEtRemark.getText().toString().trim();
        getP().beginCheckIn(context, upload, memUpload, remark, longitude, latitude, address, upToDown);
    }

    @BindView(R.id.parent)
    View parent;

    private void initUI() {
        initHead();
        initBottom();
        initOther();
    }

    /**
     * 初始化头部
     */
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        TextView tv_headTitle = findViewById(R.id.tv_head_title);
        TextView tv_headRight = findViewById(R.id.tv_head_right);
        ImageView img_head_right = findViewById(R.id.img_head_right);
        tv_headTitle.setText("考勤");
        tv_headRight.setText("列表");
        img_head_right.setImageResource(R.drawable.icon_more_white);
        tv_headRight.setVisibility(View.GONE);
        img_head_right.setVisibility(View.VISIBLE);
        findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        findViewById(R.id.img_head_right).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuPopup.showAsDropDown(findViewById(R.id.img_head_right), 0, -20);
            }
        });
    }

    /**
     * 定位；备注
     */
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    private View img_refresh, pb_refresh;
    private TextView mTvAddress;
    private String longitude, latitude, address;

    private void initOther() {
        mTvAddress = findViewById(R.id.ckin_tv_showloctinfo);
        img_refresh = findViewById(R.id.ckin_bnt_refadr);// 重新定位
        pb_refresh = findViewById(R.id.pb_refresh);// 进度条
        mTvAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MyStringUtil.isEmpty(latitude) && !MyStringUtil.isEmpty(longitude)) {
                    ActivityManager.getInstance().jumpActivityNavMap(context, longitude, latitude, address);
                }
            }
        });
        img_refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                longitude = "";
                latitude = "";
                img_refresh.setVisibility(View.INVISIBLE);
                pb_refresh.setVisibility(View.VISIBLE);
                mTvAddress.setText("定位中...");
                initLocation();
            }
        });
    }

    /**
     * 底部：上下班按钮
     */
    private String upToDown;// "1-1"上班；"1-2"下班
    private Button bt_up, bt_down;//上班，下班

    private void initBottom() {
        bt_up = findViewById(R.id.bt_up);
        bt_down = findViewById(R.id.bt_down);
        String workState = SPUtils.getSValues(ConstantUtils.Sp.WORK_STATE);
        if ("1".equals(workState)) {
            bt_up.setTextColor(Color.parseColor("#666666"));
        } else if ("2".equals(workState)) {
            bt_down.setTextColor(Color.parseColor("#666666"));
        }
        findViewById(R.id.bt_up).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doClickWork(1);
            }
        });
        findViewById(R.id.bt_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doClickWork(2);
            }
        });
    }

    /**
     * 处理点击上下班按钮
     */
    public void doClickWork(int type) {
        if (MyStringUtil.isEmpty(latitude) || MyStringUtil.isEmpty(longitude)) {
            ToastUtils.showCustomToast("获取位置失败");
            return;
        }
        if (1 == type) {
            upToDown = "1-1";
        } else {
            upToDown = "1-2";
        }
        String workState = SPUtils.getSValues(ConstantUtils.Sp.WORK_STATE);
        if (String.valueOf(type).equals(workState)) {
            showDialogRepeatWork();
        } else {
            showActionSheetDialogNoTitle();
        }
    }

    /**
     * 是否打开gps
     */
    private void doOpenGPS() {
        boolean oPen = GpsUtil.isOPen(getApplicationContext());
        if (oPen) {
            initLocation();
        } else {
            NormalDialog dialog = new NormalDialog(context);
            dialog.content("请打开GPS,如果不打开GPS会影响轨迹线路！").show();
            dialog.setOnBtnClickL(null, new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                }
            });
        }
    }

    /**
     * 初始化百度定位
     */
    private String location_from;

    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        longitude = "" + bdLocation.getLongitude();
                        latitude = "" + bdLocation.getLatitude();
                        address = "" + bdLocation.getAddrStr();
                        img_refresh.setVisibility(View.VISIBLE);
                        pb_refresh.setVisibility(View.INVISIBLE);
                        mTvAddress.setText(bdLocation.getAddrStr());// 地址
                        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                            location_from = "gps   " + MyUtils.getAppVersion();
                        }
                        if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                            location_from = "wifi   " + MyUtils.getAppVersion();
                        }
                        if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                            location_from = "lx   " + MyUtils.getAppVersion();
                        }
                    }

                    @Override
                    public void setErrorListener() {
                        img_refresh.setVisibility(View.VISIBLE);
                        pb_refresh.setVisibility(View.INVISIBLE);
                        mTvAddress.setText("定位失败");
                    }

                    @Override
                    public void setAddressListener(String addressStr) {
                        address = addressStr;
                        mTvAddress.setText(address);
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        doOpenGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtils.resetStepStatus(WorkActivity.this);//重置默认状态,删除图片
    }

    /**
     * 对话框：拍照签到，直接签到
     */
    private String[] items = {"拍照签到", "直接签到"};

    private void showActionSheetDialogNoTitle() {
        final ActionSheetDialog dialog = new ActionSheetDialog(context, items, parent);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (0 == position) {
                    MyPicUtil.getInstance().fromCamera(context, selImageList.size());
                } else {
                    addData();
                }
            }
        });
    }

    private ArrayList<ImageItem> selImageList = new ArrayList<>(); //当前选择的所有图片

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择器-添加图片返回
        if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                selImageList.addAll(images);
                Constans.publish_pics.clear();
                for (int i = 0; i < selImageList.size(); i++) {
                    Constans.publish_pics.add(selImageList.get(i).path);
                }
                //相机-记录拍照的集合（要删除拍照图片）
                if (requestCode == Constans.TAKE_PIC_XJ) {
                    for (int j = 0; j < images.size(); j++) {
                        Constans.publish_pics_xj.add(images.get(j).path);
                    }
                }
                addData();
            }
        }
    }

    /**
     * 窗体
     */
//    EasyPopup mEasyPop;
    private MyMenuPopup mMenuPopup;

    private void initPopup() {
        String[] items = {"考勤列表", "班次设置"};
        mMenuPopup = new MyMenuPopup(context, items);
        mMenuPopup.createPopup();
        mMenuPopup.setOnItemClickListener(new MyMenuPopup.OnItemClickListener() {
            @Override
            public void setOnItemClickListener(String text, int position) {
                switch (position) {
                    case 0:
                        ActivityManager.getInstance().jumpActivity(context, WorkListActivity.class);
                        break;
                    case 1:
                        ActivityManager.getInstance().jumpActivity(context, WorkClassListActivity.class);
                        break;
                }
            }
        });
    }

    /**
     * 备注：上传位置的逻辑
     * 1）后台当设置不上传时，业务员自己设置不起作用；
     * 2）后台当设置上传时，业务员自己可以设置上传还是不上传
     */
    public void startOrStopTrace(int type) {
        if (1 == type) {
            MyMapUtil.getInstance().sendLocation(1, latitude, longitude, address, location_from);
            if (upload == 2) {
                //上传的:先关闭再上传
                SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS, 1);
            }else if (upload == 1 && (memUpload == null || memUpload == 1)) {
                //上传的:先关闭再上传
                SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS, 1);
            } else {
                //不上传的（有可能之前已经打开上传了，所以要关闭）
                SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS, 0);
            }
        } else {
            MyMapUtil.getInstance().sendLocation(2, latitude, longitude, address, location_from);
            TraceServiceImpl.stopService();//关闭上传
        }
    }

    /**
     * dialog:重复上下班
     */
    private void showDialogRepeatWork() {
        String content = "";
        if ("1-1".equals(upToDown)) {
            content = "您已上班签到过，是否继续上班签到？";
        } else if ("1-2".equals(upToDown)) {
            content = "您已下班签退过，是否继续下班签退？";
        }
        final NormalDialog dialog = new NormalDialog(context);
        dialog.setCanceledOnTouchOutside(false);//外部点击不消失
        dialog.content(content)
                .btnText("取消", "继续")
                .show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                if ("1-1".equals(upToDown)) {
                    showActionSheetDialogNoTitle();
                } else if ("1-2".equals(upToDown)) {
                    showActionSheetDialogNoTitle();
                }
            }
        });
    }

    /**
     * 备注：上传位置的逻辑
     * 1）后台当设置不上传时，业务员自己设置不起作用；
     * 2）后台当设置上传时，业务员自己可以设置上传还是不上传
     */
    int upload;//上传方式：0不上传，1.手机控制，2上传
    Integer memUpload;//业务员自己修改上传方式：0不上传，1上传（默认）
    public void doAddressUpload(AddressUploadBean data) {
        upload = data.getUpload();
        memUpload = data.getMemUpload();
        if(upload == 2){
            //上传的
            SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS, 1);
        }else if (upload == 1 && (memUpload == null || memUpload == 1)) {
            //上传的
            SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS, 1);
        } else {
            //不上传的
            SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_STATUS, 0);
        }
        SPUtils.setintValues(ConstantUtils.Sp.TRACK_UPLOAD_MIN, data.getMin());
    }


}
