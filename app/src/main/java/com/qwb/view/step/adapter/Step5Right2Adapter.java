package com.qwb.view.step.adapter;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.common.OrderTypeEnum;
import com.qwb.utils.MyColorUtil;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.MyDatePickerDialog;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;
import java.math.BigDecimal;
import java.util.List;

/**
 * 步骤5:左边商品
 */
public class Step5Right2Adapter extends BaseQuickAdapter<ShopInfoBean.Data, BaseViewHolder> {
    private OrderTypeEnum mTypeEnum;
    private int mRedMark;
    private boolean isEditPrice = true;//价格是否可以编辑(默认true)
    private boolean isProductDate = false;//是否有生产日期
    private boolean isDel = true;//是否有删除操作

    public static final int TAG_DW = 1;
    public static final int TAG_XSTP = 2;
    public static final int TAG_DEl = 3;
    public static final int TAG_COUNT = 4;
    public static final int TAG_PRICE = 5;

    public Step5Right2Adapter(int redMark) {
//        super(R.layout.x_step5_table_right_item);
        super(R.layout.x_step5_red_table_right_item);
        this.mRedMark = redMark;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShopInfoBean.Data item) {
        helper.setIsRecyclable(false);

        TextView tvGG = helper.getView(R.id.tv_table_content_right_item0);
        TextView tvXstp = helper.getView(R.id.tv_table_content_right_item1);
        final TextView tvDw = helper.getView(R.id.tv_table_content_right_item2);
        final EditText etCount = helper.getView(R.id.tv_table_content_right_item3);
        final EditText etPrice = helper.getView(R.id.tv_table_content_right_item4);
        final TextView tvZj = helper.getView(R.id.tv_table_content_right_item5);
        EditText etBz = helper.getView(R.id.tv_table_content_right_item6);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item7);
        final TextView tvProduceDate = helper.getView(R.id.tv_table_content_right_item8_produce_date);
        View lineProduceDate = helper.getView(R.id.line_produce_date);

        if (Constans.ISDEBUG){
            View tvNum_ = helper.getView(R.id.tv_table_content_right_item_num_);
            View tvSum_ = helper.getView(R.id.tv_table_content_right_item_sum_);
            if (1 == mRedMark){
                tvNum_.setVisibility(View.VISIBLE);
                tvSum_.setVisibility(View.VISIBLE);
            }else{
                tvNum_.setVisibility(View.GONE);
                tvSum_.setVisibility(View.GONE);
            }
        }


        final String count = item.getCurrentCount();
        final String price = item.getCurrentPrice();
        final int position = helper.getAdapterPosition();

        tvGG.setText(item.getWareGg());
        tvXstp.setText(item.getCurrentXstp() + " ▼");
        tvDw.setText(item.getCurrentDw() + " ▼");
        etCount.setText(item.getCurrentCount());
        etPrice.setText(item.getCurrentPrice());
        etBz.setText(item.getCurrentBz());
        tvProduceDate.setText(item.getCurrentProductDate());
        tvDel.setText("删除");

        if(isProductDate){
            tvProduceDate.setVisibility(View.VISIBLE);
            lineProduceDate.setVisibility(View.VISIBLE);
        }else{
            tvProduceDate.setVisibility(View.GONE);
            lineProduceDate.setVisibility(View.GONE);
        }
        if(isDel){
            tvDel.setVisibility(View.VISIBLE);
        }else{
            tvDel.setVisibility(View.GONE);
        }

        //计算总价，总金额
        if(!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)){
            double zj = Double.valueOf(count) * Double.valueOf(price);
            tvZj.setText("" + MyDoubleUtils.getDecimal(zj));
        }else{
            tvZj.setText("");
        }

        /**
         * 不可修改价格的逻辑
         * 1.正常销售：价格不可修改
         * 2.其他销售类型：都可以修改价格
         */
        if(!isEditPrice && MyStringUtil.eq(ConstantUtils.NORMAL_XSTP,item.getCurrentXstp())){
            etPrice.setEnabled(false);
            etPrice.setBackgroundColor(MyColorUtil.getColorResId(R.color.gray_e));
        }else{
            etPrice.setEnabled(true);
            etPrice.setBackgroundColor(MyColorUtil.getColorResId(R.color.white));
        }

        /**
         * 是否小于最低销售价
         */
        if (isLessThanSalePrice(item, item.getCurrentPrice() , false)){
            etPrice.setTextColor(MyColorUtil.getColorResId(R.color.red));
        }else{
            etPrice.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
        }

        tvXstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != listener){
                    listener.onChildListener(TAG_XSTP, position, item);
                }
            }
        });
        tvDw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != listener){
                    listener.onChildListener(TAG_DW, position, item);
                }
            }
        });
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != listener){
                    listener.onChildListener(TAG_DEl, position, item);
                }
            }
        });
        etCount.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                String count = input.toString().trim();
                String price = etPrice.getText().toString().trim();
                setZj(count, price, tvZj, item);

                if(null != listener){
                    listener.onChildListener(TAG_COUNT, position, item);
                }
            }
        });
        etPrice.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                String priceStr = input.toString().trim();
                String count = etCount.getText().toString().trim();
                setZj(count, priceStr, tvZj, item);
                setPriceTextColor(priceStr, item, etPrice);
                if(null != listener){
                    listener.onChildListener(TAG_PRICE, position, item);
                }
            }
        });
        etPrice.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String priceStr = etPrice.getText().toString().trim();
                    isLessThanSalePrice(item, priceStr, true);
                }
            }
        });
        etBz.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentBz(input.toString().trim());
            }
        });

        tvProduceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProduceDate(tvProduceDate, position);
            }
        });

    }

    public OrderTypeEnum getTypeEnum() {
        return mTypeEnum;
    }

    public void setTypeEnum(OrderTypeEnum mTypeEnum) {
        this.mTypeEnum = mTypeEnum;
    }

    public boolean isEditPrice() {
        return isEditPrice;
    }

    public void setEditPrice(boolean editPrice) {
        isEditPrice = editPrice;
    }

    public boolean isProductDate() {
        return isProductDate;
    }

    public void setProductDate(boolean productDate) {
        isProductDate = productDate;
    }

    public boolean isDel() {
        return isDel;
    }

    public void setDel(boolean del) {
        isDel = del;
    }

    private Step5Right2Adapter.OnChildListener listener;
    public interface OnChildListener{
        void onChildListener(int tag, int position, ShopInfoBean.Data item);
    }

    public void setOnChildListener(Step5Right2Adapter.OnChildListener listener){
        this.listener = listener;
    }

    /**
     * 小于最低销售价提示
     */
    private boolean isLessThanSalePrice(ShopInfoBean.Data item, String priceStr, boolean isTotal) {
        try {
            //正常销售--小于最低销售价--提示
            String xstp = item.getCurrentXstp();
            String salePriceStr = item.getLowestSalePrice();
            if (MyStringUtil.eq(ConstantUtils.NORMAL_XSTP, xstp) && MyStringUtil.isNumber(salePriceStr) && MyStringUtil.isNumber(priceStr)){
                BigDecimal price = new BigDecimal(priceStr);
                BigDecimal salePrice = new BigDecimal(salePriceStr);
                if (MyStringUtil.eq(item.getCurrentCode(), item.getMinUnitCode())){
                    BigDecimal hsNum = new BigDecimal(1);
                    if (MyStringUtil.isNumber(item.getHsNum())) {
                        hsNum = new BigDecimal(item.getHsNum());
                    }
                    salePrice = MyMathUtils.divideByScale(salePrice, hsNum, 10);
                    if (price.doubleValue() < salePrice.doubleValue()){
                        if (isTotal){
                            if (mTypeEnum == null || !(mTypeEnum == OrderTypeEnum.ORDER_THXD_ADD
                                    || mTypeEnum == OrderTypeEnum.ORDER_THXD_LIST
                                    || mTypeEnum == OrderTypeEnum.ORDER_RED_ADD
                                    || mTypeEnum == OrderTypeEnum.ORDER_RED_LIST
                            )){
                                ToastUtils.showCustomToast(item.getWareNm() +"的最低销售价(小)为："+salePrice);
                            }
                        }
                        return true;
                    }
                }else{
                    if (price.doubleValue() < salePrice.doubleValue()){
                        if (isTotal){
                            if (mTypeEnum == null || !(mTypeEnum == OrderTypeEnum.ORDER_THXD_ADD
                                    || mTypeEnum == OrderTypeEnum.ORDER_THXD_LIST
                                    || mTypeEnum == OrderTypeEnum.ORDER_RED_ADD
                                    || mTypeEnum == OrderTypeEnum.ORDER_RED_LIST
                            )){
                                ToastUtils.showCustomToast(item.getWareNm() +"的最低销售价(大)为："+salePrice);
                            }
                        }
                        return true;
                    }
                }
            }
        }catch (Exception e){
        }
        return false;
    }

    /**
     * 设置价格字体颜色
     */
    private void setPriceTextColor(String priceStr, ShopInfoBean.Data item, EditText etPrice) {
        try {
            if (isLessThanSalePrice(item, priceStr , false)){
                etPrice.setTextColor(MyColorUtil.getColorResId(R.color.red));
            }else{
                etPrice.setTextColor(MyColorUtil.getColorResId(R.color.gray_3));
            }
        }catch (Exception e){}
    }

    //计算总价，总金额
    private void setZj(String count, String price, TextView tvZj, ShopInfoBean.Data item) {
        try {
            if(!MyStringUtil.isEmpty(count) && !MyStringUtil.isEmpty(price)){
                double zj = Double.valueOf(count) * Double.valueOf(price);
                tvZj.setText("" + zj);
            }else{
                tvZj.setText("");
            }
            item.setCurrentCount(count);
            item.setCurrentPrice(price);
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }

    //对话框：生产日期
    private void showDialogProduceDate(final TextView tvProduceDate, final int position) {
        try {
            String dateStr = tvProduceDate.getText().toString();
            if(MyStringUtil.isEmpty(dateStr)){
                dateStr = MyTimeUtils.getTodayStr();
            }
            String[] split = dateStr.split("-");
            int year = Integer.valueOf(split[0]);
            int month = Integer.valueOf(split[1]) - 1;
            int day = Integer.valueOf(split[2]);
            new MyDatePickerDialog(mContext, "生产日期", year, month, day, new MyDatePickerDialog.DateTimeListener() {
                @Override
                public void onSetTime(int year, int month, int day, String timeStr) {
                    try {
                        tvProduceDate.setText(timeStr);
                        List<ShopInfoBean.Data> data = getData();
                        ShopInfoBean.Data bean = data.get(position);
                        bean.setCurrentProductDate(timeStr);
                        data.set(position, bean);
                    }catch (Exception e){
                        ToastUtils.showError(e);
                    }
                }

                @Override
                public void onCancel() {
                }
            }).show();
        }catch (Exception e){
            ToastUtils.showError(e);
        }
    }
}
