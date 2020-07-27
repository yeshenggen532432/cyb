package com.qwb.view.table.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.view.table.adapter.TableAdapter;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：统计报表
 */
public class TableListActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_table_list;
    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        initData();
    }


    private void initUI() {
        initHead();
        initAdapter();
    }

    /**
     * 初始化头部
     */
    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;

    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("统计表");
    }

    /**
     * 初始化适配器（报表）
     */
    RecyclerView mRvTable;
    TableAdapter mTableAdapter;

    private void initAdapter() {
        mRvTable = findViewById(R.id.rv_table);
        mRvTable.setHasFixedSize(true);
        mRvTable.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvTable.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mTableAdapter = new TableAdapter();
        mTableAdapter.openLoadAnimation();
        mRvTable.setAdapter(mTableAdapter);
        mTableAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClickView(adapter, position);
            }
        });
    }

    private ArrayList<String> list = new ArrayList<>();

    private void initData() {
        list.add("业务拜访统计表");
        list.add("客户拜访统计表");
        list.add("产品订单统计表");
        list.add("销售订单统计表");
        mTableAdapter.setNewData(list);
    }

    /**
     * 适配器item点击
     */
    private void onItemClickView(BaseQuickAdapter adapter, int position) {

        switch (position) {
            case 0:
                Router.newIntent(context)
                        .putInt(ConstantUtils.Intent.TYPE, position + 1)
                        .to(TableActivity1.class)
                        .launch();
                break;
            case 1:
                Router.newIntent(context)
                        .putInt(ConstantUtils.Intent.TYPE, position + 1)
                        .to(TableActivity2.class)
                        .launch();
                break;
            case 2:
                Router.newIntent(context)
                        .putInt(ConstantUtils.Intent.TYPE, position + 1)
                        .to(TableActivity3.class)
                        .launch();
                break;
            case 3:
                Router.newIntent(context)
                        .putInt(ConstantUtils.Intent.TYPE, position + 1)
                        .to(TableActivity4.class)
                        .launch();
                break;
        }
    }

}
