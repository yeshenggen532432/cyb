package com.qwb.utils;

/**
 *  菜单：工具类
 */
public class MyMenuUtil {

	/**
	 * 是否有某个菜单:订货下单
	 */
	public static void hasMenuDhOrder() throws Exception {
//		boolean query = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.DHXD_QUERY_NEW);
//		boolean order = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.DHXD_ORDER_NEW);
//		if (query && !order) {
//			throw new Exception("您没有下单权限");
//		}
	}

	/**
	 * 是否有某个菜单:客户拜访
	 */
	public static void hasMenuCustomerBf() throws Exception {
//		boolean query = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.KHGL_QUERY_NEW);
//		boolean call = MyLoginUtil.getMenuByApplyCode(ConstantUtils.Apply.KHGL_CALL_NEW);
//		if (query && !call) {
//			throw new Exception("您没有拜访权限");
//		}
	}

	/**
	 * tab首页：相关的菜单
	 * 固定id:985
	 */
	public static boolean hasMenuTabMain(int pId){
		return MyStringUtil.eq("985", String.valueOf(pId));
	}
	/**
	 * tab报表：相关的菜单
	 * 固定id:986
	 */
	public static boolean hasMenuTabTable(int pId){
		return MyStringUtil.eq("986", String.valueOf(pId));
	}


}
