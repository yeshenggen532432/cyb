package com.qwb.view.tab.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.kennyc.view.MultiStateView;
import com.qwb.view.audit.ui.AuditDetailActivity;
import com.qwb.view.shop.ui.ShopStepActivity;
import com.qwb.event.CategroyMessageEvent;
import com.qwb.event.MessageEvent;
import com.qwb.event.MsgModelEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDataUtils;
import com.qwb.view.log.ui.LogDetailActivity;
import com.qwb.view.plan.ui.PlanActivity;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.utils.Constans;
import com.qwb.utils.SPUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.tab.adapter.CategroyAdapter;
import com.qwb.view.tab.adapter.MessageAdapter;
import com.qwb.db.DMessageBean;
import com.qwb.view.tab.parsent.PxMessage;
import com.qwb.view.tab.ui.message.XMessageActivity;
import com.qwb.utils.MyRecyclerViewUtil;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.call.ui.CallQueryActivity;
import com.qwb.view.log.model.RizhiListBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * tab:消息
 */
public class XMessageFragment extends XFragment<PxMessage> {

	private DMessageBean mCurrentItem;//当前行对象
	private Integer mPosition;
	private int isAgree;//1:同意；2：不用意

	public XMessageFragment() {
	}

	@Override
	public int getLayoutId() {
		return R.layout.x_fragment_message;
	}

	@Override
	public PxMessage newP() {
		return new PxMessage();
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		initBundle();
		initEvent();
		initUI();
		initReceiver();
	}

	@Override
	public boolean useEventBus() {
		return true;
	}

	private void initEvent(){
		//切换消息分类模式
		BusProvider.getBus().toFlowable(MsgModelEvent.class)
				.subscribe(new Consumer<MsgModelEvent>() {
					@Override
					public void accept(MsgModelEvent event) throws Exception {
						doMsgModel();
					}
				});
		//全部数据item点击事件，通知上个页面分类更新
		BusProvider.getBus().toFlowable(CategroyMessageEvent.class)
				.subscribe(new Consumer<CategroyMessageEvent>() {
					@Override
					public void accept(CategroyMessageEvent event) throws Exception {
						if(MyStringUtil.isEmpty(bankuai)){
							initCategoryData();
						}
					}
				});
	}


	/**
	 * 初始化传递参数
	 */
	private String bankuai;
	private void initBundle(){
		Bundle bundle = getArguments();
		if(null != bundle){
			bankuai = bundle.getString(ConstantUtils.Intent.BANKUAI);
		}
	}

	private void initUI() {
		initMultiStateView();
		initAdapter();
		initCategroyAdapter();
		initSearch();
		doMsgModel();
	}

	@BindView(R.id.et_search)
	EditText mEtSearch;
	private void initSearch(){
		mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					//TODO 关闭软件盘
					MyKeyboardUtil.closeKeyboard(context);
					// 在这里写搜索的操作,一般都是网络请求数据
					queryData(true);
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 初始化显示方式：msgModel:(1:没启用分类，2或空：启用分类（默认）)
	 */
	@BindView(R.id.layout_fenlei)
	View mFlLayout;
	private void doMsgModel() {
		queryData(false);
		initCategoryData();

		String msgModel = SPUtils.getSValues(ConstantUtils.Sp.MSG_MODEL);
		if ((!MyStringUtil.isEmpty(msgModel) && "1".equals(msgModel)) || !MyStringUtil.isEmpty(bankuai)) {
			//显示全部数据
			mFlLayout.setVisibility(View.GONE);
		}else{
			mFlLayout.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 信息数据
	 */
	List<DMessageBean> messageList = new ArrayList<>();
	private void queryData(boolean isSearch) {
		try {
			mCurrentItem = null;
			mPosition = null;
			if(null != messageList ){
				messageList.clear();
			}
			if(isSearch){
				String search = mEtSearch.getText().toString().trim();
				messageList = MyDataUtils.getInstance().queryMessageBySearch(search,bankuai);
			}else{
				if(TextUtils.isEmpty(bankuai)){
					//全部数据
					messageList = MyDataUtils.getInstance().queryMessageList();
				}else{
					//以板块获取数据（点‘分类’：如：审批，系统通知，点评，沟通，工作汇报，商城订单。。。）
					messageList = MyDataUtils.getInstance().queryMessageByBankuai(bankuai);
				}
			}

			//适配器刷新
			if(null != mMessageAdapter){
				mMessageAdapter.setNewData(messageList);
			}

			if(!isSearch){
				//空数据
				if(null != messageList && messageList.isEmpty() && null != mMultiStateView){
					mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
				}
			}
			XLog.e("queryData------------------");
		}catch (Exception e){
			ToastUtils.showLongCustomToast(e.getMessage());
		}
	}

	/**
	 * layout:content,empty,error,loading:四种状态布局
	 */
	@BindView(R.id.multiStateView)
	MultiStateView mMultiStateView;
	private void initMultiStateView() {
		mMultiStateView.setStateListener(new MultiStateView.StateListener() {
			@Override
			public void onStateChanged(int viewState) {

			}
		});
		mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.refresh)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMultiStateView.setViewState(MultiStateView.VIEW_STATE_LOADING);
					}
				});
	}

	/**
	 * 适配器
	 */
	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;
	MessageAdapter mMessageAdapter;
	private void initAdapter() {
		mMessageAdapter = new MessageAdapter(context);
		mMessageAdapter.setNewData(messageList);
		MyRecyclerViewUtil.init(context,mRecyclerView,mMessageAdapter);
		mMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				doOnItemClick(adapter, position);
			}
		});

		//长按事件:删除数据
		mMessageAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					DMessageBean item = (DMessageBean) adapter.getData().get(position);
					mCurrentItem = item;
					mPosition = position;
					showDelDialog();
				}catch (Exception e){}
				return false;
			}
		});
		//子item事件
		mMessageAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

			@Override
			public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					DMessageBean item = (DMessageBean) adapter.getData().get(position);
					mCurrentItem = item;
					mPosition = position;
					switch (view.getId()){
						case R.id.item_agree://转让客户是否同意
							showAgreeDialog();
							break;
					}
				}catch (Exception e){}
			}
		});
	}
	/**
	 * 适配器
	 */
	@BindView(R.id.recyclerView_categroy)
	RecyclerView mCategroyRecyclerView;
	CategroyAdapter mCategroyAdapter;
	private void initCategroyAdapter() {
		mCategroyAdapter = new CategroyAdapter(context);
		mCategroyAdapter.setNewData(categoryList);
		MyRecyclerViewUtil.init(context,mCategroyRecyclerView,mCategroyAdapter);
		mCategroyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				try {
					DMessageBean item = (DMessageBean)adapter.getData().get(position);
					mCurrentItem = item;
					mPosition = position;
					int bankuai = Integer.valueOf(item.getBankuai());
					switch (Integer.valueOf(item.getBankuai())){
						case ConstantUtils.Messeage.M_XTTZ://系统通知
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_XTTZ,"系统通知");
							break;
						case ConstantUtils.Messeage.M_SP:// 审批
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_SP,"审批");
							break;
						case ConstantUtils.Messeage.M_PL://点评
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_PL,"点评");
							break;
						case ConstantUtils.Messeage.M_GT://沟通
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_GT,"沟通");
							break;
						case ConstantUtils.Messeage.M_GZHB://工作汇报
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_GZHB,"工作汇报");
							break;
						case ConstantUtils.Messeage.M_SC://商城订单
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_SC,"商城订单");
							break;
						case ConstantUtils.Messeage.M_GG://公告
							jumpCategoryActivity(""+ConstantUtils.Messeage.M_GG,"公告");
							break;
					}

					delBadge(bankuai);

				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 适配器item点击监听
	 */
	private void doOnItemClick(BaseQuickAdapter adapter, int position) {
		try {
            DMessageBean item = (DMessageBean)adapter.getData().get(position);
            mCurrentItem = item;
            mPosition = position;

			String tp = item.getType();
            switch (Integer.valueOf(tp)) {
                case 43:// 拜访查询-评论
                    Router.newIntent(context)
                            .to(CallQueryActivity.class)
                            .putString(ConstantUtils.Intent.TYPE,"2")
                            .putString("bfId",String.valueOf(item.getBelongId()))
                            .launch();
                    break;
                case 12:// 企业公告
					ActivityManager.getInstance().jumpToWebX5Activity(context, String.format(Constans.WEB_URL_gonggao, SPUtils.getTK(), item.getBelongId(), "12"), null);
                    break;
                case 13:// 系统公告
					ActivityManager.getInstance().jumpToWebX5Activity(context, String.format(Constans.WEB_URL_gonggao, SPUtils.getTK(), item.getBelongId(), "13"), null);
                    break;
                case 32:// 计划拜访
                    ActivityManager.getInstance().jumpActivity(context,PlanActivity.class);
                    break;
                case 41:// 转让客户
                case 42:// 转让客户
//                    Router.newIntent(context)
//                            .to(GzhbMessageListActivity.class)
//                            .putString(ConstantUtils.Intent.TYPE,"1")
//                            .launch();
                    break;
                case 34:// 日志--工作汇报
                case 40:// 日志-日报，周报，月报评论
                    RizhiListBean.RizhiList rizhi = new RizhiListBean.RizhiList();
                    rizhi.setId(item.getBelongId());
                    rizhi.setMemberNm(item.getMemberName());
                    rizhi.setFbTime(item.getAddTime());
                    rizhi.setTp(Integer.valueOf(item.getBelongMsg()));
                    Router.newIntent(context)
                            .to(LogDetailActivity.class)
                            .putSerializable(ConstantUtils.Intent.LOG,rizhi)
                            .launch();
                    break;
                case 100:// 商城订单(订单详情)
					Router.newIntent(context)
							.putInt(ConstantUtils.Intent.ORDER_ID, item.getBelongId())// 订单id
							.putInt(ConstantUtils.Intent.ORDER_TYPE, ConstantUtils.Order.O_SC)
							.putString(ConstantUtils.Intent.COMPANY_ID,SPUtils.getSValues(ConstantUtils.Sp.COMPANY_ID))
							.to(ShopStepActivity.class)
							.launch();
                    break;
                case 31:// 31审批
                    Router.newIntent(context)
                            .to(AuditDetailActivity.class)
                            .putInt("needCheck",1)
                            .putString(ConstantUtils.Intent.ID,String.valueOf(item.getBelongMsg()))
                            .launch();
                    break;
            }
			//改变数据库已读状态;刷新列表
			if("0".equals(mCurrentItem.getIsRead())){
				mCurrentItem.setIsRead("1");
				int count =  MyDataUtils.getInstance().updateMessage(mCurrentItem);
				XLog.e("onitemclick修改数据-------",""+count);
				if(count > 0){
					doReadorDelRefreshAdapter(false);
				}
			}
        }catch (Exception e){
            e.printStackTrace();
        }
	}

	/**
	 * 是否同意
	 */
	private void showAgreeDialog() {
		NormalDialog normalDialog = new NormalDialog(context);
		normalDialog.content("确定同意该操作？").btnText("不同意","同意").show();
		normalDialog.setOnBtnClickL(new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				isAgree = 2;
				doAgree();
			}
		}, new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				isAgree = 1;
				doAgree();
			}
		});
	}

	private void doAgree(){
		String tp = mCurrentItem.getType();
		switch (tp){
			case "26":
				getP().doAgreeJoinCompany(context, mCurrentItem,""+isAgree);
				break;
			default:
				getP().doAgree(context,mCurrentItem,""+isAgree);
				break;
		}
	}

	/**
	 * 删除数据
	 */
	private void showDelDialog() {
		NormalDialog normalDialog = new NormalDialog(context);
		normalDialog.content("确定删除该条消息？").show();
		normalDialog.setOnBtnClickL(null, new OnBtnClickL() {
			@Override
			public void onBtnClick() {
				int count = MyDataUtils.getInstance().delMessage(mCurrentItem);
				if(count > 0){
					ToastUtils.showCustomToast("删除成功");
					doReadorDelRefreshAdapter(true);
				}
			}
		});
	}

	//---------------------------------------------------------------------------------------------------------------------------



	/**
	 * mark "bangkuai"+板块对应的数字 1 ：系统通知 2：审批 3：易推事板 4： 点评 5：真心话 6：沟通;10.工作汇报；11.商城订单
	 */
	List<DMessageBean> categoryList =new ArrayList<>();
	private void initCategoryData() {
		categoryList.clear();
		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_XTTZ),"系统通知",R.mipmap.msg_gg);
		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_GG),"公告",R.mipmap.msg_gg);
		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_SP),"审批",R.mipmap.msg_sp);
		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_PL),"点评",R.mipmap.msg_dp);
//		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_GT),"沟通",R.mipmap.msg_gt);
		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_GZHB),"工作汇报",R.mipmap.msg_dp);
		setCategoryList(String.valueOf(ConstantUtils.Messeage.M_SC),"商城订单",R.mipmap.msg_gt);

		mCategroyAdapter.setNewData(categoryList);
	}

	private void setCategoryList(String bankuai,String title,int imgResId){
		List<DMessageBean> xttzList = MyDataUtils.getInstance().queryMessageByBankuai(bankuai);
		int count1 = MyDataUtils.getInstance().queryCategroyMessageCount(bankuai);
		DMessageBean item = new DMessageBean();
		if(null != xttzList && !xttzList.isEmpty()){
			item = xttzList.get(0);
			item.setCount(count1);
		}else {
			item.setMsg("暂无消息");
			item.setAddTime("");
			item.setCount(0);
		}
		item.setBankuai(Integer.valueOf(bankuai));
		item.setTitle(title);
		item.setImgResId(imgResId);
		categoryList.add(item);
	}

	public void jumpCategoryActivity(String banKuai,String title){
		Router.newIntent(context)
				.to(XMessageActivity.class)
				.putString(ConstantUtils.Intent.BANKUAI,banKuai)
				.putString(ConstantUtils.Intent.TITLE,title)
				.launch();
	}

	/**
	 * 处理转让客户：同意还是不同意
	 */
	public void doAgreeSuccess(){
		mCurrentItem.setIsact(isAgree);
		mCurrentItem.setIsRead("1");
		int count = MyDataUtils.getInstance().updateMessage(mCurrentItem);
		if(count > 0){
			doReadorDelRefreshAdapter(false);
			ToastUtils.showCustomToast("操作成功");
		}
	}

	//TODO--------------------------------------广播相关：start------------------------------------------------------------
	// 广播
	private void initReceiver() {
		IntentFilter intentFilter = new IntentFilter(Constans.Action_login);
		intentFilter.addAction(Constans.UnRreadMsg);// res 未读消息 刷新广播
		intentFilter.addAction(Constans.whitchbankuai);// 区别哪个板块对应显示红点
		context.registerReceiver(myReceive, intentFilter);
	}
	private BroadcastReceiver myReceive = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constans.whitchbankuai.equals(intent.getAction())) {
				receviceRefreshAdapter();
			}else if (Constans.UnRreadMsg.equals(intent.getAction())) {
				receviceRefreshAdapter();
			}
		}
	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (myReceive != null) {
			context.unregisterReceiver(myReceive);
		}
	}

	//TODO--------------------------------------广播相关：end------------------------------------------------------------

	/**
	 * 处理已读或删除数据：
	 * 1）刷新适配器
	 * 2）分类的红点
	 * 3）删除如果是空数据；显示空布局
	 */
	public void doReadorDelRefreshAdapter(boolean isDel){
		if (isDel){
			mMessageAdapter.remove(mPosition);
			mMessageAdapter.notifyDataSetChanged();
			//TODO 如果是删除未读的数据；要通知‘分类’改变红点数字;如果没有数据了：显示空图片
			if(mMessageAdapter.getData().isEmpty()){
				mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
			}
		}else{
			mMessageAdapter.setData(mPosition,mCurrentItem);
			mMessageAdapter.notifyDataSetChanged();
		}
		//刷新分类的红点数量
		BusProvider.getBus().post(new MessageEvent());
	}

	/**
	 * 再当前页面，收到信息；刷新列表
	 */
	public void receviceRefreshAdapter(){
		queryData(false);
		initCategoryData();
	}


	//删除红点
	private void delBadge(int bankuai){
		int count = MyDataUtils.getInstance().updateCategroyMessageCount(String.valueOf(bankuai),false);
		if(count > 0){
			BusProvider.getBus().post(new CategroyMessageEvent());
			//公告要通知首页公告红点
			BusProvider.getBus().post(new CategroyMessageEvent());
		}
	}

}
