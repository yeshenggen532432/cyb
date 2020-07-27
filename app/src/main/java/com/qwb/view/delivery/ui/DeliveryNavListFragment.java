package com.qwb.view.delivery.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.event.ChooseCustomerEvent;
import com.qwb.event.ChooseDeliveryEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.delivery.adapter.DeliveryNavListAdapter;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.db.DDeliveryCustomerBean;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.view.delivery.parsent.PDeliveryNavList;
import com.qwb.view.customer.ui.ChooseCustomerActivity;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：物流配送：配送完成（pc端是：已生成发货单）
 */
public class DeliveryNavListFragment extends XFragment<PDeliveryNavList> {
    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_delivery_nav_list;
    }

    @Override
    public PDeliveryNavList newP() {
        return new PDeliveryNavList();
    }

    public DeliveryNavListFragment() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            queryData();
        }
    }

    private String psStates = "1,2,3,4";
    private void queryData(){
        getP().queryData(context, psStates);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        super.bindEvent();
        BusProvider.getBus().toFlowable(ChooseDeliveryEvent.class)
                .subscribe(new Consumer<ChooseDeliveryEvent>() {
                    @Override
                    public void accept(ChooseDeliveryEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_CHOOSE_DELIVERY) {
                            doChooseDelivery();
                        }
                    }
                });
        BusProvider.getBus().toFlowable(ChooseCustomerEvent.class)
                .subscribe(new Consumer<ChooseCustomerEvent>() {
                    @Override
                    public void accept(ChooseCustomerEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_CHOOSE_CUSTOMER) {
                            doChooseCustomer();
                        }
                    }
                });

    }

    /**
     * 处理：选择客户
     */
    private void doChooseCustomer() {
        mTip = "";
        List<DDeliveryCustomerBean> list = new ArrayList<>();
        List<DDeliveryCustomerBean> dataList = mAdapter.getData();
        for (MineClientInfo bean : ConstantUtils.selectCustomerList) {
            boolean flag = true;
            for (DDeliveryCustomerBean item : dataList) {
                if (item.getCid().equals("" + bean.getId())) {
                    flag = false;
                    mTip += bean.getKhNm() + "\n";
                    break;
                }
            }
            if (flag) {
                DDeliveryCustomerBean item = new DDeliveryCustomerBean();
                item.setUserId(SPUtils.getID());
                item.setCompanyId(SPUtils.getCompanyId());
                item.setCid("" + bean.getId());
                item.setKhNm(bean.getKhNm());
                item.setAddress(bean.getAddress());
                item.setLatitude(bean.getLatitude());
                item.setLongitude(bean.getLongitude());
                item.setPsState("");
                list.add(item);

                mCbAll.setChecked(false);
                cbStatus = false;
            }
        }

        if (!MyStringUtil.isEmpty(mTip)) {
            mTip += "以上客户已添加";
            showDialogTip();
        }
        MyDataUtils.getInstance().saveDeliveryCustomer(list);
        mAdapter.addData(list);
        mAdapter.notifyDataSetChanged();
        setMapList();
    }

    /**
     * 处理：选择配送单
     */
    private void doChooseDelivery() {
        mTip = "";
        List<DDeliveryCustomerBean> list = new ArrayList<>();
        List<DDeliveryCustomerBean> dataList = mAdapter.getData();
        //选择配送单
        for (DeliveryBean bean : ConstantUtils.selectDeliveryList) {
            boolean flag = true;
            for (DDeliveryCustomerBean item : dataList) {
                if (item.getCid().equals("" + bean.getCstId())) {
                    flag = false;
                    mTip += bean.getKhNm() + "(" + bean.getBillNo() + ")\n";
                    break;
                }
            }
            if (flag) {
                DDeliveryCustomerBean item = new DDeliveryCustomerBean();
                item.setUserId(SPUtils.getID());
                item.setCompanyId(SPUtils.getCompanyId());
                item.setCid("" + bean.getCstId());
                item.setKhNm(bean.getKhNm());
                item.setAddress(bean.getAddress());
                item.setLatitude(bean.getLatitude());
                item.setLongitude(bean.getLongitude());
                item.setBillNo(bean.getBillNo());
                item.setPsState("" + bean.getPsState());
                list.add(item);
                mCbAll.setChecked(false);
                cbStatus = false;
            }
        }

        if (!MyStringUtil.isEmpty(mTip)) {
            mTip += "以上单号的对应客户已添加";
            showDialogTip();
        }
        MyDataUtils.getInstance().saveDeliveryCustomer(list);
        mAdapter.addData(list);
        mAdapter.notifyDataSetChanged();
        setMapList();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        doIntent();
        queryData();
    }

    private void doIntent() {
//        List<DDeliveryCustomerBean> cacheList = MyDataUtils.getInstance().queryDeliveryCustomer();
//        if (cacheList != null && cacheList.size() > 0) {
//            mAdapter.setNewData(cacheList);
//            mAdapter.notifyDataSetChanged();
//        }
    }

    private void initUI() {
        initScreening();
        initAdapter();
        initOther();
    }

    /**
     * 初始化筛选:时间和搜索
     */
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    private void initScreening() {
        mTvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogStatus();
            }
        });
    }

    /**
     * 初始化适配器
     */
    RecyclerView mRecyclerView;
    DeliveryNavListAdapter mAdapter;
    private List<DDeliveryCustomerBean> selectList = new ArrayList<>();
    private void initAdapter() {
        mRecyclerView = context.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mAdapter = new DeliveryNavListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        //item点击事件
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    if (mAdapter.isDel()) {
                        DDeliveryCustomerBean bean = (DDeliveryCustomerBean) adapter.getData().get(position);
                        boolean flag = false;
                        for (DDeliveryCustomerBean item : selectList) {
                            if (String.valueOf(item.getCid()).equals(String.valueOf(bean.getCid()))) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            selectList.add(bean);
                            if (selectList.size() == adapter.getData().size()) {
                                mCbAll.setChecked(true);
                                cbStatus = true;
                            } else {
                                mCbAll.setChecked(false);
                                cbStatus = false;
                            }
                        } else {
                            selectList.remove(bean);
                            mCbAll.setChecked(false);
                            cbStatus = false;
                        }
                        mAdapter.setSelectList(selectList);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                DDeliveryCustomerBean bean = (DDeliveryCustomerBean) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.right:
//                        bean.setNav(true);
//                        MyDataUtils.getInstance().updateDeliveryCustomer(bean);
                        ActivityManager.getInstance().jumpActivityNavMap(context, bean.getLatitude(), bean.getLongitude(), bean.getAddress());
                        break;
                    case R.id.tv_nav:
//                        bean.setNav(true);
//                        MyDataUtils.getInstance().updateDeliveryCustomer(bean);
                        ActivityManager.getInstance().jumpActivityNavMap(context, bean.getLatitude(), bean.getLongitude(), bean.getAddress());
                        break;
                }
            }
        });

    }

    @BindView(R.id.layout_all_select)
    View mViewSelect;//全部按钮
    @BindView(R.id.cb_all_select)
    CheckBox mCbAll;//全部按钮
    private boolean cbStatus;//全中状态

    private void initOther() {
        //选择配货单
        context.findViewById(R.id.layout_choose_delivery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtils.selectDeliveryList.clear();
                Router.newIntent(context)
                        .requestCode(ConstantUtils.Intent.REQUEST_CODE_CHOOSE_DELIVERY)
                        .to(DeliveryListActivity.class)
                        .launch();
            }
        });
        //选择客户
        context.findViewById(R.id.layout_choose_customer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConstantUtils.selectCustomerList.clear();
                Router.newIntent(context)
                        .requestCode(ConstantUtils.Intent.REQUEST_CODE_CHOOSE_CUSTOMER)
                        .to(ChooseCustomerActivity.class)
                        .launch();
            }
        });
        //mCbAll
        mCbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cbStatus = !cbStatus;
                selectList.clear();
                if (cbStatus) {
                    selectList.addAll(mAdapter.getData());
                }
                mAdapter.setSelectList(selectList);
                mAdapter.notifyDataSetChanged();
            }
        });
        //删除
        context.findViewById(R.id.layout_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isDel = mAdapter.isDel();
                if (isDel) {
                    MyDataUtils.getInstance().delDeliveryCustomer(selectList);
                    List<DDeliveryCustomerBean> dataList = mAdapter.getData();
                    dataList.removeAll(selectList);
                    cbStatus = false;
                    mCbAll.setChecked(false);
                    selectList.clear();
                    mAdapter.setSelectList(selectList);
                    mAdapter.notifyDataSetChanged();
                    setMapList();
                } else {
                    ToastUtils.showCustomToast("请选择要删除的");
                    mViewSelect.setVisibility(View.VISIBLE);
                    mAdapter.setDel(true);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 添加重复提示语
     */
    private String mTip;
    private void showDialogTip() {
        try {
            if (!MyStringUtil.isEmpty(mTip) && context != null) {
                NormalDialog dialog = new NormalDialog(context);
                dialog.content(mTip).show();
            }
        }catch (Exception e){}
    }

    private List<DDeliveryCustomerBean> mList = new ArrayList<>();
    public void refreshAdapter(List<DDeliveryCustomerBean> list){
        if(list != null){
            mList.clear();
            mList.addAll(list);
            mAdapter.setNewData(list);
            mAdapter.notifyDataSetChanged();
            setMapList();
        }
    }

    private String[] items = {"全部", "待接收", "已接收", "配送中", "已收货", "已完成", "客户"};
    private void showDialogStatus(){
        NormalListDialog dialog = new NormalListDialog(context, items);
        dialog.show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTvStatus.setText(items[position]);
                if(0 == position){
                    queryData();
                }else{
                    doChangeStatus(position);
                }
            }
        });
    }

    /**
     * 切换状态数据
     */
    private void doChangeStatus(int type){
        List<DDeliveryCustomerBean> currentList = new ArrayList<>();
        if(mList != null && mList.size() > 0){
            for (DDeliveryCustomerBean bean:mList) {
                if(1 == type || 2 == type || 3 == type || 4 == type){
                    //待接收;已接收;配送中;已收货
                    if(String.valueOf(type).equals(bean.getPsState())){
                        currentList.add(bean);
                    }
                }else if(5 == type){
                    //已完成
                    if(String.valueOf("6").equals(bean.getPsState())){
                        currentList.add(bean);
                    }
                }else {
                    //客户
                    if(MyStringUtil.isEmpty(bean.getPsState())){
                        currentList.add(bean);
                    }
                }
            }
        }
        mAdapter.setNewData(currentList);
        mAdapter.notifyDataSetChanged();
        setMapList();
    }

    /**
     * 设置“转地图”数据
     */
    private void setMapList(){
        ConstantUtils.selectDeliveryCustomerList.clear();
        ConstantUtils.selectDeliveryCustomerList.addAll(mAdapter.getData());
    }



}