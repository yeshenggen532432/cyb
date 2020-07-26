package com.qwb.view.customer.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyUtils;
import com.qwb.view.customer.model.NearClientInfo;
import com.xmsx.qiweibao.R;

/**
 * 文 件 名: ChooseNearClientAdapter
 * 创 建 人: 叶生根
 * 创建日期: 18/05/14
 * 修改时间：
 * 修改备注：
 */
public class NearClientAdapter extends BaseQuickAdapter<NearClientInfo,BaseViewHolder> {

    public NearClientAdapter() {
        super(R.layout.x_adapter_near_client);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearClientInfo item) {
        //子item添加点击事件（tv_callonCount_mine：导航）
        helper.addOnClickListener(R.id.tv_callonCount_mine);

        helper.setText(R.id.tv_distance, item.getJlkm() + "km");//距离
        helper.setText(R.id.tv_khNm, item.getKhNm());//客户名称
        TextView tvQdtp=helper.getView(R.id.tv_qdtpNm);//渠道类型
        TextView tvNameTo=helper.getView(R.id.tv_nameTo);// 新增开立
        TextView tvDistance=helper.getView(R.id.tv_distance);
        TextView tvCountUnder=helper.getView(R.id.tv_callonCount_underling);
        TextView tvLinkman=helper.getView(R.id.tv_linkman);//联系人
        TextView tvAddress=helper.getView(R.id.tv_address);//地址
        TextView tvMobile=helper.getView(R.id.tv_mobile);//手机
        TextView tvXxzt=helper.getView(R.id.tv_xxzt);// 新鲜度(临期，正常)
        tvDistance.setVisibility(View.VISIBLE);
        tvCountUnder.setVisibility(View.GONE);

        //联系人
        if (MyUtils.isEmptyString(item.getLinkman())) {
            tvLinkman.setVisibility(View.INVISIBLE);
        } else {
            tvLinkman.setVisibility(View.VISIBLE);
            tvLinkman.setText(item.getLinkman());
        }
        //地址
        if (MyUtils.isEmptyString(item.getAddress())) {
            tvAddress.setVisibility(View.INVISIBLE);
        } else {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(item.getAddress());
        }
        //手机
        if (MyUtils.isEmptyString(item.getMobile())) {
            tvMobile.setVisibility(View.INVISIBLE);
        } else {
            tvMobile.setVisibility(View.VISIBLE);
            tvMobile.setText(item.getMobile());
        }
        //新增开立
        if (MyUtils.isEmptyString(item.getXsjdNm())) {
            tvNameTo.setText(item.getMemberNm());
        } else {
            tvNameTo.setText(item.getMemberNm()+"/"+item.getXsjdNm());
        }
        // 新鲜度(临期，正常)
        if (MyUtils.isEmptyString(item.getXxzt())) {
            tvXxzt.setVisibility(View.INVISIBLE);
        } else {
            tvXxzt.setVisibility(View.VISIBLE);
            tvXxzt.setText(item.getXxzt());
        }
        //渠道类型
        if (MyUtils.isEmptyString(item.getQdtpNm())) {
            tvQdtp.setVisibility(View.GONE);
        } else {
            tvQdtp.setVisibility(View.VISIBLE);
            tvQdtp.setText(item.getQdtpNm());
        }
    }
}
