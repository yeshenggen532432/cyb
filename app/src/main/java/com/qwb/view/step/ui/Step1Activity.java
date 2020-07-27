package com.qwb.view.step.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
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
import com.qwb.view.step.parsent.PStep1;
import com.qwb.utils.Constans;
import com.qwb.utils.ILUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PicAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.db.DStep1Bean;
import com.qwb.utils.MyLongConnectionUtil;
import com.qwb.utils.MyMapUtil;
import com.qwb.utils.MyNetWorkUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.step.model.BfphotoBean;
import com.qwb.view.step.model.QueryBfqdpzBean;
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
 * 创建描述：拜访步骤1：拜访签到拍照
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class Step1Activity extends XActivity<PStep1>{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_step1;
	}

	@Override
	public PStep1 newP() {
		return new PStep1();
	}

	private ImageLoader imageLoder = ILUtil.getImageLoder();// 加载图片
	private DisplayImageOptions options = ILUtil.getOptionsSquere();

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
		doIntent();
		getP().queryToken(null);
	}

	/**
	 * 初始化Intent
	 */
	private String pdateStr;// 补拜访时间( 拜访计划)
	private String count1 = "0";
	private String mKhNm;
	private String cid;// 客户ID
	private String mId;// 拜访拍照签到id
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			cid = intent.getStringExtra(ConstantUtils.Intent.CLIENT_ID);
			mKhNm = intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
			count1 = intent.getStringExtra(ConstantUtils.Intent.STEP);
			pdateStr = intent.getStringExtra(ConstantUtils.Intent.SUPPLEMENT_TIME);// 补拜访时间
		}
	}

	/**
	 * 处理Intent
	 */
	private void doIntent() {
		mTvHeadTitle.setText(mKhNm);
		if ("1".equals(count1)) {// 已上传
			getP().queryData(context, cid,pdateStr);//获取上次提交信息
			mTvHeadRight.setText("修改");
		} else {
			mTvHeadRight.setText("提交");
			initLocation();
		}
	}

	//**************************************************地图********************************************************
	/**
	 * 初始化地图
	 */
	@BindView(R.id.bmapView)
	MapView mMapView;
	private BaiduMap mBaiduMap;
	private BitmapDescriptor bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
	private void initMap() {
		mBaiduMap = mMapView.getMap();
		mMapView.showZoomControls(false);// 没有缩放按钮
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);// 地图缩放初始化
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setMyLocationEnabled(true);

		mIvRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIvRefresh.setVisibility(View.GONE);
				mPbRefresh.setVisibility(View.VISIBLE);
				mTvAddress.setText("定位中...");
				initLocation();
			}
		});
	}

	@BindView(R.id.tv_location)
	TextView mTvAddress;
	@BindView(R.id.ckin_bnt_refadr)
	View mIvRefresh;
	@BindView(R.id.pb_refresh)
	View mPbRefresh;
	private String mLocationFrom;
	private String mLongitude;
	private String mLatitude;
	private String mAddressStr;
	private void initLocation(){
		MyMapUtil.getInstance()
				.getLocationClient(context)
				.setOnLocationListener(new MyMapUtil.OnLocationListener() {
					@Override
					public void setOnSuccessListener(BDLocation bdLocation) {
						mBaiduMap.clear();// 要清空，不然图标会重复
						LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
						MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
						mBaiduMap.setMapStatus(u);// 中心点
						MarkerOptions markerOptions = new MarkerOptions().position(ll).icon(bdA).zIndex(9).draggable(true);
						mBaiduMap.addOverlay(markerOptions);// 标记物

						mIvRefresh.setVisibility(View.VISIBLE);
						mPbRefresh.setVisibility(View.GONE);
						mTvAddress.setText(bdLocation.getAddrStr());// 地址

						mLongitude = "" + bdLocation.getLongitude();
						mLatitude = "" + bdLocation.getLatitude();
						mAddressStr = ""+ bdLocation.getAddrStr();
						if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
							mLocationFrom = "gps   " + MyUtils.getAppVersion();
						}
						if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
							mLocationFrom = "wifi   " + MyUtils.getAppVersion();
						}
						if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
							mLocationFrom = "lx   " + MyUtils.getAppVersion();
						}
					}

					@Override
					public void setAddressListener(String addressStr) {
						mAddressStr = addressStr;
						mTvAddress.setText(addressStr);
					}

					@Override
					public void setErrorListener() {
						mIvRefresh.setVisibility(View.VISIBLE);
						mPbRefresh.setVisibility(View.GONE);
						mTvAddress.setText("定位失败");
					}

				});
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		initMap();
		initHead();
		initBaseView();
		initAdapter();
	}

	/**
	 * 头部
	 */
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead(){
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
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
	/**
	 * 其他UI
	 */
	@BindView(R.id.edit_hbzt)
	EditText mEtHbzt;
	@BindView(R.id.edit_ggyy)
	EditText mEtGgyy;
	@BindView(R.id.edit_remo)
	EditText mEtRemo;
	@BindView(R.id.rb_wu)
	RadioButton mRbWu;
	@BindView(R.id.rb_you)
	RadioButton mRbYou;
	private void initBaseView() {
	}

	//适配器-图片
	@BindView(R.id.iv_add_pic)
	ImageView mIvAddPic;
	@BindView(R.id.iv_del_pic)
	ImageView mIvDelPic;
	@BindView(R.id.recyclerview_pic)
	RecyclerView mRecyclerViewPic;
	private PicAdapter mPicAdapter;
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
					mPicList.add(imageItem.path);//上传图片的
					picList.add("file://"+imageItem.path);//图片显示
					Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
				}
				refreshAdapter(false, picList, null );
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mMapView != null) {
			mMapView.onDestroy();
			mMapView = null;
			bdA.recycle();// 回收 bitmap 资源
		}
		// 退出界面重置原状态
//		OtherUtils.resetStepStatus(Step1Activity.this);
	}



//***********************************************************接口***************************************************************
	// 上下班添加轨迹点
	private void sendLocation() {
		// 状态--1:上班，2：下班，3：拜访签到，4：拜访签退
		MyLongConnectionUtil.getInstance().addLocation(mLatitude, mLongitude, mAddressStr, mLocationFrom, 3);
	}

	//提交数据（添加或修改）
	private String mIsXy;
	private String mHbztStr;
	private String mGgyytStr;
	private String mRemoStr;
	private void addData() {
		mHbztStr = mEtHbzt.getText().toString().trim();
		mGgyytStr = mEtGgyy.getText().toString().trim();
		mRemoStr = mEtRemo.getText().toString().trim();
		if (MyUtils.isEmptyString(mLongitude) || MyUtils.isEmptyString(mLatitude)) {
			ToastUtils.showCustomToast("拜访签到地址不能为空");
			return;
		}
		if (mRbYou.isChecked()) {// 是否显眼（1有，2无）
			mIsXy = "1";
		} else {
			mIsXy = "2";
		}
		if(!MyNetWorkUtil.isNetworkConnected()){
			ToastUtils.showCustomToast("网络不流通，请检查网络是否正常");
			showDialogCache();
			return;
		}
		getP().addData(context,count1, cid, mId, mLongitude, mLatitude,mAddressStr,mHbztStr,mGgyytStr,mRemoStr,mIsXy, mPicList,mQueryToken);
	}


	/**
	 * 查询拜访签到拍照
	 */
	public void showInfo( QueryBfqdpzBean data){
		mId = data.getId();// 拜访拍照签到id
		mLatitude = data.getLatitude();
		mLongitude = data.getLongitude();
		mAddressStr = data.getAddress();
		mTvAddress.setText(mAddressStr);
		mEtGgyy.setText("" + data.getGgyy());
		mEtHbzt.setText("" + data.getHbzt());
		mEtRemo.setText("" + data.getRemo());
		if ("1".equals(data.getIsXy())) {
			mRbYou.setChecked(true);
			mRbWu.setChecked(false);
		} else {
			mRbYou.setChecked(false);
			mRbWu.setChecked(true);
		}

		if (!MyStringUtil.isEmpty(mLatitude) && !MyStringUtil.isEmpty(mLongitude)) {// 经纬度不为空
			LatLng latLng = new LatLng(Double.valueOf(mLatitude), Double.valueOf(mLongitude));
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.setMapStatus(u);// 中心点
			MarkerOptions markerOptions = new MarkerOptions().position(latLng).icon(bdA).zIndex(9).draggable(true);
			mBaiduMap.addOverlay(markerOptions);// 标记物
		}

		// 图片
		List<String> picList = new ArrayList<>();
		List<BfphotoBean> list = data.getList();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				BfphotoBean qdPhoto = list.get(i);
				Constans.publish_pics1.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + qdPhoto.getPic()));
				Constans.linDownloader.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + qdPhoto.getPic()));
				picList.add(MyUrlUtil.getUrl(Constans.IMGROOTHOST + qdPhoto.getPic()));//图片显示
			}
		}
		refreshAdapter(false, picList, null);
		saveImg(picList);
	}

	//下载图片并保存文件
	private void saveImg(final List<String> list) {
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
						Constans.publish_pics1111.add(file.getAbsolutePath());
						//j的作用：避免异步下载图片保存的位置错乱（异步下载图片，可以第一个先下载但第二个先下载完）
						mPicList.add(j, file.getAbsolutePath());//上传图片的
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	//提交数据成功-操作
	public int count = 1;
	public void submitDataSuccess(){
		sendLocation();// 上下班添加轨迹点
		Intent data = new Intent();
		data.putExtra(ConstantUtils.Intent.SUCCESS, true);
		setResult(0, data);
		// 退出界面重置原状态
		OtherUtils.resetStepStatus(Step1Activity.this);
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
		DStep1Bean bean = new DStep1Bean();
		bean.setUserId(SPUtils.getID());
		bean.setCompanyId(SPUtils.getCompanyId());
		bean.setCount(count1);
		bean.setCid(cid);
		bean.setKhNm(mKhNm);
		bean.setLatitude(mLatitude);
		bean.setLongitude(mLongitude);
		bean.setAddress(mAddressStr);
		bean.setpId(mId);
		bean.setPicList(mPicList);
		bean.setHbzt(mHbztStr);
		bean.setGgyy(mGgyytStr);
		bean.setIsXy(mIsXy);
		bean.setRemo(mRemoStr);
		bean.setTime(MyTimeUtils.getNowTime());
		MyDataUtils.getInstance().saveStep1(bean);

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
