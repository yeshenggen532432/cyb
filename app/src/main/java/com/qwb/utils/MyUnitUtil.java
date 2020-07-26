package com.qwb.utils;

import java.math.BigDecimal;


/**
 * 单位转换
 */
public class MyUnitUtil {

	/**
	 * 大单位数量，转换为大小单位显示
	 * TODO sum:大单位数量
	 */
	public static String  maxMinUnit(String maxUnit, String minUnit, String hsNum, String sum) {
		String s = "";
		try {
			BigDecimal hsNumD = new BigDecimal(1);
			if (MyStringUtil.isNumber(hsNum)){
				hsNumD = new BigDecimal(hsNum);
			}
			BigDecimal sumD = new BigDecimal(0);
			if (MyStringUtil.isNumber(sum)){
				sumD = new BigDecimal(sum);
			}

			//负数：取绝对值
			if(sumD.doubleValue() < 0){
				s += "-";
			}
			sumD = new BigDecimal(Math.abs(sumD.doubleValue()));

			int max = 0, min = 0;

			//大小单位都为空
			if(MyStringUtil.isEmpty(maxUnit) && MyStringUtil.isEmpty(minUnit)){
				s = sum;
			}else{
				if (MyStringUtil.isNotEmpty(maxUnit)){
					max = MyDoubleUtils.intFloor(sumD.doubleValue());
					if (max > 0) {
						s += max + maxUnit;
					}
				}

				if (MyStringUtil.isNotEmpty(minUnit)){
					double minD = ((MyDoubleUtils.subtract(sumD, max)) * hsNumD.doubleValue());
					min = MyDoubleUtils.intRound(minD);
					if (min > 0) {
						s += min + minUnit;
					}
				}
			}
		} catch (Exception e) {
		} finally {
			return s;
		}
	}


	/**
	 * 大单位数量，转换为大小单位显示
	 * TODO sum:大单位数量
	 */
	public static String  maxMinUnit2(String maxUnit, String minUnit, String hsNum, String sum) {
		String s = "";
		try {
			BigDecimal hsNumD = new BigDecimal(1);
			if (MyStringUtil.isNumber(hsNum)){
				hsNumD = new BigDecimal(hsNum);
			}
			BigDecimal sumD = new BigDecimal(0);
			if (MyStringUtil.isNotEmpty(sum)){
				sumD = new BigDecimal(sum);
			}

			//负数：取绝对值
			if(sumD.doubleValue() < 0){
				s += "-";
			}
			sumD = new BigDecimal(Math.abs(sumD.doubleValue()));

			int max = 0, min = 0;
			max = MyDoubleUtils.intFloor(sumD.doubleValue());
			if (max > 0) {
				if (MyStringUtil.isNotEmpty(maxUnit)){
					s += max + maxUnit;
				}else{
					//没有大单位用“/”代替
					s += max + "/";
				}
			}
			double minD = ((MyDoubleUtils.subtract(sumD, max)) * hsNumD.doubleValue());
			min = MyDoubleUtils.intRound(minD);
			if (min > 0) {
				if (MyStringUtil.isNotEmpty(minUnit)){
					s += min + minUnit;
				}else{
					//没有小单位用“/”代替
					s += min + "/";
				}
			}
		} catch (Exception e) {
		} finally {
			return s;
		}
	}


	/**
	 * 小单位数量转为大单位数量(除法)
	 */
	public static String minCountToMaxCount(String hsNum, String minCount){
		String s = "";
		try {
			BigDecimal hsNumD = new BigDecimal(1);
			if (MyStringUtil.isNumber(hsNum)) {
				hsNumD = new BigDecimal(hsNum);
			}
			if (MyStringUtil.isNumber(minCount)) {
				s = MyMathUtils.clearZero(MyMathUtils.divideByScale(new BigDecimal(minCount), hsNumD, 10));
			}
		}catch (Exception e){
			s = minCount;
		}
		return s;
	}

	/**
	 * 大单位数量转为小单位数量（乘法）
	 */
	public static String maxCountToMinCount(String hsNum, String maxCount){
		String s = "";
		try {
			BigDecimal hsNumD = new BigDecimal(1);
			if (MyStringUtil.isNumber(hsNum)) {
				hsNumD = new BigDecimal(hsNum);
			}
			if (MyStringUtil.isNumber(maxCount)) {
				s = MyMathUtils.clearZero(MyMathUtils.multiply(new BigDecimal(maxCount), hsNumD));
			}
		}catch (Exception e){
			s = maxCount;
		}
		return s;
	}





}
