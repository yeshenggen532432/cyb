package com.qwb.view.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dxs.autostart.utils.AutoStartUtils;
import com.dxs.autostart.utils.BatteryJumpUtils;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.view.mine.parsent.PSet;
import com.qwb.event.MsgModelEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.ILUtil;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.mine.model.AddressUploadBean;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 设置
 */
public class SetActivity extends XActivity<PSet> implements OnClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_set;
    }

    @Override
    public PSet newP() {
        return new PSet();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        // 从后台设置轨迹采集周期和打包周期
        getP().queryAddressUpload();
    }

    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.cb_msg_model)
    CheckBox mCbMsgModel;
    @BindView(R.id.cb_trace)
    CheckBox mCbTrace;
    @BindView(R.id.cb_small_price)
    CheckBox mCbSmallPrice;
    @BindView(R.id.tv_trace)
    TextView tv_trace;
    @BindView(R.id.tv_trace_bian)
    TextView tv_trace_bian;
    @BindView(R.id.btn_shangbaofanshi)
    Button mBtnUpdataType;
    @BindView(R.id.tv_mini)
    TextView mTvMini;

    private void initUI() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("设置");
        initMsgModel();
        initTrace();
        initSmallPrice();
    }

    /**
     * 启用分类消息（默认启用）
     */
    private void initMsgModel() {
        String messageModel = SPUtils.getSValues(ConstantUtils.Sp.MSG_MODEL);
        if (!MyStringUtil.isEmpty(messageModel) && "1".equals(messageModel)) {
            mCbMsgModel.setChecked(false);
        } else {
            mCbMsgModel.setChecked(true);
        }
        mCbMsgModel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    SPUtils.setValues(ConstantUtils.Sp.MSG_MODEL, "2");
                } else {
                    SPUtils.setValues(ConstantUtils.Sp.MSG_MODEL, "1");
                }
                BusProvider.getBus().post(new MsgModelEvent());
            }
        });
    }

    /**
     * 上传轨迹
     */
    int xtCsz = 60;
    boolean isUploadType = false;
    private void initTrace() {
        Boolean isOPen_traceTime = SPUtils.getBoolean("isOPen_traceTime");
        if (isOPen_traceTime != null && isOPen_traceTime == true) {
            mCbTrace.setChecked(true);
            int traceTime = SPUtils.getIValues("traceTime");
            if (traceTime != 0) {
                tv_trace.setText("" + traceTime);
            }
            tv_trace_bian.setText("点击变更");
            tv_trace_bian.setEnabled(true);
        } else {
            mCbTrace.setChecked(false);
            tv_trace_bian.setText("系统时间");
            tv_trace_bian.setEnabled(false);
        }

        mCbTrace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                if (cb.isChecked()) {// 1:没开启，2：开启
                    SPUtils.setBoolean("isOPen_traceTime", true);
                    int traceTime = SPUtils.getIValues("traceTime");
                    if (traceTime != 0) {
                        Constans.gatherInterval = traceTime;
                        Constans.packInterval = traceTime;
                        tv_trace.setText("" + traceTime);
                    } else {//第一次没有设置时
                        Constans.gatherInterval = xtCsz;
                        Constans.packInterval = xtCsz;
                        tv_trace.setText("" + xtCsz);
                    }
                    tv_trace_bian.setText("点击变更");
                    tv_trace_bian.setEnabled(true);
                } else {
                    SPUtils.setBoolean("isOPen_traceTime", false);
                    Constans.gatherInterval = xtCsz;
                    Constans.packInterval = xtCsz;
                    tv_trace.setText("" + xtCsz);
                    tv_trace_bian.setText("系统时间");
                    tv_trace_bian.setEnabled(false);
                }
            }
        });

        //上报方式
        if (Constans.ISDEBUG) {
            isUploadType = SPUtils.getBoolean(ConstantUtils.Sp.TRACK_UPDATE_TYPE);
        } else {
            isUploadType = SPUtils.getBoolean_true(ConstantUtils.Sp.TRACK_UPDATE_TYPE);
        }

    }

    /**
     * 开启开单小单位参考
     */
    private void initSmallPrice() {
        Boolean openSmallPrice = SPUtils.getBoolean(ConstantUtils.Sp.OPEN_SMALL_PRICE);
        if (openSmallPrice != null && openSmallPrice == true) {
            mCbSmallPrice.setChecked(true);
        } else {
            mCbSmallPrice.setChecked(false);
        }
        mCbSmallPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                SPUtils.setBoolean(ConstantUtils.Sp.OPEN_SMALL_PRICE, cb.isChecked());
            }
        });
    }


    @OnClick({R.id.head_left, R.id.head_right, R.id.view_update_password, R.id.view_clear_cache,R.id.view_user_manager, R.id.view_logout, R.id.view_clear_pic,
            R.id.tv_trace_bian, R.id.btn_shangbaofanshi,R.id.view_open_zqd, R.id.view_open_dl, R.id.view_bluetooth_print
    })
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                ActivityManager.getInstance().closeActivity(context);
                break;
            case R.id.view_update_password:
                ActivityManager.getInstance().jumpActivity(context, UpdatePasswordActivity.class);
                break;
            case R.id.view_clear_cache:
                cleanCache();
                break;
            case R.id.tv_trace_bian:// 设置轨迹时间间隔
                showDialogTraceTime();
                break;
            case R.id.btn_shangbaofanshi:// 上报方式
                showDialogTraceUploadType();
                break;
            case R.id.view_user_manager://账号管理
                ActivityManager.getInstance().jumpActivity(context, UserManagerActivity.class);
                break;
            case R.id.view_logout://退出登录把数据把数据删除
                MyUrlUtil.clearUrl();
                MyLoginUtil.logout(SetActivity.this);
                break;
            case R.id.view_clear_pic:// 清除拍照图片
                clearPic();
                break;
            case R.id.view_open_zqd://打开自启动管理设置
                try {
                    AutoStartUtils.newInstance(context).toTargetActivity();
                } catch (Exception e) {
                    ToastUtils.showCustomToast("无法自动跳转");
                }
                break;
            case R.id.view_open_dl://打开电量设置
                try {
                    BatteryJumpUtils.newInstance(context).toTargetActivity();
                } catch (Exception e) {
                    ToastUtils.showCustomToast("无法自动跳转");
                }
                break;
            case R.id.view_bluetooth_print://蓝牙打印设置
//                ActivityManager.getInstance().jumpActivity(context, XBluetoothPrintActivity.class);
                break;
        }
    }

    private void showDialogTraceTime(){
        final String[] items = {"60", "120","180","240","300","600"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择自定义时间").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String traceTime = items[position];
                tv_trace.setText(traceTime);
                SPUtils.setintValues("traceTime", Integer.valueOf(traceTime));
                Constans.gatherInterval = Integer.valueOf(traceTime);
                Constans.packInterval = Integer.valueOf(traceTime);
            }
        });
    }

    private void showDialogTraceUploadType(){
        final String[] items = {"始终上报", "永不上报"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("上报方式").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = items[position];
                mBtnUpdataType.setText(s);
                switch (position){
                    case 0:
                        getP().updateAddressUpload(context, "1");
                        SPUtils.setBoolean(ConstantUtils.Sp.TRACK_UPDATE_TYPE, true);
                        mTvMini.setText(mMin+"分钟");
                        break;
                    case 1:
                        getP().updateAddressUpload(context, "0");
                        SPUtils.setBoolean(ConstantUtils.Sp.TRACK_UPDATE_TYPE, false);
                        mTvMini.setText("");
                        break;

                }
            }
        });
    }


    /**
     * 备注：上传位置的逻辑
     * 1）后台当设置不上传时，业务员自己设置不起作用；
     * 2）后台当设置上传时，业务员自己可以设置上传还是不上传
     */
    int mMin;
    int upload;//上传方式：0不上传，1.手机控制，2上传
    Integer memUpload;//业务员自己修改上传方式：0不上传，1上传（默认）
    public void doAddressUpload(AddressUploadBean data) {
        /**
         * 备注：上传位置的逻辑
         * 1）后台当设置不上传时，业务员自己设置不起作用；
         * 2）后台当设置上传时，业务员自己可以设置上传还是不上传
         */
        upload = data.getUpload();
        memUpload = data.getMemUpload();
        mMin = data.getMin();
        if(upload == 2){
            mTvMini.setText(data.getMin()+"分钟");
            mBtnUpdataType.setText("始终上传");
            mBtnUpdataType.setEnabled(false);
            mBtnUpdataType.setBackground(getResources().getDrawable(R.drawable.shape_kuang_gray));
            mBtnUpdataType.setTextColor(getResources().getColor(R.color.gray_6));
        }else if (upload == 1) {
            if ((memUpload == null || memUpload == 1)) {
                mBtnUpdataType.setText("始终上报");
                mTvMini.setText(data.getMin()+"分钟");
            } else {
                mBtnUpdataType.setText("永不上报");
            }
            mBtnUpdataType.setEnabled(true);
            mBtnUpdataType.setBackground(getResources().getDrawable(R.drawable.shape_kuang_blue_2));
            mBtnUpdataType.setTextColor(getResources().getColor(R.color.x_main_color));
        } else {
            mBtnUpdataType.setText("永不上传");
            mBtnUpdataType.setEnabled(false);
            mBtnUpdataType.setBackground(getResources().getDrawable(R.drawable.shape_kuang_gray));
            mBtnUpdataType.setTextColor(getResources().getColor(R.color.gray_6));
        }
    }

    private void searchFile(File f) {
        File[] fl = f.listFiles();
        if (fl != null && fl.length > 0) {
            for (int i = 0; i < fl.length; i++) {
                if (fl[i].toString().endsWith(".jpg")
                        || fl[i].toString().endsWith(".JPG")
                        || fl[i].toString().endsWith(".png")
                        || fl[i].toString().endsWith(".PNG")) {
                    fl[i].delete();
                }
            }
        }
    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
        ToastUtils.showLongCustomToast("清除缓存中........");
        // 清除imageloder缓存
        ILUtil.getImageLoder().clearMemoryCache();
        // 清除 压缩图片时保存的临时图片 cloudlife/images/cache/
        // 清除 下载的 文件 cloudlife/cachefiles/
        // 清除 发送聊天的语音 文件 cloudlife/voice/
        HashMap<String, String> hashMap = new HashMap<String, String>();
        getFiles(new File(Constans.DIR_VOICE), hashMap);
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            String value = entry.getValue();
            File file = new File(value);
            if (file != null && file.exists()) {
                file.delete();
            }
        }
        ToastUtils.showLongCustomToast("清除完成........");
    }

    private void clearPic(){
        Toast.makeText(this, "清除拍照图片中........", Toast.LENGTH_SHORT).show();
        File f = new File(Constans.DIR_IMAGE);
        File f2 = new File(Constans.DIR_IMAGE_CACHE);
        if (f != null) {
            searchFile(f);
        }
        if (f2 != null) {
            searchFile(f2);
        }
        Toast.makeText(this, "清除完成", Toast.LENGTH_SHORT).show();
    }

    public HashMap<String, String> getFiles(File path, HashMap<String, String> fileList) {
        // 如果是文件夹
        if (path.isDirectory()) {
            File[] listFiles = path.listFiles();
            if (listFiles == null) {
                return null;
            } else {
                for (int i = 0; i < listFiles.length; i++) {
                    getFiles(listFiles[i], fileList);
                }
            }

        } else {
            String absolutePath = path.getAbsolutePath();
            fileList.put(absolutePath, absolutePath);
        }
        return fileList;
    }

}
