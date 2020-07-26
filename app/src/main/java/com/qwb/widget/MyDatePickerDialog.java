package com.qwb.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTimeUtils;
import com.xmsx.qiweibao.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择“年月日”时间控件（DatePicker）（4.0风格）
 */

public class MyDatePickerDialog extends AlertDialog implements View.OnClickListener {

    private Context mContext;
    private DatePicker mDatePicker;
    private TextView mTvTitle, mTvOk, mTvCancel;
    private int mYear, mDay, mMonth;
    private String title;//标题
    DateTimeListener listener;

    public MyDatePickerDialog(Context context, String title, DateTimeListener listener) {
//        super(context);
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    public MyDatePickerDialog(Context context, String title, int mYear, int mMonth, int mDay, DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.listener = listener;
    }

    public MyDatePickerDialog(Context context, String title, String date, DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
        if (MyStringUtil.isNotEmpty(date)){
            String[] dateS = date.split("-");
            this.mYear = Integer.valueOf(dateS[0]);
            this.mMonth = Integer.valueOf(dateS[1]) - 1;
            this.mDay = Integer.valueOf(dateS[2]);
        }else{
            //默认今天
            mYear = MyTimeUtils.getYear();
            mMonth = MyTimeUtils.getMonth();
            mDay = MyTimeUtils.getDay();
        }
    }

    /**
     * 创建视图
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_dialog_date_picker);
        initView();
    }

    private void initView() {
        mDatePicker = findViewById(R.id.dateAndTimePicker_datePicker);
        mTvTitle = findViewById(R.id.mytimedialog_tp_title);
        mTvOk = findViewById(R.id.mytimedialog_bt_ok);
        mTvCancel = findViewById(R.id.mytimedialog_bt_cancel);
        mDatePicker.init(mYear, mMonth, mDay, null);//设置年，月，日
        resizePikcer(mDatePicker);//调整FrameLayout大小
        //修改分割线颜色
        Resources systemResources = Resources.getSystem();
        int yearNumberPickerId = systemResources.getIdentifier("year", "id", "android");
        int monthNumberPickerId = systemResources.getIdentifier("month", "id", "android");
        int dayNumberPickerId = systemResources.getIdentifier("day", "id", "android");
        NumberPicker yearNumberPicker = (NumberPicker) mDatePicker.findViewById(yearNumberPickerId);
        NumberPicker monthNumberPicker = (NumberPicker) mDatePicker.findViewById(monthNumberPickerId);
        NumberPicker dayNumberPicker = (NumberPicker) mDatePicker.findViewById(dayNumberPickerId);
        setNumberPickerDivider(yearNumberPicker);
        setNumberPickerDivider(monthNumberPicker);
        setNumberPickerDivider(dayNumberPicker);

        mTvTitle.setText(title);
        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    /*
    * 调整numberpicker大小
    */
    private void resizeNumberPicker(NumberPicker np) {
        //TODO 正式要上面做适配，不然720*1280有问题
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 0);
        np.setLayoutParams(params);
    }

    /**
     * 调整FrameLayout大小
     *
     * @param tp
     */
    private void resizePikcer(FrameLayout tp) {
        List<NumberPicker> npList = findNumberPicker(tp);
        for (NumberPicker np : npList) {
            resizeNumberPicker(np);
        }
    }

    /**
     * 得到viewGroup里面的numberpicker组件
     *
     * @param viewGroup
     * @return
     */
    private List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
        List<NumberPicker> npList = new ArrayList<NumberPicker>();
        View child = null;
        if (null != viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                child = viewGroup.getChildAt(i);
                if (child instanceof NumberPicker) {
                    npList.add((NumberPicker) child);
                } else if (child instanceof LinearLayout) {
                    List<NumberPicker> result = findNumberPicker((ViewGroup) child);
                    if (result.size() > 0) {
                        return result;
                    }
                }
            }
        }
        return npList;
    }

    /**
     * 修改分割线颜色
     *
     * @param numberPicker
     */
    private void setNumberPickerDivider(NumberPicker numberPicker) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                Field dividerField = numberPicker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
//                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#009fe8"));
                int colorRes = mContext.getResources().getColor(R.color.blue);
                ColorDrawable colorDrawable = new ColorDrawable(colorRes);
                dividerField.set(numberPicker, colorDrawable);
                numberPicker.invalidate();
            } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            }
        }
    }

    public int getYear() {
        return mYear;
    }

    public int getDay() {
        return mDay;
    }

    public int getMonth() {
        //返回的时间是0-11
        return mMonth + 1;
    }

    /**
     * 获取日期选择的值
     */
    private void getDatePickerValue() {
        mYear = mDatePicker.getYear();
        mMonth = mDatePicker.getMonth();
        mDay = mDatePicker.getDayOfMonth();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvOk) {
            dismiss();
            getDatePickerValue();
            //拼接：不足十加“0”
            StringBuffer date = new StringBuffer();
            date.append(mYear);
            if (mMonth + 1 < 10) {
                date.append("-0" + (mMonth + 1));
            } else {
                date.append("-" + (mMonth + 1));
            }
            if (mDay < 10) {
                date.append("-0" + mDay);
            } else {
                date.append("-" + mDay);
            }
            listener.onSetTime(mYear, mMonth, mDay, date.toString());
        } else {
            dismiss();
            listener.onCancel();
        }
    }

    public static interface DateTimeListener {
        void onSetTime(int year, int month, int day, String timeStr);

        void onCancel();
    }
}
