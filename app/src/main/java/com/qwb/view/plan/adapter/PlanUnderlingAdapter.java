package com.qwb.view.plan.adapter;


import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.plan.model.PlanUnderBean;
import com.xmsx.cnlife.view.widget.MyRecyclerView;
import com.chiyong.t3.R;

import cn.droidlover.xrecyclerview.divider.HorizontalDividerItemDecoration;

/**
 * 文 件 名: 计划拜访(下属)
 */
public class PlanUnderlingAdapter extends BaseQuickAdapter<PlanUnderBean, BaseViewHolder> {

    private Activity activity;
    private int mPosition = -1;
    private String mDate;//筛选时间

    public PlanUnderlingAdapter(Activity activity) {
        super(R.layout.x_adapter_plan_underling);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final PlanUnderBean item) {
        //点击事件
//        helper.addOnClickListener(R.id.tv_state);

        //赋值
        TextView tvXlNm = helper.getView(R.id.item_tv_xlNm);
        TextView tvMemberNm = helper.getView(R.id.item_tv_memberNm);
        final MyRecyclerView recyclerView = helper.getView(R.id.recyclerView);
        final ImageView ivArrow = helper.getView(R.id.item_iv_arrow);


        tvXlNm.setText(item.getXlNm());
        tvMemberNm.setText(item.getMemberNm());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        //添加分割线
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        final PlanUnderlingSubAdapter adapter = new PlanUnderlingSubAdapter();
        adapter.setNewData(item.getSubList());
        recyclerView.setAdapter(adapter);

        final int position = helper.getAdapterPosition();
        if(mPosition == -1){
            if(position == 1){
                recyclerView.setVisibility(View.VISIBLE);
                ivArrow.animate().rotation(180).setDuration(100).start();
            }else{
                recyclerView.setVisibility(View.GONE);
                ivArrow.animate().rotation(0).setDuration(100).start();
            }
        }else{
            if(mPosition == position){
                recyclerView.setVisibility(View.VISIBLE);
                ivArrow.animate().rotation(180).setDuration(100).start();
            }else{
                recyclerView.setVisibility(View.GONE);
                ivArrow.animate().rotation(0).setDuration(100).start();
            }
        }

        helper.getView(R.id.item_layout_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility() == View.VISIBLE){
                    ivArrow.animate().rotation(0).setDuration(100).start();
                    mPosition = -2;
                } else {
                    ivArrow.animate().rotation(180).setDuration(100).start();
                    mPosition = position;
                }
                notifyDataSetChanged();

            }
        });
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }
}
