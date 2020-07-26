package com.qwb.view.company.adapter;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyColorUtil;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.step.model.ShopInfoBean;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.MyDatePickerDialog;
import com.xmsx.qiweibao.R;

import java.util.List;

/**
 * 采购单--表格--右边-适配器
 */
public class PurchaseOrderRightAdapter extends BaseQuickAdapter<ShopInfoBean.Data,BaseViewHolder> {
    private boolean isEditPrice = true;//价格是否可以编辑(默认true)
    private boolean isProductDate = false;//是否有生产日期
    private boolean isDel = true;//是否有删除操作

    public static final int TAG_DW = 1;
    public static final int TAG_XSTP = 2;
    public static final int TAG_DEl = 3;
    public static final int TAG_COUNT = 4;
    public static final int TAG_PRICE = 5;

    public PurchaseOrderRightAdapter() {
        super(R.layout.x_purchase_order_table_right_item);
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

        final EditText etRebatePrice = helper.getView(R.id.tv_table_content_right_item8_fl_price);

        final String count = item.getCurrentCount();
        final String price = item.getCurrentPrice();
        final int position = helper.getAdapterPosition();

        tvGG.setText(item.getWareGg());
        if(MyStringUtil.isEmpty(item.getInTypeName())){
            tvXstp.setText("正常采购" + " ▼");
        }else{
            tvXstp.setText(item.getInTypeName() + " ▼");
        }

        tvDw.setText(item.getCurrentDw() + " ▼");
        etCount.setText(item.getCurrentCount());
        etPrice.setText(item.getCurrentPrice());
        etBz.setText(item.getCurrentBz());
        tvProduceDate.setText(item.getCurrentProductDate());
        tvDel.setText("删除");
        etRebatePrice.setText(item.getRebatePrice());

        tvProduceDate.setVisibility(View.VISIBLE);
        lineProduceDate.setVisibility(View.VISIBLE);
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

        if(!isEditPrice){
            etPrice.setEnabled(false);
            etPrice.setBackgroundColor(MyColorUtil.getColorResId(R.color.gray_e));
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
                String price = input.toString().trim();
                String count = etCount.getText().toString().trim();
                setZj(count, price, tvZj, item);
                if(null != listener){
                    listener.onChildListener(TAG_PRICE, position, item);
                }
            }
        });
        etBz.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentBz(input.toString().trim());
            }
        });
        etRebatePrice.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setRebatePrice(input.toString().trim());
            }
        });

        tvProduceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProduceDate(tvProduceDate, position);
            }
        });

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

    private PurchaseOrderRightAdapter.OnChildListener listener;
    public interface OnChildListener{
        void onChildListener(int tag, int position, ShopInfoBean.Data item);
    }

    public void setOnChildListener(PurchaseOrderRightAdapter.OnChildListener listener){
        this.listener = listener;
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
