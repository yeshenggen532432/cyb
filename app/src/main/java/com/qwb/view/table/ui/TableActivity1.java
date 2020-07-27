package com.qwb.view.table.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.table.adapter.Table1LeftAdapter;
import com.qwb.view.table.adapter.Table1RightAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.table.parsent.PTable1;
import com.qwb.view.table.model.TableModel;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.MyDatePickerDialog;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.cnlife.widget.SyncHorizontalScrollView;
import com.qwb.view.table.model.MemberCallonBean;
import com.chiyong.t3.R;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 1-业务拜访统计表
 */

public class TableActivity1 extends XActivity<PTable1> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_table1;
    }

    @Override
    public PTable1 newP() {
        return new PTable1();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        doIntent();
        queryData();
    }

    /**
     * 刷新加载数据(复用：下拉刷新，筛选时间，客户等级，搜索)
     */
    String mDateStr, mClientLevelStr;//日期，客户等级
    private void queryData() {
        pageNo = 1;
        String khNm = mEtSearch.getText().toString().trim();
        String memberName = mEtSearchMemberName.getText().toString().trim();
        getP().queryData(context, pageNo,pageSize, mDateStr, khNm,memberName, mClientLevelStr);
    }

    private void doIntent() {
        mDateStr = MyTimeUtils.getTodayStr();
        mTvDate.setText(mDateStr);//默认今天
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initScreening();
        initTableView();
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
        mTvHeadTitle.setText("业务拜访统计表");
    }

    /**
     * 初始化筛选:排序，人员，搜索
     */
    @BindView(R.id.rl_search)
    LinearLayout mRlSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.et_search_member_name)
    EditText mEtSearchMemberName;
    @BindView(R.id.iv_search)
    TextView mIvSearch;
    @BindView(R.id.tv_date)
    TextView mTvDate;//筛选
    @BindView(R.id.tv_client_level)
    TextView mTvClientLevel;//客户等级
    @BindView(R.id.tv_search)
    TextView mTvSearch;//搜索
    private void initScreening() {
        mEtSearch.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                doSearchTextColor(s.toString());
            }
        });
        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseTime();
            }
        });
        mTvClientLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCustomerLevel();
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShowHideSearch();
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }

    private ListView leftListView;
    private ListView rightListView;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private SyncHorizontalScrollView titleHorScv;
    private SyncHorizontalScrollView contentHorScv;
    private int pageNo = 1;
    private int pageSize = 20;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    public void initTableView() {
        leftListView = findViewById(R.id.left_container_listview);
        rightListView = findViewById(R.id.right_container_listview);
        titleHorScv = findViewById(R.id.title_horsv);
        contentHorScv = findViewById(R.id.content_horsv);
        // 设置两个水平控件的联动
        titleHorScv.setScrollView(contentHorScv);
        contentHorScv.setScrollView(titleHorScv);

        mLeftAdapter = new Table1LeftAdapter(context);
        mRightAdapter = new Table1RightAdapter(context);
        leftListView.setAdapter(mLeftAdapter);
        rightListView.setAdapter(mRightAdapter);

        //上拉，下拉监听
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                queryData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                String khNm = mEtSearch.getText().toString().trim();
                String memberName = mEtSearchMemberName.getText().toString().trim();
                getP().queryData(context, pageNo,pageSize, mDateStr, khNm,memberName, mClientLevelStr);
            }
        });
    }


    // ---------------------------------接口--------------------------------------
    public void showData(List<MemberCallonBean.YwCanllon> list) {
        try {
            if (list == null) {
                list = new ArrayList<>();
            }
            List<TableModel> modelList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                MemberCallonBean.YwCanllon data = list.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setOrgCode(data.getBfbz());
                tableMode.setLeftTitle(data.getMemberNm());
                tableMode.setText0(data.getTimed());
                tableMode.setText1(data.getKhNm());
                tableMode.setText2(data.getBfbz() + "☞");
                tableMode.setText3(data.getKhdjNm());
                tableMode.setText4(data.getId() + "");//拜访id
                modelList.add(tableMode);
            }

            boolean isMore;
            if (pageNo > 1) {
                isMore = true;
                mRefreshLayout.finishLoadMore();
            } else {
                isMore = false;
                mRefreshLayout.finishRefresh();
            }
            mLeftAdapter.addData(modelList, isMore);
            mRightAdapter.addData(modelList, isMore);
            mRefreshLayout.setNoMoreData(false);
            if (list.size() < pageSize) {
                mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                ToastUtils.showCustomToast("没有更多数据");
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }


    //TODO*************************************************************************************************************************************
    //TODO*************************************************************************************************************************************

    /**
     * 筛选时间
     */
    private void showDialogChooseTime() {
        new MyDatePickerDialog(context, "筛选时间", mDateStr, new MyDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, String timeStr) {
                mDateStr = timeStr;
                mTvDate.setText(timeStr);
                queryData();
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    /**
     * 客户等级
     */
    private List<String> mCustomerLevelList = new ArrayList<>();
    private void doCustomerLevel() {
        if (MyCollectionUtil.isEmpty(mCustomerLevelList)) {
            getP().queryDataClientLevel(context);
        } else {
            showDialogCustomerLevel(mCustomerLevelList);
        }
    }

    /**
     * 客户等级
     */
    public void showDialogCustomerLevel(List<String> datas) {
        if (MyCollectionUtil.isNotEmpty(datas) && MyCollectionUtil.isEmpty(mCustomerLevelList)) {
            this.mCustomerLevelList.addAll(datas);
        }

        final ArrayList<DialogMenuItem> items = new ArrayList<>();
        if (MyCollectionUtil.isNotEmpty(datas)) {
            for (String s : datas) {
                items.add(new DialogMenuItem(s, 0));
            }
        }
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择客户类型").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = items.get(position).mOperName;
                if (MyStringUtil.eq("全部", type)) {
                    mTvClientLevel.setText("客户类型");
                    mClientLevelStr = "";
                } else {
                    mTvClientLevel.setText(type);
                    mClientLevelStr = type;
                }
                queryData();
            }
        });
    }

    /**
     * 搜索字体颜色
     */
    private void doSearchTextColor(String input) {
        if (TextUtils.isEmpty(input)) {
            mTvSearch.setTextColor(getResources().getColor(R.color.gray_6));
        } else {
            mTvSearch.setTextColor(getResources().getColor(R.color.yellow));
        }
    }

    /**
     * 显示隐藏搜索
     */
    private void doShowHideSearch() {
        if (mRlSearch.getVisibility() == View.VISIBLE) {
            mRlSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
        } else {
            mRlSearch.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 搜索请求
     */
    private void doSearch() {
//        String search = mEtSearch.getText().toString();
//        if (TextUtils.isEmpty(search)) {
//            ToastUtils.showCustomToast("请输入要搜索的关键字");
//            return;
//        }
        queryData();//刷新加载数据(复用：下拉刷新，筛选时间，客户等级，搜索)
    }


}
