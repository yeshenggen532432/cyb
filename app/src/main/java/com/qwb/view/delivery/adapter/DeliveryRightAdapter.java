package com.qwb.view.delivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.qwb.view.delivery.model.DeliverySubBean;
import com.chiyong.t3.R;

import java.util.ArrayList;

/**
 *
 */

public class DeliveryRightAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<DeliverySubBean> mDatas = new ArrayList<>();

    public DeliveryRightAdapter(Context context, ArrayList<DeliverySubBean> datas){
        this.mContext = context;
        this.mDatas = datas;

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }



    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View helper = LayoutInflater.from(mContext).inflate(R.layout.x_delivery_table_right_item,null);
        TextView tvGg = helper.findViewById(R.id.tv_table_content_right_item1);
        TextView tvDw = helper.findViewById(R.id.tv_table_content_right_item2);
        EditText etCount = helper.findViewById(R.id.tv_table_content_right_item3);
        EditText etPrice = helper.findViewById(R.id.tv_table_content_right_item4);
        TextView tvZj = helper.findViewById(R.id.tv_table_content_right_item5);

        DeliverySubBean item = mDatas.get(position);

        tvGg.setText(item.getWareGg());
        tvDw.setText(item.getUnitName());
        etCount.setText("" + item.getQty());
        etPrice.setText("" + item.getPrice());
        tvZj.setText("" + item.getAmt());


        return helper;
    }

}
