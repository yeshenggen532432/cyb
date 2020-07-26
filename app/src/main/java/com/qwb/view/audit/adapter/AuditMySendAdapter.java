package com.qwb.view.audit.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.audit.model.ShenPiIsendBean;
import com.xmsx.cnlife.widget.CircleImageView;
import com.qwb.utils.MyUtils;
import com.xmsx.qiweibao.R;

/**
 * 审批：我发送的
 */
public class AuditMySendAdapter extends BaseQuickAdapter<ShenPiIsendBean.ShenPiIsendItemBean, BaseViewHolder> {

    public AuditMySendAdapter() {
        super(R.layout.x_adapter_shen_pi_my_send);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShenPiIsendBean.ShenPiIsendItemBean item) {

        TextView tv_liucheng = helper.getView(R.id.tv_liucheng);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_title = helper.getView(R.id.tv_title);
        CircleImageView iv_head = helper.getView(R.id.iv_head);

        tv_title.setText(item.getTitle());
        tv_time.setText(MyUtils.formatTime2(item.getStime()));
        switch (Integer.valueOf(item.getAuditTp())) {
            case 1:
                iv_head.setImageResource(R.drawable.qingjia);
                break;
            case 2:
                iv_head.setImageResource(R.drawable.baoxiao_);
                break;
            case 3:
                iv_head.setImageResource(R.drawable.chuchai_);
                break;
            case 4:
                iv_head.setImageResource(R.drawable.wuping_);
                break;
            case 5:
                iv_head.setImageResource(R.drawable.tongyong_);
                break;
            case 6:
                iv_head.setImageResource(R.drawable.icon_shenpi_private);
                break;
            case 7:
                iv_head.setImageResource(R.drawable.icon_shenpi_public);
                break;
        }

        if ("2".equals(item.getIsOver())) {
            tv_liucheng.setText("等待 " + item.getCheckNm() + " 审批");
        } else {
            // 审批流程已经完 各种状态
            // 审批流程已经完 各种状态
            //表示还在审批中 1 表示审批完成 1-1 完成 1-2 拒绝 1-3 撤销
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
