package com.qwb.view.step.adapter;

import android.content.Context;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.view.step.model.ShopInfoBean;
import com.chiyong.t3.R;
import com.zyyoona7.lib.EasyPopup;

/**
 * 步骤5:左边商品
 */
public class Step4Right2Adapter extends BaseQuickAdapter<ShopInfoBean.Data,BaseViewHolder> {
    private int mCurrentPosition;
    private ShopInfoBean.Data mCurrentItem;
    private Context mContext;
    public static final int TAG_XSTP = 2;
    public static final int TAG_DEl = 3;

    public Step4Right2Adapter(Context context) {
        super(R.layout.x_step4_table_right_item);
        this.mContext = context;
        createPopupXxz();//新鲜值
    }

    @Override
    protected void convert( BaseViewHolder helper, final ShopInfoBean.Data item) {
        helper.setIsRecyclable(false);
        final View parent = helper.getView(R.id.parent);
        EditText etDhl = helper.getView(R.id.tv_table_content_right_item1);
        EditText etSxl = helper.getView(R.id.tv_table_content_right_item2);
        EditText etKcl = helper.getView(R.id.tv_table_content_right_item3);
        EditText etDd = helper.getView(R.id.tv_table_content_right_item4);
        TextView tvXstp = helper.getView(R.id.tv_table_content_right_item5);
        TextView tvXxz = helper.getView(R.id.tv_table_content_right_item6);
        TextView tvGg = helper.getView(R.id.tv_table_content_right_item7);
        EditText etBz = helper.getView(R.id.tv_table_content_right_item8);
        TextView tvDel = helper.getView(R.id.tv_table_content_right_item9);

        final int position = helper.getAdapterPosition();
        tvGg.setText(item.getWareGg());
        tvXstp.setText(item.getCurrentXstp() + " ▼");
        tvXxz.setText(item.getCurrentXxz() + " ▼");
        tvDel.setText("删除");
        etDhl.setText(item.getCurrentDhl());
        etSxl.setText(item.getCurrentSxl());
        etKcl.setText(item.getCurrentKcl());
        etDd.setText(item.getCurrentDd());
        etBz.setText(item.getCurrentBz());

        tvXstp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != listener){
                    listener.onChildListener(TAG_XSTP, position, item);
                }
            }
        });
        tvXxz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentPosition = position;
                mCurrentItem = item;
                String xxzStr = mCurrentItem.getCurrentXxz();
                if(!MyStringUtil.isEmpty(xxzStr)){
                    if(xxzStr.startsWith("临期")){
                        cb_linqi.setChecked(true);
                        edit_linqi.setText(xxzStr.substring(2, xxzStr.length()));
                    } else{
                        cb_zhengchang.setChecked(true);
                        edit_linqi.setText("");
                    }
                }
                mEasyPopXxz.showAtLocation(parent, Gravity.CENTER, 0, 0);
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
        etDhl.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentDhl(input.toString().trim());
            }
        });
        etSxl.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentSxl(input.toString().trim());
            }
        });
        etKcl.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentKcl(input.toString().trim());
            }
        });
        etDd.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentDd(input.toString().trim());
            }
        });
        etBz.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable input) {
                item.setCurrentBz(input.toString().trim());
            }
        });


    }

    private Step4Right2Adapter.OnChildListener listener;
    public interface OnChildListener{
        void onChildListener(int tag, int position, ShopInfoBean.Data item);
    }

    public void setOnChildListener(Step4Right2Adapter.OnChildListener listener){
        this.listener = listener;
    }


    // 窗体
    private RadioButton cb_linqi;
    private RadioButton cb_zhengchang;
    private EditText edit_linqi;
    private Button btn_queding;
    private Button btn_quxiao;
    private EasyPopup mEasyPopXxz;//新鲜值
    public void createPopupXxz() {
        mEasyPopXxz = new EasyPopup(mContext)
                .setContentView(R.layout.x_popup_xsxj_xxz)
                .createPopup();
        cb_linqi=mEasyPopXxz.getView(R.id.rb_linqi);
        cb_zhengchang=mEasyPopXxz.getView(R.id.rb_zhengchang);
        edit_linqi=mEasyPopXxz.getView(R.id.edit_linqi);
        btn_queding=mEasyPopXxz.getView(R.id.btn_confirm);
        btn_quxiao=mEasyPopXxz.getView(R.id.btn_quxiao);
        btn_quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mEasyPopXxz.dismiss();
            }
        });

        btn_queding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(cb_linqi.isChecked()){
                    String input = edit_linqi.getText().toString();
                    if(MyUtils.isEmptyString(input)){
                        ToastUtils.showCustomToast("临期数量不能为空");
                        return;
                    }else{
                        mCurrentItem.setCurrentXxz("临期" + edit_linqi.getText().toString());
                    }
                }else if (cb_zhengchang.isChecked()){
                    mCurrentItem.setCurrentXxz("正常");
                }
                mEasyPopXxz.dismiss();
                getData().set(mCurrentPosition, mCurrentItem);
                notifyDataSetChanged();
            }
        });
    }

}
