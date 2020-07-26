package com.xmsx.cnlife.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.qwb.view.order.ui.OrderDetailActivity;
import com.qwb.view.shop.ui.ShopOrderActivity;
import com.qwb.view.call.adapter.XsxjAdapter_record;

public class MyTableListView {
	private Context context;
	private int[] titles;
	private LinearLayout head;
	private HorizontalScrollView hs;
	private ListView listView;
	private ComputeHeightListView listViewSupplierOrder;
	private ComputeHeightListView listView2;
	private ComputeHeightListView listViewOrderDetail;
	private XsxjAdapter_record adapter_record;
	private ShopOrderActivity.XsxjAdapterOrder adapterSupplierOrder;
	private OrderDetailActivity.AdapterOrderDetail adapterOrderDetail;//订单详情


	/**
	 * 构造方法--商品展区下单
	 */
	@SuppressLint("NewApi")
	public MyTableListView(Context context, View v, int[] titles, int hsId, int lvId, int hdId, ShopOrderActivity.XsxjAdapterOrder adapter) {
		this.context = context;
		this.titles = titles;//标题数组
		this.head = (LinearLayout) v.findViewById(hdId);//标题id
		this.listViewSupplierOrder= (ComputeHeightListView) v.findViewById(lvId);//listview的id
		this.hs = (HorizontalScrollView) v.findViewById(hsId);//scrollView的id
		this.adapterSupplierOrder = adapter;
		setAdapterSupplierOrder();
	}

	/**
	 * 构造方法--拜访记录
	 */
	@SuppressLint("NewApi")
	public MyTableListView(Context context, View v, int[] titles, int hsId, int lvId, int hdId, XsxjAdapter_record adapter) {
		this.context = context;
		this.titles = titles;//标题数组
		this.head = (LinearLayout) v.findViewById(hdId);//标题id
		this.listView2 = (ComputeHeightListView) v.findViewById(lvId);//listview的id
		this.hs = (HorizontalScrollView) v.findViewById(hsId);//scrollView的id
		this.adapter_record = adapter;
//		setTitleTouchListener(v);//标题触摸监听
		setAdapter2();
	}

	/**
	 * 构造方法-订单详情
	 */
	@SuppressLint("NewApi")
	public MyTableListView(Context context, View v, int[] titles, int hsId, int lvId, int hdId, OrderDetailActivity.AdapterOrderDetail adapterOrderDetail) {
		this.context = context;
		this.titles = titles;//标题数组
		this.head = (LinearLayout) v.findViewById(hdId);//标题id
		this.listViewOrderDetail = (ComputeHeightListView) v.findViewById(lvId);//listview的id
		this.hs = (HorizontalScrollView) v.findViewById(hsId);//scrollView的id
		this.adapterOrderDetail = adapterOrderDetail;
		setAdapterOrderDetail();
	}

	/**
	 * 适配器
	 */
	private void setAdapter2() {
		listView2.setAdapter(adapter_record);
	}
	/**
	 * 适配器
	 */
	private void setAdapterSupplierOrder() {listViewSupplierOrder.setAdapter(adapterSupplierOrder);}

	/**
	 * 适配器
	 */
	private void setAdapterOrderDetail() {
		listViewOrderDetail.setAdapter(adapterOrderDetail);
	}

	/**
	 * 标题可以放大缩小
	 * */
	private void setTitleTouchListener(View v) {

		for (int i = 0; i < titles.length; i++) {
			final int column = i;
			v.findViewById(titles[i]).setOnTouchListener(new OnTouchListener() {
				int x = 0;
				int x1 = 0;
				int width = 0;
				boolean isMoved = false;
				int t = 20;

				@SuppressLint("NewApi")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					// two teps
					// 1.when touch down and move, change the width of head;
					// 2.touch up ,change the width of the columns ;
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						x = (int) event.getX();
						hs.requestDisallowInterceptTouchEvent(true);
						return true;
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						x1 = (int) event.getX();
						width = v.getMeasuredWidth() + (x1 - x) + t;
						if (t != 0)
							t = 0;
						v.setLayoutParams(new LinearLayout.LayoutParams(width,
								v.getMeasuredHeight()));
						x = x1;
						isMoved = true;
						return true;
					}
					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (isMoved)
//							adapter.setColumnWidth(column, width);
						return true;

					}
					if (event.getAction() == MotionEvent.ACTION_CANCEL) {
						if (isMoved)
//							adapter.setColumnWidth(column, width);
						return true;
					}
					return true;
				}
			});
		}
	}

	/**
	 * 设置标题颜色
	 * */
	@SuppressLint("NewApi")
	public void setHeadColor(int colorId) {
//		int color = context.getResources().getColor(colorId);
		this.head.setBackgroundColor(colorId);
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public int[] getTitles() {
		return titles;
	}

	public void setTitles(int[] titles) {
		this.titles = titles;
	}

}