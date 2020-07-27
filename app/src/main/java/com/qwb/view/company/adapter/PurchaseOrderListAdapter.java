package com.qwb.view.company.adapter;

import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.company.model.PurchaseOrderBean;
import com.chiyong.t3.R;

/**
 * 采购单列表
 */
public class PurchaseOrderListAdapter extends BaseQuickAdapter<PurchaseOrderBean, BaseViewHolder> {

    public PurchaseOrderListAdapter() {
        super(R.layout.x_adapter_purchase_order_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, PurchaseOrderBean item) {

        TextView tvProName = helper.getView(R.id.item_tv_proName);//
        TextView tvBillNo = helper.getView(R.id.item_tv_billNo);//
        TextView tvTime = helper.getView(R.id.item_tv_time);//
        TextView tvInType = helper.getView(R.id.item_tv_inType);//
        TextView tvTotalAmt = helper.getView(R.id.item_tv_totalAmt);//
        TextView tvDiscount = helper.getView(R.id.item_tv_discount);//
        TextView tvDisAmt = helper.getView(R.id.item_tv_disAmt);//
        TextView tvPayAmt = helper.getView(R.id.item_tv_payAmt);//
        TextView tvFreeAmt = helper.getView(R.id.item_tv_freeAmt);//
        TextView tvStatus = helper.getView(R.id.item_tv_status);//

        tvProName.setText(item.getProName());//
        tvBillNo.setText(item.getBillNo());//
        tvTime.setText( item.getInDate());//
        tvInType.setText(item.getInType());//
        tvDiscount.setText("" + item.getDiscount());//整单折扣
        tvTotalAmt.setText("合计金额:" + item.getTotalAmt());//合计金额
        tvDisAmt.setText("" + item.getDisAmt());//发票金额
        tvPayAmt.setText("已付金额:" + item.getPayAmt());//已付金额
        tvFreeAmt.setText("核销金额:" + item.getFreeAmt());//核销金额
        tvStatus.setText("发票状态:" + item.getBillStatus());//发票状态

//              <div data-field="submitUser" qwb-options="width:100,tooltip: true">审核人</div>
//                <div data-field="cancelUser" qwb-options="width:100,tooltip: true">作废人</div>


    }
}
