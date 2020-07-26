package com.qwb.view.car.adapter;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deadline.statebutton.StateButton;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.view.car.model.CarRecMastBean;
import com.qwb.view.order.model.QueryDhorderBean;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 车销订单列表
 */
public class CarOrderAdapter extends BaseQuickAdapter<QueryDhorderBean.Rows, BaseViewHolder> {
    private List<CarRecMastBean> carRecMastList = new ArrayList<>();

    public CarOrderAdapter() {
        super(R.layout.x_adapter_car_order);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryDhorderBean.Rows item) {
        helper.addOnClickListener(R.id.item_sb_zc);
        helper.addOnClickListener(R.id.content).addOnClickListener(R.id.tv_cancel);

        helper.setText(R.id.tv_khNm, item.getKhNm());// 客户名称
        helper.setText(R.id.tv_memberNm, item.getMemberNm());// 业务员名称
        helper.setText(R.id.tv_orderNo, "订单号:" + item.getOrderNo());// 订单号
        helper.setText(R.id.tv_orderZt, "状态:" + item.getOrderZt());// 订单状态（审核，未审核）
        LinearLayout llNum = helper.getView(R.id.ll_num);
        LinearLayout llDdje = helper.getView(R.id.ll_ddje);
        TextView tvTel = helper.getView(R.id.tv_tel);//电话
        TextView tvJinE = helper.getView(R.id.tv_jine);//金额
        TextView tvOrderTime = helper.getView(R.id.tv_orderTime);// 订单时间
        TextView tvNum = helper.getView(R.id.tv_num);// 数量
        StateButton sbZc = helper.getView(R.id.item_sb_zc);// 收款
        TextView tvCancel = helper.getView(R.id.tv_cancel);// 作废

        llNum.setVisibility(View.VISIBLE);
        llDdje.setVisibility(View.VISIBLE);
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
        StringBuffer sb = new StringBuffer();
        sb.append("下单日期:");
        if (!MyUtils.isEmptyString(item.getOddate())) {
            String date = item.getOddate().substring(5, item.getOddate().length());
            sb.append(date);
        }
        if (!MyUtils.isEmptyString(item.getOdtime())) {
            String time = item.getOdtime().substring(0, 5);
            sb.append(" " + time);
        }
        tvOrderTime.setText(sb.toString());

        //去收款
        sbZc.setVisibility(View.GONE);
        if(1 == item.getIsMe() && !isContain(item.getId())){
            sbZc.setVisibility(View.VISIBLE);
        }

        //作废
        tvCancel.setVisibility(View.GONE);
        if(1 == item.getIsMe() && MyStringUtil.eq("未审核", item.getOrderZt())){
            tvCancel.setVisibility(View.VISIBLE);
        }
    }

    public boolean isContain(int id){
        for (CarRecMastBean bean : carRecMastList){
            if(bean.getOrderId().intValue() == id){
                return true;
            }
        }
        return false;
    }

    public List<CarRecMastBean> getCarRecMastList() {
        return carRecMastList;
    }

    public void setCarRecMastList(List<CarRecMastBean> carRecMastList) {
        this.carRecMastList = carRecMastList;
    }
}
