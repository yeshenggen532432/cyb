package com.qwb.view.txl.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.qwb.application.MyApp;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.member.model.BuMenListBean;
import com.qwb.view.member.model.BuMenListBean.BuMenItem;
import com.qwb.view.member.model.BuMenListBean.MemberBean;
import com.qwb.view.member.model.BranchListBean;
import com.qwb.view.member.model.BranchListBean.BuMengItemBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyDialogManager;
import com.qwb.utils.MyDialogManager.OnCancle;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.MyPopWindowManager.OnImageBack;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class BuMenListActivity extends BaseNoTitleActivity implements OnClickListener {

	private String dataTp = MyLoginUtil.getDataTp(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TXL_NEW, ConstantUtils.Apply.TXL_OLD);
	private String dataTpMids = MyLoginUtil.getDataTpMids(ConstantUtils.IS_APPLY_NEW, ConstantUtils.Apply.TXL_NEW, ConstantUtils.Apply.TXL_OLD);

	private String bumemid, title;
	private List<BuMenItem> departls;
	private List<MemberBean> members;
	private ComputeHeightListView lv_pull;
	private ComputeHeightListView lv_mems;
	private BuMenAdapter myAdapter;
	private MemberAdapter mySecondAdapter;
	private PopupWindow mPopupWindow;
	private List<BuMengItemBean> buMengList = new ArrayList<BranchListBean.BuMengItemBean>();
	private HashMap<Integer, List<BuMengItemBean>> buMengLv = new HashMap<Integer, List<BuMengItemBean>>();

	/**
	 * 成员itembean
	 */
	private BuMenItem currentBuMenItem;

	/**
	 * 当前部门级数
	 */
	private int currentLv = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_bu_men_list);

		Intent intent = getIntent();
		if (intent != null) {
			bumemid = intent.getStringExtra("id");
			title = intent.getStringExtra("title");
		}
		creatPop();
		creatBuMengPop();
		initUI();

		initData_member();
	}

	private Dialog yaoqingDL;

	private void creatYaoQingDialog() {
		yaoqingDL = new Dialog(this, R.style.Translucent_NoTitle);
		yaoqingDL.setContentView(R.layout.x_dialog_creat_bumen);
		TextView tv_yaoqing = (TextView) yaoqingDL.findViewById(R.id.tv_biaoti);
		tv_yaoqing.setText("邀请人");
		final EditText et_name = (EditText) yaoqingDL.findViewById(R.id.et_name);
		et_name.setHint("请输入手机号码");
		et_name.setInputType(InputType.TYPE_CLASS_PHONE);
		yaoqingDL.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = et_name.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					ToastUtils.showCustomToast("号码不能为空");
				} else {
					yaoqingDL.dismiss();
					initData_yaoqin(name);
				}
			}
		});
		yaoqingDL.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				yaoqingDL.dismiss();
			}
		});
		yaoqingDL.show();
	}

	private void creatPop() {
		View view = getLayoutInflater().inflate(R.layout.x_popup_zuzhi, null);
		View tv_creat = view.findViewById(R.id.tv_creat);
		View tv_creat_line = view.findViewById(R.id.tv_creat_line);
		view.findViewById(R.id.tv_addcompany).setVisibility(View.GONE);
		view.findViewById(R.id.tv_add_line).setVisibility(View.GONE);

		tv_creat.setVisibility(View.GONE);
		tv_creat_line.setVisibility(View.GONE);
		tv_sao = view.findViewById(R.id.tv_sao);
		tv_bumen_line = view.findViewById(R.id.tv_bumen_line);
		View tv_yaoqing = view.findViewById(R.id.tv_yaoqing);
		if ("1".equals(SPUtils.getSValues("iscreat")) || "2".equals(SPUtils.getSValues("iscreat"))) {
			tv_yaoqing.setVisibility(View.VISIBLE);
		} else {
			tv_yaoqing.setVisibility(View.GONE);
		}
		tv_sao.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				if (dlg == null) {
					creatBuMenDialog();
				} else {
					dlg.show();
				}
			}
		});
		tv_yaoqing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				creatYaoQingDialog();
			}
		});

		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.MyPopAnimation3);
	}

	private Dialog dlg;
	private ImageView iv_top_right;

	private void creatBuMenDialog() {
		dlg = new Dialog(this, R.style.Translucent_NoTitle);
		dlg.setContentView(R.layout.x_dialog_creat_bumen);
		final EditText et_name = (EditText) dlg.findViewById(R.id.et_name);
		dlg.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = et_name.getText().toString().trim();
				if (TextUtils.isEmpty(name)) {
					ToastUtils.showCustomToast("名称不能为空！");

				} else {
					dlg.dismiss();
					initData_branch(name, bumemid);
				}
			}

		});
		dlg.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dlg.dismiss();
			}
		});
		dlg.show();
	}

	// 创建部门
	private void initData_branch(String name, String parentid) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("branchName", name);
		params.put("parentid", parentid);
		params.put("dataTp", dataTp);
		if ("4".equals(dataTp)) {
			params.put("mids", dataTpMids);// 角色
		}
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.addDepartURL)
				.id(2)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 邀请人
	private void initData_yaoqin(String tel) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("deptId", bumemid);
		params.put("tel", tel);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.yqworkmateURL)
				.id(3)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 获取部门或成员列表
	private void initData_member() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("dataTp", dataTp);
		if ("4".equals(dataTp)) {
			params.put("mids", dataTpMids);// 角色
		}
		String url=null;
		if (!MyUtils.isEmptyString(bumemid)) {
			params.put("parentid", bumemid);
			url=Constans.queryDepartlszOrcyURL;
		} else {
			url=Constans.queryDepartlsURL;
		}

		OkHttpUtils
				.post()
				.params(params)
				.url(url)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);


	}

	// 设置是否常用
	private void initData_changyong(String bindMemberId, String tp) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("token", SPUtils.getTK());
		params.put("memberId", bindMemberId);
		params.put("cy", tp);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.setcyURL)
				.id(5)
				.build()
				.execute(new MyStringCallback(),null);
	}

	// deptId为空时 第一级部门
	private String currentDeptId;

	private void initData_bumen(String deptId) {
		currentDeptId = deptId;
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("deptId", deptId);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.deptListURL)
				.id(8)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 重命名部门名称
	private void initData_cmmBumen(String name, int id) {
		currentName = name;
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("deptId", String.valueOf(id));
		params.put("deptNm", name);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.updateDeptNmURL)
				.id(7)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 删除成员
	private void initData_delMember() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("memId", String.valueOf(currentMemberInforBean.getMemberId()));
		params.put("deptId", String.valueOf(deptid));
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.moveDeptURL)
				.id(10)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 将成员从公司移除
	private void removeMemberFormCompany(int memId) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("memId", String.valueOf(memId));
		params.put("dataTp", dataTp);
		if ("4".equals(dataTp)) {
			params.put("mids", dataTpMids);// 角色
		}
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.delMemURL)
				.id(9)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 删除部门
	private void initData_delBuMen(int id) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("deptId", String.valueOf(id));
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.delDeptURL)
				.id(6)
				.build()
				.execute(new MyStringCallback(),this);
	}

	private void initUI() {
		findViewById(R.id.comm_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView comm_title = (TextView) findViewById(R.id.tv_head_title);
		comm_title.setText(title);
		iv_top_right = (ImageView) findViewById(R.id.iv_head_right);
		if (MyUtils.isCreater()) {
			iv_top_right.setVisibility(View.VISIBLE);
			iv_top_right.setImageResource(R.drawable.add_caozuo);
			iv_top_right.setOnClickListener(this);
		} else {
			iv_top_right.setVisibility(View.INVISIBLE);
		}

		lv_pull = (ComputeHeightListView) findViewById(R.id.lv_pull);
		lv_mems = (ComputeHeightListView) findViewById(R.id.lv_mems);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comm_back:
			finish();
			break;
		case R.id.iv_head_right:
			if ("1".equals(SPUtils.getSValues("iscreat")) || "2".equals(SPUtils.getSValues("iscreat"))) {
				tv_sao.setVisibility(View.VISIBLE);
				tv_bumen_line.setVisibility(View.VISIBLE);
			} else {
				tv_sao.setVisibility(View.GONE);
				tv_bumen_line.setVisibility(View.GONE);
			}
			mPopupWindow.showAsDropDown(iv_top_right, 0, -20);
			break;
		}
	}

	class MemberAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return members.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.x_fragment_friends_items2, null);
			}
			final MemberBean memberBean = members.get(position);
			TextView tv_name = MyUtils.getViewFromVH(convertView, R.id.tv_title);
			tv_name.setTag(position);
			tv_name.setOnClickListener(myChatOnClickListener);
			tv_name.setText(memberBean.getMemberNm());
			tv_name.setOnLongClickListener(myLongClickListener);
			View iv_tel = MyUtils.getViewFromVH(convertView, R.id.iv_tel);
			if (SPUtils.getSValues("memId").equals(String.valueOf(memberBean.getMemberId()))) {
				iv_tel.setVisibility(View.INVISIBLE);
			} else {
				iv_tel.setVisibility(View.VISIBLE);
				iv_tel.setTag(position);
				iv_tel.setOnClickListener(mySetChangyongClickListener);
			}
			CircleImageView iv_userhead = MyUtils.getViewFromVH(convertView, R.id.iv_userhead);
			iv_userhead.setTag(memberBean.getMemberId());
			iv_userhead.setOnClickListener(myIMOnClickListener);
			MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberBean.getMemberHead(), iv_userhead);
			return convertView;
		}

	}

	private int currentPosition;
	private OnClickListener mySetChangyongClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			currentPosition = (Integer) v.getTag();
			MemberBean memberBean = members.get(currentPosition);
			if (1 == memberBean.getCy()) {
				MyPopWindowManager.getI().show(BuMenListActivity.this, imageback, "拨打 " + memberBean.getMemberMobile(),
						"取消常用");
			} else {
				MyPopWindowManager.getI().show(BuMenListActivity.this, imageback, "拨打 " + memberBean.getMemberMobile(),
						"设为常用");
			}
			// if (1 == memberBean.getCy()) {
			// MyPopWindowManager.getI().show(BuMenListActivity.this, imageback,
			// "拨打", "取消常用");
			// }else{
			// MyPopWindowManager.getI().show(BuMenListActivity.this, imageback,
			// "拨打", "设为常用");
			// }
		}
	};

	private OnImageBack imageback = new OnImageBack() {

		@Override
		public void fromPhotoAlbum() {
			// 设为/取消 常用
			if (1 == members.get(currentPosition).getCy()) {
				initData_changyong(String.valueOf(members.get(currentPosition).getMemberId()), "0");
			} else {
				initData_changyong(String.valueOf(members.get(currentPosition).getMemberId()), "1");
			}
		}

		@Override
		public void fromCamera() {
			// 拨打电话
			MyUtils.call(BuMenListActivity.this, members.get(currentPosition).getMemberMobile());
		}
	};

	// 点击名字进入聊天事件
	private OnClickListener myChatOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			MemberBean memberBean = members.get(position);
			if (SPUtils.getSValues("memId").equals(String.valueOf(memberBean.getMemberId()))) {
			} else {

			}
		}
	};

	/**
	 * 长按用户名 移动部门或移除成员操作
	 */
	private OnLongClickListener myLongClickListener = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			int position = (Integer) v.getTag();
			// 被操作的对象所属角色 iscreat 当前用户在公司所属角色
			String role = members.get(position).getRole();

			if ("1".equals(SPUtils.getSValues("iscreat"))) {
				// 如果是创建者 可以有任何操作
				showMemberMoreDialog(v);
			} else if ("2".equals(SPUtils.getSValues("iscreat"))) {
				// 当前角色是2 只能操作普通成员 也不能操作自己
				if ("1".equals(role) || "2".equals(role)) {
					ToastUtils.showCustomToast("您无操作权限！");
				} else {
					showMemberMoreDialog(v);
				}
			} else {
				ToastUtils.showCustomToast("您无操作权限！");
			}
			return true;
		}

	};

	// @Override
	// protected void onResume(){
	// super.onResume();
	// getMemberList();
	// }

	private PopupWindow typePopupWindow;
	private ListView lv_types;
	private View tv_backlv;
	// 部门弹窗列表

	private void creatBuMengPop() {
		View view = getLayoutInflater().inflate(R.layout.x_dialog_zhishiku_types, null);
		tv_backlv = view.findViewById(R.id.tv_backlv);
		TextView tv_bumenname = (TextView) view.findViewById(R.id.tv_bumenname);
		tv_bumenname.setText("选择部门");
		tv_backlv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 在容器中移除当前级别的部门列表
				deptid = "";
				buMengLv.remove(currentLv);
				// 位置减去1 显示上一级的数据
				currentLv--;
				List<BuMengItemBean> list = buMengLv.get(currentLv);
				buMengList.clear();
				buMengList.addAll(list);
				refreshMoveAdapter();
				if (currentLv <= 1) {
					tv_backlv.setVisibility(View.INVISIBLE);
				} else {
					tv_backlv.setVisibility(View.VISIBLE);
				}
			}
		});
		view.findViewById(R.id.bt_sure).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyUtils.isEmptyString(deptid)) {
					ToastUtils.showCustomToast("请选择部门！");
				} else if (title.equals(currentBumengName)) {

					ToastUtils.showCustomToast("该用户已在这个部门！");
				} else if ("公司本级".equals(currentBumengName) && SPUtils.getSValues("datasource").equals(title)) {
					ToastUtils.showCustomToast("该用户已在公司本级！");
				} else {
					initData_delMember();
					currentLv = 0;
					typePopupWindow.dismiss();
				}
			}

		});
		view.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				typePopupWindow.dismiss();
			}
		});
		lv_types = (ListView) view.findViewById(R.id.lv_types);
		typePopupWindow = new PopupWindow(view, (int) (MyApp.getScreenWidth() * 0.7),
				(int) (MyApp.getScreenHeight() * 0.6), true);
		typePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		typePopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				currentLv = 0;
				deptid = "";
			}
		});

	}

	private MemberBean currentMemberInforBean;
	/**
	 * 勾选将要移动到的部门
	 */
	private String currentBumengName;
	private String deptid;
	// 点击头像进入主页事件
	private OnClickListener myIMOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};

	class BuMenAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return departls.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.x_fragment_zuzhi_item, null);
			}

			final BuMenItem buMenItem = departls.get(position);

			TextView tv_bumen = MyUtils.getViewFromVH(convertView, R.id.tv_bumen);
			View iv_more = MyUtils.getViewFromVH(convertView, R.id.iv_more);
			iv_more.setTag(buMenItem);
			iv_more.setOnClickListener(myOnClickListener);// 查看成员
			iv_more.setVisibility(View.VISIBLE);
			tv_bumen.setText(buMenItem.getBranchName() + "(" + buMenItem.getNum() + "人)");
			// 长按--重命名，删除部门
			convertView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					if (MyUtils.isCreater()) {
						showMoreDialog(buMenItem);
					} else {
						ToastUtils.showCustomToast("您无操作权限！");
					}
					return true;
				}
			});
			return convertView;
		}
	}

	/**
	 * 更多操作窗口
	 */
	private Dialog moreDialog;

	private void showMoreDialog(final BuMenItem buMenItem) {
		moreDialog = new Dialog(this, R.style.Translucent_NoTitle);
		moreDialog.setContentView(R.layout.x_dialog_zhishiku_more);
		TextView tv_title = (TextView) moreDialog.findViewById(R.id.tv_title);
		tv_title.setText(buMenItem.getBranchName());
		// 吧类别名字设置为标题
		TextView tv_deltype = (TextView) moreDialog.findViewById(R.id.tv_deltype);
		tv_deltype.setText("删除该部门");
		tv_deltype.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 删除类别
				moreDialog.dismiss();
				MyDialogManager.getI().showWithClickDialog(BuMenListActivity.this, "确定删除该部门？", new OnCancle() {

					@Override
					public void sure() {
						currentBuMenBean = buMenItem;
						initData_delBuMen(buMenItem.getBranchId());
					}

					@Override
					public void cancle() {
					}
				});
			}
		});
		moreDialog.findViewById(R.id.tv_upname).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跟新名字
				upDateBuMenDialog(buMenItem);
				moreDialog.dismiss();
			}
		});
		moreDialog.show();

	}

	private Dialog upDl;

	/**
	 * 修改知识库类
	 * 
	 * @param buMenItem
	 */
	private void upDateBuMenDialog(final BuMenItem buMenItem) {
		upDl = new Dialog(this, R.style.Translucent_NoTitle);
		upDl.setContentView(R.layout.x_dialog_zhishiku);
		final EditText et_conten = (EditText) upDl.findViewById(R.id.et_conten);
		et_conten.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if (hasFocus) {
					et_conten.setHint("");
				}
			}
		});
		TextView tv_title = (TextView) upDl.findViewById(R.id.tv_title);
		tv_title.setText("重命名");
		et_conten.setHint(buMenItem.getBranchName());
		// et_conten.setText(typeItemBean.getSortNm());
		upDl.findViewById(R.id.bt_cancle).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				upDl.dismiss();
			}
		});
		upDl.findViewById(R.id.bt_upName).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String name = et_conten.getText().toString().trim();
				if (!TextUtils.isEmpty(name)) {
					currentBuMenBean = buMenItem;
					initData_cmmBumen(name, buMenItem.getBranchId());
					upDl.dismiss();
				} else {
					ToastUtils.showCustomToast("内容不能为空！");
				}
			}

		});
		upDl.show();
	}

	/**
	 * 更多操作窗口
	 */
	private Dialog moreMemberDialog;

	private void showMemberMoreDialog(final View view) {
		moreMemberDialog = new Dialog(BuMenListActivity.this, R.style.Translucent_NoTitle);
		moreMemberDialog.setContentView(R.layout.x_dialog_zhishiku_more);
		// 吧类别名字设置为标题
		TextView tv_del = (TextView) moreMemberDialog.findViewById(R.id.tv_deltype);
		TextView tv_upname = (TextView) moreMemberDialog.findViewById(R.id.tv_upname);
		tv_del.setText("从公司移除");
		tv_upname.setText("更换部门");
		tv_del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 从公司移除
				final int position = (Integer) view.getTag();
				MemberBean InforBean = members.get(position);
				if (InforBean.getMemberId() != Integer.valueOf(SPUtils.getSValues("memId"))) {
					moreMemberDialog.dismiss();
					MyDialogManager.getI().showWithClickDialog(BuMenListActivity.this, "确定将该成员从公司移除？", new OnCancle() {

						@Override
						public void sure() {
							currentMemberBean = members.get(position);
							removeMemberFormCompany(currentMemberBean.getMemberId());
						}

						@Override
						public void cancle() {
						}
					});
				} else {
					ToastUtils.showCustomToast("自己不能移除自己");
				}
			}
		});
		moreMemberDialog.findViewById(R.id.tv_upname).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 更换部门
				int position = (Integer) view.getTag();
				currentMemberInforBean = members.get(position);
				initData_bumen("");
				moreMemberDialog.dismiss();
			}

		});
		moreMemberDialog.show();
	}

	private String currentName;

	/**
	 * 部门item数据
	 */
	private BuMenItem currentBuMenBean;
	/**
	 * 成员item数据
	 */
	private MemberBean currentMemberBean;

	private OnClickListener myOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			BuMenItem bean = (BuMenItem) v.getTag();
			Intent intent = new Intent(BuMenListActivity.this, BuMenListActivity.class);
			intent.putExtra("id", String.valueOf(bean.getBranchId()));
			intent.putExtra("title", bean.getBranchName());
			startActivity(intent);
		}
	};

	/**
	 * 创建部门控件
	 */
	private View tv_sao, tv_bumen_line;

	/**
	 * 刷新部门列表
	 */
	private void refreshAdapter() {
		if (lv_pull != null) {
			if (myAdapter == null) {
				myAdapter = new BuMenAdapter();
				lv_pull.setAdapter(myAdapter);
			} else {
				myAdapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 刷新成员列表
	 */
	private void refreshSecondAdapter() {
		if (lv_mems != null) {
			if (mySecondAdapter == null) {
				mySecondAdapter = new MemberAdapter();
				lv_mems.setAdapter(mySecondAdapter);
			} else {
				mySecondAdapter.notifyDataSetChanged();
			}
		}
	}

	private CheckBox currentCheckBox;

	class MyRemoveAdatper extends BaseAdapter {
		@Override
		public int getCount() {
			return buMengList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View convert = getLayoutInflater().inflate(R.layout.x_bumeng_item, null);

			final BuMengItemBean buMengItemBean = buMengList.get(position);
			TextView tv_groupname = (TextView) convert.findViewById(R.id.tv_groupname);
			View iv_next = convert.findViewById(R.id.iv_next);
			final CheckBox cb_aszhishi = (CheckBox) convert.findViewById(R.id.cb_aszhishi);
			if (String.valueOf(buMengItemBean.getBranchId()).equals(deptid)) {
				cb_aszhishi.setChecked(true);
			} else {
				cb_aszhishi.setChecked(false);
			}
			cb_aszhishi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (cb_aszhishi.isChecked()) {
						if (currentCheckBox == null) {
							currentCheckBox = cb_aszhishi;

						} else {
							if (!currentCheckBox.equals(cb_aszhishi)) {
								currentCheckBox.setChecked(false);
								currentCheckBox = cb_aszhishi;
								cb_aszhishi.setChecked(true);
							}
						}
						currentBumengName = buMengItemBean.getBranchName();
						deptid = String.valueOf(buMengItemBean.getBranchId());
					} else {

						deptid = "";
					}
				}
			});
			if ("1".equals(buMengItemBean.getIschild())) {
				iv_next.setVisibility(View.VISIBLE);
				iv_next.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deptid = "";// 条下一级就清掉勾选的id
						initData_bumen(String.valueOf(buMengItemBean.getBranchId()));
					}
				});
			} else {
				iv_next.setVisibility(View.INVISIBLE);
			}
			tv_groupname.setText(buMengItemBean.getBranchName());
			return convert;
		}
	}

	private MyRemoveAdatper myBuMengAdatper;

	// 移动部门操作适配器
	private void refreshMoveAdapter() {
		if (lv_types != null) {
			if (myBuMengAdatper == null) {
				myBuMengAdatper = new MyRemoveAdatper();
				lv_types.setAdapter(myBuMengAdatper);
			} else {
				myBuMengAdatper.notifyDataSetChanged();
			}
		}
	}



	// ------------------------------TODO OKHttp回调数据--------------------------
	public class MyStringCallback extends StringCallback {
		@Override
		public void onError(Call call, Exception e, int id) {
			e.printStackTrace();
		}

		@Override
		public void onResponse(String response, int id) {
			resultData(response, id);
		}
	}
	/**
	 * 回调数据
	 */
	private void resultData(String json, int tag) {
		// 1成员列表 2创建子部门3邀请同事入部门4是否是部门成员5设为常用6删除部门7修改部门名称
		// 8获取部门列表9将用户从公司移除10移动成员到其他部门
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (tag) {
				case 1:
					BuMenListBean parseObject = JSON.parseObject(json, BuMenListBean.class);
					if (parseObject.isState()) {
						departls = parseObject.getDepartls();
						members = parseObject.getMemls();
						if (departls.size() > 0) {
							refreshAdapter();
						}
						if (members.size() > 0) {
							refreshSecondAdapter();
						}
					}

					break;
				case 2:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							initData_member();
						} else {
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {

					}
					break;
				case 3:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							// getMemberList();
							ToastUtils.showCustomToast("发送邀请！");
						} else {
							ToastUtils.showCustomToast("" + jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
					}
					break;
				case 4:
					break;
				case 5:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							MemberBean memberBean = members.get(currentPosition);
							if (1 == memberBean.getCy()) {
								memberBean.setCy(2);
							} else {
								memberBean.setCy(1);
							}
						} else {
							ToastUtils.showCustomToast("操作失败" + jsonObject.getString("msg"));
						}
					} catch (JSONException e) {

						ToastUtils.showCustomToast("操作失败");
					}
					break;
				case 6:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							departls.remove(currentBuMenBean);
							refreshAdapter();
						} else {
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtils.showCustomToast("操作失败");
					}
					break;
				case 7:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							currentBuMenBean.setBranchName(currentName);
							refreshAdapter();
						} else {
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtils.showCustomToast("操作失败");
					}
					break;
				case 8:
					BranchListBean parseObject2 = JSON.parseObject(json, BranchListBean.class);
					if (parseObject2 != null && parseObject2.isState()) {
						List<BuMengItemBean> deptList = parseObject2.getDeptList();
						if (deptList != null) {
							currentLv++;
							buMengList.clear();
							buMengList.addAll(deptList);
							if (!typePopupWindow.isShowing()) {
								typePopupWindow.showAtLocation(new TextView(BuMenListActivity.this), Gravity.CENTER, 0, 0);
							}
							refreshMoveAdapter();
							buMengLv.put(currentLv, deptList);
							if (tv_backlv != null && currentLv >= 2) {
								tv_backlv.setVisibility(View.VISIBLE);
							} else {
								tv_backlv.setVisibility(View.INVISIBLE);
							}
						}
					}
					break;
				case 9:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							members.remove(currentMemberBean);
							refreshSecondAdapter();
						} else {
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {

						ToastUtils.showCustomToast("操作失败");
					}
					break;
				case 10:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							if (currentMemberInforBean != null) {
								members.remove(currentMemberInforBean);
							}
							refreshSecondAdapter();
						} else {
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						ToastUtils.showCustomToast("操作失败");
					}
					break;
			}
		}
	}

}
