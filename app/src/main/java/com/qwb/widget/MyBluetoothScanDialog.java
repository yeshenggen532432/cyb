package com.qwb.widget;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import com.example.bluetooth.prt.HPRTHelper;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.print.adapter.MyBluetoothAdapter;
import com.xmsx.cnlife.view.widget.MyChooseMemberDialog;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.droidlover.xdroidmvp.log.XLog;

/**
 *  蓝牙扫描枪
 */
public class MyBluetoothScanDialog extends BaseDialog<MyChooseMemberDialog> {
    private Context mContext;
    public MyBluetoothScanDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(mContext, R.layout.x_dialog_bluetooth, null);
        ButterKnife.bind(this, inflate);
        inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));
        initUI();
        return inflate;
    }

    @BindView(R.id.lv_bluetooth)
    ListView mListView;
    @BindView(R.id.layout_cancel)
    View mLayoutCancel;
    private MyBluetoothAdapter adapter;
    private List<BluetoothDevice> mList = new ArrayList<>();
    private android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    private void initUI() {
        if(adapter==null){
            adapter = new MyBluetoothAdapter(mContext);
            mListView.setAdapter(adapter);

            adapter.setOnSuccessListener(new MyBluetoothAdapter.OnSuccessListener() {
                @Override
                public void onSuccessListener() {
                    dismiss();
                    initHelper();
                }
            });

        }
        //初始化广播接收
        mBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //不支持蓝牙
            return;
        }
        //如果没有打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        mList.clear();
        //得到已经配对的列表
        Set<BluetoothDevice> mSet = mBluetoothAdapter.getBondedDevices();
        final Iterator mIterator = mSet.iterator();
        while (mIterator.hasNext()) {
            BluetoothDevice mBluetoothDevice = (BluetoothDevice) mIterator.next();
            mList.add(mBluetoothDevice);
        }
        adapter.addData(mList);
        adapter.notifyDataSetChanged();
    }

    private HPRTHelper mHelper;
    public void initHelper(){
        if (mHelper == null) {
            mHelper = HPRTHelper.getHPRTHelper(mContext);
            //-----是否第一次使用
            boolean b = SPUtils.getBoolean(ConstantUtils.Sp.FIRST_USE_BLUETOOTH);
            if (!b) {
                NormalDialog dialog = new NormalDialog(mContext);
                dialog.title("须知")
                        .btnNum(1)
                        .btnText("确定")
                        .content("第一次使用蓝牙扫描枪，修改了一些默认设置，蓝牙扫描机会自动关机；并且手机需要先取消配对再重新配对后才能使用！！！")
                        .show();
                dialog.setOnBtnClickL(new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        //第14个(自动连续扫描模式):0.关闭；1.开启  ； 第15个(声音)：0.关闭；1.开启
                        mHelper.setSetting(new byte[]{
                                2, 13, 5, 1, 2, 1, 5, 1,
                                0, 50, 1, 15, 10, 0, 1, 106,});
                        mHelper.setWorkModel(1);//1:GATT; 2:HID(备注;修改后蓝牙扫描机会自动关机，并且要重新匹配)
                        SPUtils.setBoolean(ConstantUtils.Sp.FIRST_USE_BLUETOOTH, true);
                    }
                });
            } else {
                //第14个(自动连续扫描模式):0.关闭；1.开启  ； 第15个(声音)：0.关闭；1.开启
                mHelper.setSetting(new byte[]{
                        2, 13, 5, 1, 2, 1, 5, 1,
                        0, 50, 1, 15, 10, 0, 1, 106,});
                mHelper.setWorkModel(1);//1:GATT; 2:HID(备注;修改后蓝牙扫描机会自动关机，并且要重新匹配)
            }
            //-----扫描回调
            mHelper.getGattData(new HPRTHelper.onGattdata() {
                @Override
                public void getdata(byte[] data) {
                    String result = new String(data);
                    if (!MyStringUtil.isEmpty(result)) {
                        //扫描结果结尾默认带"\n"
                        if (result.endsWith("\n")) {
                            result = result.replace("\n", "");
                        }
                    }
                    if(listener!=null){
                        listener.onSuccessListener(result);
                    }
                }
            });
        }
    }

    /**
     * 关闭蓝牙扫描枪连接
     */
    public void closeBluetooth() {
        if (mHelper != null) {
            mHelper.disconnect(new HPRTHelper.onDisconnect() {
                @Override
                public void succeed() {
                    XLog.e("关闭蓝牙扫描枪连接成功");
                }
            });
        }
    }

    @Override
    public void setUiBeforShow() {
        mLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface OnSuccessListener{
        void onSuccessListener(String result);
    }

    private OnSuccessListener listener;
    public void setOnClickListener(OnSuccessListener listener){
        this.listener = listener;
    }
}
