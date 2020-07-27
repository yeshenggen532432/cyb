package com.qwb.view.order.adapter;

import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.member.model.MemberListBean;
import com.chiyong.t3.R;

/**
 * 员工
 * 布局公共：x_adapter_object_common
 */
public class MemberAdapter extends BaseQuickAdapter<MemberListBean.MemberBean, BaseViewHolder> {

    public MemberAdapter() {
        super(R.layout.x_adapter_object_common);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberListBean.MemberBean item) {
        TextView tvName = helper.getView(R.id.item_tv_name);
        TextView tvTel = helper.getView(R.id.item_tv_tel);

        tvName.setText(item.getMemberNm());
        tvTel.setText(item.getMemberMobile());

    }
}
