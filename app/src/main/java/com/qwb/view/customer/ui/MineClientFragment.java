package com.qwb.view.customer.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.ToStepEnum;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyDividerUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.view.customer.adapter.MineClientAdapter;
import com.qwb.event.CarOrderChooseClientEvent;
import com.qwb.event.MineClientEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.customer.parsent.PMineClient;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.db.DMineCustomerBean;
import com.qwb.view.flow.ui.FlowCameraVoiceActivity;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.qwb.view.tree.SimpleTreeAdapter_kh;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.MineClientInfo;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zyyoona7.lib.EasyPopup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 客户管理--我的客户
 */
public class MineClientFragment extends XFragment<PMineClient> {
    public MineClientFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.x_fragment_mine_client;
    }

    @Override
    public PMineClient newP() {
        return new PMineClient();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void bindEvent() {
        //添加修改客户成功：刷新界面
        BusProvider.getBus().toFlowable(MineClientEvent.class)
                .subscribe(new Consumer<MineClientEvent>() {
                    @Override
                    public void accept(MineClientEvent event) throws Exception {
                        if (event.getTag() == ConstantUtils.Event.TAG_MINE_CLIENT) {
                            mRefreshLayout.autoRefresh();//触发自动刷新
                        }
                    }
                });
    }

    private String orderTp = "1";// 排序类型1 距离;2 拜访时间; 3：拜访时间

    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        initLocation();
        createPopupClient();
    }

    /**
     * 初始化UI
     */
    @BindView(R.id.parent)
    View parent;

    private void initUI() {
        initAdapter();
        initRefresh();
        initScreening();
    }

    /**
     * 初始化筛选:排序，人员，搜索
     */
    LinearLayout mRlSearch;
    EditText mEtSearch;
    TextView mTvSort;
    TextView mTvFrame;
    TextView mTvSearch;
    TextView mTvFrameClient;
    @BindView(R.id.tv_region)
    TextView mTvRegion;

    private void initScreening() {
        mTvSort = context.findViewById(R.id.tv_sort);//排序
        mTvFrame = context.findViewById(R.id.tv_frame);//选择成员
        mTvSearch = context.findViewById(R.id.tv_search);//搜索
        mTvFrameClient = context.findViewById(R.id.tv_frame_client);//客户等级，客户类型
        mRlSearch = context.findViewById(R.id.rl_search);
        mEtSearch = context.findViewById(R.id.et_search);
        mEtSearch.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                if (MyStringUtil.isEmpty(input.toString().trim())) {
                    mTvSearch.setTextColor(getResources().getColor(R.color.gray_6));
                } else {
                    mTvSearch.setTextColor(getResources().getColor(R.color.yellow));
                }
            }
        });
        //客户所属区域
        mTvRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mTreeRegion || mTreeRegion.isEmpty()) {
                    getP().queryRegionTree(context);
                } else {
                    showDialogRegion(mTreeRegion);
                }
            }
        });
    }

    /**
     * 初始化适配器（我的客户）
     */
    RecyclerView mRvMineClient;
    MineClientAdapter mMineAdapter;
    private int pageNo = 1;
    private int pageSize = 10;
    private void initAdapter() {
        mRvMineClient = context.findViewById(R.id.rv_mine_client);
        mRvMineClient.setHasFixedSize(true);
        mRvMineClient.setLayoutManager(new LinearLayoutManager(context));
        //添加分割线
//        mRvMineClient.addItemDecoration(MyDividerUtil.getH5CGray(context));
        mMineAdapter = new MineClientAdapter();
        mMineAdapter.openLoadAnimation();
        mRvMineClient.setAdapter(mMineAdapter);
        //item点击事件
        mMineAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClickView(adapter, position);//适配器（退货）-item点击
            }
        });
        //子item点击事件
        mMineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MineClientInfo data = (MineClientInfo) adapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.view_nav://导航
                        ActivityManager.getInstance().jumpActivityNavMap(context, data.getLatitude(), data.getLongitude(), data.getAddress());
                        break;
//                    case R.id.tv_zr://编辑客户（转让客户，删除客户）
//                        mCurrentData = data;
//                        mCurrentPosition = position;
//                        showDialogEditClient();
//                        break;
                }
            }
        });
    }

    /**
     * 初始化刷新控件（我的客户）
     */
    RefreshLayout mRefreshLayout;

    private void initRefresh() {
        mRefreshLayout = context.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                initLocation();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                queryData();
            }
        });
    }

    /**
     * 初始化定位
     */
    private double latitude;
    private double longitude;

    private void initLocation() {
        MyMapUtil.getInstance()
                .getLocationClient(context)
                .setOnLocationListener(new MyMapUtil.OnLocationListener() {
                    @Override
                    public void setOnSuccessListener(BDLocation bdLocation) {
                        latitude = bdLocation.getLatitude();
                        longitude = bdLocation.getLongitude();
                        if (MyUtils.isEmptyString(String.valueOf(latitude)) || 4.9E-324 == latitude) {
                            ToastUtils.showCustomToast("定位失败，请重新定位");
                            return;
                        }
                        queryCacheData();//查询缓存数据
                        queryData();
                    }

                    @Override
                    public void setErrorListener() {
                    }

                    @Override
                    public void setAddressListener(String addressStr) {
                    }
                });
    }

    //查询缓存数据
    private void queryCacheData() {
        String search = mEtSearch.getText().toString().trim();
        List<DMineCustomerBean> customerBeans = MyDataUtils.getInstance().queryMineClient2(pageNo, pageSize, search, latitude, longitude);
        List<MineClientInfo> dataList = new ArrayList<>();
        if (null != customerBeans) {
            for (DMineCustomerBean bean : customerBeans) {
                MineClientInfo client = new MineClientInfo();
                client.setId(Integer.valueOf(bean.getCid()));
                client.setKhNm(bean.getKhNm());
                client.setMemId(Integer.valueOf(bean.getMemId()));
                client.setMemberNm(bean.getMemberNm());
                client.setBranchName(bean.getBranchName());
                client.setQdtpNm(bean.getQdtpNm());
                client.setXsjdNm(bean.getXsjdNm());
                client.setAddress(bean.getAddress());
                client.setXxzt(bean.getXxzt());
                client.setLinkman(bean.getLinkman());
                client.setLongitude(bean.getLongitude());
                client.setLatitude(bean.getLatitude());
                client.setScbfDate(bean.getScbfDate());
                client.setTel(bean.getTel());
                client.setMobile(bean.getMobile());
                client.setJlkm("" + (bean.getDistance() / 1000));
                if (!MyStringUtil.isEmpty(bean.getKhTp())) {
                    client.setKhTp(Integer.valueOf(bean.getKhTp()));
                }
                dataList.add(client);
            }
        }
        refreshAdapter(dataList, dataList.size(), false);
    }

    /**
     * 刷新适配器-我的客户
     */
    public void refreshAdapter(List<MineClientInfo> dataList, int total, boolean isShowMore) {
        if (MyCollectionUtil.isEmpty(dataList) && isShowMore) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if (pageNo == 1) {
            //上拉刷新
            mMineAdapter.setNewData(dataList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
        } else {
            //加载更多
            mMineAdapter.addData(dataList);
            mRefreshLayout.finishLoadMore();
        }
        //显示“我的客户”数量
        if (context instanceof ClientManagerActivity) {
            this.total = total;
            ClientManagerActivity clientManagerActivity = (ClientManagerActivity) context;
            clientManagerActivity.mRbMine.setText("我的客户(" + total + ")");
        }
    }

    /**
     * 刷新适配器-删除客户
     */
    MineClientInfo mCurrentData;//当前选中的对象
    int mCurrentPosition;//当前位置
    int total;//客户数量

    public void refreshAdapterDelSuccess() {
        mMineAdapter.remove(mCurrentPosition);
        //显示“我的客户”数量
        if (context instanceof ClientManagerActivity) {
            ClientManagerActivity clientManagerActivity = (ClientManagerActivity) context;
            clientManagerActivity.mRbMine.setText("我的客户(" + (total - 1) + ")");
        }
    }

    /**
     * 关闭刷新
     */
    public void closeRefresh() {
        //关闭刷新，加载更多
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    /**
     * 刷新适配器-结构图：客户类型，等级
     */
    List<TreeBean> mClientDatas = new ArrayList<>();
    SimpleTreeAdapter_kh<TreeBean> mClientAdapter;
    HashMap<Integer, String> mClientMap = new HashMap<Integer, String>();

    public void refreshAdapterFrameClient(List<TreeBean> mDatas, HashMap<Integer, String> map, boolean isShowPop) {
        this.mClientDatas = mDatas;
        this.mClientMap = map;
        if ((mDatas != null && mDatas.size() > 0)) {
            if (mClientAdapter == null) {
                try {
                    mClientAdapter = new SimpleTreeAdapter_kh<TreeBean>(mClientTree, context, mClientDatas, 0);
                    mClientTree.setAdapter(mClientAdapter);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                mClientAdapter.notifyDataSetChanged();
            }
            //是否弹窗
            if (isShowPop) {
                mEasyPopClient.showAtLocation(parent, Gravity.CENTER, 0, 0);
            }
        }
    }

    /**
     * 适配器（我的客户）-item点击
     */
    private void onItemClickView(BaseQuickAdapter baseQuickAdapter, int i) {
        List<MineClientInfo> dataList = baseQuickAdapter.getData();
        MineClientInfo data = dataList.get(i);
        switch (ClientManagerActivity.type) {
            case 1:
                ActivityManager.getInstance().jumpToStepActivity(context, ToStepEnum.STEP_MINE_CUSTOMER, data.getId(), data.getKhNm(), data.getAddress(), data.getTel(), data.getMobile(),
                        data.getLinkman(), data.getLongitude(), data.getLatitude(), data.getLocationTag(), null);
                break;
            case 2:// 我的客户--订货
//                jumpActivitySetp5(data, ClientManagerActivity.type, Step5Activity.class);
//                break;
            case 4:// 我的客户--退货
            case 7:// 车销下单
            case 11:// 红字单
                CarOrderChooseClientEvent event = new CarOrderChooseClientEvent();
                event.setClientNm(data.getKhNm());
                event.setCid(data.getId().toString());
                event.setLinkman(data.getLinkman());
                event.setAddress(data.getAddress());
                event.setTel(data.getTel());
                event.setPhone(data.getMobile());
                BusProvider.getBus().post(event);
                ActivityManager.getInstance().closeActivity(context);
                break;
        }
    }

    /**
     * 跳转到拜访步骤5：订货下单
     */
    private void jumpActivitySetp5(MineClientInfo data, int type, Class class1) {
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.ORDER_TYPE, type)
                .putString(ConstantUtils.Intent.CLIENT_NAME, data.getKhNm())
                .putString(ConstantUtils.Intent.CLIENT_ID, String.valueOf(data.getId()))
                .putString(ConstantUtils.Intent.TEL, data.getMobile())// 电话
                .putString(ConstantUtils.Intent.LINKMAN, data.getLinkman())// 联系人
                .putString(ConstantUtils.Intent.ADDRESS, data.getAddress())// 传客户地址
                .to(class1)
                .launch();
        ActivityManager.getInstance().closeActivity(context);//关闭界面
    }

    /**
     * 结构图：客户等级，客户类型
     */
    private ListView mClientTree;
    private EasyPopup mEasyPopClient;

    public void createPopupClient() {
        mEasyPopClient = new EasyPopup(context)
                .setContentView(R.layout.x_popup_frame)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                //变暗的背景颜色
                .setDimColor(Color.BLACK)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .createPopup();
        mClientTree = mEasyPopClient.getView(R.id.id_tree);
        TextView tvTitle = mEasyPopClient.getView(R.id.tv_title);
        tvTitle.setText("客户类型和客户等级");
        // 重置
        mEasyPopClient.getView(R.id.btn_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constans.ziTrueMap.clear();
                Constans.ParentTrueMap2.clear();
                refreshAdapterFrameClient(mClientDatas, mClientMap, false);
            }
        });
        //ok
        mEasyPopClient.getView(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEasyPopClient.dismiss();
                onConfirmFrameClient();
            }
        });
    }

    /**
     * 结构图：“确定按钮”
     */
    //组织架构（临时ids）
    public HashMap<Integer, Boolean> ziTrueMap = new HashMap<Integer, Boolean>();//保存商品是否选中
    public HashMap<Integer, Integer> ParentTrueMap2 = new HashMap<Integer, Integer>();//部门：0没选中，1全选中，2：部分选中
    private String mMemberIds;


    /**
     * 结构图：“确定按钮”
     */
    //我的客户--组织架构（临时ids）
    public HashMap<Integer, Boolean> ziTrueMapClient = new HashMap<>();//保存商品是否选中
    public HashMap<Integer, Integer> ParentTrueMap2Client = new HashMap<>();//部门：0没选中，1全选中，2：部分选中
    private String clientLevelStr;
    private String clientTypeStr;
    private List<String> mClientTypeList = new ArrayList<>();
    private List<String> mClientLevelList = new ArrayList<>();

    private void onConfirmFrameClient() {
        mClientTypeList.clear();// 客户类型
        mClientLevelList.clear();// 客户等级
        Iterator<Integer> iter = Constans.ziTrueMap.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();
            Boolean val = Constans.ziTrueMap.get(key);
            if (val) {
                String vuals = mClientMap.get(key);
                if (key > ConstantUtils.TREE_ID) {
                    mClientLevelList.add(vuals);
                } else {
                    mClientTypeList.add(vuals);
                }
            }
        }
        String levelStr = JSON.toJSONString(mClientLevelList);
        String typeStr = JSON.toJSONString(mClientTypeList);
        clientLevelStr = levelStr.substring(1, levelStr.length() - 1);//去掉[]
        clientTypeStr = typeStr.substring(1, typeStr.length() - 1);//去掉[]
        mRefreshLayout.autoRefresh();
        //公用的ids赋值给临时的ids
        ziTrueMapClient.clear();
        ParentTrueMap2Client.clear();
        ziTrueMapClient.putAll(Constans.ziTrueMap);
        ParentTrueMap2Client.putAll(Constans.ParentTrueMap2);
        if (ziTrueMapClient != null && ziTrueMapClient.size() > 0) {
            mTvFrameClient.setTextColor(getResources().getColor(R.color.yellow));
        } else {
            mTvFrameClient.setTextColor(getResources().getColor(R.color.gray_6));
        }
    }

    /**
     * 点击事件
     */
    @OnClick({R.id.tv_sort, R.id.tv_frame, R.id.tv_frame_client, R.id.tv_search, R.id.iv_search, R.id.btn_cache_mine_client})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.tv_sort://排序
                showDialogSort();
                break;
            case R.id.tv_frame://结构图
                if (null == mTreeDatas || mTreeDatas.isEmpty()) {
                    getP().loadDataFrame(context);
                } else {
                    showDialogMember(mTreeDatas);
                }
                break;
            case R.id.tv_frame_client://结构图：客户类型，客户等级
                if (mClientDatas != null && mClientDatas.size() > 0) {
                    //公用的ids先清空；再重新赋值
                    Constans.ziTrueMap.clear();
                    Constans.ParentTrueMap2.clear();
                    Constans.ziTrueMap.putAll(ziTrueMapClient);
                    Constans.ParentTrueMap2.putAll(ParentTrueMap2Client);
                    refreshAdapterFrameClient(mClientDatas, mClientMap, false);
                    mEasyPopClient.showAtLocation(parent, Gravity.CENTER, 0, 0);
                } else {
                    //公用的ids先清空；再重新赋值
                    Constans.ziTrueMap.clear();
                    Constans.ParentTrueMap2.clear();
                    getP().loadDataFrameClient(context);
                }
                break;
            case R.id.tv_search://筛选：搜索
                if (mRlSearch.getVisibility() == View.VISIBLE) {
                    mRlSearch.setVisibility(View.GONE);
                    mEtSearch.setText("");
                } else {
                    mRlSearch.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_search://搜索
                String searchStr = mEtSearch.getText().toString();
                if (TextUtils.isEmpty(searchStr)) {
                    ToastUtils.showCustomToast("请输入要搜索的关键字");
                    return;
                }
                mRefreshLayout.autoRefresh();
//				//缓存状态：true:缓存（使用缓存搜索）；false:正常搜索
//				if(!isCacheState){
//					mRefreshLayout.autoRefresh();
//				}else{
//					//根据搜索模糊查询我的客户
//					List<MineClientInfo> mineClientInfoList=MyDataUtils.getInstance().queryMineClientBySearch(searchStr,pageNo,20);
//					if(mineClientInfoList!=null){
//						XLog.e("size",""+mineClientInfoList.size());
//						refreshAdapter(mineClientInfoList,mineClientInfoList.size());
//					}
//				}
                break;
            case R.id.btn_cache_mine_client://缓存我的客户-改为-（流动打卡）
//				showDialogCacheClient();
                Router.newIntent(context)
                        .to(FlowCameraVoiceActivity.class)
                        .launch();
                break;
        }
    }

    /**
     * 排序对话框
     */
    private String[] mStringItems = {"距离排序", "拜访时间降序", "拜访时间升序"};

    private void showDialogSort() {
        final NormalListDialog dialog = new NormalListDialog(context, mStringItems);
        dialog.title("请选择排序方式").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderTp = String.valueOf(position + 1);
                mTvSort.setText(mStringItems[position]);
                mRefreshLayout.autoRefresh();
            }
        });
    }

    /**
     * 排序对话框
     */
    private String[] mEditClientItems = {"转让客户", "删除客户"};

    private void showDialogEditClient() {
        final NormalListDialog dialog = new NormalListDialog(context, mEditClientItems);
        dialog.title("编辑客户").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://转让客户
                        Router.newIntent(context)
                                .putString(ConstantUtils.Intent.CLIENT_ID, String.valueOf(mCurrentData.getId()))
                                .putBoolean(ConstantUtils.Intent.IS_SIGNLE, true)
                                .to(ZuzhiActivity.class)
                                .launch();
                        break;
                    case 1://删除客户
                        getP().delDataClient(context, String.valueOf(mCurrentData.getId()));
                        break;
                }
            }
        });
    }

    /**
     * 选择员工
     */
    private List<TreeBean> mTreeDatas = new ArrayList<>();
    private MyTreeDialog mTreeDialog;
    public void showDialogMember(List<TreeBean> mDatas) {
        mTreeDatas.clear();
        mTreeDatas.addAll(mDatas);
        if (null == mTreeDialog) {
            mTreeDialog = new MyTreeDialog(context, mTreeDatas, true);
        }
        mTreeDialog.title("选择员工").show();
        mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
            @Override
            public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
                mMemberIds = checkIds;
                mRefreshLayout.autoRefresh();
                if (!MyStringUtil.isEmpty(checkIds)) {
                    mTvFrame.setTextColor(getResources().getColor(R.color.yellow));
                } else {
                    mTvFrame.setTextColor(getResources().getColor(R.color.gray_6));
                }
            }
        });
    }

    /**
     * 客户所属区域
     */
    private List<TreeBean> mTreeRegion = new ArrayList<>();
    private MyTreeDialog mTreeRegionDialog;
    private String mRegionIds;

    public void showDialogRegion(List<TreeBean> mDatas) {
        mTreeRegion.clear();
        mTreeRegion.addAll(mDatas);
        if (null == mTreeRegionDialog) {
            mTreeRegionDialog = new MyTreeDialog(context, mTreeRegion, true);
        }
        mTreeRegionDialog.title("客户所属区域").show();
        mTreeRegionDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
            @Override
            public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
                mRegionIds = clientTypeIds;
                if (!MyStringUtil.isEmpty(mRegionIds)) {
                    mTvRegion.setTextColor(getResources().getColor(R.color.yellow));
                } else {
                    mTvRegion.setTextColor(getResources().getColor(R.color.gray_6));
                }
                mRefreshLayout.autoRefresh();
            }
        });
    }

    /**
     * 请求客户数据
     */
    public void queryData() {
        getP().queryData(context, pageNo, latitude, longitude, orderTp, mEtSearch, mMemberIds, clientTypeStr, clientLevelStr, mRegionIds);
    }


}
