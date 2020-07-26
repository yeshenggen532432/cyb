package com.qwb.view.plan.adapter;


import android.widget.TextView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.customer.model.MineClientInfo;
import com.xmsx.qiweibao.R;

/**
 * 计划拜访-编辑线路
 */
public class PlanEditLineAdapter extends BaseItemDraggableAdapter<MineClientInfo, BaseViewHolder> {

    public PlanEditLineAdapter() {
        super(R.layout.x_adapter_plan_edit_line, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineClientInfo item) {
        //点击事件
        helper.addOnClickListener(R.id.item_layout_del);

        //赋值
        TextView tv_khNm = helper.getView(R.id.item_tv_khNm);
        tv_khNm.setText(item.getKhNm());

    }

}
