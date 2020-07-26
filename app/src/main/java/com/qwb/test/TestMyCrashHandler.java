package com.qwb.test;


import com.qwb.event.CrashEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.widget.MyCrashHandler;
import cn.droidlover.xdroidmvp.event.BusProvider;
import io.reactivex.functions.Consumer;

/**
 * 捕获异常
 */
public class TestMyCrashHandler {

    public static void  test(){
        //第一步：在initData()中初始化
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler());//捕获异常

        //第二步：在bindEvent()中接收异常并处理异常
        BusProvider.getBus()
                .toFlowable(CrashEvent.class)
                .subscribe(new Consumer<CrashEvent>() {
                    @Override
                    public void accept(CrashEvent applyEvent) throws Exception {
                        if (applyEvent.getTag() == ConstantUtils.Event.TAG_CRASH) {
//                            mDo.doCacheData();
                        }
                    }
                });

    }



}
