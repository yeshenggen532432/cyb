package com.qwb.view.map.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.view.location.ui.MapLocationActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.qwb.application.MyApp;
import com.qwb.view.map.adapter.TrackListAdapter;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.map.model.TrackBean;
import com.qwb.db.LocationBean;
import com.qwb.view.map.parsent.PMapTrack;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.widget.MyDateTimePickerDialog;
import com.qwb.view.map.model.TrackListBean;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 员工在线-轨迹回放
 */

public class MapTrackActivity extends XActivity<PMapTrack> {
    @Override
    public int getLayoutId() {
        return R.layout.x_activity_map_track;
    }

    @Override
    public PMapTrack newP() {
        return new PMapTrack();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initMap();
        initUI();
        // 播放监听
        setListener();
        initPopup_set();
        initPop_xiaodian();
        initPop_dadian();
        // 获取数据
        getP().loadDataMap(context, mid, date, rb_zidingyi, tv_startTime, tv_endTime);//地图
        getP().loadDataList(context, mid, pageNo, date, rb_zidingyi, tv_startTime, tv_endTime);//列表
        getP().loadDataLC(context, mid);//里程数
    }

    /**
     * 初始化Intent
     */
    private int mid;// 当前选中的用户id(默认自己)
    private String name;// 用户名称d(默认自己)
    private String date;// 日期d(默认今天)

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mid = intent.getIntExtra(ConstantUtils.Intent.USER_ID, 0);
            name = intent.getStringExtra(ConstantUtils.Intent.USER_NAME);
            date = intent.getStringExtra(ConstantUtils.Intent.DATE);
            if (MyUtils.isEmptyString(date)) {// 没传日期
                date = MyTimeUtils.getDate_nyr(0);// 今天
            }
        }
    }

    private void initUI() {
        initHead();
        initBaseView();
        initAdapter();
        initRefresh();
    }

    //头部
    private TextView mTvHeadRight;
    private int listMapState;// 0:地图 1：列表
    private int type_jiupian = 1;// 0:不纠偏 1：纠偏
    TextView mTvHeadTitle;

    private void initHead() {
        OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.pop(context);
            }
        });
        mTvHeadTitle = (TextView) findViewById(R.id.tv_head_title);
        mTvHeadRight = (TextView) findViewById(R.id.tv_head_right);
        mTvHeadTitle.setText("行动轨迹");
        mTvHeadRight.setText("列表");
    }

    //基本View
    private LinearLayout ll_seek;
    private ImageButton play;
    private SeekBar seekbar;
    private TextView tv_name_gps;
    private TextView tv_time_gps;
    private TextView tv_speed_gps;
    private TextView tv_type_gps;
    private TextView tv_licheng;

    private void initBaseView() {
        ll_seek = (LinearLayout) findViewById(R.id.ll_seek);
        play = (ImageButton) findViewById(R.id.gps_play);
        seekbar = (SeekBar) findViewById(R.id.gps_seekbar);
        tv_name_gps = (TextView) findViewById(R.id.tv_name_gps);
        tv_time_gps = (TextView) findViewById(R.id.tv_time_gps);
        tv_speed_gps = (TextView) findViewById(R.id.tv_speed_gps);
        tv_type_gps = (TextView) findViewById(R.id.tv_type_gps);
        tv_name_gps.setText(name);
        tv_licheng = (TextView) findViewById(R.id.tv_licheng);
        findViewById(R.id.rb_bfhf).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(MapPlaybackActivity.class);// 跳到:拜访回放
            }
        });
        findViewById(R.id.rb_ygfbt).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpActivity(MapMemberFBTActivity.class);// 跳到:员工分布图
            }
        });
    }

    //初始化适配器
    RecyclerView mRvMap;
    TrackListAdapter mTrackAdapter;
    private int pageNo = 1;

    private void initAdapter() {
        mRvMap = (RecyclerView) findViewById(R.id.rv_map);
        mRvMap.setHasFixedSize(true);
        mRvMap.setLayoutManager(new LinearLayoutManager(this));
        //添加分割线
        mRvMap.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.gray_e)
                .sizeResId(R.dimen.dp_1)
                .build());
        mTrackAdapter = new TrackListAdapter(context);
        mTrackAdapter.openLoadAnimation();
        mRvMap.setAdapter(mTrackAdapter);
        mTrackAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                try {
                    LocationBean bean = (LocationBean) adapter.getData().get(position);
                    Intent intent = new Intent(MapTrackActivity.this, MapLocationActivity.class);
                    intent.putExtra("locationList", bean);
                    intent.putExtra("type", 3);
                    startActivity(intent);
                } catch (Exception e) {
                    ToastUtils.showCustomToast(e.getMessage());
                }
            }
        });
    }

    //初始化刷新控件
    RefreshLayout mRefreshLayout;

    private void initRefresh() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                pageNo = 1;
                getP().loadDataList(context, mid, pageNo, date, rb_zidingyi, tv_startTime, tv_endTime);//列表
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                pageNo++;
                getP().loadDataList(context, mid, pageNo, date, rb_zidingyi, tv_startTime, tv_endTime);//列表
            }
        });
    }

    //*************************************点击事件****************************************************
    @OnClick({R.id.tv_head_right, R.id.ib_set})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:// 跳到轨迹列表中
                switch (listMapState) {
                    case 0:
                        ll_seek.setVisibility(View.GONE);
                        mMapView.setVisibility(View.GONE);
                        tv_licheng.setVisibility(View.GONE);
                        listMapState = 1;
                        mTvHeadRight.setText("地图");
                        break;
                    case 1:
                        ll_seek.setVisibility(View.VISIBLE);
                        mMapView.setVisibility(View.VISIBLE);
                        tv_licheng.setVisibility(View.VISIBLE);
                        listMapState = 0;
                        mTvHeadRight.setText("列表");
                        break;
                }
                break;
            case R.id.ib_set:// 设置
                popWinSet.showAtLocation(findViewById(R.id.parent3), Gravity.CENTER, 0, 0);
                break;
        }
    }

    //TODO ************************生命周期相关*******************************
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //TODO *********************************窗体相关**********************************************
    //窗体-小点
    private View contentView_xiaodian;
    private TextView tv_time_xiaodian;
    private TextView tv_speed_xiaodian;
    private TextView tv_type_xiaodian;
    private TextView tv_address_xiaodian;

    private void initPop_xiaodian() {
        contentView_xiaodian = getLayoutInflater().inflate(R.layout.x_popup_dongtaitu_xiaodian, null);
        tv_time_xiaodian = (TextView) contentView_xiaodian.findViewById(R.id.tv_time);
        tv_speed_xiaodian = (TextView) contentView_xiaodian.findViewById(R.id.tv_speed);
        tv_type_xiaodian = (TextView) contentView_xiaodian.findViewById(R.id.tv_type);
        tv_address_xiaodian = (TextView) contentView_xiaodian.findViewById(R.id.tv_address);
        // 设置tv_address的宽度为屏幕的0.7
        LinearLayout ll_address = (LinearLayout) contentView_xiaodian.findViewById(R.id.ll_address);
        int screenWidth = MyApp.getScreenWidth();// 获取屏幕宽度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_address.getLayoutParams();
        params.width = (int) (screenWidth * 0.7);
        ll_address.setLayoutParams(params);
    }

    //窗体-大点
    private View contentView_dadian;
    private TextView tv_time_dadian;
    private TextView tv_endTime_dadian;
    private TextView tv_time_tingliu;
    private TextView tv_address_dadian;

    private void initPop_dadian() {
        contentView_dadian = getLayoutInflater().inflate(R.layout.x_popup_dongtaitu_dadian, null);
        tv_time_dadian = (TextView) contentView_dadian.findViewById(R.id.tv_time);
        tv_endTime_dadian = (TextView) contentView_dadian.findViewById(R.id.tv_endTime);
        tv_time_tingliu = (TextView) contentView_dadian.findViewById(R.id.tv_time_tingliu);
        tv_address_dadian = (TextView) contentView_dadian.findViewById(R.id.tv_address);
        // 设置tv_address的宽度为屏幕的0.7
        LinearLayout ll_address = (LinearLayout) contentView_dadian.findViewById(R.id.ll_address);
        int screenWidth = MyApp.getScreenWidth();// 获取屏幕宽度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_address.getLayoutParams();
        params.width = (int) (screenWidth * 0.7);
        ll_address.setLayoutParams(params);
    }

    //窗体-设置
    private PopupWindow popWinSet = null;
    private RelativeLayout rl_bofang_speed;
    private LinearLayout rl_bofang_time;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private int choiceTime_type;
    private RadioButton rb_zidingyi;// 自定义
    private Calendar calendar = Calendar.getInstance();

    private void initPopup_set() {
        View contentView = getLayoutInflater().inflate(R.layout.x_popup_track_map, null);
        popWinSet = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWinSet.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWinSet.setBackgroundDrawable(new BitmapDrawable());
        // 播放速度，播放时间
        LinearLayout ll_bofang_speed = (LinearLayout) contentView.findViewById(R.id.ll_bofang_speed);
        rl_bofang_speed = (RelativeLayout) contentView.findViewById(R.id.rl_bofang_speed);
        rl_bofang_time = (LinearLayout) contentView.findViewById(R.id.rl_bofang_time);
        // 设置tv_address的宽度为屏幕的0.7
        int screenWidth = MyApp.getScreenWidth();// 获取屏幕宽度
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_bofang_speed.getLayoutParams(); // 取控件textView当前的布局参数
        params.width = (int) (screenWidth * 0.7);
        ll_bofang_speed.setLayoutParams(params);
        // 开始时间，结束时间
        tv_startTime = (TextView) contentView.findViewById(R.id.tv_startTime);
        tv_endTime = (TextView) contentView.findViewById(R.id.tv_endTime);
        tv_startTime.setText(MyTimeUtils.getDate_nyr(0) + " 00:00");// 默认今天的0点
        tv_endTime.setText(MyTimeUtils.getDateYmdhm(0));// 默认现在
        startYear = endYear = calendar.get(Calendar.YEAR);
        startMonth = endMonth = (calendar.get(Calendar.MONTH));
        startDay = endDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = 0;//默认从“00:00”
        startMin = 0;//默认从“00:00”
        endHour = calendar.get(Calendar.HOUR_OF_DAY);
        endMin = calendar.get(Calendar.MINUTE);
        tv_startTime.setEnabled(false);
        tv_endTime.setEnabled(false);
        tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
        tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);

        RadioButton rb_jintian = (RadioButton) contentView.findViewById(R.id.rb_jintian);
        rb_zidingyi = (RadioButton) contentView.findViewById(R.id.rb_zidingyi);
//		rb_jintian.setChecked(true);
        RadioGroup rg_bofang_speed = (RadioGroup) contentView.findViewById(R.id.rg_bofang_speed);
        RadioGroup rg_bofang_time = (RadioGroup) contentView.findViewById(R.id.rg_bofang_time);
        rg_bofang_speed.setOnCheckedChangeListener(new RGchangeListener());
        rg_bofang_time.setOnCheckedChangeListener(new RGchangeListener());
        contentView.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popWinSet.dismiss();
                seekbar.setProgress(0);
                pageNo = 1;
                getP().loadDataMap(context, mid, date, rb_zidingyi, tv_startTime, tv_endTime);//地图
                getP().loadDataList(context, mid, pageNo, date, rb_zidingyi, tv_startTime, tv_endTime);//列表
                getP().loadDataLC(context, mid);//里程数
            }
        });
        contentView.findViewById(R.id.btn_quxiao).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popWinSet.dismiss();
            }
        });
        tv_startTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceTime_type = 1;
                showDialogDateTime();
            }
        });
        tv_endTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                choiceTime_type = 2;
                showDialogDateTime();
            }
        });
        contentView.findViewById(R.id.ll_bofang_speed).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_bofang_speed.setVisibility(View.VISIBLE);
                rl_bofang_time.setVisibility(View.GONE);
            }
        });
        contentView.findViewById(R.id.ll_bofang_time).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_bofang_speed.setVisibility(View.GONE);
                rl_bofang_time.setVisibility(View.VISIBLE);
            }
        });
    }

    private int runtime = 500;// 毫秒 决定播放速度的快慢

    private final class RGchangeListener implements OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // 播放速度
            if (R.id.rg_bofang_speed == group.getId()) {
                switch (checkedId) {
                    case R.id.rb_man:// 慢:1s
                        runtime = 1000;
                        break;
                    case R.id.rb_yiban:// 一般:0.5s
                        runtime = 500;
                        break;
                    case R.id.rb_kuai:// 快:0.3s
                        runtime = 300;
                        break;
                    case R.id.rb_henkuai:// 很快:0.1s
                        runtime = 100;
                        break;
                }

                // 播放时间
            } else if (R.id.rg_bofang_time == group.getId()) {
                switch (checkedId) {
                    case R.id.rb_jintian:// 今天
                        date = MyTimeUtils.getDate_nyr(0);
                        tv_startTime.setEnabled(false);
                        tv_endTime.setEnabled(false);
                        tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                        tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                        break;
                    case R.id.rb_zuotian:// 昨天
                        date = MyTimeUtils.getDate_nyr(-1);
                        tv_startTime.setEnabled(false);
                        tv_endTime.setEnabled(false);
                        tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                        tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                        break;
                    case R.id.rb_qiantian:// 前天
                        date = MyTimeUtils.getDate_nyr(-2);
                        tv_startTime.setEnabled(false);
                        tv_endTime.setEnabled(false);
                        tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                        tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray3);
                        break;
                    case R.id.rb_zidingyi:// 自定义
                        tv_startTime.setEnabled(true);
                        tv_endTime.setEnabled(true);
                        tv_startTime.setBackgroundResource(R.drawable.shape_kuang_gray);
                        tv_endTime.setBackgroundResource(R.drawable.shape_kuang_gray);
                        break;
                }
            }
        }
    }

    // 显示开始日期
    private int startYear;
    private int startMonth;
    private int startDay;
    private int startHour;
    private int startMin;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int endHour;
    private int endMin;

    private void showDialogDateTime() {
        String title = null;
        int year = 0, month = 0, day = 0, hour = 0, min = 0;
        switch (choiceTime_type) {
            case 1:
                title = "开始时间";
                year = startYear;
                month = startMonth;
                day = startDay;
                hour = startHour;
                min = startMin;
                break;
            case 2:
                title = "结束时间";
                year = endYear;
                month = endMonth;
                day = endDay;
                hour = endHour;
                min = endMin;
                break;
        }
        new MyDateTimePickerDialog(MapTrackActivity.this, title, year, month, day, hour, min,
                new MyDateTimePickerDialog.DateTimeListener() {
                    @Override
                    public void onSetTime(int year, int month, int day, int hour, int minute, String timeStr) {
                        switch (choiceTime_type) {
                            case 1://开始时间
                                startYear = year;
                                startMonth = month;
                                startDay = day;
                                startHour = hour;
                                startMin = minute;
                                tv_startTime.setText(timeStr);
                                break;
                            case 2://结束时间
                                endYear = year;
                                endMonth = month;
                                endDay = day;
                                endHour = hour;
                                endMin = minute;
                                tv_endTime.setText(timeStr);
                                break;
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    //TODO *********************************播放轨迹**********************************************
    private boolean play_statu = true;

    private void setListener() {// 注册监听器
        play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (play_statu) {// 默认为true 关闭 点击一下为播放
                    play.setImageDrawable(getResources().getDrawable(R.drawable.icon_pause));
                    play_statu = !play_statu;
                    startTimer();

                } else {// 点击一下后暂停播放
                    play_statu = !play_statu;
                    play.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                    stopTimer();
                }
            }
        });
    }

    private Timer timer = null;
    private TimerTask task;
    private int progress = 0;
    private int pointCount = 0;// 从0开始数
    private int j = 0;

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    if (progress > pointCount) {// 播放结束 线程中不可以改变ui
                        progress = pointCount;
                        j = 100;
                        handler.sendEmptyMessage(j);
                    } else {
                        seekbar.setProgress(progress);
                        progress++;
                    }
                }
            };
            timer.schedule(task, 0, runtime);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            task.cancel();
            timer.cancel();
            task = null;
            timer = null;
        }
    }

    private Handler handler = new Handler() {// 该handler在主线程中
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (j) {
                case 100:// timertask 结束播放
                    play_statu = !play_statu;
                    play.setImageDrawable(getResources().getDrawable(R.drawable.icon_play));
                    stopTimer();
                    break;
            }
        }

        ;
    };

    // SeekBar变化监听
    private void setSeekBarListener() {
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progress = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < pointlist.size() - 1) {
                    final LatLng startPoint = new LatLng(pointlist.get(progress).latitude, pointlist.get(progress).longitude);
                    final LatLng endPoint = new LatLng(pointlist.get(progress + 1).latitude, pointlist.get(progress + 1).longitude);
                    mMoveMarker.setPosition(startPoint);
                    mMoveMarker.setRotate((float) getAngle(startPoint, endPoint));

                    Point p = mBaiduMap.getProjection().toScreenLocation(startPoint);
                    // // 当前坐标点跑出屏幕外 将地图拉回
                    if (p.x < 0 || p.x > mMapView.getWidth() || p.y < 10 || p.y > (mMapView.getHeight())) {
                        MapStatusUpdate u2 = MapStatusUpdateFactory.newLatLngZoom(startPoint, mBaiduMap.getMapStatus().zoom); // 设置地图中心点以及缩放级别
                        mBaiduMap.animateMapStatus(u2);
                    }
                } else {
                    final LatLng startPoint = new LatLng(pointlist.get(progress).latitude, pointlist.get(progress).longitude);
                    mMoveMarker.setPosition(startPoint);
                }
                LocationBean locationBean = locationList_map.get(progress);
                if (locationBean != null) {
                    long stay_time = locationBean.getStay_time();
                    String location_from = locationBean.getLocation_from();
                    // 加上停留时间
                    tv_time_gps.setText(String.valueOf(MyUtils.getStrFromTime(locationBean.getLocation_time() + stay_time)));
                    double speed = locationBean.getSpeed() * 3.6;
                    tv_speed_gps.setText(String.format("%.2f", speed) + "km/h");
                    if (!MyUtils.isEmptyString(location_from) && location_from.length() > 4) {
                        tv_type_gps.setText(locationBean.getLocation_from().substring(0, 4));
                    }
                }
            }
        });
    }

    //TODO**********************跳转界面相关*********************************
    //跳转界面-拜访回放；员工分布图
    private void jumpActivity(Class class1) {
//		mMapView.onDestroy();
//		mMapView = null;
        Router.newIntent(context)
                .putInt(ConstantUtils.Intent.USER_ID, mid)//用户id
                .putString(ConstantUtils.Intent.USER_NAME, name)//用户名称
                .putString(ConstantUtils.Intent.DATE, date)//今天
                .to(class1)
                .launch();
        Router.pop(context);
    }


    //TODO*************************************地图相关********************************************
    //初始化地图
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.bmapView3);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);// 地图缩放初始化
        mBaiduMap.setMapStatus(msu);
        mBaiduMap.setOnMarkerClickListener(new MarkerListener());// 标记物点击监听
        mBaiduMap.setOnMapClickListener(new MapListener());// 地图点击监听
        mBaiduMap.setOnMapStatusChangeListener(new MapStatusChangeListener());// 地图状态发生变化
    }

    // 标记物监听--marker
    private final class MarkerListener implements OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {
            LatLng latlng = marker.getPosition();
            double lat = latlng.latitude;
            double lng = latlng.longitude;
            if ("小点".equals(marker.getTitle())) {
                for (int i = 0; i < locationList_xiaodian.size(); i++) {
                    LocationBean locationBean = locationList_xiaodian.get(i);
                    if (locationBean != null) {
                        double latitude = locationBean.getLatitude();
                        double longitude = locationBean.getLongitude();
                        if (String.valueOf(lat).equals(String.valueOf(latitude))
                                && String.valueOf(lng).equals(String.valueOf(longitude))) {
                            double speed = locationBean.getSpeed() * 3.6;
                            tv_time_xiaodian
                                    .setText(String.valueOf(MyUtils.getStrFromTime(locationBean.getLocation_time())));
                            tv_speed_xiaodian.setText(String.format("%.2f", speed) + "km/h");
                            String location_from = locationBean.getLocation_from();
                            if (!MyUtils.isEmptyString(location_from) && location_from.length() > 4) {
                                tv_type_xiaodian.setText(locationBean.getLocation_from().substring(0, 4));
                            }
                            tv_address_xiaodian.setText(locationBean.getAddress());
                            InfoWindow mInfoWindow = new InfoWindow(contentView_xiaodian, latlng, -20);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                            return true;
                        }
                    }
                }
            } else if ("大点".equals(marker.getTitle())) {
                for (int i = 0; i < locationList_dadian.size(); i++) {
                    LocationBean locationBean = locationList_dadian.get(i);
                    if (locationBean != null) {
                        double latitude = locationBean.getLatitude();
                        double longitude = locationBean.getLongitude();
                        if (String.valueOf(lat).equals(String.valueOf(latitude))
                                && String.valueOf(lng).equals(String.valueOf(longitude))) {
                            long stay_time = locationBean.getStay_time();
                            tv_time_dadian
                                    .setText(String.valueOf(MyUtils.getStrFromTime(locationBean.getLocation_time())));
                            tv_endTime_dadian.setText(String
                                    .valueOf(MyUtils.getStrFromTime(locationBean.getLocation_time() + stay_time)));
                            int stay = (int) (locationBean.getStay_time() / 60);// 分
                            int stay_hour = stay / 60;
                            int stay_min = stay % 60;
                            if (0 == stay_hour) {
                                tv_time_tingliu.setText(stay_min + "分");
                            } else if (stay_hour > 0) {
                                tv_time_tingliu.setText(stay_hour + "时" + stay_min + "分");
                            }
                            tv_address_dadian.setText(locationBean.getAddress());
                            InfoWindow mInfoWindow = new InfoWindow(contentView_dadian, latlng, -20);
                            mBaiduMap.showInfoWindow(mInfoWindow);
                            return true;
                        }
                    }
                }
            }
            return true;
        }
    }

    //地图点击监听：隐藏窗体InfoWindow（原因：窗体覆盖标记物）
    private final class MapListener implements OnMapClickListener {
        @Override
        public boolean onMapPoiClick(MapPoi arg0) {
            return false;
        }

        @Override
        public void onMapClick(LatLng arg0) {
            mBaiduMap.hideInfoWindow();
        }
    }

    //地图改变监听--小点的显示与隐藏
    private final class MapStatusChangeListener implements OnMapStatusChangeListener {
        @Override
        public void onMapStatusChange(MapStatus arg0) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus arg0) {
            MapStatus ms = mBaiduMap.getMapStatus();
            float zoom = ms.zoom;
            if (overlayList_xiao != null && overlayList_xiao.size() > 0) {
                if (zoom > 17) {
                    boolean visible = Overlay_xiaodian.isVisible();
                    if (!visible) {
                        for (int i = 0; i < overlayList_xiao.size(); i++) {
                            Overlay overlay = overlayList_xiao.get(i);
                            Overlay_xiaodian.setVisible(true);
                            overlay.setVisible(true);
                        }
                    }
                } else {
                    boolean visible = Overlay_xiaodian.isVisible();
                    if (visible) {
                        for (int i = 0; i < overlayList_xiao.size(); i++) {
                            Overlay overlay = overlayList_xiao.get(i);
                            Overlay_xiaodian.setVisible(false);
                            overlay.setVisible(false);
                        }
                    }
                }
            }
        }

        @Override
        public void onMapStatusChangeStart(MapStatus arg0) {
        }

        @Override
        public void onMapStatusChangeStart(MapStatus arg0, int arg1) {
        }
    }

    // TODO ********************************百度地图小车平滑移动**************************8
    //根据点获取图标转的角度
    private double getAngle(int startIndex) {
        if ((startIndex + 1) >= mPolyline.getPoints().size()) {
            throw new RuntimeException("index out of bonds");
        }
        LatLng startPoint = mPolyline.getPoints().get(startIndex);
        LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
        return getAngle(startPoint, endPoint);
    }

    //根据两点算取图标转的角度
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        return angle;
    }

    //算斜率
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;
    }


    //TODO*************************************接口相关********************************************
    //显示数据-地图的
    public void showDataMap(TrackBean mTrackBean) {
        locationList_map.clear();
        locationList_dadian.clear();
        locationList_xiaodian.clear();
        pointlist.clear();
        dadianlist.clear();
        xiaodianlist.clear();
        if (0 == mTrackBean.getCode()) {
            List<LocationBean> location = mTrackBean.getLocation();
            if (location != null && location.size() > 0) {
                for (int j = 0; j < location.size(); j++) {
                    LocationBean locationBean = location.get(j);
                    double latitude = locationBean.getLatitude();
                    double longitude = locationBean.getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    // 去掉坐标【0，0】点
                    if (latitude == 4.9E-324 || longitude == 4.9E-324) {
                    } else {
                        pointlist.add(0, latLng);
                        locationList_map.add(0, locationBean);
                        // 大点
                        long stay_time = locationBean.getStay_time();
                        if (stay_time > 5 * 60) {
                            dadianlist.add(latLng);
                            locationList_dadian.add(locationBean);
                        } else {
                            xiaodianlist.add(latLng);
                            locationList_xiaodian.add(locationBean);
                        }
                    }
                }
            }
            addLine();
        } else {
            ToastUtils.showCustomToast("稍后再试");
        }
    }

    //显示数据-列表
    public void showDataList(TrackBean mTrackBean) {
        List<LocationBean> tempList = new ArrayList<>();
        List<LocationBean> location = mTrackBean.getLocation();
        if (location != null && location.size() > 0) {
            for (int j = 0; j < location.size(); j++) {
                LocationBean locationBean = location.get(j);
                double latitude = locationBean.getLatitude();
                double longitude = locationBean.getLongitude();
                // 去掉坐标【0，0】点
                if (latitude == 0 || longitude == 0 || latitude == 4.9E-324 || longitude == 4.9E-324) {
                } else {
                    tempList.add(locationBean);
                }
            }
        }
        refreshAdapterTrack(tempList);
    }

    //显示数据-里程数
    public void showDataLc(List<TrackListBean.TrackList> rows) {
        if (rows != null) {
            for (int i = 0; i < rows.size(); i++) {
                TrackListBean.TrackList trackList = rows.get(i);
                int userId = trackList.getUserId();
                if (mid == userId) {
                    String workingDistance = trackList.getWorkingDistance();
                    if (!MyUtils.isEmptyString(workingDistance)) {
                        try {
                            double distance = Double.valueOf(workingDistance) / 1000;
                            tv_licheng.setText("单日里程:" + String.format("%.2f", distance) + "公里");// 里程
                        } catch (Exception e) {
                        }
                    }
                    break;
                }
            }
        }
    }


    //关闭刷新，加载更多
    public void closeRefresh() {
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    //刷新适配器
    public void refreshAdapterTrack(List<LocationBean> tempList) {
        if (!(tempList != null && tempList.size() > 0)) {
            mRefreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            ToastUtils.showCustomToast("没有更多数据");
        }
        if (pageNo == 1) {
            //上拉刷新
            mTrackAdapter.setNewData(tempList);
            mRefreshLayout.finishRefresh();
            mRefreshLayout.setNoMoreData(false);
            mRvMap.scrollToPosition(0);//移动到顶部
        } else {
            //加载更多
            mTrackAdapter.addData(tempList);
            mRefreshLayout.finishLoadMore();
        }
    }

    //添加线，和一些设置
    private List<Overlay> overlayList_xiao = new ArrayList<>();
    private Overlay Overlay_xiaodian;
    private List<LocationBean> locationList_map = new ArrayList<>();
    private List<LocationBean> locationList_dadian = new ArrayList<>();
    private List<LocationBean> locationList_xiaodian = new ArrayList<>();
    private List<LatLng> pointlist = new ArrayList<>();// 所有的点
    private List<LatLng> xiaodianlist = new ArrayList<>();// 小点
    private List<LatLng> dadianlist = new ArrayList<>();// 大点
    private Marker mMoveMarker;
    private Polyline mPolyline;

    private void addLine() {
        mBaiduMap.clear();
        if (pointlist != null && pointlist.size() > 0) {
            // 全部的点包括在里边
            LatLng llC = pointlist.get(0);
            LatLng llD = pointlist.get(pointlist.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder().include(llC).include(llD).build();
            MapStatusUpdate msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.setMapStatus(msUpdate);
            MapStatus ms = mBaiduMap.getMapStatus();
            float zoom = ms.zoom;

            // 小点
            if (xiaodianlist != null && xiaodianlist.size() > 0) {
                for (int i = 0; i < xiaodianlist.size(); i++) {
                    LatLng latLng = xiaodianlist.get(i);
                    Overlay overlay = mBaiduMap.addOverlay(new MarkerOptions().position(latLng).title("小点").anchor(5, 5).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_xiaodian)));
                    overlayList_xiao.add(overlay);
                    if (zoom > 17) {
                        overlay.setVisible(true);
                    } else {
                        overlay.setVisible(false);
                    }
                }
                Overlay_xiaodian = overlayList_xiao.get(0);// 小点的第一点
            }

            // 大点
            if (dadianlist != null && dadianlist.size() > 0) {
                for (int i = 0; i < dadianlist.size(); i++) {
                    LatLng latLng = dadianlist.get(i);
                    mBaiduMap.addOverlay(new MarkerOptions().position(latLng).title("大点").anchor(5, 5).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_dadian)));
                }
            }

            // 只有一个点
            if (pointlist.size() == 1) {
                LatLng latLng = pointlist.get(0);
                MapStatusUpdate msUpdate2 = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.setMapStatus(msUpdate2);
            } else {
                // 线
                PolylineOptions polylineOptions = new PolylineOptions().points(pointlist).width(6).color(Color.RED);
                mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);

                // 第一点：覆盖物，标记物及方向
                OverlayOptions markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow)).position(pointlist.get(0))
                        .rotate((float) getAngle(0));
                mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
            }
            // 第一点数据
            LocationBean locationBean = locationList_map.get(0);
            tv_time_gps.setText(String.valueOf(MyUtils.getStrFromTime(locationBean.getLocation_time())));
            String location_from = locationBean.getLocation_from();
            if (!MyUtils.isEmptyString(location_from) && location_from.length() > 4) {
                tv_type_gps.setText(locationBean.getLocation_from().substring(0, 4));
            }
            pointCount = pointlist.size() - 1;
            seekbar.setMax(pointCount);// 设置seekbar的最大值
            setSeekBarListener();// 先存入数据再设置seekbarlistener 防止数组越界
        }
    }
}
