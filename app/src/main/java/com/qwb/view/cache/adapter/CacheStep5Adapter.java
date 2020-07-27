package com.qwb.view.cache.adapter;



import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.db.DStep5Bean;
import com.chiyong.t3.R;

import cn.droidlover.xdroidmvp.log.XLog;

/**
 * 文 件 名: 缓存步骤5（下单）
 * 修改时间：
 * 修改备注：
 */
public class CacheStep5Adapter extends BaseQuickAdapter<DStep5Bean,BaseViewHolder> {

    public CacheStep5Adapter() {
        super(R.layout.x_adapter_cache_step5);
    }

    @Override
    protected void convert(BaseViewHolder helper, DStep5Bean item) {
        helper.addOnClickListener(R.id.item_layout_sd);
        helper.addOnClickListener(R.id.item_layout_no_send);

        helper.setText(R.id.item_tv_time, item.getTime());
        ImageView ivSd = helper.getView(R.id.item_iv_sd);
//        View layoutSd = helper.getView(R.id.item_layout_sd);
        View layoutNoSend = helper.getView(R.id.item_layout_no_send);
        XLog.e("item", "type；"+item.getType());
        XLog.e("item", "autoType；"+item.getAutoType());

        int autoType = item.getAutoType();
        // 1：拜访下单(添加或修改) 2:电话下单(添加) 3：订货下单列表（查看或修改）4：退货(添加或修改) 5：退货下单列表（查看或修改）
        String type = item.getType();
        switch (type){
            case "1":
                layoutNoSend.setVisibility(View.GONE);
                helper.setText(R.id.item_tv_khNm, item.getKhNm());
                break;
            case "2":
            case "3":
                if(1 == autoType){
                    layoutNoSend.setVisibility(View.VISIBLE);
                }else{
                    layoutNoSend.setVisibility(View.GONE);
                }
                ivSd.setImageResource(R.mipmap.ic_delete_gray_666);
                helper.setText(R.id.item_tv_khNm, item.getKhNm() + "(订货单)");
                break;
            case "4":
            case "5":
                if(1 == autoType){
                    layoutNoSend.setVisibility(View.VISIBLE);
                }else{
                    layoutNoSend.setVisibility(View.GONE);
                }
                ivSd.setImageResource(R.mipmap.ic_delete_gray_666);
                helper.setText(R.id.item_tv_khNm, item.getKhNm() + "(退货单)");
                break;
        }


    }
}
