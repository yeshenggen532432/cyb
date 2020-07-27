package com.xmsx.cnlife.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.chiyong.t3.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 *  同时显示“时分”，“时分”时间控件（TimePicker，TimePicker）（4.0风格）
 */

public class MyDoubleTimePickerDialog extends AlertDialog implements View.OnClickListener{

    private Context mContext;
    private int mHour, mMinute,mHour2, mMinute2;
    private TimePicker mTimePicker;
    private TimePicker mTimePicker2;
    private TextView mTvTitle,mTvOk,mTvCancel;
    private String title;//标题
    DateTimeListener listener;

    public MyDoubleTimePickerDialog(Context context, String title, DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.listener = listener;
    }
    public MyDoubleTimePickerDialog(Context context, String title,int mHour, int mMinute,int mHour2, int mMinute2, DateTimeListener listener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
        this.mContext = context;
        this.title = title;
        this.mHour = mHour;
        this.mMinute = mMinute;
        this.mHour2 = mHour2;
        this.mMinute2 = mMinute2;
        this.listener = listener;
    }

    /**
     * 创建视图
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_dialog_double_time_picker);
        initView();
    }

    private void initView() {
        mTimePicker = (TimePicker) findViewById(R.id.dateAndTimePicker_timePicker);
        mTimePicker2 = (TimePicker) findViewById(R.id.dateAndTimePicker_timePicker2);
        mTvTitle = (TextView) findViewById(R.id.mytimedialog_tp_title);
        mTvOk = (TextView) findViewById(R.id.mytimedialog_bt_ok);
        mTvCancel = (TextView) findViewById(R.id.mytimedialog_bt_cancel);
        mTimePicker.setIs24HourView(true);
        mTimePicker2.setIs24HourView(true);
        mTimePicker.setCurrentHour(mHour);//设置时
        mTimePicker.setCurrentMinute(mMinute);//设置分
        mTimePicker2.setCurrentHour(mHour2);//设置时
        mTimePicker2.setCurrentMinute(mMinute2);//设置分
        resizePikcer(mTimePicker);//调整FrameLayout大小
        resizePikcer(mTimePicker2);//调整FrameLayout大小
        //修改分割线颜色
        Resources systemResources = Resources.getSystem();
        int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
        NumberPicker hourNumberPicker = (NumberPicker) mTimePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) mTimePicker.findViewById(minuteNumberPickerId);
        NumberPicker hourNumberPicker2 = (NumberPicker) mTimePicker2.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker2 = (NumberPicker) mTimePicker2.findViewById(minuteNumberPickerId);
        setNumberPickerDivider(hourNumberPicker);
        setNumberPickerDivider(minuteNumberPicker);
        setNumberPickerDivider(hourNumberPicker2);
        setNumberPickerDivider(minuteNumberPicker2);

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
        //TODO 正式上面要做适配，不然720*1280有问题
        int width=(int)mContext.getResources().getDimension(R.dimen.dp_50);
        int offse=(int)mContext.getResources().getDimension(R.dimen.dp_5);
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

    public int getMinute() {
        return mMinute;
    }

    public int getHour() {
        return mHour;
    }

    public int getMinute2() {
        return mMinute2;
    }

    public int getHour2() {
        return mHour2;
    }

    /**
     * 获取时间选择的值
     */
    private void getTimePickerValue() {
        // api23这两个方法过时
        mHour = mTimePicker.getCurrentHour();// timePicker.getHour();
        mMinute = mTimePicker.getCurrentMinute();// timePicker.getMinute();
    }
    /**
     * 获取时间选择的值
     */
    private void getTimePickerValue2() {
        // api23这两个方法过时
        mHour2 = mTimePicker2.getCurrentHour();// timePicker.getHour();
        mMinute2 = mTimePicker2.getCurrentMinute();// timePicker.getMinute();
    }

    @Override
    public void onClick(View v) {
        if (v == mTvOk) {
            dismiss();
            getTimePickerValue();
            getTimePickerValue2();
            //拼接：不足十加“0”
            StringBuffer time = new StringBuffer();
            if (mHour < 10) {
                time.append("0" + mHour);
            } else {
                time.append("" + mHour);
            }
            if (mMinute < 10) {
                time.append(":0" + mMinute);
            } else {
                time.append(":" + mMinute);
            }
            StringBuffer time2 = new StringBuffer();
            if (mHour2 < 10) {
                time2.append("0" + mHour2);
            } else {
                time2.append("" + mHour2);
            }
            if (mMinute2 < 10) {
                time2.append(":0" + mMinute2);
            } else {
                time2.append(":" + mMinute2);
            }
            listener.onSetTime(mHour,mMinute, mHour2,mMinute2,time.toString(),time2.toString());
        } else {
            dismiss();
            listener.onCancel();
        }
    }

    public static interface DateTimeListener {
        void onSetTime(int hour, int minute, int hour2, int minute2, String startTime, String endTime);
        void onCancel();
    }
}
