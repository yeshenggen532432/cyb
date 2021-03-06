package com.qwb.view.tab.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.web.ui.WebX5Activity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.view.shop.adapter.ShopAdapter;
import com.qwb.event.NormalShopEvent;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.company.model.CompanysBean;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.adapter.HotShopAdapter;
import com.qwb.view.tab.model.HotShopBean;
import com.qwb.view.tab.parsent.PXShop;
import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 *  tab:商城
 */
public class XShopFragment extends XFragment<PXShop> {
	int currentPosition;
	private HotShopBean mCurrentBean;
	public XShopFragment() {
	}

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_shop;
	}

	@Override
	public PXShop newP() {
		return new PXShop();
	}

	@Override
	public boolean useEventBus() {
		return true;
	}

	private void initEvent() {
		BusProvider.getBus().toFlowable(NormalShopEvent.class)
				.subscribe(new Consumer<NormalShopEvent>() {
					@Override
					public void accept(NormalShopEvent event) throws Exception {
						getP().queryShopMember(context,true);
					}
				});
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initEvent();
		initUI();
//		getP().queryShopMember(context,true);
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		initTab();
		initSearch();
		initAdapter();
	}

	private String[] mTitles = {"我的关注", "热门商家"};
	@BindView(R.id.tab_head)
	SegmentTabLayout mTabLayout;
	@BindView(R.id.tab_mine_gz)
	View mTabMineGz;
	@BindView(R.id.tab_hot_shop)
	View mTabHotShop;
	private boolean isCheckHotShop;//热门商家是否点击过
	private void initTab() {
		mTabLayout.setTabData(mTitles);
		mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
			@Override
			public void onTabSelect(int position) {
				if(0 == position){
					mTabMineGz.setVisibility(View.VISIBLE);
					mTabHotShop.setVisibility(View.GONE);
				}else if(1 == position){
					mTabMineGz.setVisibility(View.GONE);
					mTabHotShop.setVisibility(View.VISIBLE);
					if(!isCheckHotShop){
						pageNoHotShop = 1;
						getP().queryDataHotShop(context, pageNoHotShop, "", pageSizeHotShop);
						isCheckHotShop = true;
					}
				}
			}
			@Override
			public void onTabReselect(int position) {
			}
		});
	}

	/**
	 * 搜索
	 */
	@BindView(R.id.et_search)
	EditText mEtSearch;
	@BindView(R.id.tv_search)
	TextView mTvSearch;
	private String mSearchStr = "";
	private void initSearch() {
		mTvSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mSearchStr = mEtSearch.getText().toString().trim();
				pageNoHotShop = 1;
				getP().queryDataHotShop(context, pageNoHotShop, mSearchStr, pageSizeHotShop);
			}
		});
	}

	/**
	 * 初始化适配器
	 */
	private int pageNo = 1;
	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;
	private ShopAdapter mAdapter;
	@BindView(R.id.refreshLayout)
	RefreshLayout mRefreshLayout;
	private int pageNoHotShop = 1;
	private int pageSizeHotShop = 10;
	@BindView(R.id.rv_hot_shop)
	RecyclerView mRvHotShop;
	private HotShopAdapter mAdapterHotShop;
	@BindView(R.id.refreshLayout_hot_shop)
	RefreshLayout mRefreshLayoutHotShop;
	private HotShopBean mCurrentHotShop;
	private int mPositionHotShop;
	private void initAdapter() {
		//----------------------我的关注----------------------------------
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_0_5)
				.build());
		mAdapter = new ShopAdapter(context);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					currentPosition=position;
					mCurrentBean =(HotShopBean) adapter.getData().get(position);
					switch (view.getId()){
						case R.id.sb_to_shop:
							getP().toShoppingMall(context,String.valueOf(mCurrentBean.getId()),SPUtils.getSValues(ConstantUtils.Sp.USER_MOBILE),mCurrentBean.getName(),true);
							break;
						case R.id.right_tv_cancel:
							if(isCompanyMember("" +mCurrentBean.getId())){
								ToastUtils.showCustomToast("你属于该公司，不能取消关注");
							}else{
								dialogNormalStyleUnfollow();//取消关注
							}
							break;
						case R.id.right_tv_normal:
							showDialogNormalShop();
							break;
					}
				}catch (Exception e){
				}
			}
		});

		mRefreshLayout.setEnableLoadMore(false);//没有加载更多
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNo=1;
				getP().queryShopMember(context,false);
			}
		});
		mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNo++;
				getP().queryShopMember(context, false);
			}
		});


		//----------------------热门商家----------------------------------
		mRvHotShop.setHasFixedSize(true);
		mRvHotShop.setLayoutManager(new LinearLayoutManager(context));
		mRvHotShop.addItemDecoration(new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_0_5)
				.build());
		mAdapterHotShop = new HotShopAdapter(context);
		mRvHotShop.setAdapter(mAdapterHotShop);

		mRefreshLayoutHotShop.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
				pageNoHotShop = 1;
				String searchStr = mEtSearch.getText().toString().trim();
				getP().queryDataHotShop(context, pageNoHotShop, searchStr, pageSizeHotShop);
			}
		});
		mRefreshLayoutHotShop.setOnLoadMoreListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
				pageNoHotShop ++;
				String searchStr = mEtSearch.getText().toString().trim();
				getP().queryDataHotShop(context, pageNoHotShop, searchStr, pageSizeHotShop);
			}
		});

		mAdapterHotShop.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					mPositionHotShop=position;
					mCurrentHotShop =(HotShopBean) adapter.getData().get(position);
					switch (view.getId()){
						case R.id.sb_to_shop:
							getP().toShoppingMall(context,String.valueOf(mCurrentHotShop.getId()),SPUtils.getMobile(),mCurrentHotShop.getName(),true);
							break;
						case R.id.right_tv_gz:
							getP().AddShopMember(context,"" + mCurrentHotShop.getId() );
							break;
					}
				}catch (Exception e){
				}
			}
		});
	}

    /*=====================================接口相关：开始============================================*/
	/**
	 * 刷新适配器
	 */
	public void refreshAdapter(List<HotShopBean> dataList){
		try {
			if(pageNo==1){
				//上拉刷新
				mAdapter.setNewData(dataList);
				mRefreshLayout.finishRefresh();
				mRefreshLayout.setNoMoreData(false);
			}else{
				//加载更多
				mAdapter.setNewData(dataList);
				mRefreshLayout.setNoMoreData(false);
				mRefreshLayout.finishLoadMore();
			}
			if(dataList.size() < 10){
//				mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
			}
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}

	/**
	 * 刷新适配器
	 */
	public void refreshAdapterHotShop(List<HotShopBean> dataList){
		try {
			if(pageNoHotShop == 1){
				//上拉刷新
				mAdapterHotShop.setNewData(dataList);
				mRefreshLayoutHotShop.setNoMoreData(false);
				mRefreshLayoutHotShop.finishRefresh();
			}else{
				//加载更多
				mAdapterHotShop.addData(dataList);
				mRefreshLayoutHotShop.setNoMoreData(false);
				mRefreshLayoutHotShop.finishLoadMore();
			}
			if(dataList.size() < 10){
				mRefreshLayoutHotShop.finishLoadMoreWithNoMoreData();
			}
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}

	/**
	 * 关闭刷新，加载更多
	 */
	public void closeRefresh(){
//		mRefreshLayout.finishRefresh();
//		mRefreshLayout.finishLoadMore();
		mRefreshLayoutHotShop.finishRefresh();
		mRefreshLayoutHotShop.finishLoadMore();
	}

	/**
	 * 取消关注:成功
	 */
	public void delSuccess(){
		mAdapter.getData().remove(mCurrentBean);
		mAdapter.notifyDataSetChanged();
	}
     /*=====================================接口相关：结束============================================*/

	/*=====================================对话框，窗体相关：开始============================================*/
	//取消关注
	private void dialogNormalStyleUnfollow() {
		final NormalDialog dialog = new NormalDialog(context);
		dialog.setCanceledOnTouchOutside(false);//外部点击不消失
		dialog
				.content("是否取消关注:"+ mCurrentBean.getName()+"?")
				.btnText("暂不取消","取消关注")
				.show();
		dialog.setOnBtnClickL(null,new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						getP().delShopMember(context,Integer.toString(mCurrentBean.getId()),mCurrentBean.getFdId());
					}
				});
	}

	/**
	 * dialog-默认商城
	 */
	private void showDialogNormalShop() {
		final NormalDialog dialog = new NormalDialog(context);
		dialog.setCanceledOnTouchOutside(false);//外部点击不消失
		dialog.content("是否设置该为默认商城？")
				.show();
		dialog.setOnBtnClickL(null, new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						SPUtils.setValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_ID, "" + mCurrentBean.getId());
						SPUtils.setValues(ConstantUtils.Sp.NORMAL_SHOP_COMPANY_NAME, "" + mCurrentBean.getName());
//						getP().toShoppingMall(null, "" + mCurrentBean.getId(),mCurrentBean.getMemberMobile(),mCurrentBean.getName(), false);
						getP().toShoppingMall(null, "" + mCurrentBean.getId(),SPUtils.getMobile(),mCurrentBean.getName(), false);
						mAdapter.notifyDataSetChanged();
					}
				});
	}


    /*=====================================对话框，窗体相关：结束============================================*/
	public void jumpMall(String url, String title){
		Router.newIntent(context)
//				.to(WebActivity.class)
				.to(WebX5Activity.class)
				.putString(ConstantUtils.Intent.WEB_KEY,"15")
				.putString(ConstantUtils.Intent.URL, url)
				.putString(ConstantUtils.Intent.TITLE, title)
				.launch();
	}

	/**
	 * 关注成功
	 */
	public void addSuccess(){
		mAdapterHotShop.remove(mPositionHotShop);
		mAdapterHotShop.notifyDataSetChanged();
	}

	/**
	 * 是否属于该公司
	 */
	private boolean isCompanyMember(String companyId) {
		try {
			String companies = SPUtils.getSValues(ConstantUtils.Sp.COMPANY_S);
			if (MyStringUtil.isNotEmpty(companies)) {
				List<CompanysBean> companyList = JSON.parseArray(companies, CompanysBean.class);
				if (MyCollectionUtil.isNotEmpty(companyList)) {
					for (CompanysBean bean : companyList) {
						if (MyStringUtil.eq(companyId, String.valueOf(bean.getCompanyId()))) {
							return true;
						}
					}
				}
			}
		}catch (Exception e){
		}
		return false;
	}

}
