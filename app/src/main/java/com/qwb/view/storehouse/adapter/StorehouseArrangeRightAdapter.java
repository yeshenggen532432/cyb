package com.qwb.view.storehouse.adapter;

import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.storehouse.model.StorehouseWareBean;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.qiweibao.R;

import java.math.BigDecimal;

/**
 * 库位整理
 */
public class StorehouseArrangeRightAdapter extends BaseQuickAdapter<StorehouseWareBean, BaseViewHolder> {

    public StorehouseArrangeRightAdapter() {
        super(R.layout.x_storehouse_arrange_table_right_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, final StorehouseWareBean item) {
        helper.setIsRecyclable(false);
        helper.addOnClickListener(R.id.tv_table_content_right_item5);
        helper.addOnClickListener(R.id.tv_table_content_right_item9);

        TextView tvDw = helper.getView(R.id.tv_table_content_right_item1);
        TextView tvOutStk = helper.getView(R.id.tv_table_content_right_item2);
        TextView tvQty = helper.getView(R.id.tv_table_content_right_item3);
        TextView tvQtyUnit = helper.getView(R.id.tv_table_content_right_item4);
        TextView tvInStk = helper.getView(R.id.tv_table_content_right_item5);
        EditText etInQty = helper.getView(R.id.tv_table_content_right_item6);
        EditText etInMinQty = helper.getView(R.id.tv_table_content_right_item7);
        final TextView tvInSum = helper.getView(R.id.tv_table_content_right_item8);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item9);


        try {
            //移入数量
            final int position = helper.getAdapterPosition();

            //单位
            String beUnit = item.getBeUnit();
            String minUnitCode = item.getMinUnitCode();
            if (beUnit.equals(minUnitCode)) {
                tvDw.setText(item.getMinUnit());
            } else {
                tvDw.setText(item.getUnitName());
            }

            //移出库位
            tvOutStk.setText(item.getOutStkName());

            //存放数量
            tvQty.setText(MyMathUtils.clearZero(item.getQty()));
            tvQtyUnit.setText(tranUnit(item.getHsNum(), item.getQty(), item.getUnitName(), item.getMinUnit()));

            //移入库位
            String inStkName = item.getInStkName();
            if(MyStringUtil.isNotEmpty(inStkName)){
                tvInStk.setText(inStkName + " ▼");
            }


            //移入数量(大)(小)
            if (MyStringUtil.isNotEmpty("" + item.getInQty())) {
                etInQty.setText(MyMathUtils.clearZero(item.getInQty()));
            }else{
                etInQty.setText("");
            }
            if (MyStringUtil.isEmpty("" + item.getMinInQty())) {
                etInMinQty.setText("");
            } else {
                etInMinQty.setText(MyMathUtils.clearZero(item.getMinInQty()));
            }
            tvInSum.setText(setInSum(item.getHsNum(), item.getUnitName(), item.getMinUnit(), item.getInQty(), item.getMinInQty()));

            //操作
            tvDel.setText("删除");

            etInQty.addTextChangedListener(new MyAfterTextWatcher() {
                @Override
                public void afterTextChanged(Editable input) {
                    String inQty = input.toString().trim();
                    if (MyStringUtil.isEmpty(inQty)) {
                        item.setInQty(null);
                    } else {
                        item.setInQty(new BigDecimal(inQty));
                    }
                    tvInSum.setText(setInSum(item.getHsNum(), item.getUnitName(), item.getMinUnit(), item.getInQty(), item.getMinInQty()));

                }
            });
            etInMinQty.addTextChangedListener(new MyAfterTextWatcher() {
                @Override
                public void afterTextChanged(Editable input) {
                    String inQty = input.toString().trim();
                    if (MyStringUtil.isEmpty(inQty)) {
                        item.setMinInQty(null);
                    } else {
                        item.setMinInQty(new BigDecimal(inQty));
                    }
                    tvInSum.setText(setInSum(item.getHsNum(), item.getUnitName(), item.getMinUnit(), item.getInQty(), item.getMinInQty()));
                }
            });


        } catch (Exception e) {
//            ToastUtils.showError(e);
        }


    }

    /**
     * 移入总数量
     */
    public String setInSum(Double hsNumD, String maxUnit, String minUnit, BigDecimal inQty, BigDecimal inMinQty) {
        String s = "";
        try {
            BigDecimal hsNum = new BigDecimal(1);
            if (MyStringUtil.isNotEmpty("" + hsNumD)) {
                hsNum = new BigDecimal(hsNumD);
            }
            BigDecimal qty = new BigDecimal(0);
            if(MyStringUtil.isNotEmpty("" + inQty)){
                qty = inQty;
            }
            if(MyStringUtil.isNotEmpty("" + inMinQty)){
                qty = MyMathUtils.add(qty, MyMathUtils.divideByScale(inMinQty, hsNum, 10));
            }
            s = tranUnit(hsNumD, qty, maxUnit, minUnit);
        }catch (Exception e){

        }finally {
            return  s;
        }
    }

    /**
     * 数字转成单位
     */
    public String tranUnit(Double hsNumD, BigDecimal qtyB, String maxUnit, String minUnit) {
        String s = "";
        try {
            BigDecimal hsNum = new BigDecimal(1);
            if (MyStringUtil.isNotEmpty("" + hsNumD)) {
                hsNum = new BigDecimal(hsNumD);
            }
            BigDecimal qty = new BigDecimal(0);
            if (MyStringUtil.isNotEmpty("" + qtyB)) {
                qty = qtyB;
            }
            long qtyMaxUnit = MyMathUtils.getLongDown(qty);
            BigDecimal dis = MyMathUtils.subtract(qty, new BigDecimal(qtyMaxUnit));
            BigDecimal disHsNum = MyMathUtils.multiply(dis, hsNum, 10);
            long qtyMinUnit = MyMathUtils.getLongHelfUp(disHsNum);
            if(MyStringUtil.isNotEmpty(maxUnit) && qtyMaxUnit > 0){
                s += qtyMaxUnit + maxUnit;
            }
            if(MyStringUtil.isNotEmpty(minUnit) && qtyMinUnit > 0){
                s += qtyMinUnit + minUnit;
            }
        }catch (Exception e){
        }finally {
            return s;
        }
    }

}
