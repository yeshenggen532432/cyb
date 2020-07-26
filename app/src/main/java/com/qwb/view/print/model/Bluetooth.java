package com.qwb.view.print.model;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ToastUtils;

import io.reactivex.functions.Consumer;


/**
 *
 */
public class Bluetooth {
    private Activity context;
    private static Bluetooth bluetooth;
    private BluetoothAdapter mBluetoothAdapter;
    private toData mTodata;

    private Bluetooth(Activity context) {
        this.context = context;
    }

    public static Bluetooth getBluetooth(Activity context) {
        bluetooth = new Bluetooth(context);
        return bluetooth;
    }

    private void registerBroadcast() {
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);// 用BroadcastReceiver来取得搜索结果
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intent.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, intent);
    }

    public void doDiscovery() {
        if (context != null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                return;
            } else if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
            }
            registerBroadcast();
            RxPermissions rxPermissions = new RxPermissions(context);
            rxPermissions
                    .request(Manifest.permission.BLUETOOTH, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                if (null == mBluetoothAdapter) {
                                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                                }
                                if (!mBluetoothAdapter.isEnabled()) {
                                    mBluetoothAdapter.enable();
                                }
                                if (mBluetoothAdapter.isDiscovering()) {
                                    mBluetoothAdapter.cancelDiscovery();
                                }
                                mBluetoothAdapter.startDiscovery();
                            } else {
                                ToastUtils.showLongCustomToast("请在手机设置的权限管理中勾选应用所需要的动态权限，不然某些功能不能使用");
                            }
                        }
                    });
//            rxPermissions = new RxPermissions((Activity)context);
//            rxPermissions.request(Manifest.permission.BLUETOOTH_ADMIN,
//                    Manifest.permission.BLUETOOTH,
//                    Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
//                @Override
//                public void call(Boolean aBoolean) {
//                    if (aBoolean) {
//                    } else {
//                        Utility.show(context,"no bluetooth permission");
//                    }
//                }
//            });
        }
    }

    public void getData(toData todata) {
        mTodata = todata;
    }

    public interface toData {
        public void succeed(String BTname, String BTmac);
    }

    public void disReceiver() {
        if (mReceiver != null && context != null)
            context.unregisterReceiver(mReceiver);
        if (mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.cancelDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = null;
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBluetoothClass().getMajorDeviceClass() == 1536) {
                        if (mTodata != null) {
                            mTodata.succeed(TextUtils.isEmpty(device.getName()) ? "UnKnown" : device.getName(), device.getAddress());
                        }
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_BONDING:
                            Log.d("Print", "正在配对......");
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            Log.d("Print", "完成配对");
                            break;
                        case BluetoothDevice.BOND_NONE:
                            Log.d("Print", "取消配对");
                        default:
                            break;
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d("Print", "搜索完成");
                    break;
            }
        }
    };
}
