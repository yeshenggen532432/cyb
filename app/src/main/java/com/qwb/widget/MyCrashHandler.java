package com.qwb.widget;

import com.qwb.event.CrashEvent;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.log.XLog;

/**
 * 捕获异常
 */

public class MyCrashHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        BusProvider.getBus().post(new CrashEvent());
//        XLog.e("捕获异常","捕获异常");
    }
}
