package com.qwb.utils.push;

import com.qwb.application.MyApp;

/**
 *  极光推送的工具类
 */

public class MyPushUtil {

    //设置别名
    public static void setAlias(String phone){
//        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, phone));
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = TagAliasOperatorHelper.ACTION_SET;
        tagAliasBean.alias = phone;
        TagAliasOperatorHelper.sequence++;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(MyApp.getI(),TagAliasOperatorHelper.sequence,tagAliasBean);
    }
}
