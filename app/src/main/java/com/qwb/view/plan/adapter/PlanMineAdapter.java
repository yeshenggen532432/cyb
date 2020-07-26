package com.qwb.view.plan.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.plan.model.PlanBean;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.MyUtils;
import com.xmsx.qiweibao.R;

/**
 * 计划拜访--我的拜访
 */
public class PlanMineAdapter extends BaseQuickAdapter<PlanBean,BaseViewHolder> {
    private String type = "1";
    private String date = MyTimeUtils.getTodayStr();

    public PlanMineAdapter() {
        super(R.layout.x_adapter_plan_mine);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlanBean item) {
        //点击事件
        helper.addOnClickListener(R.id.tv_state);
        helper.addOnClickListener(R.id.tv_wancheng);

        //赋值
        TextView tv_khNm = helper.getView(R.id.tv_khNm);
        TextView tv_address = helper.getView(R.id.tv_address);
        TextView tv_state = helper.getView(R.id.tv_state);
        ImageView iv_wc = helper.getView(R.id.iv_wc);
        TextView tv_member = helper.getView(R.id.tv_member);

        tv_khNm.setText(item.getKhNm());
        tv_address.setText(item.getAddress());

        if ("2".equals(type)) {
            tv_member.setVisibility(View.VISIBLE);
            tv_member.setText("业代：" + item.getMemberNm() + "/" + item.getBranchName());
        } else  {
            tv_member.setVisibility(View.GONE);
        }

        // 是否完成（1是；2否）
        int isWc = item.getIsWc();
        if (1 == isWc) {
            tv_state.setVisibility(View.GONE);
            iv_wc.setVisibility(View.VISIBLE);
        } else if (2 == isWc) {
            if (MyUtils.DateBefore(date)) {
                // 昨天
                if ("1".equals(type)) {
                    tv_state.setVisibility(View.VISIBLE);
                    tv_state.setText("补拜访");
                } else if ("2".equals(type)) {
                    tv_state.setVisibility(View.GONE);
                }
                iv_wc.setVisibility(View.GONE);
            } else if (date.equals(MyTimeUtils.getTodayStr())) {
                //今天
                if ("1".equals(type)) {
                    tv_state.setVisibility(View.VISIBLE);
                    tv_state.setText("开始");
                } else if ("2".equals(type)) {
                    tv_state.setVisibility(View.GONE);
                }
                iv_wc.setVisibility(View.GONE);
            }else{
                //明天
                tv_state.setVisibility(View.GONE);
                iv_wc.setVisibility(View.GONE);
            }
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
