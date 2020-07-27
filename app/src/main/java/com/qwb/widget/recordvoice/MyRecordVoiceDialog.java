package com.qwb.widget.recordvoice;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qwb.utils.Constans;
import com.chiyong.t3.R;


/**
 * 自定义网络加载对话框
 * 备注：com.wang.avi.AVLoadingIndicatorView加载动画，网址：https://github.com/81813780/AVLoadingIndicatorView
 */
public class MyRecordVoiceDialog extends Dialog {
    private Context mContext;
    private ImageView  mIvPauseContinue;
    private VoiceLineView voicLine;
    private TextView mRecordHintTv;
    private EnRecordVoiceListener enRecordVoiceListener;
    private VoiceManager voiceManager;

    private boolean isFinish = false;

    public MyRecordVoiceDialog(@NonNull Context context) {
        super(context, R.style.record_voice_dialog);
        mContext = context;
//        super(context, R.style.LoadingDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.x_dialog_record_voice);
        try {
//            // 设置窗口大小
//            if(null != mContext){
//                WindowManager windowManager = getWindow().getWindowManager();
//                int screenWidth = windowManager.getDefaultDisplay().getWidth();
//                WindowManager.LayoutParams attributes = getWindow().getAttributes();
//                attributes.alpha = 0.5f;
//                attributes.width = screenWidth/3;
//                attributes.height = screenWidth/3;
//                getWindow().setAttributes(attributes);
//                setCanceledOnTouchOutside(false);
//            }

            setCanceledOnTouchOutside(false);
            setCancelable(false);

//            mVolumeIv = (ImageView)findViewById(R.id.iv_voice);
            voicLine= (VoiceLineView) findViewById(R.id.voicLine);
            mRecordHintTv = (TextView) findViewById(R.id.tv_length);
            mRecordHintTv.setText("00:00:00");
            mIvPauseContinue= (ImageView) findViewById(R.id.iv_continue_or_pause);
            //暂停或继续
            mIvPauseContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(voiceManager!=null){
                        voiceManager.pauseOrStartVoiceRecord();
                    }
                }
            });
            //完成
            findViewById(R.id.iv_complete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isFinish = true;
                    if(voiceManager!=null){
                        voiceManager.stopVoiceRecord();
                    }
                    dismiss();
                }
            });
            //关闭
            findViewById(R.id.layout_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(voiceManager!=null){
                        voiceManager.stopVoiceRecord();
                    }
                    if (enRecordVoiceListener != null) {
                        enRecordVoiceListener.onCloseRecord();
                    }
                    dismiss();
                }
            });
        }catch (Exception e){
        }

        initVoiceManager();

    }

    public void initVoiceManager(){
        voiceManager =VoiceManager.getInstance(mContext);
        voiceManager.setVoiceRecordListener(new VoiceManager.VoiceRecordCallBack() {
            @Override
            public void recDoing(long time, String strTime) {
                mRecordHintTv.setText(strTime);
            }

            @Override
            public void recVoiceGrade(int grade) {
                voicLine.setVolume(grade);
            }

            @Override
            public void recStart(boolean init) {
                mIvPauseContinue.setImageResource(R.drawable.icon_pause);
                voicLine.setContinue();
            }

            @Override
            public void recPause(String str) {
                mIvPauseContinue.setImageResource(R.mipmap.x_ic_continue_white);
                voicLine.setPause();
            }


            @Override
            public void recFinish(long length, String strLength, String path) {
                if (enRecordVoiceListener != null && isFinish) {
                    enRecordVoiceListener.onFinishRecord(length, strLength, path);
                }
            }
        });
        //改为自己项目的文件路径
        voiceManager.startVoiceRecord(Constans.DIR_VOICE_KQ);
    }

    /**
     * 结束回调监听
     */
    public interface EnRecordVoiceListener {
        void onFinishRecord(long length, String strLength, String filePath);
        void onCloseRecord();
    }


    /**
     * 设置监听
     */
    public void setEnrecordVoiceListener(EnRecordVoiceListener enRecordVoiceListener) {
        this.enRecordVoiceListener = enRecordVoiceListener;
    }

    /**
     * 暂停录音
     */
    public void pauseVoiceRecord(){
        if(voiceManager!=null){
            voiceManager.pauseVoiceRecord();
        }
    }



//    @Override
//    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            //按返回键是否关闭加载框
//            if(mCancelable){
//                dismiss();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
