package com.xmsx.cnlife.view.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.WindowManager;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.chiyong.t3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 创建自定义的Dialog，主要学习实现原理
 */

public class MyVoiceDialog extends Dialog {
    private OnErrorListener onErrorListener;//取消按钮被点击了的监听器
    private OnSuccessListener onSuccessOnclick;//确定按钮被点击了的监听器
    private Context mContext;

    public MyVoiceDialog(@NonNull Context context) {
        super(context,  R.style.voiceDialog);
        this.mContext = context;
    }

    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     */
    public void setOnSuccessOnclick(OnSuccessListener onSuccessOnclick) {
        this.onSuccessOnclick = onSuccessOnclick;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_dialog_voice);
        // 设置窗口大小
        if(null != mContext){
            WindowManager windowManager = getWindow().getWindowManager();
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            WindowManager.LayoutParams attributes = getWindow().getAttributes();
//            attributes.alpha = 1f;
            attributes.width = screenWidth;
            attributes.height = screenHeight;
//            attributes.width = (int)(screenWidth * 0.8);
//            attributes.height = (int)(screenWidth * 0.5);
            getWindow().setAttributes(attributes);
            setCanceledOnTouchOutside(false);
        }

        initVoice();
    }

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
//    private SharedPreferences mSharedPreferences;
    private StringBuffer buffer = new StringBuffer();
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private String resultType = "json";
    int ret = 0; // 函数调用返回值
    private Toast mToast;
    private SpeechRecognizer mIat;
    private void initVoice(){
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
//        // 移动数据分析，收集开始听写事件
//        FlowerCollector.onEvent(mContext, "iat_recognize");
//        mSharedPreferences = mContext.getSharedPreferences(IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);

        // 初始化识别无UI识别对象
//      使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);
        // 设置语言：中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域:mandarin(普通话)
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");

        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            showTip("听写失败,错误码：" + ret);
        } else {
//            showTip(getString(R.string.text_begin));
        }
    }


    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            showTip(error.getPlainDescription(true));
            dismiss();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
            dismiss();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            if (resultType.equals("json")) {
                printResult(results, isLast);
            }else if(resultType.equals("plain")) {
                buffer.append(results.getResultString());
//                mResultText.setText(buffer.toString());
//                mResultText.setSelection(mResultText.length());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    //打印结果
    private void printResult(RecognizerResult results, boolean isLast) {
        String text = parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
//        showTip(resultBuffer.toString());

        //正确回调
         if (onSuccessOnclick != null && isLast) {
             dismiss();
             onSuccessOnclick.onSuccessOnclick(resultBuffer.toString());
         }
    }



    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


    //错误回调
    public interface OnErrorListener {
        public void onErrorClick();
    }
    //成功回调
    public interface OnSuccessListener {
        public void onSuccessOnclick(String result);
    }

    //解析
    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }

}
