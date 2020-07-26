package com.qwb.view.ware.adapter;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.step.model.WarePicBean;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyGlideUtil;
import com.qwb.view.step.model.ShopInfoBean;
import com.xmsx.qiweibao.R;

import java.util.List;

/**
 * 文 件 名: 商品管理 有图片
 * 修改时间：
 * 修改备注：
 */
public class WareManagerAdapter extends BaseQuickAdapter<ShopInfoBean.Data, BaseViewHolder> {

    public WareManagerAdapter() {
        super(R.layout.x_adapter_ware_manager_right);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopInfoBean.Data item) {

        TextView tvWareNm = helper.getView(R.id.item_tv_wareNm);
        TextView tvWareGg = helper.getView(R.id.item_tv_wareGg);
        ImageView iv = helper.getView(R.id.item_iv);

        tvWareNm.setText(item.getWareNm());
        tvWareGg.setText(item.getWareGg());

        //服务器的图片
        final List<WarePicBean> warePicList = item.getWarePicList();
        if (warePicList != null && !warePicList.isEmpty()) {
            String sourcePath = Constans.IMGROOTHOST + warePicList.get(0).getPicMini();
            for (WarePicBean goodsPic : warePicList) {
                //1:为主图
                if (goodsPic.getType() == 1) {
                    sourcePath = Constans.IMGROOTHOST + goodsPic.getPicMini();
                    break;
                }
            }
            MyGlideUtil.getInstance().displayImageSquere(sourcePath, iv);
        }else{
            iv.setImageResource(R.drawable.empty_photo);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //放大图片
                int positionPic = 0;
                if (warePicList != null && !warePicList.isEmpty()) {
                    String[] urls = new String[warePicList.size()];
                    for (int i = 0; i < warePicList.size(); i++) {
                        urls[i]= Constans.IMGROOTHOST + warePicList.get(i).getPic();
                        //1:为主图
                        if(warePicList.get(i).getType() == 1){
                            positionPic=i;
                        }
                    }
                    // 点击放大图片
                    Intent intent = new Intent(mContext, ImagePagerActivity.class);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, positionPic);
                    mContext.startActivity(intent);
                }
            }
        });


    }
}
