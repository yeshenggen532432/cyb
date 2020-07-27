package com.qwb.view.step.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.adapter.Step4Right2Adapter;
import com.qwb.view.step.adapter.Step5Left2Adapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep4Bean;
import com.qwb.view.step.parsent.PStep4;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.table.TableHorizontalScrollView;
import com.qwb.widget.table.TableOnScrollListener;
import com.qwb.view.step.model.Step4Bean;
import com.qwb.view.step.model.QueryXstypeBean;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：拜访步骤4：销售小结
 */
public class Step4Activity extends XActivity<PStep4> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_step4;
    }

    @Override
    public PStep4 newP() {
        return new PStep4();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doInitUI();
        getP().queryToken(null);
    }

    /**
     * 初始化Intent
     */
    private String mKhNm;//客户名称
    private String cId;// 客户ID
    private String count4 = "0";//0:未上传，1:已上传
    private String mPdateStr;//补拜访时间
    private void initIntent() {
        Intent intent = getIntent();
        if (null != intent) {
            cId = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
            mKhNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
            count4 = intent.getStringExtra(ConstantUtils.Intent.STEP);
            mPdateStr = intent.getStringExtra("pdate");//补拜访时间
        }
    }

    //根据不同的类型处理不同的显示
    private void doInitUI() {
        if ("1".equals(count4)) {// 已上传
            getP().queryData(context,cId,mPdateStr);//加载上传提交信息
            mTvHeadRight.setText("修改");
        } else {
            mTvHeadRight.setText("提交");
        }
        mTvHeadTitle.setText(mKhNm);

        if(!MyUtils.isEmptyString(mPdateStr)){
            mTvCallOnDate.setText(mPdateStr);
        }else{
            mTvCallOnDate.setText(MyTimeUtils.getTodayStr());
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

    @BindView(R.id.tv_callOnDate)
    TextView mTvCallOnDate;
    private void initOtherUI() {
    }

    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.head_right)
    View mHeadRight;
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
        //OnNoMoreClickListener:避免重复点击
        mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
            @Override
            protected void OnMoreClick(View view) {
                addData();
            }
        });
    }

    private RecyclerView mRvLeft;
    private RecyclerView mRvRight;
    private TableHorizontalScrollView mSvWare;
    private Step5Left2Adapter mLeftAdapter;
    private Step4Right2Adapter mRightAdapter;
    @BindView(R.id.tv_table_title_left)
    TextView mTvTableTitleLeft;
    public void initTableView() {
        mRvLeft = findViewById(R.id.rv_left);
        mRvRight = findViewById(R.id.rv_right);
        mSvWare = findViewById(R.id.sv_ware);

        mRvLeft.setLayoutManager(new LinearLayoutManager(this));
        mRvLeft.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mLeftAdapter = new Step5Left2Adapter();
        mRvLeft.setAdapter(mLeftAdapter);
        mRvRight.setLayoutManager(new LinearLayoutManager(this));
        mRvRight.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_b)
                .sizeResId(R.dimen.dp_0_5)
                .showLastDivider()
                .build());
        mRightAdapter = new Step4Right2Adapter(context);
        mRvRight.setAdapter(mRightAdapter);
        mRightAdapter.setOnChildListener(new Step4Right2Adapter.OnChildListener() {
            @Override
            public void onChildListener(int tag, int position, ShopInfoBean.Data item) {
                mCurrentItem = item;
                mCurrentPosition = position;
                switch (tag){
                    case Step4Right2Adapter.TAG_DEl:
                        showDialogDel();
                        break;
                    case Step4Right2Adapter.TAG_XSTP:
                        if(null == mXstpDatas || mXstpDatas.isEmpty()){
                            getP().queryXsTp(context);
                        }else{
                            showDialogXstp(mXstpDatas);
                        }
                        break;
                }
            }
        });

        //设置两个列表的同步滚动
        setSyncScrollListener();

        //添加商品
        mTvTableTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(MyStringUtil.isEmpty(cId)){
                        ToastUtils.showShort(context,"先选择客户");
                        return;
                    }
                    ArrayList<ShopInfoBean.Data> dataList = (ArrayList<ShopInfoBean.Data>) mRightAdapter.getData();
                    Router.newIntent(context)
                            .putString(ConstantUtils.Intent.TYPE,"4")
                            .putString(ConstantUtils.Intent.CLIENT_ID, cId)
                            .putParcelableArrayList(ConstantUtils.Intent.CHOOSE_WARE,dataList)
                            .requestCode(ConstantUtils.Intent.REQUEST_STEP_4_CHOOSE_GOODS)
                            .to(ChooseWareActivity3.class)
                            .launch();
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantUtils.Intent.REQUEST_STEP_4_CHOOSE_GOODS && resultCode==ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS){
            if(data!=null){
                ArrayList<ShopInfoBean.Data> datas = data.getParcelableArrayListExtra(ConstantUtils.Intent.CHOOSE_WARE_NEW);
                if(datas!=null && datas.size() > 0){
                    mLeftAdapter.addData(datas);
                    mRightAdapter.addData(datas);
                    refreshAdapterRight();
                }
            }
        }
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
        Map<Integer,Integer> repeatMap = new HashMap<>();
        List<ShopInfoBean.Data> dataList = mLeftAdapter.getData();
        for (ShopInfoBean.Data data : dataList) {
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


    //--------------------------------dialog:开始---------------------------------------------------
    private ShopInfoBean.Data mCurrentItem;
    private int mCurrentPosition;

    //对话框-销售类型
    private List<QueryXstypeBean.QueryXstype> mXstpDatas = new ArrayList<>();
    public void showDialogXstp(List<QueryXstypeBean.QueryXstype> xstpDatas){
        this.mXstpDatas = xstpDatas;
        String wareNm = mCurrentItem.getWareNm();
        if(null == mXstpDatas || mXstpDatas.isEmpty()){
            ToastUtils.showCustomToast("没有销售类型");
            return;
        }

        final ArrayList<DialogMenuItem> dialogMenuItems = new ArrayList<>();
        for(QueryXstypeBean.QueryXstype xstp : mXstpDatas) {
            dialogMenuItems.add(new DialogMenuItem(xstp.getXstpNm(), xstp.getId()));
        }
        NormalListDialog dialog = new NormalListDialog(context,dialogMenuItems);
        dialog.title(wareNm).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int i, long id) {
                try{
                    String currentXstp = mCurrentItem.getCurrentXstp();
                    String xstpNm = dialogMenuItems.get(i).mOperName;
                    if(!xstpNm.equals(currentXstp)){
                        mCurrentItem.setCurrentXstp(xstpNm);
                        List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
                        dataList.set(mCurrentPosition, mCurrentItem);
                        refreshAdapterRight();
                    }
                }catch (Exception e){
                    ToastUtils.showError(e);
                }
                MyKeyboardUtil.closeKeyboard(context);//强制关闭软键盘
            }
        });
    }

    //对话框-删除商品
    public void showDialogDel(){
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("你确定删除''"+mCurrentItem.getWareNm()+"''吗？").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                mLeftAdapter.getData().remove(mCurrentPosition);
                mRightAdapter.getData().remove(mCurrentPosition);
                refreshAdapterRight();
            }
        });
    }
    //--------------------------------dialog:结束----------------------------------------------------


    //TODO ******************************接口******************************
    /**
     * /显示订单信息
     */
    public void showData(List<ShopInfoBean.Data> datas){
        if(datas!=null){
            mLeftAdapter.setNewData(datas);
            mRightAdapter.setNewData(datas);
            refreshAdapterRight();
        }
    }
    /**
     * 提交或修改数据
     */
    private String mJsonStr;
    private void addData() {
        List<ShopInfoBean.Data> dataList = mRightAdapter.getData();
        if(null != dataList && dataList.isEmpty()){
            ToastUtils.showLongCustomToast("请添加商品+");
            return;
        }
        // 品项，销售类型，数量，单位，单价，总价，操作
        List<Step4Bean> list = new ArrayList<>();
        list.clear();
        for (ShopInfoBean.Data data: dataList) {
            Step4Bean xiaoshou = new Step4Bean();
            xiaoshou.setWid("" + data.getWareId());

            if(MyUtils.isEmptyString(data.getCurrentDhl())){
                xiaoshou.setDhNum("0");
            }else{
                xiaoshou.setDhNum(data.getCurrentDhl());
            }
            if(MyUtils.isEmptyString(data.getCurrentSxl())){
                xiaoshou.setSxNum("0");
            }else{
                xiaoshou.setSxNum(data.getCurrentSxl());
            }
            if(MyUtils.isEmptyString(data.getCurrentKcl())){
                xiaoshou.setKcNum("0");
            }else{
                xiaoshou.setKcNum(data.getCurrentKcl());
            }
            if(MyUtils.isEmptyString(data.getCurrentDd())){
                xiaoshou.setDdNum("0");
            }else{
                xiaoshou.setDdNum(data.getCurrentDd());
            }
            if(MyUtils.isEmptyString(data.getCurrentBz())){
                xiaoshou.setRemo("0");
            }else{
                xiaoshou.setRemo(data.getCurrentBz());
            }

            xiaoshou.setXstp(data.getCurrentXstp());

            //新鲜值
            String xxzStr = data.getCurrentXxz();
            if (!MyUtils.isEmptyString(xxzStr)) {
                if ("正常".equals(xxzStr)) {
                    xiaoshou.setXxd("0");
                }else if(xxzStr.startsWith("临期")) {
                    xiaoshou.setXxd(xxzStr.substring(2, xxzStr.length()));
                }
            }
            list.add(xiaoshou);
        }
        mJsonStr = JSON.toJSONString(list);//拼接的json字符串

        if(!MyNetWorkUtil.isNetworkConnected()){
            ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
            showDialogCache();
            return;
        }
        getP().addData(context,cId,mPdateStr,count4,mJsonStr, mQueryToken);
    }

    /**
     * 下单提交数据成功
     */
    public void submitSuccess(){
        Intent data = new Intent();
        data.putExtra(ConstantUtils.Intent.SUCCESS, true);
        setResult(0,data);
        ActivityManager.getInstance().closeActivity(context);
    }

    private int mErrorCount;
    public void submitDataError(){
        mErrorCount++;
        if(mErrorCount > 1){
            showDialogCache();
        }
    }

    public void showDialogCache(){
        NormalDialog dialog = new NormalDialog(context);
        dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
        dialog.setOnBtnClickL(null, new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                saveCacheData();
            }
        });
    }

    //保存缓存数据
    public void saveCacheData(){
        ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
        DStep4Bean bean = new DStep4Bean();
        bean.setUserId(SPUtils.getID());
        bean.setCompanyId(SPUtils.getCompanyId());
        bean.setCount(count4);
        bean.setCid(cId);
        bean.setKhNm(mKhNm);
        bean.setXsxj(mJsonStr);
        bean.setTime(MyTimeUtils.getNowTime());
        MyDataUtils.getInstance().saveStep4(bean);

        Intent data = new Intent();
        data.putExtra(ConstantUtils.Intent.SUCCESS, true);
        data.putExtra(ConstantUtils.Intent.COUNT, 2);
        setResult(0, data);
        ActivityManager.getInstance().closeActivity(context);
    }


    /**
     * 设置两个列表的同步滚动
     */
    private final RecyclerView.OnScrollListener mLeftOSL = new TableOnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 当楼层列表滑动时，单元（房间）列表也滑动
            mRvRight.scrollBy(dx, dy);
        }
    };
    private final RecyclerView.OnScrollListener mRightOSL = new TableOnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // 当单元（房间）列表滑动时，楼层列表也滑动
            mRvLeft.scrollBy(dx, dy);
        }
    };
    private void setSyncScrollListener() {
        mRvLeft.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private int mLastY;
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                // 当列表是空闲状态时
                if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    onTouchEvent(rv, e);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                // 若是手指按下的动作，且另一个列表处于空闲状态
                if (e.getAction() == MotionEvent.ACTION_DOWN && mRvRight.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    // 记录当前另一个列表的y坐标并对当前列表设置滚动监听
                    mLastY = rv.getScrollY();
                    rv.addOnScrollListener(mLeftOSL);
                } else {
                    // 若当前列表原地抬起手指时，移除当前列表的滚动监听
                    if (e.getAction() == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
                        rv.removeOnScrollListener(mLeftOSL);
                    }
                }
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        mRvRight.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            private int mLastY;
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    onTouchEvent(rv, e);
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN && mRvLeft.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                    mLastY = rv.getScrollY();
                    rv.addOnScrollListener(mRightOSL);
                } else {
                    if (e.getAction() == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
                        rv.removeOnScrollListener(mRightOSL);
                    }
                }
            }
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
        mSvWare.setScrollViewListener(new TableHorizontalScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(TableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                mRvLeft.removeOnScrollListener(mLeftOSL);
                mRvRight.removeOnScrollListener(mRightOSL);
            }
        });
    }

    //避免重复的token
    private String mQueryToken;
    public void doToken(String data){
        mQueryToken = data;
    }










}
