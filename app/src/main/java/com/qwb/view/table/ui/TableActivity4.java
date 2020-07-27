package com.qwb.view.table.ui;

import android.content.Intent;
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
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.table.adapter.Table4LeftAdapter;
import com.qwb.view.table.adapter.Table4RightAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.table.parsent.PTable4;
import com.qwb.view.table.model.TableModel;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.cnlife.widget.SyncHorizontalScrollView;
import com.qwb.view.table.model.FooterBean;
import com.qwb.view.table.model.Statement_list1Bean;
import com.qwb.view.table.model.Statement_xsddBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 销售订单统计表
 */

public class TableActivity4 extends XActivity<PTable4> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_table4;
    }

    @Override
    public PTable4 newP() {
        return new PTable4();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        doIntent();
        queryData();
    }

    /**
     * 刷新加载数据(复用：下拉刷新，筛选时间，订单状态，配送指定，搜索)
     */
    private void queryData() {
        pageNo = 1;
        String khNm = mEtSearch.getText().toString().trim();
        String memberName = mEtSearchMemberName.getText().toString().trim();
        getP().queryData(context, pageNo,pageSize, sDateStr, eDateStr, khNm,memberName, orderZt, pszdStr, cid);
    }

    private int type = 1;//1:统计报表（默认）；2：拜访步骤
    private String mCustomerName;
    private String cid;
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
            if (type == 2) {
                mCustomerName = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
                cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
                sDateStr = intent.getStringExtra(ConstantUtils.Intent.START_TIME);
                eDateStr = intent.getStringExtra(ConstantUtils.Intent.END_TIME);
            } else {
                sDateStr = eDateStr = MyTimeUtils.getTodayStr();//默认今天
            }
        }
    }

    private void doIntent() {
        mTvDate.setText(sDateStr + "至" + eDateStr);//默认今天
        //从拜访步骤进入
        if (type == 2) {
            mEtSearch.setText(mCustomerName);
            mTvSearch.setTextColor(getResources().getColor(R.color.yellow));
            mRlSearch.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initScreening();
        initTableView();
        initBottom();
    }

    /**
     * 初始化头部
     */
    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);//状态栏颜色，透明度
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mTvHeadTitle.setText("销售订单统计表");
    }

    /**
     * 初始化筛选:筛选时间，配送指定,订单状态，搜索
     */
    @BindView(R.id.rl_search)
    LinearLayout mRlSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.iv_search)
    TextView mIvSearch;
    @BindView(R.id.et_search_member_name)
    EditText mEtSearchMemberName;
    @BindView(R.id.tv_date)
    TextView mTvDate;//筛选
    @BindView(R.id.tv_orderZt)
    TextView mTvOrderZt;//订单状态
    @BindView(R.id.tv_pszd)
    TextView mTvPszd;//配送指定
    @BindView(R.id.tv_search)
    TextView mTvSearch;//搜索
    String sDateStr, eDateStr, orderZt, pszdStr;//日期，
    private void initScreening() {
//        mEtSearch.setHint("业务员或客户名称");
        mEtSearch.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (MyStringUtil.isEmpty(s.toString())) {
                    mTvSearch.setTextColor(getResources().getColor(R.color.gray_6));
                } else {
                    mTvSearch.setTextColor(getResources().getColor(R.color.yellow));
                }
            }
        });
    }

    @BindView(R.id.tv_zsl)
    TextView tv_zsl;//总数量
    @BindView(R.id.tv_zje)
    TextView tv_zje;//总金额
    private void initBottom(){
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

        mLeftAdapter = new Table4LeftAdapter(context);
        mRightAdapter = new Table4RightAdapter(context);
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
                getP().queryData(context, pageNo, pageSize, sDateStr, eDateStr, khNm,memberName, orderZt, pszdStr, cid);
            }
        });
    }

    /**
     * 点击事件
     */

    @OnClick({R.id.tv_date, R.id.tv_orderZt, R.id.tv_pszd, R.id.tv_search, R.id.iv_search})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_date://筛选时间
                showDialogChooseTime();
                break;
            case R.id.tv_orderZt://订单状态
                showDialogOrderType();
                break;
            case R.id.tv_pszd://配送指定
                showDialogPszd();
                break;
            case R.id.tv_search://筛选：搜索
                doShowHideSearch();
                break;
            case R.id.iv_search://搜索
                doSearch();
                break;
        }
    }

    //TODO***********************************************************************************************************************************
    //TODO***********************************************************************************************************************************
    //TODO ********************************接口****************************


    public void showData(List<Statement_xsddBean.Xsdd> list, List<FooterBean> footer) {
        showBottomUI(footer);

        if (list == null) {
            list = new ArrayList<>();
        }

        List<TableModel> modelList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Statement_xsddBean.Xsdd dataList = list.get(i);
            List<Statement_list1Bean> list1 = dataList.getList1();
            if (list1 != null && list1.size() > 0) {
                for (int j = 0; j < list1.size(); j++) {
                    Statement_list1Bean data = list1.get(j);
                    TableModel tableMode = new TableModel();
                    //orderIds以这个字段区分
                    tableMode.setOrgCode(dataList.getOrderIds());
                    tableMode.setLeftTitle(dataList.getKhNm());
                    tableMode.setText0(dataList.getMemberNm());
                    tableMode.setText1(data.getWareNm());
                    tableMode.setText2(data.getXsTp());
                    tableMode.setText3(String.valueOf(data.getWareNum()));
                    tableMode.setText4(String.valueOf(data.getWareDj()));
                    tableMode.setText5(String.valueOf(data.getWareZj()));
                    modelList.add(tableMode);
                }
            }
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
    }

    /**
     * 底部：数量；总金额
     */
    public void showBottomUI(List<FooterBean> footer){
        if (MyCollectionUtil.isNotEmpty(footer)) {
            tv_zsl.setText("" + footer.get(0).getNums());
            tv_zje.setText("" + footer.get(0).getZjs());
        }
    }

    /**
     * 筛选时间
     */
    private void showDialogChooseTime() {
        new MyDoubleDatePickerDialog(context, "筛选时间", sDateStr, eDateStr, new MyDoubleDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                sDateStr = startDate;
                eDateStr = endDate;
                mTvDate.setText(sDateStr + "至" + eDateStr);
                queryData();
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

    /**
     * 订单类型：审核，未审核
     */
    public void showDialogOrderType() {
        final String[] items = {"全部", "审核", "未审核"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择订单状态").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = items[position];
                if (MyStringUtil.eq("全部", type)) {
                    mTvOrderZt.setText("订单状态");
                    orderZt = "";
                } else {
                    mTvOrderZt.setText(type);
                    orderZt = type;
                }
                queryData();
            }
        });
    }

    /**
     * 配送指定
     */
    public void showDialogPszd() {
        final String[] items = {"全部", "公司直送", "转二批配送"};
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择配送指定").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = items[position];
                if (MyStringUtil.eq("全部", type)) {
                    mTvPszd.setText("配送指定");
                    pszdStr = "";
                } else {
                    mTvPszd.setText(type);
                    pszdStr = type;
                }
                queryData();
            }
        });
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
        queryData();
    }


}
