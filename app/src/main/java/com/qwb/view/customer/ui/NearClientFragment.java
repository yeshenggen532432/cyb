package com.qwb.view.customer.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.common.ToStepEnum;
import com.qwb.utils.MyCollectionUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.event.CarOrderChooseClientEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.view.customer.adapter.NearClientAdapter;
import com.qwb.event.MineClientEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.customer.parsent.PNearClient;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.customer.model.NearClientInfo;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：客户管理--周边客户
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class NearClientFragment extends XFragment<PNearClient> {

	private String pxtp = "1";// 排序类型1 距离;2 拜访时间; 3：拜访时间

	public NearClientFragment() {
	}

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_near_client;
	}

	@Override
	public PNearClient newP() {
		return new PNearClient();
	}

	@Override
	public boolean useEventBus() {
		return true;
	}

	/**
	 * 初始化EventBus
	 */
	private void initEvent() {
		//添加修改客户成功：刷新界面
		BusProvider.getBus().toFlowable(MineClientEvent.class)
				.subscribe(new Consumer<MineClientEvent>() {
					@Override
					public void accept(MineClientEvent event) throws Exception {
						if(event.getTag()==ConstantUtils.Event.TAG_MINE_CLIENT){
							if(mLocClient!=null){
								mRefreshLayout.autoRefresh();//触发自动刷新
							}
						}
					}
				});
		//测试
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		initLocation();
		initEvent();
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		initAdapter();
		initRefresh();
		initScreening();
	}

	/**
	 * 初始化适配器（周边客户）
	 */
	@BindView(R.id.rv_near_client)
	RecyclerView mRvNearClient;
	NearClientAdapter mNearAdapter;
	private int pageNo = 1;
	private void initAdapter() {
		mRvNearClient.setHasFixedSize(true);
		mRvNearClient.setLayoutManager(new LinearLayoutManager(context));
		//添加分割线
		mRvNearClient.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_5)
				.build());
		mNearAdapter = new NearClientAdapter();
		mNearAdapter.openLoadAnimation();
		mRvNearClient.setAdapter(mNearAdapter);
		//item点击事件
		mNearAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				onItemClickView(adapter, position);//适配器（）-item点击
			}
		});
		//子item点击事件
		mNearAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				NearClientInfo data=(NearClientInfo)adapter.getData().get(position);
				switch (view.getId()){
					case R.id.tv_callonCount_mine://导航
						ActivityManager.getInstance().jumpActivityNavMap(context, data.getLatitude(), data.getLongitude(), data.getAddress());
						break;
				}
			}
		});
	}

	/**
	 * 初始化刷新控件（周边）
	 */
	@BindView(R.id.refreshLayout_near)
	RefreshLayout mRefreshLayout;
	private void initRefresh(){
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNo=1;
				isFirst=true;
				mLocClient.start();
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNo++;
				getP().loadData(context,pageNo,latitude,longitude,pxtp,mEtSearch,mMemberIds);
			}
		});
	}

	/**
	 * 初始化筛选:排序，人员，搜索
	 */
	@BindView(R.id.rl_search)
	LinearLayout mRlSearch;
	@BindView(R.id.et_search)
	EditText mEtSearch;
	@BindView(R.id.tv_sort)
	TextView mTvSort;//排序
	@BindView(R.id.tv_frame)
	TextView mTvFrame;//选择成员
	@BindView(R.id.tv_search)
	TextView mTvSearch;//搜索
	private void initScreening() {
		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(TextUtils.isEmpty(s)){
					mTvSearch.setTextColor(getResources().getColor(R.color.gray_6));
				}else{
					mTvSearch.setTextColor(getResources().getColor(R.color.yellow));
				}
			}
		});
	}


	/**
	 * 初始化定位
	 */
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private double latitude;
	private double longitude;
	private boolean isFirst = true;
	private void initLocation() {
		mLocClient = new LocationClient(getActivity()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		option.setScanSpan(1000);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				ToastUtils.showShort(getActivity(), "获取位置失败,请重新获取");
				return;
			}
			if (isFirst) {// 只有一次
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				if(MyUtils.isEmptyString(String.valueOf(latitude)) || 4.9E-324==latitude){
					dialogNormalStyle();
					mLocClient.stop();
					return;
				}
				getP().loadData(context,pageNo,latitude,longitude,pxtp,mEtSearch, mMemberIds);
				isFirst = false;
				mLocClient.stop();
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * 刷新适配器-周边客户
	 */
	public void refreshAdapter(List<NearClientInfo> dataList){
		if (MyCollectionUtil.isEmpty(dataList)) {
			mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			ToastUtils.showCustomToast("没有更多数据");
		}
		if(pageNo==1){
			//上拉刷新
			mNearAdapter.setNewData(dataList);
			mRefreshLayout.finishRefresh();
			mRefreshLayout.setNoMoreData(false);
		}else{
			//加载更多
			mNearAdapter.addData(dataList);
			mRefreshLayout.finishLoadMore();
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
	 * 适配器（周边客户）-item点击
	 */
	private void onItemClickView(BaseQuickAdapter adapter, int i) {
		List<NearClientInfo> dataList=adapter.getData();
		NearClientInfo  data=dataList.get(i);
		switch (ClientManagerActivity.type){
			case 1:// 周边客户--可直接拜访修改
				ActivityManager.getInstance().jumpToStepActivity(context, ToStepEnum.STEP_NEAR_CUSTOMER, data.getId(), data.getKhNm(), data.getAddress(), data.getTel(), data.getMobile(),
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
	private void jumpActivitySetp5(NearClientInfo data,int type,Class class1) {
		Router.newIntent(context)
				.putInt(ConstantUtils.Intent.ORDER_TYPE,type)
				.putString(ConstantUtils.Intent.CLIENT_NAME,data.getKhNm())
				.putString(ConstantUtils.Intent.CLIENT_ID,String.valueOf(data.getId()))
				.putString(ConstantUtils.Intent.TEL,data.getMobile())// 电话
				.putString(ConstantUtils.Intent.LINKMAN,data.getLinkman())// 联系人
				.putString(ConstantUtils.Intent.ADDRESS,data.getAddress())// 传客户地址
				.to(class1)
				.launch();
		Router.pop(context);//关闭界面
	}

	/**
	 * dialog-定位失败
	 */
	private void dialogNormalStyle() {
		final NormalDialog dialog = new NormalDialog(getActivity());
		dialog.setCanceledOnTouchOutside(false);//外部点击不消失
		dialog
				.isTitleShow(true)//是否需要标题
				.style(NormalDialog.STYLE_TWO)//标题风格二(标题居中，没有线,内容水平居中)，默认风格一
				.title("温馨提示")
				.titleTextColor(Color.parseColor("#333333"))
				.titleTextSize(20)
				.content("定位失败，请检查是否打开GPS,网络信号等等；也可以按底下的定位按钮，重新定位")
				.contentTextColor(Color.parseColor("#666666"))
				.contentTextSize(12)
				.contentGravity(Gravity.CENTER_VERTICAL)//内容显示位置（风格二时，居中）
				.bgColor(Color.parseColor("#f1f1f2"))//背景颜色
				.cornerRadius(5)//父布局的圆角
//				.dividerColor(Color.parseColor("#ff0000"))//"确定"，“取消”，“中间”三按钮分割线的颜色
				.widthScale(0.8f)//布局宽度占屏幕的比例
				.btnNum(2)//按钮数量（默认两按钮，确定"，“取消”）
				.btnText("取消","确定")
				.btnTextColor(Color.parseColor("#555555"),Color.parseColor("#555555"))
				.btnTextSize(15,15)
				.btnPressColor(Color.parseColor("#f4f4f4"))//按钮按下的背景颜色
				.show();

		//"确定"，“取消”，“中间”三按钮的点击事件
		dialog.setOnBtnClickL(
				new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				},
				new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				});
	}

	/**
	 * 点击事件
	 */
	@OnClick({R.id.tv_sort,R.id.tv_frame,R.id.tv_search,R.id.iv_search})
	public void onClickView(View view){
		switch (view.getId()){
			case R.id.tv_sort://排序
				showDialogSort();
				break;
			case R.id.tv_frame://结构图
				if(null == mTreeDatas || mTreeDatas.isEmpty()){
					getP().loadDataFrame(context);
				}else{
					showDialogMember();
				}
				break;
			case R.id.tv_search://筛选：搜索
				if(mRlSearch.getVisibility()== View.VISIBLE){
					mRlSearch.setVisibility(View.GONE);
					mEtSearch.setText("");
				}else{
					mRlSearch.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.iv_search://搜索
				String searchStr=mEtSearch.getText().toString();
				if(TextUtils.isEmpty(searchStr)){
					ToastUtils.showCustomToast("请输入要搜索的关键字");
					return;
				}
				mRefreshLayout.autoRefresh();
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
				pxtp=String.valueOf(position+1);
				mTvSort.setText(mStringItems[position]);
				mRefreshLayout.autoRefresh();
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLocClient != null) {
			mLocClient.stop();
			mLocClient = null;
		}
	}

	private String mMemberIds;
	private List<TreeBean> mTreeDatas = new ArrayList<>();
	private MyTreeDialog mTreeDialog;
	private void showDialogMember(){
		if(null == mTreeDialog){
			mTreeDialog = new MyTreeDialog(context, mTreeDatas, true);
		}
		mTreeDialog.title("选择员工").show();
		mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
			@Override
			public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
				XLog.e("checkIds",checkIds);
				mMemberIds = checkIds;
				mRefreshLayout.autoRefresh();
				if(!MyStringUtil.isEmpty(checkIds)){
					mTvFrame.setTextColor(getResources().getColor(R.color.yellow));
				}else{
					mTvFrame.setTextColor(getResources().getColor(R.color.gray_6));
				}
			}
		});
	}

	public void refreshAdapterMemberTree(List<TreeBean> mDatas){
		this.mTreeDatas = mDatas;
		showDialogMember();
	}

}
