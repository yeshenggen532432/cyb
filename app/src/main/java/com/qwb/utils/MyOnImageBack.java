package com.qwb.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.functions.Consumer;

/**
 * 相册和拍照
 */
public class MyOnImageBack implements MyPopWindowManager.OnImageBack {

    private Activity activity;
    private int picNum;

    public MyOnImageBack(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void fromCamera() {
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//TODO 许可
                            ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount - picNum);
                            ImagePicker.getInstance().setCrop(false);
                            ImagePicker.getInstance().setMultiMode(true);
                            Intent intent = new Intent(activity, ImageGridActivity.class);
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                            activity.startActivityForResult(intent, Constans.TAKE_PIC_XJ);
                        } else {
                            //TODO 未许可
                        }
                    }
                });
    }

    @Override
    public void fromPhotoAlbum() {
        //打开选择,本次允许选择的数量
        ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount - picNum);
        ImagePicker.getInstance().setCrop(false);
        ImagePicker.getInstance().setMultiMode(true);
        Intent intent1 = new Intent(activity, ImageGridActivity.class);
        activity.startActivityForResult(intent1, Constans.TAKE_PIC_XC);
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }
}
