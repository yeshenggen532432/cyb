package com.qwb.view.object.adapter;


import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.object.model.ProviderListBean;
import com.chiyong.t3.R;

/**
 * 供应商
 * 布局公共：x_adapter_object_common
 */
public class ProviderAdapter extends BaseQuickAdapter<ProviderListBean.ProviderBean, BaseViewHolder> {

    //布局公共：x_adapter_object_common
    public ProviderAdapter() {
        super(R.layout.x_adapter_object_common);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProviderListBean.ProviderBean item) {
        TextView tvName = helper.getView(R.id.item_tv_name);
        TextView tvTel = helper.getView(R.id.item_tv_tel);

        tvName.setText(item.getProName());
        tvTel.setText(item.getMobile());

    }
}
