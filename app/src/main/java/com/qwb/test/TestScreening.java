package com.qwb.test;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qwb.widget.MyDoubleDatePickerDialog;
import com.qwb.utils.MyTimeUtils;
import com.xmsx.qiweibao.R;

import butterknife.BindView;

/**
 * 筛选
 */

public class TestScreening {

    private Activity context;
    /**
     * 初始化筛选:时间和搜索
     */
    @BindView(R.id.view_search)
    View mViewSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.view_screening_tab1)
    View mViewScreeningTab1;
    @BindView(R.id.view_screening_tab2)
    View mViewScreeningTab2;
    @BindView(R.id.view_screening_tab3)
    View mViewScreeningTab3;
    @BindView(R.id.tv_screening_tab1)
    TextView mTvScreeningTab1;
    @BindView(R.id.tv_screening_tab2)
    TextView mTvScreeningTab2;
    @BindView(R.id.tv_screening_tab3)
    TextView mTvScreeningTab3;
    private String mSearchStr, mOutType;
    private String mStartDate = MyTimeUtils.getFirstOfMonth(), mEndDate = MyTimeUtils.getTodayStr();
    private void initScreening() {
        mTvScreeningTab1.setText(mStartDate + "至" + mEndDate);
        mTvScreeningTab2.setText("出库类型");
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScreeningSearch();
            }
        });
        mViewScreeningTab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScreeningTab1();
            }
        });
        mViewScreeningTab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScreeningTab2();
            }
        });
        mViewScreeningTab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScreeningTab3();
            }
        });
    }

    //筛选1
    private void doScreeningTab1(){
        showDialogStartEndTime(context);
    }
    //筛选2
    private void doScreeningTab2(){
//        showDialogOutType();
    }
    //筛选3
    private void doScreeningTab3(){
        if (mViewSearch.getVisibility() == View.VISIBLE) {
            mViewSearch.setVisibility(View.GONE);
            mEtSearch.setText("");
            mSearchStr = "";
        } else {
            mViewSearch.setVisibility(View.VISIBLE);
        }
    }
    //筛选：搜索
    private void doScreeningSearch(){
//        pageNo = 1;
//        mSearchStr = mEtSearch.getText().toString().trim();
//        queryData();
    }


    /**
     * 筛选时间的对话框
     */
    private void showDialogStartEndTime(Context context) {
        new MyDoubleDatePickerDialog(context, "筛选时间", mStartDate, mEndDate,
                new MyDoubleDatePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int year2, int month2, int day2, String startDate, String endDate) {
                        mTvScreeningTab1.setText(startDate + "至" + endDate);
                        mStartDate = startDate;
                        mEndDate = endDate;
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }
}
