package com.qwb.view.cache.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.db.DStep4Bean;
import com.xmsx.qiweibao.R;

/**
 * 文 件 名: 缓存步骤4
 * 修改时间：
 * 修改备注：
 */
public class CacheStep4Adapter extends BaseQuickAdapter<DStep4Bean,BaseViewHolder> {

    public CacheStep4Adapter() {
        super(R.layout.x_adapter_cache_step4);
    }

    @Override
    protected void convert(BaseViewHolder helper, DStep4Bean item) {
        helper.addOnClickListener(R.id.item_layout_sd);
//        helper.addOnClickListener(R.id.layout_left);
        helper.setText(R.id.item_tv_khNm, item.getKhNm());
        helper.setText(R.id.item_tv_time, item.getTime());

    }
}
