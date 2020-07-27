package com.qwb.view.storehouse.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.storehouse.model.StorehouseWareBean;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;

import java.math.BigDecimal;

/**
 * 入仓单列表
 */
public class StorehouseWareListAdapter extends BaseQuickAdapter<StorehouseWareBean, BaseViewHolder> {

    public StorehouseWareListAdapter() {
        super(R.layout.x_adapter_storehouse_ware_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, StorehouseWareBean item) {

        TextView tvWareName = helper.getView(R.id.tv_ware_name);
        TextView tvHouseName = helper.getView(R.id.tv_house_name);
        TextView tvMaxNum = helper.getView(R.id.tv_max_num);
        TextView tvMinMun = helper.getView(R.id.tv_min_num);
        TextView tvNum = helper.getView(R.id.tv_num);

        try {
            tvWareName.setText(item.getWareNm());
            tvHouseName.setText(item.getHouseName());
            setMaxNum(item, tvMaxNum);
            setMinNum(item, tvMinMun);
            setNum(item, tvNum);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    //大单位数量
    private void setMaxNum(StorehouseWareBean item, TextView tvMaxNum){
        BigDecimal qty = item.getQty();
        String unit = item.getUnitName();
        tvMaxNum.setText("库存数量(大):" + MyMathUtils.setScale(qty, 2) + unit);
    }
    //小单位数量
    private void setMinNum(StorehouseWareBean item, TextView tvMinMun){
        BigDecimal minQty = item.getMinQty();
        String unit = item.getMinUnit();
        tvMinMun.setText("库存数量(小):" + MyMathUtils.setScale(minQty, 0) + unit);
    }
    //大小单位数量
    private void setNum(StorehouseWareBean item, TextView tvNum){
        BigDecimal hsNum = new BigDecimal(1);
        if(MyStringUtil.isNotEmpty("" + item.getHsNum())){
            hsNum = new BigDecimal(item.getHsNum());
        }
        BigDecimal qty = new BigDecimal(0);
        if(MyStringUtil.isNotEmpty("" + item.getQty())){
            qty = item.getQty();
        }
        String result = "";
        String maxUnit = item.getUnitName();
        String minUnit = item.getMinUnit();
        long maxL = 0;
        if (MyStringUtil.isNotEmpty(maxUnit)) {
            maxL = MyMathUtils.getLongDown(qty);
            if(maxL > 0){
                result += maxL + maxUnit;
            }
        }
        if (MyStringUtil.isNotEmpty(minUnit)) {
            BigDecimal minQty = MyMathUtils.multiply(MyMathUtils.subtract(qty, maxL), hsNum);
            long minL = MyMathUtils.getLongHelfUp(minQty);
            if(minL > 0){
                result += minL + minUnit;
            }
        }
        tvNum.setText("大小数量:" + result);
    }

}
