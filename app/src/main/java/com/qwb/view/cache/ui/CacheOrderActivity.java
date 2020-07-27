package com.qwb.view.cache.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.view.step.ui.Step5Activity;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.cache.adapter.CacheStep5Adapter;
import com.qwb.db.DStep5Bean;
import com.qwb.view.cache.parsent.PCacheOrder;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 缓存-订单，退单
 */
public class CacheOrderActivity extends XActivity<PCacheOrder>{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_cache_order;
    }

    @Override
    public PCacheOrder newP() {
        return new PCacheOrder();
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
        getP().queryAllCache(context);
    }

    private void initUI() {
        initHead();
        initAdapter();
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("我的订单");
    }

    /**
     * 初始化适配器
     */
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    CacheStep5Adapter mAdapter5;
    private void initAdapter() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_0_5)
                .build());
        mAdapter5 = new CacheStep5Adapter();
        mAdapter5.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    mPosition = position;
                    mDStep5Bean = (DStep5Bean) adapter.getData().get(position);
                    switch (view.getId()){
                            //删除
                        case R.id.item_layout_sd:
                            int count = MyDataUtils.getInstance().delStep5(mDStep5Bean);
                            if(count > 0){
                                refreshAdapter5(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                            //未发送
                        case R.id.item_layout_no_send:
                            if(!MyNetWorkUtil.isNetworkConnected()){
                                ToastUtils.showCustomToast("当前网络不流通，请检查网络");
                                return;
                            }
                            getP().doStep5(mDStep5Bean);
                            break;
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });

        mAdapter5.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try{
                    DStep5Bean mDStep5Bean = (DStep5Bean) adapter.getData().get(position);
                    Router.newIntent(context)
                            .putInt(ConstantUtils.Intent.ORDER_TYPE, Integer.valueOf(mDStep5Bean.getType()))
                            .putInt(ConstantUtils.Intent.ORDER_ID, Integer.valueOf(mDStep5Bean.getOrderId()))// 订单id
                            .putString(ConstantUtils.Intent.CLIENT_ID, String.valueOf(mDStep5Bean.getCid()))
                            .putString(ConstantUtils.Intent.CLIENT_NAME, mDStep5Bean.getKhNm())
                            .putString(ConstantUtils.Intent.ADDRESS, mDStep5Bean.getAddress())
                            .putString(ConstantUtils.Intent.TEL, mDStep5Bean.getTel())
                            .putString(ConstantUtils.Intent.MOBILE, mDStep5Bean.getTel())
                            .putString(ConstantUtils.Intent.LINKMAN, mDStep5Bean.getShr())
                            .to(Step5Activity.class)
                            .launch();

                }catch (Exception e){
                }

            }
        });


    }

    private DStep5Bean mDStep5Bean;
    private int mPosition;
    public void showDialogCaozuo(final int step){
        String[] items = new String[]{"删除","上传"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("操作").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                int count = 0;
                //删除
                if(0 == position){
                    switch (step){
                        case 5:
                            count = MyDataUtils.getInstance().delStep5(mDStep5Bean);
                            if(count > 0){
                                refreshAdapter5(null, true);
                                ToastUtils.showCustomToast("删除成功");
                            }
                            break;
                    }
                }

                //上传
                if(1 == position){
                    if(!MyNetWorkUtil.isNetworkConnected()){
                        ToastUtils.showCustomToast("当前网络不流通，请检查网络");
                        return;
                    }
                    switch (step){
                        case 5:
                            getP().doStep5(mDStep5Bean);
                            break;
                    }
                }
            }
        });
    }

    public void refreshAdapter5(List<DStep5Bean> beans, boolean del){
        mRecyclerView.setAdapter(mAdapter5);
        if(del){
            mAdapter5.remove(mPosition);
            mAdapter5.notifyDataSetChanged();
        }else{
            mAdapter5.setNewData(beans);
        }
    }




}
