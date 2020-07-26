package com.qwb.view.cache.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.qwb.db.DStep2Bean;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: 缓存步骤1
 * 修改时间：
 * 修改备注：
 */
public class CacheStep2Adapter extends BaseQuickAdapter<DStep2Bean,BaseViewHolder> {

    public CacheStep2Adapter() {
        super(R.layout.x_adapter_cache_step2);
    }

    @Override
    protected void convert(BaseViewHolder helper, DStep2Bean item) {
        helper.addOnClickListener(R.id.item_layout_sd);
//        helper.addOnClickListener(R.id.layout_left);
        helper.setText(R.id.item_tv_khNm, item.getKhNm());
        helper.setText(R.id.item_tv_time, item.getTime());

        //备注
        TextView tvBz = helper.getView(R.id.item_tv_bz);
        TextView tvBz2 = helper.getView(R.id.item_tv_bz2);
        String remo1 = item.getRemo1();
        String remo2 = item.getRemo2();
        if(!MyStringUtil.isEmpty(remo1)){
            tvBz.setVisibility(View.VISIBLE);
            tvBz.setText("备注1：" + remo1);
        }else{
            tvBz.setVisibility(View.GONE);
        }
        if(!MyStringUtil.isEmpty(remo2)){
            tvBz2.setVisibility(View.VISIBLE);
            tvBz2.setText("备注1：" + remo2);
        }else{
            tvBz2.setVisibility(View.GONE);
        }


        //九图---配置图片显示在Application
        NineGridView nineGrid = helper.getView(R.id.nineGrid);//
        final List<String> images = item.getPicList();
        final List<String> images2 = item.getPicList2();
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if (null != images && images.size() > 0) {
            for (String image : images) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl("file://" + image);
                info.setBigImageUrl("file://" + image);
                imageInfo.add(info);
            }
        }
        if (null != images2 && images2.size() > 0) {
            for (String image : images2) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl("file://" + image);
                info.setBigImageUrl("file://" + image);
                imageInfo.add(info);
            }
        }
        if (null != imageInfo && imageInfo.size() > 0) {
            nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
            nineGrid.setVisibility(View.VISIBLE);
        } else {
            nineGrid.setVisibility(View.GONE);
        }
    }
}
