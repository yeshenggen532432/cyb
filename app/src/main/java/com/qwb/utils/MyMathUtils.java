package com.qwb.utils;

import android.support.v4.math.MathUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal：加，减，乘，除，余
 */

public class MyMathUtils {

    public MyMathUtils() {
    }

    /**
     * 除法（保留3个小数点）
     */
    public static BigDecimal divideByScale3(BigDecimal dividend, BigDecimal divisor) {
        return divideByScale((BigDecimal)dividend, (BigDecimal)divisor, 3);
    }
    /**
     * 除法（保留3个小数点）
     */
    public static BigDecimal divideByScale3(BigDecimal dividend, Number divisor) {
        return divideByScale3(dividend, getBigDecimalByType(divisor));
    }
    /**
     * 除法（保留scale个小数点）
     */
    public static BigDecimal divideByScale(Number dividend, Number divisor, int scale) {
        return divideByScale(getBigDecimalByType(dividend), getBigDecimalByType(divisor), scale);
    }

    public static BigDecimal divideByScale(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (divisor.doubleValue() == 0.0D) {
            throw new IllegalArgumentException("除数不能为0");
        } else {
            return dividend.divide(divisor, scale, 4);
        }
    }

    /**
     * 取余
     */
    public static BigDecimal remainder(BigDecimal dividend, BigDecimal divisor) {
        return dividend.remainder(divisor);
    }
    /**
     * 取余
     */
    public static BigDecimal remainder(BigDecimal dividend, Number divisor) {
        return remainder(dividend, toBigDecimal(divisor));
    }
    /**
     * 加法
     */
    public static BigDecimal add(Number val1, Number val2) {
        return getBigDecimalByType(val1).add(getBigDecimalByType(val2));
    }

    /**
     * 乘法
     */
    public static BigDecimal multiply(Number val1, Number val2) {
        return getBigDecimalByType(val1).multiply(getBigDecimalByType(val2));
    }
    /**
     * 乘法
     */
    public static BigDecimal multiply(Number val1, Number val2, int scale) {
        return setScale(multiply(val1, val2), scale);
    }
    /**
     * 减法
     */
    public static BigDecimal subtract(Number minuend, Number subtractor) {
        return getBigDecimalByType(minuend).subtract(getBigDecimalByType(subtractor));
    }

    public static BigDecimal setScale(BigDecimal val) {
        return setScale(val, 2);
    }

    public static BigDecimal setScale(BigDecimal val, int scale) {
        return val.setScale(scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal getBigDecimalByType(Number val) {
        if (val instanceof BigDecimal) {
            return (BigDecimal)val;
        } else if (val instanceof Short) {
            return BigDecimal.valueOf((long)val.shortValue());
        } else if (val instanceof Integer) {
            return BigDecimal.valueOf((long)val.intValue());
        } else if (val instanceof Long) {
            return BigDecimal.valueOf(val.longValue());
        } else {
            return val instanceof Double ? BigDecimal.valueOf(val.doubleValue()) : new BigDecimal(val.toString());
        }
    }

    public static boolean valid(Number data) {
        if (data == null) {
            return false;
        } else if (data instanceof Integer) {
            return data.intValue() > 0;
        } else if (data instanceof Long) {
            return data.longValue() > 0L;
        } else {
            return data.doubleValue() > 0.0D;
        }
    }

    public static BigDecimal toBigDecimal(Object val) {
        if (val == null) {
            return BigDecimal.ZERO;
        } else {
            return val instanceof BigDecimal ? (BigDecimal)val : BigDecimal.valueOf(toDouble(val));
        }
    }

    public static Double toDouble(Object val) {
        if (val == null) {
            return 0.0D;
        } else {
            try {
                return Double.valueOf(val.toString());
            } catch (Exception var2) {
                return 0.0D;
            }
        }
    }

    /**
     * 向上取整
     */
    public static long getLongUp(BigDecimal bd){
        return bd.setScale( 0, BigDecimal.ROUND_UP ).longValue();
    }

    /**
     * 向下取整
     */
    public static long getLongDown(BigDecimal bd){
        return bd.setScale( 0, BigDecimal.ROUND_DOWN ).longValue();
    }

    /**
     * 四舍五入
     */
    public static long getLongHelfUp(BigDecimal bd){
        return bd.setScale( 0, BigDecimal.ROUND_HALF_UP ).longValue();
    }

    /**
     * 去掉尾号0
     */
    public static String clearZero(BigDecimal bd){
        String s = "";
        try{
            if (bd != null){
                s =  bd .stripTrailingZeros().toPlainString();
            }
        }catch (Exception e){}finally {
            return s;
        }
    }

    /**
     * 取绝对值
     */
    public static String getABS(String s){
        try {
            if (MyStringUtil.isNotEmpty(s)){
                s = "" + Math.abs(Double.valueOf(s));
            }
        }catch (Exception e){

        }
        return s;
    }



    public static void main(String[] args) {
        System.out.println(add(2.5D, 3.7D).setScale(2, RoundingMode.HALF_UP));
        System.out.println(multiply(3, 5));
        System.out.println(subtract(210, 0.033D));
        System.out.println(add(0.01F, 0.05F));
        System.out.println(add(3, 5));
        BigDecimal b1 = setScale(new BigDecimal(0.1D), 5);
        System.out.println(b1);
        BigDecimal b2 = setScale(new BigDecimal(0.10000000149011612D), 5);
        System.out.println(b1.compareTo(b2));
    }
}
