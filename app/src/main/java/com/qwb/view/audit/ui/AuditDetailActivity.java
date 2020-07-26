package com.qwb.view.audit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.widget.MyEditDialog;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyCopyUtil;
import com.qwb.utils.ToastUtils;
import com.qwb.view.audit.adapter.AuditProcessAdapter;
import com.qwb.view.audit.adapter.ShenpiRenAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.member.model.BuMenListBean;
import com.qwb.view.audit.parsent.PAuditDetail;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.view.common.model.PicList;
import com.qwb.view.audit.model.ShenPiDetialBean;
import com.qwb.view.audit.model.ShenPiDetialBean.LiuChengListBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.qwb.view.audit.adapter.FuJianAdapter;
import com.qwb.utils.Constans;
import com.qwb.utils.MyDialogManager;
import com.qwb.utils.MyDialogManager.OnCancle;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.file.ui.FileDetailActivity;
import com.qwb.view.audit.adapter.PicsAdapter;
import com.xmsx.qiweibao.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * 审批详情
 */
public class AuditDetailActivity extends XActivity<PAuditDetail> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_shen_pi_detial;
	}

	@Override
	public PAuditDetail newP() {
		return new PAuditDetail();
	}


	@Override
	public void initData(Bundle savedInstanceState) {
		AuditDetailActivity.I = this;
		initIntent();
		initUI();
		getP().queryData(context, id);
	}

	private static AuditDetailActivity I;
	public static AuditDetailActivity getI() {
		return I;
	}

	private String id;
	private int needCheck;// 1 我的审核 （同意 、拒绝、 转交等操作）2 我发起的 （撤销操作）
	private void initIntent(){
		Intent intent = getIntent();
		id = intent.getStringExtra(ConstantUtils.Intent.ID);
		needCheck = intent.getIntExtra(ConstantUtils.Intent.NEED_CHECK, 0);
	}

	/**
	 * ui
	 */
	private void initUI() {
		initHead();
		initOther();
		initAdapter();
		initAdapterExec();
		initBottom();
	}


	/**
	 * 头部
	 */
	TextView mTvHeadRight;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorBlue(context);
		findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		TextView tvHeadTitle = findViewById(R.id.tv_head_title);
		tvHeadTitle.setText("审批详情");
		mTvHeadRight = findViewById(R.id.tv_head_right);
		mTvHeadRight.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				getP().updateAuditExecStatus(context, id);
			}
		});
	}

	private TextView mTvTitle;
	private TextView mTvWhoSp;
	private TextView mTvBianhao;
	private TextView mTvType;
	private TextView mTvAmount;
	private TextView mTvLiyou;
	private TextView mTvStartTime;
	private TextView mTvEndTime;
	private TextView mTvTianshu;
	private CircleImageView mIvHead;
	private ScrollView sv_detial;

	@BindView(R.id.tv_object)
	TextView mTvObject;
	@BindView(R.id.tv_account)
	TextView mTvAccount;
	private void initOther() {
		mIvHead = findViewById(R.id.iv_head);
		mTvTitle = findViewById(R.id.tv_title);// 标题
		mTvWhoSp = findViewById(R.id.tv_who_shenpi);//谁审批
		mTvBianhao = findViewById(R.id.tv_bianhao);// 编号
		mTvType = findViewById(R.id.tv_type);// 类型
		mTvAmount = findViewById(R.id.tv_amount);// 金额
		mTvLiyou = findViewById(R.id.tv_liyou);// 理由
		mTvStartTime = findViewById(R.id.tv_start_time);// 开始时间
		mTvEndTime = findViewById(R.id.tv_end_time);// 结束时间
		mTvTianshu = findViewById(R.id.tv_tianshu);// 天数
		//复制
		mTvLiyou.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				String copyContent = mTvLiyou.getText().toString().trim();
				MyCopyUtil.getInstance().copyText(context, view, copyContent);
				return false;
			}
		});
		sv_detial =  findViewById(R.id.sv_detial);
		sv_detial.post(new Runnable() {
			@Override
			public void run() {
				sv_detial.fullScroll(ScrollView.FOCUS_UP);
			}
		});
	}

	/**
	 * 图片,流程,附件
	 */
	//图片
	View rlPicContent;
	ComputeHeightGridView mGridViewPic;
	PicsAdapter mPicAdapter;
	List<PicList> picList = new ArrayList<>();
	//流程
	private ComputeHeightListView mListViewLiucheng;
	private List<LiuChengListBean> checkList = new ArrayList<>();
	//附件
	View mViewFujian;
	private ComputeHeightListView mListViewFujian;
	private FuJianAdapter mFujianAdapter;
	private List<FileBean> fileBeanList = new ArrayList<>();
	private void initAdapter() {
		rlPicContent = findViewById(R.id.rl_pic_content);
		mGridViewPic = findViewById(R.id.gv_pics);
		mPicAdapter = new PicsAdapter(context, picList);
		mGridViewPic.setAdapter(mPicAdapter);
		mGridViewPic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				String[] urls = new String[picList.size()];
				for (int i = 0; i < picList.size(); i++) {
					urls[i] = Constans.IMGROOTHOST + picList.get(i).getPic();
				}
				ActivityManager.getInstance().zoomPic(context, urls, position);
			}
		});

		//流程
		mListViewLiucheng = findViewById(R.id.lv_liucheng);
		refreshAdapterProcess();

		//附件
		mViewFujian =  findViewById(R.id.rl_fujian);
		mListViewFujian = findViewById(R.id.listview_fujian);
		refreshAdapterFujian();
		mListViewFujian.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				FileBean fileBean = fileBeanList.get(position);
				if (fileBean == null) {
					return;
				}
				String fileType = fileBean.getTp1();
				// 1：如果是图片类型（点击放大）2：跳到“文件详情”界面
				if ("png".equals(fileType)
						|| "jpg".equals(fileType)
						|| "bmp".equals(fileType)
						|| "jpeg".equals(fileType)
						|| "gif".equals(fileType)) {
					// 点击放大图片
					String[] urls = new String[1];
					urls[0] = Constans.ROOT_imgUrl+ fileBean.getFileNm();
					ActivityManager.getInstance().zoomPic(context, urls, position);
				} else {
					//fileBean:文件名，文件类型，文件大小
					Intent intent = new Intent(AuditDetailActivity.this,FileDetailActivity.class);
					intent.putExtra("fileBean", fileBean);
					startActivity(intent);
				}
			}
		});

	}

	//审批人
	private ComputeHeightGridView mGridViewExec;
	private List<MemberBean> mExecList = new ArrayList<>();
	private ShenpiRenAdapter mAdapterExec;
	private View mViewExec;
	public void initAdapterExec(){
		mViewExec =  findViewById(R.id.rl_exec);
		mGridViewExec = findViewById(R.id.gv_exec);
		mAdapterExec = new ShenpiRenAdapter(context, mExecList);
		mGridViewExec.setAdapter(mAdapterExec);
	}

	/**
	 * 底部
	 */
	View mViewBottom;
	View mViewJujue;
	View mViewZhuangjiao;
	View mViewBack;
	View mViewAgress;
	TextView mTvChexiao;
	View mLine1;
	View mLine2;
	private void initBottom() {
		mViewBottom = findViewById(R.id.ll_bottom);
		mLine1 = findViewById(R.id.tv_line_1);
		mLine2 = findViewById(R.id.tv_line_2);
		mViewJujue = findViewById(R.id.rl_jujue);
		mViewZhuangjiao = findViewById(R.id.rl_zhuangjiao);
		mViewBack = findViewById(R.id.rl_back);
		mViewAgress = findViewById(R.id.rl_agree);
		mTvChexiao = findViewById(R.id.tv_chexiao);
		//拒绝
		mViewJujue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// (如果needCheck 为1是拒绝操作 如果是2 撤销操作)
				if (1 == needCheck) {
					showDialogDes("3", "");
				} else {
					MyDialogManager.getI().showWithClickDialog(context, "您确定撤销申请吗？",
							new OnCancle() {
								@Override
								public void sure() {
									getP().upDateStatusCancel(context, id);
								}

								@Override
								public void cancle() {
								}
							});
				}
			}
		});
		// 转交
		mViewZhuangjiao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				zhuanJiao();
			}
		});
		// 同意
		mViewAgress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogDes("2", "");
			}
		});
		// 退回
		mViewBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogDes("6", "");
			}
		});
	}

	/**
	 * 头部
	 */
	public void doUI(ShenPiDetialBean bean) {
		MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + bean.getMemberHead(), mIvHead);
		mTvTitle.setText(bean.getMemberNm());
		String isOver = bean.getIsOver();// 2 审批流程未走完 1-1 完成 1-2 拒绝 1-3 撤销
		if ("2".equals(isOver)) {
			mTvWhoSp.setText("等待" + bean.getCheckNm() + "审批");
		} else {
			mTvWhoSp.setTextColor(getResources().getColor(R.color.red));
			if ("1-2".equals(isOver)) {
				mTvWhoSp.setText("审批拒绝");
			} else if ("1-3".equals(isOver)) {
				mTvWhoSp.setText("审批撤销");
			} else {
				mTvWhoSp.setText("审批通过");
				//判断是否有“执行完成”按钮
				String execOver = bean.getExecOver();
				mTvHeadRight.setText("");
				if(MyStringUtil.isEmpty(execOver)){
					List<BuMenListBean.MemberBean> execList = bean.getExecList();
					if(execList!=null && execList.size()>0){
						for (BuMenListBean.MemberBean memberBean:execList) {
							if (SPUtils.getID().equals("" + memberBean.getMemberId())){
								mTvHeadRight.setText("执行完成");
								break;
							}
						}
					}
				}
			}
		}
		mTvBianhao.setText(MyUtils.getColorText("#969696","审批编号：" + bean.getAuditNo(), 0, 5));
		// auditTp(必传1 请假 2 报销 3 出差 4 物品领用5 通用审批'6.自定义私用 7.自定义共用
		switch (Integer.valueOf(bean.getAuditTp())) {
		case 1:
			mTvTitle.setText(MyUtils.getColorText("#969696","审批标题：" + bean.getTitle(), 0, 5));
			mTvType.setText(MyUtils.getColorText("#969696","请假类型：" + bean.getTp(), 0, 5));
			mTvStartTime.setText(MyUtils.getColorText("#969696", "开始时间："+ bean.getStime(), 0, 5));
			mTvEndTime.setText(MyUtils.getColorText("#969696","结束时间：" + bean.getEtime(), 0, 5));
			mTvLiyou.setText(MyUtils.getColorText("#969696","请假事由：" + bean.getDsc(), 0, 5));
			mTvTianshu.setText(MyUtils.getColorText("#969696","请假天数：" + bean.getAuditData(), 0, 5));
			break;
		case 2:
			mTvTitle.setText(MyUtils.getColorText("#969696","审批标题：" + bean.getTitle(), 0, 5));
			mTvType.setText(MyUtils.getColorText("#969696","报销类别：" + bean.getTp(), 0, 5));
			mTvEndTime.setVisibility(View.GONE);
			mTvStartTime.setVisibility(View.GONE);
			mTvTianshu.setText(MyUtils.getColorText("#969696", "报销金额（元）："+ bean.getAuditData(), 0, 5));
			mTvLiyou.setText(MyUtils.getColorText("#969696","费用明细：" + bean.getDsc(), 0, 5));
			break;
		case 3:
			mTvTitle.setText(MyUtils.getColorText("#969696","审批标题：" + bean.getTitle(), 0, 5));
			mTvType.setText(MyUtils.getColorText("#969696","出差地点：" + bean.getTp(), 0, 5));
			mTvStartTime.setText(MyUtils.getColorText("#969696", "出发时间："+ bean.getStime(), 0, 5));
			mTvEndTime.setText(MyUtils.getColorText("#969696","返回时间：" + bean.getEtime(), 0, 5));
			mTvTianshu.setText(MyUtils.getColorText("#969696","出差天数：" + bean.getAuditData(), 0, 5));
			mTvLiyou.setText(MyUtils.getColorText("#969696","出差事由：" + bean.getDsc(), 0, 5));
			break;
		case 4:
			mTvTitle.setText(MyUtils.getColorText("#969696","审批标题：" + bean.getTitle(), 0, 5));
			mTvType.setText(MyUtils.getColorText("#969696","物品领用：" + bean.getTp(), 0, 5));
			mTvTianshu.setVisibility(View.GONE);
			mTvEndTime.setVisibility(View.GONE);
			mTvStartTime.setVisibility(View.GONE);
			mTvLiyou.setText(MyUtils.getColorText("#969696","领用详情：" + bean.getDsc(), 0, 5));
			break;
		case 5:
			mTvTitle.setText(MyUtils.getColorText("#969696","审批标题：" + bean.getTp(), 0, 5));
			mTvType.setVisibility(View.GONE);
			mTvTianshu.setVisibility(View.GONE);
			mTvEndTime.setVisibility(View.GONE);
			mTvStartTime.setVisibility(View.GONE);
			mTvLiyou.setText(MyUtils.getColorText("#969696","审批详情：" + bean.getDsc(), 0, 5));
			break;
		case 6://自定义模板:私用
		case 7://自定义模板：公用
			String zdyNm = bean.getZdyNm();
			int num=zdyNm.length()+3;
			mTvTitle.setText(MyUtils.getColorText("#969696", bean.getZdyNm()+"-标题：" + bean.getTitle(), 0, num));
			//类型
			if(!MyUtils.isEmptyString(bean.getDsc())){
				mTvType.setText(MyUtils.getColorText("#969696", "类型：" + bean.getTp(), 0, 3));
			}else{
				mTvType.setVisibility(View.GONE);
			}
			//报销类型(金额)
			if(!MyUtils.isEmptyString(bean.getAmount())){
				mTvAmount.setText(MyUtils.getColorText("#969696", "金额：" + bean.getAmount(), 0, 3));
			}else{
				mTvAmount.setVisibility(View.GONE);
			}
			//开始时间
			if(!MyUtils.isEmptyString(bean.getStime())){
				mTvStartTime.setText(MyUtils.getColorText("#969696", "开始时间："+bean.getStime(), 0, 5));
			}else{
				mTvStartTime.setVisibility(View.GONE);
			}
			//结束时间
			if(!MyUtils.isEmptyString(bean.getEtime())){
				mTvEndTime.setText(MyUtils.getColorText("#969696", "结束时间："+bean.getEtime(), 0, 5));
			}else{
				mTvEndTime.setVisibility(View.GONE);
			}
			//详情
			if(!MyUtils.isEmptyString(bean.getDsc())){
				mTvLiyou.setText(MyUtils.getColorText("#969696", "详情："+bean.getDsc(), 0, 3));
			}else{
				mTvLiyou.setVisibility(View.GONE);
			}
			//备注
			if(!MyUtils.isEmptyString(bean.getAuditData())){
				mTvTianshu.setText(MyUtils.getColorText("#969696", "备注："+bean.getAuditData(), 0, 3));
			}else{
				mTvTianshu.setVisibility(View.GONE);
			}
			//对象
			if(!MyUtils.isEmptyString(bean.getObjectName())){
				mTvObject.setText(MyUtils.getColorText("#969696", "对象："+bean.getObjectName(), 0, 3));
				mTvObject.setVisibility(View.VISIBLE);
			}else{
				mTvObject.setVisibility(View.GONE);
			}
			//账户
			if(!MyUtils.isEmptyString(bean.getAccName())){
				mTvAccount.setText(MyUtils.getColorText("#969696", "账户："+bean.getAccName(), 0, 3));
				mTvAccount.setVisibility(View.VISIBLE);
			}else{
				mTvAccount.setVisibility(View.GONE);
			}
			break;
		}

		//图片
		picList.clear();
		picList.addAll(bean.getPicList());
		if (picList != null && picList.size() > 0) {
			rlPicContent.setVisibility(View.VISIBLE);
			mPicAdapter.notifyDataSetChanged();
		} else {
			rlPicContent.setVisibility(View.GONE);
		}
		//流程
		checkList.clear();
		checkList.addAll(bean.getCheckList());
		if (checkList != null && checkList.size() > 0) {
			refreshAdapterProcess();
		} else {
			mListViewLiucheng.setVisibility(View.GONE);
		}
		//附件
		fileBeanList.clear();
		String fileNms = bean.getFileNms();
		if(!MyUtils.isEmptyString(fileNms)){
			String[] split = fileNms.split(",");
			for (int i = 0; i < split.length; i++) {
				String fileNm = split[i];
				int lastIndexOf = fileNm.lastIndexOf(".") + 1;
				String tp = fileNm.substring(lastIndexOf, fileNm.length());//文件后缀
				FileBean fileBean = new FileBean();
				fileBean.setFileNm(fileNm);
				fileBean.setTp1(tp);
				fileBeanList.add(fileBean);
			}
			refreshAdapterFujian();
		}else{
			mViewFujian.setVisibility(View.GONE);
		}
		//底部：同意，拒绝，转交，撤销
		showBottomButton(bean);

		//执行人
		mExecList.clear();
		if(null != bean.getExecList()){
			mExecList.addAll(bean.getExecList());
		}
		if (mExecList != null && mExecList.size() > 0) {
			mViewExec.setVisibility(View.VISIBLE);
			mAdapterExec.notifyDataSetChanged();
		} else {
			mViewExec.setVisibility(View.GONE);
		}
	}

	// 初始化底部的操作按钮
	private void showBottomButton(ShenPiDetialBean bean) {
		//1.发起人：撤销 2.最终审核人：同意，拒绝，退回 3.第一审核人：同意，拒绝，转交 4.普通审核人：同意，拒绝，转交，退回 5.转交人：同意，拒绝, 退回
		// 审批流程没走完
		if ("2".equals(bean.getIsOver())) {
			if (2 == needCheck) {
				// 1.发起人
				mViewBottom.setVisibility(View.VISIBLE);
				mLine1.setVisibility(View.GONE);
				mLine2.setVisibility(View.GONE);
				mViewAgress.setVisibility(View.GONE);
				mViewBack.setVisibility(View.GONE);
				mViewZhuangjiao.setVisibility(View.GONE);
				mTvChexiao.setText("撤销");
			} else if(1 == needCheck){// 我审核的
				LiuChengListBean item = isCheckMember(bean);
				if (item != null && SPUtils.getID().equals(item.getMemberId())) {
					//4.普通审核人
					mViewBottom.setVisibility(View.VISIBLE);
					String checkNum = item.getCheckNum();
					String isApprover = item.getIsApprover();
					String isZj = item.getIsZr();
					if("1".equals(isApprover)){
						//2.最终审核人
						mViewZhuangjiao.setVisibility(View.GONE);
					}else if ("1".equals(checkNum)){
						//3.第一审核人
						mViewBack.setVisibility(View.GONE);
					}else if ("1".equals(isZj)){
						//5.转交人
						mViewZhuangjiao.setVisibility(View.GONE);
					}
				} else {
					// 当前审核人不是我 隐藏按钮
					mViewBottom.setVisibility(View.GONE);
				}
			}else if(3 == needCheck){// 我执行的
				mViewBottom.setVisibility(View.GONE);
			}
		} else {
			// 审批流程已经走完
			mViewBottom.setVisibility(View.GONE);
		}
	}

	/**
	 * 审批操作 checkTp（操作类型 2 同意 3 拒绝4 转发）, int memId（转发的对象,当checkTp=3时必传）
	 * */
	private void showDialogDes(final String checkTp, final String nextMemId) {
		MyEditDialog dialog = new MyEditDialog(context, "请输入理由(非必填)");
		dialog.show();
		dialog.setOnClickListener(new MyEditDialog.OnClickListener() {
			@Override
			public void setOnClickListener(String text) {
				getP().updateStatusShenpi(context, id, checkTp, text, nextMemId);
			}
		});

	}
	
	//转交
	private void zhuanJiao() {
		if (MyCollectionUtil.isEmpty(mTreeDataList)) {
			getP().queryMemberList(context);
		} else {
			showDialogMember(mTreeDataList);
		}
	}
	
	//显示“转交”对话框
	public void showZhuanJiaoDL() {
		showDialogDes("4", String.valueOf(bean.getMemberId()));
	}

	private AuditProcessAdapter mShenpiProcessAdapter;
	private void refreshAdapterProcess() {
		if (mShenpiProcessAdapter == null) {
			mShenpiProcessAdapter = new AuditProcessAdapter(context, checkList);
			mListViewLiucheng.setAdapter(mShenpiProcessAdapter);
		} else {
			mShenpiProcessAdapter.notifyDataSetChanged();
		}
	}

	//附件——适配器
	private void refreshAdapterFujian() {
		if (mFujianAdapter == null) {
			mFujianAdapter = new FuJianAdapter(context, fileBeanList,false);
			mListViewFujian.setAdapter(mFujianAdapter);
		} else {
			mFujianAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 循环从流程列表判断是否是审批人
	 * */
	private LiuChengListBean isCheckMember(ShenPiDetialBean bean) {
		List<LiuChengListBean> checkList = bean.getCheckList();
		for (int i = 0; i < checkList.size(); i++) {
			LiuChengListBean liuChengListBean = checkList.get(i);
			if ("1".equals(liuChengListBean.getIsCheck())) {
				return liuChengListBean;
			}
		}
		return null;
	}

	public void removeBean() {
		if (this.bean != null) {
			this.bean = null;
		}
	}
	private MemberBean bean;
	public void setBean(MemberBean memberBean) {
		this.bean = memberBean;
	}
	public MemberBean getBean() {
		return this.bean;
	}


	private List<TreeBean> mTreeDataList = new ArrayList<>();
	public void showDialogMember(List<TreeBean> treeBeans) {
		try {
			if (MyCollectionUtil.isEmpty(mTreeDataList)) {
				this.mTreeDataList = treeBeans;
			}
			MyTreeDialog treeDialog = new MyTreeDialog(context, mTreeDataList, null, false);
			treeDialog.title("选择部门,员工").show();
			treeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
				@Override
				public void onOkListener(String containPIds, String noContainPIds, Map<Integer, Integer> checkMap) {
					showDialogDes("4",noContainPIds);
				}
			});
		} catch (Exception e) {
			ToastUtils.showError(e);
		}
	}


}
