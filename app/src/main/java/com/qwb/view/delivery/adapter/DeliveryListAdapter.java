package com.qwb.view.delivery.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.view.delivery.model.DeliveryBean;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送单列表
 */
public class DeliveryListAdapter extends BaseQuickAdapter<DeliveryBean,BaseViewHolder> {
    private List<DeliveryBean> selectList = new ArrayList<>();

    public DeliveryListAdapter() {
        super(R.layout.x_adapter_delivery_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeliveryBean item) {
        helper.setText(R.id.tv_orderNo, "配送单号:" + item.getBillNo());
        helper.setText(R.id.tv_khNm, item.getKhNm());// 客户名称
        helper.setText(R.id.tv_time, item.getOutDate());//"配送日期:" +
        helper.setText(R.id.tv_num, "" + item.getDdNum());
        helper.setText(R.id.tv_amount, "" + item.getDisAmt1());
        helper.setText(R.id.tv_car, item.getVehNo());

        TextView tv_tel = helper.getView(R.id.tv_tel);
        TextView tv_address = helper.getView(R.id.tv_address);

        //电话和地址
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

        //状态
        TextView tvStatus = helper.getView(R.id.tv_status);
        Integer psState = item.getPsState();
        if(1 == psState){
            tvStatus.setText("待接收");
        }else if(2 == psState){
            tvStatus.setText("已接收");
        }else if(3 == psState){
            tvStatus.setText("配送中");
        }else if(4 == psState){
            tvStatus.setText("已收货");
        }

        //---------------------cb:start-----------------------
        ImageView ivCb = helper.getView(R.id.item_iv_cb);
        boolean flag = false;
        for (DeliveryBean bean : selectList) {
            if(String.valueOf(bean.getId()).equals(String.valueOf(item.getId()))){
                flag = true;
                break;
            }
        }
        if(flag){
            ivCb.setImageResource(R.drawable.icon_dxz);
        }else{
            ivCb.setImageResource(R.drawable.icon_dx);
        }
        //---------------------cb:end-----------------------

    }

    public List<DeliveryBean> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<DeliveryBean> selectList) {
        this.selectList = selectList;
    }
}
