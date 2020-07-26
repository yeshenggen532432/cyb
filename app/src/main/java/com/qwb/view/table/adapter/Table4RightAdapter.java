package com.qwb.view.table.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.view.table.model.TableModel;
import com.xmsx.qiweibao.R;

/**
 * 销售订单统计表
 */

public class Table4RightAdapter extends AbsCommonAdapter<TableModel> {

    public Table4RightAdapter(Context context){
        super(context, R.layout.x_table_right_item4);
    }

    @Override
    public void convert(AbsViewHolder helper, TableModel item, int pos) {
        TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
        TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
        TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
        TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);
        TextView tv_table_content_right_item4 = helper.getView(R.id.tv_table_content_right_item4);
        TextView tv_table_content_right_item5 = helper.getView(R.id.tv_table_content_right_item5);
        View tv_table_content_right_line = helper.getView(R.id.tv_table_content_right_line);
        View tv_table_content_right_line2 = helper.getView(R.id.tv_table_content_right_line2);

        tv_table_content_right_item0.setText(item.getText0());
        tv_table_content_right_item1.setText(item.getText1());
        tv_table_content_right_item2.setText(item.getText2());
        tv_table_content_right_item3.setText(item.getText3());
        tv_table_content_right_item4.setText(item.getText4());
        tv_table_content_right_item5.setText(item.getText5());

        //首先第一个可见；本个与上一个比较，如果相同隐藏，不相同显示（orderIds以这个字段区分）
        if (pos == 0) {
            tv_table_content_right_item0.setVisibility(View.VISIBLE);
            tv_table_content_right_line2.setVisibility(View.INVISIBLE);
        } else {
            TableModel itemUp = getDatas().get(pos - 1);
            String codeUp = itemUp.getOrgCode();
            String code = item.getOrgCode();
            if (codeUp.equals(code)) {
                tv_table_content_right_item0.setVisibility(View.INVISIBLE);
                tv_table_content_right_line.setVisibility(View.INVISIBLE);
                tv_table_content_right_line2.setVisibility(View.VISIBLE);
            } else {
                tv_table_content_right_item0.setVisibility(View.VISIBLE);
                tv_table_content_right_line.setVisibility(View.VISIBLE);
                tv_table_content_right_line2.setVisibility(View.INVISIBLE);
            }
        }
    }
}
