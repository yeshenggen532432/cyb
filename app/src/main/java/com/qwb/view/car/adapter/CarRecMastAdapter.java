package com.qwb.view.car.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deadline.statebutton.StateButton;
import com.qwb.view.car.model.CarRecMastBean;
import com.xmsx.qiweibao.R;

/**
 * 车销收款
 */
public class CarRecMastAdapter extends BaseQuickAdapter<CarRecMastBean, BaseViewHolder> {

    public CarRecMastAdapter() {
        super(R.layout.x_adapter_car_rec_mast);
    }

    @Override
    protected void convert(BaseViewHolder helper, CarRecMastBean item) {
        helper.addOnClickListener(R.id.item_sb_zf).addOnClickListener(R.id.item_sb_qrsk);

        TextView tvSkNo = helper.getView(R.id.item_tv_sk_no);
        TextView tvOrderNo = helper.getView(R.id.item_tv_order_no);
        TextView tvTime = helper.getView(R.id.item_tv_time);
        TextView tvWldw = helper.getView(R.id.item_tv_wldw);
        TextView tvStkName = helper.getView(R.id.item_tv_stk_name);
//        TextView tvRemarks = helper.getView(R.id.item_tv_remarks);
        TextView tvMember = helper.getView(R.id.item_tv_member_name);
        TextView tvStatus = helper.getView(R.id.item_tv_status);
        StateButton sbZf = helper.getView(R.id.item_sb_zf);
        StateButton sbQrsk = helper.getView(R.id.item_sb_qrsk);
        TextView tvMoney=helper.getView(R.id.item_tv_money);

        tvSkNo.setText("收款单号:" + item.getBillNo());
        tvOrderNo.setText("订单号:" + item.getOrderNo());
        tvTime.setText("" + item.getRecTimeStr());//收款时间
        tvWldw.setText("往来单位:" + item.getProName());
        tvStkName.setText("车销仓库:" + item.getStkNm());
//        tvRemarks.setText("备注:" + item.getRemarks());
        tvMember.setText("" + item.getStaff());//车销人员:
        tvMoney.setText("" + item.getSumAmt() + "元");//收款金额:

        int status = item.getStatus();
        if (-2 == status) {
            tvStatus.setText("状态:未收款");
            sbZf.setVisibility(View.VISIBLE);
            sbQrsk.setVisibility(View.VISIBLE);
        } else if (1 == status) {
            tvStatus.setText("状态:已收款");
            sbZf.setVisibility(View.VISIBLE);
            sbQrsk.setVisibility(View.GONE);
        } else if (2 == status) {
            tvStatus.setText("状态:已作废");
            sbZf.setVisibility(View.GONE);
            sbQrsk.setVisibility(View.GONE);
        } else if (3 == status) {
            tvStatus.setText("状态:已入账");
            sbZf.setVisibility(View.GONE);
            sbQrsk.setVisibility(View.GONE);

        }
    }
}
