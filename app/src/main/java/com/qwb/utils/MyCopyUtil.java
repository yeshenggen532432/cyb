package com.qwb.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xmsx.qiweibao.R;

/**
    复制工具类
 */
public class MyCopyUtil {

    private static MyCopyUtil MANAGER = null;
    public static MyCopyUtil getInstance() {
        if (MANAGER == null) {
            MANAGER = new MyCopyUtil();
        }
        return MANAGER;
    }

    private ClipboardManager clipboardManager;
    private float scale;
    private PopupWindow typePopupWindow;
    public void copyText(final Activity activity, View v, final String copyContent) {
        scale = activity.getResources().getDisplayMetrics().density;
        TextView mPopupText = new TextView(activity);
        mPopupText.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if (clipboardManager == null) {
                    clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
                }
                if (MyStringUtil.isEmpty(copyContent)) {
                    ToastUtils.showCustomToast("复制的内容不能为空");
                } else {
                    clipboardManager.setText(copyContent);
                    ToastUtils.showCustomToast("复制成功");
                }
                typePopupWindow.dismiss();
            }
        });
        mPopupText.setBackgroundResource(R.drawable.copy_bg);
        mPopupText.setTextColor(Color.WHITE);
        mPopupText.setPadding(0, 0, 0, 15);
        mPopupText.setText("复制");
        mPopupText.setGravity(Gravity.CENTER);
        typePopupWindow = new PopupWindow(mPopupText, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        typePopupWindow.setBackgroundDrawable(new BitmapDrawable());

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        TextView tv_copy = (TextView) v;
        int height = tv_copy.getHeight();
        int width = tv_copy.getWidth();
        typePopupWindow.showAsDropDown(v, (int) (width / 2 - 40 * scale), (int) (-height - 25 * scale));
    }

}
