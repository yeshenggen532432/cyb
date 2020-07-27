package com.qwb.view.plan.adapter;


import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.plan.model.PlanLineDetailBean;
import com.chiyong.t3.R;

/**
 * 文 件 名: 计划拜访(下属)
 */
public class PlanUnderRightSubAdapter extends BaseQuickAdapter<PlanLineDetailBean, BaseViewHolder> {

    public PlanUnderRightSubAdapter() {
        super(R.layout.x_adapter_plan_under_right_sub);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlanLineDetailBean item) {
        //点击事件
//        helper.addOnClickListener(R.id.tv_state);

        //赋值
        TextView tvKhNm = helper.getView(R.id.item_tv_khNm);
        tvKhNm.setText(item.getKhNm());



    }

}
