package com.qwb.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
    图片工具类
 */
public class MyPicUtil {

    private static MyPicUtil MANAGER = null;
    public static MyPicUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyPicUtil();
        }
        return MANAGER;
    }


    public void fromCamera(final Activity activity, final int size) {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//TODO 许可
                            ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount - size);
                            ImagePicker.getInstance().setCrop(false);
                            ImagePicker.getInstance().setMultiMode(true);
                            Intent intent = new Intent(activity, ImageGridActivity.class);
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                            activity.startActivityForResult(intent, Constans.TAKE_PIC_XJ);
                        } else {
                            ToastUtils.showCustomToast("权限未开启！");
                        }
                    }
                });
    }

}
