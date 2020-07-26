package com.qwb.view.work.parsent;


import android.app.Activity;
import android.graphics.Bitmap;
import com.alibaba.fastjson.JSON;
import com.nanchen.compresshelper.CompressHelper;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.mine.model.AddressUploadBean;
import com.qwb.view.work.ui.WorkActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.MyHttpCallback;
import org.json.JSONObject;
import java.io.File;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import cn.droidlover.xdroidmvp.mvp.XPresent;
import okhttp3.Call;

/**
 * 考勤
 */
public class PWork extends XPresent<WorkActivity>{

    // 从后台设置轨迹采集周期和打包周期
    public void queryAddressUpload() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        OkHttpUtils
                .post()
                .params(params)
                .url(Constans.queryAddressUplaodWeb)
                .id(3)
                .build()
                .execute(new MyHttpCallback(null) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson2(response);
                    }
                });
    }

    /**
     * 签到提交请求 tp 1 考勤 2外出反馈
     */
    public void beginCheckIn(final Activity activity, int upload, Integer memUpload, String remark, String longitude, String latitude, String address, final String upToDown) {
        String sbType;
        /**
         * 备注：上传位置的逻辑
         * 0）后台当设置“不上传”时，业务员自己设置不起作用；
         * 1）后台当设置“手机控制”时，业务员自己可以设置上传还是不上传
         * 2）后台当设置“上传”时，业务员自己设置不起作用
         */
        if (upload == 2){
            sbType = "2";
        }else if (upload == 1 && (memUpload == null || memUpload == 1)) {
            //上传的
            sbType = "2";
        } else {
            //不上传的
            sbType = "1";
        }
        Map<String, String> params = new HashMap<>();
        params.put("token", SPUtils.getTK());
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("location", address);
        params.put("tp", upToDown);
        params.put("remark", remark);
        params.put("sbType", sbType);

        // 图片数组(map按key升序)
        TreeMap<String, File> files = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);//将o1和o2调换位置是倒序排序
            }
        });
        int size = Constans.publish_pics.size();
        if (size > 0) {
            params.put("publishTp", "2");
            for (int i = 0; i < size; i++) {
                File oldFile = new File(Constans.publish_pics.get(i));
                File newFile = new CompressHelper.Builder(activity)
                        .setMaxWidth(720)  // 默认最大宽度为720
                        .setMaxHeight(1280) // 默认最大高度为960
                        .setQuality(80)    // 默认压缩质量为80
                        .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式i
                        .setFileName(String.valueOf(System.currentTimeMillis() + "_" + i)) // 设置你的文件名
                        .setDestinationDirectoryPath(Constans.DIR_IMAGE_PROCEDURE)
                        .build()
                        .compressToFile(oldFile);
                //TODO 文件拼接“_i”,取出“_i”为key,最后key升序
                String absolutePath = newFile.getAbsolutePath();
                String j = absolutePath.substring(absolutePath.length() - 6, absolutePath.length() - 5);
                files.put("file" + j, newFile);
            }
        } else {
            params.put("publishTp", "1");
        }
        OkHttpUtils.post()
                .files(files)
                .params(params)
                .url(Constans.addCheckInURL)
                .id(1)
                .build()
                .execute(new MyHttpCallback(activity) {
                    @Override
                    public void myOnError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void myOnResponse(String response, int id) {
                        parseJson1(response, activity, upToDown);
                    }
                });
    }


    //解析数据
    private void parseJson1(String response, Activity activity, String upToDown) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            int state = jsonObject.getInt("state");
            if (state == 1) {
                Constans.current.clear();// 清空数组
                Constans.publish_pics.clear();
                if ("1-1".equals(upToDown)) {
                    ToastUtils.showCustomToast("签到成功");
                    SPUtils.setValues(ConstantUtils.Sp.WORK_STATE, "1");
                    SPUtils.setValues(ConstantUtils.Sp.WORK_TIME, MyTimeUtils.getNowTime());
                    getV().startOrStopTrace(1);// 开启长连接
                }
                if ("1-2".equals(upToDown)) {
                    ToastUtils.showCustomToast("签退成功");
                    SPUtils.setValues(ConstantUtils.Sp.WORK_STATE, "2");
                    SPUtils.setValues(ConstantUtils.Sp.WORK_TIME, "");
                    getV().startOrStopTrace(2);// 关闭长连接
                }
                ActivityManager.getInstance().closeActivity(activity);
            } else {
                ToastUtils.showCustomToast(jsonObject.getString("msg"));
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //解析数据
    private void parseJson2(String response) {
        try {
            AddressUploadBean data = JSON.parseObject(response, AddressUploadBean.class);
            if (data != null) {
                if (data.isState()) {
                    getV().doAddressUpload(data);
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }


}
