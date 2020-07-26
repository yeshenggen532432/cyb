package com.qwb.view.storehouse.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.storehouse.model.StorehouseStayOutBean;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;

/**
 * 待出仓发票
 */
public class StorehouseStayOutAdapter extends BaseQuickAdapter<StorehouseStayOutBean, BaseViewHolder> {

    public StorehouseStayOutAdapter() {
        super(R.layout.x_adapter_storehouse_stay_in);
    }

    @Override
    protected void convert(BaseViewHolder helper, StorehouseStayOutBean item) {
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvOrderNo = helper.getView(R.id.tv_order_no);
        TextView tvInType = helper.getView(R.id.tv_in_type);
        TextView tvMemberName = helper.getView(R.id.tv_member_name);
        TextView tvTotalAmt = helper.getView(R.id.tv_total_amt);
        TextView tvDisAmt = helper.getView(R.id.tv_dis_amt);
        TextView tvStatus = helper.getView(R.id.tv_status);

        try {
            tvName.setText(item.getKhNm());
            tvTime.setText("日期:" + item.getOutDate());
            tvOrderNo.setText("单号:" + item.getBillNo());
            tvInType.setText(item.getOutType());
            tvMemberName.setText(item.getStaff());
            tvTotalAmt.setText("" + item.getTotalAmt());
            tvDisAmt.setText("" + item.getDisAmt());
            tvStatus.setText("状态:" + item.getBillStatus());
        }catch (Exception e){
            ToastUtils.showError(e);
        }

    }
}
