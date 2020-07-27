package com.qwb.view.plan.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.plan.model.PlanSubBean;
import com.chiyong.t3.R;

/**
 * 文 件 名: 计划拜访(下属)
 */
public class PlanUnderlingSubAdapter extends BaseQuickAdapter<PlanSubBean, BaseViewHolder> {

    public PlanUnderlingSubAdapter() {
        super(R.layout.x_adapter_plan_underling_sub);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlanSubBean item) {
        //点击事件
//        helper.addOnClickListener(R.id.tv_state);

        //赋值
        TextView tvKhNm = helper.getView(R.id.item_tv_khNm);
        TextView tvAddress = helper.getView(R.id.item_tv_address);
        TextView tvWwc = helper.getView(R.id.item_tv_wwc);
        ImageView ivWc = helper.getView(R.id.item_iv_wc);
        tvKhNm.setText(item.getKhNm());
        tvAddress.setText(item.getAddress());

        int isWc = item.getIsWc();
        if(1 == isWc){
            ivWc.setVisibility(View.VISIBLE);
            tvWwc.setVisibility(View.GONE);
        }else{
            ivWc.setVisibility(View.GONE);
            tvWwc.setVisibility(View.VISIBLE);
        }


    }

}
