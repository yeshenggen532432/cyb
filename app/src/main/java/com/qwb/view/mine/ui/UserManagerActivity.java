package com.qwb.view.mine.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.ComputeHeightListView;
import com.qwb.utils.OtherUtils;
import com.qwb.view.mine.parsent.PUserManager;
import com.qwb.view.mine.model.UserManagerBean;
import com.xmsx.cnlife.widget.CircleImageView;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyLoginUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 设置-账号管理
 */
public class UserManagerActivity extends XActivity<PUserManager> {
	@Override
	public int getLayoutId() {
		return R.layout.x_activity_user_manager;
	}

	@Override
	public PUserManager newP() {
		return new PUserManager();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		loadData();
		initUI();
	}

	/**
	 * 加载缓存的账号
	 */
	private void loadData() {
		zhglList.clear();
		String zhanghaoguanli = SPUtils.getSValues(ConstantUtils.Sp.USER_MANAGER);
		if (!MyUtils.isEmptyString(zhanghaoguanli)) {
			List<UserManagerBean> parseArray = JSON.parseArray(zhanghaoguanli, UserManagerBean.class);
			if (parseArray != null) {
				for (int i = 0; i < parseArray.size(); i++) {
					UserManagerBean zhanghaoglBean = parseArray.get(i);
					zhglList.add(zhanghaoglBean);
				}
			}
		}
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		initHead();
		initAdapter();
	}

	/**
	 * 初始化头部
	 */
	private TextView tv_headRight;
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Router.pop(context);
			}
		});
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		tv_headTitle.setText("账号管理");
		tv_headRight.setText("编辑");
	}

	/**
	 * 适配器
	 */
	private void initAdapter() {
		mListView = findViewById(R.id.listView);
		zhglAdapter = new ZhglAdapter();
		mListView.setAdapter(zhglAdapter);
		// 切换账号
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				UserManagerBean zhanghaoglBean = zhglList.get(position);
				if (zhanghaoglBean != null) {
					if(!isBianji){
						// 判断是否是当前账号
						if (!SPUtils.getID().equals(zhanghaoglBean.getMid())) {
							phone = zhanghaoglBean.getPhone();
							pwd = zhanghaoglBean.getPwd();
							getP().submit(context,phone,pwd,true);
						}
					}
				}
			}
		});
	}

	private String pwd;//密码
	private String phone;//账号：手机号
	private boolean isBianji=false;//是否编辑
	private UserManagerBean mUserManagerBean;//要删除的账号
	private ComputeHeightListView mListView;
	private ZhglAdapter zhglAdapter;
	private ArrayList<UserManagerBean> zhglList = new ArrayList<>();
	private final class ZhglAdapter extends BaseAdapter {
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.x_adapter_zhanghaoguanli, null);
			CircleImageView iv_head = MyUtils.getViewFromVH(convertView, R.id.iv_head);
			TextView tv_name = MyUtils.getViewFromVH(convertView, R.id.tv_title);
			TextView tv_phone = MyUtils.getViewFromVH(convertView, R.id.tv_phone);
			ImageView iv_dagou = MyUtils.getViewFromVH(convertView, R.id.iv_dagou);
			ImageView iv_delete = MyUtils.getViewFromVH(convertView, R.id.iv_delete);
			final UserManagerBean zhanghaoglBean = zhglList.get(position);
			if (zhanghaoglBean != null) {
				String memberHead = zhanghaoglBean.getMemberHead();
				if (!MyUtils.isEmptyString(memberHead)) {
					MyGlideUtil.getInstance().displayImageSquere(Constans.IMGROOTHOST + memberHead, iv_head);
				}
				tv_name.setText(zhanghaoglBean.getName());
				tv_phone.setText(zhanghaoglBean.getPhone());
				if(isBianji){
					iv_delete.setVisibility(View.VISIBLE);
					iv_dagou.setVisibility(View.GONE);
				}else{
					iv_delete.setVisibility(View.GONE);
					if (SPUtils.getID().equals(zhanghaoglBean.getMid())) {
						iv_dagou.setVisibility(View.VISIBLE);
					} else {
						iv_dagou.setVisibility(View.GONE);
					}
				}
				iv_delete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mUserManagerBean = zhanghaoglBean;
						showDialogPhone(zhanghaoglBean.getPhone());
					}
				});
			}
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return zhglList.size();
		}
	}

	/**
	 * 刷新适配器
	 */
	private void refreshAdapter(){
		if(zhglAdapter==null){
			zhglAdapter = new ZhglAdapter();
			mListView.setAdapter(zhglAdapter);
		}else{
			zhglAdapter.notifyDataSetChanged();
		}
	}

	@OnClick({R.id.rl_add_zh,R.id.tv_head_right})
	public void onClickView (View v){
		switch (v.getId()) {
			case R.id.rl_add_zh://添加账号
				MyLoginUtil.logout(context);
				break;
			case R.id.tv_head_right://编辑
				if(isBianji){
					isBianji=false;
					tv_headRight.setText("编辑");
				}else{
					isBianji=true;
					tv_headRight.setText("完成");
				}
				refreshAdapter();
				break;
		}
	}

	/**
	 * 拨打电话
	 */
	private void showDialogPhone(final String phone) {
		final NormalDialog dialog = new NormalDialog(context);
		dialog
				.isTitleShow(true)//是否需要标题
				.style(NormalDialog.STYLE_TWO)//标题风格二(标题居中，没有线)，默认风格一
				.title("温馨提示")
				.titleTextColor(getResources().getColor(R.color.gray_3))
				.titleTextSize(18)
				.content("是否要删除账号："+phone+"吗？")
				.contentTextColor(getResources().getColor(R.color.blue))
				.contentTextSize(15)
				.contentGravity(Gravity.CENTER)//内容显示位置
				.show();

		dialog.setOnBtnClickL(
				new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						dialog.dismiss();
					}
				},
				new OnBtnClickL() {
					@Override
					public void onBtnClick() {
						zhglList.remove(mUserManagerBean);
						String jsonString = JSON.toJSONString(zhglList);
						SPUtils.setValues(ConstantUtils.Sp.USER_MANAGER, jsonString);
						refreshAdapter();
						dialog.dismiss();
					}
				});
	}

}
