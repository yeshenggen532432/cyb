package com.qwb.view.storehouse.adapter;

import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.storehouse.model.StorehouseInSubBean;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyColorUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.chiyong.t3.R;

import java.math.BigDecimal;
import java.util.List;

/**
 * 入仓单：右边
 */
public class StorehouseInRightAdapter extends BaseQuickAdapter<StorehouseInSubBean, BaseViewHolder> {

    public StorehouseInRightAdapter() {
        super(R.layout.x_storehouse_in_table_right_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, final StorehouseInSubBean item) {
        helper.setIsRecyclable(false);
        helper.addOnClickListener(R.id.tv_table_content_right_item1);
        helper.addOnClickListener(R.id.tv_table_content_right_item2);
        helper.addOnClickListener(R.id.tv_table_content_right_item5);
        helper.addOnClickListener(R.id.tv_table_content_right_item6);

        TextView tvDw = helper.getView(R.id.tv_table_content_right_item1);
        TextView tvInStk = helper.getView(R.id.tv_table_content_right_item2);
        EditText etCount = helper.getView(R.id.tv_table_content_right_item3);
        final TextView tvStayCount = helper.getView(R.id.tv_table_content_right_item4);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item5);
        TextView tvCopy = helper.getView(R.id.tv_table_content_right_item6);


        try {
            //单位
            String beUnit = item.getBeUnit();
            String minUnitCode = item.getMinUnitCode();
            if (beUnit.equals(minUnitCode)) {
                tvDw.setText(item.getMinUnit() + " ▼");
            } else {
                tvDw.setText(item.getWareDw() + " ▼");
            }

            //移入库位
            String inStkName = item.getInStkName();
            if (MyStringUtil.isEmpty(inStkName)) {
                inStkName = "临时库位";
            }
            tvInStk.setText(inStkName + " ▼");

            //移入数量
            if (MyStringUtil.isEmpty("" + item.getQty())) {
                etCount.setText("0");
            } else {
                etCount.setText(MyMathUtils.clearZero(item.getQty()));
            }



            //操作
            tvDel.setText("删除");
            tvCopy.setText("复制");

            //移入数量
            final int position = helper.getAdapterPosition();
            //待入仓
            showStayCount(item, tvStayCount);
            etCount.addTextChangedListener(new MyAfterTextWatcher() {
                @Override
                public void afterTextChanged(Editable input) {
                    String qty = input.toString().trim();
                    if (MyStringUtil.isEmpty(qty)) {
                        item.setQty(new BigDecimal(0));
                    } else {
                        item.setQty(new BigDecimal(qty));
                    }
                    getData().set(position, item);
                    showStayCount(item, tvStayCount);

                }
            });


        } catch (Exception e) {
            ToastUtils.showError(e);
        }





    }

    /**
     * 待入仓
     */
    public void showStayCount(StorehouseInSubBean item, TextView tvStayCount) {

        int scale = 10;
        try {
            BigDecimal inQty1 = new BigDecimal(0);
            if (item.getInQty1() != null) {
                inQty1 = item.getInQty1();
            }

            BigDecimal hsNum = new BigDecimal(1);
            if (MyStringUtil.isNotEmpty("" + item.getHsNum())) {
                hsNum = new BigDecimal(item.getHsNum());
            }


            //全部转为大单位
            BigDecimal count = new BigDecimal(0);
            List<StorehouseInSubBean> list = getData();
            for(int i = 0; i < list.size(); i++){
                StorehouseInSubBean sub = list.get(i);
                if(String.valueOf(sub.getWareId()).equals("" + item.getWareId()) && String.valueOf(sub.getId()).equals("" + item.getId())){
                    if(sub.getBeUnit().equals(sub.getMinUnitCode())){
                        count = MyMathUtils.add(count, MyMathUtils.divideByScale(sub.getQty(), hsNum, scale));
                    }else{
                        count = MyMathUtils.add(count, sub.getQty());
                    }
                }
            }


            String oldBeUnit = item.getOldBeUnit();//后台
            String beUnit = item.getBeUnit();
            BigDecimal dis = new BigDecimal(0);

            if (beUnit.equals(item.getMaxUnitCode())) {
                if(oldBeUnit.equals(item.getMaxUnitCode())){
                    dis = MyMathUtils.subtract(inQty1, count);
                }else{
                    dis = MyMathUtils.subtract(MyMathUtils.divideByScale(inQty1, hsNum, scale), count);
                }
            } else {
                if(oldBeUnit.equals(item.getMaxUnitCode())){
                    dis = MyMathUtils.subtract(MyMathUtils.multiply(inQty1, hsNum, scale), MyMathUtils.multiply(count, hsNum, scale));
                }else{
                    dis = MyMathUtils.subtract(inQty1, MyMathUtils.multiply(count, hsNum, scale));
                }
            }
            //最后显示2位
            tvStayCount.setText(MyMathUtils.clearZero(dis));

            if (dis.doubleValue() == 0) {
                tvStayCount.setTextColor(MyColorUtil.getColorResId(R.color.blue));
            } else if (dis.doubleValue() < 0) {
                tvStayCount.setTextColor(MyColorUtil.getColorResId(R.color.red));
            } else {
                tvStayCount.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
            }


        } catch (Exception e) {

        }

    }

    public void setTextColor() {

    }

}
