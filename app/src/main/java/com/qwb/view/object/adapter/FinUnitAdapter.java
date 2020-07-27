package com.qwb.view.object.adapter;


import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.object.model.FinUnitListBean;
import com.chiyong.t3.R;

/**
 * 其他往来单位
 * 布局公用：x_adapter_object_common
 */
public class FinUnitAdapter extends BaseQuickAdapter<FinUnitListBean.FinUnitBean, BaseViewHolder> {

    public FinUnitAdapter() {
        super(R.layout.x_adapter_object_common);
    }

    @Override
    protected void convert(BaseViewHolder helper, FinUnitListBean.FinUnitBean item) {
        TextView tvName = helper.getView(R.id.item_tv_name);
        TextView tvTel = helper.getView(R.id.item_tv_tel);

        tvName.setText(item.getProName());
        tvTel.setText(item.getMobile());

    }
}
