package com.qwb.view.work.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.work.model.WorkBean;
import com.chiyong.t3.R;

/**
 * 文 班次列表
 */
public class WorkClassListAdapter extends BaseQuickAdapter<WorkBean,BaseViewHolder> {

    private Context context;
    public WorkClassListAdapter(Context context) {
        super(R.layout.x_adapter_work_list);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkBean item) {
//        //子view添加点击事件（修改地址）
        helper.addOnClickListener(R.id.tv_update);
//
        helper.setText(R.id.tv_bc_name, item.getBcName());
        helper.setText(R.id.tv_areaLong, item.getAreaLong()+"米");
        helper.setText(R.id.tv_address, item.getAddress());

    }
}
