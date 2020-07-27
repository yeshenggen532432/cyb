package com.qwb.view.company.adapter;


import android.content.Context;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.company.model.CompanysBean;
import com.chiyong.t3.R;

/**
 * 文 件 名: 我的单位
 * 创建日期:
 * 修改时间：
 * 修改备注：
 */
public class MyCompanyAdapter extends BaseQuickAdapter<CompanysBean, BaseViewHolder> {

    private Context context;

    public MyCompanyAdapter(Context context) {
        super(R.layout.x_adapter_my_company);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CompanysBean item) {

        TextView tvName = helper.getView(R.id.item_tv_name);
        tvName.setText(item.getCompanyName());

    }


}
