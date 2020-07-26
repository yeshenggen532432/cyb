package com.qwb.view.log.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qwb.utils.MyUtils;
import com.qwb.view.log.model.RizhiPinlunBean.RizhiPinlun;
import com.xmsx.qiweibao.R;

import java.util.List;

/**
 * 日志模块--日志详情的回复
 */
public class CommentAdapter extends BaseAdapter {
	private Context mContext;
	private List<RizhiPinlun> mHuifuList;

	public CommentAdapter(Context context, List<RizhiPinlun> huifuList) {
		this.mContext = context;
		this.mHuifuList = huifuList;
	}

	@Override
	public int getCount() {
		return mHuifuList.size();
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
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.x_adapter_log_comment, null);
		} 
		
		TextView tv_head = MyUtils.getViewFromVH(convertView,R.id.tv_head);
		TextView tv_name = MyUtils.getViewFromVH(convertView,R.id.tv_title);
		TextView tv_time = MyUtils.getViewFromVH(convertView,R.id.tv_time);
		TextView tv_content = MyUtils.getViewFromVH(convertView,R.id.tv_content);
		
		RizhiPinlun rizhiPinlun = mHuifuList.get(position);
		if (rizhiPinlun != null) {
			tv_head.setText(rizhiPinlun.getMemberNm());
			tv_name.setText(rizhiPinlun.getMemberNm());
			tv_time.setText(rizhiPinlun.getPltime());
			tv_content.setText(rizhiPinlun.getContent());
		}

		return convertView;
	}
}
