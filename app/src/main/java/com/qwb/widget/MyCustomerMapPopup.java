package com.qwb.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;
import com.zyyoona7.lib.BaseCustomPopup;

/**
 * popup：客户分布图
 */

public class MyCustomerMapPopup extends BaseCustomPopup {

    private Context mContext;

    public MyCustomerMapPopup(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.x_dialog_my_customer_map, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFocusAndOutsideEnable(false)
                .setBackgroundDimEnable(true)
//                .setDimValue(0.5f)
        ;
    }

    private TextView mTvShowCity, mTvShowCustomerType, mTvShowMember;
    private View layout_cancel, layout_reset, layout_ok;
    private CheckBox mCbNormal;

    @Override
    protected void initViews(View view) {
        mTvShowCity = getView(R.id.tv_show_city);
        mTvShowCustomerType = getView(R.id.tv_show_customer_type);
        mTvShowMember = getView(R.id.tv_show_member);
        mCbNormal = getView(R.id.cb_normal);
        layout_cancel = getView(R.id.layout_cancel);
        layout_reset = getView(R.id.layout_reset);
        layout_ok = getView(R.id.layout_ok);

        layout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        layout_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.setReset();
                }
            }
        });

        layout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null){
                    listener.setOnClickListener();
                }
            }
        });

        mTvShowCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.setOnShowCity(mTvShowCity);
                }
            }
        });
        mTvShowCustomerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.setOnShowCustomerType(mTvShowCustomerType);
                }
            }
        });
        mTvShowMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.setOnShowMember(mTvShowMember);
                }
            }
        });
        mCbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.setCbNormal(mCbNormal);
                }
            }
        });
    }


    public void setCityText(String province, String city, String area) {
        StringBuffer sb = new StringBuffer();
        if (MyStringUtil.isNotEmpty(province)){
            sb.append(province);
            if (MyStringUtil.isNotEmpty(city)){
                sb.append("/").append(city);
                if (MyStringUtil.isNotEmpty(area)){
                    sb.append("/").append(area);
                }
            }
        }
        this.mTvShowCity.setText(sb.toString());
    }
    public void setCustomerTypeText(String customerType) {
        if (MyStringUtil.isNotEmpty(customerType)) {
            this.mTvShowCustomerType.setText(customerType);
        }else{
            this.mTvShowCustomerType.setText("");
        }
    }
    public void setMemberText(String memberNames) {
        if (MyStringUtil.isNotEmpty(memberNames)) {
            this.mTvShowMember.setText(memberNames);
        }else{
            this.mTvShowMember.setText("");
        }
    }
    public void setCbNormal(Boolean check) {
        if (check != null && check){
            this.mCbNormal.setChecked(true);
        }else{
            this.mCbNormal.setChecked(false);
        }
    }

    public interface OnClickListener{
        void setOnClickListener();
        void setOnShowCity(TextView tvShowCity);
        void setOnShowCustomerType(TextView tvShowCustomerType);
        void setOnShowMember(TextView tvShowMember);
        void setCbNormal(CheckBox cbNormal);
        void setReset();
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }


}
