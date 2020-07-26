package com.qwb.view.wangpan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qwb.view.file.model.FileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.xmsx.qiweibao.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 选择文件
 */
public class FileSelectorActivity extends BaseNoTitleActivity {
	
	public static int requestCodeSingleFile;//获取 单个文件 的路径
	public static String keyClassName = "keyClassName";//从哪个Activity 到 FileSelectorActivity
	public static String keyIsSelectFile = "keyIsSelectFile";//true 选择文件路径,fasle选择文件夹路径 默认为true
	public static String keyIsSingleSelector = "keyIsSingleSelector";//true:单选，false:多选
	private boolean isSelectFile;//是否选择文件
	private boolean isSingleSelector;// 是否单选
	
	private Context context;
	private String rootPath;
	private String className;
	private Class<?> clazz;
	private FileSelectorAdapter adapter;
	private LinearLayout layoutPath;
	private List<TextView> listTvPath;
	private boolean hasClicked;

	/**
	 * 在 onActivityResult 中获取 文件的全路径
	 */
	public static String keyFilePaths = "keyFilePaths";
	public static String keyFileName = "keyFileName";//文件名

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_selector_file);
		hasClicked = false;
		context = this;
		listTvPath = new ArrayList<TextView>();
		rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		adapter = new FileSelectorAdapter(context);
		
		Intent intent = getIntent();
		className = intent.getStringExtra(keyClassName);
		Log.e("className = ","className = " + className);
		isSelectFile = intent.getBooleanExtra(keyIsSelectFile, true);
		isSingleSelector = intent.getBooleanExtra(keyIsSingleSelector, true);
		clazz = forName(className);
		File file = new File(rootPath);
		if (!file.isDirectory()) {
			Intent data = new Intent(context, clazz);
			setResult(RESULT_OK, data);
			finish();
		}
		adapter.setIsSingleSelector(isSingleSelector);
		initView();
		loadJsonData(rootPath);
	}

	private void initView() {
		findViewById(R.id.iv_back).setOnClickListener(new MyOnClickListener(-1));
		findViewById(R.id.sbt).setOnClickListener(new MyOnClickListener(-1));//“确定”按钮
		TextView textView = (TextView) findViewById(R.id.tv_root_path);//SD卡：显示父路径
		textView.setTag(rootPath);
		listTvPath.add(textView);
		layoutPath = (LinearLayout) findViewById(R.id.layout_path);
		//根目录sd卡的点击事件
		findViewById(R.id.tv_root_path).setOnClickListener(new MyOnClickListener(0));
		ListView listView = (ListView) findViewById(R.id.lv);
		listView.setAdapter(adapter);
	}

	private final class MyOnClickListener implements View.OnClickListener {
		private int position;
		public MyOnClickListener(int position) {
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			if (R.id.iv_back == v.getId()) {
				finish();
				return;
			} else if (R.id.sbt == v.getId()) {
				setResult();
				return;
			}
			if ((listTvPath == null) || (listTvPath.size() == 0)) {
				return;
			}
			String filePath = (String) listTvPath.get(position).getTag();
			for (int i = listTvPath.size() - 1; (i >= 0) && (i >= position); i--) {
				layoutPath.removeView(listTvPath.get(i));
				listTvPath.remove(i);
			}
			if (position == 0) {
				TextView textView = (TextView) findViewById(R.id.tv_root_path);
				textView.setTag(rootPath);
				listTvPath.add(textView);
			}
			loadJsonData(filePath);
		}
	}

	public void loadJsonData(String filePath) {
		File mainFile = new File(filePath);
		if (!rootPath.equalsIgnoreCase(filePath)) {
			hasClicked = true;
			TextView tvPath = new TextView(context);
			tvPath.setTag(mainFile.getAbsolutePath());
			tvPath.setGravity(Gravity.CENTER);
			tvPath.setPadding(dpToPx(context, 16F), 0, dpToPx(context, 16F), 0);
			tvPath.setText(mainFile.getName());
			tvPath.setBackgroundResource(R.drawable.bg9_path_arrow);
			listTvPath.add(tvPath);
			layoutPath.addView(tvPath);
			//文件路径的点击事件
			tvPath.setOnClickListener(new MyOnClickListener(listTvPath.size() - 1));
		}
		File[] files = mainFile.listFiles();
		List<FileBean> listFileBean = new ArrayList<FileBean>();
		for (int i = 0; (files != null) && (i < files.length); i++) {
			File subFile = files[i];
			FileBean bean = new FileBean();
			bean.isSelected = false;//默认没有选中
			if (subFile.isHidden()) {
				continue;
			}
			bean.path = subFile.getPath();
			String fileName = subFile.getName();
			String fileSize = MyUtils.getFileSize(subFile.length());//文件大小
			bean.fsize=fileSize;
			if (subFile.isDirectory()) {//判断是否是父目录（是否是文件夹）
				if (isSelectFile) {
					bean.isShowEditor = false;//展示 选中框
				} else {
					bean.isShowEditor = true;
				}
				bean.resId = R.drawable.ic_folder;
				if ("DCIM".equalsIgnoreCase(fileName)
						|| "Pictures".equalsIgnoreCase(fileName)
						|| "Picture".equalsIgnoreCase(fileName)
						|| "Camera".equalsIgnoreCase(fileName)
						|| "photo".equalsIgnoreCase(fileName)
						|| "screenshots".equalsIgnoreCase(fileName)
						|| "Screenshot".equalsIgnoreCase(fileName)) {
					bean.resId = R.drawable.ic_folder_picture;
				} else if ("movie".equalsIgnoreCase(fileName)
						|| "movies".equalsIgnoreCase(fileName)
						|| "video".equalsIgnoreCase(fileName)
						|| "videos".equalsIgnoreCase(fileName)) {
					bean.resId = R.drawable.ic_folder_video;
				} else if ("audio".equalsIgnoreCase(fileName)
						|| "audios".equalsIgnoreCase(fileName)
						|| "music".equalsIgnoreCase(fileName)
						|| "musics".equalsIgnoreCase(fileName)) {
					bean.resId = R.drawable.ic_folder_music;
				}
			} else if (subFile.isFile()) {//（是否是文件）
				if (isSelectFile) {
					bean.isShowEditor = true;
				} else {
					bean.isShowEditor = false;
				}
				String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
				Log.e("----文件后缀名","fileName = "+fileName+" suffix = "+suffix);
				if ("log".equalsIgnoreCase(suffix)
						|| "java".equalsIgnoreCase(suffix)
						|| "txt".equalsIgnoreCase(suffix)
						|| "text".equalsIgnoreCase(suffix)
						|| "json".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_text;
				} else if ("doc".equalsIgnoreCase(suffix)
						|| "docx".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_doc;
				} else if ("ppt".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_ppt;
				} else if ("xls".equalsIgnoreCase(suffix)
						| "xlsx".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_xls;
				} else if ("aac".equalsIgnoreCase(suffix)
						|| "flac".equalsIgnoreCase(suffix)
						|| "wav".equalsIgnoreCase(suffix)
						|| "ape".equalsIgnoreCase(suffix)
						|| "wma".equalsIgnoreCase(suffix)
						|| "ogg".equalsIgnoreCase(suffix)
						|| "mp3".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_music;
				} else if ("pdf".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_pdf;
				} else if ("html".equalsIgnoreCase(suffix)
						|| "htm".equalsIgnoreCase(suffix)
						|| "xml".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_html;
				} else if ("f4v".equalsIgnoreCase(suffix)
						|| "3gp".equalsIgnoreCase(suffix)
						|| "mkv".equalsIgnoreCase(suffix)
						|| "wmv".equalsIgnoreCase(suffix)
						|| "rm".equalsIgnoreCase(suffix)
						|| "avi".equalsIgnoreCase(suffix)
						|| "mp4".equalsIgnoreCase(suffix)
						|| "rmvb".equalsIgnoreCase(suffix)
						|| "flv".equalsIgnoreCase(suffix)) {
					bean.resId = R.drawable.ic_file_movie;
				} else if ("jpg".equalsIgnoreCase(suffix)
						|| "jpeg".equalsIgnoreCase(suffix)
						|| "png".equalsIgnoreCase(suffix)
						|| "gif".equalsIgnoreCase(suffix)
						|| "bmp".equalsIgnoreCase(suffix)) {
					bean.isImage = true;
				} else {
					bean.resId = R.drawable.ic_file_unknown;
				}
			}
			bean.name = subFile.getName();
			listFileBean.add(bean);
		}
		adapter.refreshItem(listFileBean);
		 ((ListView) findViewById(R.id.lv)).setSelection(0);
	}

	public void setResult() {
		Intent data = new Intent(context, clazz);
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> namelist = new ArrayList<String>();
		ArrayList<FileBean> fileList = new ArrayList<>();
		ArrayList<FileBean> fileBeans = (ArrayList<FileBean>) adapter.getList();
		for (int i = 0; (fileBeans != null) && (i < fileBeans.size()); i++) {
			FileBean bean = fileBeans.get(i);
			if (bean.isSelected) {
				list.add(fileBeans.get(i).path);
				namelist.add(fileBeans.get(i).name);
				String name = fileBeans.get(i).name;
				int lastIndexOf = name.lastIndexOf(".") + 1;
				String fileType = name.substring(lastIndexOf, name.length());
				
				bean.setWanCheng(false);
				bean.setTp1(fileType);//设置文件类型
				bean.setTp3(2);////1文件夹；2文件
				bean.setFileNm("id"+SPUtils.getID()+"￥"+bean.getName());//文件名要拼接："id用户id_"(如：id181_)
				if("png".equals(fileType) || "jpg".equals(fileType)//类型：图片格式
						|| "bmp".equals(fileType) || "jpeg".equals(fileType)
						|| "gif".equals(fileType)){
					bean.setImageType("2");//图片类型1：云盘，2：本地
				}
				//还要设置文件大小
				fileList.add(bean);
			}
		}
//		data.putStringArrayListExtra(keyFilePaths, list);//传文件路径
//		data.putStringArrayListExtra(keyFileName, namelist);//传文件名
		data.putParcelableArrayListExtra("keyFilePaths", fileList);
		setResult(RESULT_OK, data);
		finish();
	}

	private Class<?> forName(String className) {
		Class clazz;
		try {
			clazz = Class.forName(className);
			return clazz;
		} catch (ClassNotFoundException e) {
		}
		return null;
	}

	/**
	 * 数据转换: dp---->px
	 */
	private int dpToPx(Context context, float dp) {
		if (context == null) {
			return -1;
		}
		return (int) (dp * context.getResources().getDisplayMetrics().density);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((listTvPath != null) && (listTvPath.size() >= 2)) {
				TextView lastTextView = listTvPath.get(listTvPath.size() - 1);
				TextView gotoTextView = listTvPath.get(listTvPath.size() - 2);
				String gotoFilePath = (String) gotoTextView.getTag();
				layoutPath.removeView(lastTextView);
				layoutPath.removeView(gotoTextView);
				listTvPath.remove(listTvPath.size() - 1);
				listTvPath.remove(listTvPath.size() - 1);
				loadJsonData(gotoFilePath);
				return true;
			}
			if ((listTvPath != null) && (listTvPath.size() >= 1) && hasClicked) {
				TextView lastTextView = listTvPath.get(listTvPath.size() - 1);
				layoutPath.removeView(lastTextView);
				listTvPath.remove(listTvPath.size() - 1);
				loadJsonData(rootPath);
				return true;
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		
	}
}
