package com.xmsx.cnlife.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qwb.utils.MyFileUtil;
import com.xmsx.qiweibao.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/***
 * dialog文件下载
 */
public class DownloadDialog extends Dialog implements android.view.View.OnClickListener {
	private static final int DOWNLOAD_PREPARE = 0;
	private static final int DOWNLOAD_WORK = 1;
	private static final int DOWNLOAD_OK = 2;
	private static final int DOWNLOAD_ERROR = 3;
	private static final String TAG = "DownloadDialog";
	private static Context mContext;

	private Button bt;
	private TextView qx;
	private ProgressBar pb;
	private LinearLayout ll_content;
	private TextView tv_content;
	private RelativeLayout rl_dowm;
	private TextView tv_wancheng;
	private TextView tv;

	/** 下载过程中不能点击 */
	private boolean isClick = false;
	private boolean downloadOk = false;

	private String filePath;
	private String url = null;// 下载的url
	private String content = null;// 更新内容
	private String isQz = "2";// 1:强制更新，2：否

	int fileSize = 0;// 文件大小
	int downloadSize = 0;// 下载的大小

	/**
	 * handler
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case DOWNLOAD_PREPARE:
					Toast.makeText(mContext, "准备下载", Toast.LENGTH_SHORT).show();
					pb.setVisibility(ProgressBar.VISIBLE);
//					Log.e(TAG, "文件大小:" + fileSize);
					pb.setMax(fileSize);
					break;
				case DOWNLOAD_WORK:
					pb.setProgress(downloadSize);
					int ds = downloadSize * 100;
					int res =  ds / fileSize;
//					tv.setText("已下载：" + res + "%");
//					Log.e(TAG, "已经下载:" + downloadSize);
//					Log.e(TAG, "文件大小:" + fileSize);
					tv_wancheng.setText(MyFileUtil.FormetFileSize(downloadSize) + "/" + MyFileUtil.FormetFileSize(fileSize));
					ll_content.setVisibility(View.GONE);
					rl_dowm.setVisibility(View.VISIBLE);
					break;
				case DOWNLOAD_OK:
					tv.setText("已下载：100%");
					downloadOk = true;
					bt.setText("安装新版本");
					downloadSize = 0;
					fileSize = 0;
					Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
					break;
				case DOWNLOAD_ERROR:
					downloadSize = 0;
					fileSize = 0;
					Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
					break;
			}
			super.handleMessage(msg);
		}
	};

	public DownloadDialog(Context context, String url, String content, String isQz) {
		super(context, R.style.Translucent_NoTitle);
		mContext = context;
		this.url = url;
		this.content = content;
		this.isQz = isQz;
		filePath = MyFileUtil.getPath(mContext, url);
	}

	@Override

	public void cancel() {
		super.cancel();
	}

	/**
	 * 下载文件
	 */
	private void downloadFile() {
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			InputStream is = conn.getInputStream();
			fileSize = conn.getContentLength();
			if (fileSize < 1 || is == null) {
				sendMessage(DOWNLOAD_ERROR);
			} else {
				sendMessage(DOWNLOAD_PREPARE);
				FileOutputStream fos = new FileOutputStream(filePath);
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
					fos.flush();
					downloadSize += len;
					sendMessage(DOWNLOAD_WORK);
				}
				sendMessage(DOWNLOAD_OK);
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			sendMessage(DOWNLOAD_ERROR);
			e.printStackTrace();
		}
	}

	/***
	 * 得到APK的路径
	 *
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}

	private void init() {
		bt =  this.findViewById(R.id.down_bt);
		qx =  this.findViewById(R.id.quxiao);
		bt.setOnClickListener(this);
		qx.setOnClickListener(this);
		tv = this.findViewById(R.id.down_tv);
		pb = this.findViewById(R.id.down_pb);

		ll_content = this.findViewById(R.id.ll_content);
		tv_content = this.findViewById(R.id.tv_content);
		rl_dowm = this.findViewById(R.id.rl_dowm);
		tv_wancheng = this.findViewById(R.id.tv_wancheng);
		tv_content.setText(content);

		if("1".equals(isQz)){
			qx.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.down_bt: {
				if (isClick) {
					// 启动一个线程下载文件
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							downloadFile();
						}
					});
					thread.start();
					isClick = false;
				}

				if (downloadOk) // 下载完成后 ，打开APK
				{
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				intent.setDataAndType(Uri.fromFile(new File(getFilePath())), "application/vnd.android.package-archive");
//				DownloadDialog.mContext.startActivity(intent);

					Context context=DownloadDialog.mContext;
					File file = new File(getFilePath());
					Intent var2 = new Intent();
					var2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					var2.setAction(Intent.ACTION_VIEW);
					if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
						Uri uriForFile = FileProvider.getUriForFile(context, "com.xmsx.qiweibao.fileProvider", file);
						var2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
						var2.setDataAndType(uriForFile, context.getContentResolver().getType(uriForFile));
					}else{
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
						context.startActivity(intent);
					}
					try {
						context.startActivity(var2);
					} catch (Exception var5) {
						var5.printStackTrace();
						Toast.makeText(context, "没有找到打开此类文件的程序", Toast.LENGTH_SHORT).show();
					}
					//取消
					cancel();
				}
				break;
			}
			case R.id.quxiao: {
				cancel();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_download_layuot);
		init();
	}

	/**
	 * @param what
	 */
	private void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	@Override
	public void show() {
		isClick = true;
		downloadOk = false;
		super.show();
	}

}
