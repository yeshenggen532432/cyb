package com.qwb.view.print.util;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.print.model.Bluetooth;
import com.qwb.view.print.model.BluetoothBean;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙连接对话框
 */

public class MyBluetoothDialog extends BaseDialog<MyBluetoothDialog> {
    private Activity activity;

    public MyBluetoothDialog(Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(activity, R.layout.x_dialog_my_bluetooth, null);
        mRecyclerView = inflate.findViewById(R.id.recyclerView);

        initAdapter();
        return inflate;
    }

    /**
     * 初始化适配器
     */
    RecyclerView mRecyclerView;
    BluetoothAdapter mAdapter;
    private List<BluetoothBean> list = new ArrayList<>();
    public android.bluetooth.BluetoothAdapter myBluetoothAdapter;
    private Bluetooth bluetooth;
    private void initAdapter() {
        //蓝牙连接之前判断：是否打开蓝牙等等
        if ((myBluetoothAdapter = android.bluetooth.BluetoothAdapter.getDefaultAdapter()) == null) {
            ToastUtils.showCustomToast("没有找到蓝牙适配器");
            return;
        }
        if (!myBluetoothAdapter.isEnabled()) {
//            Intent intent = new Intent(android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            activity.startActivityForResult(intent, 2);
        }

        //适配器
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new BluetoothAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                try {
                    String mac = list.get(position).getBTmac();
                    dismiss();
                    connect(mac);
                } catch (Exception e) {
                }
            }
        });

        //获取蓝牙数据
        bluetooth = Bluetooth.getBluetooth(activity);
        list.clear();
        mAdapter.setNewData(list);
        mAdapter.notifyDataSetChanged();
        bluetooth.doDiscovery();
        bluetooth.getData(new Bluetooth.toData() {
            @Override
            public void succeed(String name, String mac) {
                for (BluetoothBean printBT : list) {
                    if (name.equals(printBT.getBTmac())) {
                        return;
                    }
                }
                BluetoothBean printBT = new BluetoothBean();
                printBT.setBTname(name);
                printBT.setBTmac(mac);
                list.add(printBT);
                mAdapter.setNewData(list);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void setUiBeforShow() {
    }


    public interface OnSuccessConnectListener{
        void setOnSuccessConnectListener(String text);
    }

    private OnSuccessConnectListener listener;
    public void setOnSuccessConnectListener(OnSuccessConnectListener listener){
        this.listener = listener;
    }


    //连接设备
    private void connect(final String mac) {
        if (MyStringUtil.isEmpty(mac)){
            return;
        }
        if (listener != null) {
            listener.setOnSuccessConnectListener(mac);
        }

//        final int result;
//        try {
//            result = PrinterHelper.PortOpenBT(mac);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (result==0){
//                        ToastUtils.showCustomToast("蓝牙连接成功");
//                        dismiss();
//
//                    }else{
//                        ToastUtils.showCustomToast("蓝牙连接失败");
//                    }
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }



    //蓝牙适配器
    public class BluetoothAdapter extends BaseQuickAdapter<BluetoothBean, BaseViewHolder>{

        public BluetoothAdapter() {
            super(R.layout.x_adapter_my_bluetooth);
        }

        @Override
        protected void convert(BaseViewHolder helper, BluetoothBean item) {
            helper.setText(R.id.item_tv_name, item.getBTname());
            helper.setText(R.id.item_tv_mac, item.getBTmac());

        }
    }





}