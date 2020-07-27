package com.qwb.view.base.adapter;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.base.model.ApplyBean;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 文 件 名: 首页tab
 * 修改时间：
 * 修改备注：
 */
public class SubApplyAdapter extends BaseQuickAdapter<ApplyBean,BaseViewHolder> {

    private Context context;
    public SubApplyAdapter(Context context) {
        super(R.layout.x_adapter_sub_apply);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyBean item) {
        helper.setIsRecyclable(false);//不复用
        //点击事件
//        helper.addOnClickListener(R.id.tv_default);

        //赋值
        helper.setText(R.id.tv_home_tab, item.getApplyName());
        ImageView iv = helper.getView(R.id.iv_home_tab);
        String applyIcon = item.getApplyIcon();
        if(!MyStringUtil.isEmpty(applyIcon)){
            MyGlideUtil.getInstance().displayImageSquere(applyIcon, iv);
        }else{
            if("添加".equals(item.getApplyName())){
                iv.setImageResource(R.mipmap.home_tab_tj);
            }else{
                iv.setImageResource(R.mipmap.home_tab_qycpk);
            }
        }

        View layout = helper.getView(R.id.iv_layout);
        if("xx".equals(item.getApplyCode())){
            //红点角标
            Badge badge = new QBadgeView(context).bindTarget(layout);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
            badge.setBadgeTextSize(10, true);
            badge.setBadgeNumber(item.getCount());
        }
        if(ConstantUtils.Apply.GG_NEW.equals(item.getApplyCode())){
            //红点角标
            Badge badge = new QBadgeView(context).bindTarget(layout);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
            badge.setBadgeTextSize(10, true);
            badge.setBadgeNumber(item.getCount());
        }
    }
}
