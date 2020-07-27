package com.qwb.view.file.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qwb.view.file.model.FileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;

/**
 * 云盘模块--文件详情
 */
public class FileDetailActivity extends BaseNoTitleActivity {

	private FileBean fileBean;
	private String fileurl = "";
	private Button bt_down;
	private ExecutorService poolThread;
	private ProgressBar pb_file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_file_detail);

		poolThread = Executors.newCachedThreadPool();
		Intent intent = getIntent();
		if (intent != null) {
			fileBean = intent.getParcelableExtra("fileBean");
		}

		initPopup2();
		initUI();

		initData_download();
		initData_getBucketInfo();
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		// findViewById(R.id.tv_head_right).setOnClickListener(this);
		findViewById(R.id.img_head_right).setOnClickListener(this);
		TextView tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
		tv_headTitle.setText("文件详情");
		// tv_headRight.setText("");
		img_head_right.setImageResource(R.drawable.icon_add_yunpan);
		tv_headRight.setVisibility(View.GONE);
		img_head_right.setVisibility(View.GONE);
	}

	private void initUI() {
		initHead();

		TextView tv_fileName = (TextView) findViewById(R.id.tv_fileName);
		tv_fileSize = (TextView) findViewById(R.id.tv_fileSize);
		ImageView file_ico = (ImageView) findViewById(R.id.file_ico);
		pb_file = (ProgressBar) findViewById(R.id.pb_file);
		bt_down = (Button) findViewById(R.id.btn_download);

		initPb();

		bt_down.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * 按钮的状态 0 下载异常 1 下载中 2 暂停 3 下载完成
				 * */
				int model = fileBean.getModel();
				switch (model) {
					case 3:
						// 查看文件
						openFile(fileurl, fileBean.getTp1());
						break;
					case 0:
						// 开启下载文件
						if (!MyUtils.isEmptyString(fileurl)) {
							fileBean.setModel(1);
							bt_down.setBackgroundResource(R.drawable.select_roundcorner_blue);
							poolThread.execute(new MyThread(fileurl, fileBean));
						}
					case 1:
						// 暂停下载
						if (!MyUtils.isEmptyString(fileurl)) {
							fileBean.setModel(2);
							bt_down.setBackgroundResource(R.drawable.select_roundcorner_blue);
						}
						break;
					case 2:
						// 继续下载
						fileBean.setModel(1);
						poolThread.execute(new MyThread(fileurl, fileBean));
						break;
				}
			}
		});

		tv_fileSize.setText("文件大小:" + fileBean.getFsize());
		displayImageview(file_ico, fileBean.getTp1());
		
		String fileNm = fileBean.getFileNm();
		int indexOf = fileNm.indexOf("￥");
		String name=fileNm.substring(indexOf+1, fileNm.length());//去掉"id用户id_"(用户手机系统自带的名称)
		tv_fileName.setText(name);
	}

	private void initPb() {

		if (fileBean.getFileCurrent() != 0) {
			pb_file.setMax(fileBean.getFileLength());
		}
		if (fileBean.getFileCurrent() != 0) {
			pb_file.setProgress(fileBean.getFileCurrent());
		} else {
			pb_file.setProgress(0);
		}
		if (fileBean.getModel() != 0) {
			/**
			 * 按钮的状态 0 下载异常 1 下载中 2 暂停 3 下载完成
			 * */
			switch (fileBean.getModel()) {
			case 1:
				pb_file.setVisibility(View.VISIBLE);
				bt_down.setBackgroundResource(R.drawable.select_roundcorner_yellow);
				bt_down.setClickable(true);
				bt_down.setText("停止");
				break;
			case 2:
				pb_file.setVisibility(View.VISIBLE);
				bt_down.setBackgroundResource(R.drawable.select_roundcorner_blue);
				bt_down.setClickable(true);
				bt_down.setText("重载");
				break;
			case 3:
				bt_down.setBackgroundResource(R.drawable.select_roundcorner_green);
				pb_file.setVisibility(View.GONE);
				bt_down.setClickable(true);
				bt_down.setText("查看");
				break;
			}
		}else{
			if (new File(Constans.DIR_FILES, fileBean.getFileNm()).exists()) {
				// sd卡存在文件 直接查看
				fileBean.setModel(3);
				bt_down.setBackgroundResource(R.drawable.select_roundcorner_green);
				pb_file.setVisibility(View.GONE);
				bt_down.setClickable(true);
				bt_down.setText("查看");
			} else {
				pb_file.setVisibility(View.GONE);
				bt_down.setBackgroundResource(R.drawable.select_roundcorner_blue);
				bt_down.setClickable(true);
				bt_down.setText("下载");
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.img_head_right:
			popWin2.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0,
					0);
			backgroundAlpha(0.5f);
			break;
		}
	}

	private void initData_download() {
		Map<String, String> params = new HashMap<>();
		params.put("key", fileBean.getFileNm());
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.downloadBucke)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);
	}
	
	//获取文件信息
	private void initData_getBucketInfo() {
		Map<String, String> params = new HashMap<>();
		params.put("key", fileBean.getFileNm());
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.getBucketInfo)
				.id(2)
				.build()
				.execute(new MyStringCallback(),null);
	}


	private PopupWindow popWin2 = null;
	private View contentView2 = null;

	private void initPopup2() {
		contentView2 = getLayoutInflater().inflate(R.layout.x_popup_yunpan_more,
				null);
		// 新建文件夹
		popWin2 = new PopupWindow(contentView2,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWin2.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin2.setBackgroundDrawable(new BitmapDrawable());
		popWin2.setOnDismissListener(new poponDismissListener());
	}

	// 窗体的背景透明度0~1
	public void backgroundAlpha(float bgAlpha) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; // 0.0-1.0
		getWindow().setAttributes(lp);
	}

	// 窗体消失监听
	class poponDismissListener implements PopupWindow.OnDismissListener {
		@Override
		public void onDismiss() {
			backgroundAlpha(1f);
		}
	}

	// 对应文件类型设置图标
	private void displayImageview(ImageView file_ico, String fileType) {
		if (MyUtils.isEmptyString(fileType)) {
			file_ico.setImageResource(R.drawable.file_other);
		} else {
			fileType = fileType.toLowerCase();
			if ("xls".equals(fileType) || "xlsx".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_excel);
			} else if ("docx".equals(fileType) || "doc".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_word);
			} else if ("pdf".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_pdf);
			} else if ("ppt".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_ppt);
			} else if ("txt".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_txt);
			} else if ("png".equals(fileType) || "jpg".equals(fileType)
					|| "bmp".equals(fileType) || "jpeg".equals(fileType)
					|| "gif".equals(fileType)) {
				file_ico.setImageResource(R.drawable.file_pic);
			} else {
				file_ico.setImageResource(R.drawable.file_other);
			}
		}
	}

	private class MyThread implements Runnable {
		private String path;
		private int mFilelenth;
		private FileBean fileInfor;

		public MyThread(String path, FileBean fileInfor) {
			this.path = path;
			this.fileInfor = fileInfor;
		}

		@SuppressWarnings("resource")
		public void run() {
			InputStream is = null;

			try {
				// 先判断SDcard的使用环境
				if (!Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					// SDCard状态问题，无法下载
					fileInfor.setModel(0);
					fileHandler.sendEmptyMessage(0);
					return;
				}
				// 判断SD卡状态、空间
				File apkFile = new File(Constans.DIR_FILES);
				// 判断文件是否存在
				if (!apkFile.exists()) {
					apkFile.getParentFile().mkdirs();
				}
				// 获取文件大小
				URL urlcurrent = new URL(path);
				HttpURLConnection conncurrent = (HttpURLConnection) urlcurrent
						.openConnection();
				// HTTP连接超时设置
				conncurrent.setConnectTimeout(15000);
				// HTTP数据传输超时设置
				conncurrent.setReadTimeout(15000);
				if (conncurrent.getResponseCode() != HttpURLConnection.HTTP_OK) {
					fileInfor.setModel(0);
					fileHandler.sendEmptyMessage(0);
					return;
				}
				mFilelenth = conncurrent.getContentLength();
				conncurrent.disconnect();

				// 开始下载
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// HTTP连接超时设置
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				// HTTP数据传输超时设置
				conn.setReadTimeout(15000);

				int fileCurrent = fileInfor.getFileCurrent();
				Log.e("fileCurrent----", "" + fileCurrent);
				Log.e("mFilelenth----", "" + mFilelenth);
				conn.setRequestProperty("Range", "bytes=" + fileCurrent + "-"
						+ mFilelenth);

				is = conn.getInputStream();
				File outfile = new File(Constans.DIR_FILES, fileBean.getFileNm());
				if (!outfile.exists()) {
					outfile.createNewFile();
				}

				RandomAccessFile raf = new RandomAccessFile(outfile, "rwd");
				raf.seek(fileCurrent);
				int len = -1;
				// 每次读取5kb
				byte[] buffer = new byte[1024 * 5];
				int loadlenth = fileCurrent;
				int times = 1;
				fileInfor.setFileLength(mFilelenth);
				while (-1 != (len = is.read(buffer))) {

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						fileInfor.setModel(0);
						fileHandler.sendEmptyMessage(0);
					}
					loadlenth += len;
					// 将流写入到文件
					raf.write(buffer, 0, len);
					if ((loadlenth * 100) / mFilelenth > 2 * times) {
						times++;
						fileInfor.setModel(1);
						fileInfor.setFileCurrent(loadlenth);
						fileHandler.sendEmptyMessage(0);
					}

					if (fileInfor.getModel() == 2) {
						// 暂停
						if (outfile.exists()) {
							outfile.delete();
						}
						fileInfor.setModel(2);
						fileInfor.setFileCurrent(0);
						fileHandler.sendEmptyMessage(0);
						return;
					} else if (FileDetailActivity.this.isFinishing()) {
						// 页面销毁
						if (outfile.exists()) {
							outfile.delete();
						}
						return;
					}
				}
				fileInfor.setModel(3);
				fileInfor.setFileCurrent(mFilelenth);
				fileHandler.sendEmptyMessage(0);
			} catch (MalformedURLException e) {
				fileInfor.setModel(0);
				fileHandler.sendEmptyMessage(0);
			} catch (IOException e) {
				fileInfor.setModel(0);
				fileHandler.sendEmptyMessage(0);
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						fileInfor.setModel(0);
						fileHandler.sendEmptyMessage(0);
					}
				}
			}

		}
	}

	@SuppressLint("HandlerLeak")
	private Handler fileHandler = new Handler() {
		public void handleMessage(Message msg) {
			initPb();
		}
	};
	private TextView tv_fileSize;

	/**
	 * 打开文件
	 */
	private void openFile(String fileurl, String fileType) {
		Uri uri = null;
		try {
			String filename = fileBean.getFileNm();
			uri = Uri.fromFile(new File(Constans.DIR_FILES, filename));
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			fileType = fileType.toLowerCase();
			if ("xls".equals(fileType)) {
				intent.setDataAndType(uri, "application/vnd.ms-excel");
			} else if ("docx".equals(fileType) || "doc".equals(fileType)) {
				intent.setDataAndType(uri, "application/msword");
			} else if ("pdf".equals(fileType)) {
				intent.setDataAndType(uri, "application/pdf");
			} else if ("ppt".equals(fileType)) {
				intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
			} else if ("txt".equals(fileType)) {
				intent.setDataAndType(uri, "text/plain");
			} else if ("png".equals(fileType) || "jpg".equals(fileType)
					|| "bmp".equals(fileType) || "jpeg".equals(fileType)
					|| "gif".equals(fileType)) {
				intent.setDataAndType(uri, "image/*");
			}
			startActivity(intent);
		} catch (Exception e) {
			ToastUtils.showCustomToast("打开文件失败");
		}
	}


	// ------------------------------TODO OKHttp回调数据--------------------------
	public class MyStringCallback extends StringCallback {
		@Override
		public void onError(Call call, Exception e, int id) {
			e.printStackTrace();
		}

		@Override
		public void onResponse(String response, int id) {
			resultData(response, id);
		}
	}
	/**
	 * 回调数据
	 */
	private void resultData(String json, int tag) {
		if (!MyUtils.isEmptyString(json) && json.startsWith("{")) {
			switch (tag) {
				case 1:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							fileurl = jsonObject.getString("downloadRUL");
							String msg = jsonObject.getString("msg");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 2:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							long fsize = jsonObject.getLong("fsize");
							String fileSize = MyUtils.getFileSize(fsize);
							if(!MyUtils.isEmptyString(fileSize)){
								tv_fileSize.setVisibility(View.VISIBLE);
								tv_fileSize.setText("文件大小:"+fileSize);
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
			}
		}
	}

}
