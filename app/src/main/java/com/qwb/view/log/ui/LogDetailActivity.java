package com.qwb.view.log.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.log.parsent.PLogDetails;
import com.qwb.view.log.adapter.LogDetailPicAdapter;
import com.qwb.view.audit.adapter.FuJianAdapter;
import com.qwb.view.log.adapter.CommentAdapter;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.file.ui.FileDetailActivity;
import com.qwb.view.log.model.RizhiDetialBean;
import com.qwb.view.log.model.RizhiDetialBean.List2;
import com.qwb.view.log.model.RizhiListBean.RizhiList;
import com.qwb.view.log.model.RizhiPinlunBean;
import com.qwb.view.log.model.RizhiPinlunBean.RizhiPinlun;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 日报；周报；月报；--详情
 */
public class LogDetailActivity extends XActivity<PLogDetails> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_log_detial;
	}

	@Override
	public PLogDetails newP() {
		return new PLogDetails();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		Intent intent = getIntent();
		if (intent != null) {
			rizhi = (RizhiList) intent.getSerializableExtra(ConstantUtils.Intent.LOOK_WORK_TABLE_DETAILS);
		}

		initUI();
		getP().loadData(context,rizhi.getId());
		getP().loadDataComment(context,rizhi.getId());

		// TODO 复制功能
		scale = getResources().getDisplayMetrics().density;
		creatPop();
	}

	private RizhiList rizhi;
	@BindView(R.id.et_content)
	EditText et_content;
	@BindView(R.id.tv_huifu)
	TextView tv_huifu;
	@BindView(R.id.lv_huifu)
	ComputeHeightListView lv_huifu;
	private void initUI() {
		initHead();
		initBaseView();
	}

	/**
	 *  头部
	 */
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色；透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Router.pop(context);
			}
		});
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		switch (rizhi.getTp()) {
			case 1:
				tv_headTitle.setText("日报详情");
				break;
			case 2:
				tv_headTitle.setText("周报详情");
				break;
			case 3:
				tv_headTitle.setText("月报详情");
				break;
		}
	}

	//列表；详情
	@BindView(R.id.iv_head)
	TextView iv_head;
	@BindView(R.id.tv_title)
	TextView tv_title;
	@BindView(R.id.tv_time)
	TextView tv_time;

	@BindView(R.id.tv_gzNr)
	TextView tv_gzNr;
	@BindView(R.id.tv_gzZj)
	TextView tv_gzZj;
	@BindView(R.id.tv_gzJh)
	TextView tv_gzJh;
	@BindView(R.id.tv_gzBz)
	TextView tv_gzBz;
	@BindView(R.id.tv_remo)
	TextView tv_remo;

	@BindView(R.id.tv_gzNr0)
	TextView tv_gzNr0;
	@BindView(R.id.tv_gzZj0)
	TextView tv_gzZj0;
	@BindView(R.id.tv_gzJh0)
	TextView tv_gzJh0;
	@BindView(R.id.tv_gzBz0)
	TextView tv_gzBz0;
	@BindView(R.id.tv_remo0)
	TextView tv_remo0;

	@BindView(R.id.rl_fujian)
	RelativeLayout rl_fujian;
	@BindView(R.id.listview_fujian)
	ComputeHeightListView listview_fujian;
	private void initBaseView() {
		tv_gzNr.setOnLongClickListener(myCopyLongClickListener);
		tv_gzZj.setOnLongClickListener(myCopyLongClickListener);
		tv_gzJh.setOnLongClickListener(myCopyLongClickListener);
		tv_gzBz.setOnLongClickListener(myCopyLongClickListener);
		tv_remo.setOnLongClickListener(myCopyLongClickListener);

		iv_head.setText(rizhi.getMemberNm());
		tv_title.setText(rizhi.getMemberNm());
		tv_time.setText(rizhi.getFbTime());
		int tp = rizhi.getTp();
		switch (tp) {
			case 1:
				tv_gzNr0.setText("今日完成工作");
				tv_gzZj0.setText("未完成工作");
				tv_gzJh0.setText("需调协工作");
				tv_remo0.setText("备注");
				tv_gzBz0.setVisibility(View.GONE);
				tv_gzBz.setVisibility(View.GONE);
				break;
			case 2:
				tv_gzNr0.setText("本周完成工作");
				tv_gzZj0.setText("本周工作总结");
				tv_gzJh0.setText("下周工作计划");
				tv_gzBz0.setText("需调协与帮助");
				tv_remo0.setText("备注");
				break;
			case 3:
				tv_gzNr0.setText("本月工作 内容");
				tv_gzZj0.setText("本月工作总结");
				tv_gzJh0.setText("下月工作计划");
				tv_gzBz0.setText("需帮助与支持");
				tv_remo0.setText("备注");
				break;
		}

		listview_fujian.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				FileBean fileBean = fileBeanList.get(position);
				if (fileBean == null) {
					return;
				}
				String fileType = fileBean.getTp1();
				// 1：如果是图片类型（点击放大）2：跳到“文件详情”界面
				if ("png".equals(fileType) || "jpg".equals(fileType) || "bmp".equals(fileType)
						|| "jpeg".equals(fileType) || "gif".equals(fileType)) {
					// 点击放大图片
					String[] urls = new String[1];
					urls[0] = Constans.ROOT_imgUrl + fileBean.getFileNm();
					Intent intent = new Intent(LogDetailActivity.this, ImagePagerActivity.class);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
					startActivity(intent);
				} else {
					// fileBean:文件名，文件类型，文件大小
					Intent intent = new Intent(LogDetailActivity.this, FileDetailActivity.class);
					intent.putExtra("fileBean", fileBean);
					startActivity(intent);
				}
			}
		});
	}


	@OnClick({R.id.btn_send})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send:
			addData();
			break;
		}
	}


	// TODO***************************** 复制功能********************************
	private PopupWindow typePopupWindow;
	private String copyContent;
	private float scale;
	private OnLongClickListener myCopyLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			int[] location = new int[2];
			v.getLocationOnScreen(location);
			TextView tv_copy = (TextView) v;
			int height = tv_copy.getHeight();
			int width = tv_copy.getWidth();
			typePopupWindow.showAsDropDown(v, (int) (width / 2 - 40 * scale), (int) (-height - 25 * scale));
			copyContent = tv_copy.getText().toString().trim();
			return false;
		}
	};

	@SuppressWarnings({})
	ClipboardManager clipboardManager;

	private void creatPop() {
		TextView mPopupText = new TextView(this);
		mPopupText.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (clipboardManager == null) {
					clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				}
				if (MyUtils.isEmptyString(copyContent)) {
					ToastUtils.showCustomToast("复制的内容不能为空");
				} else {
					clipboardManager.setText(copyContent);
					ToastUtils.showCustomToast("复制成功");
				}
				typePopupWindow.dismiss();
			}
		});
		mPopupText.setBackgroundResource(R.drawable.copy_bg);
		mPopupText.setTextColor(Color.WHITE);
		mPopupText.setPadding(0, 0, 0, 15);
		mPopupText.setText("复制");
		mPopupText.setGravity(Gravity.CENTER);
		typePopupWindow = new PopupWindow(mPopupText, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		typePopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}




	//TODO ********************************************接口***********************************************
	// 添加报评论
	private void addData() {
		String content = et_content.getText().toString();
		if (MyUtils.isEmptyString(content)) {
			ToastUtils.showCustomToast("请填写评论内容");
			return;
		}
		getP().addDataComment(context,rizhi.getId(),content);
	}

	/**
	 * 显示数据
	 */
	public void showData(RizhiDetialBean parseObject) {
		int tp = rizhi.getTp();
		switch (tp) {
			case 1:
				tv_gzNr.setText(parseObject.getGzNr());
				tv_gzZj.setText(parseObject.getGzZj());
				tv_gzJh.setText(parseObject.getGzJh());
				tv_remo.setText(parseObject.getRemo());
				break;
			case 2:
				tv_gzNr.setText(parseObject.getGzNr());
				tv_gzZj.setText(parseObject.getGzZj());
				tv_gzJh.setText(parseObject.getGzJh());
				tv_gzBz.setText(parseObject.getGzBz());
				tv_remo.setText(parseObject.getRemo());
				break;
			case 3:
				tv_gzNr.setText(parseObject.getGzNr());
				tv_gzZj.setText(parseObject.getGzZj());
				tv_gzJh.setText(parseObject.getGzJh());
				tv_gzBz.setText(parseObject.getGzBz());
				tv_remo.setText(parseObject.getRemo());
				break;
		}

		// 附件
		String fileNms = parseObject.getFileNms();
		if (!MyUtils.isEmptyString(fileNms)) {
			String[] split = fileNms.split(",");
			for (int i = 0; i < split.length; i++) {
				String fileNm = split[i];
				int lastIndexOf = fileNm.lastIndexOf(".") + 1;
				String fileTp = fileNm.substring(lastIndexOf, fileNm.length());// 文件后缀
				FileBean fileBean = new FileBean();
				fileBean.setFileNm(fileNm);
				fileBean.setTp1(fileTp);
				fileBeanList.add(fileBean);
			}
			refreshAdapter_fujian();
		} else {
			rl_fujian.setVisibility(View.GONE);
		}

		// 图片列表
		View rl_pic_content = findViewById(R.id.rl_pic_content);
		final List<List2> list2 = parseObject.getList2();
		if (list2 != null && list2.size() > 0) {
			rl_pic_content.setVisibility(View.VISIBLE);
			ComputeHeightGridView gv_pics = (ComputeHeightGridView) findViewById(R.id.gv_pics);
			LogDetailPicAdapter picsAdapter = new LogDetailPicAdapter(context, list2);
			gv_pics.setAdapter(picsAdapter);

			gv_pics.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String[] urls = new String[list2.size()];
					for (int i = 0; i < list2.size(); i++) {
						urls[i] = Constans.IMGROOTHOST + list2.get(i).getPic();
					}
					// 点击放大图片
					Intent intent = new Intent(context, ImagePagerActivity.class);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
					intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
					startActivity(intent);
				}
			});
		} else {
			rl_pic_content.setVisibility(View.GONE);
		}
	}

	// 附件——适配器
	private FuJianAdapter fuJianAdapter;
	private List<FileBean> fileBeanList = new ArrayList<FileBean>();
	private void refreshAdapter_fujian() {
		if (fuJianAdapter == null) {
			fuJianAdapter = new FuJianAdapter(LogDetailActivity.this, fileBeanList, false);
			listview_fujian.setAdapter(fuJianAdapter);
		} else {
			fuJianAdapter.notifyDataSetChanged();
		}
	}

	// 附件——适配器
	List<RizhiPinlun> huifuList = new ArrayList<>();
	private CommentAdapter mCommentAdapter;
	public void refreshAdapterComment(RizhiPinlunBean data) {
		if(data!=null){
			tv_huifu.setText("共有" + data.getTotal() + "条回复");
			List<RizhiPinlun> rows = data.getRows();
			huifuList.clear();
			huifuList.addAll(rows);
			if (mCommentAdapter == null) {
				mCommentAdapter = new CommentAdapter(context, huifuList);
				lv_huifu.setAdapter(mCommentAdapter);
			} else {
				mCommentAdapter.notifyDataSetChanged();
			}
		}
	}

	//添加评论成功
	public void addDataSuccess(){
		et_content.setText("");
		getP().loadDataComment(context,rizhi.getId());
	}






}
