package com.qwb.widget;

import android.content.Context;
import android.os.health.TimerStat;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyChooseTimeUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.utils.MyTimeUtils;
import com.xmsx.qiweibao.R;

import java.lang.reflect.Type;

/**
 * 我的拜访-选择时间
 */
public class MyCallChooseDateDialog extends BaseDialog<MyCallChooseDateDialog> {
    private Context context;
    private View sb_ok;
    private View sb_cancel;

    RadioGroup radioGroup;
    private TextView tv_startTime;
    private TextView tv_endTime;

    public MyCallChooseDateDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyCallChooseDateDialog(Context context,  String startDate, String endDate, int type) {
        super(context);
        this.context = context;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.type = type;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        View inflate = View.inflate(context, R.layout.x_dialog_my_call_choose_date, null);
        radioGroup = inflate.findViewById(R.id.radioGroup);
        tv_startTime = inflate.findViewById(R.id.tv_startTime);
        tv_endTime = inflate.findViewById(R.id.tv_endTime);
        sb_cancel = inflate.findViewById(R.id.layout_cancel);
        sb_ok = inflate.findViewById(R.id.layout_ok);

        doIntent();

        return inflate;
    }

    private void doIntent() {
        switch (type){
            case 1:
                radioGroup.check(R.id.radio0);
            break;
            case 2:
                radioGroup.check(R.id.radio1);
            break;
            case 3:
                radioGroup.check(R.id.radio2);
            break;
            case 4:
                radioGroup.check(R.id.radio3);
            break;
            case 5:
                radioGroup.check(R.id.radio4);
            break;
            case 6:
                radioGroup.check(R.id.radio5);
            break;
            case 7:
                radioGroup.check(R.id.radio6);
            break;
        }

        if (MyStringUtil.isEmpty(mStartDate)){
           mStartDate = MyTimeUtils.getToday_nyr();
        }
        if (MyStringUtil.isEmpty(mEndDate)){
           mEndDate = MyTimeUtils.getToday_nyr();
        }
        tv_startTime.setText(mStartDate);
        tv_endTime.setText(mEndDate);
    }

    @Override
    public void setUiBeforShow() {
        tv_startTime.setText(MyTimeUtils.getToday_nyr());
        tv_endTime.setText(MyTimeUtils.getToday_nyr());
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
                    listener.setOnClickListener(mTimeTitle, mStartDate, mEndDate, type);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        setData_time("今天",  MyTimeUtils.getToday_nyr(),  MyTimeUtils.getToday_nyr(),1);
                        break;
                    case R.id.radio1:
                        setData_time("昨天",  MyTimeUtils.getYesterday(),  MyTimeUtils.getYesterday(), 2);
                        break;
                    case R.id.radio2:
                        setData_time("本周", MyTimeUtils.getFirstOfThisWeek(), MyTimeUtils.getLastOfThisWeek(), 3);
                        break;
                    case R.id.radio3:
                        setData_time("上周",  MyTimeUtils.getFirstOfShangWeek(),  MyTimeUtils.getLastOfShangWeek(), 4);
                        break;
                    case R.id.radio4:
                        setData_time("本月", MyTimeUtils.getFirstOfMonth(), MyTimeUtils.getLastOfMonth(), 5);
                        break;
                    case R.id.radio5:
                        setData_time("上月", MyTimeUtils.getFirstOfShangMonth(), MyTimeUtils.getLastOfShangMonth(), 6);
                        break;
                    case R.id.radio6:
                        setData_time("自定义", tv_startTime.getText().toString(), tv_endTime.getText().toString(), 7);
                        break;
                }
            }
        });

        tv_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate("开始时间", mStartDate, 1);
            }
        });
        tv_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate("结束时间", mEndDate, 2);
            }
        });
    }

    // 设置时间等状态，今天，昨天，本周，上周，本月，上月
    private int type;
    private String mTimeTitle;
    private String mStartDate, mEndDate;
    private void setData_time(String timeTitle, String startDate, String endDate, int type) {
        mTimeTitle = timeTitle;
        mStartDate = startDate;
        mEndDate = endDate;
        this.type = type;
    }

    public interface OnClickListener{
        void setOnClickListener(String timeTitle, String startDate, String endDate, int type);
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }


    public void chooseDate(String title, String date, final int dateType){
        new com.qwb.widget.MyDatePickerDialog(context, title, date, new com.qwb.widget.MyDatePickerDialog.DateTimeListener() {
            @Override
            public void onSetTime(int year, int month, int day, String timeStr) {
                if (1 == dateType){
                    mStartDate = timeStr;
                    tv_startTime.setText(timeStr);
                }else{
                    mEndDate = timeStr;
                    tv_endTime.setText(timeStr);
                }
            }

            @Override
            public void onCancel() {
            }
        }).show();
    }

}