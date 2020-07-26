package com.qwb.view.common.ui;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanlibrary.ImageUtil;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.parsent.PxScan;
import com.qwb.view.step.model.ShopInfoBean;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 扫描二维码
 */
public class XScanActivity extends XActivity<PxScan> implements QRCodeView.Delegate{
    @Override
    public int getLayoutId() {
        return com.example.scanlibrary.R.layout.activity_scan;
    }

    @Override
    public PxScan newP() {
        return new PxScan();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }

        initUI();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //扫描回调监听：onScanQRCodeSuccess，onCameraAmbientBrightnessChanged，onScanQRCodeOpenCameraError
        mZXingView.setDelegate(this);
    }

    private static final String TAG = XScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    public static final String EXTRAS_MULTIPLE = "multiple";
    public static final String EXTRAS_STKID = "stkId";
    public static final String SCAN_RESULT_LIST = "scan_result_list";//多选的结果数据
    public static final String SCAN_RESULT = "scan_result";//单选的结果数据
    private boolean flashState;//闪光灯是否打开

    private int stkId;//仓库id
    private boolean multiple = true;//多次扫描
    private ArrayList<String> resultList = new ArrayList<>();


    ZXingView mZXingView;
    AppCompatImageView backIv;
    AppCompatImageView flashLightIv;
    AppCompatImageView albumIv;
    TextView mTvOk;
    TextView mTvResult;
    private void initUI(){
        mZXingView = findViewById(com.example.scanlibrary.R.id.zxingview);
        backIv = findViewById(com.example.scanlibrary.R.id.backIv);
        flashLightIv = findViewById(com.example.scanlibrary.R.id.flashLightIv);
        albumIv = findViewById(com.example.scanlibrary.R.id.albumIv);
        mTvOk = findViewById(com.example.scanlibrary.R.id.tv_ok);
        mTvResult = findViewById(com.example.scanlibrary.R.id.tv_result);
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
                    flashLightIv.setImageResource(com.example.scanlibrary.R.drawable.ic_scan_flash_close);
                } else {
                    mZXingView.openFlashlight(); // 打开闪光灯
                    flashLightIv.setImageResource(com.example.scanlibrary.R.drawable.ic_scan_flash_open);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            String picturePath = ImageUtil.getImageAbsolutePath(this, data.getData());
            mZXingView.decodeQRCode(picturePath);
        }
    }

    //---------------------------------------------------------------------------------------------------------------

    private void initIntent(){
        Intent intent = getIntent();
        if(null != intent && intent.getExtras() != null){
            multiple = intent.getBooleanExtra(EXTRAS_MULTIPLE, false);
            stkId = intent.getIntExtra(EXTRAS_STKID, 0);
            if(multiple){
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
                        intent.putParcelableArrayListExtra(SCAN_RESULT_LIST, wareList);
//                        intent.putStringArrayListExtra(SCAN_RESULT_LIST,resultList);
                        setResult(RESULT_OK, intent);
                        ActivityManager.getInstance().closeActivity(context);
                    }
                });
            }
        }
    }

    ArrayList<DialogMenuItem> baseItems = new ArrayList<>();
    private void showDialogScanResult() {
        if(null == resultList || resultList.isEmpty()){
            Toast.makeText(XScanActivity.this,"没有扫描结果哦",Toast.LENGTH_LONG);
           return;
        }
        baseItems.clear();
        for (String s:resultList) {
            DialogMenuItem item = new DialogMenuItem(s,0);
            baseItems.add(item);
        }
        NormalListDialog listDialog = new NormalListDialog(XScanActivity.this,baseItems);
        listDialog.title("扫描结果").show();
    }


    //处理扫描结果
    private void doScanResult(String result) {
        try{
            if(!multiple){
                //播放声音
                startPlayMusic();
                // 震动
                vibrate();
                Intent intent = new Intent();
                intent.putExtra(SCAN_RESULT, result);
                setResult(RESULT_OK, intent);
                ActivityManager.getInstance().closeActivity(context);
            }else{
//                // 开始识别
//                mZXingView.startSpot();
                //过滤重复的
                if(null != resultList && !resultList.contains(result)){
                    //播放声音
                    startPlayMusic();
                    // 震动
                    vibrate();
                    resultList.add(result);
                    getP().getWareByScan(context, result, stkId);
                    Toast.makeText(XScanActivity.this, "扫描成功：" + result, Toast.LENGTH_LONG).show();
                }

                //延迟扫描
                Thread.sleep(2000);
                mZXingView.startSpot();
            }
        }catch (Exception e){
            Toast.makeText(XScanActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
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
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), com.example.scanlibrary.R.raw.scan);
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


    //------------------------------------------------------------------------------------------------------
    private ArrayList<ShopInfoBean.Data> wareList = new ArrayList<>();
    //扫描-单个或多个
    public void doScanSuccess(List<ShopInfoBean.Data> list){
        if(null == list || list.isEmpty()){
            showDialogNoData();
            return;
        }
        //扫描单个条码：可能有多个商品
        if(list.size() == 1){
            ShopInfoBean.Data ware = list.get(0);
            doSelectWare(ware);
//            restartScan();
        }else{
            //多个数据:弹出‘选择商品’对话框
            showDialogSelectWare(list);
        }
    }

    private void showDialogNoData(){
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("抱歉！没有匹配数据哦。可以去后台查看是否设置该条码的商品").btnNum(1).btnText("确定").show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
//                restartScan();
            }
        });
    }

    //单个条码多种商品-选择商品
    private ArrayList<DialogMenuItem> items =new ArrayList<>();
    public void showDialogSelectWare(final List<ShopInfoBean.Data> list){
        try{
            items.clear();
            for (ShopInfoBean.Data ware:list) {
                DialogMenuItem item =new DialogMenuItem(ware.getWareNm(),ware.getWareId());
                items.add(item);
            }
            NormalListDialog dialog = new NormalListDialog(context, items);
            dialog.title("单个条码多种商品").show();
            dialog.setOnOperItemClickL(new OnOperItemClickL() {
                @Override
                public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                    doSelectWare(list.get(position));
//                    restartScan();
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
//            restartScan();
        }
    }

    //处理选择单个商品：1）扫描的结果 2）搜索的结果
    private void doSelectWare(ShopInfoBean.Data ware) {
        try{
            if(!isAddWareList(ware)){
                wareList.add(ware);
            }
        }catch (Exception e){
            ToastUtils.showLongCustomToast(e.getMessage());
        }
    }

    //列表中是否添加过
    public boolean isAddWareList(ShopInfoBean.Data ware){
        boolean flag = false;
        //查询列表是否已经存在：存在提示并定位该位置
        if(null != wareList && !wareList.isEmpty() && null != ware){
            for (int i = 0;i < wareList.size(); i++) {
                ShopInfoBean.Data tempBean = wareList.get(i);
                if(String.valueOf(ware.getWareId()).equals(String.valueOf(tempBean.getWareId()))){
                    flag = true;
                    ToastUtils.showCustomToast("该商品已添加");
                    break;
                }
            }
        }
        return flag;
    }

    //重新开始扫描:1.没数据按‘确定’按钮，2.单个数据 3：多个数据（选择一个）----避扫描太频繁
    public void restartScan(){
        mZXingView.startSpot();
    }


}