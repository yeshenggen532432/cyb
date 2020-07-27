package com.qwb.view.audit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.member.model.MemberListBean;
import com.qwb.widget.MyDatePickerDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyOnImageBack;
import com.qwb.view.audit.adapter.AuditPersonCommonAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.view.audit.parsent.PAuditBusinessTravel;
import com.qwb.view.base.model.TreeBean;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.qwb.utils.OtherUtils;
import com.qwb.view.audit.adapter.FuJianAdapter;
import com.qwb.view.audit.adapter.ShenPi_PicAdatper;
import com.qwb.utils.Constans;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.ToastUtils;
import com.qwb.view.file.model.FileBean;
import com.chiyong.t3.R;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 审批:出差
 */
public class AuditBusinessTravelActivity extends XActivity<PAuditBusinessTravel> {

	private final int TYPE = AddCurrentIds.TYPE_CC;
	@Override
	public int getLayoutId() {
		return R.layout.x_activity_audit_business_travel;
	}

	@Override
	public PAuditBusinessTravel newP() {
		return new PAuditBusinessTravel();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
	}

	private void initUI() {
		initHead();
		initOther();
		initAdapterPic();
		initAdapterPerson();
		initAdapterFujian();
	}

	/**
	 * 头部
	 */
	@BindView(R.id.head_left)
	View mHeadLeft;
	@BindView(R.id.head_right)
	View mHeadRight;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead(){
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mTvHeadTitle.setText("我的出差");
		mTvHeadRight.setText("提交");
		mHeadLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				addData();
			}
		});
	}

	@BindView(R.id.edit_title)
	EditText mEtTitle;
	@BindView(R.id.et_place)
	EditText mEtPlace;
	@BindView(R.id.et_num)
	EditText mEtNum;
	@BindView(R.id.et_reason)
	EditText mEtReason;
	@BindView(R.id.tv_start_time)
	TextView mTvStartTime;
	@BindView(R.id.tv_end_time)
	TextView mTvEndTime;
	private void initOther(){
		findViewById(R.id.rl_start_time).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogDate(1);
			}
		});
		findViewById(R.id.rl_end_time).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogDate(2);
			}
		});
	}

	//todo-------------------------------------图片，附件，审批人：start-------------------------------------------------
	/**
	 * 图片
	 */
	private boolean isDelModel;
	private ComputeHeightGridView mGridViewPic;
	private ShenPi_PicAdatper mAdapterPic;
	MyOnImageBack onImageBack;
	private void initAdapterPic() {
		if (onImageBack == null) {
			onImageBack = new MyOnImageBack(context);
		}

		mGridViewPic = findViewById(R.id.gv_pic);
		refreshAdapterPic(false);
		mGridViewPic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				int size = Constans.publish_pics.size();
				if (size > 0) {
					//加
					if (position == size) {
						if (size >= Constans.maxImgCount) {
							ToastUtils.showCustomToast("最多只能添加6张图片");
							return;
						} else {
							onImageBack.setPicNum(Constans.publish_pics.size());
							MyPopWindowManager.getI().show(context, onImageBack, "", "");
						}
						//减
					} else if (position == size + 1) {
						isDelModel = !isDelModel;
						refreshAdapterPic(isDelModel);
					} else {
						//图片
						if (isDelModel) {
							Constans.publish_pics.remove(position);
							refreshAdapterPic(isDelModel);
						} else {
							String[] urls = new String[size];
							for (int i = 0; i < size; i++) {
								urls[i] = "file://" + Constans.publish_pics.get(i);
							}
							ActivityManager.getInstance().zoomPic(context, urls, position);
						}
					}
				} else {
					//加
					onImageBack.setPicNum(Constans.publish_pics.size());
					MyPopWindowManager.getI().show(context, onImageBack, "", "");
				}
			}
		});
	}

	/**
	 * 刷新图片
	 */
	private void refreshAdapterPic(boolean isDelModel) {
		if (mAdapterPic == null) {
			mAdapterPic = new ShenPi_PicAdatper(this);
			mGridViewPic.setAdapter(mAdapterPic);
		} else {
			mAdapterPic.refreshAdapter(isDelModel);
		}
	}



	/**
	 * 附件
	 */
	ComputeHeightListView mListViewFujian;
	private FuJianAdapter fuJianAdapter;// 附件适配器
	private ArrayList<FileBean> fileList = new ArrayList<>();
	private void initAdapterFujian() {
		findViewById(R.id.img_fujian).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().jumpActivityFile(context);
			}
		});
		mListViewFujian = findViewById(R.id.listview_fujian);
		fuJianAdapter = new FuJianAdapter(context, fileList, true);
		mListViewFujian.setAdapter(fuJianAdapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		}
		if (Constans.TAKE_PIC_YUNPAN == requestCode) {
			// 附件
			ArrayList<FileBean> fileBeans = data.getParcelableArrayListExtra(ConstantUtils.Intent.FILE_BEAN);
			fileList.addAll(fileBeans);
			fuJianAdapter.notifyDataSetChanged();
		} else if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
			//图片选择器
			ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
			if (images != null) {
				for (int i = 0; i < images.size(); i++) {
					Constans.publish_pics.add(images.get(i).path);
				}
				refreshAdapterPic(isDelModel);
			}
		}
	}
	//todo-------------------------------------图片，附件，审批人：end-------------------------------------------------


	private void addData() {
		final String title = mEtTitle.getText().toString();
		if (MyStringUtil.isEmpty(title)) {
			ToastUtils.showCustomToast("请填写出差标题");
			return;
		}
		final String type = mEtPlace.getText().toString();
		if (MyStringUtil.isEmpty(type)) {
			ToastUtils.showCustomToast("请填写出差地点");
			return;
		}
		final String start_time = mTvStartTime.getText().toString();
		if (MyStringUtil.isEmpty(start_time)) {
			ToastUtils.showCustomToast("请选择出发时间");
			return;
		}
		final String end_time = mTvEndTime.getText().toString();
		if (MyStringUtil.isEmpty(end_time)) {
			ToastUtils.showCustomToast("请选择返回时间");
			return;
		}
		final String auditData = mEtNum.getText().toString().trim();
		if (TextUtils.isEmpty(auditData)) {
			ToastUtils.showCustomToast("请填写出差天数");
			return;
		}
		final String dsc = mEtReason.getText().toString().trim();
		if (MyStringUtil.isEmpty(dsc)) {
			ToastUtils.showCustomToast("请填写出差事由");
			return;
		}
//		if (AddCurrentIds.getI().getChoiseIds(TYPE, false).size() <= 0) {
//			ToastUtils.showCustomToast("请选择审批人");
//			return;
//		}

//		String checkNm = AddCurrentIds.getI().getChoiseIds(TYPE, false).get(0).getMemberNm();
		getP().addData(context, title, type, dsc, auditData, MyStringUtil.getMemberIds(mPersonList), start_time, end_time, fileList);

	}

	// 显示开始日期
	private void showDialogDate(final int type) {
		String title;
		String date;
		if (1 == type) {
			title = "开始时间";
			date = mTvStartTime.getText().toString().trim();
		} else {
			title = "结束时间";
			date = mTvEndTime.getText().toString().trim();
		}
		new MyDatePickerDialog(context, title, date, new MyDatePickerDialog.DateTimeListener() {
			@Override
			public void onSetTime(int year, int month, int day, String timeStr) {
				try {
					if (1 == type) {
						mTvStartTime.setText(timeStr);
					} else {
						mTvEndTime.setText(timeStr);
					}
				} catch (Exception e) {
					ToastUtils.showError(e);
				}
			}

			@Override
			public void onCancel() {
			}
		}).show();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		OtherUtils.resetStepStatus(context);//重置默认状态,删除图片
	}


	/**
	 * 审批人
	 */
	AuditPersonCommonAdapter mAdapterPerson;
	boolean mIsDelPerson;
	@BindView(R.id.gv_person)
    ComputeHeightGridView mGridViewAudit;
	private List<MemberBean> mPersonList = new ArrayList<>();
	public void initAdapterPerson() {
		mAdapterPerson = new AuditPersonCommonAdapter(context, mPersonList, true);
		mGridViewAudit.setAdapter(mAdapterPerson);
		mGridViewAudit.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int count = parent.getAdapter().getCount();
				if (count >= 3) {
					if (position == count - 2) {
						// 倒数第二个item:添加
						doAddMember();
					} else if (position == count - 1) {
						// 倒数第一个item：删除
						mIsDelPerson = !mIsDelPerson;
						mAdapterPerson.refreshAdapter(mIsDelPerson);
					} else {
						if (mIsDelPerson) {
							mAdapterPerson.removeItem(position);
						}
					}
				} else {
					doAddMember();
				}
			}
		});
	}


	public void doAddMember() {
		if (null == mTreeDataList || mTreeDataList.isEmpty()) {
			getP().queryMemberList(context);
		} else {
			showDialogMember(mTreeDataList, mSumMemberList);
		}
	}

	private List<TreeBean> mTreeDataList = new ArrayList<>();
	private List<MemberListBean.MemberBean> mSumMemberList = new ArrayList<>();//总的员工列表
	public void showDialogMember(List<TreeBean> treeBeans, List<MemberListBean.MemberBean> sumMemberList) {
		try {
			if (MyCollectionUtil.isEmpty(mTreeDataList)) {
				this.mTreeDataList = treeBeans;
				this.mSumMemberList = sumMemberList;
			}
			Map<Integer, Integer> checkMap = new LinkedHashMap<>();
			for (MemberBean bean : mPersonList) {
				checkMap.put(bean.getMemberId(), 1);
			}
			MyTreeDialog treeDialog = new MyTreeDialog(context, mTreeDataList, checkMap, true);
			treeDialog.title("选择部门,员工").show();
			treeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
				@Override
				public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
					mPersonList.clear();
					for (Map.Entry<Integer, Integer> entry : checkMap.entrySet()){
						for (MemberListBean.MemberBean memberBean : mSumMemberList) {
							if( entry.getValue() == 1 && (String.valueOf(entry.getKey()).equals("" + memberBean.getMemberId()))){
								MemberBean bean = new MemberBean();
								bean.setMemberId(memberBean.getMemberId());
								bean.setMemberNm(memberBean.getMemberNm());
								bean.setMemberHead(memberBean.getMemberHead());
								mPersonList.add(bean);
								break;
							}
						}
					}
					mAdapterPerson.refreshAdapter(mIsDelPerson, mPersonList);
				}
			});
		} catch (Exception e) {
			ToastUtils.showError(e);
		}
	}





}
