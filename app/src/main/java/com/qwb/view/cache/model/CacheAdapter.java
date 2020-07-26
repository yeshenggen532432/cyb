package com.qwb.view.cache.model;


import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xmsx.qiweibao.R;

/**
 * 文 件 名: 我的缓存
 * 修改时间：
 * 修改备注：
 */
public class CacheAdapter extends BaseQuickAdapter<MineCacheBean,BaseViewHolder> {

    public CacheAdapter() {
        super(R.layout.x_adapter_mine_cache);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineCacheBean item) {
        helper.addOnClickListener(R.id.layout_right);
        helper.addOnClickListener(R.id.layout_left);

        TextView tvLeft = helper.getView(R.id.tv_left);//渠道类型
        ImageView ivRigth = helper.getView(R.id.iv_right);//距离

        tvLeft.setText(item.getName());
        if(null != item.getImgRes()){
            ivRigth.setImageResource(item.getImgRes());
        }
    }
}
