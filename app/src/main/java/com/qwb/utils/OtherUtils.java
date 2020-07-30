package com.qwb.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.qwb.application.MyApp;
import com.chiyong.t3.R;

import java.io.File;

/**
 * Created by Administrator on 2018/4/25.
 */

public class OtherUtils {
    /**
     * 根据文件路径--清除缓存图片
     */
    public static void deleteFile(String path){
        File dir = new File(path);
        deleteDirWihtFile(dir);
    }
    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    /**
     * 删除图片文件（拍照的图片步骤1,2,3,4）
     */
    public static void deletePhotoFile(Context context,String filePath){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        String where = MediaStore.Images.Media.DATA + "='" + filePath + "'";
        //删除图片
        mContentResolver.delete(uri, where, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(context, paths, null, null);
            MediaScannerConnection.scanFile(context, new String[] {filePath},null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri){}
            });
        }else{
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
    }

    /**
     * 拜访步骤1,2,3,6：重置默认状态
     * 1: 删除拍照图片
     * 2: 清空数据
     */
    public static void resetStepStatus(Context context){
        try {
            //这个会把相册的图片一起删除
//        for (int i=0;i<Constans.publish_pics.size();i++){
//            OtherUtils.deletePhotoFile(context,Constans.publish_pics.get(i));
//        }
//        if(!TextUtils.isEmpty(Constans.headUrl)){
//            OtherUtils.deletePhotoFile(context,Constans.headUrl);
//        }
            //拍照的图片要删除
            for (int i=0;i<Constans.publish_pics_xj.size();i++){
                OtherUtils.deletePhotoFile(context,Constans.publish_pics_xj.get(i));
            }
            for (int i=0;i<Constans.publish_pics1111.size();i++){
                OtherUtils.deletePhotoFile(context,Constans.publish_pics1111.get(i));
            }
            for (int i=0;i<Constans.publish_pics2222.size();i++){
                OtherUtils.deletePhotoFile(context,Constans.publish_pics2222.get(i));
            }
            for (int i=0;i<Constans.publish_pics3333.size();i++){
                OtherUtils.deletePhotoFile(context,Constans.publish_pics3333.get(i));
            }
            // 删除拍照的压缩文件图片
            deleteFile(Constans.DIR_IMAGE_PROCEDURE);
            Constans.pic_type = 0;// 退出界面变为原状态
            Constans.headUrl = "";// 退出界面变为原状态
            Constans.isDelModel = false;// 退出界面变为原状态
            Constans.isDelModel2 = false;// 退出界面变为原状态
            Constans.isDelModel3 = false;// 退出界面变为原状态
            Constans.publish_pics_xj.clear();//拼接“完整图片地址”
            Constans.publish_pics.clear();//拼接“完整图片地址”
            Constans.publish_pics1.clear();//拼接“完整图片地址”
            Constans.publish_pics2.clear();
            Constans.publish_pics3.clear();
            Constans.publish_pics1111.clear();//“图片地址”
            Constans.publish_pics2222.clear();
            Constans.publish_pics3333.clear();
        }catch (Exception e){}
    }

    /**
     * 统一设置：状态栏颜色，透明度
     */
    public static void setStatusBarColor(Context context){
//        StatusBarUtil.setColor((Activity)context, context.getResources().getColor(R.color.green), ConstantUtils.STATUSBAR_ALPHA);//设置状态栏颜色
        StatusBarUtil.setColor((Activity)context, context.getResources().getColor(R.color.green), 0);//设置状态栏颜色
    }
    /**
     * 统一设置：全屏，透明度
     */
    public static void setStatusBarTranslucent(Context context){
        StatusBarUtil.setTranslucent((Activity) context,ConstantUtils.STATUSBAR_ALPHA);
    }

    /**
     * 提交数据时：避免重复提交（）
     */
    static TextView mTv;
    public static void setSubmitDelay(Context context,TextView tv){
        mTv=tv;
        tv.setEnabled(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 避免头部右边重复点击--更新UI
     */
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mTv.setEnabled(true);
        }
    };

    /**
     * 获取app版本号
     */
    public static String getAppVersion() {
        try {
            PackageManager packageManager = MyApp.getI().getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(MyApp.getI().getPackageName(), 0);
            String versionCode = info.versionName;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return "";
    }


}
