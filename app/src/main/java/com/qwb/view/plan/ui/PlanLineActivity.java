package com.qwb.view.plan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.event.BaseEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.adapter.PlanLineAdapter;
import com.qwb.view.customer.model.MineClientInfo;
import com.qwb.view.plan.model.PlanLineBean;
import com.qwb.view.plan.model.PlanLineDetailBean;
import com.qwb.view.plan.parsent.PPlanLine;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xrecyclerview.divider.HorizontalDividerItemDecoration;
import io.reactivex.functions.Consumer;

/**
 * 拜访计划--线路
 *
 */
public class PlanLineActivity extends XActivity<PPlanLine> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_plan_line;
	}

	@Override
	public PPlanLine newP() {
		return new PPlanLine();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
		getP().queryData(context, pageNo, pageSize);
	}

	@Override
	public boolean useEventBus() {
		return true;
	}

	@Override
	public void bindEvent() {
		BusProvider.getBus().toFlowable(BaseEvent.class)
				.subscribe(new Consumer<BaseEvent>() {
					@Override
					public void accept(BaseEvent event) throws Exception {
						if(event.getTag() == ConstantUtils.Event.TAG_BASE){
							getP().queryData(context, pageNo, pageSize);
						}
					}
				});
	}

	private String mDate;
	private void initIntent(){
		Intent intent = getIntent();
		if(intent!=null){
			mDate = intent.getStringExtra(ConstantUtils.Intent.PDATE);
		}
	}

	private void initUI() {
		initHead();
		initAdapter();
		initBottom();
	}


	//初始化头部
	@BindView(R.id.head_left)
	View mHeadLeft;
	@BindView(R.id.head_right)
	View mHeadRight;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mTvHeadTitle.setText("线路");
		mTvHeadRight.setText("地图");
		mHeadLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mHeadRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				getCustomerMap();
				ActivityManager.getInstance().jumpActivity(context, PlanLineMapActivity.class);
			}
		});
	}

	/**
	 * 初始化适配器
	 */
	RecyclerView mRecyclerView;
	PlanLineAdapter mAdapter;
	private int pageNo = 1;
	private int pageSize = 1000;//暂时这样处理（刷新导致选中）
	private void initAdapter() {
		mRecyclerView = findViewById(R.id.recyclerView);
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		//添加分割线
		mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_0_5)
				.build());
		mAdapter = new PlanLineAdapter();
		mRecyclerView.setAdapter(mAdapter);

		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					List<PlanLineBean> datas = adapter.getData();
					PlanLineBean bean = datas.get(position);
					switch (view.getId()){
						//加入计划
						case R.id.tv_plan_add:
							getP().addData(context, "" + bean.getId(), mDate);
							break;
						//修改线路
						case R.id.tv_plan_update:
							Router.newIntent(context)
									.putInt(ConstantUtils.Intent.TYPE, ConstantUtils.Menu.INT_UPDATE)
									.putInt(ConstantUtils.Intent.ID, bean.getId())
									.putString(ConstantUtils.Intent.NAME, bean.getXlNm())
									.to(PlanEditLineActivity.class)
									.launch();
							break;
						//
						case R.id.cb:
							bean.setCheck(!bean.getCheck());
							datas.set(position, bean);
							adapter.notifyDataSetChanged();
							setAllSelect(datas);
							break;
						//显示客户列表
						case R.id.tv_count:
							showDialogCustomer(bean);
							break;
					}
				}catch (Exception e){
					ToastUtils.showError(e);
				}
			}
		});
	}


	/**
	 * 显示客户
	 */
	private void showDialogCustomer(PlanLineBean bean){
		List<PlanLineDetailBean> subList = bean.getChildren();
		if (MyCollectionUtil.isNotEmpty(subList)) {
			ArrayList<DialogMenuItem> items = new ArrayList<>();
			for (PlanLineDetailBean sub : subList
				 ) {
				items.add(new DialogMenuItem(sub.getKhNm(), 0));
			}
			NormalListDialog dialog = new NormalListDialog(context, items);
			dialog.show();
		}


	}

	public void refreshAdapter(List<PlanLineBean> dataList) {
		if(pageNo == 1){
			//上拉刷新
			mAdapter.setNewData(dataList);
		}else{
			//加载更多
			mAdapter.addData(dataList);
		}

		if(dataList.size() < 10){
			ToastUtils.showCustomToast("没有更多数据");
		}
	}

	/**
	 * 底部：全选，删除，添加
	 */
	@BindView(R.id.layout_delete)
	View mLayoutDelete;
	@BindView(R.id.layout_add)
	View mLayoutAdd;
	@BindView(R.id.layout_all_select)
	View mLayoutAllSelect;
	@BindView(R.id.cb_all_select)
	CheckBox mCbAllSelect;
	private void initBottom() {
		//全选
		mCbAllSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				for (PlanLineBean line :mAdapter.getData()) {
					line.setCheck(mCbAllSelect.isChecked());
				}
				mAdapter.notifyDataSetChanged();
			}
		});
		//删除
		mLayoutDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogDel();
			}
		});
		//添加线路
		mLayoutAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Router.newIntent(context)
						.putInt(ConstantUtils.Intent.TYPE, ConstantUtils.Menu.INT_ADD)
						.to(PlanEditLineActivity.class)
						.launch();
			}
		});
	}

	private List<PlanLineBean> removeList = new ArrayList<>();
	private void delData() {
		removeList.clear();
		//判断全选
		boolean flag = false;
		String ids = null;
		for (PlanLineBean bean : mAdapter.getData()) {
            if(bean.getCheck()){
                flag = true;
                if(MyStringUtil.isEmpty(ids)){
                    ids = "" + bean.getId();
                }else{
                    ids += "," + bean.getId();
                }
				removeList.add(bean);
            }
        }
		if(!flag){
            ToastUtils.showCustomToast("请选择要删除的线路");
            return;
        }
		getP().delData(context, ids);
	}

	private void setAllSelect(List<PlanLineBean> datas) {
		if(datas != null){
			//判断全选
			boolean flag = true;
			for (PlanLineBean line :datas) {
				if(!line.getCheck()){
					flag = false;
					break;
				}
			}
			if(flag){
				mCbAllSelect.setChecked(true);
			}else{
				mCbAllSelect.setChecked(false);
			}
		}
	}

	private int xlId;
	private void getCustomerMap(){
		ConstantUtils.selectCustomerLists.clear();
		for (PlanLineBean bean : mAdapter.getData()) {
			if(bean.getCheck()){
				List<PlanLineDetailBean> subList = bean.getChildren();
				List<MineClientInfo> customerList = new ArrayList<>();
				if (MyCollectionUtil.isNotEmpty(subList)) {
					for(PlanLineDetailBean sub : subList){
						MineClientInfo customer = new MineClientInfo();
						customer.setKhNm(sub.getKhNm());
						customer.setLatitude(sub.getLatitude());
						customer.setLongitude(sub.getLongitude());
						customerList.add(customer);
					}
					ConstantUtils.selectCustomerLists.add(customerList);
				}

				if (xlId == 0){
					xlId = bean.getId();
				}
			}
		}
	}



	/**
	 * 对话框：删除线路
	 */
	private void showDialogDel(){
		NormalDialog dialog = new NormalDialog(context);
		dialog.content("您确定要删除吗？")
				.show();
		dialog.setOnBtnClickL(null, new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				delData();
			}
		});
	}

	/**
	 * 删除成功
	 */
	public void delSuccess(){
		mAdapter.getData().removeAll(removeList);
		mAdapter.notifyDataSetChanged();
	}

}
