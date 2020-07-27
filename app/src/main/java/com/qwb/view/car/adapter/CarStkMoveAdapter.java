package com.qwb.view.car.adapter;

import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.car.model.StkMoveBean;
import com.chiyong.t3.R;

/**
 * 移库
 */
public class CarStkMoveAdapter extends BaseQuickAdapter<StkMoveBean, BaseViewHolder> {

    public CarStkMoveAdapter() {
        super(R.layout.x_adapter_car_stk_move);
    }

    @Override
    protected void convert(BaseViewHolder helper, StkMoveBean item) {
        helper.addOnClickListener(R.id.tv_audit);
        helper.addOnClickListener(R.id.tv_cancel);
        helper.addOnClickListener(R.id.content);

        TextView tvOrderNo = helper.getView(R.id.item_tv_order_no);
        TextView tvTime = helper.getView(R.id.item_tv_time);
        TextView tvStkOutName = helper.getView(R.id.item_tv_stk_out);
        TextView tvStkInName = helper.getView(R.id.item_tv_stk_in);
        TextView tvMemberName = helper.getView(R.id.item_tv_name);
        TextView tvStatus = helper.getView(R.id.item_tv_status);
        TextView tvAudit = helper.getView(R.id.tv_audit);
        TextView tvCancel = helper.getView(R.id.tv_cancel);

        tvOrderNo.setText("订单号:" + item.getBillNo());
        tvTime.setText("日期:" + item.getInDate());
        tvStkOutName.setText("出库仓库:" + item.getStkName());
        tvStkInName.setText("车销仓库:" + item.getStkInName());
        tvMemberName.setText("创建人:" + item.getOperator());

        Integer status = item.getStatus();
        if(status == null || status == 0 || status == -2){
            tvStatus.setText("状态:暂存");
            tvAudit.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
        }else if(status !=null && status==1){
            tvStatus.setText("状态:已审批");
            tvAudit.setVisibility(View.GONE);
            tvCancel.setVisibility(View.VISIBLE);
        }else {
            tvStatus.setText("状态:已作废");
            tvAudit.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
        }

    }
}
