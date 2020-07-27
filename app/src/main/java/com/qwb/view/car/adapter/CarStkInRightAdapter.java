package com.qwb.view.car.adapter;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyMathUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyUnitUtil;
import com.qwb.view.step.model.ShopInfoBean;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.chiyong.t3.R;

import java.math.BigDecimal;

/**
 * 车销回库
 */
public class CarStkInRightAdapter extends BaseQuickAdapter<ShopInfoBean.Data,BaseViewHolder> {
    private boolean isDel = true;//是否有删除操作

    public static final int TAG_DW = 1;
    public static final int TAG_XSTP = 2;
    public static final int TAG_DEl = 3;
    public static final int TAG_COUNT = 4;
    public static final int TAG_PRICE = 5;

    public CarStkInRightAdapter() {
        super(R.layout.x_table_right_car_stk_in);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShopInfoBean.Data item) {
        helper.setIsRecyclable(false);

        TextView tvGG = helper.getView(R.id.tv_table_content_right_item1);
        final EditText etCount = helper.getView(R.id.tv_table_content_right_item2);
        final TextView tvDw = helper.getView(R.id.tv_table_content_right_item3);
        final TextView tvCountMax = helper.getView(R.id.tv_table_content_right_item4);
        TextView tvDwMax = helper.getView(R.id.tv_table_content_right_item5);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item6);

        final int position = helper.getAdapterPosition();
        final String count = item.getCurrentCount();
        etCount.setText(count);

        tvGG.setText(item.getWareGg());
        tvDw.setText(item.getMinUnit());
        tvCountMax.setText(MyUnitUtil.minCountToMaxCount(item.getHsNum(), count));
//        getMaxCount(tvCountMax, count, item);
        tvDwMax.setText(item.getWareDw());
        tvDel.setText("删除");

        if(isDel){
            tvDel.setVisibility(View.VISIBLE);
        }else{
            tvDel.setVisibility(View.GONE);
        }

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
                item.setCurrentCount(count);
//                getMaxCount(tvCountMax, count, item);
                tvCountMax.setText(MyUnitUtil.minCountToMaxCount(item.getHsNum(), count));
            }
        });

    }

    private OnChildListener listener;
    public interface OnChildListener{
        void onChildListener(int tag, int position, ShopInfoBean.Data item);
    }

    public void setOnChildListener(OnChildListener listener){
        this.listener = listener;
    }

//    public void getMaxCount(TextView tvMaxCount, String count, final ShopInfoBean.Data item){
//        if (MyStringUtil.isEmpty(count)){
//            tvMaxCount.setText("");
//        }else{
//            BigDecimal hsNum = new BigDecimal(1);
//            BigDecimal minCountBig = new BigDecimal(count);
//            if (MyStringUtil.isNumber(item.getHsNum())){
//                hsNum = new BigDecimal(item.getHsNum());
//            }
//            BigDecimal maxCountBig = MyMathUtils.divideByScale(minCountBig, hsNum, 2);
//            tvMaxCount.setText(maxCountBig.toString());
//        }
//    }
//    public String getMinCount(String count, final ShopInfoBean.Data item){
//        if (MyStringUtil.isEmpty(count)){
//            return "";
//        }else{
//            BigDecimal hsNum = new BigDecimal(1);
//            BigDecimal maxCountBig = new BigDecimal(count);
//            if (MyStringUtil.isNumber(item.getHsNum())){
//                hsNum = new BigDecimal(item.getHsNum());
//            }
//            BigDecimal minCountBig = MyMathUtils.multiply(maxCountBig, hsNum, 2);
//            return MyMathUtils.clearZero(minCountBig);
//        }
//    }




}
