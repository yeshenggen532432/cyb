package com.qwb.view.shop.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.tab.model.HotShopBean;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.company.model.CompanysBean;
import com.qwb.utils.MyGlideUtil;
import com.chiyong.t3.R;

import java.util.List;

/**
 * 文 件 名: 供货商
 */
public class ShopAdapter extends BaseQuickAdapter<HotShopBean,BaseViewHolder> {

    private Context context;
    public ShopAdapter(Context context) {
        super(R.layout.x_adapter_supplier);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, HotShopBean item) {
//        //子view添加点击事件
        helper.addOnClickListener(R.id.sb_to_shop);
        helper.addOnClickListener(R.id.right_tv_cancel);
        helper.addOnClickListener(R.id.right_tv_normal);

        ImageView ivLogo = helper.getView(R.id.iv_logo);
        helper.setText(R.id.tv_company_name, item.getName());// 供货商(公司名)
        TextView tvNormal = helper.getView(R.id.tv_normal);
        TextView rightTvCancel = helper.getView(R.id.right_tv_cancel);
        TextView rightTvNormal = helper.getView(R.id.right_tv_normal);

        MyGlideUtil.getInstance().displayImageSquere(item.getLogoUrl(), ivLogo, false);

        //默认：默认商城
        String normalShopCompanyId = SPUtils.getSValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_ID);
        if(String.valueOf(item.getId()).equals(normalShopCompanyId)){
            tvNormal.setVisibility(View.VISIBLE);
            rightTvNormal.setVisibility(View.GONE);
        }else{
            tvNormal.setVisibility(View.GONE);
            rightTvNormal.setVisibility(View.VISIBLE);
        }

        //取消关注
        if (isCompanyMember(""+item.getId())){
            rightTvCancel.setVisibility(View.GONE);
        }else{
            rightTvCancel.setVisibility(View.VISIBLE);
        }


    }

    /**
     * 是否属于该公司
     */
    private boolean isCompanyMember(String companyId) {
        try {
            String companies = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
            if (MyStringUtil.isNotEmpty(companies)) {
                List<CompanysBean> companyList = JSON.parseArray(companies, CompanysBean.class);
                if (MyCollectionUtil.isNotEmpty(companyList)) {
                    for (CompanysBean bean : companyList) {
                        if (MyStringUtil.eq(companyId, String.valueOf(bean.getCompanyId()))) {
                            return true;
                        }
                    }
                }
            }
        }catch (Exception e){
        }
        return false;
    }
}
