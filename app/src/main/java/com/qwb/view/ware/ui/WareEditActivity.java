package com.qwb.view.ware.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deadline.statebutton.StateButton;
import com.example.scanlibrary.ScanActivity;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PublicPicAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.common.model.PublicPicBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.ware.model.WareBean;
import com.qwb.view.step.model.WarePicBean;
import com.qwb.view.ware.parsent.PWareEdit;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：商品：添加，查看，修改
 * 修改描述：
 * 修改作者：
 * 修改时间：
 */
public class WareEditActivity extends XActivity<PWareEdit> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_ware_edit;
	}

	@Override
	public PWareEdit newP() {
		return new PWareEdit();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initUI();
		doIntent();
	}

	private String mWareId;//商品Id
	private int mType;//1:添加；2：查看 3.修改
	private void initIntent() {
		Intent intent = getIntent();
		if(intent != null){
			mWareId = intent.getStringExtra(ConstantUtils.Intent.ID);
			mType = intent.getIntExtra(ConstantUtils.Intent.TYPE, 1);
		}
	}

	private boolean mMenuUpdate = false;//菜单：修改
	private boolean mMenuLook = false;//菜单：查看全部
	private void doIntent(){
		if(2 == mType){
			//查看-处理
			mSbSubmit.setVisibility(View.GONE);
			mTvHeadTitle.setText("查看商品");
			mTvEditAll.setText("查看全部");
			getP().queryDataWare(context, mWareId);
			setViewEnabled(false);

			//获取菜单（修改，查看全部）权限
			String hasAdmin = SPUtils.getSValues(ConstantUtils.Sp.IS_UNITMNG);//是否管理员(管理员默认权限都有)
			if("1".equals(hasAdmin)){
				mMenuUpdate = true;
				mMenuLook = true;
			}else{
				mMenuUpdate = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.SPZQ_NEW_UPDATE);
				mMenuLook = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.SPZQ_NEW_CKQB);
			}
			if(mMenuLook){
				mLayoutLook.setVisibility(View.VISIBLE);
			}else{
				mLayoutLook.setVisibility(View.GONE);
			}
			if(mMenuUpdate){
				mHeadRight.setEnabled(true);
				mTvHeadRight.setText("修改");
				mTvHeadRight.setVisibility(View.VISIBLE);
			}else{
				mHeadRight.setEnabled(false);
				mTvHeadRight.setVisibility(View.GONE);
			}
		}else{
			mHeadRight.setEnabled(false);
			mTvHeadRight.setVisibility(View.GONE);
		}
	}

	@BindView(R.id.parent)
	View parent;
	private void initUI() {
		initHead();
		initAdapter();
		initOther();//商品基础信息
		initMaxUnit();//大单位
		initMinUnit();//小单位
	}

	//初始化头部
	@BindView(R.id.head_left)
	View mHeadLeft;
	@BindView(R.id.head_right)
	View mHeadRight;
	@BindView(R.id.head_right2)
	View mHeadRight2;
	@BindView(R.id.iv_head_right)
	ImageView mIvHeadRight;
	@BindView(R.id.iv_head_right2)
	ImageView mIvHeadRight2;
	@BindView(R.id.tv_head_title)
	TextView mTvHeadTitle;
	@BindView(R.id.tv_head_right)
	TextView mTvHeadRight;
	private void initHead() {
		MyStatusBarUtil.getInstance().setColorWhite(context);
		mHeadLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityManager.getInstance().closeActivity(context);
			}
		});
		mTvHeadTitle.setText("新建商品");

		//为“修改”
		mTvHeadRight.setText("修改");
		mHeadRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mTvHeadTitle.setText("修改商品");
				mSbSubmit.setText("确认修改");
				mTvEditAll.setText("修改全部");
				mSbSubmit.setVisibility(View.VISIBLE);
				setViewEnabled(true);
			}
		});

	}

	//---------------------------------------图片相关：start-------------------------------------------------------
	//适配器-图片
	@BindView(R.id.layout_add_pic)
	View mLayoutAddPic;
	@BindView(R.id.recyclerView_pic)
	RecyclerView mRecyclerViewPic;
	private PublicPicAdapter mPicAdapter;
	private List<String> mPicList = new ArrayList<>();//路径没有拼接file://(要上传的)
	private String delPicIds;//要删除的图片ids
	private void initAdapter() {
		mRecyclerViewPic.setHasFixedSize(true);
		mRecyclerViewPic.setLayoutManager(new GridLayoutManager(context,3));
		mPicAdapter = new PublicPicAdapter(context);
		mPicAdapter.openLoadAnimation();
		mRecyclerViewPic.setAdapter(mPicAdapter);
		mPicAdapter.setOnDeletePicListener(new PublicPicAdapter.OnDeletePicListener() {
			@Override
			public void onDeletePicListener(int position, PublicPicBean picBean) {
				if(picBean.getId() == 0){
					//拍照的
					mPicList.remove(picBean.getPic());
				}else{
					//后台获取的
					if(MyStringUtil.isEmpty(delPicIds)){
						delPicIds = "" + picBean.getId();
					}else{
						delPicIds = "," + picBean.getId();
					}
				}
				refreshAdapter(null, position);
			}
		});
		//添加图片
		mLayoutAddPic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mPicAdapter.getData().size() >= 6) {
					ToastUtils.showCustomToast("最多只能选6张图片！");
					return;
				}
				showActionSheetDialog();
			}
		});
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
	// 相册
	public void doAlbum() {
		ImagePicker.getInstance().setSelectLimit(Constans.maxImgCount - mPicAdapter.getData().size());
		ImagePicker.getInstance().setCrop(false);
		ImagePicker.getInstance().setMultiMode(true);
		Intent intent = new Intent(context, ImageGridActivity.class);
		startActivityForResult(intent, Constans.TAKE_PIC_XC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (data != null && resultCode == ImagePicker.RESULT_CODE_ITEMS) {
				ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
				if (images != null){
					List<PublicPicBean> picList = new ArrayList<>();
					for (ImageItem imageItem:images) {
						PublicPicBean picBean = new PublicPicBean();
						picBean.setPic("file://" + imageItem.path);
						mPicList.add(imageItem.path);//上传图片的
						picList.add(picBean);//图片显示
						Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
					}
					refreshAdapter(picList, null);
				}
			}

			//扫描二维码/条码回传
			if (RESULT_OK == resultCode && requestCode == ConstantUtils.Intent.REQUEST_CODE_SCAN) {
				if (data != null) {
					String result = data.getStringExtra(ScanActivity.SCAN_RESULT);
					if (!MyStringUtil.isEmpty(result)) {
						if(isMaxScan){
							mEtMaxBarCode.setText(result);
						}else{
							mEtMinBarCode.setText(result);
						}
					} else {
						ToastUtils.showCustomToast("扫描内容为空");
					}
				}
			}
		}catch (Exception e){
		}
	}

	/**
	 * 刷新适配器（isDelete：是否删除状态，movePosition：删除的下标）
	 */
	private void refreshAdapter(List<PublicPicBean> picList, Integer movePosition) {
		if(null != picList){
			List<PublicPicBean> datas = mPicAdapter.getData();
			if(datas !=null && datas.isEmpty()){
				mPicAdapter.setNewData(picList);
			}else{
				mPicAdapter.addData(picList);
			}
		}
		if(null != movePosition){
			mPicAdapter.remove(movePosition);
		}
		mPicAdapter.notifyDataSetChanged();
	}

	private void showActionSheetDialog() {
		String[] items = {"拍照", "相册"};
		final ActionSheetDialog dialog = new ActionSheetDialog(context, items, parent);
		dialog.isTitleShow(false).show();

		dialog.setOnOperItemClickL(new OnOperItemClickL() {
			@Override
			public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
				dialog.dismiss();
				if(0 == position){
					doCamera();
				}else{
					doAlbum();
				}

			}
		});
	}

	//---------------------------------------图片相关：end-------------------------------------------------------


	/**
	 * 商品基本信息
	 */
	@BindView(R.id.layout_look)
	View mLayoutLook;
	@BindView(R.id.et_wareNm)
	EditText mEtWareNm;
	@BindView(R.id.tv_wareType)
	TextView mTvWareType;
	@BindView(R.id.layout_wareType)
	View mLayoutWareType;
	@BindView(R.id.tv_edit_all)
	TextView mTvEditAll;
	@BindView(R.id.layout_hide)
	View mLayoutHide;
	@BindView(R.id.sb_submit)
	StateButton mSbSubmit;
	private void initOther(){
		//商品分类
		mLayoutWareType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(null == mTreeDatas || mTreeDatas.isEmpty()){
					getP().queryDataWareTypes(context);
				}else{
					showDialogTree();
				}
			}
		});
		//编辑全部
		mTvEditAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mLayoutHide.getVisibility() == View.VISIBLE){
					mLayoutHide.setVisibility(View.GONE);
				}else{
					mLayoutHide.setVisibility(View.VISIBLE);
				}
			}
		});
		//添加或修改
		mSbSubmit.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				saveData();
			}
		});
	}

	/**
	 * 大单位
	 */
	@BindView(R.id.et_max_wareDw)
	EditText mEtMaxWareDw;
	@BindView(R.id.et_max_wareGg)
	EditText mEtMaxWareGg;
	@BindView(R.id.et_max_wareDj)
	EditText mEtMaxWareDj;
	@BindView(R.id.et_max_barCode)
	EditText mEtMaxBarCode;
	@BindView(R.id.et_max_inPrice)
	EditText mEtMaxInPrice;
	@BindView(R.id.layout_max_scan)
	View mLayoutMaxScan;
	@BindView(R.id.et_bUnit)
	EditText mEtBUnit;
	@BindView(R.id.view_sale_price)
	View mViewSalePrice;
	@BindView(R.id.view_in_price)
	View mViewInPrice;
	private boolean isMaxScan = true;//扫描（大单位）
	private void initMaxUnit(){
		mLayoutMaxScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isMaxScan = true;
				ActivityManager.getInstance().jumpScanActivity(context, false);//扫描
			}
		});
	}

	/**
	 * 小单位
	 */
	@BindView(R.id.et_min_wareGg)
	EditText mEtMinWareGg;
	@BindView(R.id.et_min_wareDw)
	EditText mEtMinWareDw;
	@BindView(R.id.et_min_wareDj)
	EditText mEtMinWareDj;
	@BindView(R.id.et_min_barCode)
	EditText mEtMinBarCode;
	@BindView(R.id.et_min_inPrice)
	EditText mEtMinInPrice;
	@BindView(R.id.layout_min_scan)
	View mLayoutMinScan;
	@BindView(R.id.et_sUnit)
	EditText mEtSUnit;
	private void initMinUnit(){
		mLayoutMinScan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				isMaxScan = false;
				ActivityManager.getInstance().jumpScanActivity(context, false);//扫描
			}
		});
	}

	/**
	 * 商品分类树(单选)
	 */
	private List<TreeBean> mTreeDatas = new ArrayList<>();
	private MyTreeDialog mTreeDialog;
	private String mWareTypeId = "-1";//商品分类id(默认‘未分类’)
	private void showDialogTree(){
		if(null == mTreeDialog){
			mTreeDialog = new MyTreeDialog(context, mTreeDatas, false);
		}
		mTreeDialog.title("选择商品分类").show();
		mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
			@Override
			public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
				mWareTypeId = clientTypeIds;
				for (TreeBean bean : mTreeDatas) {
					if(clientTypeIds.equals("" + (bean.get_id()))){
						mTvWareType.setText(bean.getName());
						break;
					}
				}
			}
		});
	}

	public void refreshAdapterTree(List<TreeBean> mDatas){
		this.mTreeDatas = mDatas;
		showDialogTree();
	}

	/**
	 * 查询商品信息
	 * 显示数据
	 */
	public void showData(WareBean bean){
		try{
			mWareId = "" + bean.getWareId();
			mWareTypeId = "" + bean.getWaretype();
			mEtWareNm.setText(bean.getWareNm());
			mTvWareType.setText(bean.getWaretypeNm());

			//是否显示：销售价，采购价
			boolean showSalePrice = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.SPZQ_SHOW_SALE_PRICE);
			boolean showInPrice = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.SPZQ_SHOW_IN_PRICE);
			if(showSalePrice){
				mViewSalePrice.setVisibility(View.VISIBLE);
			}else{
				mViewSalePrice.setVisibility(View.GONE);
			}
			if(showInPrice){
				mViewInPrice.setVisibility(View.VISIBLE);
			}else{
				mViewInPrice.setVisibility(View.GONE);
			}

			//大的
			mEtMaxWareDw.setText(bean.getWareDw());
			mEtMaxWareGg.setText(bean.getWareGg());
			mEtMaxBarCode.setText(bean.getPackBarCode());
			mEtBUnit.setText("1");
			if(MyDoubleUtils.moreThanZero(bean.getWareDj())){
				mEtMaxWareDj.setText(bean.getWareDj() + "");
			}else{
				mEtMaxWareDj.setText("0");
			}
			if(MyDoubleUtils.moreThanZero(bean.getInPrice())){
				mEtMaxInPrice.setText(bean.getInPrice() + "");
			}else{
				mEtMaxInPrice.setText("");
			}

			//小的
			mEtMinWareDw.setText(bean.getMinUnit());
			mEtMinWareGg.setText(bean.getMinWareGg());
			mEtMinBarCode.setText(bean.getBeBarCode());
			//换算比例是1可修改
			mEtSUnit.setText(bean.getHsNum() + "");
			if(1.0 == bean.getHsNum()){
				mEtSUnit.setEnabled(true);
			}else{
				mEtSUnit.setEnabled(false);
			}
			if(MyDoubleUtils.moreThanZero(bean.getSunitPrice())){
				mEtMinWareDj.setText(bean.getSunitPrice() + "");
			}else{
				mEtMinWareDj.setText("");
			}
			if(MyDoubleUtils.moreThanZero(bean.getMinInPrice())){
				mEtMinInPrice.setText(bean.getMinInPrice() + "");
			}else{
				mEtMinInPrice.setText("");
			}

			// 图片
			List<PublicPicBean> picList = new ArrayList<>();
			List<WarePicBean> list = bean.getWarePicList();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					WarePicBean warePic = list.get(i);
					PublicPicBean picBean = new PublicPicBean();
					picBean.setId(warePic.getId());
					picBean.setWareId(warePic.getWareId());
					picBean.setPic(Constans.IMGROOTHOST + warePic.getPic());
					picBean.setPicMini(Constans.IMGROOTHOST + warePic.getPicMini());
					picBean.setType(warePic.getType());
					picList.add(picBean);//图片显示
				}
			}
			refreshAdapter(picList, null);
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}




	/**
	 * 保存数据
	 */
	private String mStatus = "1";//启用状态 （1是；2否）
	private void saveData() {
		String wareNm = mEtWareNm.getText().toString().trim();
		String wareType = mTvWareType.getText().toString().trim();
		//大单位
		String maxWareDw = mEtMaxWareDw.getText().toString().trim();
		String maxWareGg = mEtMaxWareGg.getText().toString().trim();
		String maxWareDj = mEtMaxWareDj.getText().toString().trim();
		String maxBarCode = mEtMaxBarCode.getText().toString().trim();
		String maxInPrice = mEtMaxInPrice.getText().toString().trim();
		//小单位
		String minWareDw = mEtMinWareDw.getText().toString().trim();
		String minWareGg = mEtMinWareGg.getText().toString().trim();
		String minWareDj = mEtMinWareDj.getText().toString().trim();
		String minBarCode = mEtMinBarCode.getText().toString().trim();
		String minInPrice = mEtMinInPrice.getText().toString().trim();
		//比例
		String bUnit = mEtBUnit.getText().toString().trim();
		String sUnit = mEtSUnit.getText().toString().trim();
		String hsNum = "";
		if(!MyStringUtil.isEmpty(bUnit) && !MyStringUtil.isEmpty(sUnit)){
			hsNum = "" + (Double.valueOf(sUnit) / Double.valueOf(bUnit));
		}
		if(MyStringUtil.isEmpty(wareNm)){
			ToastUtils.showCustomToast("请输入商品名称");
			return;
		}
		if(MyStringUtil.isEmpty(maxWareDw)){
			ToastUtils.showCustomToast("请输入大单位名称");
			return;
		}
		if(MyStringUtil.isEmpty(maxWareGg)){
			ToastUtils.showCustomToast("请输入大单位规格");
			return;
		}
		if(MyStringUtil.isEmpty(maxWareGg)){
			ToastUtils.showCustomToast("请输入大单位规格");
			return;
		}
		if("0".equals(sUnit) || "0.0".equals(sUnit)){
			ToastUtils.showCustomToast("小单位比例不能为0");
			return;
		}

		getP().addData(context, delPicIds, mPicList,
				mStatus, mWareTypeId, mWareId, wareNm,
				maxWareDw, maxWareGg, maxWareDj, maxInPrice, maxBarCode,
				bUnit, sUnit, hsNum,
				minWareDw, minWareGg, minWareDj, minInPrice, minBarCode
		);
	}


	/**
	 * 设置控件是否可以编辑
	 */
	private void setViewEnabled(boolean enabled){
		mEtWareNm.setEnabled(enabled);
		mEtMaxWareDw.setEnabled(enabled);
		mEtMaxWareGg.setEnabled(enabled);
		mEtMaxWareDj.setEnabled(enabled);
		mEtMaxBarCode.setEnabled(enabled);
		mEtMaxInPrice.setEnabled(enabled);
		mEtBUnit.setEnabled(enabled);
		mEtSUnit.setEnabled(enabled);
		mEtMinWareGg.setEnabled(enabled);
		mEtMinWareDw.setEnabled(enabled);
		mEtMinWareDj.setEnabled(enabled);
		mEtMinBarCode.setEnabled(enabled);
		mEtMinInPrice.setEnabled(enabled);

		mLayoutWareType.setEnabled(enabled);
		mLayoutMaxScan.setEnabled(enabled);
		mLayoutMinScan.setEnabled(enabled);
		mLayoutWareType.setEnabled(enabled);
		if(enabled){
			mLayoutAddPic.setVisibility(View.VISIBLE);
		}else{
			mLayoutAddPic.setVisibility(View.GONE);
		}
	}







}
