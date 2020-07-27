package com.qwb.view.work.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.work.model.WorkSubBean;
import com.chiyong.t3.R;

/**
 * 考勤列表
 */
public class WorkListAdapter extends BaseQuickAdapter<WorkSubBean, BaseViewHolder> {
    private Context context;

    public WorkListAdapter(Context context) {
        super(R.layout.x_adapter_qiandao);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WorkSubBean item) {
        helper.addOnClickListener(R.id.tv_sb);

        TextView tv_line_top = helper.getView(R.id.tv_line_top);
        TextView tv_sb = helper.getView(R.id.tv_sb);
        ImageView iv_sb = helper.getView(R.id.iv_icon_sb);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_checkTime = helper.getView(R.id.tv_checkTime);

        String checkTime = item.getCheckTime();
        tv_checkTime.setText(checkTime);

        //拼接：员工名称+时间
        String checkTimeStr = item.getMemberName() + item.getCheckTime().substring(0, 10);
        tv_time.setText(item.getMemberName());
        tv_sb.setText(item.getLocationup());
        String tp = item.getTp();
        if ("1-1".equals(tp)) {
            iv_sb.setImageDrawable(context.getResources().getDrawable(R.drawable.qiandao_ico_sb));
            tv_sb.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.location_gray, 0, 0, 0);
        } else {
            iv_sb.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.qiandao_ico_xb));
            tv_sb.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.location_gray, 0, 0, 0);
        }

        int position = helper.getAdapterPosition();
        //拼接：员工名称+时间，如果一致隐藏
        if (position != 0) {//上一行
            WorkSubBean kaoqianItemBean_shang = getData().get(position - 1);
            String checkTimeStr_shang = kaoqianItemBean_shang.getMemberName() + kaoqianItemBean_shang.getCheckTime().substring(0, 10);
            if (checkTimeStr.equals(checkTimeStr_shang)) {//如果当行与上行的日期一样，要隐藏
                tv_time.setVisibility(View.GONE);
            } else {
                tv_time.setVisibility(View.VISIBLE);
                tv_line_top.setVisibility(View.INVISIBLE);
            }
        }

        if (position == 0) {
            tv_time.setVisibility(View.VISIBLE);
            tv_line_top.setVisibility(View.INVISIBLE);
        } else {
            tv_line_top.setVisibility(View.VISIBLE);
        }
    }
}
