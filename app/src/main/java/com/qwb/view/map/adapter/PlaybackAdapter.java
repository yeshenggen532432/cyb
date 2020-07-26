package com.qwb.view.map.adapter;


import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.map.model.QueryBflsmwebBean;
import com.xmsx.qiweibao.R;

/**
 * 员工在线-拜访回放
 */
public class PlaybackAdapter extends BaseQuickAdapter<QueryBflsmwebBean.QueryBfHf,BaseViewHolder> {

    public PlaybackAdapter() {
        super(R.layout.x_adapter_play_back);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryBflsmwebBean.QueryBfHf item) {
        TextView tv_xuhao=helper.getView(R.id.tv_xuhao);
        TextView tv_khNm=helper.getView(R.id.tv_khNm);
        TextView tv_startTime=helper.getView(R.id.tv_startTime);
        TextView tv_endTime=helper.getView(R.id.tv_endTime);
        TextView tv_address=helper.getView(R.id.tv_address);

        tv_xuhao.setText(String.valueOf(item.getXhNm()));
        tv_khNm.setText(item.getKhNm());
        tv_startTime.setText(item.getTime1());
        tv_endTime.setText(item.getTime2());
        tv_address.setText(item.getAddress());

    }

}
