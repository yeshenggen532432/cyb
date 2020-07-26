package com.qwb.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yeshenggen on 2019/5/11.
 */

public class MyTimeCount extends CountDownTimer {
    private TextView view;

    public MyTimeCount(long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
        this.view = view ;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        view.setTextColor(Color.parseColor("#CDCDCD"));
        view.setClickable(false);
        view.setText("重新发送("+millisUntilFinished / 1000 +")");
    }

    @Override
    public void onFinish() {
        view.setText("获取验证码");
        view.setClickable(true);
        view.setTextColor(Color.parseColor("#2597F0"));
    }
}
