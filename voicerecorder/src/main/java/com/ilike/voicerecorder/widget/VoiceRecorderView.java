package com.ilike.voicerecorder.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ilike.voicerecorder.R;
import com.ilike.voicerecorder.core.VoiceRecorder;
import com.ilike.voicerecorder.utils.CommonUtils;
import com.ilike.voicerecorder.utils.EMError;


/**
 * 语音录制控件
 */
public class VoiceRecorderView extends RelativeLayout {
    protected VoiceRecorder voiceRecorder;//声音录制类
//    protected PowerManager.WakeLock wakeLock;//电源服务-唤醒锁

    protected Context context;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected Drawable[] micImages;
    protected boolean isImagesCustom = false;//图片是否自定义

    protected String release_to_cancel = "松开手指，取消发送";
    protected String move_up_to_cancel = "手指上滑，取消发送";

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 改变图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    public VoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_recorder, this);

        micImage = findViewById(R.id.mic_image);
        recordingHint =  findViewById(R.id.recording_hint);

        voiceRecorder = new VoiceRecorder(micImageHandler);

        // 图片动画
        micImages = new Drawable[]{
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14)};

    }

    /**
     * 按下说话按钮时
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    v.setPressed(true);
                    startRecording();
                } catch (Exception e) {
                    //v.setPressed(false);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < 0) {
                    showReleaseToCancelHint();
                } else {
                    showMoveUpToCancelHint();
                }
                return true;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                if (event.getY() < 0) {
                    //丢弃录制的音频
                    discardRecording();
                } else {
                    // 停止录制并发送
                    try {
                        int length = stopRecoding();
                        if (length > 0) {
                            if (recorderCallback != null) {
                                recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                            }

                        } else if (length == EMError.FILE_INVALID) {
                            Toast.makeText(context, "没打开录制权限", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "录制时间太短了", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            default:
                //丢弃录制的音频
                discardRecording();
                return false;
        }
    }

    /**
     * 语音录制完成回调
     * voiceFilePath   录音完毕后的文件路径
     * voiceTimeLength 录音时长
     */
    public interface EaseVoiceRecorderCallback {
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!CommonUtils.isSdcardExist()) {
            Toast.makeText(context, "没有sd卡", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            this.setVisibility(View.VISIBLE);
            recordingHint.setText(context.getString(R.string.move_up_to_cancel));
            recordingHint.setBackgroundColor(Color.TRANSPARENT);
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            e.printStackTrace();
            if (voiceRecorder != null){
                voiceRecorder.discardRecording();
            }
            this.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "录制失败，请重试！", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 取消录制
     */
    public void discardRecording() {
        try {
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 停止录制并返回录制时间
     */
    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        return voiceRecorder.stopRecoding();
    }

    /**
     * 松开手指，取消发送
     */
    public void showReleaseToCancelHint() {
        recordingHint.setText(release_to_cancel);
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 手指上滑，取消发送
     */
    public void showMoveUpToCancelHint() {
        recordingHint.setText(move_up_to_cancel);
        recordingHint.setBackgroundColor(Color.TRANSPARENT);
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

    /**
     * 自定义语音命名
     * isTrue true为自定义，false为默认命名（时间戳）
     */
    public void setCustomNamingFile(boolean isTrue, String name) {
        voiceRecorder.isCustomNamingFile(isTrue, name);
    }

    /**
     * 目前需要传入15张帧动画png
     */
    public void setDrawableAnimation(Drawable[] animationDrawable) {
        micImages = null;
        this.micImages = animationDrawable;
    }


    /**
     * 设置按下显示的提示
     */
    public void setShowReleaseToCancelHint(String releaseToCancelHint) {
        this.release_to_cancel = releaseToCancelHint;
    }

    /**
     * 设置手指向上移动显示的提示语
     */
    public void setShowMoveUpToCancelHint(String moveUpToCancelHint) {
        this.move_up_to_cancel = moveUpToCancelHint;
    }


    /**
     * 设置语音文件存储根目录
     * @param voiceDirPath
     */
    public void setVoiceDirPath(String voiceDirPath) {
        if(!TextUtils.isEmpty(voiceDirPath) && voiceRecorder!=null){
            voiceRecorder.setVoiceDirPath(voiceDirPath);
        }
    }
}
