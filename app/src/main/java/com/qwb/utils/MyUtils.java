package com.qwb.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qwb.application.MyApp;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 七七八八的工具类
 * @author 武
 */
public class MyUtils {

	private static Context mApplicationContent;

	/**
	 * 在application中初始化
	 */
	public static void initUtils(Application app) {
		mApplicationContent = app.getApplicationContext();
	}

	/**
	 * 检查是否存在SDCard
	 *
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据总的数据条数 跟后台每一页放回的条数算出总页数
	 *
	 * @param total
	 *            后台放回的数据总条数
	 */
	public static int getTPager(int total) {

		return total % 10 == 0 ? total / 10 : total / 10 + 1;
	}

	/**
	 * 从游标里获取String类型数据
	 */
	public static String getSFromCursor(Cursor cursor, String key) {
		return cursor.getString(cursor.getColumnIndex(key));
	}

	/**
	 * 从游标里获取Int类型数据
	 */
	public static int getIFromCursor(Cursor cursor, String key) {
		return cursor.getInt(cursor.getColumnIndex(key));
	}

	/**
	 * 从ViewHolder中获取View
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T getViewFromVH(View convertView, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			convertView.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}

	/**
	 * 得到固定格式为 2014-12-11 10:50:13 的字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getSysTime() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	/**
	 * 获取今天日期--格式为 2014-12-11 的字符串
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getJintian() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	/**
	 * 获取日期--格式为 2014-12-11 的字符串 只有i：把日期往后增加一天.整数往后推,负数往前移动
	 */
	public static String getDate(int i) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, i);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	// 上个月的今天
	public static String getNowOfLastMonth() {
		SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar aGregorianCalendar = new GregorianCalendar();
		aGregorianCalendar.set(Calendar.MONTH, aGregorianCalendar.get(Calendar.MONTH) - 1);
		String nowOfLastMonth = aSimpleDateFormat.format(aGregorianCalendar.getTime());
		return nowOfLastMonth;
	}

	/**
	 * 得到当前系统时间和GMT时间(格林威治时间)1970年1月1号0时0分0秒所差的毫秒数 可以作为文件名 该时间点是唯一的
	 */
	public static long getTimeAsName() {
		return System.currentTimeMillis();
	}

	/**
	 * @说明 得到格式为 formate 的日期Str 例如 ：yyyy年MM月dd日 HH:mm yyyy-MM-dd HH:mm:ss
	 *     yyyyMMddHHmm
	 * @param formate
	 *            转化格式
	 * @return String
	 */
	public static String getDateToStr(String formate) throws Exception {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formate, java.util.Locale.CHINA);
			return simpleDateFormat.format(new Date());
		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	public static String getStrFromDate(Date date) {

		if (date != null) {

			Calendar cl = Calendar.getInstance();
			cl.setTime(date);

			String text = cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH) + 1) + "-" + cl.get(Calendar.DATE) + " "
					+ cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE);
			return text;
		} else {
			return "";
		}

	}

	/**
	 * 传进来的time的格式要跟"yyyy-MM-dd HH:mm"一致
	 */
	public static Date getDateFromTime(String time) {
		if (!MyUtils.isEmptyString(time)) {
			DateFormat startdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date;
			try {
				date = startdf.parse(time);
			} catch (ParseException e) {
				date = null;
			}
			return date;
		} else {
			return null;
		}

	}

	/**
	 * 将时间装换成自定义格式 如 "yyyy-MM-dd HH:mm:ss"
	 *
	 *
	 */
	public static String formatTimeAsFormat(String time, String format) {
		DateFormat df = new SimpleDateFormat(format);
		Date aDate = null;// 从中获取秒数
		try {
			aDate = df.parse(time);
		} catch (ParseException e) {
			return time;
		}
		return df.format(aDate);

	}

	/**
	 * 时间格式转换 类型有 ：刚刚、 N分钟之前、 N小时之前 、 N天前
	 */
	public static String formatTime(String time) {

		long days = 0;
		long hours = 0;
		long minutes = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			Date d1 = df.parse(time);
			long enterTime = System.currentTimeMillis();
			long diff = enterTime - d1.getTime();// 这样得到的差值是微秒级别
			days = diff / (1000 * 60 * 60 * 24);
			hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
			minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
		} catch (ParseException e) {
		}
		if (days > 0) {
			return "" + days + "天前";
		} else if (hours > 0) {
			return "" + hours + "小时前";
		} else {
			return "" + minutes + "分钟前";
		}
	}

	/**
	 * 两个时间的差有没有超过5分钟
	 */
	@SuppressLint("SimpleDateFormat")
	public static boolean isBeforeFiveMinute(String starttime, String endtime) {

		if (MyUtils.isEmptyString(starttime) || MyUtils.isEmptyString(endtime)) {
			return false;
		}

		DateFormat startdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date startDate = null;// 从中获取秒数
		try {
			startDate = startdf.parse(starttime);
		} catch (ParseException e) {
			return false;
		}

		// 开始时间的秒数
		long startSecond = startDate.getTime();

		DateFormat enddf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date endDate = null;// 从中获取秒数
		try {
			endDate = enddf.parse(endtime);
		} catch (ParseException e) {
			return false;
		}

		long endseconds = endDate.getTime();

		long seconds = endseconds - startSecond;// 这样得到的差值是微秒级别
		long days = seconds / (1000 * 60 * 5);
		if (days >= 1) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 将微秒转换成自定义格式 如 "yyyy-MM-dd HH:mm:ss"
	 *
	 *
	 */
	public static String formatTimeFromMillis(long millis, String format) {
		try {
			Date date = new Date(millis);
			DateFormat df = new SimpleDateFormat(format);
			return df.format(date);
		} catch (Exception e) {
			return String.valueOf(millis);
		}
	}

	/**
	 * 时间格式转换 24小时制 类型有 ：18:33、昨天18:33、星期二18:33、2015-04-19
	 */
	public static String formatTime2(String time) {
		if (MyUtils.isEmptyString(time)) {
			return "";
		}
		// 传进来的时间处理
		double days = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date aDate = null;// 从中获取秒数
		try {
			aDate = df.parse(time);
		} catch (ParseException e) {
			return time;
		}
		// 传进来的时间格式化
		Calendar calendar = df.getCalendar();
		calendar.setTime(aDate);

		// 当前系统时间格式化
		long enterTime = System.currentTimeMillis();
		Calendar calendarNow = Calendar.getInstance();
		calendarNow.setTime(new Date());
		long time2 = aDate.getTime();
		long diff = enterTime - time2;// 这样得到的差值是微秒级别
		days = Double.valueOf(diff) / Double.valueOf(1000 * 60 * 60 * 24);

		int i = calendar.get(Calendar.HOUR_OF_DAY);
		// 分钟小于10时添加“0”
		int j = calendar.get(Calendar.MINUTE);
		String minutes = "";
		if (j < 10) {
			minutes = "0" + j;
		} else {
			minutes = "" + j;
		}
		if (days <= 1) {
			// 时间差还没超过24小时
			if (calendar.get(Calendar.DATE) != calendarNow.get(Calendar.DATE)) {

				return "昨天  " + i + ":" + minutes;
			} else {
				return i + ":" + minutes;
			}
		} else if (days > 1 && days < 7) {
			// 7天内 先判断 是星期几 如果是星期一 就一上周处理

			// 先判断现在是星期几
			if (calendarNow.get(Calendar.DAY_OF_WEEK) == 1) {
				// 现在是周日
				String week = "";
				int dayForWeek = 0;
				if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
					dayForWeek = 7;
				} else {
					dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
				}
				switch (dayForWeek) {
				case 1:
					week = "一";
					break;
				case 2:
					week = "二";
					break;
				case 3:
					week = "三";
					break;
				case 4:
					week = "四";
					break;
				case 5:
					week = "五";
					break;
				case 6:
					week = "六";
					break;
				case 7:
					week = "日";
					break;
				}
				return "星期" + week + " " + i + ":" + minutes;
			} else {
				// 现在是非周日
				int nowWeek = calendarNow.get(Calendar.DAY_OF_WEEK) - 1;
				// 被对比的时间是星期几
				int otherWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
				if (otherWeek < nowWeek && otherWeek >= 1) {
					String week = "";
					int dayForWeek = 0;
					if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
						dayForWeek = 7;
					} else {
						dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
					}
					switch (dayForWeek) {
					case 1:
						week = "一";
						break;
					case 2:
						week = "二";
						break;
					case 3:
						week = "三";
						break;
					case 4:
						week = "四";
						break;
					case 5:
						week = "五";
						break;
					case 6:
						week = "六";
						break;
					case 7:
						week = "日";
						break;
					}
					return "星期" + week + " " + i + ":" + minutes;
				} else {
					return time.substring(0, 11);
				}
			}
		} else {
			// 一周以上
			return formatTimeAsFormat(time, "yyyy-MM-dd");
		}
	}

	/**
	 * 如果该字符串为空 则return掉
	 */
	public static boolean isEmptyString(String json) {
		if (TextUtils.isEmpty(json)) {
			return true;
		}
		if (TextUtils.isEmpty(json.trim())) {
			return true;
		}

		if ("null".equals(json.trim())) {
			return true;
		}

		if ("NULL".equals(json.trim())) {
			return true;
		}
		if ("".equals(json.trim())) {
			return true;
		}
		return false;
	}

	// 判断当前用户是不是公司的管理者或创建者
	public static boolean isCreater() {
		if ("1".equals(SPUtils.getSValues("iscreat")) || "2".equals(SPUtils.getSValues("iscreat"))) {
			return true;
		} else {
			return false;
		}
	}

	// 判断手机格式是否正确
	public static boolean isMobileNum(String mobiles) {
		if (mobiles.length() == 11) {
			String subSequence = mobiles.subSequence(0, 1).toString();
			if ("1".equals(subSequence)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}





	/** 判断email格式是否正确 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/** 判断email格式是否正确 */
	public static boolean isURL(String URL) {
		String str = "^((https|http|ftp|rtsp|mms)?://)"
				+ "?(([0-9a-zA-Z_!~*'().&=+$%-]+: )?[0-9a-zA-Z_!~*'().&=+$%-]+@)?" // ftp的user@
				+ "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
				+ "|" // 允许IP和DOMAIN（域名）
				+ "([0-9a-zA-Z_!~*'()-]+\\.)*" // 域名- www.
				+ "([0-9a-zA-Z][0-9a-zA-Z-]{0,61})?[0-9a-zA-Z]\\." // 二级域名
				+ "[a-zA-Z]{2,6})" // first level domain- .com or .museum
				+ "(:[0-9]{1,4})?" // 端口- :80
				+ "((/?)|" + "(/[0-9a-zA-Z_!~*'().;?:@&=+$,%#-]+)+/?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(URL);

		return m.matches();
	}

	public static String getFileNameFromUrl(String url) {
		if (!MyUtils.isEmptyString(url)) {
			int lastIndexOf = url.lastIndexOf("/");
			return url.substring(lastIndexOf, url.length());
		}
		return "";

	}

	/**
	 * 从将bitmap装换成file文件
	 */
	public static File getImageFileFromBitmap(Bitmap bitmap, String filename) {
		if (bitmap == null) {
			return null;
		}
		File file = new File(Constans.DIR_IMAGE);
		if (!file.exists()) {
			file.mkdirs();
		}

		file = new File(Constans.DIR_IMAGE_CACHE, filename);

		BufferedOutputStream bos = null;
		try {

			bos = new BufferedOutputStream(new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
		try {
			bos.flush();
			bos.close();
		} catch (IOException e) {
			return null;
		}

		return file;

	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/**
	 * 从路径中获取文件
	 */
	public static File getImageFileFromPath2(String path) {

		if (TextUtils.isEmpty(path)) {
			return null;
		}

		// 获取旋转的角度
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			Log.e("IOException", "旋转角度异常");
		}

		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e1) {
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000) && (options.outHeight >> i <= 1000)) {
				try {
					in = new BufferedInputStream(new FileInputStream(new File(path)));
				} catch (FileNotFoundException e) {
				}
				int size = (int) Math.pow(2.0D, i);
				options.inSampleSize = size;
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}

		// 修正图片旋转的角
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		File file_img = new File(path);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

		try {
			FileOutputStream fos = new FileOutputStream(file_img);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return file_img;
	}

	/**
	 * 从路径中获取图片文件
	 */
	@SuppressWarnings("resource")
	public static File getImageFileFromPath(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File file = new File(path);
		String name = file.getName();

		File floders = new File(Constans.DIR_IMAGE_CACHE);// 缓存图片文件夹
		if (!floders.exists()) {
			floders.mkdirs();
		}

		File cachefile = new File(Constans.DIR_IMAGE_CACHE, name);
		if (cachefile != null && cachefile.exists()) {
			return cachefile;
		}
		BufferedInputStream in_buff = null;
		try {
			in_buff = new BufferedInputStream(new FileInputStream(new File(path)));
			int i = in_buff.available() / 1024;
			if (i <= 150) {
				Log.e("size", "小于150");
				return file;
			}
		} catch (FileNotFoundException e1) {
			return null;
		} catch (IOException e) {
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in_buff, null, options);

		if (in_buff != null) {
			try {
				in_buff.close();
			} catch (IOException e) {
				return null;
			}
		}

		Bitmap decodeStream = null;

		double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);

		options.inSampleSize = (int) Math.ceil(ratio);
		options.inJustDecodeBounds = false;

		BufferedInputStream in_file = null;
		try {
			in_file = new BufferedInputStream(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e1) {
			return null;
		}

		decodeStream = BitmapFactory.decodeStream(in_file, null, options);

		if (decodeStream == null) {
			return null;
		}

		if (in_file != null) {
			try {
				in_file.close();
			} catch (IOException e) {
				return null;
			}
		}

		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			return null;
		}
		if (degree > 0) {
			// 修正角度
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap resizedBitmap = Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(),
					decodeStream.getHeight(), matrix, true);
			// bitma转换成文件
			return compressImageFileSize(resizedBitmap, cachefile);
		} else {
			return compressImageFileSize(decodeStream, cachefile);
		}
	}

	/**
	 * 从路径中获取图片文件
	 */
	@SuppressWarnings("resource")
	public static File getImageFileFromPath3(String path) {

		if (TextUtils.isEmpty(path)) {
			return null;
		}
		File file = new File(path);
		String name = file.getName();

		File floders = new File(Constans.DIR_IMAGE_CACHE);// 缓存图片文件夹
		if (!floders.exists()) {
			floders.mkdirs();
		}

		File cachefile = new File(Constans.DIR_IMAGE_CACHE, name);
		if (cachefile != null && cachefile.exists()) {

			return cachefile;
		}
		BufferedInputStream in_buff = null;
		try {
			in_buff = new BufferedInputStream(new FileInputStream(new File(path)));
			int i = in_buff.available() / 1024;
			if (i <= 150) {
				return file;
			}
		} catch (FileNotFoundException e1) {
			return null;
		} catch (IOException e) {
			return null;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in_buff, null, options);

		if (in_buff != null) {
			try {
				in_buff.close();
			} catch (IOException e) {
				return null;
			}
		}

		Bitmap decodeStream = null;

		double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);

		options.inSampleSize = (int) Math.ceil(ratio);
		options.inJustDecodeBounds = false;

		BufferedInputStream in_file = null;
		try {
			in_file = new BufferedInputStream(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e1) {
			return null;
		}

		decodeStream = BitmapFactory.decodeStream(in_file, null, options);

		if (decodeStream == null) {
			return null;
		}

		if (in_file != null) {
			try {
				in_file.close();
			} catch (IOException e) {
				return null;
			}
		}

		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			return null;
		}
		if (degree > 0) {
			// 修正角度
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap resizedBitmap = Bitmap.createBitmap(decodeStream, 0, 0, decodeStream.getWidth(),decodeStream.getHeight(), matrix, true);
			// bitma转换成文件
			return compressImageFileSize3(resizedBitmap, cachefile);
		} else {
			return compressImageFileSize3(decodeStream, cachefile);
		}
	}

	/**
	 * 将bitmap转换成文件
	 */
	public static File getFileFromBitmap(Bitmap bitmap, File file) {
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 40, fileOutputStream);
			return file;
		} catch (FileNotFoundException e) {

			return null;
		}
	}

	/**
	 * @param resizedBitmap
	 * @param cachefile
	 */
	public static File compressImageFileSize(Bitmap resizedBitmap, File cachefile) {

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(cachefile);
			resizedBitmap.compress(CompressFormat.JPEG, 50, fileOutputStream);// 是50压缩率，表示压缩50%;
			if (resizedBitmap != null && !resizedBitmap.isRecycled()) {
				resizedBitmap.recycle();
			}
			return cachefile;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * @param resizedBitmap
	 * @param cachefile
	 */
	public static File compressImageFileSize3(Bitmap resizedBitmap, File cachefile) {

		FileOutputStream fileOutputStream;
		try {
			fileOutputStream = new FileOutputStream(cachefile);
			resizedBitmap.compress(CompressFormat.JPEG, 90, fileOutputStream);// 是90压缩率，表示压缩10%;
			// 如果不压缩是100，表示压缩率为0
			if (resizedBitmap != null && !resizedBitmap.isRecycled()) {
				resizedBitmap.recycle();
			}
			return cachefile;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 获取文件大小
	 */
	@SuppressWarnings("resource")
	public static long getFileSize(File file) {
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				return 0;
			}
			try {
				return fis.available();
			} catch (IOException e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 隐藏输入法
	 */
	public static void hideIMM(View v) {
		InputMethodManager imm = MyApp.getImm();
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * 打开输入法
	 */
	public static void opendIMM(final View v) {
		v.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				InputMethodManager inputManager =
				(InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(v, 0);
			}
		}, 100);
	}

	/**
	 * 把dp值转换成px像素
	 */
	public static int getPXfromDP(float dp, Context context) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	public static SQLiteDatabase getDB() {
		return MyApp.getDB();
	}

//	public static MyPostUtil creat() {
//		return new MyPostUtil();
//	}

	/**
	 * color 颜色值 比如 #179F01 str 字符串 startp 有颜色字体的开始位置 endp 有颜色字体的结束位置
	 */
	public static SpannableString getColorText(String color, String str, int startP, int endp) {
		SpannableString sb = new SpannableString(str);
		sb.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startP, endp, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sb;
	}

	/**
	 * 拨打电话
	 */
	public static void call(Context context, String phoneNum) {
		// 传入服务， parse（）解析号码
//		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
//		// 通知activtity处理传入的call服务
//		context.startActivity(intent);

		 Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));//跳转到拨号界面，同时传递电话号码
		 context.startActivity(dialIntent);
	}

	// jsonp转json
	public static String getjsonFromJsonp(String json) {
		if (!MyUtils.isEmptyString(json) && json.length() >= 3) {
			return json.substring(1, json.length() - 1);
		} else {
			return json;
		}
	}

	/**
	 * 判断是否有网络
	 */
	public static boolean isNetwork(Context context) {
		if (context != null) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = cm.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		ToastUtils.showShort(context, "网络没连接哦");
		return false;
	}

	public static void logE(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void logW(String tag, String msg) {
		Log.w(tag, msg);
	}

	/**
	 * type 文件类型
	 */
	public static Intent getOpenFileIntent(Uri uri, String type) {
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (TextUtils.isEmpty(type)) {
			// 未知类型的文件 打开手机应用让用户选
			intent.setDataAndType(uri, "application/*");
			return intent;
		} else {

			String intentType = null;
			for (int i = 0; i < MIME_MapTable.length; i++) { // MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
				if (type.equals(MIME_MapTable[i][0]))
					intentType = MIME_MapTable[i][1];
			}
			if (TextUtils.isEmpty(intentType)) {
				// 未匹配出后缀 打开手机应用让用户选
				intent.setDataAndType(uri, "application/*");
				return intent;
			} else {
				intent.setDataAndType(uri, intentType);
				return intent;
			}
		}
	}

	private static String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x_activity_rz_mobile-ms-asf" }, { ".avi", "video/x_activity_rz_mobile-msvideo" }, { ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" },
			{ ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x_activity_rz_mobile-gtar" },
			{ ".gz", "application/x_activity_rz_mobile-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" },
			{ ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x_activity_rz_mobile-javascript" }, { ".log", "text/plain" },
			{ ".m3u", "audio/x_activity_rz_mobile-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x_activity_rz_mobile-m4v" },
			{ ".mov", "video/quicktime" }, { ".mp2", "audio/x_activity_rz_mobile-mpeg" }, { ".mp3", "audio/x_activity_rz_mobile-mpeg" },
			{ ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".arm", "audio/*" }, { ".ogg", "audio/ogg" },
			{ ".pdf", "application/pdf" }, { ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" }, { ".rar", "application/*" },
			{ ".rmvb", "audio/x_activity_rz_mobile-pn-realaudio" }, { ".rtf", "application/rtf" }, { ".sh", "text/plain" },
			{ ".tar", "application/x_activity_rz_mobile-tar" }, { ".tgz", "application/x_activity_rz_mobile-compressed" }, { ".txt", "text/plain" },
			{ ".wav", "audio/x_activity_rz_mobile-wav" }, { ".wma", "audio/x_activity_rz_mobile-ms-wma" }, { ".wmv", "audio/x_activity_rz_mobile-ms-wmv" },
			{ ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" }, { ".z", "application/x_activity_rz_mobile-compress" },
			{ ".zip", "application/*" }, { "", "*/*" } };

	/**
	 * 把dp转换成px
	 */
	public static int Dp2Px(float dp) {
		final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	/**
	 * 把px转换成dp
	 */
	public static int Px2Dp(float px) {
		final float scale = mApplicationContent.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	public static boolean isServiceRunning(Context mContext, String className) {

		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}

	/**
	 * 判断某个服务是否正在运行的方法
	 *
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：com.baidu.trace.LBSTraceService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(80);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			System.out.println("serviceName : " + mName);
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

	/**
	 * 获取设备IMEI码
	 */
	public static String getImei(Context context) {
		String mImei = "NULL";
		try {
			mImei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			System.out.println("获取IMEI码失败");
			mImei = "NULL";
		}
		return mImei;
	}
	/**
	 * 获取前一周的日期
	 */
	public static String getdate_week(Context context) {
		String paramStartDate = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateNow = new Date();
		Date dateBefore = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
		cal.add(Calendar.DAY_OF_MONTH, -6);
		dateBefore = cal.getTime();
		paramStartDate = sdf.format(dateBefore);
		return paramStartDate;
	}

	/**
	 * 得到本周周一
	 */
	public static String getMondayOfThisWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 1);
		return sdf.format(c.getTime());
	}

	/**
	 * 得到本周周日
	 * 
	 * @return yyyy-MM-dd
	 */
	public static String getSundayOfThisWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0)
			day_of_week = 7;
		c.add(Calendar.DATE, -day_of_week + 7);
		return sdf.format(c.getTime());
	}

	/**
	 * 指定日期是否再系统日期前
	 * @return
	 */
	public static boolean DateBefore(String mydate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date sysdate, selectdate;
		try {
			sysdate = sdf.parse(sdf.format(new Date()));
			selectdate = sdf.parse(mydate);
			// 测试此日期是否在指定日期之前。
			boolean flag = selectdate.before(sysdate);
			if (flag) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 字符串的日期格式:相差几天
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 转换文件大小
	 */
	public static String getFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	// 将时间戳转为字符串
	public static String getStrFromTime(long cc_time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 例如：cc_time=1291778220
		long lcc_time = Long.valueOf(cc_time);
		re_StrTime = sdf.format(new Date(lcc_time * 1000L));
		return re_StrTime;
	}

	/**
	 * 获取今天00：00时间戳 i:0今天 -24：昨天
	 * 
	 * @return
	 */
	public static long getTimeInMillis(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, i);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTimeInMillis() / 1000;
	}

	/**
	 * 获取版本号
	 * 
	 * @return
	 */
	public static String getAppVersion() {
		try {
			PackageManager packageManager = mApplicationContent.getPackageManager();
			PackageInfo info = packageManager.getPackageInfo(mApplicationContent.getPackageName(), 0);
			String versionCode = info.versionName;
			return versionCode;
		} catch (NameNotFoundException e) {
		}
		return "";
	}


	/**
	 * 当dataTp=4,自定义获取mids
	 * apply:模块的编码
	 */
	public static String getMids(int tp, String apply) {
		String mids = null;
		String qwbStr = null;
		if (tp == 1) {
			qwbStr = SPUtils.getSValues(ConstantUtils.Sp.APPLY_QWB_LIST);
		} else if (tp == 2) {
			qwbStr = SPUtils.getSValues(ConstantUtils.Sp.APPLY_QWB_LIST);
		}

		if (!MyUtils.isEmptyString(qwbStr)) {
			JSONArray parseArray = JSON.parseArray(qwbStr);
			if (parseArray != null) {
				for (int i = 0; i < parseArray.size(); i++) {
					JSONObject jsonObject = parseArray.getJSONObject(i);
					String applyCode = jsonObject.getString("applyCode");// 应用代码
					if (apply.equals(applyCode)) {
						mids = jsonObject.getString("mids");
						return mids;
					}
				}
			}
		}
		return mids;
	}
	
	/**
	 * 根据文件路径--清除缓存图片
	 */
	public static void deleteFile(String path){
		File dir = new File(path);
		deleteDirWihtFile(dir);
	}
	public static void deleteDirWihtFile(File dir) {
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDirWihtFile(file); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	/**
	 * 有选择图片的地方，销毁界面要清空下
	 */
	public static void clearPhoto(){
		Constans.pic_type = 0;// 退出界面变为原状态
		Constans.isDelModel = false;// 退出界面变为原状态
		Constans.isDelModel2 = false;// 退出界面变为原状态
		Constans.isDelModel3 = false;// 退出界面变为原状态
		Constans.current.clear();
		Constans.current2.clear();
		Constans.current3.clear();
		Constans.publish_pics.clear();
		Constans.publish_pics1.clear();
		Constans.publish_pics2.clear();
		Constans.publish_pics3.clear();
		Constans.publish_pics1111.clear();
		Constans.publish_pics2222.clear();
		Constans.publish_pics3333.clear();
		Constans.publish_file1.clear();
		Constans.publish_file2.clear();
		Constans.publish_file3.clear();
		Constans.picmap.clear();
		Constans.picmap2.clear();
		Constans.picmap3.clear();
	}


}
