package com.qwb.listener;

import android.os.CountDownTimer;
import android.view.View;

import java.util.Calendar;

/**
 * android 防止按钮多次点击
 */
public abstract class OnNoMoreClickListener implements View.OnClickListener {
    private int MIN_CLICK_DELAY_TIME = 3000;//多少秒点击一次 默认2.5秒
    private long lastClickTime = 0;
    private TimeCount timeCount;

    public OnNoMoreClickListener() {
    }

    /**
     * 设置多少秒之内
     *
     * @param time
     */
    public OnNoMoreClickListener(int time) {
        this.MIN_CLICK_DELAY_TIME = time;
    }

    @Override
    public void onClick(View view) {
        try {
            if(null == timeCount){
                timeCount = new TimeCount(MIN_CLICK_DELAY_TIME, 1000,view);
            }
            timeCount.start();
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                OnMoreClick(view);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 在N秒之内的 ==1 次点击回调次方法
     */
    protected abstract void OnMoreClick(View view);


    public class TimeCount extends CountDownTimer {
        private View mView;

        public TimeCount(long millisInFuture, long countDownInterval, View view) {
            super(millisInFuture, countDownInterval);
            this.mView = view ;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if(null != mView){
                mView.setEnabled(false);
            }
        }

        @Override
        public void onFinish() {
            if(null != mView){
                mView.setEnabled(true);
            }
        }
    }

}

