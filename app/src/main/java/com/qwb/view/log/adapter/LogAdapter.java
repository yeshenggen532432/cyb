package com.qwb.view.log.adapter;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.log.model.RizhiListBean;
import com.xmsx.qiweibao.R;

/**
 * 文 件 名: LogAdapter(工作汇报-列表：“我收到的”，“我发出的”)
 */
public class LogAdapter extends BaseQuickAdapter<RizhiListBean.RizhiList,BaseViewHolder> {

    private Context context;
    private int type;
    public LogAdapter(Context context,int type) {
        super(R.layout.x_adapter_look_log);
        this.context=context;
        this.type=type;
    }

    @Override
    protected void convert(BaseViewHolder helper, RizhiListBean.RizhiList item) {

        helper.setText(R.id.tv_memNm, item.getMemberNm());
        helper.setText(R.id.tv_time, item.getFbTime());
        helper.setText(R.id.edit_gzNr,  item.getGzNr());
        helper.setText(R.id.edit_gzZj,  item.getGzZj());
        helper.setText(R.id.edit_gzJh,  item.getGzJh());
        helper.setText(R.id.edit_gzBz,  item.getGzBz());
        TextView img_memhead=helper.getView(R.id.img_memhead);
        img_memhead.setText(item.getMemberNm());
        switch (type) {
            case 1:
                img_memhead.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_oval_juhuang_50));
                break;
            case 2:
                img_memhead.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shape_oval_zise_50));
                break;
        }

        TextView tv_tpye=helper.getView(R.id.tv_tpye);
        TextView tv_gzNr=helper.getView(R.id.tv_gzNr);
        TextView tv_gzZj=helper.getView(R.id.tv_gzZj);
        TextView tv_gzJh=helper.getView(R.id.tv_gzJh);
        TextView tv_gzBz=helper.getView(R.id.tv_gzBz);
        RelativeLayout rl_gzBz=helper.getView(R.id.rl_gzBz);
        int tp = item.getTp();
        switch (tp) {
            case 1:
                tv_tpye.setText("日报");
                tv_gzNr.setText("今日完成工作：");
                tv_gzZj.setText("　未完成工作：");
                tv_gzJh.setText("　需调协工作：");
                rl_gzBz.setVisibility(View.GONE);
                break;
            case 2:
                tv_tpye.setText("周报");
                tv_gzNr.setText("本周完成工作：");
                tv_gzZj.setText("本周工作总结：");
                tv_gzJh.setText("下周工作计划：");
                tv_gzBz.setText("需调协与帮助：");
                rl_gzBz.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_tpye.setText("月报");
                tv_gzNr.setText("本月工作内容：");
                tv_gzZj.setText("本月工作总结：");
                tv_gzJh.setText("下月工作计划：");
                tv_gzBz.setText("需调协与支持：");
                rl_gzBz.setVisibility(View.VISIBLE);
                break;
        }
    }
}
