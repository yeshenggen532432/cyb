package com.qwb.view.map.adapter;


import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.db.LocationBean;
import com.qwb.utils.MyUtils;
import com.xmsx.qiweibao.R;

/**
 * 文 件 名: 轨迹回放
 */
public class TrackListAdapter extends BaseQuickAdapter<LocationBean,BaseViewHolder> {

    private Context context;
    public TrackListAdapter(Context context) {
        super(R.layout.x_adapter_track_map);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, LocationBean item) {
        TextView tv_time=helper.getView(R.id.tv_time);
        TextView tv_ceshi=helper.getView(R.id.tv_ceshi);
        TextView tv_ceshi2=helper.getView(R.id.tv_ceshi2);
        TextView tv_address=helper.getView(R.id.tv_address);
        if (item != null) {
            tv_time.setText(String.valueOf(MyUtils.getStrFromTime(item.getLocation_time())));
            tv_ceshi.setText(item.getOs());
            tv_ceshi2.setText(item.getLocation_from());
            tv_address.setText(item.getAddress());
        }
    }
}
