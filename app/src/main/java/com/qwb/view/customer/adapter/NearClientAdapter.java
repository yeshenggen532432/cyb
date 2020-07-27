package com.qwb.view.customer.adapter;


import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.view.customer.model.NearClientInfo;
import com.chiyong.t3.R;

/**
 */
public class NearClientAdapter extends BaseQuickAdapter<NearClientInfo,BaseViewHolder> {

    public NearClientAdapter() {
//        x_adapter_near_client
        super(R.layout.x_adapter_mine_client2);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearClientInfo item) {
        //子item添加点击事件（tv_callonCount_mine：导航）
        helper.addOnClickListener(R.id.view_nav);

//        helper.setText(R.id.tv_branch_member, item.getMemberNm() + "/" + item.getBranchName());//业务员/部门
        helper.setText(R.id.tv_khNm, item.getKhNm());//客户名称
        TextView tvBranchMember=helper.getView(R.id.tv_branch_member);
        TextView tvDistance=helper.getView(R.id.tv_distance);//距离
        TextView tvLinkman=helper.getView(R.id.tv_linkman);//联系人
        View viewAddress=helper.getView(R.id.view_address);//地址
        TextView tvAddress=helper.getView(R.id.tv_address);//地址
        TextView tvMobile=helper.getView(R.id.tv_mobile);//手机
        TextView tvTime=helper.getView(R.id.tv_time);//拜访时间
//        TextView tvXxzt=helper.getView(R.id.tv_xxzt);// 新鲜度(临期，正常)

        String branchMember = item.getMemberNm();
        if(MyStringUtil.isNotEmpty(item.getBranchName())){
            branchMember += "/" +item.getBranchName();
        }
        tvBranchMember.setText(branchMember);
        //距离
        if(item.getJlkm()==null || ""==item.getJlkm()){
            tvDistance.setText("");
        }else{
            tvDistance.setText(item.getJlkm() + "km");
        }

        //联系人
        if (MyUtils.isEmptyString(item.getLinkman())) {
            tvLinkman.setVisibility(View.INVISIBLE);
        } else {
            tvLinkman.setVisibility(View.VISIBLE);
            tvLinkman.setText(item.getLinkman());
        }
        //地址
        if (MyUtils.isEmptyString(item.getAddress())) {
            viewAddress.setVisibility(View.INVISIBLE);
        } else {
            viewAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(item.getAddress());
        }
        //手机
        String tel = item.getMobile();
        if (MyStringUtil.isEmpty(tel)) {
            tel = item.getTel();
        }
        if (MyStringUtil.isEmpty(tel)) {
            tvMobile.setVisibility(View.INVISIBLE);
        } else {
            tvMobile.setVisibility(View.VISIBLE);
            tvMobile.setText(item.getMobile());
        }
        //拜访时间
        if (MyUtils.isEmptyString(item.getScbfDate())) {
            tvTime.setVisibility(View.INVISIBLE);
        } else {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(item.getScbfDate());
        }
        // 新鲜度(临期，正常)
//        if (MyUtils.isEmptyString(item.getXxzt())) {
//            tvXxzt.setVisibility(View.INVISIBLE);
//        } else {
//            tvXxzt.setVisibility(View.VISIBLE);
//            tvXxzt.setText(item.getXxzt());
//        }



//        helper.setText(R.id.tv_distance, item.getJlkm() + "km");//距离
//        helper.setText(R.id.tv_khNm, item.getKhNm());//客户名称
//        TextView tvQdtp=helper.getView(R.id.tv_qdtpNm);//渠道类型
//        TextView tvNameTo=helper.getView(R.id.tv_nameTo);// 新增开立
//        TextView tvDistance=helper.getView(R.id.tv_distance);
//        TextView tvCountUnder=helper.getView(R.id.tv_callonCount_underling);
//        TextView tvLinkman=helper.getView(R.id.tv_linkman);//联系人
//        TextView tvAddress=helper.getView(R.id.tv_address);//地址
//        TextView tvMobile=helper.getView(R.id.tv_mobile);//手机
//        TextView tvXxzt=helper.getView(R.id.tv_xxzt);// 新鲜度(临期，正常)
//        tvDistance.setVisibility(View.VISIBLE);
//        tvCountUnder.setVisibility(View.GONE);
//
//        //联系人
//        if (MyUtils.isEmptyString(item.getLinkman())) {
//            tvLinkman.setVisibility(View.INVISIBLE);
//        } else {
//            tvLinkman.setVisibility(View.VISIBLE);
//            tvLinkman.setText(item.getLinkman());
//        }
//        //地址
//        if (MyUtils.isEmptyString(item.getAddress())) {
//            tvAddress.setVisibility(View.INVISIBLE);
//        } else {
//            tvAddress.setVisibility(View.VISIBLE);
//            tvAddress.setText(item.getAddress());
//        }
//        //手机
//        if (MyUtils.isEmptyString(item.getMobile())) {
//            tvMobile.setVisibility(View.INVISIBLE);
//        } else {
//            tvMobile.setVisibility(View.VISIBLE);
//            tvMobile.setText(item.getMobile());
//        }
//        //新增开立
//        if (MyUtils.isEmptyString(item.getXsjdNm())) {
//            tvNameTo.setText(item.getMemberNm());
//        } else {
//            tvNameTo.setText(item.getMemberNm()+"/"+item.getXsjdNm());
//        }
//        // 新鲜度(临期，正常)
//        if (MyUtils.isEmptyString(item.getXxzt())) {
//            tvXxzt.setVisibility(View.INVISIBLE);
//        } else {
//            tvXxzt.setVisibility(View.VISIBLE);
//            tvXxzt.setText(item.getXxzt());
//        }
//        //渠道类型
//        if (MyUtils.isEmptyString(item.getQdtpNm())) {
//            tvQdtp.setVisibility(View.GONE);
//        } else {
//            tvQdtp.setVisibility(View.VISIBLE);
//            tvQdtp.setText(item.getQdtpNm());
//        }
    }
}
