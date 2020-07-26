package com.qwb.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.ninegrid.NineGridView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pgyersdk.PgyerActivityManager;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.crash.PgyerCrashObservable;
import com.pgyersdk.crash.PgyerObserver;
import com.tencent.smtt.sdk.QbSdk;
import com.xdandroid.hellodaemon.DaemonEnv;
import com.qwb.imageloader.GlideImageLoader;
import com.qwb.utils.ActivityManager;
import com.qwb.view.longconnection.TraceServiceImpl;
import com.qwb.receive.MySQLite;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.cnlife.view.widget.CornersTransform;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cn.droidlover.xdroidmvp.log.XLog;
import cn.jpush.android.api.JPushInterface;
import dev.DevUtils;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import okhttp3.OkHttpClient;


public class MyApp extends Application {

	private static MyApp instance;
	private SharedPreferences sp;
	private static int mScreenWidth;
	private static int mScreenHeight;
	private static InputMethodManager imm;
	private static MySQLite mySQLite;
	private static Context mContext;

	@Override
	public void onCreate() {
		super.onCreate();
		// 跟新版本的时候 关掉进程
		sp = getSharedPreferences("mitu", MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putBoolean("isloding", false);
		edit.commit();

		instance = this;
		mContext = getApplicationContext();

		makeAppFolder();// 创建根目录
		initScreenWH(); // 获得屏幕的宽高
		MyUtils.initUtils(this);
		ToastUtils.initUtils(this);
		initImageLoader(getApplicationContext());// 初始化ImageLoader
//		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
		JPushInterface.setLatestNotificationNumber(this, 1);

		// 百度地图 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
		SDKInitializer.initialize(this);

		// 网络请求
		initOkHttpUtils();
		// 蒲公英注册
		initPgyer();
		//图片选择器初始化
		initImagePicker();
		//进程保活
		initDeamonService();
		//初始化数据库
		LitePal.initialize(this);
		// 注册activity监听器
		registerActivityListener();
		//今日头条屏幕适配方案终极版
		initAutoSize();
		//初始化讯飞语音
		initVoice();
		//初始化九图的图片显示
		initNineGridView();
		//初始化DevUtils工具类
		initDevUtils();
		//初始化x5内核
		initWebView();
	}

	/**
	 * 初始化DevUtils工具类
	 */
	public void initDevUtils(){
		// 初始化工具类
		DevUtils.init(this.getApplicationContext());
//		// = 初始化日志配置 =
//		// 设置默认Logger配置
//		LogConfig logConfig = new LogConfig();
//		logConfig.logLevel = LogLevel.DEBUG;
//		logConfig.tag = LOG_TAG;
//		logConfig.sortLog = true; // 美化日志, 边框包围
//		DevLogger.init(logConfig);
//		// 打开 lib 内部日志 - 线上环境, 不调用方法就行
//		DevUtils.openLog();
//		DevUtils.openDebug();
	}
	/**
	 * 初始化DeamonService(进程保活)
	 */
	public void initDeamonService(){
		int MINIMAL_WAKE_UP_INTERVAL = 3 * 60 * 1000;//唤醒间隔
		DaemonEnv.initialize(this, TraceServiceImpl.class, MINIMAL_WAKE_UP_INTERVAL);
	}

	/**
	 * 初始化请求：OkHttpUtil
	 */
	private void initOkHttpUtils() {
		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(30L, TimeUnit.SECONDS)
				.readTimeout(30L, TimeUnit.SECONDS)
				.writeTimeout(30L, TimeUnit.SECONDS)
				.build();
		OkHttpUtils.initClient(okHttpClient);
	}

	/**
	 * 屏幕像素跟dp的比例 一些品牌机的比例不一样
	 */
	public float getBiLi() {
		return getResources().getDisplayMetrics().density;
	}

	/**
	 * 获取数据库对象
	 */
	public static SQLiteDatabase getDB() {
		if (mySQLite == null) {
			mySQLite = new MySQLite(mContext, Constans.dbVesion);

		}
		return mySQLite.getWritableDatabase();
	}

	// 初始化uil
	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 获取输入法管理
	 */
	public static InputMethodManager getImm() {
		if (imm == null) {
			imm = (InputMethodManager) instance.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		return imm;
	}

	private void initScreenWH() {
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		boolean isPortrait = dm.widthPixels < dm.heightPixels;
		mScreenWidth = isPortrait ? dm.widthPixels : dm.heightPixels;
		mScreenHeight = isPortrait ? dm.heightPixels : dm.widthPixels;
	}

	/**
	 * 获取屏幕宽高
	 */
	public static int getScreenWidth() {
		return mScreenWidth;
	}

	public static int getScreenHeight() {
		return mScreenHeight;
	}

	/**
	 * 生成app的根文件夹
	 */
	private void makeAppFolder() {
		File file = new File(Constans.APPDIR);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		File file1 = new File(Constans.DIR_IMAGE_CACHE);
		if (!file1.isDirectory()) {
			file1.mkdirs();
		}
		File file2 = new File(Constans.DIR_VOICE);
		if (!file2.isDirectory()) {
			file2.mkdirs();
		}
		File file3 = new File(Constans.DIR_FILES);
		if (!file3.isDirectory()) {
			file3.mkdirs();
		}
	}

	public ArrayList<Activity> mAllActivities = new ArrayList<Activity>();

	// 关闭界面
	public void finishActivity(Activity act) {
		for (int i = 0; i < mAllActivities.size(); i++) {
			Activity activity = mAllActivities.get(i);
			if (!activity.isFinishing() && activity.equals(act)) {
				act.finish();
				act = null;
			}
		}
	}

	// 退出时，界面全部关闭
	public void exit() {
		for (int i = 0; i < mAllActivities.size(); i++) {
			mAllActivities.get(i).finish();
		}
		mAllActivities.clear();
	}

	// 添加界面
	public void addActivity(Activity a) {
		if (!mAllActivities.contains(a)) {
			mAllActivities.add(a);
		}
	}

	/**
	 * 审批添加人时 要关闭几个activity
	 */
	public ArrayList<Activity> mShenPiActivities = new ArrayList<Activity>();

	public void closeShenPiActivity() {
		for (int i = 0; i < mPicActivities.size(); i++) {
			Activity activity = mPicActivities.get(i);
			if (activity != null) {
				activity.finish();
			}
		}
		mPicActivities.clear();
	}

	public void addShenPiActivity(Activity a) {
		if (!mPicActivities.contains(a)) {
			mPicActivities.add(a);
		}
	}

	public ArrayList<Activity> mPicActivities = new ArrayList<Activity>();

	public void closePicActivity() {
		for (int i = 0; i < mPicActivities.size(); i++) {
			mPicActivities.get(i).finish();
		}
		mPicActivities.clear();
	}

	public void addPicActivity(Activity a) {
		if (!mPicActivities.contains(a)) {
			mPicActivities.add(a);
		}
	}

	public static MyApp getI() {
		return instance;
	}

	public SharedPreferences getSP() {
		if (sp == null) {
			sp = getSharedPreferences("mitu", MODE_PRIVATE);
		}
		return sp;
	}

	private static ExecutorService threedPool;

	public static ExecutorService getThreedPool() {
		if (threedPool == null) {
			threedPool = Executors.newCachedThreadPool();
		}
		return threedPool;
	}

	/**
	 * dex初始化
	 * @param base
	 */
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	private void initImagePicker() {
		ImagePicker imagePicker = ImagePicker.getInstance();
		imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
		imagePicker.setShowCamera(false);                      //显示拍照按钮
		imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
		imagePicker.setSaveRectangle(false);                   //是否按矩形区域保存
		imagePicker.setSelectLimit(Constans.maxImgCount);              //选中数量限制
		imagePicker.setStyle(CropImageView.Style.CIRCLE);  //裁剪框的形状
		imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
		imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
		imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
		imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
	}

	private void registerActivityListener() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
				@Override
				public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
					//监听到 Activity创建事件 将该 Activity 加入list
					ActivityManager.getInstance().pushActivity(activity);
				}

				@Override
				public void onActivityStarted(Activity activity) {

				}

				@Override
				public void onActivityResumed(Activity activity) {

				}

				@Override
				public void onActivityPaused(Activity activity) {

				}

				@Override
				public void onActivityStopped(Activity activity) {

				}

				@Override
				public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

				}

				@Override
				public void onActivityDestroyed(Activity activity) {
					if (null == ActivityManager.getInstance().getActivitys() || ActivityManager.getInstance().getActivitys().isEmpty()) {
						return;
					}
					if (ActivityManager.getInstance().getActivitys().contains(activity)) {
						//监听到 Activity销毁事件 将该Activity 从list中移除
						ActivityManager.getInstance().popActivity(activity);
					}
				}
			});
		}
	}


	/**
	 * 今日头条屏幕适配方案终极版
	 */
	private void initAutoSize(){
		AutoSize.initCompatMultiProcess(this);
		AutoSizeConfig.getInstance()
				//是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
				.setCustomFragment(true)
				//是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
				//如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                 .setExcludeFontScale(true)
				//是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
                 .setLog(false)
				//是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时AutoSize 会将屏幕总高度减去状态栏高度来做适配
				//设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
//				.setUseDeviceSize(true)
				//是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
				.setBaseOnWidth(true)
				.setOnAdaptListener(new onAdaptListener() {
					@Override
					public void onAdaptBefore(Object target, Activity activity) {
						//使用以下代码, 可支持 Android 的分屏或缩放模式, 但前提是在分屏或缩放模式下当用户改变您 App 的窗口大小时
						//系统会重绘当前的页面, 经测试在某些机型, 某些情况下系统不会重绘当前页面, ScreenUtils.getScreenSize(activity) 的参数一定要不要传 Application!!!
//                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
//                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
					}

					@Override
					public void onAdaptAfter(Object target, Activity activity) {
					}
				});
	}

	/**
	 * 初始化讯飞语音
	 */
	private void initVoice(){
		// 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用半角“,”分隔。
		// 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符
		// 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
		SpeechUtility.createUtility(MyApp.this, "appid="+Constans.APPID_VOICE);
		// 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
		 Setting.setShowLog(false);
	}

	/**
	 * 初始化蒲公英
	 */
	private void initPgyer(){
		PgyCrashManager.register();
		PgyerCrashObservable.get().attach(new PgyerObserver() {
			@Override
			public void receivedCrash(Thread thread, Throwable throwable) {

			}
		});
		PgyerActivityManager.set(this);
//		Pgyer.setAppId("20dfb77471ebfd226f5c38d0c2767a60");
	}

	//初始化九图的图片显示
	private void initNineGridView(){
		NineGridView.setImageLoader(new NineGridView.ImageLoader() {
			@Override
			public void onDisplayImage(Context context, ImageView imageView, String url) {
				Glide.with(context)
                            .load(url)
                            .placeholder(R.drawable.qwb_normal_kuang)
                            .error(R.drawable.qwb_normal_kuang)
                             .transform(new CornersTransform(mContext,1))//圆角
                            .into(imageView);
			}

			@Override
			public Bitmap getCacheImage(String url) {
				return null;
			}
		});
	}

	/**
	 *  初始化X5内核
	 */
	private void initWebView(){
		QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
			@Override
			public void onCoreInitFinished() {
				//x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
			}
			@Override
			public void onViewInitFinished(boolean b) {
				//x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
				XLog.e("@@","加载内核是否成功:"+b);
			}
		});
	}


}
