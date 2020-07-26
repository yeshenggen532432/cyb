package com.qwb.view.customer.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyUtils;
import com.qwb.view.customer.model.MineClientInfo;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 文 件 名: ChooseMineClientAdapter
 * 修改时间：
 * 修改备注：
 */
public class ChooseMineClientAdapter extends BaseQuickAdapter<MineClientInfo,BaseViewHolder> {
    private List<MineClientInfo> selectList = new ArrayList<>();

    public ChooseMineClientAdapter() {
        super(R.layout.x_adapter_choose_mine_client);
    }

    @Override
    protected void convert(BaseViewHolder helper, MineClientInfo item) {

        helper.setText(R.id.tv_nameTo, item.getMemberNm() + "/" + item.getBranchName());//业务员/部门
        helper.setText(R.id.tv_khNm, item.getKhNm());//客户名称
        TextView tvQdtp = helper.getView(R.id.tv_qdtpNm);//渠道类型
        TextView tvDistance = helper.getView(R.id.tv_distance);//距离
        TextView tvCountUnder = helper.getView(R.id.tv_callonCount_underling);
        TextView tvLinkman = helper.getView(R.id.tv_linkman);//联系人
        TextView tvAddress = helper.getView(R.id.tv_address);//地址
        TextView tvMobile = helper.getView(R.id.tv_mobile);//手机
        TextView tvTime = helper.getView(R.id.tv_time);//拜访时间
        TextView tvXxzt = helper.getView(R.id.tv_xxzt);// 新鲜度(临期，正常)



        tvDistance.setVisibility(View.VISIBLE);
        tvCountUnder.setVisibility(View.GONE);

        //---------------------cb:start-----------------------
        ImageView ivCb = helper.getView(R.id.item_iv_cb);
        boolean flag = false;
        for (MineClientInfo mineClientInfo : selectList) {
            if(String.valueOf(mineClientInfo.getId()).equals(String.valueOf(item.getId()))){
                flag = true;
                break;
            }
        }
        if(flag){
            ivCb.setImageResource(R.drawable.icon_dxz);
        }else{
            ivCb.setImageResource(R.drawable.icon_dx);
        }
        //---------------------cb:end-----------------------

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
        //拜访时间
        if (MyUtils.isEmptyString(item.getScbfDate())) {
            tvTime.setVisibility(View.INVISIBLE);
        } else {
            tvTime.setVisibility(View.VISIBLE);
            tvTime.setText(item.getScbfDate());
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

    public List<MineClientInfo> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<MineClientInfo> selectList) {
        this.selectList = selectList;
    }
}
