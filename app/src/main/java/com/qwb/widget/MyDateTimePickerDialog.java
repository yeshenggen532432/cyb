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
import android.widget.TimePicker;

import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTimeUtils;
import com.xmsx.qiweibao.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *  选择时间（4.0风格）：同时显示“年月日”，“时分”时间控件（DatePicker，TimePicker）
 */

public class MyDateTimePickerDialog extends AlertDialog implements View.OnClickListener{

    private Context mContext;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private TextView mTvTitle,mTvOk,mTvCancel;
    private int mYear, mDay, mMonth,mHour, mMinute;
    private String title;//标题
    DateTimeListener listener;

    public MyDateTimePickerDialog(Context context, String title, DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
//        super(context);
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }

    public MyDateTimePickerDialog(Context context, String title,int year,int month,int day,int hour,int minute, DateTimeListener listener) {
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
        this.mContext = context;
        this.mYear = year;
        this.mMonth = month;
        this.mDay = day;
        this.mHour = hour;
        this.mMinute = minute;
    }

    public MyDateTimePickerDialog(Context context, String title,String time, DateTimeListener listener) {
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
        this.mContext = context;
        if(MyStringUtil.isEmpty(time)){
            this.mYear = MyTimeUtils.getYear();
            this.mMonth = MyTimeUtils.getMonth();
            this.mDay = MyTimeUtils.getDay();
            this.mHour = MyTimeUtils.getHour();
            this.mMinute = MyTimeUtils.getMin();
        }else {
            Calendar calendar = MyTimeUtils.getStrToDate(time);
            if(calendar != null){
                this.mYear = calendar.get(Calendar.YEAR);
                this.mMonth = calendar.get(Calendar.MONTH);
                this.mDay = calendar.get(Calendar.DAY_OF_MONTH);
                this.mHour = calendar.get(Calendar.HOUR_OF_DAY);
                this.mMinute = calendar.get(Calendar.MINUTE);
            }
        }

    }

    /**
     * 创建视图
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_dialog_date_time_picker);
        initView();
    }

    private void initView() {
        mTimePicker = (TimePicker) findViewById(R.id.dateAndTimePicker_timePicker);
        mDatePicker = (DatePicker) findViewById(R.id.dateAndTimePicker_datePicker);
        mTvTitle = (TextView) findViewById(R.id.mytimedialog_tp_title);
        mTvOk = (TextView) findViewById(R.id.mytimedialog_bt_ok);
        mTvCancel = (TextView) findViewById(R.id.mytimedialog_bt_cancel);
        mTimePicker.setIs24HourView(true);
        mDatePicker.init(mYear,mMonth,mDay,null);//设置年，月，日
        mTimePicker.setCurrentHour(mHour);//设置时
        mTimePicker.setCurrentMinute(mMinute);//设置分
        resizePikcer(mTimePicker);//调整FrameLayout大小
        resizePikcer(mDatePicker);//调整FrameLayout大小
        //修改分割线颜色
        Resources systemResources = Resources.getSystem();
        int yearNumberPickerId = systemResources.getIdentifier("year", "id", "android");
        int monthNumberPickerId = systemResources.getIdentifier("month", "id", "android");
        int dayNumberPickerId = systemResources.getIdentifier("day", "id", "android");
        int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
        NumberPicker yearNumberPicker = (NumberPicker) mDatePicker.findViewById(yearNumberPickerId);
        NumberPicker monthNumberPicker = (NumberPicker) mDatePicker.findViewById(monthNumberPickerId);
        NumberPicker dayNumberPicker = (NumberPicker) mDatePicker.findViewById(dayNumberPickerId);
        NumberPicker hourNumberPicker = (NumberPicker) mTimePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) mTimePicker.findViewById(minuteNumberPickerId);
        setNumberPickerDivider(yearNumberPicker);
        setNumberPickerDivider(monthNumberPicker);
        setNumberPickerDivider(dayNumberPicker);
        setNumberPickerDivider(hourNumberPicker);
        setNumberPickerDivider(minuteNumberPicker);

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
        int width=(int)mContext.getResources().getDimension(R.dimen.dp_50);
        int offse=(int)mContext.getResources().getDimension(R.dimen.dp_2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(offse, 0, offse, 0);
        np.setLayoutParams(params);
    }

    /**
     * 调整FrameLayout大小
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
     * @param numberPicker
     */
    private void setNumberPickerDivider(NumberPicker numberPicker) {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            try{
                Field dividerField = numberPicker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
//                ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#009fe8"));
                int colorRes=mContext.getResources().getColor(R.color.blue);
                ColorDrawable colorDrawable = new ColorDrawable(colorRes);
                dividerField.set(numberPicker,colorDrawable);
                numberPicker.invalidate();
            }
            catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException e){
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
        return mMonth+1;
    }

    public int getMinute() {
        return mMinute;
    }

    public int getHour() {
        return mHour;
    }

    /**
     * 获取日期选择的值
     */
    private void getDatePickerValue() {
        mYear = mDatePicker.getYear();
        mMonth = mDatePicker.getMonth();
        mDay= mDatePicker.getDayOfMonth();
    }

    /**
     * 获取时间选择的值
     */
    private void getTimePickerValue() {
        // api23这两个方法过时
        mHour = mTimePicker.getCurrentHour();// timePicker.getHour();
        mMinute = mTimePicker.getCurrentMinute();// timePicker.getMinute();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvOk) {
            dismiss();
            getDatePickerValue();
            getTimePickerValue();
            //拼接：不足十加“0”
            StringBuffer time = new StringBuffer();
            time.append(mYear);
            if (mMonth+1 < 10) {
                time.append("-0" + (mMonth+1));
            } else {
                time.append("-" + (mMonth+1));
            }
            if (mDay < 10) {
                time.append("-0" + mDay);
            } else {
                time.append("-" + mDay);
            }
            if (mHour < 10) {
                time.append(" 0" + mHour);
            } else {
                time.append(" " + mHour);
            }
            if (mMinute < 10) {
                time.append(":0" + mMinute);
            } else {
                time.append(":" + mMinute);
            }
            listener.onSetTime(mYear, mMonth, mDay, mHour,mMinute,time.toString());
        } else {
            dismiss();
            listener.onCancel();
        }
    }

    public static interface DateTimeListener {
        void onSetTime(int year, int month, int day, int hour, int minute, String timeStr);
        void onCancel();
    }
}
