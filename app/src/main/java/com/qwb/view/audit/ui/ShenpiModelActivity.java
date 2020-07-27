package com.qwb.view.audit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qwb.view.audit.parsent.PShenPiModel;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.audit.adapter.ShenPi_PersonAdatper;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.audit.model.ShenpiModel;
import com.chiyong.t3.R;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 审批模块：自定义审批模板
 */
public class ShenpiModelActivity extends XActivity<PShenPiModel> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_shenpi_model;
	}

	@Override
	public PShenPiModel newP() {
		return new PShenPiModel();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
		doIntent();
	}

	private int type;// 1:添加 2：修改
	private void initIntent(){
		Intent intent = getIntent();
		if (intent != null) {
			type = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
			if (type == 2) {
				mShenpiModel = (ShenpiModel) intent.getSerializableExtra("shenpiModel");
			}
		}
	}

	private void doIntent(){
		// 2:管理员
		String isUnitmng = SPUtils.getSValues(ConstantUtils.Sp.IS_UNITMNG);
		if ("1".equals(isUnitmng) || "2".equals(isUnitmng)) {
			rg_privateOrPublic.setVisibility(View.VISIBLE);
		} else {
			rg_privateOrPublic.setVisibility(View.GONE);
		}

		switch (type) {
			case 1:
				tv_head_title.setText("添加审批模板");
				tv_headRight.setText("添加");
				break;
			case 2:
				tv_head_title.setText("修改审批模板");
				tv_headRight.setText("修改");
				break;
		}


		// 修改时：要显示相对应的UI数据
		if (type == 2) {
			edit_name.setText(mShenpiModel.getZdyNm());
			// 1:私用 2：公用
			String isSy = mShenpiModel.getIsSy();
			if ("1".equals(isSy)) {
				rd_private.setChecked(true);
			} else if ("2".equals(isSy)) {
				rd_public.setChecked(true);
			}
			String tp = mShenpiModel.getTp();
			if (tp.indexOf("1") != -1) {
				cb_type.setChecked(true);
			} else {
				cb_type.setChecked(false);
			}
			if (tp.indexOf("2") != -1) {
				cb_time.setChecked(true);
			} else {
				cb_time.setChecked(false);
			}
			if (tp.indexOf("3") != -1) {
				cb_detail.setChecked(true);
			} else {
				cb_detail.setChecked(false);
			}
			if (tp.indexOf("4") != -1) {
				cb_remo.setChecked(true);
			} else {
				cb_remo.setChecked(false);
			}
			if (tp.indexOf("5") != -1) {
				cb_amount.setChecked(true);
			} else {
				cb_amount.setChecked(false);
			}
			// 保存到数据库
			List<MemberBean> mlist = mShenpiModel.getMlist();
			AddCurrentIds.getI().saveToDB(AddCurrentIds.TYPE_ZDY, mlist);
		}
		refreshPersonAdapter(false);
	}

	private void initUI() {
		initHead();
		initOther();
		// 初始化审批人
		initShenpiRen();
	}

	// 头部
	TextView tv_headRight;
	TextView tv_head_title;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorBlue(context);
		ImageView img_head_back =  findViewById(R.id.iv_head_back);
		tv_head_title =  findViewById(R.id.tv_head_title);
		tv_headRight =  findViewById(R.id.tv_head_right);
		ImageView img_head_right =  findViewById(R.id.img_head_right);
		img_head_right.setVisibility(View.GONE);
		img_head_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		tv_headRight.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				addData();
			}
		});
	}

	private EditText edit_name;
	private CheckBox cb_type;
	private CheckBox cb_time;
	private CheckBox cb_detail;
	private CheckBox cb_amount;
	private CheckBox cb_remo;
	private RadioButton rd_public;
	private RadioButton rd_private;
	private RadioGroup rg_privateOrPublic;
	private ShenpiModel mShenpiModel;// 对象为空：添加 不为空：修改
	private String isSy = "1";// 1私用;2共用(默认私用)
	private void initOther() {
		edit_name =  findViewById(R.id.edit_name);
		cb_type =  findViewById(R.id.cb_type);
		cb_time =  findViewById(R.id.cb_time);
		cb_detail =  findViewById(R.id.cb_detail);
		cb_remo =  findViewById(R.id.cb_remo);
		cb_amount =  findViewById(R.id.cb_amount);
		rd_private =  findViewById(R.id.rd_private);
		rd_public =  findViewById(R.id.rd_public);
		rg_privateOrPublic = findViewById(R.id.rg_privateOrPublic);
	}

	/**
	 * 初始化审批人
	 */
	private boolean isDelModel_Person;// 添加审批人的删除/正常模式
	private ComputeHeightGridView gv_person;
	private ShenPi_PersonAdatper personAdapter;
	private void initShenpiRen() {
		gv_person =  findViewById(R.id.gv_person);
		gv_person.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				int count = arg0.getAdapter().getCount();
				if (isDelModel_Person) {
					// 删除模式
					if (count >= 3) {
						if (position == count - 2) {
							// 倒数第二个item 添加审批人的item
							toAddRenACT();
						} else if (position == count - 1) {
							// 倒数第一个item 即删除图标的item 只要切换删除模式即可
							isDelModel_Person = false;
							refreshPersonAdapter(isDelModel_Person);
						} else {
							// 移除
							personAdapter.removeItem(position);
						}
					} else {
						toAddRenACT();
					}
				} else {
					// 添加人模式
					if (count >= 3) {
						if (position == count - 1) {
							// 倒数第一个item 切换删除模式而已
							isDelModel_Person = true;
							refreshPersonAdapter(isDelModel_Person);
						} else {
							// 点击其他都是添加人
							toAddRenACT();
						}
					} else {
						toAddRenACT();
					}
				}
			}
		});
	}

	/**
	 * 跳转添加审批人act
	 */
	private void toAddRenACT() {
		isDelModel_Person = false;
		Intent intent = new Intent(context, AddShenPiRenActivity.class);
		intent.putExtra("type", 6);// 1 请假 2报销 3出差 4物品领用 5通用审批 6：自定义审批模板
		intent.putExtra("title", "选择审批人");
		startActivity(intent);
	}

	/**
	 * 审批人-适配器刷新
	 */
	private void refreshPersonAdapter(boolean isDelModel) {
		if (personAdapter == null) {
			personAdapter = new ShenPi_PersonAdatper(this,
					AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_ZDY, true));
			gv_person.setAdapter(personAdapter);
		} else {
			personAdapter.refreshAdapter(isDelModel);
		}
	}

	@Override
	protected void onResume() {
		refreshPersonAdapter(isDelModel_Person = false);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 修改时，退出界面要删除数据库
		AddCurrentIds.getI().deleteToDB(6);
	}

	// 添加或修改审批自定义模板
	private String name;
	private String tp;
	private void addData() {
		name = edit_name.getText().toString().trim();
		if (MyStringUtil.isEmpty(name)) {
			ToastUtils.showCustomToast("请输入自定义名称");
			return;
		}
		//TODO 1:类型；2时间；3：详情；4：备注；5：金额
		StringBuffer sb = new StringBuffer();
		if (cb_type.isChecked()) {
			sb.append("1,");
		}
		if (cb_time.isChecked()) {
			sb.append("2,");
		}
		if (cb_detail.isChecked()) {
			sb.append("3,");
		}
		if (cb_remo.isChecked()) {
			sb.append("4,");
		}
		if (cb_amount.isChecked()) {
			sb.append("5");
		}
		// 判断最后一个字符是否是“,”逗号
		if (sb.length() > 0) {
			char charAt = sb.charAt(sb.length() - 1);
			if (charAt == ',') {
				tp = sb.substring(0, sb.length() - 1);
			} else {
				tp = sb.toString();
			}
		}

		if (AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_ZDY, false).size() <= 0) {
			ToastUtils.showCustomToast("请选择审批人");
			return;
		}
		if (rd_public.isChecked()) {
			isSy = "2";
		}
		if(mShenpiModel!= null){
			getP().addData(context, name, getIds(), tp, isSy, type, mShenpiModel.id);
		}else{
			getP().addData(context, name, getIds(), tp, isSy, type, 0);
		}
	}

	/**
	 * 获取审批人ids
	 */
	private String getIds() {
		StringBuffer sb = new StringBuffer();
		List<MemberBean> choiseIds = AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_ZDY, false);
		if (choiseIds != null) {
			if (choiseIds.size() == 1) {
				sb.append(choiseIds.get(0).getMemberId());
			} else {
				for (int i = 0; i < choiseIds.size(); i++) {
					if (i == choiseIds.size() - 1) {// 最后一个不加“,”
						sb.append(choiseIds.get(i).getMemberId());
					} else {
						sb.append(choiseIds.get(i).getMemberId() + ",");
					}
				}
			}
		}
		return sb.toString();
	}

	//添加成功
	public void addSuccess(){
		Intent intent = new Intent();
		intent.setAction("state_true");
		if(2 == type){
			// 修改成功后要传递ShenpiModel对象，上面的界面也要改变
			ShenpiModel bean = new ShenpiModel();
			bean.setId(mShenpiModel.getId());
			bean.setMemberId(Integer.valueOf(SPUtils.getID()));
			bean.setZdyNm(name);
			bean.setTp(tp);
			bean.setIsSy(isSy);
			bean.setMemIds(getIds());
			List<MemberBean> choiseIds = AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_ZDY, false);
			bean.setMlist(choiseIds);
			intent.putExtra("mShenpiModel_chuan", bean);
			setResult(0, intent);
		}
		sendBroadcast(intent);
		ActivityManager.getInstance().closeActivity(context);
	}
}
