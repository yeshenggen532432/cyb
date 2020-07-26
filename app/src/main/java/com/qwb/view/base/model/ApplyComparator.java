package com.qwb.view.base.model;

import java.util.Comparator;

/**
 * 首页模块--排序
 */
public class ApplyComparator implements Comparator<ApplyBean> {
	@Override
	public int compare(ApplyBean p1, ApplyBean p2) {
			// 先排id
			if (p1.sort != p2.sort) {
				return p1.sort - p2.sort;
			} else {
				// id相同则按名称排序
				if (!p1.applyName.equals(p2.applyName)) {
					return p1.applyName.compareTo(p2.applyName);
				} else {
					// 名称也相同则按学号排序
					return p1.sort - p2.sort;
				}
			}
	}
}
