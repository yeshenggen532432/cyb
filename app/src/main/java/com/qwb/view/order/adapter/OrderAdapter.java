package com.qwb.view.order.adapter;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.order.model.QueryDhorderBean;
import com.xmsx.qiweibao.R;

/**
 * 下单列表
 */
public class OrderAdapter extends BaseQuickAdapter<QueryDhorderBean.Rows,BaseViewHolder> {

    public OrderAdapter() {
        super(R.layout.x_adapter_order_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryDhorderBean.Rows item) {
        helper.addOnClickListener(R.id.tv_orderZt);
        helper.addOnClickListener(R.id.content);

        helper.setText(R.id.tv_khNm, item.getKhNm());// 客户名称
        helper.setText(R.id.tv_memberNm, item.getMemberNm());// 业务员名称
        helper.setText(R.id.tv_orderNo, "订单号:" + item.getOrderNo());// 订单号
        helper.setText(R.id.tv_orderZt, "状态:" + item.getOrderZt());// 订单状态（审核，未审核）
        LinearLayout llNum=helper.getView(R.id.ll_num);
        LinearLayout llDdje=helper.getView(R.id.ll_ddje);
        TextView tvTel=helper.getView(R.id.tv_tel);//电话
        TextView tvJinE=helper.getView(R.id.tv_jine);//金额
        TextView tvOrderTime=helper.getView(R.id.tv_orderTime);// 订单时间
        TextView tvNum=helper.getView(R.id.tv_num);// 数量

        //根据公司类型：快消，卖场--跳到不同的页面
        String tpNmStr = SPUtils.getSValues("tpNm");
        llNum.setVisibility(View.VISIBLE);
        llDdje.setVisibility(View.VISIBLE);
        if("卖场".equals(tpNmStr)){
            llNum.setVisibility(View.INVISIBLE);
            llDdje.setVisibility(View.INVISIBLE);
        }
        //电话
        if (!MyUtils.isEmptyString(item.getTel())) {
            tvTel.setVisibility(View.GONE);
        } else {
            tvTel.setText(item.getTel());
        }
        //金额
        if (MyUtils.isEmptyString(item.getCjje())) {
            tvJinE.setText("0");
        } else {
            tvJinE.setText(item.getCjje());
        }
        // 数量
        if (!MyUtils.isEmptyString(item.getDdNum())) {
            tvNum.setText(item.getDdNum());
        }
        // 时间拼接
        StringBuffer sb=new StringBuffer();
        sb.append("下单日期:");
        if (!MyUtils.isEmptyString(item.getOddate())) {
            String date = item.getOddate().substring(5, item.getOddate().length());
            sb.append(date);
        }
        if (!MyUtils.isEmptyString(item.getOdtime())) {
            String time = item.getOdtime().substring(0, 5);
            sb.append(" "+time);
        }
        tvOrderTime.setText(sb.toString());
    }
}
