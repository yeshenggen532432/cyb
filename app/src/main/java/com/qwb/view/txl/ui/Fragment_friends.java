package com.qwb.view.txl.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qwb.view.txl.model.MyFriendBean;
import com.qwb.view.txl.model.MyFriendBean.FriendInfor;
import com.qwb.utils.MyGlideUtil;
import com.xmsx.cnlife.widget.BladeView;
import com.xmsx.cnlife.widget.BladeView.OnBladeViewItemClickListener;
import com.xmsx.cnlife.widget.CircleImageView;
import com.qwb.utils.Constans;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.MyPopWindowManager.OnImageBack;
import com.qwb.utils.MyUtils;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.xmsx.qiweibao.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class Fragment_friends extends Fragment {

	private PullToRefreshListView lv_pull;
	private ListView refreshableView;
	private int pageNo = 1;
	private MyAdapter myAdapter;
	private List<FriendInfor> friendList_friends = new ArrayList<FriendInfor>();
	private Map<String, Integer> charPosition = new HashMap<String, Integer>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.x_fragment_friends, null);
		initUI(view);
		return view;
	}

	private boolean isRefresh;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initUI(View view) {
		
		lv_pull = (PullToRefreshListView) view.findViewById(R.id.lv_pull);
		refreshableView = lv_pull.getRefreshableView();
    	lv_pull.setOnRefreshListener(new OnRefreshListener2() {


			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				lv_pull.setMode(Mode.BOTH);
				isRefresh = true ;
				pageNo = 1 ;
				getList();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				isRefresh = false ;
				pageNo ++ ;
				getList();
				
			}
		});
		lv_pull.setAdapter(new MyAdapter());
		
		BladeView bv_speedsearch = (BladeView) view.findViewById(R.id.bv_speedsearch);
    	bv_speedsearch.setOnBladeViewItemClickListener(new OnBladeViewItemClickListener() {
			
			@Override
			public void onItemClick(String s) {
				if (charPosition.containsKey(s)) {
					int position = charPosition.get(s);
					refreshableView.setSelection(position);
				}
			}
		});
	}
	
	private void getList() {
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("pageNo", String.valueOf( pageNo ));
		params.put("tp", String.valueOf( 1 ));
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.queryMyMemberURL)
				.id(1)
				.build()
				.execute(new MyStringCallback(),getActivity());
	}
	
	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return friendList_friends.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.x_fragment_friends_items2, null);
			}
			
			FriendInfor friendInfor = friendList_friends.get(position);
			String firstChar = friendInfor.getFirstChar();
			TextView tv_name = MyUtils.getViewFromVH(convertView, R.id.tv_title);
			tv_name.setTag(position);
			tv_name.setOnClickListener(myChatOnClickListener);
			TextView tv_tag = MyUtils.getViewFromVH(convertView, R.id.tv_tag);
			 View iv_tel = MyUtils.getViewFromVH(convertView, R.id.iv_tel);
			 iv_tel.setTag(friendInfor);
			 iv_tel.setOnClickListener(mySetChangyongClickListener);
			CircleImageView iv_userhead = MyUtils.getViewFromVH(convertView, R.id.iv_userhead);
			iv_userhead.setTag(friendInfor.getBindMemberId());
			iv_userhead.setOnClickListener(myIMOnClickListener);
			
			if (position == 0 ) {
				tv_tag.setVisibility(View.VISIBLE);
				tv_tag.setText(friendInfor.getFirstChar());
				charPosition.put(firstChar, position);
			}else{
				String firstChar2 = friendList_friends.get(position-1).getFirstChar();
				if (firstChar.equals(firstChar2)) {
					tv_tag.setVisibility(View.GONE);
				}else{
					charPosition.put(firstChar, position);
					tv_tag.setVisibility(View.VISIBLE);
					tv_tag.setText(firstChar);
				}
			}
			
			tv_name.setText(friendInfor.getMemberNm());
			MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + friendInfor.getMemberHead(), iv_userhead);
			return convertView;
		}
		
	}
	
	private FriendInfor currentInfor;
	private OnClickListener mySetChangyongClickListener = new OnClickListener() {


		@Override
		public void onClick(View v) {
		    currentInfor = (FriendInfor) v.getTag();
		    if ("1".equals(currentInfor.getIsUsed())) {
		    	MyPopWindowManager.getI().show(getActivity(), imageback, "拨打  "+currentInfor.getMemberMobile() , "取消常用");
		    }else{
		    	MyPopWindowManager.getI().show(getActivity(), imageback, "拨打 "+currentInfor.getMemberMobile(), "设为常用");
		    }
		}
	};
	
	
	private OnImageBack imageback = new OnImageBack() {
		
		@Override
		public void fromPhotoAlbum() {
			//设为/取消 常用
			if ("1".equals(currentInfor.getIsUsed())) {
				setClose(String.valueOf(currentInfor.getBindMemberId()),"2");
			}else{
				setClose(String.valueOf(currentInfor.getBindMemberId()),"1");
			}
		}
		
		@Override
		public void fromCamera() {
			//拨打电话
			MyUtils.call(getActivity(), currentInfor.getMemberMobile());
		}
	};
	
	//点击头像进入主页事件
	private OnClickListener myIMOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		}
	};
	
	//点击名字进入聊天事件
	private OnClickListener myChatOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		}
	};
	private void setClose (String bindMemberId , String tp){
		Map<String, String> params = new HashMap<>();
		params.put("token", SPUtils.getTK());
		params.put("bindMemberId", bindMemberId);
		params.put("tp", tp);
		OkHttpUtils
				.post()
				.params(params)
				.url(Constans.updateIsusedURL)
				.id(2)
				.build()
				.execute(new MyStringCallback(),getActivity());
	}
	
	private void refreshAdapter(){
		if (lv_pull != null) {
			 if (myAdapter == null) {
				 myAdapter = new MyAdapter();
				 lv_pull.setAdapter(myAdapter);
			}else{
				myAdapter.notifyDataSetChanged();
			}
		}
	}
	
	public void refershData() {
		pageNo = 1;
		isRefresh = true ;
		getList();
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
				return ;
			}
			// 1通讯录 2设置是否常用
			switch (tag) {
				case 1:
					MyFriendBean parseObject = JSON.parseObject(json, MyFriendBean.class);
					if (parseObject != null && parseObject.isState()) {
						if (pageNo >= parseObject.getTotalPage() ) {
							lv_pull.setMode(Mode.PULL_FROM_START);
						}else{
							lv_pull.setMode(Mode.BOTH);
						}
						List<FriendInfor> rows = parseObject.getRows();
						if (rows != null) {
							if (isRefresh) {
								friendList_friends.clear();
							}
							friendList_friends.addAll(rows);
							Collections.sort(friendList_friends);
							refreshAdapter();
						}
					}
					break;
				case 2 :
					TongXunLuActivity activity = (TongXunLuActivity) getActivity();
					activity.refreshChangYong();
					try {
						JSONObject jsonObject = new JSONObject(json);
						if (jsonObject.getBoolean("state")) {
							if ("1".equals(currentInfor.getIsUsed())) {
								currentInfor.setIsUsed("2");
							}else{
								currentInfor.setIsUsed("1");
							}
						}else{
							ToastUtils.showCustomToast( "操作失败"+jsonObject.getString("msg"));
						}
					} catch (JSONException e) {

						ToastUtils.showCustomToast("操作失败");
					}
					break;
			}
		}
	}
}
