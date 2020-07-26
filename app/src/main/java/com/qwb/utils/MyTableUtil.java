package com.qwb.utils;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.qwb.widget.table.TableHorizontalScrollView;
import com.qwb.widget.table.TableOnScrollListener;


/**
 * 表格：设置左，右，右边头部，三者之间滑动关联
 */
public class MyTableUtil {

	private static MyTableUtil MANAGER = null;


	public static MyTableUtil getInstance() {
		if (MANAGER == null) {
			MANAGER = new MyTableUtil();
		}
		return MANAGER;
	}


	public void setSyncScrollListener(final RecyclerView rvLeft, final RecyclerView rvRight, TableHorizontalScrollView scrollView) {
		/**
		 * 设置两个列表的同步滚动
		 */
		final RecyclerView.OnScrollListener mLeftOSL = new TableOnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				// 当楼层列表滑动时，单元（房间）列表也滑动
				rvRight.scrollBy(dx, dy);
			}
		};
		final RecyclerView.OnScrollListener mRightOSL = new TableOnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				// 当单元（房间）列表滑动时，楼层列表也滑动
				rvLeft.scrollBy(dx, dy);
			}
		};
		rvLeft.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
			private int mLastY;

			@Override
			public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
				// 当列表是空闲状态时
				if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
					onTouchEvent(rv, e);
				}
				return false;
			}

			@Override
			public void onTouchEvent(RecyclerView rv, MotionEvent e) {
				// 若是手指按下的动作，且另一个列表处于空闲状态
				if (e.getAction() == MotionEvent.ACTION_DOWN && rvRight.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
					// 记录当前另一个列表的y坐标并对当前列表设置滚动监听
					mLastY = rv.getScrollY();
					rv.addOnScrollListener(mLeftOSL);
				} else {
					// 若当前列表原地抬起手指时，移除当前列表的滚动监听
					if (e.getAction() == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
						rv.removeOnScrollListener(mLeftOSL);
					}
				}
			}

			@Override
			public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
			}
		});
		rvRight.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
			private int mLastY;

			@Override
			public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
				if (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
					onTouchEvent(rv, e);
				}
				return false;
			}

			@Override
			public void onTouchEvent(RecyclerView rv, MotionEvent e) {
				if (e.getAction() == MotionEvent.ACTION_DOWN && rvLeft.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
					mLastY = rv.getScrollY();
					rv.addOnScrollListener(mRightOSL);
				} else {
					if (e.getAction() == MotionEvent.ACTION_UP && rv.getScrollY() == mLastY) {
						rv.removeOnScrollListener(mRightOSL);
					}
				}
			}

			@Override
			public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
			}
		});
		scrollView.setScrollViewListener(new TableHorizontalScrollView.ScrollViewListener() {
			@Override
			public void onScrollChanged(TableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
				rvLeft.removeOnScrollListener(mLeftOSL);
				rvRight.removeOnScrollListener(mRightOSL);
			}
		});
	}

}
