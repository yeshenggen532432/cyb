package com.qwb.utils;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;

import io.reactivex.functions.Consumer;

/**
 * 图片
 */

public class MyImageUtil {


    private static MyImageUtil MANAGER = null;

    public static MyImageUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyImageUtil();
        }
        return MANAGER;
    }

    private MyImageUtil() {

    }

    /**
     * 缩放图片
     */
    public void zoomImage(Activity activity, String[] urls, int index){
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
        activity.startActivity(intent);
    }

    /**
     * 获取图片（相册）
     */
    public void getImageFromAlbum(Activity activity){
        ImagePicker.getInstance().setMultiMode(false);
        ImagePicker.getInstance().setCrop(true);
        Intent intent1 = new Intent(activity, ImageGridActivity.class);
        activity.startActivityForResult(intent1, Constans.TAKE_PIC_XC);
    }

    /**
     * 获取图片（拍照）
     */
    public void getImageFromCamera(final Activity activity){
        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {//TODO 许可
                            ImagePicker.getInstance().setMultiMode(false);
                            ImagePicker.getInstance().setCrop(true);
                            Intent intent = new Intent(activity, ImageGridActivity.class);
                            intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                            activity.startActivityForResult(intent, Constans.TAKE_PIC_XJ);
                        } else {
                            //TODO 未许可
                        }
                    }
                });
    }





}
