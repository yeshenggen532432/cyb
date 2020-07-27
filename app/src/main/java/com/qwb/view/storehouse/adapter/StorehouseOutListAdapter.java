package com.qwb.view.storehouse.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.storehouse.model.StorehouseOutBean;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;

/**
 * 出仓单列表
 */
public class StorehouseOutListAdapter extends BaseQuickAdapter<StorehouseOutBean, BaseViewHolder> {

    public StorehouseOutListAdapter() {
        //复用：入仓单列表
        super(R.layout.x_adapter_storehouse_in_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, StorehouseOutBean item) {
        helper.addOnClickListener(R.id.content);
        helper.addOnClickListener(R.id.right_audit);
        helper.addOnClickListener(R.id.right_cancel);

        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvOrderNo = helper.getView(R.id.tv_order_no);
        TextView tvStkName = helper.getView(R.id.tv_stkName);
        TextView tvStatus = helper.getView(R.id.tv_status);

        TextView rightAudit = helper.getView(R.id.right_audit);
        TextView rightCancel = helper.getView(R.id.right_cancel);

        try {
            tvName.setText(item.getCreateName());
            tvTime.setText("日期:" + item.getInDate());
            tvOrderNo.setText("单号:" + item.getBillNo());
            tvStkName.setText(item.getStkName());
            getStatusStr(item.getStatus(), tvStatus, rightAudit, rightCancel);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

    public String getStatusStr(Integer status,TextView tvStatus, TextView rightAudit, TextView rightCancel) {
        String s = "";
        if(status != null){
            if (status == 0 || status == -2) {
                s = "状态：暂存";
                rightAudit.setVisibility(View.VISIBLE);
                rightCancel.setVisibility(View.GONE);
            } else if (status == 1) {
                s = "状态：已审批";
                rightAudit.setVisibility(View.GONE);
                rightCancel.setVisibility(View.GONE);
            } else {
                s = "状态：已作废";
                rightAudit.setVisibility(View.GONE);
                rightCancel.setVisibility(View.GONE);
            }
        }else{
            s = "状态：暂存";
            rightAudit.setVisibility(View.VISIBLE);
            rightCancel.setVisibility(View.GONE);
        }
        tvStatus.setText(s);
        return s;
    }
}
