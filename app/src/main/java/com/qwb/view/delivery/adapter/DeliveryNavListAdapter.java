package com.qwb.view.delivery.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.db.DDeliveryCustomerBean;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送单:导航客户列表
 */
public class DeliveryNavListAdapter extends BaseQuickAdapter<DDeliveryCustomerBean, BaseViewHolder> {
    private List<DDeliveryCustomerBean> selectList = new ArrayList<>();
    private boolean isDel;

    public DeliveryNavListAdapter() {
        super(R.layout.x_adapter_delivery_nav_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, DDeliveryCustomerBean item) {
        helper.addOnClickListener(R.id.right);
        helper.addOnClickListener(R.id.tv_nav);

        helper.setText(R.id.tv_khNm, item.getKhNm());// 客户名称
        TextView tv_orderNo = helper.getView(R.id.tv_orderNo);
        TextView tv_address = helper.getView(R.id.tv_address);
//        TextView tv_is_nav = helper.getView(R.id.tv_nav);
        TextView tv_status = helper.getView(R.id.tv_status);

        //地址
        String address = item.getAddress();
        if (MyStringUtil.isEmpty(address)) {
            tv_address.setVisibility(View.INVISIBLE);
        } else {
            tv_address.setVisibility(View.VISIBLE);
            tv_address.setText(address);
        }
        //配送单号
        String billNo = item.getBillNo();
        if (MyStringUtil.isEmpty(billNo)) {
            tv_orderNo.setVisibility(View.GONE);
            tv_status.setVisibility(View.GONE);
        } else {
            tv_orderNo.setVisibility(View.VISIBLE);
            tv_status.setVisibility(View.VISIBLE);
            tv_orderNo.setText(billNo);
            String psState = item.getPsState();
            if("1".equals(psState)){
                tv_status.setText("待接收");
            }else if("2".equals(psState)){
                tv_status.setText("已接收");
            }else if("3".equals(psState)){
                tv_status.setText("配送中");
            }else if("4".equals(psState)){
                tv_status.setText("已收货");
            }else if("6".equals(psState)){
                tv_status.setText("已完成");
            }
        }
        //已导航
//        Boolean isNav = item.getNav();
//        if (isNav != null && isNav) {
//            tv_is_nav.setVisibility(View.VISIBLE);
//        } else {
//            tv_is_nav.setVisibility(View.GONE);
//        }

        ImageView ivCb = helper.getView(R.id.item_iv_cb);
        //---------------------cb:start-----------------------
        //只有自己添加的客户才能删除
        if (MyStringUtil.isEmpty(billNo) && isDel) {
            ivCb.setVisibility(View.VISIBLE);
            boolean flag = false;
            for (DDeliveryCustomerBean bean : selectList) {
                if (String.valueOf(bean.getId()).equals(String.valueOf(item.getId()))) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                ivCb.setImageResource(R.drawable.icon_dxz);
            } else {
                ivCb.setImageResource(R.drawable.icon_dx);
            }
        } else {
            ivCb.setVisibility(View.GONE);
        }
        //---------------------cb:end-----------------------

    }

    public List<DDeliveryCustomerBean> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<DDeliveryCustomerBean> selectList) {
        this.selectList = selectList;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }
}
