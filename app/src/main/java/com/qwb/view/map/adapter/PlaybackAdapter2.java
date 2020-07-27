package com.qwb.view.map.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.flow.model.FlowBfListBean;
import com.chiyong.t3.R;

/**
 * 文 件 名: 拜访回放
 * 修改时间：
 * 修改备注：
 */
public class PlaybackAdapter2 extends BaseQuickAdapter<FlowBfListBean.FlowBfBean,BaseViewHolder> {

    public PlaybackAdapter2() {
        super(R.layout.x_adapter_play_back);
    }

    @Override
    protected void convert(BaseViewHolder helper, FlowBfListBean.FlowBfBean item) {
        TextView tv_xuhao=helper.getView(R.id.tv_xuhao);
        TextView tv_khNm=helper.getView(R.id.tv_khNm);
        TextView tv_startTime=helper.getView(R.id.tv_startTime);
        TextView tv_endTime=helper.getView(R.id.tv_endTime);
        TextView tv_address=helper.getView(R.id.tv_address);

        TextView tv_start_label=helper.getView(R.id.tv_start_label);

        int position = helper.getAdapterPosition();
        tv_xuhao.setText("" + (position + 1));
        tv_startTime.setText(item.getSignTime());
        tv_address.setText(item.getAddress());

        helper.getView(R.id.layout_khNm).setVisibility(View.GONE);
        helper.getView(R.id.layout_edate).setVisibility(View.GONE);

        String signType = item.getSignType();
        if("1".equals(signType)){
            tv_start_label.setText("上班：");
        }else if("2".equals(signType)){
            tv_start_label.setText("下班：");
        }else if("3".equals(signType)){
            tv_start_label.setText("打卡：");
        }

    }

}
