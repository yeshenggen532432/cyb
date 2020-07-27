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
 *  选择时间（4.0风格）：同时显示“年月日”，“时分”时间控件（DatePicker，TimePicker）
 */

public class MyTimePickerDialog extends AlertDialog implements View.OnClickListener{

    private Context mContext;
    private int mHour, mMinute;
    private TimePicker mTimePicker;
    private TextView mTvTitle,mTvOk,mTvCancel;
    private String title;//标题
    DateTimeListener listener;

    public MyTimePickerDialog(Context context, String title, DateTimeListener timeSetListener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
//        super(context);
        this.mContext = context;
        this.title = title;
        this.listener = timeSetListener;
    }
    public MyTimePickerDialog(Context context, String title,int mHour,int mMinute, DateTimeListener timeSetListener) {
        //        TODO 改变字体大小，颜色-在构造函数中指定 style
        super(context, R.style.Theme_picker);
//        super(context);
        this.mContext = context;
        this.title = title;
        this.mHour = mHour;
        this.mMinute = mMinute;
        this.listener = timeSetListener;
    }

    /**
     * 创建视图
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.x_dialog_time_picker);
        initView();
    }

    private void initView() {
        mTimePicker = (TimePicker) findViewById(R.id.dateAndTimePicker_timePicker);
        mTvTitle = (TextView) findViewById(R.id.mytimedialog_tp_title);
        mTvOk = (TextView) findViewById(R.id.mytimedialog_bt_ok);
        mTvCancel = (TextView) findViewById(R.id.mytimedialog_bt_cancel);
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(mHour);//设置时
        mTimePicker.setCurrentMinute(mMinute);//设置分
        resizePikcer(mTimePicker);//调整FrameLayout大小
        //修改分割线颜色
        Resources systemResources = Resources.getSystem();
        int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
        NumberPicker hourNumberPicker = (NumberPicker) mTimePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = (NumberPicker) mTimePicker.findViewById(minuteNumberPickerId);
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
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 0);
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
            getTimePickerValue();
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
            listener.onSetTime(mHour,mMinute,time.toString());
        } else {
            dismiss();
            listener.onCancel();
        }
    }

    public static interface DateTimeListener {
        void onSetTime(int hour, int minute, String timeStr);
        void onCancel();
    }
}
