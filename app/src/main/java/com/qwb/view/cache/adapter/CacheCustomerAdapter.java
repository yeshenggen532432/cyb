package com.qwb.view.cache.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.db.DMineCustomerBean;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

/**
 * 文 件 名: MineCustomerAdapter
 * 修改时间：
 * 修改备注：
 */
public class CacheCustomerAdapter extends BaseQuickAdapter<DMineCustomerBean,BaseViewHolder> {

    public CacheCustomerAdapter() {
        super(R.layout.x_adapter_mine_customer);
    }

    @Override
    protected void convert(BaseViewHolder helper, DMineCustomerBean item) {
        helper.addOnClickListener(R.id.item_layout_nav);

        helper.setText(R.id.item_tv_member_branch, item.getMemberNm() + "/" + item.getBranchName());//业务员/部门
        helper.setText(R.id.item_tv_khNm, item.getKhNm());//客户名称

        TextView tvQdtp=helper.getView(R.id.item_tv_qdtpNm);//渠道类型
        TextView tvDistance=helper.getView(R.id.item_tv_distance);//距离
        TextView tvLinkman=helper.getView(R.id.item_tv_linkman);//联系人
        View layoutAddress=helper.getView(R.id.item_layout_address);//地址
        TextView tvAddress=helper.getView(R.id.item_tv_address);//地址
        View layoutPhone=helper.getView(R.id.item_layout_phone);//手机
        TextView tvMobile=helper.getView(R.id.item_tv_mobile);//手机
        View layoutScbf=helper.getView(R.id.item_layout_scbf);//拜访时间
        TextView tvTime=helper.getView(R.id.item_tv_time);//拜访时间
        TextView tvXxzt=helper.getView(R.id.item_tv_xxzt);// 新鲜度(临期，正常)

        //距离
        double distance = item.getDistance();
        if(MyStringUtil.isEmpty(""+ item.getDistance())){
            tvDistance.setText("");
        }else{
            if(distance < 1000){
                tvDistance.setText((int)item.getDistance() + "米");
            }else{
                tvDistance.setText((item.getDistance()/1000) + "千米");
            }
        }

        //联系人
        if (MyStringUtil.isEmpty(item.getLinkman())) {
            tvLinkman.setVisibility(View.GONE);
        } else {
            tvLinkman.setVisibility(View.VISIBLE);
            tvLinkman.setText(item.getLinkman());
        }
        //地址
        if (MyStringUtil.isEmpty(item.getAddress())) {
            layoutAddress.setVisibility(View.GONE);
        } else {
            layoutAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(item.getAddress());
        }
        //手机
        if (MyStringUtil.isEmpty(item.getMobile()) && MyStringUtil.isEmpty(item.getTel())) {
            layoutPhone.setVisibility(View.GONE);
        } else {
            layoutPhone.setVisibility(View.VISIBLE);
            if(MyStringUtil.isEmpty(item.getMobile())){
                tvMobile.setText(item.getTel());
            }else{
                tvMobile.setText(item.getMobile());
            }
        }
        //拜访时间
        if (MyStringUtil.isEmpty(item.getScbfDate())) {
            layoutScbf.setVisibility(View.GONE);
        } else {
            layoutScbf.setVisibility(View.VISIBLE);
            tvTime.setText(item.getScbfDate());
        }
        // 新鲜度(临期，正常)
        if (MyStringUtil.isEmpty(item.getXxzt())) {
            tvXxzt.setVisibility(View.GONE);
        } else {
            tvXxzt.setVisibility(View.VISIBLE);
            tvXxzt.setText(item.getXxzt());
        }
        //渠道类型
        if (MyStringUtil.isEmpty(item.getQdtpNm())) {
            tvQdtp.setVisibility(View.GONE);
        } else {
            tvQdtp.setVisibility(View.VISIBLE);
            tvQdtp.setText(item.getQdtpNm());
        }
    }
}
