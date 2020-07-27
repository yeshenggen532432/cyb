package com.qwb.view.delivery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qwb.view.delivery.model.DeliverySubBean;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyColorUtil;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */

public class DeliveryLeftAdapter extends BaseAdapter{
    private Map<Integer, Integer> repeatMap = new HashMap<>();
    private Context mContext;
    private ArrayList<DeliverySubBean> mDatas = new ArrayList<>();

    public DeliveryLeftAdapter(Context context, ArrayList<DeliverySubBean> datas){
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View helper = LayoutInflater.from(mContext).inflate(R.layout.x_delivery_table_left_item,null);
        TextView tvWareNm = helper.findViewById(R.id.tv_table_content_item_left);
        try{
            DeliverySubBean data = mDatas.get(position);

            tvWareNm.setText(data.getWareNm());
            //商品重复的颜色变红
            Integer repeat = repeatMap.get(data.getWareId());
            if(null != repeat && repeat > 1){
                tvWareNm.setTextColor(MyColorUtil.getColorResId(R.color.blue));
            }else{
                tvWareNm.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return helper;
    }

    public Map<Integer, Integer> getRepeatMap() {
        return repeatMap;
    }

    public void setRepeatMap(Map<Integer, Integer> repeatMap) {
        this.repeatMap = repeatMap;
    }
}
