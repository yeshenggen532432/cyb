package com.qwb.view.delivery.adapter;



import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deadline.statebutton.StateButton;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.utils.SPUtils;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

/**
 * 文 件 名: DeliveryAdapter
 */
public class Delivery3Adapter extends BaseQuickAdapter<DeliveryBean, BaseViewHolder> {

    public Delivery3Adapter() {
        super(R.layout.x_adapter_delivery);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeliveryBean item) {
        //子view添加点击事件
        helper.addOnClickListener(R.id.btn_js);
        helper.addOnClickListener(R.id.btn_wc);
        helper.addOnClickListener(R.id.btn_zz);
        helper.addOnClickListener(R.id.right);

        helper.setText(R.id.tv_orderNo, "配送单号:" + item.getBillNo());
        helper.setText(R.id.tv_khNm, item.getKhNm());// 客户名称
        helper.setText(R.id.tv_time, item.getOutDate());//"配送日期:" +
        helper.setText(R.id.tv_num, "" + item.getDdNum());
        helper.setText(R.id.tv_amount, "" + item.getDisAmt1());
        helper.setText(R.id.tv_car, item.getVehNo());

        TextView tv_tel = helper.getView(R.id.tv_tel);
        TextView tv_address = helper.getView(R.id.tv_address);
        StateButton btn_js = helper.getView(R.id.btn_js);
        StateButton btn_wc = helper.getView(R.id.btn_wc);
        StateButton btn_zz = helper.getView(R.id.btn_zz);

        String tel = item.getTel();
        String address = item.getAddress();
        if(MyStringUtil.isEmpty(tel)){
            tv_tel.setVisibility(View.GONE);
        }else{
            tv_tel.setVisibility(View.VISIBLE);
            tv_tel.setText(tel);
        }
        if(MyStringUtil.isEmpty(address)){
            tv_address.setVisibility(View.GONE);
        }else{
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(address);
        }

        btn_js.setVisibility(View.GONE);
//        btn_wc.setVisibility(View.GONE);
//        btn_zz.setVisibility(View.GONE);
        //如果司机对接的员工 == 员工；可操作
        String driverMemberId = item.getDriverMemberId();
        String userId = SPUtils.getID();
        if(MyStringUtil.isEmpty(driverMemberId) || userId.equals(driverMemberId)){
            btn_wc.setVisibility(View.VISIBLE);
            btn_zz.setVisibility(View.VISIBLE);
        }else{
            btn_wc.setVisibility(View.GONE);
            btn_zz.setVisibility(View.GONE);
        }
    }
}
