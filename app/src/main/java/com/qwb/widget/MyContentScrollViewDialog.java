package com.qwb.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.MyStringUtil;
import com.chiyong.t3.R;

/**
 * 中间内容可滑动
 */
public class MyContentScrollViewDialog extends BaseDialog<MyContentScrollViewDialog> {
    private Context context;
    private TextView tv_title, mTvContext;
    private View layout_cancel, layout_ok;
    private String title, content;

    public MyContentScrollViewDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyContentScrollViewDialog(Context context, String content) {
        super(context);
        this.context = context;
        this.content = content;
    }

    public MyContentScrollViewDialog(Context context, String content, String title) {
        super(context);
        this.context = context;
        this.content = content;
        this.title = title;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(context, R.layout.x_dialog_my_content_scroll, null);
        tv_title = inflate.findViewById(R.id.tv_title);
        mTvContext = inflate.findViewById(R.id.tv_content);
        layout_cancel = inflate.findViewById(R.id.layout_cancel);
        layout_ok = inflate.findViewById(R.id.layout_ok);

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        if(MyStringUtil.isNotEmpty(title)){
            tv_title.setText(title);
        }
        if(MyStringUtil.isNotEmpty(content)){
            mTvContext.setText(content);
        }
        layout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
    }


    public interface OnClickListener{
        void setOnClickListener();
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }

}