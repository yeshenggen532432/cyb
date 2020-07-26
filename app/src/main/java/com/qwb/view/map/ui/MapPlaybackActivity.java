package com.qwb.view.map.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qwb.application.MyApp;
import com.qwb.view.map.adapter.PlaybackAdapter;
import com.qwb.view.map.adapter.PlaybackAdapter2;
import com.qwb.view.call.ui.CallQueryActivity;
import com.qwb.view.work.ui.WorkDetailActivity;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.view.map.parsent.PMapPlayBack;
import com.qwb.utils.Constans;
import com.qwb.utils.JsonHttpUtil;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.flow.model.FlowBfListBean;
import com.qwb.widget.MyDatePickerDialog;
import com.qwb.view.call.ui.CallRecordActivity;
import com.qwb.view.map.model.QueryBflsmwebBean;
import com.qwb.view.map.model.QueryBflsmwebBean.QueryBfHf;
import com.xmsx.qiweibao.R;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zyyoona7.lib.EasyPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 *  员工在线--拜访回放
 * 备注：type 1:拜访  2：流动打卡
 */
public class MapPlaybackActivity extends XActivity<PMapPlayBack>{

	@Override
	public int getLayoutId() {
		return R.layout.x_activity_map_playback;
	}

	@Override
	public PMapPlayBack newP() {
		return new PMapPlayBack();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initIntent();
		initMap();
		initUI();
		createPopup();// 初始化窗体
		doIntent();
	}

	/**
	 * 初始化Intent
	 */
	private int mid;// 当前选中的用户id(默认自己)--(3个都用到)
	private String name;// 用户名称d(默认自己)--（3个都用到）
	private String date;// 日期d(默认今天)--（拜访回放，轨迹回放：用到）
	private String type;// 1:拜访地图(默认) 2：流动打卡
	private void initIntent() {
		Intent intent = getIntent();
		if (intent != null) {
			mid = intent.getIntExtra(ConstantUtils.Intent.USER_ID, 0);
			name = intent.getStringExtra(ConstantUtils.Intent.USER_NAME);
			date = intent.getStringExtra(ConstantUtils.Intent.DATE);
			type = ConstantUtils.BFHF_TYPE;
		}
	}

	private void doIntent() {
		//type 1:拜访  2：流动打卡
		if("2".equals(type)){
			getP().querySignInBfhf(context,mid,date);
			mTvHeadTitle.setText("流动打卡回放");
		}else{
			getP().loadData(context,mid,date);
			mTvHeadTitle.setText("拜访回放");
		}
	}

	//TODO ***************************UI相关****************************************
	private void initUI() {
		initHead();
		initBaseView();
		initAdapter();
	}

	// 头部
	private TextView mTvHeadTitle;
	private TextView tv_headRight;
	private void initHead() {
		OtherUtils.setStatusBarColor(context);//设置状态栏颜色；透明度
		findViewById(R.id.iv_head_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Router.pop(context);
			}
		});
		mTvHeadTitle = findViewById(R.id.tv_head_title);
		tv_headRight =  findViewById(R.id.tv_head_right);
		mTvHeadTitle.setText("拜访回放");
		tv_headRight.setText("列表");
	}

	// 基本View
	private TextView tv_date;
	private Button btn_up;
	private Button btn_down;
	private void initBaseView() {
		tv_date = findViewById(R.id.tv_track_date);// 时间
		TextView tv_name = findViewById(R.id.tv_track_name);// 用户名
		tv_name.setText(name);
		tv_date.setText(date);
		// 向上，向下(默认“向上”不可以点，“向下”可以点)
		btn_up = findViewById(R.id.btn_up);
		btn_down = findViewById(R.id.btn_down);
		findViewById(R.id.rb_ygfbt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpActivity(MapMemberFBTActivity.class);// 跳到员工分布图
			}
		});
		findViewById(R.id.rb_gjhf).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpActivity(MapTrackActivity.class);// 跳到轨迹回放
			}
		});
	}

	//适配器
	RecyclerView mRvPlayBack;
	PlaybackAdapter mPlaybackAdapter;
	PlaybackAdapter2 mPlaybackAdapter2;
	private void initAdapter() {
		mRvPlayBack = findViewById(R.id.rv_playback);
		mRvPlayBack.setHasFixedSize(true);
		//添加分割线
		mRvPlayBack.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
				.colorResId(R.color.gray_e)
				.sizeResId(R.dimen.dp_1)
				.build());
		mRvPlayBack.setLayoutManager(new LinearLayoutManager(this));
		if ("2".equals(type)){
			mPlaybackAdapter2 = new PlaybackAdapter2();
			mRvPlayBack.setAdapter(mPlaybackAdapter2);
			mPlaybackAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
					try {
						mCurrentDataFlow = (FlowBfListBean.FlowBfBean)baseQuickAdapter.getData().get(position);
						jumpActivityToFlow();
					}catch (Exception e){
					}
				}
			});
		}else{
			mPlaybackAdapter = new PlaybackAdapter();
			mRvPlayBack.setAdapter(mPlaybackAdapter);
			mPlaybackAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
				@Override
				public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
					try {
						QueryBfHf item = (QueryBfHf)baseQuickAdapter.getData().get(position);
						jumpActivityToCallOnRecord(item.getId(),item.getKhNm());//拜访记录
					}catch (Exception e){
					}
				}
			});
		}

	}


	//TODO *****************************点击事件相关******************************************
	private int listMapState = 1;// 1:地图 2：列表
	@OnClick({R.id.tv_track_date,R.id.tv_head_right,R.id.btn_up,R.id.btn_down})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_track_date:// 选择时间
			showDialogChooseTime();
			break;
		case R.id.tv_head_right:// "列表"和"地图"切换
			if (1 == listMapState) {// 地图
				listMapState = 2;
				tv_headRight.setText("地图");
				mMapView.setVisibility(View.GONE);
				btn_up.setVisibility(View.GONE);
				btn_down.setVisibility(View.GONE);
			} else if (2 == listMapState) {
				listMapState = 1;
				tv_headRight.setText("列表");
				mMapView.setVisibility(View.VISIBLE);
				btn_up.setVisibility(View.VISIBLE);
				btn_down.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_up:// 向上
			if("2".equals(type)){
				mCurrentDataFlow = mDataListFlow.get(mCurrentPosition - 1);
				mCurrentPosition = mCurrentPosition - 1;
				showPopupFlow();
			}else{
				mCurrentDataBf = mDataListBf.get(mCurrentPosition - 1);
				mCurrentPosition = mCurrentPosition - 1;
				showPopupBf();
			}
			break;
		case R.id.btn_down:// 向下
			if("2".equals(type)){
				mCurrentDataFlow = mDataListFlow.get(mCurrentPosition + 1);
				mCurrentPosition = mCurrentPosition + 1;
				showPopupFlow();
			}else{
				mCurrentDataBf = mDataListBf.get(mCurrentPosition + 1);
				mCurrentPosition = mCurrentPosition + 1;
				showPopupBf();
			}
			break;
		}
	}


	//TODO *****************************地图相关******************************************
   // 初始化地图
   private MapView mMapView;
	private BaiduMap mBaiduMap;
	private void initMap() {
		mMapView = findViewById(R.id.bmapView1);
		mBaiduMap = mMapView.getMap();
		// 地图缩放初始化
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setOnMarkerClickListener(new MarkerListener());// 标记物点击监听
		mBaiduMap.setOnMapClickListener(new MapListener());// 地图点击监听
	}

	// 标记物监听--marker
	private final class MarkerListener implements OnMarkerClickListener {
		@Override
		public boolean onMarkerClick(Marker marker) {
			LatLng latlng = marker.getPosition();
			if("2".equals(type)){
				for (int i = 0; i < mDataListFlow.size(); i++) {
					FlowBfListBean.FlowBfBean bean = mDataListFlow.get(i);
					if (bean.getLatitude().equals(String.valueOf(latlng.latitude))&& bean.getLongitude().equals(String.valueOf(latlng.longitude))) {
						mCurrentDataFlow = bean;
						mCurrentPosition = i;
						showPopupFlow();
						break;
					}
				}
			}else{
				for (int i = 0; i < mDataListBf.size(); i++) {
					QueryBfHf queryBfHf = mDataListBf.get(i);
					if (queryBfHf.getLatitude().equals(String.valueOf(latlng.latitude))&& queryBfHf.getLongitude().equals(String.valueOf(latlng.longitude))) {
						mCurrentDataBf = queryBfHf;
						mCurrentPosition = i;
						showPopupBf();
						break;
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


	//TODO *****************************生命周期相关******************************************
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
		stopPlayer();// 界面停止时，停止播放
//		mMapView.onDestroy();
//		mMapView = null;
	}

	/**
	 * 选择时间
	 */
	int mYear,mMonth,mDay;
	private void showDialogChooseTime(){
		if(MyUtils.isEmptyString(date)){
			mYear= MyTimeUtils.getYear();
			mMonth= MyTimeUtils.getMonth();
			mDay= MyTimeUtils.getDay();
		}else{
			try {
				String[] split = date.split("-");
				mYear=Integer.parseInt(split[0]);
				mMonth=Integer.parseInt(split[1])-1;
				mDay=Integer.parseInt(split[2]);
			}catch (Exception e){
				ToastUtils.showCustomToast(e.getMessage());
			}
		}
		new MyDatePickerDialog(context, "选择时间", mYear, mMonth, mDay, new MyDatePickerDialog.DateTimeListener() {
			@Override
			public void onSetTime(int year, int month, int day, String timeStr) {
				mYear=year;mMonth=month;mDay=day;date=timeStr;
				tv_date.setText(timeStr);
				mCurrentPosition = -1;
				if("2".equals(type)){
					getP().querySignInBfhf(context,mid,date);
				}else{
					getP().loadData(context,mid,date);
				}
			}
			@Override
			public void onCancel() {
			}
		}).show();
	}


	//TODO**********************跳转界面相关*********************************
	//跳转界面-拜访记录
	private void jumpActivityToCallOnRecord(int id ,String khNm){
		Router.newIntent(context)
				.putInt(Constans.id, id)// 拜访id
				.putString(Constans.khNm, khNm)// 客户名称
				.putString(Constans.memberNm, name)// 用户名
				.to( CallRecordActivity.class)
				.launch();
	}
	//跳转界面-拜访查询
	private void jumpActivityToCallOnQuery(){
		Router.newIntent(context)
				.putString(Constans.type, "1")// 客户名称
				.putString(Constans.cid, String.valueOf(mCurrentDataBf.getCid()))// 客户id
				.to( CallQueryActivity.class)
				.launch();
	}
	//跳转界面-拜访回放；轨迹回放
	private void jumpActivity(Class class1) {
//		mMapView.onDestroy();
//		mMapView = null;
		Router.newIntent(context)
				.putInt(ConstantUtils.Intent.USER_ID,mid)//用户id
				.putString(ConstantUtils.Intent.USER_NAME,name)//用户名称
				.putString(ConstantUtils.Intent.DATE, date)//今天
				.to(class1)
				.launch();
		Router.pop(context);
	}
	//跳转界面-流动查询，考勤记录
	private void jumpActivityToFlow() {
		String signType = mCurrentDataFlow.getSignType();
		//1.上班，2：下班，3.流动
		if("1".equals(signType) || "2".equals(signType)){
			Router.newIntent(context)
					.putString(ConstantUtils.Intent.ID,"" + mCurrentDataFlow.getId())
					.to(WorkDetailActivity.class)
					.launch();
		}

	}




   //TODO**********************语音相关*********************************
	private boolean playState;
	private MediaPlayer mediaPlayer;
	private int bfid_play;// 以拜访id作为区分（当前点击播放是否与上次播放一致；一致暂停播放，不一致先暂停播放上次的，再播放当前的）
	private void setVoice(final String path, int id, final ImageView iv_voice) {
		if (!playState) {
			downFileToStartPlayer(path, iv_voice);
		} else {
			stopPlayer();
			if (bfid_play != id) {
				downFileToStartPlayer(path, iv_voice);
			}
		}
	}
	//下载音频文件并播放
	private void downFileToStartPlayer(final String path, final ImageView iv_voice) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				String fileName = JsonHttpUtil.getInstance().downFile(path);
				if (!TextUtils.isEmpty(fileName)) {
					openVioceFile(fileName, iv_voice);
				} else {
					ToastUtils.showCustomToast("文件下载中...");
				}
				Looper.loop();
			}
		}).start();
	}

	//自己发送的文件直接打开
	private void openVioceFile(String path, final ImageView iv_voice) {
		ToastUtils.showCustomToast("播放中。。");
		// TODO 播放动画
		Message message = new Message();
		message.what = 0;
		message.obj = iv_voice;
		mHandler.sendMessage(message);

		if (!playState) {
			mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepare();
				mediaPlayer.start();
				playState = true;
				// 设置播放结束时监听
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						// TODO 停止动画
						mHandler.sendEmptyMessage(1);

						playState = false;
						mediaPlayer.release();// 释放资源
						mediaPlayer = null;
					}
				});
			} catch (IllegalArgumentException e) {
				ToastUtils.showCustomToast("播放失败");
			} catch (IllegalStateException e) {
				ToastUtils.showCustomToast("播放失败");
			} catch (IOException e) {
				ToastUtils.showCustomToast("播放失败");
			}
		}
	}

	//停止播放
	private void stopPlayer() {
		mHandler.sendEmptyMessage(1);// 停止动画
		if (mediaPlayer != null) {
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					mediaPlayer.release();// 释放资源
					mediaPlayer = null;
					playState = false;
				} else {
					playState = false;
				}
			} catch (IllegalStateException e) {
				playState = false;
			}
		}
	}

	// 子线程中不能更新ui（播放动画，停止动画）
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				playAnimation((ImageView) msg.obj);
				break;
			case 1:
				stopAnimation();
				break;
			}
		}
	};

	// 播放动画
	private ImageView iv_playVoice;// TODO 备注播放动画的对象只有一个，（否则播放完动画停不了
	private int voicePlayPosition = -1;// 记录播放动画的位置
	private void playAnimation(ImageView iv_voice) {
		voicePlayPosition = iv_voice.getId();
		iv_playVoice = iv_voice;
		iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
		iv_playVoice.setImageResource(R.drawable.animation_left);
		AnimationDrawable animation = (AnimationDrawable) iv_playVoice.getDrawable();
		animation.start();
	}

	// 停止播放动画
	private void stopAnimation() {
		voicePlayPosition = -1;
		if (iv_playVoice != null) {
			iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
			iv_playVoice = null;
		}
	}
	
	//是否重播动画
	private void restartPlayAnimation() {
		if("2".equals(type)){
			if (null != mCurrentDataFlow && mCurrentDataFlow.getId() == voicePlayPosition) {
				iv_voice.setId(mCurrentDataFlow.getId());
				iv_playVoice = null;
				iv_playVoice = iv_voice;
				iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
				iv_playVoice.setImageResource(R.drawable.animation_left);
				AnimationDrawable drawable = (AnimationDrawable) iv_playVoice.getDrawable();
				drawable.start();
			} else {
				iv_voice.setImageResource(R.drawable.voice_from_playing_s0);
			}
		}else{
			if (null != mCurrentDataBf && mCurrentDataBf.getId() == voicePlayPosition) {
				iv_voice.setId(mCurrentDataBf.getId());
				iv_playVoice = null;
				iv_playVoice = iv_voice;
				iv_playVoice.setImageResource(R.drawable.voice_from_playing_s0);
				iv_playVoice.setImageResource(R.drawable.animation_left);
				AnimationDrawable drawable = (AnimationDrawable) iv_playVoice.getDrawable();
				drawable.start();
			} else {
				iv_voice.setImageResource(R.drawable.voice_from_playing_s0);
			}
		}
	}


	//TODO*******************************接口相关***********************************************

	//TODO**********************窗体相关*********************************
	private View mContentView;
	private View mPLayoutKhNm;
	private TextView tv_khNm;
	private TextView tv_startTime;
	private TextView tv_endTime;
	private TextView tv_address;
	// 语音
	private LinearLayout ll_voice;
	private ImageView iv_voice;
	private TextView tv_voicetime;
	public void createPopup() {
		EasyPopup mEasyPop = new EasyPopup(context)
				.setContentView(R.layout.x_popwin_visit_playback)
				.createPopup();
		mContentView = mEasyPop.getContentView();
		LinearLayout ll_address = mEasyPop.getView(R.id.ll_address);
		int screenWidth = MyApp.getScreenWidth();// 获取屏幕宽度
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_address.getLayoutParams(); // 取控件textView当前的布局参数
		params.width = (int) (screenWidth * 0.8);
		ll_address.setLayoutParams(params);
		mPLayoutKhNm = mEasyPop.getView(R.id.ll_khNm);
		tv_khNm = mEasyPop.getView(R.id.tv_khNm);
		tv_startTime = mEasyPop.getView(R.id.tv_startTime);
		tv_endTime = mEasyPop.getView(R.id.tv_endTime);
		tv_address = mEasyPop.getView(R.id.tv_address);
		//跳转
		tv_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if("2".equals(type)){
					jumpActivityToFlow();
				}else{
					jumpActivityToCallOnRecord(mCurrentDataBf.getId(), mCurrentDataBf.getKhNm());
				}
			}
		});
		mEasyPop.getView(R.id.iv_baifangchaxun).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpActivityToCallOnQuery();
			}
		});
		// 语音
		ll_voice = mEasyPop.getView(R.id.ll_voice);
		iv_voice = mEasyPop.getView(R.id.iv_voice);
		tv_voicetime = mEasyPop.getView(R.id.tv_voicetime);
		iv_voice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 下载语音
				if("2".equals(type)){
					int id = mCurrentDataFlow.getId();// 拜访id
					iv_voice.setId(id);// TODO 记录设置要播放动画的位置
					setVoice(mCurrentDataFlow.getVoiceUrl(), id, iv_voice);
					bfid_play = id;
				}else{
					iv_voice.setId(mCurrentDataBf.getId());// TODO 记录设置要播放动画的位置
					int id = mCurrentDataBf.getId();// 拜访id
					setVoice(mCurrentDataBf.getVoiceUrl(), id, iv_voice);
					bfid_play = id;
				}
			}
		});
	}

	public void showData(List<QueryBflsmwebBean.QueryBfHf> list){
		mBaiduMap.clear();
		mDataListBf.clear();
		if (list != null && list.size() > 0) {
			if (list.size() == 1) {// 当只有一个时：向上和向下都不可点击
				btn_up.setEnabled(false);
				btn_down.setEnabled(false);
			} else {// 默认“向上”不能点，“向下”可以点
				btn_up.setEnabled(false);
				btn_down.setEnabled(true);
			}
			for (int i = 0; i < list.size(); i++) {
				QueryBflsmwebBean.QueryBfHf queryBfHf = list.get(i);
				queryBfHf.setXhNm(String.valueOf(i + 1));
				mDataListBf.add(queryBfHf);
			}
			// 中心点
			QueryBflsmwebBean.QueryBfHf queryBfHf = mDataListBf.get(0);
			LatLng latLng = new LatLng(Double.valueOf(queryBfHf.getLatitude()),Double.valueOf(queryBfHf.getLongitude()));
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.setMapStatus(u);

			for (int i = 0; i < mDataListBf.size(); i++) {
				QueryBflsmwebBean.QueryBfHf queryBfHf2 = mDataListBf.get(i);
				LatLng latLng2 = new LatLng(Double.valueOf(queryBfHf2.getLatitude()),Double.valueOf(queryBfHf2.getLongitude()));
				addText(latLng2, " " + queryBfHf2.getXhNm() + " ");// 文本左右加空格
				String ys = queryBfHf2.getYs();
				if ("红色".equals(ys)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbred)));
				} else if ("蓝色".equals(ys)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblue)));
				} else if ("绿色".equals(ys)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbgreen)));
				} else if ("黄色".equals(ys)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbyellow)));
				} else if ("黑色".equals(ys)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblack)));
				} else {// 默认-蓝色
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblue)));
				}
			}
		} else {
			btn_up.setEnabled(false);
			btn_down.setEnabled(false);
			ToastUtils.showCustomToast("没有数据");
		}

		mPlaybackAdapter.setNewData(mDataListBf);
	}

	public void showDataFlow(List<FlowBfListBean.FlowBfBean> list){
		mDataListFlow.clear();
		mDataListFlow.addAll(list);
		mBaiduMap.clear();
		if (list != null && list.size() > 0) {
			if (list.size() == 1) {// 当只有一个时：向上和向下都不可点击
				btn_up.setEnabled(false);
				btn_down.setEnabled(false);
			} else {// 默认“向上”不能点，“向下”可以点
				btn_up.setEnabled(false);
				btn_down.setEnabled(true);
			}

			for (int i = 0; i < list.size(); i++) {
				FlowBfListBean.FlowBfBean bean = list.get(i);
				LatLng latLng2 = new LatLng(Double.valueOf(bean.getLatitude()),Double.valueOf(bean.getLongitude()));
				addText(latLng2, " " + (i+1) + " ");// 文本左右加空格
				String signType = bean.getSignType();
				if ("1".equals(signType)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbred)));
				} else if ("2".equals(signType)) {
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbgreen)));
				} else {// 默认-蓝色
					mBaiduMap.addOverlay(new MarkerOptions().position(latLng2).icon(BitmapDescriptorFactory.fromResource(R.drawable.qwbblue)));
				}
			}

			// 中心点
			FlowBfListBean.FlowBfBean bean = list.get(0);
			LatLng latLng = new LatLng(Double.valueOf(bean.getLatitude()),Double.valueOf(bean.getLongitude()));
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
			mBaiduMap.setMapStatus(u);
		} else {
			btn_up.setEnabled(false);
			btn_down.setEnabled(false);
			ToastUtils.showCustomToast("没有数据");
		}

		mPlaybackAdapter2.setNewData(mDataListFlow);
	}

	// 添加文字
	private void addText(LatLng latlng, String context) {
		// 添加文字 位置，背景颜色，字体大小，字体颜色，内容，旋转,字体加粗（Typeface.DEFAULT_BOLD）
		OverlayOptions ooText = new TextOptions().bgColor(0xFF000000).fontSize(20).fontColor(0xFFFFFFFF).text(context)
				.rotate(0).position(latlng).align(10, 10).typeface(Typeface.DEFAULT_BOLD);
		mBaiduMap.addOverlay(ooText);
	}

	/**
	 * 共同的部分
	 */
	private List<QueryBfHf> mDataListBf = new ArrayList<>();
	private QueryBfHf mCurrentDataBf;// 当前的“拜访回放对象”
	private int mCurrentPosition = -1;// 记录之前点击是哪个标记物
	private void showPopupBf() {
		//"向上按钮"：处于第一个时（不可点击），其他可点击；"向下按钮"：处于最后一个（不可点击），其他可点击
		if (mCurrentPosition == 0) {
			btn_up.setEnabled(false);
		} else {
			btn_up.setEnabled(true);
		}
		if (mCurrentPosition == mDataListBf.size() - 1) {
			btn_down.setEnabled(false);
		}else{
			btn_down.setEnabled(true);
		}
		//中心点
		LatLng latlngCenter = new LatLng(Double.valueOf(mCurrentDataBf.getLatitude()), Double.valueOf(mCurrentDataBf.getLongitude()));
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlngCenter);
		mBaiduMap.setMapStatus(u);
		//显示数据
		tv_khNm.setText(mCurrentDataBf.getKhNm() + "(停留" + mCurrentDataBf.getFz() + "分)");
		tv_startTime.setText("开始时间：" + mCurrentDataBf.getTime1());
		tv_endTime.setText("结束时间：" + mCurrentDataBf.getTime2());
		tv_address.setText(mCurrentDataBf.getAddress());
		if (mCurrentDataBf.getVoiceTime() == 0) {// 语音
			ll_voice.setVisibility(View.GONE);
		} else {
			ll_voice.setVisibility(View.VISIBLE);
			tv_voicetime.setText(mCurrentDataBf.getVoiceTime() + "\'");
			restartPlayAnimation();// 是否重播动画
		}
		//地图弹出窗体
		InfoWindow infoWindow = new InfoWindow(mContentView, latlngCenter, -50);
		mBaiduMap.showInfoWindow(infoWindow);
	}

	private List<FlowBfListBean.FlowBfBean> mDataListFlow = new ArrayList<>();
	private FlowBfListBean.FlowBfBean mCurrentDataFlow;// 当前的“拜访回放对象”
	private void showPopupFlow() {
		//"向上按钮"：处于第一个时（不可点击），其他可点击；"向下按钮"：处于最后一个（不可点击），其他可点击
		if (mCurrentPosition == 0) {
			btn_up.setEnabled(false);
		} else {
			btn_up.setEnabled(true);
		}
		if (mCurrentPosition == mDataListFlow.size() - 1) {
			btn_down.setEnabled(false);
		}else{
			btn_down.setEnabled(true);
		}
		//中心点
		LatLng latlngCenter = new LatLng(Double.valueOf(mCurrentDataFlow.getLatitude()), Double.valueOf(mCurrentDataFlow.getLongitude()));
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latlngCenter);
		mBaiduMap.setMapStatus(u);
		//显示数据
//		tv_khNm.setText(mCurrentDataBf.getKhNm() + "(停留" + mCurrentDataBf.getFz() + "分)");
		tv_startTime.setText("打卡:" + mCurrentDataFlow.getSignTime());
//		tv_endTime.setText("结束时间：" + mCurrentDataBf.getTime2());
		tv_address.setText(mCurrentDataFlow.getAddress());
		if (mCurrentDataFlow.getVoiceTime() == 0) {// 语音
			mPLayoutKhNm.setVisibility(View.GONE);
			ll_voice.setVisibility(View.GONE);
		} else {
			mPLayoutKhNm.setVisibility(View.VISIBLE);
			ll_voice.setVisibility(View.VISIBLE);
			tv_voicetime.setText(mCurrentDataFlow.getVoiceTime() + "''");
			restartPlayAnimation();// 是否重播动画
		}
		//地图弹出窗体
		InfoWindow infoWindow = new InfoWindow(mContentView, latlngCenter, -50);
		mBaiduMap.showInfoWindow(infoWindow);
	}



}
