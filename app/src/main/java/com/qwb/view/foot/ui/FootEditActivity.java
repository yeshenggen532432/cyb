package com.qwb.view.foot.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.deadline.statebutton.StateButton;
import com.ilike.voicerecorder.widget.VoiceRecorderView;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.foot.parsent.PFootEdit;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.OtherUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PicAdapter;
import com.qwb.db.LocationBean;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.recordvoice.MyRecordVoiceDialog;
import com.qwb.widget.recordvoice.VoiceManager;
import com.chiyong.t3.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 我的足迹
 */
public class FootEditActivity extends XActivity<PFootEdit>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_flow_foot_edit;
    }

    @Override
    public PFootEdit newP() {
        return new PFootEdit();
    }

    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
    }

    private String mPicUrl;
    public void initIntent(){
        Intent intent = getIntent();
        if(intent != null){
            String picUrl = intent.getStringExtra("pic_url");
            if(!MyStringUtil.isEmpty(picUrl)){
                mPicList.add(picUrl);
                mPicUrl = picUrl;
            }
        }
    }

    private void initUI() {
        initHead();
        initLocation();
        initVoice();
        initAdapter();
        initVoiceRecorder();
        initOther();
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mtvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvHeadTitle.setText("我的足迹");
        mtvHeadRight.setText("上传");
        mHeadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFlow();
            }
        });
    }

    // 定位
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.iv_refresh)
    ImageView mIvRefresh;
    @BindView(R.id.pb_refresh)
    ProgressBar mPbRefresh;
    private String mLongitude, mLatitude, mAddress, mLocationFrom;
    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        mLongitude = String.valueOf(bdLocation.getLongitude());
                        mLatitude = String.valueOf(bdLocation.getLatitude());
                        mAddress = String.valueOf(bdLocation.getAddrStr());
                        mIvRefresh.setVisibility(View.VISIBLE);
                        mPbRefresh.setVisibility(View.GONE);
                        mTvAddress.setText(mAddress);
                        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                            mLocationFrom = "gps   " + MyUtils.getAppVersion();
                        }
                        if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                            mLocationFrom = "wifi   " + MyUtils.getAppVersion();
                        }
                        if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                            mLocationFrom = "lx   " + MyUtils.getAppVersion();
                        }
                    }

                    @Override
                    public void setAddressListener(String addressStr) {

                    }

                    @Override
                    public void setErrorListener() {
                        mIvRefresh.setVisibility(View.VISIBLE);
                        mPbRefresh.setVisibility(View.GONE);
                        mTvAddress.setText("定位失败");
                    }
                });

    }

    @OnClick({R.id.tv_address, R.id.layout_refresh})
    public void onClickLocation(View view){
        switch (view.getId()){
            case R.id.tv_address:
                ActivityManager.getInstance().jumpActivityNavMap(context, mLatitude,mLongitude, mAddress);
                break;
                //位置刷新
            case R.id.layout_refresh:
                mLongitude = "";
                mLatitude = "";
                mTvAddress.setText("定位中...");
                mIvRefresh.setVisibility(View.GONE);
                mPbRefresh.setVisibility(View.VISIBLE);
                initLocation();
                break;
        }
    }

    @BindView(R.id.layout_voice)
    View mLayoutVoice;
    @BindView(R.id.btn_voice)
    StateButton mBtnVoice;
    @BindView(R.id.layout_play_voice)
    View mLayoutPlayVoice;
    @BindView(R.id.iv_voice)
    ImageView mIvVoice;
    @BindView(R.id.tv_voice_time)
    TextView mTvVoiceTime;
    @BindView(R.id.layout_del_voice)
    View mLayoutDelVoice;
    private AnimationDrawable voiceAnimation;
    private VoiceManager voiceManager;
    private void initVoice() {
        mBtnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRecord();
            }
        });
        //播放语音
        mLayoutPlayVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPlayVoice();
            }
        });
        //删除语音
        mLayoutDelVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mVoiceFile = null;
                mVoiceTime = 0;
                mVoiceFilePath = null;
                mVoiceFile = null;
                mLayoutVoice.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 录音
     */
    protected VoiceRecorderView voiceRecorderView;
    private void initVoiceRecorder() {
        voiceRecorderView = findViewById(R.id.voice_recorder);
        voiceRecorderView.setVoiceDirPath(Constans.DIR_IMAGE);
        mBtnVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return voiceRecorderView.onPressToSpeakBtnTouch(view, motionEvent, new VoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        mVoiceFilePath = voiceFilePath;
                        mVoiceFile = new File(voiceFilePath);
                        mVoiceTime = voiceTimeLength;
                        mTvVoiceTime.setText(voiceTimeLength + "s''");
                        mLayoutVoice.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @BindView(R.id.et_bz)
    EditText mEtBz;
    private void initOther() {

    }



    //播放语音
   private void  doPlayVoice(){
       try{
           if(null == voiceAnimation){
               voiceAnimation = (AnimationDrawable) mIvVoice.getBackground();
           }
           if(null == voiceManager){
               voiceManager =  VoiceManager.getInstance(context);
           }
           if (voiceManager.isPlaying()) {
               voiceManager.stopPlay();
               voiceAnimation.stop();
               voiceAnimation.selectDrawable(0);
           }
           voiceManager.setVoicePlayListener(new VoiceManager.VoicePlayCallBack() {
               @Override
               public void voiceTotalLength(long time, String strTime) {
               }

               @Override
               public void playDoing(long time, String strTime) {
               }

               @Override
               public void playPause() {
               }

               @Override
               public void playStart() {
               }

               @Override
               public void playFinish() {
                   if (voiceAnimation != null) {
                       voiceAnimation.stop();
                       voiceAnimation.selectDrawable(0);
                   }
               }
           });

           if(!MyStringUtil.isEmpty(mVoiceFilePath)){
               voiceAnimation.start();
               voiceManager.startPlay(mVoiceFilePath);
           }
       }catch (Exception e){
       }
    }

    //适配器-图片
    @BindView(R.id.iv_add_pic)
    ImageView mIvAddPic;
    @BindView(R.id.iv_del_pic)
    ImageView mIvDelPic;
    @BindView(R.id.recyclerview_pic)
    RecyclerView mRecyclerViewPic;
    private PicAdapter mPicAdapter;
    private void initAdapter() {
        mRecyclerViewPic.setHasFixedSize(true);
        mRecyclerViewPic.setLayoutManager(new GridLayoutManager(context,3));
        mPicAdapter = new PicAdapter(context);
        mPicAdapter.openLoadAnimation();
        mRecyclerViewPic.setAdapter(mPicAdapter);
        if(!MyStringUtil.isEmpty(mPicUrl)){//上个界面有传图片
            List<String> picList = new ArrayList<>();
            picList.add("file://"+ mPicUrl);
            refreshAdapter(false, picList, null);
        }
        mPicAdapter.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
            @Override
            public void onDeletePicListener(int position) {
                mPicList.remove(position);
                refreshAdapter(mPicAdapter.isDelete(),null, position);
            }
        });
        //添加图片
        mIvAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPicAdapter.getData().size() >= 6) {
                    ToastUtils.showCustomToast("最多只能选6张图片！");
                    return;
                }
                doCamera();
            }
        });
        //删除图片
        mIvDelPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshAdapter(!mPicAdapter.isDelete(),null, null );
            }
        });
    }

    /**
     * 刷新适配器（isDelete：是否删除状态，movePosition：删除的下标）
     */
    private List<String> mPicList = new ArrayList<>();//路径没有拼接file://：用来上传的
    private void refreshAdapter(boolean isDelete, List<String> picList, Integer movePosition) {
        List<String> dataList = mPicAdapter.getData();
        mPicAdapter.setDelete(isDelete);
        if(null != picList){
            if(MyCollectionUtil.isEmpty(dataList)){
                mPicAdapter.setNewData(picList);
            }else{
                mPicAdapter.addData(picList);
            }
        }
        if(null != movePosition){
            mPicAdapter.remove(movePosition);
        }
        mPicAdapter.notifyDataSetChanged();
        List<String> dataList2 = mPicAdapter.getData();
        if (MyCollectionUtil.isEmpty(dataList2)) {
            mIvDelPic.setVisibility(View.GONE);
        } else {
            mIvDelPic.setVisibility(View.VISIBLE);// 删除图标可见或不可见
        }
    }

    // 相机直接拍摄
    public void doCamera() {
        new RxPermissions(this)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//TODO 许可
                            Intent intent = new Intent(context, ImageGridActivity.class);
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                            startActivityForResult(intent, Constans.TAKE_PIC_XJ);
                        } else {
                            //TODO 未许可
                            ToastUtils.showCustomToast("拍照权限和存储权限未开启，请在手机设置中打开权限！");
                        }
                    }
                });
    }

    //考勤语音目录+当前时间戳+“.amr”:作为语音文件路径
    String mVoiceFilePath = null;
    MyRecordVoiceDialog dialog = null;
    private File mVoiceFile = null;
    private long mVoiceTime;
    public void doRecord(){
        dialog = new MyRecordVoiceDialog(context);
        dialog.setEnrecordVoiceListener(new MyRecordVoiceDialog.EnRecordVoiceListener() {
            @Override
            public void onFinishRecord(long length, String strLength, String filePath) {
                mVoiceFilePath = filePath;
                mVoiceFile = new File(filePath);
                mVoiceTime = length;
                dialog = null;
                mTvVoiceTime.setText(length + "s''");
                mLayoutVoice.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCloseRecord() {
                dialog = null;
            }
        });
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null){
                List<String> picList = new ArrayList<>();
                for (ImageItem imageItem:images) {
                    mPicList.add(imageItem.path);//上传图片的
                    picList.add("file://"+imageItem.path);//图片显示
                    Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
                }
                refreshAdapter(false, picList, null );
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //如果有录音要暂停
        if(null != dialog){
            dialog.pauseVoiceRecord();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtils.resetStepStatus(context);//重置默认状态,删除图片
    }


    /**
     * 签到提交请求
     */
    private void submitFlow() {
        String bz = mEtBz.getText().toString().trim();// 备注
        getP().addFlow(context, mLongitude, mLatitude, mAddress, bz, mVoiceFile, mVoiceTime, mPicList);
    }


    /**
     * 上下班添加轨迹点 1:上班，2：下班，3：拜访签到，4：拜访签退
     */
    public void sendLocation(int work_status) {
        List<LocationBean> list = new ArrayList<>();
        LocationBean mLocationBean = new LocationBean();
        mLocationBean.setLatitude(Double.valueOf(mLatitude));
        mLocationBean.setLongitude(Double.valueOf(mLongitude));
        mLocationBean.setAddress(mAddress);
        mLocationBean.setLocation_time(Long.valueOf(System.currentTimeMillis() / 1000));
        mLocationBean.setLocation_from(mLocationFrom);
        mLocationBean.setOs(Build.MODEL + "   " + Build.VERSION.RELEASE);
        mLocationBean.setWork_status(work_status);
        list.add(mLocationBean);
        // 作用：同一个时间连线，不同时间断开连线
        if (1 == work_status) {
            SPUtils.setValues("check_in_time", String.valueOf(System.currentTimeMillis() / 1000));
        }
        String location = JSON.toJSONString(list);
        MyMapUtil.getInstance().sendData(location);
    }




}
