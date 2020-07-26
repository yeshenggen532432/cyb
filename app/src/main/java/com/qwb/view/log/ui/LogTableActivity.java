package com.qwb.view.log.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.common.adapter.AbsViewHolder;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.log.parsent.PLogTable;
import com.qwb.widget.RefreshParams;
import com.qwb.view.table.model.TableModel;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.widget.MyDatePickerDialog;
import com.xmsx.cnlife.widget.SyncHorizontalScrollView;
import com.xmsx.cnlife.widget.pullrefresh.AbPullToRefreshView;
import com.qwb.view.tree.SimpleTreeAdapter_map;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.member.model.MemberBean;
import com.qwb.view.log.model.RizhiListBean;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 *  工作报表 1：日报报表（默认查看今天）；2：周报报表（默认本周）；3：月报报表（默认本月）
 */
public class LogTableActivity extends XActivity<PLogTable> implements View.OnClickListener{

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_log_table;
    }

    @Override
    public PLogTable newP() {
        return new PLogTable();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initDate();
        initUI();
        createPopup();
        setListener();//上拉，下拉监听
        refreshLoadData();
    }

    private int type =1;//1:日报（默认的），2：周报，3：月报
    private String startDate,endDate;//开始时间，结束时间
    private void initDate() {
        startDate= MyTimeUtils.getTodayStr();//今日
        endDate= MyTimeUtils.getTodayStr();//今日
    }

    /**
     * 头部
     */
    private TextView mTVHeadTitle;
    private void initHead() {
        OtherUtils.setStatusBarColor(context);//状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {Router.pop(context);
            }
        });
        findViewById(R.id.tv_head_right).setOnClickListener(this);
        mTVHeadTitle = (TextView) findViewById(R.id.tv_head_title);
        TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
        mTVHeadTitle.setText("日报汇报报表");
        tv_headRight.setText("筛选");
    }

    /**
     * UI
     */
    private void initUI(){
        initHead();
        initTable();//初始化表格
    }

    //TODO ***********************************表格的一些设置**********************************
    /**
     * 初始化表格
     */
    private TextView tv_table_title_left;
    private LinearLayout right_title_container;
    private ListView leftListView;
    private ListView rightListView;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private SyncHorizontalScrollView titleHorScv;
    private SyncHorizontalScrollView contentHorScv;
    private AbPullToRefreshView pulltorefreshview;
    private int pageNo = 1;
    private void initTable() {
        pulltorefreshview = (AbPullToRefreshView) findViewById(R.id.pulltorefreshview);
        tv_table_title_left = (TextView) findViewById(R.id.tv_table_title_left);
        tv_table_title_left.setText("填报人");
        leftListView = (ListView) findViewById(R.id.left_container_listview);
        rightListView = (ListView) findViewById(R.id.right_container_listview);
        right_title_container = (LinearLayout) findViewById(R.id.right_title_container);
        getLayoutInflater().inflate(R.layout.x_work_table_right_title1, right_title_container);
        titleHorScv = (SyncHorizontalScrollView) findViewById(R.id.title_horsv);
        contentHorScv = (SyncHorizontalScrollView) findViewById(R.id.content_horsv);
        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);
        findTitleTextViewIds();
        initTableView();
    }

    /**
     * 初始化标题的TextView的item引用
     */
    TextView tv_table_title_0;
    TextView tv_table_title_1;
    TextView tv_table_title_2;
    TextView tv_table_title_3;
    TextView tv_table_title_4;
    private void findTitleTextViewIds() {
        tv_table_title_0=(TextView) findViewById(R.id.tv_table_title_0);
        tv_table_title_1=(TextView) findViewById(R.id.tv_table_title_1);
        tv_table_title_2=(TextView) findViewById(R.id.tv_table_title_2);
        tv_table_title_3=(TextView) findViewById(R.id.tv_table_title_3);
        tv_table_title_4=(TextView) findViewById(R.id.tv_table_title_4);
    }

    public void initTableView() {
        mLeftAdapter = new AbsCommonAdapter<TableModel>(context, R.layout.x_table_left_item) {
            @Override
            public void convert(AbsViewHolder helper, TableModel item, int pos) {
                TextView tv_table_content_left = helper.getView(R.id.tv_table_content_item_left);
                tv_table_content_left.setText(item.getLeftTitle());
            }
        };
        mRightAdapter = new AbsCommonAdapter<TableModel>(context, R.layout.x_work_table_right_item1) {
            @Override
            public void convert(AbsViewHolder helper, final TableModel item, final int pos) {
                TextView tv_table_content_right_item0 = helper.getView(R.id.tv_table_content_right_item0);
                TextView tv_table_content_right_item1 = helper.getView(R.id.tv_table_content_right_item1);
                TextView tv_table_content_right_item2 = helper.getView(R.id.tv_table_content_right_item2);
                TextView tv_table_content_right_item3 = helper.getView(R.id.tv_table_content_right_item3);
                TextView tv_table_content_right_item4 = helper.getView(R.id.tv_table_content_right_item3);

                tv_table_content_right_item0.setText(item.getText0());
                tv_table_content_right_item1.setText(item.getText1());
                tv_table_content_right_item2.setText(item.getText2());
                tv_table_content_right_item3.setText(item.getText3());
                tv_table_content_right_item4.setText(item.getText4());
            }
        };
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);
        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, LogDetailActivity.class);
                RizhiListBean.RizhiList workTableList = mWorkTableList.get(position);
                intent.putExtra(ConstantUtils.Intent.LOOK_WORK_TABLE_DETAILS, workTableList);
                startActivity(intent);
            }
        });
        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, LogDetailActivity.class);
                RizhiListBean.RizhiList workTableList = mWorkTableList.get(position);
                intent.putExtra(ConstantUtils.Intent.LOOK_WORK_TABLE_DETAILS, workTableList);
                startActivity(intent);
            }
        });
    }


    /**
     * 上拉，下拉监听
     */
    int mState = RefreshParams.REFRESH_DATA;//默认上拉状态;下拉加载更多
    public void setListener() {
        pulltorefreshview.setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(AbPullToRefreshView view) {
                refreshLoadData();//刷新加载数据(复用：下拉刷新，筛选时间，客户等级，搜索)
            }
        });
        pulltorefreshview.setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
            @Override
            public void onFooterLoad(AbPullToRefreshView view) {
                String mid = getMid();//选择汇报人ids
                mState =RefreshParams.LOAD_DATA;
                getP().loadData(context,pageNo, type,startDate,endDate,mid,String.valueOf(isHuibao));
            }
        });
    }

    /**
     * 刷新加载数据(复用：下拉刷新)
     */
    private void refreshLoadData() {
        String mid = getMid();//选择汇报人ids
        pageNo=1;
        mState = RefreshParams.REFRESH_DATA;
        pulltorefreshview.setLoadMoreEnable(true);
        getP().loadData(context,pageNo, type,startDate,endDate,mid,String.valueOf(isHuibao));
    }
    //TODO ***********************************以上是：表格的一些设置**********************************

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right://筛选
                if(mDatas!=null && mDatas.size()>0){
                    //公用的ids先清空；再重新赋值
                    Constans.ziTrueMap.clear();
                    Constans.ParentTrueMap2.clear();
                    Constans.ziTrueMap.putAll(ziTrueMap);
                    Constans.ParentTrueMap2.putAll(ParentTrueMap2);
                    refreshAdapterFrame(mDatas,false);
                    mEasyPop.showAsDropDown(mTVHeadTitle);
                    setPopData();//窗体的一些显示设置
                }else{
                    //公用的ids先清空；再重新赋值
                    Constans.ziTrueMap.clear();
                    Constans.ParentTrueMap2.clear();
                    getP().loadDataFrame(context);
                }
                break;
            case R.id.tv_startTime://开始时间
                timeType = 0;
                showChooseDate();
                break;
            case R.id.tv_endTime://结束时间
                timeType = 1;
                showChooseDate();
                break;
        }
    }

    /*
	 * 筛选--窗体
	 */
    private TextView tv_startTime;
    private TextView tv_endTime;
    private CheckBox cb_weihuibao;
    private EasyPopup mEasyPop;
    private RadioButton radio0,radio1,radio2;//0:月报；1：周报；2：日报
    private CheckBox cb_benzhou,cb_shangzhou,cb_zidingyi;//今天（本周，本月）；昨天（上周，上月）；自定义时间
    private int tempType=1,tempTimeType=1,isHuibao=1;//临时的（日报，周报，月报）；临时的（今天，昨天，自定义时间）;临时的汇报
    private void createPopup() {
        mEasyPop = new EasyPopup(context)
                .setContentView(R.layout.x_popup_work_table)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                //变暗的背景颜色
                .setDimColor(Color.BLACK)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .createPopup();
        tv_startTime=mEasyPop.getView(R.id.tv_startTime);
        tv_endTime=mEasyPop.getView(R.id.tv_endTime);
        tv_startTime.setText(MyTimeUtils.getToday_nyr());
        tv_endTime.setText(MyTimeUtils.getToday_nyr());
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        tv_startTime.setEnabled(false);
        tv_endTime.setEnabled(false);
        tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
        tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);

        //显示隐藏组织架构(默认隐藏：只有管理员才可查看)
        mTree=mEasyPop.getView(R.id.id_tree);//组织架构
        LinearLayout ll_hide =mEasyPop.getView(R.id.ll_hide);
        LinearLayout ll_huibaoren =mEasyPop.getView(R.id.ll_huibaoren);
        cb_weihuibao=mEasyPop.getView(R.id.cb_weihuibao);
        //成员类型:0.普通成员1.单位超级管理员 2.单位管理员 3.部门管理员
        String isUnitmng = SPUtils.getSValues("isUnitmng");
        if("1".equals(isUnitmng) || "2".equals(isUnitmng)){
            ll_hide.setVisibility(View.VISIBLE);
            ll_huibaoren.setVisibility(View.VISIBLE);
            cb_weihuibao.setVisibility(View.VISIBLE);
        }else{
            ll_hide.setVisibility(View.GONE);
            ll_huibaoren.setVisibility(View.GONE);
            cb_weihuibao.setVisibility(View.GONE);
        }

        //重置
        mEasyPop.getView(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constans.ziTrueMap.clear();
                Constans.ParentTrueMap2.clear();
                refreshAdapterFrame(mDatas,false);
            }
        });

        //取消
        mEasyPop.getView(R.id.btn_quxiao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { mEasyPop.dismiss();  }
        });

        radio0=mEasyPop.getView(R.id.radio0);//月报
        radio1=mEasyPop.getView(R.id.radio1);//周报
        radio2=mEasyPop.getView(R.id.radio2);//日报
        cb_benzhou=mEasyPop.getView(R.id.cb_benzhou);//今天（本周，本月）
        cb_shangzhou=mEasyPop.getView(R.id.cb_shangzhou);//昨天（上周，上月）
        cb_zidingyi=mEasyPop.getView(R.id.cb_zidingyi);//自定义时间

        //确定
        mEasyPop.getView(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radio0.isChecked()){
                    type =3;
                }
                if(radio1.isChecked()){
                    type =2;
                }
                if(radio2.isChecked()){
                    type =1;
                }
                if(cb_benzhou.isChecked()){
                    tempTimeType =1;
                }
                if(cb_shangzhou.isChecked()){
                    tempTimeType =2;
                }
                if(cb_zidingyi.isChecked()){
                    tempTimeType =3;
                }
                if(cb_weihuibao.isChecked()){
                    isHuibao =2;
                }else{
                    isHuibao =1;
                }
                startDate=tv_startTime.getText().toString().trim();
                endDate=tv_endTime.getText().toString().trim();
                refreshLoadData();
                mEasyPop.dismiss();

                //公用的ids赋值给临时的ids
                ziTrueMap.clear();
                ParentTrueMap2.clear();
                ziTrueMap.putAll(Constans.ziTrueMap);
                ParentTrueMap2.putAll(Constans.ParentTrueMap2);
            }
        });

        //月报
        radio0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempType =3;
                cb_benzhou.setText("本月");
                cb_shangzhou.setText("上月");
                cb_benzhou.setChecked(true);
                cb_shangzhou.setChecked(false);
                cb_zidingyi.setChecked(false);
                tv_startTime.setText(MyTimeUtils.getFirstOfMonth());
                tv_endTime.setText(MyTimeUtils.getLastOfMonth());
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
            }
        });
        //周报
        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempType =2;
                cb_benzhou.setText("本周");
                cb_shangzhou.setText("上周");
                cb_benzhou.setChecked(true);
                cb_shangzhou.setChecked(false);
                cb_zidingyi.setChecked(false);
                tv_startTime.setText(MyTimeUtils.getFirstOfThisWeek());
                tv_endTime.setText(MyTimeUtils.getLastOfThisWeek());
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
            }
        });
        //日报
        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempType =1;
                cb_benzhou.setText("今天");
                cb_shangzhou.setText("昨天");
                cb_benzhou.setChecked(true);
                cb_shangzhou.setChecked(false);
                cb_zidingyi.setChecked(false);
                tv_startTime.setText(MyTimeUtils.getToday_nyr());
                tv_endTime.setText(MyTimeUtils.getToday_nyr());
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
            }
        });
        //今天--本周--本月
        cb_benzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tempType) {
                    case 1://日报
                        tv_startTime.setText(MyTimeUtils.getToday_nyr());
                        tv_endTime.setText(MyTimeUtils.getToday_nyr());
                        break;
                    case 2://周报
                        tv_startTime.setText(MyTimeUtils.getFirstOfThisWeek());
                        tv_endTime.setText(MyTimeUtils.getLastOfThisWeek());
                        break;
                    case 3://月报
                        tv_startTime.setText(MyTimeUtils.getFirstOfMonth());
                        tv_endTime.setText(MyTimeUtils.getLastOfMonth());
                        break;
                }
                cb_shangzhou.setChecked(false);
                cb_zidingyi.setChecked(false);
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
            }
        });
        //昨天--上周--上月
        cb_shangzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tempType) {
                    case 1://日报
                        tv_startTime.setText(MyTimeUtils.getYesterday());
                        tv_endTime.setText(MyTimeUtils.getYesterday());
                        break;
                    case 2://周报
                        tv_startTime.setText(MyTimeUtils.getFirstOfShangWeek());
                        tv_endTime.setText(MyTimeUtils.getLastOfShangWeek());
                        break;
                    case 3://月报
                        tv_startTime.setText(MyTimeUtils.getFirstOfShangMonth());
                        tv_endTime.setText(MyTimeUtils.getLastOfShangMonth());
                        break;
                }
                cb_benzhou.setChecked(false);
                cb_zidingyi.setChecked(false);
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
            }
        });
        //自定义时间
        cb_zidingyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cb_benzhou.setChecked(false);
                cb_shangzhou.setChecked(false);
                tv_startTime.setEnabled(true);
                tv_endTime.setEnabled(true);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray);
            }
        });
    }

    /**
     * 设置窗体的显示数据
     */
    public void setPopData(){
        switch (type){
            case 1://日报
                radio0.setChecked(false);
                radio1.setChecked(false);
                radio2.setChecked(true);
                cb_benzhou.setText("今天");
                cb_shangzhou.setText("昨天");
                break;
            case 2://周报
                radio0.setChecked(false);
                radio1.setChecked(true);
                radio2.setChecked(false);
                cb_benzhou.setText("本周");
                cb_shangzhou.setText("上周");
                break;
            case 3://月报
                radio0.setChecked(true);
                radio1.setChecked(false);
                radio2.setChecked(false);
                cb_benzhou.setText("本月");
                cb_shangzhou.setText("上月");
                break;
        }
        switch (tempTimeType){
            case 1://今天；本周；本月
                cb_benzhou.setChecked(true);
                cb_shangzhou.setChecked(false);
                cb_zidingyi.setChecked(false);
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                break;
            case 2://昨天；上周；上月
                cb_benzhou.setChecked(false);
                cb_shangzhou.setChecked(true);
                cb_zidingyi.setChecked(false);
                tv_startTime.setEnabled(false);
                tv_endTime.setEnabled(false);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                break;
            case 3://自定义时间
                cb_benzhou.setChecked(false);
                cb_shangzhou.setChecked(false);
                cb_zidingyi.setChecked(true);
                tv_startTime.setEnabled(true);
                tv_endTime.setEnabled(true);
                tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray);
                tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray);
                break;
        }
        //:1：汇报和2：未汇报
        switch (isHuibao){
            case 1:
                cb_weihuibao.setChecked(false);
                break;
            case 2:
                cb_weihuibao.setChecked(true);
                break;
        }
        tv_startTime.setText(startDate);//开始时间
        tv_endTime.setText(endDate);//结束时间
    }

    /**
     * 选择时间 timeType：1）开始时间  2）结束时间
     */
    private int timeType;
    private void showChooseDate() {
        String title=null;
        int year=0;
        int month=0;
        int day=0;
        switch (timeType) {
            case 0:
                title="开始时间";
                String startDateStr=tv_startTime.getText().toString().trim();
                String[] split=startDateStr.split("-");
                year=Integer.parseInt(split[0]);
                month=Integer.parseInt(split[1])-1;
                day=Integer.parseInt(split[2]);
                break;
            case 1:
                title="结束时间";
                String endDateStr=tv_endTime.getText().toString().trim();
                String[] split2=endDateStr.split("-");
                year=Integer.parseInt(split2[0]);
                month=Integer.parseInt(split2[1])-1;
                day=Integer.parseInt(split2[2]);
                break;
        }
        MyDatePickerDialog dialog=new MyDatePickerDialog(context, title, year, month, day, new MyDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, String timeStr) {
                switch (timeType) {
                        case 0:
                            tv_startTime.setText(timeStr);
                            break;
                        case 1:
                            tv_endTime.setText(timeStr);
                            break;
                        }
            }

            @Override
            public void onCancel() {
            }
        });
        dialog.show();
    }


    // TODO ---------------------------------接口--------------------------------------
    private List<RizhiListBean.RizhiList> mWorkTableList =new ArrayList<>();
    public void showData(List<RizhiListBean.RizhiList> list) {
        if(mState == RefreshParams.REFRESH_DATA){
            mWorkTableList.clear();
            mWorkTableList.addAll(list);
            pulltorefreshview.onHeaderRefreshFinish();
        }else{
            pulltorefreshview.onFooterLoadFinish();
            mWorkTableList.addAll(list);
        }
        //根据tp设置一些显示：1：日报；2：周报；3：月报
        switch (type){
            case 1:
                mTVHeadTitle.setText("日报汇报报表");
                tv_table_title_0.setText("填报时间");
                tv_table_title_1.setText("今日完成工作");
                tv_table_title_2.setText("未完成工作");
                tv_table_title_3.setText("需调协工作");
                tv_table_title_4.setText("备注");
                break;
            case 2:
                mTVHeadTitle.setText("周报汇报报表");
                tv_table_title_0.setText("填报时间");
                tv_table_title_1.setText("本周完成工作");
                tv_table_title_2.setText("本周工作总结");
                tv_table_title_3.setText("下周工作计划");
                tv_table_title_4.setText("需调协与帮助");
                break;
            case 3:
                mTVHeadTitle.setText("月报汇报报表");
                tv_table_title_0.setText("填报时间");
                tv_table_title_1.setText("本月工作内容");
                tv_table_title_2.setText("本月工作总结");
                tv_table_title_3.setText("本月工作计划");
                tv_table_title_4.setText("需帮助与支持");
                break;
        }

        if (list.size() > 0) {
            List<TableModel> mDatas = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                RizhiListBean.RizhiList data = list.get(i);
                TableModel tableMode = new TableModel();
//                tableMode.setOrgCode(data.getBfbz());
                tableMode.setLeftTitle(data.getMemberNm());//业务员
                tableMode.setText0(data.getFbTime());//填报时间
                tableMode.setText1(data.getGzNr());//今日完成工作
                tableMode.setText2(data.getGzZj());//未完成工作
                tableMode.setText3(data.getGzJh());//调协工作
                tableMode.setText4(data.getGzBz());//备注
                mDatas.add(tableMode);
            }
            boolean isMore;
            if (mState == RefreshParams.LOAD_DATA) {
                isMore = true;
            } else {
                isMore = false;
            }
            mLeftAdapter.addData(mDatas, isMore);
            mRightAdapter.addData(mDatas, isMore);
            //加载数据成功，增加页数
            pageNo++;
            if (mDatas.size() < 10) {
                pulltorefreshview.setLoadMoreEnable(false);
            }
            mDatas.clear();
        } else {
            if (mState == RefreshParams.REFRESH_DATA) {
                mLeftAdapter.clearData(true);
                mRightAdapter.clearData(true);
            } else if (mState == RefreshParams.LOAD_DATA) {
                pulltorefreshview.setLoadMoreEnable(false);
            }
        }
    }

    /**
     * 刷新适配器-结构图：部门，员工
     */
    private List<TreeBean> mDatas = new ArrayList<TreeBean>();
    private SimpleTreeAdapter_map<TreeBean> mFrameAdapter;
    private ListView mTree;
    public void refreshAdapterFrame(List<TreeBean> mDatas, boolean isShowPop){
        this.mDatas=mDatas;
        if ((mDatas != null && mDatas.size() > 0)) {
            if (mFrameAdapter == null) {
                try {
                    mFrameAdapter = new SimpleTreeAdapter_map<TreeBean>(mTree,context, mDatas, 0,false);
                    mTree.setAdapter(mFrameAdapter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                mFrameAdapter.notifyDataSetChanged();
            }
            //是否弹窗
            if(isShowPop){
                mEasyPop.showAsDropDown(mTVHeadTitle);
            }
        }
    }

    /**
     * 获取成员的ids
     */
    public HashMap<Integer, Boolean> ziTrueMap=new HashMap<Integer, Boolean>();//是否选中
    public HashMap<Integer, Integer> ParentTrueMap2=new HashMap<Integer, Integer>();//部门：0没选中，1全选中，2：部分选中
    private List<Integer> memIdList = new ArrayList<Integer>();
    private String getMid(){
        memIdList.clear();
        Iterator<Integer> iter = Constans.ziTrueMap.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();
            Boolean val = Constans.ziTrueMap.get(key);
            if (val) {
                MemberBean memberBean = Constans.memberMap.get(key);
                if (memberBean != null) {
                    Integer memberId = memberBean.getMemberId();
                    memIdList.add(memberId);
                }
            }
        }
        // 成员
        String entityStr = JSON.toJSONString(memIdList);
        String entityStr2 = entityStr.substring(1, entityStr.length() - 1);//去掉[]
        return entityStr2;
    }
}
