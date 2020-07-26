package com.example.scanlibrary;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;

import java.util.ArrayList;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 扫描二维码
 */
public class ScanActivity extends AppCompatActivity implements QRCodeView.Delegate{

    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    public static final String EXTRAS_MULTIPLE = "multiple";
    public static final String SCAN_RESULT_LIST = "scan_result_list";//多选的结果数据
    public static final String SCAN_RESULT = "scan_result";//单选的结果数据
    private boolean flashState;//闪光灯是否打开

    private boolean multiple = true;//多次扫描
    private ArrayList<String> resultList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // 保持Activity处于唤醒状态
                                                    Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }

        initUI();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//      mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //扫描回调监听：onScanQRCodeSuccess，onCameraAmbientBrightnessChanged，onScanQRCodeOpenCameraError
        mZXingView.setDelegate(this);
    }


    ZXingView mZXingView;
    AppCompatImageView backIv;
    AppCompatImageView flashLightIv;
    AppCompatImageView albumIv;
    TextView mTvOk;
    TextView mTvResult;
    private void initUI(){
        mZXingView = findViewById(R.id.zxingview);
        backIv = findViewById(R.id.backIv);
        flashLightIv = findViewById(R.id.flashLightIv);
        albumIv = findViewById(R.id.albumIv);
        mTvOk = findViewById(R.id.tv_ok);
        mTvResult = findViewById(R.id.tv_result);
        //返回键
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //闪光灯
        flashLightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flashState) {
                    mZXingView.closeFlashlight(); // 关闭闪光灯
                    flashLightIv.setImageResource(R.drawable.ic_scan_flash_close);
                } else {
                    mZXingView.openFlashlight(); // 打开闪光灯
                    flashLightIv.setImageResource(R.drawable.ic_scan_flash_open);
                }
                flashState = !flashState;
            }
        });
        //选择二维码图片
        albumIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
            }
        });

        initIntent();
        initMediaPlayer();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
////      mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
//
//        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        stopPlayMusic();
        super.onDestroy();
    }

//    //震动
//    private void vibrate() {
//        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(200);
//    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        doScanResult(result);
    }


    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.backIv:
//                finish();
//                break;
//            case R.id.flashLightLayout:
//                if (flashState) {
//                    mZXingView.closeFlashlight(); // 关闭闪光灯
//                    flashLightIv.setImageResource(R.drawable.ic_scan_flash_close);
//                } else {
//                    mZXingView.openFlashlight(); // 打开闪光灯
//                    flashLightIv.setImageResource(R.drawable.ic_scan_flash_open);
//                }
//                flashState = !flashState;
//                break;
//            case R.id.albumLayout://打开相册
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//                break;
//        }
//            case R.id.start_preview:
//                mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//                break;
//            case R.id.stop_preview:
//                mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
//                break;
//            case R.id.start_spot:
//                mZXingView.startSpot(); // 开始识别
//                break;
//            case R.id.stop_spot:
//                mZXingView.stopSpot(); // 停止识别
//                break;
//            case R.id.start_spot_showrect:
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且开始识别
//                break;
//            case R.id.stop_spot_hiddenrect:
//                mZXingView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
//                break;
//            case R.id.show_scan_rect:
//                mZXingView.showScanRect(); // 显示扫描框
//                break;
//            case R.id.hidden_scan_rect:
//                mZXingView.hiddenScanRect(); // 隐藏扫描框
//                break;
//            case R.id.decode_scan_box_area:
//                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
//                break;
//            case R.id.decode_full_screen_area:
//                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
//                break;
//            case R.id.scan_one_dimension:
//                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZXingView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_two_dimension:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_qr_code:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_code128:
//                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZXingView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_ean13:
//                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZXingView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_high_frequency:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、UPC_A、EAN_13、CODE_128
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_all:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZXingView.setType(BarcodeType.ALL, null); // 识别所有类型的码
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_custom:
//                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
//                List<BarcodeFormat> formatList = new ArrayList<>();
//                formatList.add(BarcodeFormat.QR_CODE);
//                formatList.add(BarcodeFormat.UPC_A);
//                formatList.add(BarcodeFormat.EAN_13);
//                formatList.add(BarcodeFormat.CODE_128);
//                hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
//                hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
//                hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
//                mZXingView.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型
//
//                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.choose_qrcde_from_gallery:
//                /*
//                从相册选取二维码图片，这里为了方便演示，使用的是
//                https://github.com/bingoogolapple/BGAPhotoPicker-Android
//                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
//                 */
////                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
////                        .cameraFileDir(null)
////                        .maxChooseCount(1)
////                        .selectedPhotos(null)
////                        .pauseOnScroll(false)
////                        .build();
////                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
//                break;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            String picturePath = ImageUtil.getImageAbsolutePath(this, data.getData());
            mZXingView.decodeQRCode(picturePath);


//            final String picturePath = BGAPhotoPickerActivity.getSelectedPhotos(data).get(0);
//            // 本来就用到 QRCodeView 时可直接调 QRCodeView 的方法，走通用的回调
//            mZXingView.decodeQRCode(picturePath);

            /*
            没有用到 QRCodeView 时可以调用 QRCodeDecoder 的 syncDecodeQRCode 方法

            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
            .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                }
//
//                @Override
//                protected void onPostExecute(String result) {
//                    if (TextUtils.isEmpty(result)) {
//                        Toast.makeText(TestScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }.execute();
        }
    }

    //---------------------------------------------------------------------------------------------------------------

    private void initIntent(){
        Intent intent = getIntent();
        if(null != intent && intent.getExtras() != null){
            multiple = intent.getBooleanExtra(EXTRAS_MULTIPLE, false);
            if(multiple){
//                showNormalDialog();
                mTvResult.setText("预览扫描结果");
                mTvResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialogScanResult();
                    }
                });
                mTvOk.setVisibility(View.VISIBLE);
                mTvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putStringArrayListExtra(SCAN_RESULT_LIST,resultList);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }
    }

    ArrayList<DialogMenuItem> baseItems = new ArrayList<>();
    private void showDialogScanResult() {
        if(null == resultList || resultList.isEmpty()){
            Toast.makeText(ScanActivity.this,"没有扫描结果哦",Toast.LENGTH_LONG);
           return;
        }
        baseItems.clear();
        for (String s:resultList) {
            DialogMenuItem item = new DialogMenuItem(s,0);
            baseItems.add(item);
        }
        NormalListDialog listDialog = new NormalListDialog(ScanActivity.this,baseItems);
        listDialog.title("扫描结果").show();
    }


    //处理扫描结果
    private void doScanResult(String result) {
        try{
            //播放声音
            startPlayMusic();
            // 震动
            vibrate();
            if(!multiple){
                Intent intent = new Intent();
                intent.putExtra(SCAN_RESULT, result);
                setResult(RESULT_OK, intent);
                this.finish();
            }else{
                // 开始识别
                mZXingView.startSpot();
                //过滤重复的
                if(null != resultList && !resultList.contains(result)){
                    resultList.add(result);
                }
            }
            Toast.makeText(ScanActivity.this,"扫描成功：" + result,Toast.LENGTH_LONG);
        }catch (Exception e){
            Toast.makeText(ScanActivity.this,result,Toast.LENGTH_LONG);
        }
    }

    // 震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }


    /**
     * TODO -----------------------------音乐的一些设置-------------------------
     */
    //初始化音乐
    public void initMediaPlayer(){
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.scan);
        mMediaPlayer.setLooping(false);//是否循环播放
    }
    private static MediaPlayer mMediaPlayer;
    //播放音乐
    private static void startPlayMusic(){
        if(mMediaPlayer != null){
            mMediaPlayer.start();
        }
    }
    //关闭音乐，释放资源
    private static void stopPlayMusic(){
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer=null;
        }
    }



}