package com.qwb.view.wangpan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.qwb.view.wangpan.model.QueryYunFileBean;
import com.qwb.view.base.ui.BaseNoTitleActivity;
import com.qwb.widget.RoundProgressBar;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 云盘模块--移动文件
 *
 */
public class MoveFileActivity extends BaseNoTitleActivity {

	private final class YunPanAdapter extends BaseAdapter {
		@Override
		public View getView(final int position, View convertView,
                            ViewGroup parent) {
			
			View rootview = getLayoutInflater().inflate(R.layout.x_adapter_yunpan, null);
			LinearLayout ll_bg = MyUtils.getViewFromVH(rootview, R.id.ll_bg);
			TextView tv_name = MyUtils.getViewFromVH(rootview, R.id.tv_title);
			final ImageView file_ico = MyUtils.getViewFromVH(rootview,R.id.file_ico);
			final ImageView img_more = MyUtils.getViewFromVH(rootview,R.id.img_more);
			final RoundProgressBar roundProgressBar = MyUtils.getViewFromVH(rootview, R.id.roundProgressBar);

			final FileBean fileBean = fileList.get(position);
			String path = fileBean.getPath();
			String fileNm = fileBean.getFileNm();
			String fileType = fileBean.getTp1();
			
			if(fileBean.getTp3()==1){
				ll_bg.setBackgroundResource(R.color.white);
			}else{
				ll_bg.setBackgroundResource(R.color.yunpan_gray_bg);
			}

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
					MyGlideUtil.getInstance().displayImageSquere("file://" + path, file_ico);
				} else {
					MyGlideUtil.getInstance().displayImageSquere(Constans.ROOT_imgUrl + fileNm, file_ico);
				}
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

	private PullToRefreshListView lv_pull;
	private ArrayList<FileBean> fileList = new ArrayList<>();
	private ArrayList<FileBean> fileList_shang = new ArrayList<>();// 记录上次的fileBean
	private FileBean fileBean_current = new FileBean();// 当前的filebean
	private YunPanAdapter yunPanAdapter;
	private int pageNo = 1;
	private boolean isRefresh = true;
	private TextView tv_headTitle;
	private FileBean fileBean_yuan;
	private int position_yuan;
	private Button btn_move;
	private LinearLayout ll_hideSpace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.x_activity_move_file);
		fileBean_current.setFileNm("我的文件");
		fileBean_current.setTp2(1);//默认类型是1，“我的文件”
		fileBean_current.setId(0);
		Intent intent = getIntent();
		if (intent != null) {
			fileBean_yuan = intent.getParcelableExtra("fileBean");
			position_yuan = intent.getIntExtra("position", -1);//移动成功后，原来列表要移除那行
			Log.e("fileBean_yuan----", ""+fileBean_yuan.toString());
		} 
		
		initUI();
		
		initData_queryYFile(null, ""+fileBean_current.getTp2(), ""+fileBean_current.getId());// 查询文件
	}

	// 头部
	private void initHead() {
		findViewById(R.id.iv_head_back).setOnClickListener(this);
		// findViewById(R.id.tv_head_right).setOnClickListener(this);
		findViewById(R.id.img_head_right).setOnClickListener(this);
		tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
		TextView tv_headRight = (TextView) findViewById(R.id.tv_head_right);
		ImageView img_head_right = (ImageView) findViewById(R.id.img_head_right);
		tv_headTitle.setText("我的文件");
		// tv_headRight.setText("");
		img_head_right.setImageResource(R.drawable.icon_add_yunpan);
		tv_headRight.setVisibility(View.GONE);
		img_head_right.setVisibility(View.GONE);
	}

	private void initUI() {
		initHead();
		ll_hideSpace = (LinearLayout) findViewById(R.id.ll_hideSpace);
		findViewById(R.id.ll_hideSpace).setOnClickListener(this);//隐藏空间
		btn_move = (Button) findViewById(R.id.btn_move);
		btn_move.setOnClickListener(this);
		//设置按钮是否可编辑
		if(fileBean_yuan.getPid()==0){
			btn_move.setEnabled(false);
		}else{
			btn_move.setEnabled(true);
		}

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
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isRefresh = false;
				pageNo++;
			}
		});

		lv_pull.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ll_hideSpace.setVisibility(View.GONE);
				FileBean fileBean = fileList.get(position - 1);
				if (fileBean == null) {
					return;
				}
				// 1文件夹；2文件
				if (2 == fileBean.getTp3()) {// 是文件
				} else if (1 == fileBean.getTp3()) {// 是文件夹
					isRefresh = true;
					fileList_shang.add(fileBean_current);
					fileBean_current = fileBean;// 记录当前的fileBean_chuan；
//					tv_headTitle.setText(fileBean.getFileNm());
					initData_queryYFile(null, "" + fileBean.getTp2(), ""+ fileBean.getId());
				}
				
				Log.e("fileList_shang---------", fileList_shang.toString());
			}
		});
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

	// 移动文件
	private void initData_movefile(String id, String pid) {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("id", id);// 当前文件id
		params.put("pid", pid);// 对应文件夹id
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.movefile)
				.id(2)
				.build()
				.execute(new MyStringCallback(),null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_head_back:
			finish();
			break;
		case R.id.btn_move:
			initData_movefile(""+fileBean_yuan.getId(), ""+fileBean_current.getId());
			break;
		case R.id.ll_hideSpace:
			isRefresh = true;
			FileBean fileBean = new FileBean();
			fileBean.setFileNm("隐藏空间");
			fileBean.setTp2(3);
			fileList_shang.add(fileBean_current);
			fileBean_current = fileBean;// 记录当前的fileBean_chuan；
//			tv_headTitle.setText(fileBean.getFileNm());
			ll_hideSpace.setVisibility(View.GONE);
			initData_queryYFile(null, "" + fileBean.getTp2(), ""+ fileBean.getId());
			Log.e("fileList_shang---------", fileList_shang.toString());
			break;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 按返回键是判断上级id不是0
			if (fileList_shang != null ) {
				if(fileList_shang.size() > 0){
					isRefresh = true;
					FileBean fileBean = fileList_shang.get(fileList_shang.size() - 1);
					fileBean_current = fileBean;
					fileList_shang.remove(fileList_shang.size() - 1);
					initData_queryYFile(null, "" + fileBean.getTp2(),"" + fileBean.getId());
					if(fileList_shang.size() == 0){
						ll_hideSpace.setVisibility(View.VISIBLE);
					}
				}else if(fileList_shang.size() == 0){//默认请求是“我的文件”根目录
					finish();
				}
				return true;
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
			if (MyUtils.isEmptyString(json)) {
				return;
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
							}

							List<FileBean> rows = parseObject.getRows();
							if (rows != null) {
								fileList.addAll(rows);
								yunPanAdapter.notifyDataSetChanged();
								//设置按钮是否可编辑
								tv_headTitle.setText(fileBean_current.getFileNm());
								if(fileBean_yuan.getPid()==fileBean_current.getId()
										&& fileBean_yuan.getTp2()==fileBean_current.getTp2()){
									btn_move.setEnabled(false);
								}else{
									btn_move.setEnabled(true);
								}
							}
						} else {
							ToastUtils.showCustomToast(parseObject.getMsg());
						}
					}
					break;

				case 2://移动文件
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject != null) {
							if(jsonObject.getBoolean("state")){
								Intent data=new Intent();
								data.putExtra("fileBean", fileBean_yuan);
								data.putExtra("position", position_yuan);
								setResult(Constans.requestcode_movefile, data);
								finish();
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
