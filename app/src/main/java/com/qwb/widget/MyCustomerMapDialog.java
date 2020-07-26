package com.qwb.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.MyStringUtil;
import com.xmsx.qiweibao.R;

/**
 * 客户分布图-筛选
 */
public class MyCustomerMapDialog extends BaseDialog<MyCustomerMapDialog> {
    private Context context;
    private TextView tv_title, mTvShowCity, mTvShowCustomerType, mTvShowMember;
    private Button sb_ok;
    private Button sb_cancel;
    private String title;

    public MyCustomerMapDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyCustomerMapDialog(Context context, String title) {
        super(context);
        this.context = context;
        this.title = title;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(context, R.layout.x_dialog_my_customer_map, null);
        tv_title = inflate.findViewById(R.id.tv_title);
        mTvShowCity = inflate.findViewById(R.id.tv_show_city);
        mTvShowCustomerType = inflate.findViewById(R.id.tv_show_customer_type);
        mTvShowMember = inflate.findViewById(R.id.tv_show_member);
        sb_ok = inflate.findViewById(R.id.sb_ok);
        sb_cancel = inflate.findViewById(R.id.sb_cancel);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        if(MyStringUtil.isNotEmpty(title)){
            tv_title.setText(title);
        }
        sb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        sb_ok.setOnClickListener(new View.OnClickListener() {
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
    }


    public void setCityText(String province, String city, String area) {
        if (MyStringUtil.isNotEmpty(city)) {
            this.mTvShowCity.setText(province+"/"+city+"/"+area);
        }else{
            this.mTvShowMember.setText("");
        }
    }
    public void setCustomerTypeText(String customerType) {
        if (MyStringUtil.isNotEmpty(customerType)) {
            this.mTvShowCustomerType.setText(customerType);
        }else{
            this.mTvShowMember.setText("");
        }
    }
    public void setMemberText(String memberNames) {
        if (MyStringUtil.isNotEmpty(memberNames)) {
            this.mTvShowMember.setText(memberNames);
        }else{
            this.mTvShowMember.setText("");
        }
    }

    public interface OnClickListener{
        void setOnClickListener();
        void setOnShowCity(TextView tvShowCity);
        void setOnShowCustomerType(TextView tvShowCustomerType);
        void setOnShowMember(TextView tvShowMember);
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

}