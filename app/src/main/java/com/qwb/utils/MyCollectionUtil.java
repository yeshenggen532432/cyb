package com.qwb.utils;

import java.util.*;

/**
 * Collections集合工具类
 */
public class MyCollectionUtil {
	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(Collection c) {
		return (c == null) || c.isEmpty();
	}

	public static boolean isNotEmpty(Collection c) {
		return !(isEmpty(c));
	}

	public static boolean isNotEmpty(Map c) {
		return !(isEmpty(c));
	}
	public static boolean isEmpty(Map c) {
		return (c == null) || c.isEmpty();
	}

}
