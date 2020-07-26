package com.qwb.view.storehouse.adapter;

import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyColorUtil;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.storehouse.model.StorehouseOutSubBean;
import com.qwb.utils.ToastUtils;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.xmsx.qiweibao.R;

import java.math.BigDecimal;

/**
 * 出仓单：右边
 */
public class StorehouseOutRightAdapter extends BaseQuickAdapter<StorehouseOutSubBean, BaseViewHolder> {

    public StorehouseOutRightAdapter() {
        super(R.layout.x_storehouse_in_table_right_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, final StorehouseOutSubBean item) {
        helper.setIsRecyclable(false);
        helper.addOnClickListener(R.id.tv_table_content_right_item1);
        helper.addOnClickListener(R.id.tv_table_content_right_item2);
        helper.addOnClickListener(R.id.tv_table_content_right_item5);

        TextView tvDw = helper.getView(R.id.tv_table_content_right_item1);
        TextView tvInStk = helper.getView(R.id.tv_table_content_right_item2);
        EditText etCount = helper.getView(R.id.tv_table_content_right_item3);
        final TextView tvStayCount = helper.getView(R.id.tv_table_content_right_item4);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item5);


        try {
            //单位
            String beUnit = item.getBeUnit();
            String minUnitCode = item.getMinUnitCode();
            if (beUnit.equals(minUnitCode)) {
                tvDw.setText(item.getMinUnit() + " ▼");
            } else {
                tvDw.setText(item.getWareDw() + " ▼");
            }

            //移出库位
            String outStkName = item.getOutStkName();
            if (MyStringUtil.isEmpty(outStkName)) {
                outStkName = "临时库位";
            }
            tvInStk.setText(outStkName + " ▼");

            //移出数量
            if (MyStringUtil.isEmpty("" + item.getQty())){
                etCount.setText("0");
            }else{
                etCount.setText(MyMathUtils.clearZero(item.getQty()));
            }

            //待入仓
            showStayCount(item, tvStayCount);

            //操作
            tvDel.setText("删除");

            //移入数量
            etCount.addTextChangedListener(new MyAfterTextWatcher() {
                @Override
                public void afterTextChanged(Editable input) {
                    String qty = input.toString().trim();
                    if(MyStringUtil.isEmpty(qty)){
                        item.setQty(new BigDecimal(0));
                    }else{
                        item.setQty(new BigDecimal(qty));
                    }
                    showStayCount(item, tvStayCount);
                }
            });


        } catch (Exception e) {
            ToastUtils.showError(e);
        }

    }

    /**
     * 待出仓
     */
    public void showStayCount(StorehouseOutSubBean item, TextView tvStayCount){

        try {
            BigDecimal outQty1 = new BigDecimal(0);
            if(item.getOutQty1() != null){
                outQty1 = item.getOutQty1();
            }
            BigDecimal count = new BigDecimal(0);
            if(MyStringUtil.isNotEmpty("" + item.getQty())){
                count = item.getQty();
            }
            BigDecimal hsNum = new BigDecimal(1);
            if(MyStringUtil.isNotEmpty("" + item.getHsNum())){
                hsNum = new BigDecimal(item.getHsNum());
            }
            String oldBeUnit = item.getOldBeUnit();//后台
            String beUnit = item.getBeUnit();

            BigDecimal dis = new BigDecimal(0);
            if(beUnit.equals(oldBeUnit)){
                dis = MyMathUtils.subtract(outQty1, count);
            }else{
                if(beUnit.equals(item.getMaxUnitCode())){
                    dis = MyMathUtils.subtract(MyMathUtils.divideByScale(outQty1, hsNum, 2), count);
                }else{
                    dis = MyMathUtils.subtract(MyMathUtils.multiply(outQty1, hsNum, 2), count);
                }
            }
            tvStayCount.setText(MyMathUtils.clearZero(dis));

            if (dis.doubleValue() == 0) {
                tvStayCount.setTextColor(MyColorUtil.getColorResId(R.color.blue));
            } else if (dis.doubleValue() < 0) {
                tvStayCount.setTextColor(MyColorUtil.getColorResId(R.color.red));
            } else {
                tvStayCount.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
            }
        }catch (Exception e){

        }


    }

}
