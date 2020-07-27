package com.qwb.view.file.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qwb.view.file.model.FileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.view.wangpan.model.QueryYunFileBean;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.photo.ImagePagerActivity;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.chiyong.t3.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import okhttp3.Call;

/**
 * 云盘：聊天和审批--选择文件
 */
public class ChooseFileActivity extends BaseNoTitleActivity {

	private final class YunPanAdapter extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView,
                            ViewGroup parent) {
			View rootview = getLayoutInflater().inflate(R.layout.x_adapter_wanpan_choose_file, null);
			final CheckBox cb_choose = MyUtils.getViewFromVH(rootview,R.id.cb_choose);
			TextView tv_name = MyUtils.getViewFromVH(rootview, R.id.tv_title);
			final ImageView file_ico = MyUtils.getViewFromVH(rootview,R.id.file_ico);

			final FileBean fileBean = fileList.get(position);
			String path = fileBean.getPath();
			String fileNm = fileBean.getFileNm();
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
			// 类型：图片格式
			if ("png".equals(fileType) || "jpg".equals(fileType)
					|| "bmp".equals(fileType) || "jpeg".equals(fileType)
					|| "gif".equals(fileType)) {
				//1:七牛2：本地
				if ("2".equals(fileBean.getImageType())) {
					MyGlideUtil.getInstance().displayImageSquere("file://" + path, file_ico);
				} else {
					MyGlideUtil.getInstance().displayImageSquere(Constans.ROOT_imgUrl + fileNm,file_ico);
				}
			}
			// tp3;//1文件夹；2文件
			if (fileBean.getTp3() == 1) {
				cb_choose.setVisibility(View.INVISIBLE);
			} else if (fileBean.getTp3() == 2) {
				cb_choose.setVisibility(View.VISIBLE);
			}

			Boolean isChecked = trueMap.get(fileBean.getId());
			if (isChecked == null) {
				cb_choose.setChecked(false);
			} else {
				cb_choose.setChecked(isChecked);
			}

			cb_choose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean checked = cb_choose.isChecked();
					if(isSingle){//单选
						if (checked) {// 当选中时，遍历map去掉之前选中的
							Iterator<Entry<Integer, Boolean>> it = trueMap.entrySet().iterator();
							while (it.hasNext()) {
								Map.Entry<Integer, Boolean> entry = it.next();
								Integer key = entry.getKey();
								Boolean value = entry.getValue();
								if (value) {
									trueMap.put(key, false);
								}
							}
						}
					}else{//多选
					}
					trueMap.put(fileBean.getId(), checked);
					notifyDataSetChanged();
				}
			});
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

	private int pageNo = 1;
	private boolean isRefresh = true;
	private PullToRefreshListView lv_pull;
	private YunPanAdapter yunPanAdapter;
	private ArrayList<FileBean> fileList = new ArrayList<>();
	private ArrayList<FileBean> fileList_shang = new ArrayList<>();// 记录上次的fileBean
	public HashMap<Integer, Boolean> trueMap = new HashMap<Integer, Boolean>();
	public HashMap<Integer, FileBean> FileBeanMap = new HashMap<Integer, FileBean>();
	private TextView tv_headTitle;
	private FileBean fileBean_chuan = new FileBean();
	private boolean isSingle;
	private LinearLayout ll_parent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_choose_file);
		Intent intent = getIntent();
		if(intent!=null){
			isSingle = intent.getBooleanExtra("isSingle", true);
		}
		initUI();
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		findViewById(R.id.tv_head_right).setOnClickListener(this);
		tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
		tv_headTitle.setText("从云盘获取文件");
		tv_headRight.setText("发送");
		// img_head_right.setImageResource(R.drawable.icon_add_yunpan);
		tv_headRight.setVisibility(View.VISIBLE);
		img_head_right.setVisibility(View.GONE);
	}

	private void initUI() {
		initHead();
		//"公司共享"和“我的文件”两个布局
		ll_parent = (LinearLayout) findViewById(R.id.parent3);
		TextView tv_gongsigongxiang = (TextView) findViewById(R.id.tv_gongsigongxiang);
		TextView tv_minefile = (TextView) findViewById(R.id.tv_minefile);
		tv_gongsigongxiang.setOnClickListener(this);
		tv_minefile.setOnClickListener(this);

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
				initData_queryYFile(null, "" + fileBean_chuan.getTp2(), ""+ fileBean_chuan.getId());
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isRefresh = false;
				pageNo++;
				initData_queryYFile(null, "" + fileBean_chuan.getTp2(), ""+ fileBean_chuan.getId());
			}
		});

		lv_pull.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
				FileBean fileBean = fileList.get(position - 1);
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
						Intent intent = new Intent(ChooseFileActivity.this,ImagePagerActivity.class);
						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,urls);
						intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,position);
						startActivity(intent);
					} else {
						Intent intent = new Intent(ChooseFileActivity.this,FileDetailActivity.class);
						intent.putExtra("fileBean", fileBean);
						startActivity(intent);
					}
				} else if (1 == fileBean.getTp3()) {// 是文件夹
					isRefresh = true;
					fileList_shang.add(fileBean_chuan);
					fileBean_chuan = fileBean;// 记录当前的fileBean_chuan；
					tv_headTitle.setText(fileBean.getFileNm());
					Log.e("fileBean.getTp2---", "" + fileBean.getTp2());
					Log.e("fileBean.getId---", "" + fileBean.getId());
					initData_queryYFile(null, "" + fileBean.getTp2(), ""+ fileBean.getId());
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
		case R.id.tv_head_right:
			ArrayList<FileBean> fileBean_faSong = new ArrayList<>();
			FileBean fileBean=null;
			Iterator<Entry<Integer, Boolean>> it = trueMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, Boolean> entry = it.next();
				Integer key = entry.getKey();
				Boolean value = entry.getValue();
				if (value) {
					if(isSingle){//单选：聊天
						fileBean = FileBeanMap.get(key);
						break;
					}else{//多选：审批
						fileBean = FileBeanMap.get(key);
						fileBean_faSong.add(fileBean);
					}
				}
			}
			
			Intent data = new Intent();
			if(isSingle){//单选：聊天
				data.putExtra("fileBean", fileBean);
			}else{//多选
				if(fileBean_faSong!=null && fileBean_faSong.size()>0){
					data.putExtra("fileBean", fileBean_faSong);
				}
			}
			setResult(0, data);
			finish();
			break;
		case R.id.tv_gongsigongxiang://公司共享
			lv_pull.setVisibility(View.VISIBLE);
			ll_parent.setVisibility(View.GONE);
			tv_headTitle.setText("公司共享");
			fileBean_chuan.setTp2(2);
			isRefresh = true;
			initData_queryYFile(null, String.valueOf(2), null);
			break;
		case R.id.tv_minefile://我的文件
			lv_pull.setVisibility(View.VISIBLE);
			ll_parent.setVisibility(View.GONE);
			tv_headTitle.setText("我的文件");
			isRefresh = true;
			fileBean_chuan.setTp2(1);
			initData_queryYFile(null, String.valueOf(1), null);
			break;
		}
	}

	// 查询文件
	private void initData_queryYFile(String fileNm, String tp2, String pid) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		if (!MyUtils.isEmptyString(fileNm)) {
			params.put("fileNm", fileNm);
		}
		if (!MyUtils.isEmptyString(pid)) {
			params.put("pid", pid);
		}
		params.put("tp2", tp2);// 文件位置类型（1自己；2公司）
		params.put("pageNo", "" + pageNo);// 传-文件名
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.queryYfileWeb)
				.id(1)
				.build()
				.execute(new MyStringCallback(),this);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 按返回键是判断上级id不是0
			if (fileList_shang != null && fileList_shang.size() >= 1) {
				isRefresh = true;
				FileBean fileBean = fileList_shang.get(fileList_shang.size() - 1);
				fileBean_chuan = fileBean;
				fileList_shang.remove(fileList_shang.size() - 1);
				tv_headTitle.setText(fileBean.getFileNm());
				initData_queryYFile(null, "" + fileBean.getTp2(),"" + fileBean.getId());
				if(fileList_shang.size()==0){//"公司共享"和"我的文件"
					//tp2：文件位置类型（1自己；2公司）
					if(1==fileBean_chuan.getTp2()){
						tv_headTitle.setText("我的文件");
					}else if(2==fileBean_chuan.getTp2()){
						tv_headTitle.setText("公司共享");
					}
				}
				return true;
			} else {
				if(ll_parent.getVisibility()== View.VISIBLE){
					finish();
				}else{
					tv_headTitle.setText("从云盘获取文件");
					lv_pull.setVisibility(View.GONE);
					ll_parent.setVisibility(View.VISIBLE);
				}
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
								trueMap.clear();
							}

							List<FileBean> rows = parseObject.getRows();
							if (rows != null) {
								for (int i = 0; i < rows.size(); i++) {
									FileBean fileBean = rows.get(i);
									// tp3;//1文件夹；2文件
									if (fileBean.getTp3() == 2) {
										trueMap.put(fileBean.getId(), false);
										FileBeanMap.put(fileBean.getId(), fileBean);
									}
								}
								fileList.addAll(rows);
								yunPanAdapter.notifyDataSetChanged();
							}
						} else {
							ToastUtils.showCustomToast(parseObject.getMsg());
						}
					}
					break;
			}
		}
	}
}
