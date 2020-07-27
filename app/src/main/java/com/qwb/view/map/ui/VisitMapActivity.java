package com.qwb.view.map.ui;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.view.location.ui.MapLocationActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.view.map.adapter.MapAdapter;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.db.DMapTrackBean;
import com.qwb.view.map.parsent.PVisitMap;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.map.model.TrackListBean;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * (员工在线)拜访地图
 */
public class VisitMapActivity extends XActivity<PVisitMap> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_visit_map;
    }

    @Override
    public PVisitMap newP() {
        return new PVisitMap();
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        queryDB();//查询数据库(缓存)
        mMemberIds = SPUtils.getSValues(ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS);// 成员ID数组
        getP().loadData(null,pageNo, mMemberIds,1);//1-普通查询，2：自动刷新查询
    }

    /**
     * UI
     */
    private String mMemberIds;
    private void initUI() {
        initHead();
        initAdapter();
        initRefresh();
        initAutoRefresh();//自动刷新
    }

    /**
     * 头部
     */
    @BindView(R.id.parent)
    LinearLayout parent;
    private void initHead() {
        OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        TextView mTvHeadTitle = findViewById(R.id.tv_head_title);
        mTvHeadTitle.setText("拜访地图");
        ImageView mIvHeadRight = findViewById(R.id.iv_head_right);
        mIvHeadRight.setImageResource(R.drawable.icon_map);
        ImageView mIvHeadRight2 = findViewById(R.id.iv_head_right2);
        mIvHeadRight2.setImageResource(R.drawable.y_ic_frame);
    }

    /**
     * 初始化适配器
     */
    RecyclerView mRvMap;
    MapAdapter mMapAdapter;
    private int pageNo = 1;
    LinearLayoutManager manager;
    private int position;// ListView停止滑动时第一个显示的位置
    private int pageNoAuto = 1;//刷新
    private void initAdapter() {
        mRvMap = findViewById(R.id.rv_map);
        mRvMap.setHasFixedSize(true);
        manager=new LinearLayoutManager(this);
        mRvMap.setLayoutManager(manager);
        //添加分割线
        mRvMap.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mMapAdapter = new MapAdapter(context);
        mMapAdapter.openLoadAnimation();
        mRvMap.setAdapter(mMapAdapter);
        mMapAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                jumpActivityToLocation(baseQuickAdapter, i);//查看位置
            }
        });
        mMapAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                JumpActivityToBfhf(baseQuickAdapter, view, i);//拜访回放
            }
        });

        //RecyclerView滑动监听
        mRvMap.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 不滚动时保存当前滚动到的位置
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    position = manager.findFirstVisibleItemPosition();
                    pageNoAuto = (int) ((position + 1) / 15) + 1;//每页15条数据
                }
            }
        });
    }

    /**
     * 初始化刷新控件
     */
    RefreshLayout mRefreshLayout;
    private void initRefresh(){
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo=1;
                getP().loadData(null,pageNo, mMemberIds,1);//1-普通查询，2：自动刷新查询
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().loadData(null,pageNo, mMemberIds,1);//1-普通查询，2：自动刷新查询
            }
        });
    }

    /**
     * 自动刷新
     */
    public Disposable sDisposable;
    private boolean isScroll = true;// 是否自动刷新列表，1：界面运行，listview停止滚动--刷新2：界面暂停，listview滚动--不刷新
    private void initAutoRefresh() {
        //异步线程
        sDisposable= Observable.interval(60, TimeUnit.SECONDS)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long count) throws Exception {
                        if (isScroll) {
                            getP().loadData(null,pageNoAuto, mMemberIds,2);//1-普通查询，2：自动刷新查询
                        }
                    }
                });
    }

    /**
     * 跳转到“拜访回放”
     */
    private void JumpActivityToBfhf(BaseQuickAdapter baseQuickAdapter, View view, int i) {
        try{
            TrackListBean.TrackList item=(TrackListBean.TrackList)baseQuickAdapter.getData().get(i);
            switch (view.getId()){
                case R.id.tv_track:
                    int mid=item.getUserId();
                    String name=item.getUserNm();
                    String date = item.getTimes().substring(0, 10);// 截取日期：0000-00-00
                    Router.newIntent(context)
                            .putInt(ConstantUtils.Intent.TYPE,1)
                            .putInt(ConstantUtils.Intent.USER_ID,mid)
                            .putString(ConstantUtils.Intent.USER_NAME,name)
                            .putString(ConstantUtils.Intent.DATE,date)
                            .to(MapPlaybackActivity.class)
                            .launch();
                    break;
            }
        }catch (Exception e){
            ToastUtils.showCustomToast("类型转换异常");
        }
    }

    /**
     * 跳转到“查看位置”
     */
    private void jumpActivityToLocation(BaseQuickAdapter baseQuickAdapter, int i) {
        try{
            TrackListBean.TrackList item=(TrackListBean.TrackList)baseQuickAdapter.getData().get(i);
            Router.newIntent(context)
                    .putInt(ConstantUtils.Intent.TYPE,1)
                    .putSerializable("trackList",item)
                    .to(MapLocationActivity.class)
                    .launch();
        }catch (Exception e){
            ToastUtils.showCustomToast("类型转换异常");
        }
    }

    /**
     * 跳转到“分布图”
     */
    private void jumpActivityToFbt() {
        //默认自己；如果“员工分布图”有设置“地图中心点”；以它为准
        int userId = Integer.valueOf(SPUtils.getID());
        String userName = SPUtils.getUserName();
        if (MyStringUtil.isNotEmpty(SPUtils.getSValues(ConstantUtils.Sp.MAP_MEMBER_USER_ID))){
            userId = Integer.valueOf(SPUtils.getSValues(ConstantUtils.Sp.MAP_MEMBER_USER_ID));
            userName = SPUtils.getSValues(ConstantUtils.Sp.MAP_MEMBER_USER_NAME);
        }
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.TYPE,2)
                .putInt(ConstantUtils.Intent.USER_ID,userId)//用户id
                .putString(ConstantUtils.Intent.USER_NAME,userName)//用户名称
                .putString(ConstantUtils.Intent.DATE, MyTimeUtils.getDate_nyr(0))//今天
                .to(MapMemberFBTActivity.class)
                .launch();
    }

    //TODO******************************************************************************************
    @OnClick({R.id.iv_head_right2,R.id.iv_head_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_right2:
                if(null == mTreeDatas || mTreeDatas.isEmpty()){
                    getP().loadDataFrame(context);
                }else{
                    showDialogMember();
                }
                break;

            case R.id.iv_head_right:// 分布图
                jumpActivityToFbt();
                break;
        }
    }


    //TODO ************************************生命周期*****************************************************

    @Override
    protected void onResume() {
        super.onResume();
        isScroll = true;// 界面暂停时，要不刷新列表
    }
    @Override
    protected void onPause() {
        super.onPause();
        isScroll = false;// 界面暂停时，要不刷新列表
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存数据库
        if(tempList!=null && !tempList.isEmpty()){
            MyDataUtils.getInstance().saveTrackList(tempList,SPUtils.getID(), SPUtils.getCompanyId());
        }
        Constans.branchMap.clear();
        Constans.memberMap.clear();
        Constans.ziTrueMap.clear();
        Constans.ParentTrueMap.clear();
        Constans.ParentTrueMap2.clear();//0:没选中，1：全选中，2：部分选中
        if (sDisposable!=null) sDisposable.dispose();//关闭自动刷新
    }


    //TODO ***********************************接口处理**********************************************
    private List<TrackListBean.TrackList> tempList=new ArrayList<>();//数据库临时要缓存的数据
    //设置显示数据
    public void setShowData(TrackListBean parseObject){
        String ztStr = parseObject.getZt();// 1:只有自己
        List<TrackListBean.TrackList> rows = parseObject.getRows();
        if(rows!=null && rows.size()>0){
            for (int i = 0; i < rows.size(); i++) {
                TrackListBean.TrackList trackList = rows.get(i);
                if ("离职".equals(trackList.getZt())) {
                    rows.remove(trackList);
                }
            }
            //保存数据库(改为界面销毁时保存数据，减少缓存的次数)
            tempList.clear();
            tempList.addAll(rows);
            //MyDataUtils.getInstance().saveTrackList(rows,SPUtils.getUser_id(),SPUtils.getCompanyId());
        }
        //TODO 放遍历的区域外（不让没数据有问题）
        refreshAdapterMap(rows);
    }

    //设置显示数据-自动刷新
    public void setShowDataAuto(TrackListBean parseObject){
        List<TrackListBean.TrackList> rows = parseObject.getRows();
        if(rows!=null && rows.size()>0){
            List<TrackListBean.TrackList> datas=mMapAdapter.getData();
            for (int i = 0; i < rows.size(); i++) {
                TrackListBean.TrackList track1 = rows.get(i);
                if ("离职".equals(track1.getZt())) {
                } else {
                    for (int k=0;k<datas.size();k++){
                        TrackListBean.TrackList track2 = datas.get(k);
                        if(track1.getUserId()==track2.getUserId()){
                            datas.set(k,track1);
                            break;
                        }
                    }
                }
            }
//            mPlaybackAdapter.setNewData(datas);
            mMapAdapter.notifyDataSetChanged();
            // 自动刷新
            if (isScroll) {
                if(position!=-1){
                    mRvMap.scrollToPosition(position);
                }
            }
        }
    }

    /**
     * 关闭刷新
     */
    public void closeRefresh(){
        //关闭刷新，加载更多
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    /**
     * 刷新适配器
     */
    public void refreshAdapterMap(List<TrackListBean.TrackList> dataList){
        if(!(dataList!=null && dataList.size()>0)){
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if(pageNo==1){
            //上拉刷新
            mMapAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
            mRvMap.scrollToPosition(0);//移动到顶部
        }else{
            //加载更多
            mMapAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
    }

    private List<TreeBean> mTreeDatas = new ArrayList<>();
    private MyTreeDialog mTreeDialog;
    private void showDialogMember(){
        try {
            if(null == mTreeDialog){
                HashMap<Integer, Integer> checkMap = new HashMap<>();
                String mapStr = SPUtils.getSValues(ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS_MAP);// 成员ID数组
                if(!MyStringUtil.isEmpty(mapStr)){
                    checkMap = JSON.parseObject(mapStr, HashMap.class);
//                    checkMap.putAll((HashMap<Integer, Integer>)JSON.parsea(mapStr));
                }
                mTreeDialog = new MyTreeDialog(context, mTreeDatas, checkMap,true);
            }
            mTreeDialog.title("选择部门,员工").show();
            mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
                @Override
                public void onOkListener(String checkIds, String clientTypeIds,Map<Integer, Integer> checkMap) {
                    mMemberIds = checkIds;
                    SPUtils.setValues(ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS, checkIds);// 成员ID数组
                    SPUtils.setValues(ConstantUtils.Sp.TREE_BFDT_MEMBER_BRANCH_IDS_MAP, JSON.toJSONString(checkMap));// 成员ID数组
                    //请求数据
                    pageNo = 1;
                    pageNoAuto = 1;
                    getP().loadData(context,pageNo, mMemberIds,1);
                }
            });
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //请求员工数据--tree
    public void refreshAdapterMemberTree(List<TreeBean> mDatas){
        this.mTreeDatas = mDatas;
        showDialogMember();
    }

    /**
     * 查询数据库
     */
    public void queryDB(){
        List<DMapTrackBean> mapTrackBeans=MyDataUtils.getInstance().queryTrackList(SPUtils.getID(),SPUtils.getCompanyId());
        if(mapTrackBeans!=null && !mapTrackBeans.isEmpty()){
            List<TrackListBean.TrackList> trackList=new ArrayList<>();
            for (int i=0;i<mapTrackBeans.size();i++){
                DMapTrackBean bean =mapTrackBeans.get(i);
                TrackListBean.TrackList track = new TrackListBean.TrackList();
                track.setUserId(bean.getUserId());
                track.setUserNm(bean.getUserNm());
                track.setUserHead(bean.getUserHead());
                track.setUserTel(bean.getUserTel());
                track.setAddress(bean.getAddress());
                track.setLocation(bean.getLocation());
                track.setMemberJob(bean.getMemberJob());
                track.setTimes(bean.getTimes());
                track.setZt(bean.getZt());
                trackList.add(track);
            }
            refreshAdapterMap(trackList);
        }
    }

}
