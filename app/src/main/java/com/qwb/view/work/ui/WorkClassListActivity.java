package com.qwb.view.work.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.view.work.adapter.WorkClassListAdapter;
import com.qwb.event.BaseEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.work.model.WorkBean;
import com.qwb.view.work.model.WorkClassListResult;
import com.qwb.view.work.parsent.PWorkClassList;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 班次列表
 */
public class WorkClassListActivity extends XActivity<PWorkClassList> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_work_class_list;
    }

    @Override
    public PWorkClassList newP() {
        return new PWorkClassList();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        getP().queryWorkList(context);
    }

    @Override
    public void bindEvent() {
        super.bindEvent();
        BusProvider.getBus().toFlowable(BaseEvent.class)
                .subscribe(new Consumer<BaseEvent>() {
                    @Override
                    public void accept(BaseEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_BASE) {
                            try {
                                getP().queryWorkList(context);
                            } catch (Exception e) {
                            }
                        }
                    }
                });
    }

    @Override
    public boolean useEventBus() {
        return true;
    }


    /**
     * 初始化UI
     */
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
        mTvHeadTitle.setText("班次列表");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    WorkClassListAdapter mAdapter;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_5)
                .build());
        mAdapter = new WorkClassListAdapter(context);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        //item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        //子item点击事件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                WorkBean data = (WorkBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.tv_update:
                        Router.newIntent(context)
                                .to(WorkClassAddressActivity.class)
                                .putString(ConstantUtils.Intent.LATITUDE, data.getLatitude())
                                .putString(ConstantUtils.Intent.LONGITUDE, data.getLongitude())
                                .putString(ConstantUtils.Intent.ADDRESS, data.getAddress())
                                .putString(ConstantUtils.Intent.AREA_LONG, String.valueOf(data.getAreaLong()))
                                .putString(ConstantUtils.Intent.ID, String.valueOf(data.getId()))
                                .putString(ConstantUtils.Intent.TYPE, String.valueOf(data.getOutOf()))
                                .launch();
                        break;
                }
            }
        });
    }

    //==========================================================================================
    //班次列表
    public void refreshAdapter(WorkClassListResult data) {
        if (data == null) return;
        List<WorkBean> bcList = data.getRows();
        mAdapter.setNewData(bcList);
    }


}
