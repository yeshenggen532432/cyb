package com.qwb.view.base.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.ui.XTabActivity;
import com.xmsx.qiweibao.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 启动页面
 */
public class XWelcomeActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_welcome;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        // 混淆打包apk点击Home键重启App的问题
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action.equals(Intent.ACTION_MAIN)) {
                Router.pop(context);
                return;
            }
        }
        //设置动态权限
        initPermissions();
    }

    /**
     * 设置动态权限
     */
    private void initPermissions() {
        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE ,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            //自动跳转界面
                            initTimer();
                        } else {
                            ToastUtils.showLongCustomToast("请在手机设置的权限管理中勾选应用所需要的动态权限，不然某些功能不能使用");
                        }
                    }
                });
    }

    /**
     * 定时0.1s跳转界面
     */
    private void initTimer() {
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        // 是否有缓存应用列表;判断是否登录
                        String applyList = SPUtils.getSValues(ConstantUtils.Sp.APP_LIST_NEW);
                        if (SPUtils.getBoolean(ConstantUtils.Sp.IS_LOGIN) && !MyUtils.isEmptyString(applyList) && !"[]".equals(applyList)) {
                            ActivityManager.getInstance().jumpActivity(context,XTabActivity.class);
                        } else {
                            ActivityManager.getInstance().jumpActivity(context,XLoginActivity.class);
                        }
                        ActivityManager.getInstance().closeActivity(context);
                    }
                });
            }
        },1000);
    }

}
