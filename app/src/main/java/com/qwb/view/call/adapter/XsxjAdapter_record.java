package com.qwb.view.call.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qwb.view.step.model.QueryXstypeBean.QueryXstype;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *拜访记录-销售小结的适配器
 * 
 * */
/*
 * Warning: getView(int position, View convertView, ViewGroup
 * parent),对于viewList中的最后一项需要特殊处理，
 */
public class XsxjAdapter_record extends BaseAdapter {
	// ====================================================================
	private int[] mTo;
	private String[] mFrom;
	private List<? extends Map<String, ?>> mData;
	private int mResource;
	private LayoutInflater mInflater;
	private List<View> viewList = new ArrayList<View>();
	private Context context;
	// ====================================================================
	int id_row_layout;
	int selectedPosition = -1;
	int[] difColor = new int[2];
//	public MyOnItemClickListener listener;
	private int lastViewsSize = -1;
	
	//窗体
	private View contentView;
	private ListView mListView;
	private PopupWindow popWin;
	private Activity mActivity;
	private List<QueryXstype> xstypeList=new ArrayList<>();
	private int index=-1;

	/**
	 * Constructs a MyAdapter.This is like the constructor of SimpleAdapter.And
	 * this constructor set the background color of item.
	 * */
	public XsxjAdapter_record(Context context, List<? extends Map<String, ?>> data,
                              int resource, String[] from, int[] to, int colorId, int color1Id) {
		this.context = context;
		mData = data;// 总数据
		mResource = resource;// 资源id
		mFrom = from;
		mTo = to; // 标题id数组
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		difColor[0] = context.getResources().getColor(colorId);
		difColor[1] = context.getResources().getColor(color1Id);
		
	}

	/**
	 * Constructs a MyAdapter.This is like the constructor of SimpleAdapter.
	 * */
	public XsxjAdapter_record(Context context, List<? extends Map<String, ?>> data,
                              int resource, String[] from, int[] to) {
		this.context = context;
		mData = data;
		mResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

	/**
	 * 
	 * */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final View root=mInflater.inflate(R.layout.x_adapter_xsxj_record, null);
		
		TextView tv_dhl=(TextView) root.findViewById(R.id.tv_dhl);
		TextView tv_sxl=(TextView) root.findViewById(R.id.tv_sxl);
		TextView tv_kcl=(TextView) root.findViewById(R.id.tv_kcl);
		TextView tv_dd=(TextView) root.findViewById(R.id.tv_dd);
		TextView tv_xsType=(TextView) root.findViewById(R.id.tv_xsType);
		TextView tv_xxd=(TextView) root.findViewById(R.id.tv_xxd);
		TextView tv_remo=(TextView) root.findViewById(R.id.tv_remo);
		TextView tv1=(TextView) root.findViewById(R.id.column1);
		TextView tv7=(TextView) root.findViewById(R.id.column7);
		TextView tv9=(TextView) root.findViewById(R.id.column9);
		
		for (int i = 0; i < mData.size(); i++) {
			Map<String, ?> map = mData.get(position);
			tv1.setText((String) map.get(mFrom[0]));
			tv_dhl.setText((String) map.get(mFrom[1]));//到货量
			tv_sxl.setText((String) map.get(mFrom[2]));//实销量
			tv_kcl.setText((String) map.get(mFrom[3]));//库存量
			tv_dd.setText((String) map.get(mFrom[4]));//订单
			tv_xsType.setText((String) map.get(mFrom[5]));//销售类型
			tv_xxd.setText((String) map.get(mFrom[6]));//新鲜度
			tv7.setText((String) map.get(mFrom[7]));//规格
			tv_remo.setText((String) map.get(mFrom[8]));//备注
		}
		
		
//		View v = createViewFromResource(position, convertView, parent,mResource);
//		// 最后一项
//		if (lastViewsSize == viewList.size()) {
//			if (position != 0) {
//				if (position == parent.getChildCount()
//						&& position == viewList.size() - 1) {
//					setWidth(v);
//				}
//			}
//		} else {
//			lastViewsSize = viewList.size();
//		}
		return root;
	}

	

	private View createViewFromResource(int position, View convertView,
                                        ViewGroup parent, int resource) {
		View v=mInflater.inflate(resource, parent, false);
		viewList.add(v);
		
//		bindView(position, v);
		v.setBackgroundColor(difColor[position % 2]);
		return v;
	}

//	// 添加数据
//	private void bindView(int position, View v) {
//
//		for (int i = 0; i < mFrom.length; i++) {//
//			final int line = position;// 行
//			final int row = i; // 列
//			TextView txt = (TextView) v.findViewById(mTo[i]);
//			txt.setText((String) mData.get(position).get(mFrom[i]));
//			txt.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (MyAdapter.this.listener != null)
//						MyAdapter.this.listener.OnItemClickListener(v, line,
//								row, 0);
//				}
//			});
//		}
//	}

	/**
	 
	 * */
	public void setColumnWidth(int column, int width) {
		for (int i = 0; i < viewList.size(); i++) {
			View v = viewList.get(i);
			TextView txt = (TextView) v.findViewById(mTo[column]);
			txt.setLayoutParams(new LinearLayout.LayoutParams(width, txt
					.getHeight()));
		}
	}

	@SuppressLint("NewApi")
	private void setWidth(View v) {
		// 对于最后一项进行特殊的处理
		View v1 = viewList.get(0);
		for (int j = 1; j < viewList.size(); j++) {
			View v2 = viewList.get(j);
			for (int i = 0; i < mTo.length; i++) {
				final TextView txt = (TextView) v2.findViewById(mTo[i]);
				final TextView txt1 = (TextView) v1.findViewById(mTo[i]);
				txt.setLayoutParams(new LinearLayout.LayoutParams(txt1
						.getMeasuredWidth(), txt1.getMeasuredHeight()));
			}
		}
	}

	// 选中的position
	public void setSelectedPosition(int position) {
		this.selectedPosition = position;
	}

//	public void setMyOnItemClickListener(MyOnItemClickListener l) {
//		this.listener = l;
//	}

	public void setItemColor(int colorId, int color1Id) {
		difColor[0] = context.getResources().getColor(colorId);
		difColor[1] = context.getResources().getColor(color1Id);
	}

    
}
