package com.qwb.view.step.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.step.parsent.PStep2;
import com.qwb.utils.Constans;
import com.qwb.utils.ILUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PicAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep2Bean;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.QueryBfsdhjcBean;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.utils.MyUrlUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：拜访2：生动化检查-注意很多地方用到Constans.pic_type:区分同一个界面有多个“拍照和相册”的功能
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class Step2Activity extends XActivity<PStep2> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_step2;
	}

	@Override
	public PStep2 newP() {
		return new PStep2();
	}

	private ImageLoader imageLoder;
	private DisplayImageOptions options;
	@Override
	public void initData(Bundle savedInstanceState) {
		imageLoder = ILUtil.getImageLoder();// 加载图片
		options = ILUtil.getOptionsSquere();
		initIntent();//注意放initUI()后面
		initUI();
		doIntent();
		getP().queryToken(null);
	}

	/**
	 * 初始化Intent
	 */
	private String cid;// 客户ID
	private String mKhNm;
	private String count2;//0:未上传（默认）；1：已上传
	private String mPdateStr;// 补拜访时间(拜访计划)
	private String mId;//生动化检查id
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
			mKhNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
			count2 = intent.getStringExtra(ConstantUtils.Intent.STEP);
			mPdateStr = intent.getStringExtra(ConstantUtils.Intent.SUPPLEMENT_TIME);
		}
	}

	private void doIntent() {
		mTvHeadTitle.setText(mKhNm);
		if ("1".equals(count2)) {// 已上传
			getP().loadDataInfo(context, cid, mPdateStr);//获取上次提交信息
			mTvHeadRight.setText("修改");
		} else {
			mTvHeadRight.setText("提交");
		}
		if(MyStringUtil.isEmpty(mPdateStr)){
			mTvCallOnDate.setText(MyTimeUtils.getTodayStr());
		}else{
			mTvCallOnDate.setText(mPdateStr);
		}
	}

	private void initUI() {
		initHead();
		initBaseView();
		initAdapter();
	}

	// 头部
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色；透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadRight.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				addData();
			}
		});
	}

	//基本ui
	@BindView(R.id.scrollView)
	ScrollView mScrollView;
	@BindView(R.id.tv_callOnDate)
	TextView mTvCallOnDate;
	@BindView(R.id.edit_pophb)
	EditText mEtPophb;
	@BindView(R.id.edit_cq)
	EditText mEtCq;
	@BindView(R.id.edit_wq)
	EditText mEtWq;
	@BindView(R.id.rb_wu1)
	RadioButton mRbWu;
	@BindView(R.id.rb_you1)
	RadioButton mRbYou;
	@BindView(R.id.edit_remo1)
	EditText mEtRemo1;
	@BindView(R.id.edit_remo2)
	EditText mEtRemo2;
	private void initBaseView() {
		mScrollView.smoothScrollTo(0, 0);
	}

	//适配器-图片
	@BindView(R.id.iv_add_pic)
	ImageView mIvAddPic;
	@BindView(R.id.iv_del_pic)
	ImageView mIvDelPic;
	@BindView(R.id.recyclerview_pic)
	RecyclerView mRecyclerViewPic;
	private PicAdapter mPicAdapter;
	@BindView(R.id.iv_add_pic2)
	ImageView mIvAddPic2;
	@BindView(R.id.iv_del_pic2)
	ImageView mIvDelPic2;
	@BindView(R.id.recyclerview_pic2)
	RecyclerView mRecyclerViewPic2;
	private PicAdapter mPicAdapter2;
	private int mPhotoType;//默认1生动化拍照；2:堆头拍照
	private void initAdapter() {
		mRecyclerViewPic.setHasFixedSize(true);
		mRecyclerViewPic.setLayoutManager(new GridLayoutManager(context,3));
		mPicAdapter = new PicAdapter(context);
		mPicAdapter.openLoadAnimation();
		mRecyclerViewPic.setAdapter(mPicAdapter);
		mPicAdapter.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
			@Override
			public void onDeletePicListener(int position) {
				mPicList.remove(position);
				refreshAdapter(mPicAdapter.isDelete(),null, position);
			}
		});
		//添加图片
		mIvAddPic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPicAdapter.getData().size() >= 6) {
					ToastUtils.showCustomToast("最多只能选6张图片！");
					return;
				}
				mPhotoType = 1;
				doCamera();
			}
		});
		//删除图片
		mIvDelPic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				refreshAdapter(!mPicAdapter.isDelete(),null, null );
			}
		});
		//-----------------------------2------------------------------------------------
		mRecyclerViewPic2.setHasFixedSize(true);
		mRecyclerViewPic2.setLayoutManager(new GridLayoutManager(context,3));
		mPicAdapter2 = new PicAdapter(context);
		mPicAdapter2.openLoadAnimation();
		mRecyclerViewPic2.setAdapter(mPicAdapter2);
		mPicAdapter2.setOnDeletePicListener(new PicAdapter.OnDeletePicListener() {
			@Override
			public void onDeletePicListener(int position) {
				mPicList2.remove(position);
				refreshAdapter2(mPicAdapter2.isDelete(),null, position);
			}
		});
		//添加图片
		mIvAddPic2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPicAdapter2.getData().size() >= 6) {
					ToastUtils.showCustomToast("最多只能选6张图片！");
					return;
				}
				mPhotoType = 2;
				doCamera();
			}
		});
		//删除图片
		mIvDelPic2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				refreshAdapter2(!mPicAdapter2.isDelete(),null, null );
			}
		});
	}

	/**
	 * 刷新适配器（isDelete：是否删除状态，movePosition：删除的下标）
	 */
	private List<String> mPicList = new ArrayList<>();//路径没有拼接file://：用来上传的
	private void refreshAdapter(boolean isDelete, List<String> picList, Integer movePosition) {
		List<String> datas = mPicAdapter.getData();
		mPicAdapter.setDelete(isDelete);
		if(null != picList){
			if(null == datas || datas.isEmpty()){
				mPicAdapter.setNewData(picList);
			}else{
				mPicAdapter.addData(picList);
			}
		}
		if(null != movePosition){
			mPicAdapter.remove(movePosition);
		}
		mPicAdapter.notifyDataSetChanged();
		List<String> datas2 = mPicAdapter.getData();
		if (null == datas2 || datas2.isEmpty()) {
			mIvDelPic.setVisibility(View.GONE);
		} else {
			mIvDelPic.setVisibility(View.VISIBLE);// 删除图标可见或不可见
		}
	}
	private List<String> mPicList2 = new ArrayList<>();//路径没有拼接file://：用来上传的
	private void refreshAdapter2(boolean isDelete, List<String> picList, Integer movePosition) {
		List<String> datas = mPicAdapter2.getData();
		mPicAdapter2.setDelete(isDelete);
		if(null != picList){
			if(null == datas || datas.isEmpty()){
				mPicAdapter2.setNewData(picList);
			}else{
				mPicAdapter2.addData(picList);
			}
		}
		if(null != movePosition){
			mPicAdapter2.remove(movePosition);
		}
		mPicAdapter2.notifyDataSetChanged();
		List<String> datas2 = mPicAdapter2.getData();
		if (null == datas2 || datas2.isEmpty()) {
			mIvDelPic2.setVisibility(View.GONE);
		} else {
			mIvDelPic2.setVisibility(View.VISIBLE);// 删除图标可见或不可见
		}
	}

	// 相机直接拍摄
	public void doCamera() {
		new RxPermissions(this)
				.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
				.subscribe(new Consumer<Boolean>() {
					@Override
					public void accept(Boolean granted) throws Exception {
						if (granted) {//TODO 许可
							Intent intent = new Intent(context, ImageGridActivity.class);
							intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
							startActivityForResult(intent, Constans.TAKE_PIC_XJ);
						} else {
							//TODO 未许可
							ToastUtils.showCustomToast("拍照权限和存储权限未开启，请在手机设置中打开权限！");
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
			ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
			if (images != null){
				List<String> picList = new ArrayList<>();
				for (ImageItem imageItem:images) {
					picList.add("file://"+imageItem.path);//图片显示
					if(mPhotoType == 2){
						mPicList2.add(imageItem.path);//上传图片的
						Constans.publish_pics2222.add(imageItem.path);//上传成功后删除手机图片
					}else{
						mPicList.add(imageItem.path);//上传图片的
						Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
					}
				}
				if(mPhotoType == 2){
					refreshAdapter2(false, picList, null );
				}else{
					refreshAdapter(false, picList, null );
				}

			}
		}
	}


//TODO ***********************************接口相关*********************************************
	private String mPophbStr;
	private String mCqStr;
	private String mWqStr;
	private String mRemoStr1;
	private String mRemoStr2;
	private String mIsXy;
	private void addData() {
		mPophbStr = mEtPophb.getText().toString().trim();
		mCqStr = mEtCq.getText().toString().trim();
		mWqStr = mEtWq.getText().toString().trim();
		mRemoStr1 = mEtRemo1.getText().toString().trim();
		mRemoStr2 = mEtRemo2.getText().toString().trim();
		if (mRbYou.isChecked()) {// 是否显眼（1有，2无）
			mIsXy="1";
		} else {
			mIsXy="2";
		}
		if(!MyNetWorkUtil.isNetworkConnected()){
			ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
			showDialogCache();
			return;
		}
		getP().addData(context,count2, cid, mId, mPdateStr,mPophbStr,mCqStr,mWqStr,mRemoStr1,mRemoStr2,mIsXy,mPicList,mPicList2, mQueryToken);
	}

	//显示上传提交的数据信息
	public void showInfo(QueryBfsdhjcBean data){
		mId = data.getId();// 生动化检查id
		mEtPophb.setText(data.getPophb());
		mEtCq.setText(data.getCq());
		mEtWq.setText(data.getWq());
		mEtRemo1.setText(data.getRemo1());
		mEtRemo2.setText(data.getRemo2());
		if ("1".equals(data.getIsXy())) {
			mRbYou.setChecked(true);
			mRbWu.setChecked(false);
		} else {
			mRbYou.setChecked(false);
			mRbWu.setChecked(true);
		}
		// 图片
		List<QueryBfsdhjcBean.SdPhoto1> list1 = data.getList1();
		List<String> picList = new ArrayList<>();
		if (list1 != null && list1.size() > 0) {
			for (int i = 0; i < list1.size(); i++) {
				QueryBfsdhjcBean.SdPhoto1 sdPhoto1 = list1.get(i);
				picList.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + sdPhoto1.getPic()));//图片显示
			}
		}
		List<QueryBfsdhjcBean.SdPhoto2> list2 = data.getList2();
		List<String> picList2 = new ArrayList<>();
		if (list2 != null && list2.size() > 0) {
			for (int i = 0; i < list2.size(); i++) {
				QueryBfsdhjcBean.SdPhoto2 sdPhoto2 = list2.get(i);
				picList2.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + sdPhoto2.getPic()));//图片显示
			}
		}
		refreshAdapter(false, picList, null);
		refreshAdapter2(false, picList2, null);
		saveImg(picList, 1);
		saveImg(picList2, 2);
	}

	//下载图片并保存文件
	private void saveImg(final List<String> list, final int type) {
		if(null == list || list.isEmpty()){
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			final int j = i;
			imageLoder.loadImage(list.get(i), options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					super.onLoadingComplete(imageUri, view, loadedImage);
					// 第一次保存的文件路径容器不等于图片url容器长度，就下载，添加
					// 后续选择会往同时往url路径和图片路径增加，相同，就不继续下载
					File outDir = new File(Constans.DIR_IMAGE_PROCEDURE);// 保存到临时文件夹
					if (!outDir.exists()) {
						outDir.mkdirs();
					}
					File file = new File(outDir, String.valueOf(System.currentTimeMillis()) + ".jpg");
					try {
						// 压缩图片 100为不压缩
						FileOutputStream fos = new FileOutputStream(file);
						loadedImage.compress(CompressFormat.JPEG, 100, fos);
						fos.flush();
						fos.close();
						if(2 == type){
							Constans.publish_pics2222.add(file.getAbsolutePath());
							//j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
							mPicList2.add(j, file.getAbsolutePath());//上传图片的
						}else{
							Constans.publish_pics1111.add(file.getAbsolutePath());
							//j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
							mPicList.add(j, file.getAbsolutePath());//上传图片的
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	//提交数据成功-添加或修改
	public void submitDataSuccess(){
		Intent data = new Intent();
		data.putExtra(ConstantUtils.Intent.SUCCESS, true);
		setResult(0, data);
		OtherUtils.resetStepStatus(Step2Activity.this);// 退出界面重置原状态
		ActivityManager.getInstance().closeActivity(context);
	}

	private int mErrorCount;
	public void submitDataError(){
		mErrorCount++;
		if(mErrorCount > 1){
			showDialogCache();
		}
	}

	public void showDialogCache(){
		NormalDialog dialog = new NormalDialog(context);
		dialog.content("是否数据缓存到本地,待网络正常后，自动缓存数据?").show();
		dialog.setOnBtnClickL(null, new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				saveCacheData();
			}
		});
	}


	//保存缓存数据
	public void saveCacheData(){
		ToastUtils.showLongCustomToast("保存数据到缓存中，并自动上传缓存数据");
		DStep2Bean bean = new DStep2Bean();
		bean.setUserId(SPUtils.getID());
		bean.setCompanyId(SPUtils.getCompanyId());
		bean.setCount(count2);
		bean.setCid(cid);
		bean.setKhNm(mKhNm);
		bean.setBfId(mId);
		bean.setPicList(mPicList);
		bean.setPicList2(mPicList2);
		bean.setPophb(mPophbStr);
		bean.setCq(mCqStr);
		bean.setWq(mWqStr);
		bean.setRemo1(mRemoStr1);
		bean.setRemo2(mRemoStr2);
		bean.setIsXy(mIsXy);
		bean.setTime(MyTimeUtils.getNowTime());
		MyDataUtils.getInstance().saveStep2(bean);

		Intent data = new Intent();
		data.putExtra(ConstantUtils.Intent.SUCCESS, true);
		data.putExtra(ConstantUtils.Intent.COUNT, 2);
		setResult(0, data);
		ActivityManager.getInstance().closeActivity(context);
	}

	//避免重复的token
	private String mQueryToken;
	public void doToken(String data){
		mQueryToken = data;
	}

}
