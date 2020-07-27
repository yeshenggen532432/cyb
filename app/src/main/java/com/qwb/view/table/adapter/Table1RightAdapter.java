package com.qwb.view.table.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.table.model.TableModel;
import com.qwb.view.call.ui.CallRecordActivity;
import com.chiyong.t3.R;

import cn.droidlover.xdroidmvp.router.Router;

/**
 * 销售订单统计表
 */

public class Table1RightAdapter extends AbsCommonAdapter<TableModel> {
    private Activity mContext;

    public Table1RightAdapter(Activity context){
        super(context, R.layout.x_table_right_item1);
        this.mContext = context;
    }

    @Override
    public void convert(AbsViewHolder helper, final TableModel item, int pos) {
        TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
        TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
        TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
        TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);

        tv_table_content_right_item0.setText(item.getText0());
        tv_table_content_right_item1.setText(item.getText1());
        tv_table_content_right_item2.setText(item.getText2());
        tv_table_content_right_item3.setText(item.getText3());

        //跳到拜访记录
        tv_table_content_right_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.newIntent(mContext)
                        .putInt(ConstantUtils.Intent.TYPE, 4)
                        .putInt(ConstantUtils.Intent.ID, Integer.valueOf(item.getText4()))//拜访id
                        .putString(ConstantUtils.Intent.CLIENT_NAME, item.getText3())//客户名称
                        .putString(ConstantUtils.Intent.MEMBER_NAME, item.getLeftTitle())//业务员
                        .to(CallRecordActivity.class)
                        .launch();
            }
        });
    }
}
