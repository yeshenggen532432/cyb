package com.ilike.voicerecorder.core;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.Time;

import com.ilike.voicerecorder.utils.EMError;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 声音录制类
 */
public class VoiceRecorder {
    //语音根目录(默认)，可调用VoiceRecorderView类中setVoiceDirPath()修改
    public String voiceDirPath = Environment.getExternalStorageDirectory() + "/qwb/voice/kq";

    MediaRecorder recorder;

    static final String EXTENSION = ".amr";

    private boolean isRecording = false;
    private long startTime;
    private String voiceFilePath = null;
    private String voiceFileName = null;
    private File file;
    private Handler handler;

    private boolean isCustomNamingFile = false;

    public VoiceRecorder(Handler handler) {
        this.handler = handler;
    }

    /**
     * 开始录制并返回录制文件名
     */
    public String startRecording(Context appContext) {
        try {
            file = null;
            // recorder需要重新创建，不然会抛异常
            if (recorder != null) {
                recorder.release();
                recorder = null;
            }

            //指定录音文件
            File dirFile = new File(voiceDirPath);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            if (!isCustomNamingFile) {
                voiceFileName = getVoiceFileName(System.currentTimeMillis() + "");
            }
            file = new File(voiceDirPath, voiceFileName);
            voiceFilePath = file.getAbsolutePath();

            //创建MediaRecorder对象，并一些配置
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setAudioChannels(1); // MONO
            recorder.setAudioSamplingRate(8000); // 8000Hz
            recorder.setAudioEncodingBitRate(64); // seems if change this to
            recorder.setOutputFile(file.getAbsolutePath());
            recorder.prepare();//准备录制
            recorder.start();

            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
                        /**
                         * 监听音量，改变录音显示icon
                         */
                        android.os.Message msg = new android.os.Message();
                        msg.what = recorder.getMaxAmplitude() * 13 / 0x7FFF;
                        handler.sendMessage(msg);
                        SystemClock.sleep(100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        startTime = new Date().getTime();
        return file == null ? null : file.getAbsolutePath();
    }


    /**
     * 停止录制并删除文件
     */
    public void discardRecording() {
        if (recorder != null) {
            try {
                isRecording = false;
                recorder.stop();
                recorder.release();//释放资源
                recorder = null;

                if (file != null && file.exists() && !file.isDirectory()) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 停止录制并返回录制时间
     */
    public int stopRecoding() {
        if (recorder != null) {
            isRecording = false;
            recorder.stop();
            recorder.release();//释放资源
            recorder = null;

            if (file == null || !file.exists() || !file.isFile()) {
                return EMError.FILE_INVALID;
            }
            if (file.length() == 0) {
                file.delete();
                return EMError.FILE_INVALID;
            }
            int seconds = (int) (new Date().getTime() - startTime) / 1000;
            return seconds;
        }
        return 0;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        if (recorder != null) {
            recorder.release();
        }
    }

    private String getVoiceFileName(String uid) {
        Time now = new Time();
        now.setToNow();
        return uid + now.toString().substring(0, 15) + EXTENSION;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public String getVoiceFilePath() {
        return voiceFilePath;
    }

    public String getVoiceFileName() {
        return voiceFileName;
    }


    public void isCustomNamingFile(boolean isTrue, String name) {
        if (isTrue) {
            isCustomNamingFile = isTrue;
            setVoiceFileName(name);
        }
    }

    /**
     * 自定义音频文件命名
     */
    public void setVoiceFileName(String voiceFileName) {
        this.voiceFileName = voiceFileName + EXTENSION;
    }

    public String getVoiceDirPath() {
        return voiceDirPath;
    }

    /**
     * 指定语音根目录
     */
    public void setVoiceDirPath(String voiceDirPath) {
        this.voiceDirPath = voiceDirPath;
    }
}
