package com.qwb.view.table.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chiyong.t3.R;

/**
 * 文 件 名: TableAdapter(统计报表)
 * 创 建 人: yeshenggen
 * 创建日期: 2018-05-07
 * 修改时间：
 * 修改备注：
 */
public class TableAdapter extends BaseQuickAdapter<String,BaseViewHolder> {

    public TableAdapter() {
        super(R.layout.x_adapter_table);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_title, item);
    }
}
