package com.qwb.view.car.adapter;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;

/**
 * 车销配货
 */
public class CarStkOutRightAdapter extends BaseQuickAdapter<ShopInfoBean.Data,BaseViewHolder> {
    public static final int TAG_DW = 1;
    public static final int TAG_XSTP = 2;
    public static final int TAG_DEl = 3;
    public static final int TAG_COUNT = 4;
    public static final int TAG_PRICE = 5;

    public CarStkOutRightAdapter() {
        super(R.layout.x_table_right_car_stk_out);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ShopInfoBean.Data item) {
        helper.setIsRecyclable(false);

        TextView tvGG = helper.getView(R.id.tv_table_content_right_item1);
        final TextView tvDw = helper.getView(R.id.tv_table_content_right_item2);
        final EditText etCount = helper.getView(R.id.tv_table_content_right_item3);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item4);

        final int position = helper.getAdapterPosition();

        tvGG.setText(item.getWareGg());
        tvDw.setText(item.getCurrentDw() + " ▼");
        etCount.setText(item.getCurrentCount());
        tvDel.setText("删除");

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
                item.setCurrentCount(count);
                if(null != listener){
                    listener.onChildListener(TAG_COUNT, position, item);
                }
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


}
