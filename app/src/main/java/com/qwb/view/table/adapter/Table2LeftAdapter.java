package com.qwb.view.table.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.view.table.model.TableModel;
import com.chiyong.t3.R;

/**
 * 业务拜访统计表
 */
public class Table2LeftAdapter extends AbsCommonAdapter<TableModel> {

    public Table2LeftAdapter(Context context){
        super(context, R.layout.x_table_left_item);
    }

    @Override
    public void convert(AbsViewHolder helper, TableModel item, int pos) {
        TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
        TextView tv_table_content_left_line = helper.getView(R.id.tv_table_content_left_line);
        TextView tv_table_content_left_line2 = helper.getView(R.id.tv_table_content_left_line2);
        tv_table_content_left.setText(item.getLeftTitle());
        tv_table_content_left_line.setVisibility(View.GONE);
        tv_table_content_left_line2.setVisibility(View.VISIBLE);

    }
}
