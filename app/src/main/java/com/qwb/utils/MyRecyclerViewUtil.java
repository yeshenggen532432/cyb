package com.qwb.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by yeshenggen on 2019/5/14.
 */

public class MyRecyclerViewUtil {

    public static void init(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter){
        try{
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //添加分割线
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                    .colorResId(R.color.gray_e)
                    .sizeResId(R.dimen.dp_0_5)
//				.marginResId(R.dimen.dp_62,R.dimen.dp_0)//线左右边距
                    .build());
            recyclerView.setAdapter(adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }catch (Exception e){
        }

    }

    public static void init(Context context, RecyclerView recyclerView, BaseQuickAdapter adapter,int leftMarginResId){
        try {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            //添加分割线
            recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                    .colorResId(R.color.gray_e)
                    .sizeResId(R.dimen.dp_0_5)
                    .marginResId(leftMarginResId,R.dimen.dp_0)//线左右边距
                    .build());
            recyclerView.setAdapter(adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }catch (Exception e){}
    }


}
