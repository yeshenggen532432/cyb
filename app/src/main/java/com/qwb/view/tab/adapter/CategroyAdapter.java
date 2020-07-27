package com.qwb.view.tab.adapter;


import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyUtils;
import com.qwb.db.DMessageBean;
import com.xmsx.cnlife.widget.emoji.CCPTextView;
import com.chiyong.t3.R;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 消息分类
 */
public class CategroyAdapter extends BaseQuickAdapter<DMessageBean,BaseViewHolder> {

    private Context context;
    public CategroyAdapter(Context context) {
        super(R.layout.x_adapter_message);
        this.context=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final DMessageBean item) {
        helper.setIsRecyclable(false);//不复用
        //点击事件
        helper.addOnClickListener(R.id.item_agree);

        //赋值
//        helper.setText(R.id.item_title, item.getTitle());
//        helper.setText(R.id.item_subTitle, item.getSubTitle());
//        helper.setText(R.id.item_time, item.getTime());
        TextView tvTitle=helper.getView(R.id.item_title);
        CCPTextView tvSubTitle=helper.getView(R.id.item_subTitle);
        TextView tvTime=helper.getView(R.id.item_time);
        ImageView iv=helper.getView(R.id.item_iv);

        iv.setImageResource(item.getImgResId());
        tvTitle.setText(item.getTitle());
        tvSubTitle.setEmojiText(item.getMsg());
        String formatTime = MyUtils.formatTime2(item.getAddTime());
        if (MyUtils.isEmptyString(formatTime)) {
            tvTime.setText("");
        } else {
            tvTime.setText(formatTime);
        }
        //红点角标
        Badge badge = new QBadgeView(context).bindTarget(iv);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(10, true);
        badge.setGravityOffset(5,5,true);
        badge.setBadgeNumber(item.getCount());
    }

}
