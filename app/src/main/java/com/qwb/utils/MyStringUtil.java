package com.qwb.utils;

import android.text.TextUtils;

import com.qwb.view.checkstorage.model.StkCheckWareBean;
import com.qwb.view.member.model.BuMenListBean;
import com.qwb.view.step.model.ShopInfoBean;

import org.apache.http.impl.cookie.DateUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * String工具类
 */
public class MyStringUtil {

    /**
     * 如果该字符串为空 则return掉
     */
    public static boolean isEmpty(String s) {
        if (TextUtils.isEmpty(s)) {
            return true;
        }
        if (TextUtils.isEmpty(s.trim())) {
            return true;
        }
        if ("null".equals(s.trim())) {
            return true;
        }
        if ("NULL".equals(s.trim())) {
            return true;
        }
        if ("".equals(s.trim())) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }


    /**
     * 判断手机格式是否正确
      */
    public static boolean isNotMobile(String mobiles) {
        if (isEmpty(mobiles)){
            return true;
        }
        if (mobiles.trim().length() == 11) {
            String subSequence = mobiles.subSequence(0, 1).toString();
            if ("1".equals(subSequence)) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * 把byte[]转为String
     */
    public static String getByteToStr(byte[] by){
        String str="";
        for (int i = 0; i < by.length; i++) {
            char b=(char)by[i];
            str+=b;
        }
        return str;
    }

    /**
     * 拼接逗号
     */
    public static String getPjDh(String oldStr, String s){
        if(isEmpty(oldStr)){
            oldStr = s;
        }else{
            oldStr += "," + s;
        }
        return oldStr;
    }

    /**
     * 去掉尾部逗号
     */
    public static String clearEndComma(String s){
        if(isEmpty(s)){
            return "";
        }else{
            if(s.endsWith(",")){
                s.substring(0, s.length()-2);
            }
        }
        return s;
    }

    /**
     * 拼接datas商品wareIds(过滤checkWareList存在的wareIds)
     */
    public static String getWareIds(List<ShopInfoBean.Data> datas, List<StkCheckWareBean> checkWareList){
        String s = null;
        if(MyCollectionUtil.isNotEmpty(datas)){
            for (ShopInfoBean.Data data: datas) {
                boolean flag = true;
                int wareId = data.getWareId();
                if (MyCollectionUtil.isNotEmpty(checkWareList)){
                    for (StkCheckWareBean checkWare:checkWareList) {
                        if(String.valueOf(wareId).equals(String.valueOf(checkWare.getWareId() + ""))){
                            flag = false;
                            break;
                        }
                    }
                }
                if (flag){
                    if (isEmpty(s)){
                        s = "" + data.getWareId();
                    }else{
                        s += "," + data.getWareId();
                    }
                }
            }
        }
        return s;
    }


    /**
     * 获取审批人ids(执行人前后拼接逗号)
     */
    public static String getMemberIds(List<BuMenListBean.MemberBean> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            sb.append(list.get(0).getMemberId());
            for (int i = 1; i < list.size(); i++) {
                sb.append("," + list.get(i).getMemberId());
            }
        }
        return sb.toString();
    }

    /**
     * 字符串添加字符：如 s + "/"; s + ","
     */
    public static String append(String s, String append) {
        if (isNotEmpty(s)) {
            s += append;
        }else{
            s = "";
        }
        return s;
    }

    /**
     * 去掉末尾指定字符
     */
    public static String clearEndChar(String s, String c) {
        if (isNotEmpty(s) && s.endsWith(c)) {
            s = s.substring(0, s.length() -1);
        }
        return s;
    }

    /**
     * 替换SpUtils中用户id和公司id
     */
    public static String replaceUserIdAndCompanyId(String key){
        if (isNotEmpty(key)) {
            int index = key.indexOf("&");
            if (index != -1){
                key = key.substring(0, index + 1) + SPUtils.getID()+"_"+SPUtils.getCompanyId();
            }
        }
        return key;
    }


    /**
     * 判断是否为数字（包括小数点）
     */
    public static boolean isNumber(String str){
        try {
            if(isEmpty(str)){
                return false;
            }
            Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]*");
            return pattern.matcher(str).matches();
        }catch (Exception e){
        }
        return false;
    }

    /**
     * 是否相等
     */
    public static boolean eq(Object o1, Object o2){
        if (o1 == null){
            return false;
        }
        if (o2 == null){
            return false;
        }
        return o1.toString().equals(o2.toString());
    }

    /**
     * 添加 23:59:59
     */
    public static String appendHMS(String s){
       if (isNotEmpty(s)) {
           s += " 23:59:59";
        }
        return s;
    }

    /**
     * 把“0E-10”转为“0”
     */
    public static String getString(String s){
        if (isNotEmpty(s)) {
            if (eq(s,"0E-10")){
                s = "0";
            }
        }
        return s;
    }

    /**
     * 是否为日期格式
     */
    private static String[] parsePatterns = {"yyyy-MM-dd","yyyy年MM月dd日",
            "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy/MM/dd",
            "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyyMMdd"};
    public static boolean isValidDate(String str) {
        if (isEmpty(str)) {
            return false;
        }
        try {
            DateUtils.parseDate(str, parsePatterns);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


}
