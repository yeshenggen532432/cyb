package com.qwb.view.cache.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.qwb.db.DStep1Bean;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: 缓存步骤1
 * 修改时间：
 * 修改备注：
 */
public class CacheStep1Adapter extends BaseQuickAdapter<DStep1Bean,BaseViewHolder> {

    public CacheStep1Adapter() {
        super(R.layout.x_adapter_cache_step1);
    }

    @Override
    protected void convert(BaseViewHolder helper, DStep1Bean item) {
        helper.addOnClickListener(R.id.item_layout_sd);
//        helper.addOnClickListener(R.id.layout_left);
        helper.setText(R.id.item_tv_khNm, item.getKhNm());
        helper.setText(R.id.item_tv_time, item.getTime());

        //备注
        TextView tvBz = helper.getView(R.id.item_tv_bz);
        String remarks = item.getRemo();
        if(!MyStringUtil.isEmpty(remarks)){
            tvBz.setVisibility(View.VISIBLE);
            tvBz.setText("备注：" + item.getRemo());
        }else{
            tvBz.setVisibility(View.GONE);
        }
//
//        TextView tvAddress = helper.getView(R.id.item_tv_address);
//        String address = item.getAddress();
//        if(!MyStringUtil.isEmpty(address)){
//            tvAddress.setVisibility(View.VISIBLE);
//            tvAddress.setText(address);
//        }else{
//            tvAddress.setVisibility(View.GONE);
//        }

        //九图---配置图片显示在Application
        NineGridView nineGrid = helper.getView(R.id.nineGrid);//
        final List<String> images = item.getPicList();
        if (null != images && images.size() > 0) {
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            if (images != null) {
                for (String image : images) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl("file://" + image);
                    info.setBigImageUrl("file://" + image);
                    imageInfo.add(info);
                }
            }
            nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
            nineGrid.setVisibility(View.VISIBLE);
        } else {
            nineGrid.setVisibility(View.GONE);
        }
    }
}
