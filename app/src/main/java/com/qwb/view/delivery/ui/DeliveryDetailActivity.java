package com.qwb.view.delivery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qwb.view.delivery.adapter.DeliveryLeftAdapter;
import com.qwb.view.delivery.adapter.DeliveryRightAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.model.DeliverySubBean;
import com.qwb.view.delivery.parsent.PDeliveryDetail;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.table.SyncHorizontalScrollView;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 配送单详情
 */
public class DeliveryDetailActivity extends XActivity<PDeliveryDetail> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_delivery_detail;
    }

    @Override
    public PDeliveryDetail newP() {
        return new PDeliveryDetail();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        getP().queryDataDetail(context, billId);
    }

    /**
     * 初始化Intent
     */
    private String billId;//配送单id
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            billId = intent.getStringExtra(ConstantUtils.Intent.ID);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initTableView();
        initOtherUI();
    }

    /**
     * 头部
     */
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("配送单");
    }

    @BindView(R.id.tv_khNm)
    TextView mTvKhNm;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.tv_bz)
    TextView mTvBz;
    @BindView(R.id.layout_show)
    View mLayoutShow;
    @BindView(R.id.layout_hide)
    View mLayoutHide;
    @BindView(R.id.iv_show)
    ImageView mIvShow;
    @BindView(R.id.tv_storage)
    TextView mTvStorage;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_pszd)
    TextView mTvPszd;
    @BindView(R.id.tv_car_lable)
    TextView mTvCarLable;
    @BindView(R.id.tv_car)
    TextView mTvCar;
    @BindView(R.id.layout_storage)
    View mLayoutStorage;
    @BindView(R.id.tv_time_lable)
    TextView mTvTimeLable;
    @BindView(R.id.layout_pszd)
    View mLayoutPszd;
    private boolean isHide = true;
    private void initOtherUI() {
        //显示和隐藏：电话，地址，仓库
        mLayoutShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHide){
                    mLayoutHide.setVisibility(View.VISIBLE);
                    mIvShow.setImageResource(R.drawable.icon_jian);
                }else{
                    mLayoutHide.setVisibility(View.GONE);
                    mIvShow.setImageResource(R.drawable.icon_jia);
                }
                isHide = !isHide;
            }
        });
    }

    /**
     * 表格
     */
    @BindView(R.id.tv_table_title_left)
    TextView mTvTableTitleLeft;
    private SyncHorizontalScrollView mTitleScrollView;
    private ListView mLeftListView;
    private ListView mRightListView;
    private SyncHorizontalScrollView mContentScrollView;
    private ArrayList<DeliverySubBean> mDatas = new ArrayList<>();
    private DeliveryLeftAdapter mLeftAdapter;
    private DeliveryRightAdapter mRightAdapter;
    public void initTableView() {
        mTitleScrollView = findViewById(R.id.title_horsv);
        mContentScrollView = findViewById(R.id.content_horsv);
        // 设置两个水平控件的联动
        mTitleScrollView.setScrollView(mContentScrollView);
        mContentScrollView.setScrollView(mTitleScrollView);

        mLeftListView = findViewById(R.id.left_container_listview);
        mRightListView = findViewById(R.id.right_container_listview);

        mLeftAdapter = new DeliveryLeftAdapter(context,mDatas);
        mRightAdapter = new DeliveryRightAdapter(context, mDatas);
        mLeftListView.setAdapter(mLeftAdapter);
        mRightListView.setAdapter(mRightAdapter);
    }



    /**
     * 刷新表格数据
     */
    private void refreshAdapterRight() {
        //标记商品的个数（重复商品颜色变）
        setRepeatMap();
        //刷新
        mLeftAdapter.notifyDataSetChanged();
        mRightAdapter.notifyDataSetChanged();
    }
    /**
     * 标记商品的个数（重复商品颜色变）--1:选择商品后 2：删除商品 3：获取订单数据
     */
    public void setRepeatMap(){
        Map<Integer, Integer> repeatMap = new HashMap<>();
        for (DeliverySubBean data : mDatas) {
            int wareId = data.getWareId();
            if(repeatMap.containsKey(wareId)){
                int qty = repeatMap.get(wareId);
                repeatMap.put(wareId,qty + 1);
            }else{
                repeatMap.put(wareId,1);
            }
        }
        mLeftAdapter.setRepeatMap(repeatMap);
    }


    /**
     * /显示信息
     */
    public void doUI(DeliveryBean data){
        mTvKhNm.setText(data.getKhNm());
        mTvCar.setText(data.getVehNo());
        mTvAddress.setText(data.getAddress());
        mTvPhone.setText(data.getTel());
        mTvBz.setText(data.getRemarks());
        mTvStorage.setText(data.getStkName());
        mTvTime.setText(data.getOutDate());
        mTvPszd.setText(data.getPszd());
        mTvStorage.setText(data.getStkName());

        List<DeliverySubBean> list = data.getList();
        if(null != list && null != mDatas){
            mDatas.clear();
            mDatas.addAll(list);
            refreshAdapterRight();
        }
    }










}
