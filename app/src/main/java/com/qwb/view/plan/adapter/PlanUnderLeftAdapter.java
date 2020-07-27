package com.qwb.view.plan.adapter;


import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyColorUtil;
import com.qwb.view.member.model.MemberBean;
import com.chiyong.t3.R;

/**
 * 文 件 名: 计划拜访--下属拜访--下属线路--左边（业务员）
 */
public class PlanUnderLeftAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {
    private int mPosition = 0;

    public PlanUnderLeftAdapter() {
        super(R.layout.x_adapter_plan_under_left);
    }

    @Override
    protected void convert(BaseViewHolder helper, final MemberBean item) {
        //点击事件
//        helper.addOnClickListener(R.id.tv_state);

        //赋值
        TextView tvName = helper.getView(R.id.item_tv_name);
        tvName.setText(item.getMemberNm());

        final int position = helper.getAdapterPosition();
        if(mPosition == position){
            tvName.setTextColor(MyColorUtil.getColorResId(R.color.x_main_color));
        }else{
            tvName.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
        }


    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }
}
