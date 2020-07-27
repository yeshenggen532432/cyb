package com.qwb.view.plan.adapter;


import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.plan.model.PlanLineBean;
import com.chiyong.t3.R;

/**
 * 计划拜访-线路
 */
public class PlanLineAdapter extends BaseQuickAdapter<PlanLineBean,BaseViewHolder> {

    public PlanLineAdapter() {
        super(R.layout.x_adapter_plan_line);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PlanLineBean item) {
        //点击事件
        helper.addOnClickListener(R.id.tv_plan_add);
        helper.addOnClickListener(R.id.tv_plan_update);
        helper.addOnClickListener(R.id.cb);
        helper.addOnClickListener(R.id.tv_count);

        //赋值
        TextView tv_khNm = helper.getView(R.id.tv_khNm);
        TextView tv_count = helper.getView(R.id.tv_count);
        final CheckBox cb = helper.getView(R.id.cb);
        cb.setVisibility(View.VISIBLE);

        tv_khNm.setText(item.getXlNm());
        tv_count.setText("共" + item.getNum() + "家客户");

        if(item.getCheck()){
            cb.setChecked(true);
        }else{
            cb.setChecked(false);
        }

    }
}
