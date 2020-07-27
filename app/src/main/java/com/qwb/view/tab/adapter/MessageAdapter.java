package com.qwb.view.tab.adapter;


import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deadline.statebutton.StateButton;
import com.qwb.utils.MyDataUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.db.DMessageBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.emoji.CCPTextView;
import com.chiyong.t3.R;

import cn.droidlover.xdroidmvp.log.XLog;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 消息
 */
public class MessageAdapter extends BaseQuickAdapter<DMessageBean,BaseViewHolder> {

    private Context context;
    public MessageAdapter(Context context) {
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
        StateButton btnAgree=helper.getView(R.id.item_agree);
//        iv.setImageResource(item.getImgRes());
//
        // 是否已读
        String isRead = item.getIsRead();
        if(!TextUtils.isEmpty(isRead) && "1".equals(isRead)){
        }else{
            //红点角标
            Badge badge = new QBadgeView(context).bindTarget(iv);
            badge.setBadgeGravity(Gravity.TOP | Gravity.END);
            badge.setBadgeTextSize(10, true);
            badge.setGravityOffset(5,5,true);
            badge.setBadgeText("");
            badge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                @Override
                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                    try {
                        if (dragState == STATE_SUCCEED) {
                            //改变数据库已读状态
                            int count =  MyDataUtils.getInstance().updateMessage(item);
                            if(count > 0){
                                //刷新分类的红点数量
                            }
                            XLog.e("修改数据",""+count);
                        }
                    }catch (Exception e){
                    }

                }
            });
        }

        final String tp = item.getType();
        switch (Integer.valueOf(tp)) {
            case 12:
            case 13:
            case 32:// 计划拜访
                tvTitle.setText(item.getBelongMsg());
                tvSubTitle.setText(item.getMsg());
                iv.setImageResource(R.drawable.sys_ico);
                break;
            case 34:// 日志
            case 40:// 日志-日报，周报，月报评论
            case 100://商城订单
            case 26://申请入职
                tvTitle.setText(item.getBelongName());
                tvSubTitle.setText(item.getMsg());
                iv.setImageResource(R.drawable.sys_ico);
                break;
            default:
                // 在这里处理的有：23被移除圈
                tvTitle.setText(item.getMemberName());
                tvSubTitle.setEmojiText(item.getMsg());
                //Glide设置图片
                MyGlideUtil.getInstance().setCircle(iv,Constans.IMGROOTHOST + item.getMemberHead() );
                break;
        }

        String formatTime = MyUtils.formatTime2(item.getAddTime());
        if (MyUtils.isEmptyString(formatTime)) {
            tvTime.setText("");
        } else {
            tvTime.setText(formatTime);
        }

        switch (Integer.valueOf(tp)) {
            case 8:
            case 16:
            case 18:
            case 19:// 19好友申请
			case 41:// 转让客户-列表（不要“同意”按钮）
//			case 42:// 转让客户-对方同意或拒绝-所有不要是否“是否同意”按钮(只展示就行)
            case 26:
                if (1 == item.getIsact()) {
                    btnAgree.setEnabled(false);
                    btnAgree.setVisibility(View.VISIBLE);
                    btnAgree.setText("已同意");
                } else if (2 == item.getIsact()) {
                    btnAgree.setEnabled(false);
                    btnAgree.setVisibility(View.VISIBLE);
                    btnAgree.setText("已拒绝");
                } else {
                    btnAgree.setEnabled(true);
                    btnAgree.setVisibility(View.VISIBLE);
                    btnAgree.setText("是否同意");
                }
                break;

            default:
                btnAgree.setVisibility(View.GONE);
                break;
        }
    }

}
