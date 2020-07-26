package com.qwb.view.plan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.qwb.event.BaseEvent;
import com.qwb.event.PlanLineEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.plan.adapter.PlanEditLineAdapter;
import com.qwb.view.plan.parsent.PPlanEditLine;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.customer.model.MineClientInfo;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xrecyclerview.divider.HorizontalDividerItemDecoration;
import io.reactivex.functions.Consumer;

/**
 * 计划拜访--编辑线路
 */
public class PlanEditLineActivity extends XActivity<PPlanEditLine> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_plan_edit_line;
	}

	@Override
	public PPlanEditLine newP() {
		return new PPlanEditLine();
	}

	@Override
	public boolean useEventBus() {
		return true;
	}

	@Override
	public void bindEvent() {
		BusProvider.getBus().toFlowable(PlanLineEvent.class)
				.subscribe(new Consumer<PlanLineEvent>() {
					@Override
					public void accept(PlanLineEvent event) throws Exception {
						if(event.getTag()==ConstantUtils.Event.TAG_PLAN_LINE){
							mAdapter.setNewData(ConstantUtils.selectCustomerList);
						}
					}
				});

	}

	@Override
	public void initData(Bundle savedInstanceState) {
		ConstantUtils.selectCustomerList.clear();
		initIntent();
		initUI();
		doIntent();
	}

	private int mType = 1; //1:添加；2：修改
	private int mXlId; //线路id
	private String mXlNm;//线路名称
	private void initIntent() {
		Intent intent = getIntent();
		if(intent != null){
			mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, ConstantUtils.Menu.INT_ADD);
			mXlId = intent.getIntExtra(ConstantUtils.Intent.ID, 0);
			mXlNm = intent.getStringExtra(ConstantUtils.Intent.NAME);
		}
	}

	private void doIntent(){
		if(2 == mType){
			getP().queryData(context, pageNo, pageSize, mXlId);
			mEtName.setText(mXlNm);
			mTvHeadTitle.setText("修改线路");
		}
	}

	@BindView(R.id.et_name)
	EditText mEtName;
	private void initUI() {
		initHead();
		initAdapter();
	}

	//初始化头部
	@BindView(R.id.head_left)
	View mHeadLeft;
	@BindView(R.id.head_right)
	View mHeadRight;
	@BindView(R.id.head_right2)
	View mHeadRight2;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	@BindView(R.id.tv_head_right2)
	TextView mTvHeadRight2;

	@BindView(R.id.layout_add_customer)
	View mLayoutAddCustomer;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mHeadLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadTitle.setText("添加线路");
		mTvHeadRight.setText("提交");
		mTvHeadRight2.setText("地图");
		mHeadRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				doAddData();
			}
		});
		mHeadRight2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ConstantUtils.selectCustomerLists.clear();
				ConstantUtils.selectCustomerLists.add(ConstantUtils.selectCustomerList);
				ActivityManager.getInstance().jumpToPlanLineMapActivity(context, 1);
			}
		});
        //选择客户
		mLayoutAddCustomer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().jumpToChooseCustomerActivity(context);
			}
		});
	}


	/**
	 * 初始化适配器
	 */
	RecyclerView mRecyclerView;
	PlanEditLineAdapter mAdapter;
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
		mAdapter = new PlanEditLineAdapter();
		mRecyclerView.setAdapter(mAdapter);

		ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
		itemTouchHelper.attachToRecyclerView(mRecyclerView);
         // 开启拖拽
		mAdapter.enableDragItem(itemTouchHelper);
//		mAdapter.setOnItemDragListener(onItemDragListener);

		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					MineClientInfo bean = (MineClientInfo) adapter.getData().get(position);
					switch (view.getId()){
						case R.id.item_layout_del:
							ConstantUtils.selectCustomerList.remove(bean);
							mAdapter.notifyDataSetChanged();
							break;
					}
				}catch (Exception e){
					ToastUtils.showError(e);
				}
			}
		});
	}


	public void refreshAdapter(ArrayList<MineClientInfo> mineClientList) {
		ConstantUtils.selectCustomerList.addAll(mineClientList);
		mAdapter.setNewData(ConstantUtils.selectCustomerList);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			if(resultCode == ConstantUtils.Intent.REQUEST_CODE_CHOOSE_CUSTOMER){
				mAdapter.setNewData(ConstantUtils.selectCustomerList);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 处理请求：添加线路
	 */
	private void doAddData() {
		String name = mEtName.getText().toString().trim();
		if(MyStringUtil.isEmpty(name)){
			ToastUtils.showCustomToast("请输入线路名称");
			return;
		}
		if(ConstantUtils.selectCustomerList == null || ConstantUtils.selectCustomerList.size() == 0){
			ToastUtils.showCustomToast("请点击右上角选择客户");
			return;
		}
		String cIds = "";
		List<MineClientInfo> list = mAdapter.getData();
		for (MineClientInfo bean : list) {
			if(MyStringUtil.isEmpty(cIds)){
				cIds += "" + bean.getId();
			}else{
				cIds += "," + bean.getId();
			}
		}
		if(2 == mType){
			getP().updateData(context, mXlId, cIds, name);
		}else{
			getP().addData(context, name, cIds);
		}
	}

	/**
	 * 添加线路成功
	 */
	public void addSuccess(){
		BusProvider.getBus().post(new BaseEvent());
		ActivityManager.getInstance().closeActivity(context);
	}



}
