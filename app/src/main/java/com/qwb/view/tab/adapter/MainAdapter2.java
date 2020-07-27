package com.qwb.view.tab.adapter;


import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.Constans;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.tab.model.MainFuncBean;
import com.chiyong.t3.R;

/**
 * 整理单列表
 */
public class MainAdapter2 extends BaseQuickAdapter<MainFuncBean, BaseViewHolder> {

    public MainAdapter2() {
        super(R.layout.x_adapter_main_func);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainFuncBean item) {
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvQty = helper.getView(R.id.tv_qty);
        try {
//            MyGlideUtil.getInstance().displayImageSquere(Constans.url + item.getImagePath(), ivIcon);
//            http://47.107.141.109:8080//upload/resource/img/icon1.png

            tvName.setText(item.getFuncName());
            if (MyStringUtil.isNotEmpty(item.getQty())){
                tvQty.setText(item.getQty());
            }
            if (item.getImgRes() != null){
                ivIcon.setImageResource(item.getImgRes());
            }else{
                MyGlideUtil.getInstance().displayImageSquere(Constans.ROOT + item.getImagePath(), ivIcon);
            }
        } catch (Exception e) {
        }

    }


}
