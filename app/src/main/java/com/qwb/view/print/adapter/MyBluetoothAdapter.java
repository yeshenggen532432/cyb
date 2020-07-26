package com.qwb.view.print.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bluetooth.prt.HPRTHelper;
import com.example.bluetooth.prt.HPRTHelper.onConnect;
import com.example.bluetooth.prt.HidConncetUtil;
import com.xmsx.qiweibao.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙打印机
 */
public class MyBluetoothAdapter extends BaseAdapter {
    private List<BluetoothDevice> mList = new ArrayList<>();
    private Context mContext;
    private HidConncetUtil mHidConncetUtil;
	private HPRTHelper mHprt;
    public MyBluetoothAdapter(Context mContext) {
        this.mContext = mContext;
        //4.0以上才支持HID模式
        if (Build.VERSION.SDK_INT >= 14) {
            this.mHidConncetUtil = new HidConncetUtil(mContext);
        	mHprt = HPRTHelper.getHPRTHelper(mContext);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addData(List<BluetoothDevice> bluetoothDevice){
    	mList.clear();
        mList.addAll(bluetoothDevice);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = View.inflate(mContext, R.layout.x_adapter_bluetooth,null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initData(position,viewHolder);
        return convertView;
    }

    private void initData(int position,ViewHolder viewHolder) {
        final BluetoothDevice bluetoothDevice = mList.get(position);
        viewHolder.bluehandlename.setText(bluetoothDevice.getName());
        viewHolder.bluehandlebond.setText(bluetoothDevice.getAddress());
        viewHolder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	mHprt.setDevice(bluetoothDevice);
            	mHprt.buleconnect(mHidConncetUtil,mContext,new onConnect() {
					@Override
					public void succeed() {
						Toast.makeText(mContext, "连接成功", Toast.LENGTH_LONG).show();
                        if(null != listener){
                            listener.onSuccessListener();
                        }
					}
					
					@Override
					public void failure() {
						Toast.makeText(mContext, "连接失败",  Toast.LENGTH_LONG).show();
					}
				});
            }
        });
    }

    public class ViewHolder {
        public final TextView bluehandlename;
        public final TextView bluehandlebond;
        public final Button connect;
        public final View root;

        public ViewHolder(View root) {
            bluehandlename =  root.findViewById(R.id.blue_handle_name);
            bluehandlebond =  root.findViewById(R.id.blue_handle_bond);
            connect =  root.findViewById(R.id.connect);
            this.root = root;
        }
    }

    public interface OnSuccessListener{
        void onSuccessListener();
    }

    private OnSuccessListener listener;
    public void setOnSuccessListener(OnSuccessListener listener){
        this.listener = listener;
    }

}
