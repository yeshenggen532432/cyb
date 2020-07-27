package com.qwb.view.order.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.order.model.OrderBean;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.MyColorUtil;
import com.chiyong.t3.R;

/**
 * 下单列表
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderBean,BaseViewHolder> {

//    public OrderListAdapter() {
//        super(R.layout.x_adapter_order_list);
//    }
    public OrderListAdapter() {
        super(R.layout.x_adapter_order_list2);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        helper.addOnClickListener(R.id.tv_cancel);
        helper.addOnClickListener(R.id.content);

        helper.setText(R.id.tv_khNm, item.getKhNm());// 客户名称
//        helper.setText(R.id.tv_memberNm, item.getMemberNm());// 业务员名称
        helper.setText(R.id.tv_orderNo, "订单号:" + item.getOrderNo());// 订单号
//        helper.setText(R.id.tv_orderZt, "状态:" + item.getOrderZt());// 订单状态（审核，未审核）
        ImageView ivStatus = helper.getView(R.id.iv_status);//作废
        TextView tvKhNm=helper.getView(R.id.tv_khNm);//客户名称
        TextView tvOrderNo=helper.getView(R.id.tv_orderNo);//订单号
        View viewTel=helper.getView(R.id.view_tel);//电话
        TextView tvTel=helper.getView(R.id.tv_tel);//电话
        TextView tvJinE=helper.getView(R.id.tv_jine);//金额
        TextView tvOrderTime=helper.getView(R.id.tv_orderTime);// 订单时间
        TextView tvNum=helper.getView(R.id.tv_num);// 数量
        TextView tvCancel = helper.getView(R.id.tv_cancel);//作废

        //状态
        if (MyStringUtil.eq("已作废", item.getOrderZt())) {
            ivStatus.setImageResource(R.mipmap.ic_status_zf);
        }else if (MyStringUtil.eq("审核", item.getOrderZt())) {
            ivStatus.setImageResource(R.mipmap.ic_status_sp);
        }else{
            ivStatus.setImageResource(R.mipmap.ic_status_wsp);
        }

        if (1 == item.getRedMark()){
            tvKhNm.setTextColor(MyColorUtil.getColorResId(R.color.red));
            tvOrderNo.setTextColor(MyColorUtil.getColorResId(R.color.red));
        }else{
            tvKhNm.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
            tvOrderNo.setTextColor(MyColorUtil.getColorResId(R.color.gray_6));
        }

        //作废
        if (MyStringUtil.eq("未审核", item.getOrderZt()) && MyStringUtil.eq("1", ""+item.getIsMe())){
            tvCancel.setVisibility(View.VISIBLE);
        }else{
            tvCancel.setVisibility(View.GONE);
        }

        //根据公司类型：快消，卖场--跳到不同的页面
        //电话
        if (MyStringUtil.isEmpty(item.getTel())) {
            viewTel.setVisibility(View.GONE);
        } else {
            viewTel.setVisibility(View.VISIBLE);
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
