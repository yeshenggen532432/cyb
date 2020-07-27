package com.qwb.view.storehouse.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.storehouse.model.StorehouseInBean;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;

/**
 * 整理单列表
 */
public class StorehouseArrangeListAdapter extends BaseQuickAdapter<StorehouseInBean, BaseViewHolder> {

    public StorehouseArrangeListAdapter() {
        super(R.layout.x_adapter_storehouse_arrange);
    }

    @Override
    protected void convert(BaseViewHolder helper, StorehouseInBean item) {
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvOrderNo = helper.getView(R.id.tv_order_no);
        TextView tvStkName = helper.getView(R.id.tv_stkName);
        TextView tvStatus = helper.getView(R.id.tv_status);

        try {
            tvName.setText(item.getCreateName());
            tvTime.setText("日期:" + item.getInDate());
            tvOrderNo.setText("单号:" + item.getBillNo());
            tvStkName.setText(item.getStkName());
            tvStatus.setText(getStatusStr(item.getStatus()));
        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

    public String getStatusStr(Integer status) {
        String s = "";
        if(status != null){
            if (status == 0 || status == -2) {
                s = "状态：暂存";
            } else if (status == 1) {
                s = "状态：已审批";
            } else {
                s = "状态：已作废";
            }
        }else{
            s = "状态：暂存";
        }
        return s;
    }
}
