package com.qwb.view.audit.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.audit.adapter.AuditModelAdapter;
import com.qwb.view.audit.model.AuditModelBean;
import com.qwb.view.audit.parsent.PAuditTabSendAudit;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * tab:发起审批
 */
public class AuditTabSendAuditFragment extends XFragment<PAuditTabSendAudit> {

	public int mPosition;
	private ArrayList<AuditModelBean> mNormalModelList = new ArrayList<>();// 默认审批模板列表
	private ArrayList<AuditModelBean> mZdyModelList = new ArrayList<>();// 自定义审批模板列表
	private ArrayList<AuditModelBean> mSumModelList = new ArrayList<>();// 总的审批模板列表

	public AuditTabSendAuditFragment() {
	}

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_audit_tab_send_audit;
	}

	@Override
	public PAuditTabSendAudit newP() {
		return new PAuditTabSendAudit();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initNormalData();
		initUI();
		getP().queryAuditModelList(context);
	}

	/**
	 * 初始化默认几个模板(请假，报销，出差，物品领用，通用审批，添加) id是排序用的
 	 */
	private void initNormalData() {
		mNormalModelList.add(new AuditModelBean(-5, R.mipmap.home_tab_qj, "请假"));
		mNormalModelList.add(new AuditModelBean(-3, R.mipmap.home_tab_cc, "出差"));
//		//鹭百川公司不要"报销"，"物品领用"，"通用审批"
//		if(!"139".equals(SPUtils.getCompanyId())){
//			mNormalModelList.add(new AuditModelBean(-4, R.mipmap.home_tab_bx, "报销"));
//			mNormalModelList.add(new AuditModelBean(-2, R.mipmap.home_tab_wply, "物品领用"));
//			mNormalModelList.add(new AuditModelBean(-1, R.mipmap.home_tab_tysp, "通用审批"));
//		}
		mSumModelList.addAll(mNormalModelList);
	}


	private void initUI() {
		initAdapter();
	}

	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;
	AuditModelAdapter mAdapter;
	private void initAdapter() {
		mRecyclerView.setHasFixedSize(true);
		mRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
		mAdapter = new AuditModelAdapter(context);
		mAdapter.setNewData(mSumModelList);
		mRecyclerView.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				doItemClick(adapter, position);
			}
		});
		mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				doItemChildClick(adapter, view, position);
			}
		});
	}


	//删除对话框
	public void showDelDialog(final int id) {
		NormalDialog norMalDialog = new NormalDialog(context);
		norMalDialog.content("确定要删除吗").show();
		norMalDialog.setOnBtnClickL(null, new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				getP().deleteData(context,id);
			}
		});
	}

	//添加自定义模板数据
	public void doZdyModelData(List<AuditModelBean> list) {
		mZdyModelList.clear();
		for (AuditModelBean model : list) {
			int id = model.getId();
			int offset = id%10;
			model.setImgRes(R.mipmap.ic_audit_model_0);
			if(offset == 0){
				model.setImgRes(R.mipmap.ic_audit_model_0);
			}else if(offset == 1){
				model.setImgRes(R.mipmap.ic_audit_model_1);
			}else if(offset == 2){
				model.setImgRes(R.mipmap.ic_audit_model_2);
			}else if(offset == 3){
				model.setImgRes(R.mipmap.ic_audit_model_3);
			}else if(offset == 4){
				model.setImgRes(R.mipmap.ic_audit_model_4);
			}else if(offset == 5){
				model.setImgRes(R.mipmap.ic_audit_model_5);
			}else if(offset == 6){
				model.setImgRes(R.mipmap.ic_audit_model_6);
			}else if(offset == 7){
				model.setImgRes(R.mipmap.ic_audit_model_7);
			}else if(offset == 8){
				model.setImgRes(R.mipmap.ic_audit_model_8);
			}else if(offset == 9){
				model.setImgRes(R.mipmap.ic_audit_model_9);
			}
			mZdyModelList.add(model);
		}
		mSumModelList.clear();
		mSumModelList.addAll(mNormalModelList);
		mSumModelList.addAll(mZdyModelList);
		mAdapter.setNewData(mSumModelList);
	}

	//删除适配器数据
	public void doDelData(){
		mAdapter.remove(mPosition);
	}

	//适配器item
	private void doItemClick(BaseQuickAdapter adapter, int position) {
		try {
			AuditModelBean item = (AuditModelBean) adapter.getData().get(position);
			switch (item.getId()) {
				case -5:// 请假
					ActivityManager.getInstance().jumpActivity(context, AuditLeaveActivity.class);
					break;
				case -3:// 出差
					ActivityManager.getInstance().jumpActivity(context, AuditBusinessTravelActivity.class);
					break;
				default:// 默认：自定义模板
					Router.newIntent(context)
							.to(AuditModelActivity.class)
							.putSerializable(ConstantUtils.Intent.SHENPI_MODEL, item)
							.launch();
					break;
			}
		} catch (Exception e) {
			ToastUtils.showError(e);
		}
	}

	//适配器子view
	private void doItemChildClick(BaseQuickAdapter adapter, View view, int position) {
		try {
			AuditModelBean item = (AuditModelBean) adapter.getItem(position);
			mPosition = position;
			switch (view.getId()){
				case R.id.item_layout_del:
					showDelDialog(item.getId());
					break;
			}
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}




}
