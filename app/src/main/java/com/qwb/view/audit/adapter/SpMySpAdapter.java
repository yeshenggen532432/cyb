package com.qwb.view.audit.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.audit.model.ShenPiIShenPiBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.CircleImageView;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.chiyong.t3.R;

/**
 * 审批：我审批的
 */
public class SpMySpAdapter extends BaseQuickAdapter<ShenPiIShenPiBean.ShenPiIShenPiItemBean, BaseViewHolder> {
    public SpMySpAdapter() {
        super(R.layout.x_adapter_shen_pi_my_sp);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShenPiIShenPiBean.ShenPiIShenPiItemBean item) {
        TextView tv_liucheng = helper.getView(R.id.tv_liucheng);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_title = helper.getView(R.id.tv_title);
        CircleImageView iv_head = helper.getView(R.id.iv_head);

        tv_title.setText(item.getTitle());
        tv_time.setText(MyUtils.formatTime2(item.getCheckTime()));
        MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + item.getMemberHead(), iv_head);
        if ("2".equals(item.getIsOver())) {
            // 审批流程未完的状态
            tv_liucheng.setText("等待 " + item.getCheckNm() + " 审批");
        } else {
            // 审批流程已经完 各种状态
            /**
             * 2 表示还在审批中 1 表示审批完成 1-1 完成 1-2 拒绝 1-3 撤销
             */
            String isOver = item.getIsOver();
            if ("1-3".equals(isOver)) {
                tv_liucheng.setText("审批完成（撤销）");
            } else if ("1-2".equals(isOver)) {
                tv_liucheng.setText("审批完成（拒绝）");
            } else {
                tv_liucheng.setText("审批完成（同意）");
            }
        }


    }
}
