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
import com.chiyong.t3.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 同时显示“年月日”，“时分”时间控件（DatePicker，TimePicker）
 */

public class MyDoubleDatePickerDialog extends AlertDialog implements View.OnClickListener {

    private Context mContext;
    private DatePicker mDatePicker2;
    private DatePicker mDatePicker;
    private TextView mTvTitle, mTvOk, mTvCancel;
    private int mYear, mMonth, mDay;
    private int mYear2, mMonth2, mDay2;
    private String title;//标题
    DateTimeListener listener;

    public MyDoubleDatePickerDialog(Context context, String title, DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    public MyDoubleDatePickerDialog(Context context, String title,
                                    int mYear, int mMonth, int mDay,
                                    int mYear2, int mMonth2, int mDay2,
                                    DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.mYear = mYear;
        this.mMonth = mMonth;
        this.mDay = mDay;
        this.mYear2 = mYear2;
        this.mMonth2 = mMonth2;
        this.mDay2 = mDay2;
        this.listener = listener;
    }

    public MyDoubleDatePickerDialog(Context context, String title, String startDate, String endDate, DateTimeListener listener) {
        // TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
        if (MyStringUtil.isNotEmpty(startDate)){
            String[] dateS = startDate.split("-");
            this.mYear = Integer.valueOf(dateS[0]);
            this.mMonth = Integer.valueOf(dateS[1]) - 1;
            this.mDay = Integer.valueOf(dateS[2]);
        }else{
            //默认今天
            mYear = MyTimeUtils.getYear();
            mMonth = MyTimeUtils.getMonth();
            mDay = MyTimeUtils.getDay();
        }
        if (MyStringUtil.isNotEmpty(endDate)){
            String[] dateS = endDate.split("-");
            this.mYear2 = Integer.valueOf(dateS[0]);
            this.mMonth2 = Integer.valueOf(dateS[1]) - 1;
            this.mDay2 = Integer.valueOf(dateS[2]);
        }else{
            //默认今天
            mYear2 = MyTimeUtils.getYear();
            mMonth2 = MyTimeUtils.getMonth();
            mDay2 = MyTimeUtils.getDay();
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
        setContentView(R.layout.x_dialog_double_date_picker);
        initView();
    }

    private void initView() {
        mDatePicker2 = (DatePicker) findViewById(R.id.dateAndTimePicker_timePicker);
        mDatePicker = (DatePicker) findViewById(R.id.dateAndTimePicker_datePicker);
        mTvTitle = (TextView) findViewById(R.id.mytimedialog_tp_title);
        mTvOk = (TextView) findViewById(R.id.mytimedialog_bt_ok);
        mTvCancel = (TextView) findViewById(R.id.mytimedialog_bt_cancel);
        mDatePicker.init(mYear, mMonth, mDay, null);//设置年，月，日
        mDatePicker2.init(mYear2, mMonth2, mDay2, null);//设置年，月，日
        resizePikcer(mDatePicker2);//调整FrameLayout大小
        resizePikcer(mDatePicker);//调整FrameLayout大小
        setDatePickerDividerColor(mDatePicker2);//设置分割线的颜色
        setDatePickerDividerColor(mDatePicker);//设置分割线的颜色

        mTvTitle.setText(title);
        mTvOk.setOnClickListener(this);
        mTvCancel.setOnClickListener(this);
    }

    /*
    * 调整numberpicker大小
    */
    private void resizeNumberPicker(NumberPicker np) {
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)mContext.getResources().getDimension(R.dimen.x100),LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins((int)mContext.getResources().getDimension(R.dimen.x5), 0, (int)mContext.getResources().getDimension(R.dimen.x5), 0);
//        np.setLayoutParams(params);
        //TODO 正式要上面做适配，不然720*1280有问题
        int width = (int) mContext.getResources().getDimension(R.dimen.dp_40);
        int offse = (int) mContext.getResources().getDimension(R.dimen.dp_2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(offse, 0, offse, 0);
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
     * 设置时间选择器的分割线颜色
     */
    private void setDatePickerDividerColor(DatePicker datePicker) {
        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < mSpinners.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        int colorRes = mContext.getResources().getColor(R.color.blue);
                        ColorDrawable colorDrawable = new ColorDrawable(colorRes);
                        pf.set(picker, colorDrawable);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
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

    public int getYear2() {
        return mYear2;
    }

    public int getDay2() {
        return mDay2;
    }

    public int getMonth2() {
        //返回的时间是0-11
        return mMonth2 + 1;
    }


    /**
     * 获取日期选择的值
     */
    private void getDatePickerValue() {
        mYear = mDatePicker.getYear();
        mMonth = mDatePicker.getMonth();
        mDay = mDatePicker.getDayOfMonth();
    }

    /**
     * 获取时间选择的值
     */
    private void getTimePickerValue() {
        // api23这两个方法过时
        mYear2 = mDatePicker2.getYear();
        mMonth2 = mDatePicker2.getMonth();
        mDay2 = mDatePicker2.getDayOfMonth();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvOk) {
            dismiss();
            getDatePickerValue();
            getTimePickerValue();
            StringBuffer startDate = new StringBuffer();
            startDate.append(mYear);
            if (mMonth + 1 < 10) {
                startDate.append("-0" + (mMonth + 1));
            } else {
                startDate.append("-" + (mMonth + 1));
            }
            if (mDay < 10) {
                startDate.append("-0" + mDay);
            } else {
                startDate.append("-" + mDay);
            }
            StringBuffer endDate = new StringBuffer();
            endDate.append(mYear2);
            if (mMonth2 + 1 < 10) {
                endDate.append("-0" + (mMonth2 + 1));
            } else {
                endDate.append("-" + (mMonth2 + 1));
            }
            if (mDay2 < 10) {
                endDate.append("-0" + mDay2);
            } else {
                endDate.append("-" + mDay2);
            }
            listener.onSetTime(mYear, mMonth, mDay, mYear2, mMonth2, mDay2, startDate.toString(), endDate.toString());
        } else {
            dismiss();
            listener.onCancel();
        }
    }

    public static interface DateTimeListener {
        void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate);

        void onCancel();
    }
}
