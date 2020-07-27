package com.qwb.view.table.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.view.table.model.TableModel;
import com.chiyong.t3.R;

/**
 * 销售订单统计表
 */

public class Table4LeftAdapter extends AbsCommonAdapter<TableModel> {

    public Table4LeftAdapter(Context context){
        super(context, R.layout.x_table_left_item);
    }

    @Override
    public void convert(AbsViewHolder helper, TableModel item, int pos) {
        TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
        View tv_table_content_left_line = helper.getView(R.id.tv_table_content_left_line);
        tv_table_content_left.setText(item.getLeftTitle());

        //首先第一个可见；本个与上一个比较，如果相同隐藏，不相同显示（orderIds以这个字段区分）
        if (pos == 0) {
            tv_table_content_left.setVisibility(View.VISIBLE);
        } else {
            TableModel itemUp = getDatas().get(pos - 1);
            String codeUp = itemUp.getOrgCode();
            String code = item.getOrgCode();
            if (codeUp.equals(code)) {
                tv_table_content_left.setVisibility(View.INVISIBLE);
                tv_table_content_left_line.setVisibility(View.INVISIBLE);
            } else {
                tv_table_content_left.setVisibility(View.VISIBLE);
                tv_table_content_left_line.setVisibility(View.VISIBLE);
            }
        }
    }
}
