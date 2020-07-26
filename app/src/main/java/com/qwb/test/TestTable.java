package com.qwb.test;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.utils.MyTableUtil;
import com.qwb.view.storehouse.adapter.StorehouseOutLeftAdapter;
import com.qwb.view.storehouse.adapter.StorehouseOutRightAdapter;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import butterknife.BindView;

/**
 *  表格
 */

public class TestTable {

    @BindView(R.id.rv_left)
    RecyclerView mRvLeft;
    @BindView(R.id.rv_right)
    RecyclerView mRvRight;
    @BindView(R.id.sv_ware)
    TableHorizontalScrollView mSvWare;
    @BindView(R.id.tv_table_title_left)
    TextView mTvTableTitleLeft;
    private StorehouseOutLeftAdapter mLeftAdapter;
    private StorehouseOutRightAdapter mRightAdapter;

    public void initTableView(Context context) {
        //left
        mRvLeft.setLayoutManager(new LinearLayoutManager(context));
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mLeftAdapter = new StorehouseOutLeftAdapter();
        mRvLeft.setAdapter(mLeftAdapter);
        //right
        mRvRight.setLayoutManager(new LinearLayoutManager(context));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mRightAdapter = new StorehouseOutRightAdapter();
        mRvRight.setAdapter(mRightAdapter);
        //右边点击事件：1.切换单位；2.选择库位
        mRightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

        //设置两个列表的同步滚动
        MyTableUtil.getInstance().setSyncScrollListener(mRvLeft, mRvRight, mSvWare);

        //添加商品
        mTvTableTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });

    }


}
