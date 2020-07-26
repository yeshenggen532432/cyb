package com.qwb.view.step.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.MyTimeUtils;
import com.xmsx.cnlife.widget.AudioRecorder;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.step.parsent.PStep6;
import com.qwb.utils.Constans;
import com.qwb.utils.ILUtil;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PicAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep6Bean;
import com.qwb.utils.MyLongConnectionUtil;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.QueryBfgzxcBean;
import com.qwb.utils.MyChooseTimeUtil;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：拜访6：道谢并告知下次拜访日期
 */
public class Step6Activity extends XActivity<PStep6> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_step6;
    }

    @Override
    public PStep6 newP() {
        return new PStep6();
    }

    private ImageLoader imageLoder = ILUtil.getImageLoder();// 加载图片
    private DisplayImageOptions options = ILUtil.getOptionsSquere();

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initMap();
        initUI();
        doIntent();
        getP().queryToken(null);
    }

    /**
     * 初始化Intent
     */
    private String pdateStr;// 补拜访时间(拜访计划)
    private String cid;// 客户ID
    private String mKhNm;
    private String count6;
    private String bfId;//拜访id

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            mKhNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
            count6 = intent.getStringExtra(ConstantUtils.Intent.STEP);
            pdateStr = intent.getStringExtra(ConstantUtils.Intent.SUPPLEMENT_TIME);// 补拜访时间
        }
    }

    /**
     * 处理Intent
     */
    private void doIntent() {
        mTvHeadTitle.setText(mKhNm);
        if ("1".equals(count6)) {// 已上传
            getP().loadDataInfo(context, cid, pdateStr);//上次提交的信息
            mTvHeadRight.setText("修改");
        } else {
            mTvHeadRight.setText("提交");
            initLocation();
        }
        if (MyStringUtil.isEmpty(pdateStr)) {
            tv_callOnDate.setText(MyTimeUtils.getTodayStr());
        } else {
            tv_callOnDate.setText(pdateStr);
        }
    }

    /**
     * 初始化地图
     */
    private void initMap() {
        mIvRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvRefresh.setVisibility(View.GONE);
                mPbRefresh.setVisibility(View.VISIBLE);
                mTvAddress.setText("定位中...");
                initLocation();
            }
        });
    }

    @BindView(R.id.tv_location)
    TextView mTvAddress;
    @BindView(R.id.ckin_bnt_refadr)
    View mIvRefresh;
    @BindView(R.id.pb_refresh)
    View mPbRefresh;
    private String mLocationFrom;
    private String mLongitude;
    private String mLatitude;
    private String mAddressStr;

    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        mIvRefresh.setVisibility(View.VISIBLE);
                        mPbRefresh.setVisibility(View.GONE);
                        mTvAddress.setText(bdLocation.getAddrStr());// 地址

                        mLongitude = "" + bdLocation.getLongitude();
                        mLatitude = "" + bdLocation.getLatitude();
                        mAddressStr = "" + bdLocation.getAddrStr();
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
                        mAddressStr = addressStr;
                        mTvAddress.setText(addressStr);
                    }

                    @Override
                    public void setErrorListener() {
                        mIvRefresh.setVisibility(View.VISIBLE);
                        mPbRefresh.setVisibility(View.GONE);
                        mTvAddress.setText("定位失败");
                    }

                });
    }


    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initBaseView();
        initAdapter();
        initVoiceView();
    }

    /**
     * 头部
     */
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;

    private void initHead() {
        OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                addData();
            }
        });
    }

    //基本UI
    private TextView tv_callOnDate;
    private TextView tv_choiceDate;
    private RadioButton rd1;
    private RadioButton rd2;
    private RadioButton rd3;
    private RadioButton rd4;
    private RadioButton rd5;
    private RadioButton rd6;
    private RadioButton rd7;
    private RadioButton rd8;
    private EditText edit_bfzj;
    private EditText edit_dbsx;
    private RadioGroup rg_khzt;
    private RadioGroup rg_bffl;

    private void initBaseView() {
        tv_callOnDate = findViewById(R.id.tv_callOnDate);
        tv_choiceDate = findViewById(R.id.tv_choiceDate);// 选择日期
        rd1 = findViewById(R.id.rd1);// 选择日期
        rd2 = findViewById(R.id.rd2);// 选择日期
        rd3 = findViewById(R.id.rd3);// 选择日期
        rd4 = findViewById(R.id.rd4);// 选择日期
        rd5 = findViewById(R.id.rd5);// 选择日期
        rd6 = findViewById(R.id.rd6);// 选择日期
        rd7 = findViewById(R.id.rd8);// 选择日期
        rd8 = findViewById(R.id.rd8);// 选择日期
        edit_bfzj = findViewById(R.id.edit_bfzj);// 选择日期
        edit_dbsx = findViewById(R.id.edit_dbsx);// 选择日期
        rg_khzt = findViewById(R.id.rg_khzt);// 选择日期
        rg_bffl = findViewById(R.id.rg_bffl);// 选择日期
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
        mRecyclerViewPic.setLayoutManager(new GridLayoutManager(context, 3));
        mPicAdapter = new PicAdapter(context);
        mPicAdapter.openLoadAnimation();
        mRecyclerViewPic.setAdapter(mPicAdapter);
        mPicAdapter.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
            @Override
            public void onDeletePicListener(int position) {
                mPicList.remove(position);
                refreshAdapter(mPicAdapter.isDelete(), null, position);
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
                refreshAdapter(!mPicAdapter.isDelete(), null, null);
            }
        });
    }

    /**
     * 刷新适配器（isDelete：是否删除状态，movePosition：删除的下标）
     */
    private List<String> mPicList = new ArrayList<>();//路径没有拼接file://：用来上传的

    private void refreshAdapter(boolean isDelete, List<String> picList, Integer movePosition) {
        List<String> datas = mPicAdapter.getData();
        mPicAdapter.setDelete(isDelete);
        if (null != picList) {
            if (null == datas || datas.isEmpty()) {
                mPicAdapter.setNewData(picList);
            } else {
                mPicAdapter.addData(picList);
            }
        }
        if (null != movePosition) {
            mPicAdapter.remove(movePosition);
        }
        mPicAdapter.notifyDataSetChanged();
        List<String> datas2 = mPicAdapter.getData();
        if (null == datas2 || datas2.isEmpty()) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                List<String> picList = new ArrayList<>();
                for (ImageItem imageItem : images) {
                    mPicList.add(imageItem.path);//上传图片的
                    picList.add("file://" + imageItem.path);//图片显示
                    Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
                }
                refreshAdapter(false, picList, null);
            }
        }
    }

    /**
     * 录音相关
     */
    private LinearLayout ll_voice;// 语音时间
    private TextView tv_voicetime;// 语音时间
    private ImageView iv_voice;// 语音时间
    private View bt_record;// 语音按钮
    private View layout_recode;// 语音按钮
    private ImageView dialog_img;
    private boolean isRecord;
    private static int RECORD_ING = 1; // 正在录音
    private static int RECODE_STATE = 0; // 录音的状态
    private static int RECODE_ED = 2; // 完成录音
    private static int RECORD_NO = 0; // 不在录音
    private static float recodeTime = 0.0f; // 录音的时间
    private static int MAX_TIME = 0; // 最长录制时间，单位秒，0为无时间限制
    private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1
    private static double voiceValue = 0.0; // 麦克风获取的音量值
    private AudioRecorder mr;
    private long l;
    private File file_voice;// 录音文件
    private String path_voice;// 语音路径

    private void initVoiceView() {
        // 录音时显示的麦克风提示
        ll_voice = findViewById(R.id.ll_voice);
        tv_voicetime = findViewById(R.id.tv_voicetime);
        layout_recode = findViewById(R.id.layout_recode);
        dialog_img = findViewById(R.id.iv_voiceProgress);
        bt_record = findViewById(R.id.record);// 录音按钮
        // 删除语音
        findViewById(R.id.iv_delvoice).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlayer();// 停止播放
                ll_voice.setVisibility(View.GONE);
                File f = new File(Constans.DIR_VOICE, path_voice);
                if (f != null) {
                    f.delete();
                }
                recodeTime = 0;
                path_voice = null;
            }
        });
        // 录音按钮
        bt_record.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isRecord) {
                    try {
                        recoord(event);
                    } catch (IOException e) {
                        ToastUtils.showCustomToast("录音失败！检查sd卡是否损坏！");
                    }
                }
                return false;
            }
        });
        bt_record.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
                if (!sdCardExist) {
                    ToastUtils.showCustomToast("SD卡不存在！");
                } else {
                    if (RECODE_STATE != RECORD_ING) {
                        l = System.currentTimeMillis();
                        mr = new AudioRecorder(String.valueOf(l));
                        RECODE_STATE = RECORD_ING;
                        layout_recode.setVisibility(View.VISIBLE);
                        try {
                            mr.start();
                            isRecord = true;
                        } catch (IOException e) {
                            ToastUtils.showCustomToast("操作失败！");
                        }
                        mythread();
                    }
                }
                return false;
            }
        });

        iv_voice = (ImageView) findViewById(R.id.iv_voice);
        iv_voice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下载语音
                setVoice(path_voice, 0);
            }
        });
    }


    /**
     * 点击事件
     */
    @OnClick({R.id.tv_choiceDate, R.id.tv_khzt, R.id.tv_bffl})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choiceDate:
                MyChooseTimeUtil.chooseDate(context,"下次日期", tv_choiceDate);
                break;
            case R.id.tv_khzt:// 客户状态
                if (rg_khzt.getVisibility() == rg_khzt.VISIBLE) {
                    rg_khzt.setVisibility(View.GONE);
                } else {
                    rg_khzt.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_bffl:// 拜访分类
                if (rg_bffl.getVisibility() == rg_bffl.VISIBLE) {
                    rg_bffl.setVisibility(View.GONE);
                } else {
                    rg_bffl.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlayer();// 界面停止时，停止播放
    }

    //TODO *****************************************录音相关*************************************************
    // 录音计时线程
    private Thread recordThread;

    private void mythread() {
        recordThread = new Thread(ImgThread);
        recordThread.start();
    }

    // 录音线程
    private Runnable ImgThread = new Runnable() {
        @Override
        public void run() {
            recodeTime = 0.0f;
            while (RECODE_STATE == RECORD_ING) {
                if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
                    imgHandle.sendEmptyMessage(0);
                } else {
                    try {
                        Thread.sleep(200);
                        recodeTime += 0.2;
                        if (RECODE_STATE == RECORD_ING) {
                            voiceValue = mr.getAmplitude();//振幅值
                            imgHandle.sendEmptyMessage(1);
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        Handler imgHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                switch (msg.what) {
                    case 0:
                        // 录音超过15秒自动停止
                        if (RECODE_STATE == RECORD_ING) {
                            RECODE_STATE = RECODE_ED;
                            layout_recode.setVisibility(View.INVISIBLE);
                            try {
                                mr.stop();
                                isRecord = false;
                            } catch (IOException e) {
                                ToastUtils.showCustomToast("录音失败请重新录音！");
                            }
                            voiceValue = 0.0;
                            if (recodeTime < 1.0) {
                                ToastUtils.showCustomToast("录音时间太短！");
                                RECODE_STATE = RECORD_NO;
                            }
                        }
                        break;
                    case 1:
                        setDialogImage();
                        break;
                }
            }
        };
    };

    // 录音Dialog图片随声音大小切换
    private void setDialogImage() {
        if (voiceValue < 200.0) {
            dialog_img.setImageResource(R.drawable.record_animate_01);
        } else if (voiceValue > 200.0 && voiceValue < 400) {
            dialog_img.setImageResource(R.drawable.record_animate_02);
        } else if (voiceValue > 400.0 && voiceValue < 800) {
            dialog_img.setImageResource(R.drawable.record_animate_03);
        } else if (voiceValue > 800.0 && voiceValue < 1600) {
            dialog_img.setImageResource(R.drawable.record_animate_04);
        } else if (voiceValue > 1600.0 && voiceValue < 3200) {
            dialog_img.setImageResource(R.drawable.record_animate_05);
        } else if (voiceValue > 3200.0 && voiceValue < 5000) {
            dialog_img.setImageResource(R.drawable.record_animate_06);
        } else if (voiceValue > 5000.0 && voiceValue < 7000) {
            dialog_img.setImageResource(R.drawable.record_animate_07);
        } else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_08);
        } else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_09);
        } else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_10);
        } else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_11);
        } else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_12);
        } else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_13);
        } else if (voiceValue > 28000.0) {
            dialog_img.setImageResource(R.drawable.record_animate_14);
        }
    }

    //录音
    private void recoord(MotionEvent event) throws IOException {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (RECODE_STATE == RECORD_ING) {
                    RECODE_STATE = RECODE_ED;
                    layout_recode.setVisibility(View.INVISIBLE);
                    mr.stop();
                    isRecord = false;
                    voiceValue = 0.0;

                    if (recodeTime < MIX_TIME) {
                        ToastUtils.showCustomToast("录音时间太短！");
                        RECODE_STATE = RECORD_NO;
                        File f = new File(Constans.DIR_VOICE);
                        if (!f.isDirectory()) {
                            f.mkdirs();
                        }
                        file_voice = new File(Constans.DIR_VOICE, l + ".amr");
                        if (file_voice != null) {
                            file_voice.delete();
                        }
                    } else {
                        File f = new File(Constans.DIR_VOICE);
                        if (!f.isDirectory()) {
                            f.mkdirs();
                        }
                        file_voice = new File(Constans.DIR_VOICE, l + ".amr");
                        path_voice = file_voice.getPath();
                        recodeTime = (int) recodeTime;
                        ll_voice.setVisibility(View.VISIBLE);
                        tv_voicetime.setText(String.valueOf(recodeTime) + "'");
                    }
                }
                break;
        }
    }

    // 语音处理
    private boolean playState;
    private MediaPlayer mediaPlayer;
    private int bfid_play;// 以拜访id作为区分（当前点击播放是否与上次播放一致；一致暂停播放，不一致先暂停播放上次的，再播放当前的）

    private void setVoice(final String path, int id) {
        if (!playState) {
            downFileToStartPlayer(path);
        } else {
            stopPlayer();
            if (bfid_play != id) {
                downFileToStartPlayer(path);
            }
        }
    }

    /**
     * 下载音频文件并播放
     */
    private void downFileToStartPlayer(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String fileName = JsonHttpUtil.getInstance().downFile(path);
                if (!TextUtils.isEmpty(fileName)) {
                    openVioceFile(fileName);
                } else {
                    ToastUtils.showCustomToast("文件下载中...");
                }
                Looper.loop();
            }
        }).start();
    }

    /**
     * 自己发送的文件直接打开
     */
    private void openVioceFile(String path) {
        ToastUtils.showCustomToast("播放中。。");
        mHandler.sendEmptyMessage(0);
        if (!playState) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                playState = true;
                // 设置播放结束时监听
                mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playState = false;
                        mediaPlayer.release();
                        mediaPlayer = null;
                        mHandler.sendEmptyMessage(1);
                    }
                });
            } catch (IllegalArgumentException e) {
                ToastUtils.showCustomToast("播放失败");
            } catch (IllegalStateException e) {
                ToastUtils.showCustomToast("播放失败");
            } catch (IOException e) {
                ToastUtils.showCustomToast("播放失败");
            }
        }
    }

    /**
     * 停止播放
     */
    private void stopPlayer() {
        mHandler.sendEmptyMessage(1);
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();// 释放资源
                    mediaPlayer = null;
                    playState = false;
                } else {
                    playState = false;
                }
            } catch (IllegalStateException e) {
                playState = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPlayer();
    }

    //在子线程中用handler播放动画，停止动画
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    playAnimation();
                    break;
                case 1:
                    stopAnimation();
                    break;
            }
        }
    };

    // 播放动画
    private void playAnimation() {
        if (iv_voice != null) {
            iv_voice.setImageResource(R.drawable.voice_from_playing_s0);
            iv_voice = null;
        }
        iv_voice = findViewById(R.id.iv_voice);
        iv_voice.setImageResource(R.drawable.animation_left);
        AnimationDrawable animation = (AnimationDrawable) iv_voice.getDrawable();
        animation.start();
    }

    // 停止播放动画
    private void stopAnimation() {
        if (iv_voice != null) {
            iv_voice.setImageResource(R.drawable.voice_from_playing_s0);
        }
    }

    //下载音频文件(临时的：修改数据的)
    private void downFile(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String fileName = JsonHttpUtil.getInstance().downFile(path);
                if (!TextUtils.isEmpty(fileName)) {
                    file_voice = new File(fileName);
                }
                Looper.loop();
            }
        }).start();
    }


    //TODO*********************************************接口相关的**********************************************************
    private String bfzjStr;
    private String dbsxStr;
    private String dataStr;
    private String xsjdNm;
    private String bfflNm;

    private void addData() {
        bfzjStr = edit_bfzj.getText().toString().trim();
        dbsxStr = edit_dbsx.getText().toString().trim();
        dataStr = tv_choiceDate.getText().toString();
        if (MyUtils.isEmptyString(mLongitude) || MyUtils.isEmptyString(mLatitude)) {
            ToastUtils.showCustomToast("拜访签到地址不能为空");
            return;
        }

        if ("点击选择".equals(dataStr)) {// 默认"点击选择"--提示
            dataStr = "";
        }

        if (rd1.isChecked()) {
            xsjdNm = "新增开立";
        } else if (rd2.isChecked()) {
            xsjdNm = "跟进拜访";
        } else if (rd3.isChecked()) {
            xsjdNm = "意向签约";
        } else if (rd4.isChecked()) {
            xsjdNm = "订货下单";
        } else if (rd5.isChecked()) {
            xsjdNm = "流失";
        }

        if (rd6.isChecked()) {
            bfflNm = "按计划拜访";
        } else if (rd7.isChecked()) {
            bfflNm = "临时拜访";
        } else if (rd8.isChecked()) {
            bfflNm = "新开立拜访";
        }
        if (!MyNetWorkUtil.isNetworkConnected()) {
            ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
            showDialogCache();
            return;
        }
        getP().addData(context, count6, bfId, cid, mLongitude, mLatitude, mAddressStr, pdateStr,
                file_voice, (int) recodeTime, bfzjStr, dbsxStr, dataStr, xsjdNm, bfflNm, mPicList, mQueryToken);
    }


    //显示上次提交的信息
    public void showInfo(QueryBfgzxcBean data) {
        bfId = data.getId();// 下次拜访ID
        edit_bfzj.setText(data.getBcbfzj());
        edit_dbsx.setText(data.getDbsx());

        mLatitude = data.getLatitude();
        mLongitude = data.getLongitude();
        mAddressStr = data.getAddress();
        mTvAddress.setText(mAddressStr);

        if (MyUtils.isEmptyString(data.getXcdate())) {// 默认"点击选择"--提示
            tv_choiceDate.setText("点击选择");
        } else {
            tv_choiceDate.setText("" + data.getXcdate());
        }

        xsjdNm = data.getXsjdNm();
        bfflNm = data.getBfflNm();
        if ("新增开立".equals(xsjdNm)) {
            rd1.setChecked(true);
        } else if ("跟进拜访".equals(xsjdNm)) {
            rd2.setChecked(true);
        } else if ("意向签约".equals(xsjdNm)) {
            rd3.setChecked(true);
        } else if ("订货下单".equals(xsjdNm)) {
            rd4.setChecked(true);
        } else if ("流失".equals(xsjdNm)) {
            rd5.setChecked(true);
        }
        if ("按计划拜访".equals(bfflNm)) {
            rd6.setChecked(true);
        } else if ("临时拜访".equals(bfflNm)) {
            rd7.setChecked(true);
        } else if ("新开立拜访".equals(bfflNm)) {
            rd8.setChecked(true);
        }

        path_voice = data.getVoiceUrl();
        if (!MyUtils.isEmptyString(path_voice)) {
            ll_voice.setVisibility(View.VISIBLE);
            tv_voicetime.setText(String.valueOf(data.getVoiceTime()) + "\"");
            //下载文件
            try {
                downFile(path_voice);
                recodeTime = Integer.valueOf(data.getVoiceTime());
            } catch (Exception e) {
            }
        }

        // 图片
        List<String> picList = new ArrayList<>();
        List<QueryBfgzxcBean.XcbfPhoto> list = data.getList();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                QueryBfgzxcBean.XcbfPhoto xcbfPhoto = list.get(i);
                Constans.publish_pics1.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + xcbfPhoto.getPic()));
                picList.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + xcbfPhoto.getPic()));
            }
        }
        refreshAdapter(false, picList, null);
        saveImg(picList);
    }

    //下载图片并保存文件
    private void saveImg(final List<String> list) {
        if (null == list || list.isEmpty()) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            final int j = i;
            imageLoder.loadImage(list.get(i), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    // 第一次保存的文件路径容器不等于图片url容器长度，就下载，添加
                    // 后续选择会往同时往url路径和图片路径增加，相同，就不继续下载
                    File outDir = new File(Constans.DIR_IMAGE_PROCEDURE);// 保存到临时文件夹
                    if (!outDir.exists()) {
                        outDir.mkdirs();
                    }
                    File file = new File(outDir, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        // 压缩图片 100为不压缩
                        FileOutputStream fos = new FileOutputStream(file);
                        loadedImage.compress(CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                        Constans.publish_pics1111.add(file.getAbsolutePath());
                        //j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
                        mPicList.add(j, file.getAbsolutePath());//上传图片的
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    //数据提交成功-添加或修改
    public void submitDataSuccess() {
        Intent data = new Intent();
        data.putExtra(ConstantUtils.Intent.SUCCESS, true);
        setResult(0, data);
        OtherUtils.resetStepStatus(Step6Activity.this);// 退出界面重置原状态
        sendLocation();// 上下班添加轨迹点
        ActivityManager.getInstance().closeActivity(context);
    }

    // 上下班添加轨迹点
    private void sendLocation() {
        // 状态--1:上班，2：下班，3：拜访签到，4：拜访签退
        MyLongConnectionUtil.getInstance().addLocation(mLatitude, mLongitude, mAddressStr, mLocationFrom, 4);
    }

    private int mErrorCount;

    public void submitDataError() {
        mErrorCount++;
        if (mErrorCount > 1) {
            showDialogCache();
        }
    }

    public void showDialogCache() {
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                saveCacheData();
            }
        });
    }

    //保存缓存数据
    public void saveCacheData() {
        ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
        DStep6Bean bean = new DStep6Bean();
        bean.setUserId(SPUtils.getID());
        bean.setCompanyId(SPUtils.getCompanyId());
        bean.setCount(count6);
        bean.setCid(cid);
        bean.setKhNm(mKhNm);
        bean.setLatitude(mLatitude);
        bean.setLongitude(mLongitude);
        bean.setAddress(mAddressStr);
        bean.setBfId(bfId);
        bean.setPicList(mPicList);
        bean.setBcbfzj(bfzjStr);
        bean.setDbsx(dbsxStr);
        bean.setXcdate(dataStr);
        bean.setXsjdNm(xsjdNm);
        bean.setBfflNm(bfflNm);
        bean.setVoice(path_voice);
        bean.setVoiceTime((int) recodeTime);
        bean.setTime(MyTimeUtils.getNowTime());
        MyDataUtils.getInstance().saveStep6(bean);

        Intent data = new Intent();
        data.putExtra(ConstantUtils.Intent.SUCCESS, true);
        data.putExtra(ConstantUtils.Intent.COUNT, 2);
        setResult(0, data);
        ActivityManager.getInstance().closeActivity(context);
    }

    //避免重复的token
    private String mQueryToken;

    public void doToken(String data) {
        mQueryToken = data;
    }


}
