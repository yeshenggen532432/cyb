package com.qwb.view.table.adapter;

import android.app.Activity;
import android.widget.TextView;

import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.view.table.model.TableModel;
import com.xmsx.qiweibao.R;

/**
 * 产品统计表
 */

public class Table3RightAdapter extends AbsCommonAdapter<TableModel> {
    private Activity mContext;

    public Table3RightAdapter(Activity context){
        super(context, R.layout.x_table_right_item3);
        this.mContext = context;
    }

    @Override
    public void convert(AbsViewHolder helper, final TableModel item, int pos) {
        TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
        TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
        TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
        TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);
        TextView tv_table_content_right_item4 = helper.getView(R.id.tv_table_content_right_item4);

        tv_table_content_right_item0.setText(item.getText0());
        tv_table_content_right_item1.setText(item.getText1());
        tv_table_content_right_item2.setText(item.getText2());
        tv_table_content_right_item3.setText(item.getText3());
        tv_table_content_right_item4.setText(item.getText4());
    }
}
