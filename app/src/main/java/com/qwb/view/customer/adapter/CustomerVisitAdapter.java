package com.qwb.view.customer.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.customer.model.CustomerVisitBean;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;

/**
 * 客户拜访(我的拜访)
 */
public class CustomerVisitAdapter extends BaseQuickAdapter<CustomerVisitBean, BaseViewHolder> {

    public CustomerVisitAdapter() {
        super(R.layout.x_adapter_customer_visit);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomerVisitBean item) {
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvStartTime = helper.getView(R.id.tv_start_time);
        TextView tvEndTime = helper.getView(R.id.tv_end_time);
        TextView tvZtTime = helper.getView(R.id.tv_zt_time);
        TextView tvVisitTime = helper.getView(R.id.tv_visit_time);
        TextView tvAddress = helper.getView(R.id.tv_address);

        try {
            tvName.setText(item.getKhNm());
            tvStartTime.setText(item.getQddate());
            tvEndTime.setText(item.getKhNm());
//            tvZtTime.setText(item.getKhNm());
            tvVisitTime.setText(item.getQdtime());
            tvAddress.setText(item.getAddress());
        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

}
