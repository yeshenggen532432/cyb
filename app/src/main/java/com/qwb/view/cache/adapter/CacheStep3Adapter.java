package com.qwb.view.cache.adapter;


import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.qwb.db.DStep3Bean;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.QueryBfcljccjBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: 缓存步骤3
 * 修改时间：
 * 修改备注：
 */
public class CacheStep3Adapter extends BaseQuickAdapter<DStep3Bean,BaseViewHolder> {

    public CacheStep3Adapter() {
        super(R.layout.x_adapter_cache_step3);
    }

    @Override
    protected void convert(BaseViewHolder helper, DStep3Bean item) {
        helper.addOnClickListener(R.id.item_layout_sd);
//        helper.addOnClickListener(R.id.layout_left);
        helper.setText(R.id.item_tv_khNm, item.getKhNm());
        helper.setText(R.id.item_tv_time, item.getTime());

        //备注
        TextView tvBz = helper.getView(R.id.item_tv_bz);
        TextView tvBz2 = helper.getView(R.id.item_tv_bz2);
        TextView tvBz3 = helper.getView(R.id.item_tv_bz3);
        String json = item.getXxjh();
        if(!MyStringUtil.isEmpty(json)){
            List<QueryBfcljccjBean.QueryBfcljccj> list = JSON.parseArray(json, QueryBfcljccjBean.QueryBfcljccj.class);
            for (int i = 0; i < list.size(); i++){
                QueryBfcljccjBean.QueryBfcljccj bean = list.get(i);
                if(0 == i){
                    if(!MyStringUtil.isEmpty(bean.getRemo())){
                        tvBz.setVisibility(View.VISIBLE);
                        tvBz.setText("备注1：" + bean.getRemo());
                    }else{
                        tvBz.setVisibility(View.GONE);
                    }
                }else if(1 == i){
                    if(!MyStringUtil.isEmpty(bean.getRemo())){
                        tvBz2.setVisibility(View.VISIBLE);
                        tvBz2.setText("备注1：" + bean.getRemo());
                    }else{
                        tvBz2.setVisibility(View.GONE);
                    }
                }else if(2 == i){
                    if(!MyStringUtil.isEmpty(bean.getRemo())){
                        tvBz3.setVisibility(View.VISIBLE);
                        tvBz3.setText("备注1：" + bean.getRemo());
                    }else{
                        tvBz3.setVisibility(View.GONE);
                    }
                }
            }
        }

        //九图---配置图片显示在Application
        NineGridView nineGrid = helper.getView(R.id.nineGrid);
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();

        final List<String> images = item.getPicList();
        if (null != images && images.size() > 0) {
            for (String image : images) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl("file://" + image);
                info.setBigImageUrl("file://" + image);
                imageInfo.add(info);
            }
        }
        final List<String> images2 = item.getPicList2();
        if (null != images2 && images2.size() > 0) {
            for (String image : images2) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl("file://" + image);
                info.setBigImageUrl("file://" + image);
                imageInfo.add(info);
            }
        }
        final List<String> images3 = item.getPicList3();
        if (null != images3 && images3.size() > 0) {
            for (String image : images3) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl("file://" + image);
                info.setBigImageUrl("file://" + image);
                imageInfo.add(info);
            }
        }
        if(null != imageInfo && imageInfo.size() > 0){
            nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
            nineGrid.setVisibility(View.VISIBLE);
        } else {
            nineGrid.setVisibility(View.GONE);
        }
    }
}
