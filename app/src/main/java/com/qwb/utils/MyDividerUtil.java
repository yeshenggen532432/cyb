package com.qwb.utils;

import android.content.Context;

import com.chiyong.t3.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


/**
 * RecyclerView的分割线
 */
public class MyDividerUtil {
	/**
	 * 线：水平高度：0.5；颜色：gray_b
	 */
	public static HorizontalDividerItemDecoration getH05CGray(Context context) {
			return new HorizontalDividerItemDecoration.Builder(context)
					.colorResId(R.color.gray_b)
					.sizeResId(R.dimen.dp_0_5)
					.showLastDivider()
					.build();
	}
	/**
	 * 线：水平高度：1；颜色：gray_e
	 */
	public static HorizontalDividerItemDecoration getH1CGray(Context context) {
			return new HorizontalDividerItemDecoration.Builder(context)
					.colorResId(R.color.gray_e)
					.sizeResId(R.dimen.dp_1)
					.showLastDivider()
					.build();
	}

	/**
	 * 线：水平高度：5；颜色：gray_e
	 */
	public static HorizontalDividerItemDecoration getH5CGray(Context context) {
		return new HorizontalDividerItemDecoration.Builder(context)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_5)
				.showLastDivider()
				.build();
	}



}
