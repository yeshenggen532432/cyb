package com.qwb.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;

/**
 * 车销收款
 */
public class MyCarCollectionDialog extends BaseDialog<MyCarCollectionDialog> {
    private Context context;
    private String mKhNm;
    private String mMoney;

    public MyCarCollectionDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyCarCollectionDialog(Context context, String khNm, String money) {
        super(context);
        this.context = context;
        this.mKhNm = khNm;
        this.mMoney = money;
    }

    private View mViewOk;
    private View mViewCancel;
    private TextView mTvKhNm;
    private EditText mEtMoney;
    private Spinner mSpinner;
    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(context, R.layout.x_dialog_my_car_collection, null);
        mTvKhNm = inflate.findViewById(R.id.tv_khNm);
        mEtMoney = inflate.findViewById(R.id.et_money);
        mSpinner = inflate.findViewById(R.id.spinner);
        mViewCancel = inflate.findViewById(R.id.layout_cancel);
        mViewOk = inflate.findViewById(R.id.layout_ok);

        return inflate;
    }


    @Override
    public void setUiBeforShow() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,  int pos, long id) {
                String[] items = context.getResources().getStringArray(R.array.car_pay_type);
                String item = items[pos];
                if (MyStringUtil.eq("现金", item)){
                    accType = 0;
                }else if (MyStringUtil.eq("微信", item)){
                    accType = 1;
                }else if (MyStringUtil.eq("支付宝", item)){
                    accType = 2;
                }else if (MyStringUtil.eq("银行卡", item)){
                    accType = 3;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        if (MyStringUtil.isNotEmpty(mKhNm)) {
            mTvKhNm.setText(mKhNm);
        }
        if (MyStringUtil.isNotEmpty(mMoney)) {
            mEtMoney.setText(mMoney);
        }

        mViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = mEtMoney.getText().toString().trim();
                if (MyStringUtil.isEmpty(money)){
                    ToastUtils.showCustomToast("请输入金额");
                    return;
                }
                dismiss();
                if (listener != null){
                    listener.setOnClickListener(accType, money);
                }
            }
        });


    }

    private Integer accType;//0 现金 1微信 2支付宝 3银行卡 4 无卡现金
    public interface OnClickListener{
        void setOnClickListener(int accType, String money);
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }



}