package com.qwb.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * App
 */
public class MyPassWordUtil {
	public static final String TIP = "数字,字母及特殊字符至少包含2种的6到16个";
	/**
	 * 验证密码:数字,字母及特殊字符至少包含2种的6到16个字符
	 */
	public static void verify(String pwd) throws Exception {
		if (MyStringUtil.isEmpty(pwd)){
			throw new Exception("密码不能为空");
		}

		if (6 > pwd.length() || 16 < pwd.length()){
			throw new Exception("密码需要6到16个字符");
		}

		int count = 0;
		if (isDigit(pwd)){
			count += 1;
		}
		if (isChar(pwd)){
			count += 1;
		}
		if (isSpecialChar(pwd)){
			count += 1;
		}

		if (count<2){
			throw new Exception("数字,字母及特殊字符至少包含2种");
		}

	}



	// 是否包含数字
	public static boolean isDigit(String pwd) {
		boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
		for (int i = 0; i < pwd.length(); i++) {
			if (Character.isDigit(pwd.charAt(i))) {
				isDigit = true;
			}
		}
		return isDigit;
	}

	//是否包含字母
	public static boolean isChar(String pwd){
		boolean isletter = false;//定义一个boolean值，用来表示是否包含字母
		for (int i = 0; i < pwd.length(); i++) {
			if (Character.isLetter(pwd.charAt(i))) {
				isletter = true;
			}
		}
		return isletter;
	}

	public static boolean isSpecialChar(String str) {
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}




}
