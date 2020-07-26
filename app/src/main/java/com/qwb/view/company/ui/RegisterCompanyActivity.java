package com.qwb.view.company.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.common.adapter.PublicPicAdapter;
import com.qwb.listener.OnNoMoreClickListener;
import com.qwb.view.common.model.PublicPicBean;
import com.qwb.view.base.model.TreeBean;
import com.qwb.view.company.model.CompanyIndustryBean;
import com.qwb.view.company.model.CompanyIndustryCategoryBean;
import com.qwb.view.company.model.CompanyInfoBean;
import com.qwb.view.company.model.CompanySalesmanBean;
import com.qwb.view.company.parsent.PRegisterCompany;
import com.qwb.utils.MyStatusBarUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTimeCount;
import com.qwb.widget.treedialog.MyTreeDialog;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import io.reactivex.functions.Consumer;

/**
 *  创建公司
 */
public class RegisterCompanyActivity extends XActivity<PRegisterCompany> {

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_register_company;
	}

	@Override
	public PRegisterCompany newP() {
		return new PRegisterCompany();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initUI();
		getP().queryCompanyIndustry(context);
		getP().querySalesman(context);
	}

	View parent;
	private void initUI() {
		initHead();
		initAdapter();
		initOther();
		initCode();
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
		mTvHeadTitle.setText("创建公司");
		mTvHeadRight.setText("提交");
		mHeadRight.setOnClickListener(new OnNoMoreClickListener() {
			@Override
			protected void OnMoreClick(View view) {
				addData();
			}
		});
	}

	/**
	 * 基本信息
	 */
	@BindView(R.id.et_company_name)
	EditText mEtCompany;
	@BindView(R.id.et_industry)
	TextView mEtIndustry;
	@BindView(R.id.et_category)
	TextView mEtCategory;
	@BindView(R.id.et_brand)
	EditText mEtBrand;
	@BindView(R.id.et_leader)
	EditText mEtLeader;
	@BindView(R.id.et_tel)
	EditText mEtTel;
	@BindView(R.id.et_email)
	EditText mEtEmail;
	@BindView(R.id.et_employee_count)
	EditText mEtEmployeeCount;
	@BindView(R.id.et_salesman_count)
	EditText mEtSalesmanCount;
	@BindView(R.id.et_bizLicense_number)
	EditText mEtBizlicenseNumber;
	@BindView(R.id.et_register_mobile)
	EditText mEtRegisterMobile;
	@BindView(R.id.et_code)
	EditText mEtCode;
	@BindView(R.id.et_salesman)
	TextView mEtSalesman;
	private void initOther(){
		mEtRegisterMobile.setText(SPUtils.getMobile());
		//所属行业
		mEtIndustry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showDialogIndustry();
			}
		});
		//行业分类
		mEtCategory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(MyStringUtil.isEmpty(mIndustryId)){
					ToastUtils.showCustomToast("先选择所属行业");
					return;
				}
				getP().queryCompanyCategory(context, mIndustryId, true);
			}
		});
		//平台业务员
		mEtSalesman.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mSalesmanList == null || mSalesmanList.isEmpty()){
					getP().querySalesman(context);
				}else {
					showDialogSalesman(mSalesmanList);
				}
			}
		});
	}

	@BindView(R.id.tv_code)
	TextView mTvCode;
	private MyTimeCount time;
	public void initCode(){
		time = new MyTimeCount(60000, 1000, mTvCode);
		mTvCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendCode();
			}
		});
	}

	/**
	 * 显示数据
	 */
	private String mIndustryId;
	private String mCategoryId;
	public void showData(CompanyInfoBean.CompanyInfo bean){
		try{
			mEtCompany.setText(bean.getName());
			mEtBrand.setText(bean.getBrand());
			mEtLeader.setText(bean.getLeader());
			mEtTel.setText(bean.getTel());
			mEtEmail.setText(bean.getEmail());
			mEtEmployeeCount.setText("" + bean.getEmployeeCount());
			mEtSalesmanCount.setText("" + bean.getSalesmanCount());
			mEtBizlicenseNumber.setText(bean.getBizLicenseNumber());

			mIndustryId = bean.getIndustryId();
			mCategoryId = bean.getCategoryId();
			getP().queryCompanyIndustry(context);
			if(!MyStringUtil.isEmpty(mIndustryId)){
				getP().queryCompanyCategory(context, mIndustryId, false);
			}

			// 图片
			List<PublicPicBean> picList = new ArrayList<>();
			String bizLicensePic = bean.getBizLicensePic();
			if(!MyStringUtil.isEmpty(bizLicensePic)){
				PublicPicBean picBean = new PublicPicBean();
				picBean.setPic(Constans.IMGROOTHOST + bizLicensePic);
				picBean.setPicMini(Constans.IMGROOTHOST + bizLicensePic);
				picList.add(picBean);//图片显示
			}
			refreshAdapter(picList, null);
		}catch (Exception e){
			ToastUtils.showError(e);
		}
	}

	/**
	 * 所属行业
	 */
	List<CompanyIndustryBean.CompanyIndustry> mIndustryList = new ArrayList<>();
	public void refreshAdapterIndustry(List<CompanyIndustryBean.CompanyIndustry> list){
		if(list == null){
			return;
		}
		mIndustryList.clear();
		mIndustryList.addAll(list);
		if(!MyStringUtil.isEmpty(mIndustryId)){
			for (CompanyIndustryBean.CompanyIndustry bean : list) {
				if(mIndustryId.equals(bean.getId())){
					mEtIndustry.setText(bean.getName());
					return;
				}
			}
		}
	}
	/**
	 * 所属行业
	 */
	List<CompanyIndustryCategoryBean.CompanyCategroy> mCategoryList = new ArrayList<>();
	public void refreshAdapterCategory(List<CompanyIndustryCategoryBean.CompanyCategroy> list, boolean show){
		if(list == null){
			return;
		}
		mCategoryList.clear();
		mCategoryList.addAll(list);
		if(show){
			showDialogCategory();
		}else{
			if(!MyStringUtil.isEmpty(mCategoryId)){
				for (CompanyIndustryCategoryBean.CompanyCategroy bean : list) {
					if(mCategoryId.equals(bean.getId())){
						mEtCategory.setText(bean.getName());
						return;
					}
				}
			}
		}
	}


	/**
	 * 所属行业
	 */
	private void showDialogIndustry(){
		if(mIndustryList != null && mIndustryList.size() > 0){
			final ArrayList<DialogMenuItem> items = new ArrayList<>();
			for (CompanyIndustryBean.CompanyIndustry bean: mIndustryList) {
				items.add(new DialogMenuItem(bean.getName(), 0));
			}
			NormalListDialog dialog = new NormalListDialog(context, items);
			dialog.title("所属行业").show();
			dialog.setOnOperItemClickL(new OnOperItemClickL() {
				@Override
				public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
					DialogMenuItem item = items.get(position);
					String name = item.mOperName;
					for (CompanyIndustryBean.CompanyIndustry bean: mIndustryList) {
						if(name.equals(bean.getName())){
							if(!bean.getId().equals(mIndustryId)){
								mIndustryId = bean.getId();
								mEtIndustry.setText(bean.getName());
								mCategoryId = "";
								mEtCategory.setText("");
							}
							return;
						}
					}
				}
			});
		}else{
			ToastUtils.showCustomToast("没有数据");
		}
	}
	/**
	 * 所属行业分类
	 */
	private void showDialogCategory(){
		if(mCategoryList != null && mCategoryList.size() > 0){
			final ArrayList<DialogMenuItem> items = new ArrayList<>();
			for (CompanyIndustryCategoryBean.CompanyCategroy bean: mCategoryList) {
				items.add(new DialogMenuItem(bean.getName(), 0));
			}
			NormalListDialog dialog = new NormalListDialog(context, items);
			dialog.title("所属行业分类").show();
			dialog.setOnOperItemClickL(new OnOperItemClickL() {
				@Override
				public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
					DialogMenuItem item = items.get(position);
					String name = item.mOperName;
					for (CompanyIndustryCategoryBean.CompanyCategroy bean: mCategoryList) {
						if(name.equals(bean.getName())){
							if(!bean.getId().equals(mCategoryId)){
								mCategoryId = bean.getId();
								mEtCategory.setText(bean.getName());
							}
							return;
						}
					}
				}
			});
		}else{
			ToastUtils.showCustomToast("没有数据");
		}
	}

	/**
	 * 处理默认平台业务员“系统小白”
	 */
	public void doSalesman(List<CompanySalesmanBean.CompanySalesman> salesmanList){
		if(salesmanList != null && salesmanList.size() > 0){
			mSalesmanList.clear();
			mSalesmanList.addAll(salesmanList);
			for (CompanySalesmanBean.CompanySalesman bean: salesmanList) {
				if(bean.getName().equals("系统小白")){
					mEtSalesman.setText("系统小白");
					mSalesmanId = "" + bean.getId();
					mSalesman = "系统小白";
					return;
				}
			}
		}
	}

	/**
	 * 平台业务员
	 */
	private String mSalesman, mSalesmanId;
	private List<CompanySalesmanBean.CompanySalesman> mSalesmanList = new ArrayList<>();
	public void showDialogSalesman(List<CompanySalesmanBean.CompanySalesman> salesmanList){
		if(mSalesmanList != null && mSalesmanList.size() == 0){
			mSalesmanList.clear();
		    mSalesmanList.addAll(salesmanList);
		}
		if(mSalesmanList != null && mSalesmanList.size() > 0){
			final ArrayList<DialogMenuItem> items = new ArrayList<>();
			for (CompanySalesmanBean.CompanySalesman bean: mSalesmanList) {
				items.add(new DialogMenuItem(bean.getName(), bean.getId()));
			}
			NormalListDialog dialog = new NormalListDialog(context, items);
			dialog.title("平台业务员").show();
			dialog.setOnOperItemClickL(new OnOperItemClickL() {
				@Override
				public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
					DialogMenuItem item = items.get(position);
					String name = item.mOperName;
					mEtSalesman.setText(name);
					mSalesmanId = "" + item.mResId;
					mSalesman = name;
				}
			});
		}else{
			ToastUtils.showCustomToast("没有数据");
		}
	}

	/**
	 * 分类树(单选)--行业分类
	 */
	public void refreshAdapterTree(List<TreeBean> mDatas){
		this.mTreeDatas = mDatas;
		showDialogTree();
	}

	/**
	 * 分类树(单选)--行业分类
	 */
	private List<TreeBean> mTreeDatas = new ArrayList<>();
	private MyTreeDialog mTreeDialog;
	private void showDialogTree(){
		if(null == mTreeDialog){
			mTreeDialog = new MyTreeDialog(context,mTreeDatas,false);
		}
		mTreeDialog.title("选择行业分类").show();
		mTreeDialog.setOnClickListener(new MyTreeDialog.OnClickListener() {
			@Override
			public void onOkListener(String checkIds, String clientTypeIds, Map<Integer, Integer> checkMap) {
				for (TreeBean bean : mTreeDatas) {
					if(clientTypeIds.equals("" + (bean.get_id()))){
						break;
					}
				}
			}
		});
	}


	/**
	 * 发送验证码
	 */
	private void sendCode() {
		String phone = mEtRegisterMobile.getText().toString().trim();
		if (TextUtils.isEmpty(phone) || !MyUtils.isMobileNum(phone)) {
			ToastUtils.showCustomToast( "手机号码格式不对");
			return;
		}
		time.start();
		getP().sendCode(context,phone);
	}

	/**
	 * 提交数据
	 */
	String mPicUrl;
	private void addData() {
		String companyName = mEtCompany.getText().toString().trim();
		String brand = mEtBrand.getText().toString().trim();
		String leader = mEtLeader.getText().toString().trim();
		String tel = mEtTel.getText().toString().trim();
		String email = mEtEmail.getText().toString().trim();
		String employeeCount = mEtEmployeeCount.getText().toString().trim();
		String salesmanCount = mEtSalesmanCount.getText().toString().trim();
		String bizliceseNumber = mEtBizlicenseNumber.getText().toString().trim();
		String code = mEtCode.getText().toString().trim();
		String mobile = mEtRegisterMobile.getText().toString().trim();
		if(MyStringUtil.isEmpty(companyName)){
			ToastUtils.showCustomToast("请输入公司名称");
			return;
		}
		if(MyStringUtil.isEmpty(mobile) || 11 != mobile.length()){
			ToastUtils.showCustomToast("注册手机号格式不正确");
			return;
		}
		if(MyStringUtil.isEmpty(code)){
			ToastUtils.showCustomToast("请输入验证码");
			return;
		}
		if(mPicList == null || mPicList.isEmpty()){
			mPicList = null;
		}
		getP().addCompanySave(context,companyName,mIndustryId,mCategoryId,brand,leader,tel,email,employeeCount,
				salesmanCount,bizliceseNumber,mPicUrl,code,mobile, mSalesman, mSalesmanId);
	}

	/**
	 * 添加成功
	 */
	public void addDataSuccess(){
		ActivityManager.getInstance().closeActivity(context);
	}
	/**
	 * 添加成功
	 */
	public void addDataPicSuccess(String picUrl){
		mPicUrl = picUrl;
	}

	//---------------------------------------图片相关：start-------------------------------------------------------
	//适配器-图片
	@BindView(R.id.layout_add_pic)
	View mLayoutAddPic;
	@BindView(R.id.recyclerView)
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
							ImagePicker imagePicker = ImagePicker.getInstance();
							imagePicker.setMultiMode(false);
							imagePicker.setCrop(false);
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
		ImagePicker imagePicker = ImagePicker.getInstance();
		imagePicker.setMultiMode(false);
		imagePicker.setCrop(false);
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
					mPicList.clear();
					Constans.publish_pics1111.clear();
					for (ImageItem imageItem:images) {
						PublicPicBean picBean = new PublicPicBean();
						picBean.setPic("file://" + imageItem.path);
						mPicList.add(imageItem.path);//上传图片的
						picList.add(picBean);//图片显示
						Constans.publish_pics1111.add(imageItem.path);//上传成功后删除手机图片
					}
					refreshAdapter(picList, null);
					getP().uploadPic(context, mPicList);
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
			mPicAdapter.setNewData(picList);
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
	//---------------------------------------图片相关：end-----------------------------------------










}
