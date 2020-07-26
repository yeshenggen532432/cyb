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
 * dialog(输入)
 */

public class MyEditDialog extends BaseDialog<MyEditDialog> {
    private Context context;
    private TextView tv_title;
    private EditText mEtName;
    private Button sb_ok;
    private Button sb_cancel;
    private String title;

    public MyEditDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyEditDialog(Context context, String title) {
        super(context);
        this.context = context;
        this.title = title;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(context, R.layout.x_dialog_my_edit, null);
        tv_title = inflate.findViewById(R.id.tv_title);
        mEtName = inflate.findViewById(R.id.et_name);
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
                    String text = mEtName.getText().toString().trim();
                    listener.setOnClickListener(text);
                }
            }
        });
    }


    public interface OnClickListener{
        void setOnClickListener(String text);
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

}