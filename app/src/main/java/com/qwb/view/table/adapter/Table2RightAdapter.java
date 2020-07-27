package com.qwb.view.table.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.view.table.model.TableModel;
import com.chiyong.t3.R;

/**
 * 客户拜访统计表
 */

public class Table2RightAdapter extends AbsCommonAdapter<TableModel> {
    private Activity mContext;

    public Table2RightAdapter(Activity context){
        super(context, R.layout.x_table_right_item2);
        this.mContext = context;
    }

    @Override
    public void convert(AbsViewHolder helper, final TableModel item, int pos) {
        TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
        TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
        TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);

        tv_table_content_right_item0.setText(item.getText0());
        tv_table_content_right_item1.setText(item.getText1());
        tv_table_content_right_item2.setText(item.getText2());
    }
}
