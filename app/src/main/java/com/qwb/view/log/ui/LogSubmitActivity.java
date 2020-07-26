package com.qwb.view.log.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.qwb.view.audit.ui.AddShenPiRenActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.application.MyApp;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.xmsx.cnlife.widget.ComputeHeightGridView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.log.model.LogStrLengthBean;
import com.qwb.db.SaveLogBean;
import com.qwb.view.log.parsent.PLogSubmit;
import com.qwb.view.audit.ui.AddCurrentIds;
import com.qwb.view.audit.adapter.FuJianAdapter;
import com.qwb.view.audit.adapter.ShenPi_PersonAdatper;
import com.qwb.view.audit.adapter.ShenPi_PicAdatper;
import com.qwb.utils.Constans;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.MyPopWindowManager.OnImageBack;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.file.ui.ChooseFileActivity;
import com.qwb.view.file.model.FileBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 工作汇报模块：日报;周报;月报
 */
public class LogSubmitActivity extends XActivity<PLogSubmit> implements OnClickListener{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_log_submit;
	}

	@Override
	public PLogSubmit newP() {
		return new PLogSubmit();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		Intent intent = getIntent();
		if (intent != null) {
			tp = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
		}
		initUI();
		getP().loadDataStrLength(context,tp);// 限制文字输入的字数
		queryDataDraft();//查询数据库--草稿
		getP().queryToken(null);
	}

	private int tp = 1;// 1:日报（默认）2：周报 3：月报

	/**
	 * UI
	 */
	private void initUI() {
		initHead();
		initEditView();//输入框相关的
		initPersonAndPic();// 初始化 添加照片跟添加人模块
		initAttachment();// 附件
	}

	/**
	 * 头部
	 */
	private TextView tv_headRight;
	private TextView tv_headTitle;
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyApp.getImm().hideSoftInputFromWindow(tv_headTitle.getWindowToken(),0);//强制关闭软键盘
				Router.pop(context);
			}
		});
		findViewById(R.id.tv_head_right).setOnClickListener(this);
		tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		switch (tp) {
			case 1:
				tv_headTitle.setText("写日报");
				break;
			case 2:
				tv_headTitle.setText("写周报");
				break;
			case 3:
				tv_headTitle.setText("写月报");
				break;
		}
		tv_headRight.setText("提交");
	}


	/**
	 * 输入框相关的
	 */
	private TextView tv_gzNr_num;
	private TextView tv_gzZj_num;
	private TextView tv_gzJh_num;
	private TextView tv_gzBz_num;
	private TextView tv_remo_num;
	private EditText edit_gzNr;
	private EditText edit_gzZj;
	private EditText edit_gzJh;
	private EditText edit_gzBz;
	private EditText edit_remo;
	// 限制文字数量
	private int int_gzNrcd = 0;
	private int int_gzZjcd = 0;
	private int int_gzJhcd = 0;
	private int int_gzBzcd = 0;
	private int int_remocd = 0;
	private void initEditView() {
		TextView tv_gzNr = (TextView) findViewById(R.id.tv_gzNr);
		TextView tv_gzZj = (TextView) findViewById(R.id.tv_gzZj);
		TextView tv_gzJh = (TextView) findViewById(R.id.tv_gzJh);
		TextView tv_gzBz = (TextView) findViewById(R.id.tv_gzBz);
		TextView tv_remo = (TextView) findViewById(R.id.tv_remo);
		RelativeLayout rl_gzBz = (RelativeLayout) findViewById(R.id.rl_gzBz);
		tv_gzNr_num = (TextView) findViewById(R.id.tv_gzNr_num);
		tv_gzZj_num = (TextView) findViewById(R.id.tv_gzZj_num);
		tv_gzJh_num = (TextView) findViewById(R.id.tv_gzJh_num);
		tv_gzBz_num = (TextView) findViewById(R.id.tv_gzBz_num);
		tv_remo_num = (TextView) findViewById(R.id.tv_remo_num);
		edit_gzNr = (EditText) findViewById(R.id.edit_gzNr);
		edit_gzZj = (EditText) findViewById(R.id.edit_gzZj);
		edit_gzJh = (EditText) findViewById(R.id.edit_gzJh);
		edit_gzBz = (EditText) findViewById(R.id.edit_gzBz);
		edit_remo = (EditText) findViewById(R.id.edit_remo);
		edit_gzNr.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int length = s.toString().length();
				if (int_gzNrcd != 0) {
					tv_gzNr_num.setText("最少" + int_gzNrcd + "已写" + length);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		edit_gzZj.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int length = s.toString().length();
				if (int_gzZjcd != 0) {
					tv_gzZj_num.setText("最少" + int_gzZjcd + "已写" + length);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		edit_gzJh.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int length = s.toString().length();
				if (int_gzJhcd != 0) {
					tv_gzJh_num.setText("最少" + int_gzJhcd + "已写" + length);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		edit_gzBz.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int length = s.toString().length();
				if (int_gzBzcd != 0) {
					tv_gzBz_num.setText("最少" + int_gzBzcd + "已写" + length);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		edit_remo.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int length = s.toString().length();
				if (int_remocd != 0) {
					tv_remo_num.setText("最少" + int_remocd + "已写" + length);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		switch (tp) {
		case 1:
			tv_gzNr.setText("今日完成工作");
			tv_gzZj.setText("　未完成工作");
			tv_remo.setText("　　　　备注");
			tv_gzJh.setText("　需调协工作");
			rl_gzBz.setVisibility(View.GONE);
			break;
		case 2:
			tv_gzNr.setText("本周完成工作");
			tv_gzZj.setText("本周工作总结");
			tv_gzJh.setText("下周工作计划");
			tv_gzBz.setText("需调协与帮助");
			tv_remo.setText("　　　　备注");
			break;
		case 3:
			tv_gzNr.setText("本月工作内容");
			tv_gzZj.setText("本月工作总结");
			tv_gzJh.setText("下月工作计划");
			tv_gzBz.setText("需帮助与支持");
			tv_remo.setText("　　　　备注");
			break;
		}
	}

	/**
	 * 初始化 添加照片跟添加人模块
	 */
	private InputMethodManager imm;
	private ComputeHeightGridView gv_pic;
	private boolean isDelModel;
	private ShenPi_PersonAdatper personAdapter;
	private ComputeHeightGridView gv_person;
	private boolean isDelModel_Person;
	private void initPersonAndPic() {
		TextView tv_shenpiRen = (TextView) findViewById(R.id.tv_shenpiRen);
		tv_shenpiRen.setText("发给谁");
		imm = MyApp.getImm();
//		});
		gv_pic = findViewById(R.id.gv_pic);
		gv_pic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				int size = Constans.publish_pics.size();
				if (isDelModel) {
					if (size > 0) {
						if (position == size) {
							addPic(view);
						} else if (position == size + 1) {
							isDelModel = false;
						} else {
							Constans.publish_pics.remove(position);
							selImageList.remove(position);
						}
					} else {
						addPic(view);
					}
					refreshAdapter(isDelModel);
				} else {

					if (size <= 0) {
						addPic(view);
					} else {
						if (size == position) {
							if (size >= 6) {
								ToastUtils.showCustomToast("最多只能添加6张图片");
								return;
							}
							addPic(view);
						} else if (size + 1 == position) {
							isDelModel = true;
							refreshAdapter(isDelModel);
						} else {

							String[] urls = new String[size];
							for (int i = 0; i < size; i++) {
								urls[i] = "file://" + Constans.publish_pics.get(i);
							}
							Intent intent = new Intent(context, ImagePagerActivity.class);
							intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
							intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
							startActivity(intent);
						}
					}
				}
			}
		});
		refreshAdapter(false);
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
		refreshPersonAdapter(false);
	}

	/**
	 * 附件
	 */
	private FuJianAdapter fuJianAdapter;// 附件适配器
	private ArrayList<FileBean> fileList = new ArrayList<>();
	private void initAttachment() {
		findViewById(R.id.img_fujian).setOnClickListener(this);
		ComputeHeightListView listview_fujian = (ComputeHeightListView) findViewById(R.id.listview_fujian);
		fuJianAdapter = new FuJianAdapter(this, fileList, true);
		listview_fujian.setAdapter(fuJianAdapter);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_head_right:
			addData();
			OtherUtils.setSubmitDelay(context,tv_headRight);
			break;
		case R.id.img_fujian:// 附件
			Intent intent = new Intent(context, ChooseFileActivity.class);
			intent.putExtra("isSingle", false);// 是否单选，true:单选，false：多选
			startActivityForResult(intent, Constans.TAKE_PIC_YUNPAN);
			break;
		}
	}


	/**
	 * 选择图片：拍照和相册
	 */
	private ArrayList<ImageItem> selImageList=new ArrayList<>(); //当前选择的所有图片
	private OnImageBack myImageBack = new OnImageBack() {
		@Override
		public void fromPhotoAlbum() {
			Constans.pic_tag = 2;
			//打开选择,本次允许选择的数量
			ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount-selImageList.size());
			ImagePicker.getInstance().setCrop(false);
			ImagePicker.getInstance().setMultiMode(true);
			Intent intent1 = new Intent(LogSubmitActivity.this, ImageGridActivity.class);
			startActivityForResult(intent1, Constans.TAKE_PIC_XC);
		}

		@Override
		public void fromCamera() {
			new RxPermissions(LogSubmitActivity.this)
					.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
					.subscribe(new Consumer<Boolean>() {
						@Override
						public void accept(Boolean granted) throws Exception {
							if (granted) {//TODO 许可
								ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount-selImageList.size());
								ImagePicker.getInstance().setCrop(false);
								ImagePicker.getInstance().setMultiMode(true);
								Intent intent = new Intent(LogSubmitActivity.this, ImageGridActivity.class);
								intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
								startActivityForResult(intent, Constans.TAKE_PIC_XJ);
							} else {
								//TODO 未许可
								ToastUtils.showCustomToast("权限未开启！");
							}
						}
					});
		}
	};

	private void addPic(View view) {
		isDelModel = false;
		refreshAdapter(isDelModel);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0); // 强制隐藏键盘
		}
		Constans.pic_tag = 2;
		Constans.current.clear();
		if (Constans.publish_pics.size() > 0) {
			Constans.current.addAll(Constans.publish_pics);
		}
		MyPopWindowManager.getI().show(this, myImageBack, "", "");
	}

	private ShenPi_PicAdatper picAdapter;
	private void refreshAdapter(boolean isDelModel) {
		if (picAdapter == null) {
			picAdapter = new ShenPi_PicAdatper(this);
			gv_pic.setAdapter(picAdapter);
		} else {
			picAdapter.refreshAdapter(isDelModel);
		}
	}

	private void refreshPersonAdapter(boolean isDelModel) {
		if (personAdapter == null) {
			switch (tp) {
			case 1:
				personAdapter = new ShenPi_PersonAdatper(this,
						AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_RB, true));
				break;
			case 2:
				personAdapter = new ShenPi_PersonAdatper(this,
						AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_ZB, true));
				break;
			case 3:
				personAdapter = new ShenPi_PersonAdatper(this,
						AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_YB, true));
				break;
			}
			gv_person.setAdapter(personAdapter);
		} else {
			personAdapter.refreshAdapter(isDelModel);
		}
	}

	/**
	 * 跳转添加审批人act
	 */
	private void toAddRenACT() {
		isDelModel_Person = false;
		Intent intent = new Intent(context, AddShenPiRenActivity.class);
		switch (tp) {
		case 1:
			intent.putExtra("type", AddCurrentIds.TYPE_LOG_RB);
			break;
		case 2:
			intent.putExtra("type", AddCurrentIds.TYPE_LOG_ZB);
			break;
		case 3:
			intent.putExtra("type", AddCurrentIds.TYPE_LOG_YB);
			break;
		}
		intent.putExtra("title", "选择审批人");
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && Constans.TAKE_PIC_YUNPAN == requestCode) {
			// 附件
			ArrayList<FileBean> fileBeans = data.getParcelableArrayListExtra("fileBean");
			fileList.addAll(fileBeans);
			fuJianAdapter.notifyDataSetChanged();
		}

		//图片选择器-添加图片返回
		if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
			ArrayList<ImageItem> images=(ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
			if (images!=null ){
				selImageList.addAll(images);
				Constans.publish_pics.clear();
				for (int i=0;i<selImageList.size();i++){
					Constans.publish_pics.add(selImageList.get(i).path);
				}
				//相机-记录拍照的集合（要删除拍照图片）
				if(requestCode==Constans.TAKE_PIC_XJ){
					for (int j=0;j<images.size();j++){
						Constans.publish_pics_xj.add(images.get(j).path);
					}
				}
				refreshAdapter(false);
			}
		}
	}

	@Override
	protected void onResume() {
		refreshAdapter(isDelModel = false);
		refreshPersonAdapter(isDelModel_Person = false);
		super.onResume();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		OtherUtils.resetStepStatus(LogSubmitActivity.this);//重置默认状态,删除图片
		if(isSaveDelState){
			//保存草稿
			saveDataDraft();
		}else{
			//删除草稿
			delDataDraft();
		}
	}


	//TODO **********************************接口******************************************
	/**
	 * 提交数据
	 */
	private void addData() {
		String gzNr = edit_gzNr.getText().toString().trim();
		if (TextUtils.isEmpty(gzNr)) {
			switch (tp) {
				case 1:
					ToastUtils.showCustomToast("请输入今日完成工作");
					break;
				case 2:
					ToastUtils.showCustomToast("请输入本周完成工作");
					break;
				case 3:
					ToastUtils.showCustomToast("请输入本月工作内容");
					break;
			}
			return;
		} else {
			if (int_gzNrcd != 0 && gzNr.length() < int_gzNrcd) {
				switch (tp) {
					case 1:
						ToastUtils.showCustomToast("今日完成工作不能少于" + int_gzNrcd + "个数");
						break;
					case 2:
						ToastUtils.showCustomToast("本周完成工作不能少于" + int_gzNrcd + "个数");
						break;
					case 3:
						ToastUtils.showCustomToast("本月工作内容不能少于" + int_gzNrcd + "个数");
						break;
				}
				return;
			}
		}
		String gzZj = edit_gzZj.getText().toString().trim();
		if (TextUtils.isEmpty(gzZj)) {
			switch (tp) {
				case 1:
					ToastUtils.showCustomToast("请输入未完成工作");
					break;
				case 2:
					ToastUtils.showCustomToast("请输入本周工作总结");
					break;
				case 3:
					ToastUtils.showCustomToast("请输入本月工作总结");
					break;
			}
			return;
		} else {
			if (int_gzZjcd != 0 && gzZj.length() < int_gzZjcd) {
				switch (tp) {
					case 1:
						ToastUtils.showCustomToast("未完成工作不能少于" + int_gzZjcd + "个数");
						break;
					case 2:
						ToastUtils.showCustomToast("本周工作总结不能少于" + int_gzZjcd + "个数");
						break;
					case 3:
						ToastUtils.showCustomToast("本月工作总结不能少于" + int_gzZjcd + "个数");
						break;
				}
				return;
			}
		}
		String gzJh = edit_gzJh.getText().toString().trim();
		if (TextUtils.isEmpty(gzJh)) {
			switch (tp) {
				case 1:
					ToastUtils.showCustomToast("请输入需调协工作");
					break;
				case 2:
					ToastUtils.showCustomToast("请输入下周工作计划");
					return;
				case 3:
					ToastUtils.showCustomToast("请输入下月工作计划");
					return;
			}
		} else {
			if (int_gzJhcd != 0 && gzJh.length() < int_gzJhcd) {
				switch (tp) {
					case 1:
						ToastUtils.showCustomToast("需调协工作不能少于" + int_gzJhcd + "个数");
						break;
					case 2:
						ToastUtils.showCustomToast("下周工作计划不能少于" + int_gzJhcd + "个数");
						break;
					case 3:
						ToastUtils.showCustomToast("下月工作计划不能少于" + int_gzJhcd + "个数");
						break;
				}
				return;
			}
		}
		switch (tp) {
			case 1:
				if (AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_RB, false).size() <= 0) {
					ToastUtils.showCustomToast("请选择发给谁");
					return;
				}
				break;
			case 2:
				if (AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_ZB, false).size() <= 0) {
					ToastUtils.showCustomToast("请选择发给谁");
					return;
				}
				break;
			case 3:
				if (AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_YB, false).size() <= 0) {
					ToastUtils.showCustomToast("请选择发给谁");
					return;
				}
				break;
		}
		String gzBz = edit_gzBz.getText().toString().trim();
		String remo = edit_remo.getText().toString().trim();

		getP().addData(context,tp,gzNr,gzZj,gzJh,gzBz,remo,getIds(),fileList,mQueryToken);
	}

	/**
	 * 获取成员ids
	 */
	private String getIds() {
		StringBuffer sb = new StringBuffer();
		List<MemberBean> choiseIds = null;
		switch (tp) {
			case 1:
				choiseIds = AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_RB, false);
				break;
			case 2:
				choiseIds = AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_ZB, false);
				break;
			case 3:
				choiseIds = AddCurrentIds.getI().getChoiseIds(AddCurrentIds.TYPE_LOG_YB, false);
				break;
		}
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

	// 限制文字输入的字数
	public void setDataStrLength(LogStrLengthBean data){
		int_gzNrcd = data.getGzNrcd();
		int_gzZjcd = data.getGzZjcd();
		int_remocd = data.getRemocd();
		// 注意：日报时，第三和第四替换下:日报少一行
		if (tp == 1) {
			int_gzJhcd = data.getGzBzcd();
			int_gzBzcd = data.getGzJhcd();
		} else {
			int_gzJhcd = data.getGzJhcd();
			int_gzBzcd = data.getGzBzcd();
		}
		//查询数据库
		queryDataDraft();
	}

	/**
	 * 提交成功
	 */
	public void addDataSuccess(){
		switch (tp) {
			case 1:
				AddCurrentIds.getI().saveToDB(AddCurrentIds.TYPE_LOG_RB);// 保存到数据库
				break;
			case 2:
				AddCurrentIds.getI().saveToDB(AddCurrentIds.TYPE_LOG_ZB);// 保存到数据库
				break;
			case 3:
				AddCurrentIds.getI().saveToDB(AddCurrentIds.TYPE_LOG_YB);// 保存到数据库
				break;
		}

		isSaveDelState=false;//删除草稿（默认保存草稿）
		Router.pop(context);//关闭界面
	}

	//***********************************数据库操作：草稿************************************************
	private boolean isSaveDelState=true;//是保存还是删除（默认保存草稿）
	public void saveDataDraft(){
		String gzNr = edit_gzNr.getText().toString().trim();
		String gzZj = edit_gzZj.getText().toString().trim();
		String gzJh = edit_gzJh.getText().toString().trim();
		String gzBz = edit_gzBz.getText().toString().trim();
		String remo = edit_remo.getText().toString().trim();
		if(!MyUtils.isEmptyString(gzNr) || !MyUtils.isEmptyString(gzZj) || !MyUtils.isEmptyString(gzJh) || !MyUtils.isEmptyString(gzBz) || !MyUtils.isEmptyString(remo)){
			SaveLogBean bean=new SaveLogBean();
			bean.setGzNrcd(gzNr);
			bean.setGzZjcd(gzZj);
			bean.setGzJhcd(gzJh);
			bean.setGzBzcd(gzBz);
			bean.setRemocd(remo);
			bean.setUserId(SPUtils.getID());
			bean.setCompanyId(SPUtils.getCompanyId());
			bean.setType(String.valueOf(tp));
			MyDataUtils.getInstance().saveLog(bean,SPUtils.getID(),SPUtils.getCompanyId(),String.valueOf(tp));
		}
	}

	//删除数据库-草稿
	public void delDataDraft(){
		MyDataUtils.getInstance().delLog(SPUtils.getID(),SPUtils.getCompanyId(),String.valueOf(tp));
	}

	//查询数据库-草稿
	public void queryDataDraft() {
		SaveLogBean bean = MyDataUtils.getInstance().queryLog(SPUtils.getID(),SPUtils.getCompanyId(),String.valueOf(tp));
		if(bean==null){
			//数据库有-没有草稿
			if (int_gzNrcd > 0) {
				tv_gzNr_num.setVisibility(View.VISIBLE);
				tv_gzNr_num.setText("最少" + int_gzNrcd + "已写0");
			}
			if (int_gzZjcd > 0) {
				tv_gzZj_num.setVisibility(View.VISIBLE);
				tv_gzZj_num.setText("最少" + int_gzZjcd + "已写0");
			}
			if (int_gzJhcd > 0) {
				tv_gzJh_num.setVisibility(View.VISIBLE);
				tv_gzJh_num.setText("最少" + int_gzJhcd + "已写0");
			}
			if (int_gzBzcd > 0) {
				tv_gzBz_num.setVisibility(View.VISIBLE);
				tv_gzBz_num.setText("最少" + int_gzBzcd + "已写0");
			}
			if (int_remocd > 0) {
				tv_remo_num.setVisibility(View.VISIBLE);
				tv_remo_num.setText("最少" + int_remocd + "已写0");
			}
		}else{
			//数据库有-草稿
			edit_gzNr.setText(bean.getGzNrcd());
			edit_gzZj.setText(bean.getGzZjcd());
			edit_gzJh.setText(bean.getGzJhcd());
			edit_gzBz.setText(bean.getGzBzcd());
			edit_remo.setText(bean.getRemocd());
			if (int_gzNrcd > 0) {
				tv_gzNr_num.setVisibility(View.VISIBLE);
				if (!MyUtils.isEmptyString(bean.getGzNrcd())) {
					tv_gzNr_num.setText("最少" + int_gzNrcd + "已写"+bean.getGzNrcd().trim().length());
				}else{
					tv_gzNr_num.setText("最少" + int_gzNrcd + "已写0");
				}
			}
			if (int_gzZjcd > 0) {
				tv_gzZj_num.setVisibility(View.VISIBLE);
				if (!MyUtils.isEmptyString(bean.getGzZjcd())) {
					tv_gzZj_num.setText("最少" + int_gzNrcd + "已写"+bean.getGzZjcd().trim().length());
				}else{
					tv_gzZj_num.setText("最少" + int_gzNrcd + "已写0");
				}
			}
			if (int_gzJhcd > 0) {
				tv_gzJh_num.setVisibility(View.VISIBLE);
				if (!MyUtils.isEmptyString(bean.getGzJhcd())) {
					tv_gzJh_num.setText("最少" + int_gzNrcd + "已写"+bean.getGzJhcd().trim().length());
				}else{
					tv_gzJh_num.setText("最少" + int_gzNrcd + "已写0");
				}
			}
			if (int_gzBzcd > 0) {
				tv_gzBz_num.setVisibility(View.VISIBLE);
				if (!MyUtils.isEmptyString(bean.getGzBzcd())) {
					tv_gzBz_num.setText("最少" + int_gzNrcd + "已写"+bean.getGzBzcd().trim().length());
				}else{
					tv_gzBz_num.setText("最少" + int_gzNrcd + "已写0");
				}
			}
			if (int_remocd > 0) {
				tv_remo_num.setVisibility(View.VISIBLE);
				if (!MyUtils.isEmptyString(bean.getRemocd())) {
					tv_remo_num.setText("最少" + int_gzNrcd + "已写"+bean.getRemocd().trim().length());
				}else{
					tv_remo_num.setText("最少" + int_gzNrcd + "已写0");
				}
			}
		}
	}

	//避免重复的token
	private String mQueryToken;
	public void doToken(String data){
		mQueryToken = data;
	}



}
