package com.qwb.view.table.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.qwb.view.table.adapter.Table3LeftAdapter;
import com.qwb.view.table.adapter.Table3RightAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.MyTimeUtils;
import com.qwb.view.common.adapter.AbsCommonAdapter;
import com.qwb.view.table.parsent.PTable3;
import com.qwb.view.table.model.TableModel;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.widget.SyncHorizontalScrollView;
import com.qwb.view.table.model.FooterBean;
import com.qwb.view.table.model.Statement_cpddBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 3-产品订单统计表
 */

public class TableActivity3 extends XActivity<PTable3> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_table3;
    }

    @Override
    public PTable3 newP() {
        return new PTable3();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        doIntent();
        queryData();
    }

    /**
     * 刷新加载数据(复用：)
     */
    private String sDateStr, eDateStr, jepxStr, xsTpStr, pszdStr;
    private void queryData() {
        pageNo = 1;
        String khNm = mEtSearch.getText().toString().trim();
        String memberName = mEtSearchMemberName.getText().toString().trim();
        getP().queryData(context, pageNo, pageSize, sDateStr, eDateStr, khNm,memberName, jepxStr, xsTpStr, pszdStr);
    }

    private void doIntent() {
        sDateStr = eDateStr = MyTimeUtils.getTodayStr();//今天
        sDateStr = MyTimeUtils.getTodayStr();//今天
        mTvDate.setText(sDateStr + "至" + eDateStr);//默认今天-今天
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
        mTvHeadTitle.setText("产品订单统计表");
    }

    /**
     * 初始化筛选:排序，人员，搜索
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
    @BindView(R.id.tv_xstp)
    TextView mTvXstp;//销售类型
    @BindView(R.id.tv_pszd)
    TextView mTvPszd;//配送指定
    @BindView(R.id.tv_money_sort)
    TextView mTvMoneySort;//金额排序
    @BindView(R.id.tv_num_sort)
    TextView mTvNumSort;//数量排序
    @BindView(R.id.tv_zsl)
    TextView tv_zsl;//总数量
    @BindView(R.id.tv_zje)
    TextView tv_zje;//总金额

    private void initScreening() {
//        mEtSearch.setHint("业务员或客户名称");
        mTvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseTime();
            }
        });
        mTvXstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doXStp();
            }
        });
        mTvPszd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPszd();
            }
        });
        mTvMoneySort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doMoneySort();
            }
        });
        mTvNumSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNumSort();
            }
        });
        mIvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }

    /**
     * 表格
     */
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

        mLeftAdapter = new Table3LeftAdapter(context);
        mRightAdapter = new Table3RightAdapter(context);
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
                getP().queryData(context, pageNo, pageSize, sDateStr, eDateStr, khNm,memberName, jepxStr, xsTpStr, pszdStr);
            }
        });
    }

    //TODO*************************************************************************************************************************************
    //TODO*************************************************************************************************************************************
    public void showData(List<Statement_cpddBean.Cpdd> list, List<FooterBean> footer) {
        try {
            showBottomUI(footer);

            if (list == null) {
                list = new ArrayList<>();
            }
            List<TableModel> modelList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Statement_cpddBean.Cpdd data = list.get(i);
                TableModel tableMode = new TableModel();
                tableMode.setLeftTitle(data.getWareNm());//商品名称
                tableMode.setText0(data.getXsTp());//销售类型
                tableMode.setText1(String.valueOf(data.getNums()));//订单数量
                tableMode.setText2(String.valueOf(data.getWareDj()));//单价
                tableMode.setText3(String.valueOf(data.getZjs()));//订单金额
                tableMode.setText4(String.valueOf(data.getWareDw()));//单位
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

    /**
     * 底部：数量；总金额
     */
    public void showBottomUI(List<FooterBean> footer) {
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
     * 销售类型
     */
    private List<String> mXsTpList = new ArrayList<>();

    private void doXStp() {
        if (MyCollectionUtil.isEmpty(mXsTpList)) {
            getP().queryDataXstp(context);
        } else {
            showDialogXsTp(mXsTpList);
        }
    }

    /**
     * 销售类型
     */
    public void showDialogXsTp(List<String> datas) {
        if (MyCollectionUtil.isNotEmpty(datas) && MyCollectionUtil.isEmpty(mXsTpList)) {
            this.mXsTpList.addAll(datas);
        }

        final ArrayList<DialogMenuItem> items = new ArrayList<>();
        if (MyCollectionUtil.isNotEmpty(datas)) {
            for (String s : datas) {
                items.add(new DialogMenuItem(s, 0));
            }
        }
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.title("选择销售类型").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = mXsTpList.get(position);
                if (MyStringUtil.eq("全部", type)) {
                    mTvXstp.setText("销售类型");
                    xsTpStr = "";
                } else {
                    mTvXstp.setText(type);
                    xsTpStr = type;
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
     * 搜索请求
     */
    private void doSearch() {
        queryData();//刷新加载数据(复用：下拉刷新，筛选时间，客户等级，搜索)
    }

    /**
     * 金额排序
     */
    private void doMoneySort() {
        if (MyStringUtil.isEmpty(jepxStr)) {
            jepxStr = "1";
            mTvMoneySort.setText("金额降序");
        } else {
            if ("1".equals(jepxStr)) {
                jepxStr = "2";
                mTvMoneySort.setText("金额升序");
            } else {
                jepxStr = "1";
                mTvMoneySort.setText("金额降序");
            }
        }
        mTvNumSort.setText("数量排序");
        queryData();
    }

    /**
     * 数量排序
     */
    private void doNumSort() {
        if (MyStringUtil.isEmpty(jepxStr)) {
            jepxStr = "3";
            mTvNumSort.setText("数量降序");
        } else {
            if ("3".equals(jepxStr)) {
                jepxStr = "4";
                mTvNumSort.setText("数量升序");
            } else {
                jepxStr = "3";
                mTvNumSort.setText("数量降序");
            }
        }
        mTvMoneySort.setText("金额排序");
        queryData();
    }


}
