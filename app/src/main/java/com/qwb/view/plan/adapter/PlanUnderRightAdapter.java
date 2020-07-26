package com.qwb.view.plan.adapter;


import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.plan.model.PlanLineBean;
import com.xmsx.cnlife.view.widget.MyRecyclerView;
import com.xmsx.qiweibao.R;

import cn.droidlover.xrecyclerview.divider.HorizontalDividerItemDecoration;

/**
 * 文 件 名: 计划拜访--下属拜访--下属线路--右（线路）
 */
public class PlanUnderRightAdapter extends BaseQuickAdapter<PlanLineBean, BaseViewHolder> {
    private Activity activity;
    private int mPosition = -1;

    public PlanUnderRightAdapter(Activity activity) {
        super(R.layout.x_adapter_plan_under_right);
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, PlanLineBean item) {
        //点击事件
//        helper.addOnClickListener(R.id.tv_state);

        //赋值
        TextView tvXlNm = helper.getView(R.id.item_tv_xlNm);
        final ImageView ivArrow = helper.getView(R.id.item_iv_arrow);
        final MyRecyclerView recyclerView = helper.getView(R.id.recyclerView);

        tvXlNm.setText(item.getXlNm());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        //添加分割线
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(activity)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        PlanUnderRightSubAdapter adapter = new PlanUnderRightSubAdapter();
        adapter.setNewData(item.getChildren());
        recyclerView.setAdapter(adapter);

        final int position = helper.getAdapterPosition();
        if(mPosition == -1){
            if(position == 0){
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
}
