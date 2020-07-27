package com.qwb.view.tab.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.tab.model.HotShopBean;
import com.qwb.utils.MyGlideUtil;
import com.chiyong.t3.R;

/**
 * 热门商家
 */
public class HotShopAdapter extends BaseQuickAdapter<HotShopBean,BaseViewHolder> {

    private Context context;
    public HotShopAdapter(Context context) {
        super(R.layout.x_adapter_hot_shop);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotShopBean item) {
        helper.addOnClickListener(R.id.sb_to_shop);
        helper.addOnClickListener(R.id.right_tv_gz);

        ImageView ivLogo = helper.getView(R.id.iv_logo);
        helper.setText(R.id.tv_company_name, item.getName());

        MyGlideUtil.getInstance().displayImageSquere(item.getLogoUrl(), ivLogo, false);
    }
}
