package com.qwb.view.wangpan;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.UrlSafeBase64;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.wangpan.model.QueryYunFileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.widget.RoundProgressBar;
import com.qwb.view.file.ui.FileDetailActivity;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class Wangpan2Activity extends BaseNoTitleActivity  {

	private final class YunPanAdapter extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView,
                            ViewGroup parent) {
			View rootview = getLayoutInflater().inflate(R.layout.x_adapter_yunpan, null);

			TextView tv_name= MyUtils.getViewFromVH(rootview, R.id.tv_title);
			final ImageView file_ico= MyUtils.getViewFromVH(rootview, R.id.file_ico);
			final ImageView img_more= MyUtils.getViewFromVH(rootview, R.id.img_more);
			final RoundProgressBar roundProgressBar= MyUtils.getViewFromVH(rootview, R.id.roundProgressBar);

			final FileBean fileBean = fileList.get(position);
			String path = fileBean.getPath();
			final String fileNm = fileBean.getFileNm();
			String fileType = fileBean.getTp1();
			
			//tp3:1文件夹；2文件(显示文件名时：要去掉前面拼接的“id(用户id)_”)
			int indexOf = fileNm.indexOf("￥");
			final String name=fileNm.substring(indexOf+1, fileNm.length());//去掉"id用户id_"(用户手机系统自带的名称)
			if(1==fileBean.getTp3()){
				tv_name.setText(fileNm);
			}else if(2==fileBean.getTp3()){
				tv_name.setText(name);
			}
			
			displayImageview(file_ico, fileType);// 根据文件类型设置图片
			if ("png".equals(fileType)
					|| "jpg".equals(fileType)// 类型：图片格式
					|| "bmp".equals(fileType) || "jpeg".equals(fileType)
					|| "gif".equals(fileType)) {

				if ("2".equals(fileBean.getImageType())) {
					MyGlideUtil.getInstance().displayImageRound("file://" + path, file_ico);
				} else {
					MyGlideUtil.getInstance().displayImageRound(Constans.ROOT_imgUrl + fileNm, file_ico);
				}
			}

			img_more.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//tp3;//1文件夹；2文件
					//文件夹没有“移动”功能
					if(fileBean.getTp3()==1){
						ll_moveFile.setVisibility(View.GONE);
						tv_fileName.setText(fileNm);
					}else if(fileBean.getTp3()==2){
						ll_moveFile.setVisibility(View.VISIBLE);
						tv_fileName.setText(name);//去掉"id用户id_"(用户手机系统自带的名称)
					}
					positions = position;
					popWin2.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
					backgroundAlpha(0.5f);
				}
			});

			// 上传
//			if (!fileBean.isWanCheng) {// 是否上传完成
			if (fileBeans.contains(fileBean)) {// 是否上传完成
				if (!MyUtils.isEmptyString(path) && !MyUtils.isEmptyString(token)) {
					uploadManager.put(path, fileNm, token,
							new UpCompletionHandler() {
								@Override
								public void complete(String key, ResponseInfo info, JSONObject res) {
									Log.e("complete----------", ""+info.toString());
//									if (info.isOK() == true) {
									if (info.statusCode == 200) {
										sum=sum+1;//记录上传成功个数
										if(sum==fileBeans.size()){//判断最后一个上传成功
											Toast.makeText(getApplication(),"完成上传", Toast.LENGTH_SHORT).show();
											if(fileBeans.size()>1){//上传多个
												StringBuffer sb=new StringBuffer();
												for (int i = 0; i < fileBeans.size(); i++) {
													FileBean fileBean2 = fileBeans.get(i);
													if(i==fileBeans.size()-1){//最后一个不加“,”
														sb.append(fileBean2.getFileNm());
													}else{
														sb.append(fileBean2.getFileNm()+",");
													}
												}
												initData_addYfile(sb.toString().trim(),"1", null);
											}else{//只上传一个
												initData_addYfile(fileBean.getFileNm(),"1", null);
											}
										}
									} else {
										Toast.makeText(getApplication(),"上传失败", Toast.LENGTH_SHORT).show();
									}
								}
							}, new UploadOptions(null, null, false,
									new UpProgressHandler() {
										public void progress(String key, double percent) {
											roundProgressBar.setVisibility(View.VISIBLE);
											img_more.setVisibility(View.GONE);
											int percent1 = (int) (percent * 100);
											roundProgressBar.setProgress(percent1);
											if (percent1 == 100) {
												roundProgressBar.setVisibility(View.GONE);
												img_more.setVisibility(View.VISIBLE);
												fileBean.setWanCheng(true);
											}
										}
									}, null));
				}
			}else{
				img_more.setVisibility(View.VISIBLE);
			}
			return rootview;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return fileList.size();
		}
	}

	private UploadManager uploadManager = null;
	private ArrayList<FileBean> fileBeans = new ArrayList<>();
	private ArrayList<FileBean> fileList = new ArrayList<>();
	private String token;
	private YunPanAdapter yunPanAdapter;
	private int positions;
	private int pageNo = 1;
	private boolean isRefresh = true;
	private int sum=0;//记录上传成功个数


	public Wangpan2Activity() {
		initUploadManager();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_wangpan2);
		initPopup();
		initPopup2();
		initPopup3();
		initUI();
		initData_token();// 获取token
		initData_queryYFile(null, String.valueOf(1), null);// 查询文件
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		// findViewById(R.id.tv_head_right).setOnClickListener(this);
		findViewById(R.id.img_head_right).setOnClickListener(this);
		tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
		tv_headTitle.setText("云盘");
		// tv_headRight.setText("");
		img_head_right.setImageResource(R.drawable.icon_add_yunpan);
		tv_headRight.setVisibility(View.GONE);
		img_head_right.setVisibility(View.VISIBLE);
	}

	private void initUI() {
		initHead();
		ll_gongsigongxiang = (LinearLayout) findViewById(R.id.ll_gongsigongxiang);
		findViewById(R.id.ll_gongsigongxiang).setOnClickListener(this);//公司共享
		findViewById(R.id.ll_hideSpace).setOnClickListener(this);//隐藏空间
		
		lv_pull = (PullToRefreshListView) findViewById(R.id.listView);
		yunPanAdapter = new YunPanAdapter();
		lv_pull.setAdapter(yunPanAdapter);
		lv_pull.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				lv_pull.setMode(Mode.BOTH);
				isRefresh = true;
				pageNo = 1;
				initData_queryYFile(null, "1", null);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isRefresh = false;
				pageNo++;
				initData_queryYFile(null, "1", null);
			}
		});
		
		
		lv_pull.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				FileBean fileBean = fileList.get(position-1);
				if (fileBean == null) {
					return;
				}
				// 1文件夹；2文件
				if (2 == fileBean.getTp3()) {// 是文件
					String fileType = fileBean.getTp1();
					// 1：如果是图片类型（点击放大）2：跳到“文件详情”界面
					if ("png".equals(fileType)
							|| "jpg".equals(fileType)// 类型：图片格式
							|| "bmp".equals(fileType)
							|| "jpeg".equals(fileType)
							|| "gif".equals(fileType)) {
						// 点击放大图片
						String[] urls = new String[1];
						if ("2".equals(fileBean.getImageType())) {// 图片类型1：云盘，2：本地
							urls[0] = "file://" + fileBean.getPath();
						} else {
							urls[0] = Constans.ROOT_imgUrl+ fileBean.getFileNm();
						}
						Intent intent = new Intent(Wangpan2Activity.this,
								ImagePagerActivity.class);
						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,urls);
						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,position);
						startActivity(intent);
					} else {
						Intent intent = new Intent(Wangpan2Activity.this,FileDetailActivity.class);
						intent.putExtra("fileBean", fileBean);
						startActivity(intent);
					}
				} else if (1 == fileBean.getTp3()) {// 是文件夹
					Intent intent = new Intent(Wangpan2Activity.this,NextFileActivity.class);
					intent.putExtra("fileBean", fileBean);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.img_head_right:
			popWin.showAsDropDown(findViewById(R.id.img_head_right), 0, -20);
			backgroundAlpha(0.5f);
			break;
		case R.id.rl_file:
			popWin.dismiss();
			Intent intent = new Intent(Wangpan2Activity.this,FileSelectorActivity.class);
			intent.putExtra(FileSelectorActivity.keyClassName,Wangpan2Activity.class.getName());
			intent.putExtra(FileSelectorActivity.keyIsSelectFile, true);
			intent.putExtra(FileSelectorActivity.keyIsSingleSelector, false);// true:单文件
			startActivityForResult(intent, FileSelectorActivity.requestCodeSingleFile);
			break;
			//删除
		case R.id.tv_deleteFile:
			popWin2.dismiss();
			initData_deleteBucke();
			break;
			//新建文件夹
		case R.id.rl_creatFile:
			popwin_type = 1;
			tv_creatFile.setText("新建文件夹");
			edit_newFile.setText("");
			popWin.dismiss();
			popWin3.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0,0);
			backgroundAlpha(0.5f);
			break;
			//"新建文件夹"，“重命名”---“确定”按钮
		case R.id.tv_queding:
			popWin3.dismiss();
			String name = edit_newFile.getText().toString().trim();
			if(MyUtils.isEmptyString(name)){
				ToastUtils.showCustomToast("文件夹名不能为空");
				return;
			}
			if(1==popwin_type){//新建文件夹
				initData_addYfile(name, String.valueOf("1"), null);
			}else if(2==popwin_type){//重命名
				FileBean fileBean = fileList.get(positions);
				//1文件夹；2文件
				int tp3 = fileBean.getTp3();
				if (1 == tp3) {// 1:文件夹不加后缀
					initData_moveBucke(String.valueOf(fileBean.getId()),fileBean.getFileNm(),name);
				} else if (2 == tp3) {// 2:文件加后缀
					name="id"+SPUtils.getID()+"￥"+ name + "." + fileBean.getTp1();//前面拼接“id(用户id)_”
					initData_moveBucke(String.valueOf(fileBean.getId()),fileBean.getFileNm(),name);// 新的key要拼接后缀
				}
			}
			break;
			//公司共享
		case R.id.ll_gongsigongxiang:
			FileBean fileBean = new FileBean();
			fileBean.setFileNm("公司共享");
			fileBean.setTp2(2);
			Intent intent2 = new Intent(Wangpan2Activity.this,NextFileActivity.class);
			intent2.putExtra("fileBean", fileBean);
			startActivity(intent2);
			break;
		case R.id.ll_hideSpace://隐藏空间
			FileBean fileBean5 = new FileBean();
			fileBean5.setFileNm("隐藏空间");
			fileBean5.setTp2(3);
			Intent intent5 = new Intent(Wangpan2Activity.this,HideSpaceActivity.class);
			intent5.putExtra("fileBean", fileBean5);
			startActivity(intent5);
			break;
			//重命名文件/文件夹
		case R.id.tv_updatefileNm:
			popwin_type=2;
			tv_creatFile.setText("重命名");
			popWin2.dismiss();
			popWin3.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0,0);
			backgroundAlpha(0.5f);
			//去掉后缀名
			FileBean fileBean2 = fileList.get(positions);
			String fileNm = fileBean2.fileNm;
			if(fileBean2.tp3==1){//文件夹
				
			}else if(fileBean2.tp3==2){//文件
				int indexOf = fileNm.indexOf("￥");
				int lastIndexOf = fileNm.lastIndexOf(".");
				fileNm=fileNm.substring(indexOf+1, lastIndexOf);
			}
			edit_newFile.setText(fileNm);
			break;
		case R.id.tv_moveFile://移动文件
			popWin2.dismiss();
			Intent intent3 = new Intent(Wangpan2Activity.this,MoveFileActivity.class);
			intent3.putExtra("position", positions);//移动成功后，原来列表要移除那行
			intent3.putExtra("fileBean", fileList.get(positions));
			startActivityForResult(intent3, Constans.requestcode_movefile);
			break;
		case R.id.ll_zhuanfa://转发文件
			popWin2.dismiss();
			Intent intent4 = new Intent(Wangpan2Activity.this,ZhuanFaActivity.class);
			intent4.putExtra("isYunpan", true);
			intent4.putExtra("fileBean", fileList.get(positions));
			startActivity(intent4);
			break;
		}
	}

	/*
	 * 窗体
	 */
	private PopupWindow popWin = null;
	private View contentView = null;
	private void initPopup() {
		contentView = getLayoutInflater().inflate(R.layout.x_popup_yunpan, null);
		contentView.findViewById(R.id.rl_file).setOnClickListener(this);// 上传文件
		contentView.findViewById(R.id.rl_creatFile).setOnClickListener(this);// 新建文件夹
		popWin = new PopupWindow(contentView,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWin.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin.setBackgroundDrawable(new BitmapDrawable());
		popWin.setOnDismissListener(new poponDismissListener());
	}

	private PopupWindow popWin2 = null;
	private View contentView2 = null;
	private TextView tv_fileName = null;
	private LinearLayout ll_moveFile = null;//点击“更多”：文件没有"移动"
	private void initPopup2() {
		contentView2 = getLayoutInflater().inflate(R.layout.x_popup_yunpan_more,null);
		tv_fileName = (TextView) contentView2.findViewById(R.id.tv_fileName);
		ll_moveFile = (LinearLayout) contentView2.findViewById(R.id.ll_moveFile);
		contentView2.findViewById(R.id.tv_deleteFile).setOnClickListener(this);// 删除文件
		contentView2.findViewById(R.id.tv_updatefileNm).setOnClickListener(this);//重命名
		contentView2.findViewById(R.id.tv_moveFile).setOnClickListener(this);//移动文件
		contentView2.findViewById(R.id.ll_zhuanfa).setOnClickListener(this);//转发文件
		popWin2 = new PopupWindow(contentView2,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWin2.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin2.setBackgroundDrawable(new BitmapDrawable());
		popWin2.setOnDismissListener(new poponDismissListener());
	}

	private PopupWindow popWin3 = null;
	private View contentView3 = null;
	private EditText edit_newFile;
	private TextView tv_creatFile;
	private LinearLayout ll_gongsigongxiang;
	private TextView tv_headTitle;
	private int popwin_type=1;//区分"新建文件夹”，“修改文件名”
	private PullToRefreshListView lv_pull;

	private void initPopup3() {
		contentView3 = getLayoutInflater().inflate(R.layout.x_popup_yunpan_new_file, null);
		tv_creatFile = (TextView) contentView3.findViewById(R.id.tv_creatFile);
		edit_newFile = (EditText) contentView3.findViewById(R.id.edit_newFile);
		contentView3.findViewById(R.id.tv_queding).setOnClickListener(this);// 删除文件
		popWin3 = new PopupWindow(contentView3,
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		popWin3.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popWin3.setBackgroundDrawable(new BitmapDrawable());
		popWin3.setOnDismissListener(new poponDismissListener());
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

	// 获取token
	private void initData_token() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.getToken)
				.id(1)
				.build()
				.execute(new MyStringCallback(),null);
	}

	// 重命名文件/文件夹
	private void initData_moveBucke(String id, String key, String newKey) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("id", id);//文件id
		params.put("key", key);// 传-文件名
		params.put("newKey", newKey);// 传-文件名
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.moveBucke)
				.id(2)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 删除文件
	private void initData_deleteBucke() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("key", fileList.get(positions).getFileNm());// 传-文件名
		params.put("dataTp", SPUtils.getSValues(SPUtils.getID()+"dataTp2_"+3));//角色
		String dataTP = SPUtils.getSValues(SPUtils.getID()+"dataTp2_"+3);
		if("4".equals(dataTP)){
			params.put("mids", MyUtils.getMids(1, "yp"));//角色
		}
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.deleteBucke)
				.id(3)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 查询文件
	private void initData_queryYFile(String fileNm, String tp2, String pid) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		if(!MyUtils.isEmptyString(fileNm)){
			params.put("fileNm", fileNm);
		}
		if(!MyUtils.isEmptyString(pid)){
			params.put("pid", pid);
		}
		params.put("tp2", tp2);// 文件位置类型（1自己；2公司）
		params.put("pageNo", "" + pageNo);// 传-文件名
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.queryYfileWeb)
				.id(4)
				.build()
				.execute(new MyStringCallback(),this);
	}

	// 新建文件夹
	private void initData_addYfile(String name, String tp2, String pid) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("fileNm", name);// 文件名
		params.put("tp2", tp2);// 文件位置类型（1自己；2公司）
		if (!MyUtils.isEmptyString(pid)) {// 上级id
			params.put("pid", pid);
		}
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.addYfileWeb)
				.id(5)
				.build()
				.execute(new MyStringCallback(),null);
	}


	// 初始化“上传管理器”
	private void initUploadManager() {
		// 断点上传
		String dirPath = "";// <断点记录文件保存的文件夹位置>
		Recorder recorder = null;
		try {
			File f = File.createTempFile("qiniu_xxxx", ".tmp");
			dirPath = f.getParent();// 文件父目录
			recorder = new FileRecorder(dirPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final String dirPath1 = dirPath;
		// 默认使用 key 的url_safe_base64编码字符串作为断点记录文件的文件名。
		// 避免记录文件冲突（特别是key指定为null时），也可自定义文件名(下方为默认实现)：
		KeyGenerator keyGen = new KeyGenerator() {
			public String gen(String key, File file) {
				// 不必使用url_safe_base64转换，uploadManager内部会处理
				// 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
				String path = key + "_._"
						+ new StringBuffer(file.getAbsolutePath()).reverse();
				Log.d("qiniu", path);
				File f = new File(dirPath1, UrlSafeBase64.encodeToString(path));
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new FileReader(f));
					String tempString = null;
					int line = 1;
					try {
						while ((tempString = reader.readLine()) != null) {
							Log.e("qiniu", "line " + line + ": " + tempString);
							line++;
						}

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							reader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return path;
			}
		};

		// recorder 分片上传时，已上传片记录器
		// keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
		Configuration config = new Configuration.Builder().recorder(recorder,
				keyGen).build();
		// 实例化一个上传的实例
		uploadManager = new UploadManager(config);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (data == null || data.getExtras() == null) {
			return;
		}
		if (requestCode == FileSelectorActivity.requestCodeSingleFile) {
			fileBeans.clear();
			fileBeans = data.getParcelableArrayListExtra("keyFilePaths");
			fileList.addAll(fileBeans);
			yunPanAdapter.notifyDataSetChanged();
//			Log.e("fileBeans----大小", ""+fileBeans.toString());
		}
		if(requestCode == Constans.requestcode_movefile){
			FileBean fileBean_yuan = data.getParcelableExtra("fileBean");
			fileList.remove(fileBean_yuan);
			yunPanAdapter.notifyDataSetChanged();
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
			}
			else {
				file_ico.setImageResource(R.drawable.file_other);
			}
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
			if (lv_pull != null) {
				lv_pull.onRefreshComplete();
			}
			switch (tag) {
				case 1:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							token = jsonObject.getString("token");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				//重命名文件
				case 2:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							if(jsonObject.getBoolean("state")){
								initData_queryYFile(null, String.valueOf(1), null);// 查询文件
							}
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				// 删除文件
				case 3:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							if(jsonObject.getBoolean("state")){
								fileList.remove(positions);
								yunPanAdapter.notifyDataSetChanged();
							}
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;

				case 4://查询文件
					QueryYunFileBean parseObject = JSON.parseObject(json,
							QueryYunFileBean.class);
					if (parseObject != null) {
						if (parseObject.isState()) {
							if (pageNo >= parseObject.getTotalPage()) {
								lv_pull.setMode(Mode.PULL_FROM_START);
							} else {
								lv_pull.setMode(Mode.BOTH);
							}
							if (isRefresh) {
								fileList.clear();
							}

							List<FileBean> rows = parseObject.getRows();
							if (rows != null) {
								fileList.addAll(rows);
								yunPanAdapter.notifyDataSetChanged();
							}
						} else {
							ToastUtils.showCustomToast(parseObject.getMsg());
						}
					}
					break;

				//新建文件夹
				case 5:
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							if(jsonObject.getBoolean("state")){
								initData_queryYFile(null, String.valueOf(1), null);// 查询文件
							}
							ToastUtils.showCustomToast(jsonObject.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
			}
		}
	}
}
