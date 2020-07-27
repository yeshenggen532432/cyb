package com.qwb.view.audit.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.view.audit.model.ShenPiDetialBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 审批详情流程
 */
public class AuditProcessAdapter extends BaseAdapter {
	private Activity mActivity;
	private List<ShenPiDetialBean.LiuChengListBean> checkList = new ArrayList<>();

	public AuditProcessAdapter(Activity activity, List<ShenPiDetialBean.LiuChengListBean> checkList) {
		this.mActivity = activity;
		this.checkList = checkList;
	}

	@Override
	public int getCount() {
		return checkList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.x_activity_shen_pi_detial_adapter, null);
		}

		ShenPiDetialBean.LiuChengListBean item = checkList.get(position);
		View tv_bottom = MyUtils.getViewFromVH(convertView, R.id.tv_bottom);//底部的线
		if (position == checkList.size() - 1) {
			tv_bottom.setVisibility(View.GONE);
		} else {
			tv_bottom.setVisibility(View.VISIBLE);
		}
		ImageView iv_ico = MyUtils.getViewFromVH(convertView, R.id.iv_ico);
		CircleImageView viewFromVH = MyUtils.getViewFromVH(convertView, R.id.iv_head);
		TextView tv_time = MyUtils.getViewFromVH(convertView, R.id.tv_time);
		TextView tv_name = MyUtils.getViewFromVH(convertView, R.id.tv_title);
		TextView tv_zhuangtai = MyUtils.getViewFromVH(convertView, R.id.tv_zhuangtai);
		tv_name.setText(item.getMemberNm());
		MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + item.getMemberHead(), viewFromVH);
		String isCheck = item.getIsCheck();
		// 1发起申请 2 同意 3 拒绝 4 转发 5 撤销
		String checkTp = item.getCheckTp();
		String dsc = item.getDsc();
		if ("2".equals(isCheck)) {
			// 审批过了
			tv_zhuangtai.setVisibility(View.VISIBLE);
			tv_time.setVisibility(View.VISIBLE);
			if ("1".equals(checkTp)) {
				SpannableString sb = MyUtils.getColorText("#179F01", "发起申请", 0, 4);
				tv_zhuangtai.setText(sb);
				iv_ico.setImageResource(R.drawable.sp_detial_ty);
			} else if ("2".equals(checkTp)) {
				iv_ico.setImageResource(R.drawable.sp_detial_ty);

				if (!MyUtils.isEmptyString(dsc)) {
					String string = "已同意" + "(" + item.getDsc() + ")";
					SpannableString sb = MyUtils.getColorText("#179F01", string, 0, 3);
					tv_zhuangtai.setText(sb);
				} else {
					SpannableString sb = MyUtils.getColorText("#179F01", "已同意", 0, 3);
					tv_zhuangtai.setText(sb);
				}
			} else if ("3".equals(checkTp)) {
				iv_ico.setImageResource(R.drawable.sp_detial_jj);
				if (!MyUtils.isEmptyString(dsc)) {
					String string = "已拒绝" + "(" + item.getDsc() + ")";
					SpannableString sb = MyUtils.getColorText("#179F01", string, 0, 3);
					tv_zhuangtai.setText(sb);
				} else {
					SpannableString sb = MyUtils.getColorText("#179F01", "已拒绝", 0, 3);
					tv_zhuangtai.setText(sb);
				}
			} else if ("4".equals(checkTp)) {
				iv_ico.setImageResource(R.drawable.sp_detial_zj);
				if (!MyUtils.isEmptyString(dsc)) {
					String string = "已转交" + "(" + item.getDsc() + ")";
					SpannableString sb = MyUtils.getColorText("#179F01", string, 0, 3);
					tv_zhuangtai.setText(sb);
				} else {
					SpannableString sb = MyUtils.getColorText("#179F01", "已转交", 0, 3);
					tv_zhuangtai.setText(sb);
				}
			} else if ("5".equals(checkTp)) {
				SpannableString sb = MyUtils.getColorText("#179F01", "已撤销", 0, 3);
				iv_ico.setImageResource(R.drawable.sp_detial_cx);
				tv_zhuangtai.setText(sb);
			}
			tv_time.setText(item.getCheckTime());
		} else if ("1".equals(isCheck)) {
			if("6".equals(item.getCheckTp())){
				iv_ico.setImageResource(R.drawable.sp_detial_ing);
				tv_time.setVisibility(View.INVISIBLE);
				tv_zhuangtai.setVisibility(View.VISIBLE);
				tv_zhuangtai.setText("退回：" + dsc);
			}else{
				iv_ico.setImageResource(R.drawable.sp_detial_ing);
				tv_time.setVisibility(View.INVISIBLE);
				tv_zhuangtai.setVisibility(View.VISIBLE);
				tv_zhuangtai.setText("审批中");
			}
		} else {
			// 还没轮到的审批用户
			iv_ico.setImageResource(R.drawable.sp_detial_);
			tv_time.setVisibility(View.INVISIBLE);
			tv_zhuangtai.setVisibility(View.GONE);
		}

		return convertView;
	}
	
	
}
