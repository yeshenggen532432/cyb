package com.qwb.view.shop.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.qwb.common.OrderTypeEnum;
import com.qwb.view.print.util.MyPrintUtil;
import com.qwb.event.BaseEvent;
import com.qwb.event.SupplierAddressEvent;
import com.qwb.event.SupplierCacheGoodsListEvent;
import com.qwb.event.SupplierOrderGoodsListEvent;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.utils.MyKeyboardUtil;
import com.qwb.view.shop.model.ShopAddressInfo;
import com.qwb.view.shop.parsent.PShopOrder;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.cnlife.widget.MyTableListView;
import com.qwb.view.step.model.XiaJi;
import com.xmsx.qiweibao.R;
import com.zyyoona7.lib.EasyPopup;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.event.BusProvider;
import cn.droidlover.xdroidmvp.log.XLog;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;
import io.reactivex.functions.Consumer;

/**
 * 创建描述：商城订单（添加，查看）
 */
public class ShopOrderActivity extends XActivity<PShopOrder> {


    @Override
    public int getLayoutId() {
        return R.layout.x_activity_shop_order;
    }

    @Override
    public PShopOrder newP() {
        return new PShopOrder();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initEvent();
        initIntent();
        initUI();
        doIntent();
        createPopupUpdateGoods();//窗体（修改商品信息：数量，单位，单价）
        createPopupRemark();//备注
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 初始化EventBus
     */
    private void initEvent() {
        BusProvider.getBus().toFlowable(SupplierAddressEvent.class)
                .subscribe(new Consumer<SupplierAddressEvent>() {
                    @Override
                    public void accept(SupplierAddressEvent event) throws Exception {
                        if(event.getTag()==ConstantUtils.Event.TAG_SUPPLIER_ADDRESS){
                            try {
                                edit_address.setText(event.getAddress());
                                edit_shr.setText(event.getLinkman());
                                edit_phone.setText(event.getMobile());
                            }catch (Exception e){
                            }
                        }
                    }
                });
    }

    /**
     * 初始化Intent
     */
    private int orderType;//1:添加，2：修改 3：查看
    private int orderId;//订单id
    private String companyId;//
    private String orderZt;//订单状态:未审核；审核
    private String shr;
    private String phone;
    private String address;
    private String remark;
    private String mOrderNo;
    private String mKhNm;
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            orderType = intent.getIntExtra(ConstantUtils.Intent.ORDER_TYPE, 1);
            companyId=intent.getStringExtra(ConstantUtils.Intent.COMPANY_ID);
            orderId = intent.getIntExtra(ConstantUtils.Intent.ORDER_ID, 0);
            shr=intent.getStringExtra(ConstantUtils.Intent.LINKMAN);
            phone=intent.getStringExtra(ConstantUtils.Intent.TEL);
            address=intent.getStringExtra(ConstantUtils.Intent.ADDRESS);
            remark=intent.getStringExtra(ConstantUtils.Intent.REMARK);
            mOrderNo=intent.getStringExtra(ConstantUtils.Intent.ORDER_NO);
            mKhNm=intent.getStringExtra(ConstantUtils.Intent.CLIENT_NAME);
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        initHead();
        initBaseView();
        initAdapter();
    }

    //处理UI
    private void doIntent() {
        switch (orderType){
            //1:添加，2：修改或查看
            case 1:
                mTvHeadRight.setText("提交");
                edit_shr.setText(shr);//默认用户名
                edit_phone.setText(phone);//默认账号
                edit_address.setText(address);
                edit_remo.setText(remark);
                //备注：没有地址信息的时候才请求“默认地址”（这个界面经常关闭）
                if(MyUtils.isEmptyString(address)){
                    getP().queryDefaultAddress(context,companyId);
                }
                refreshListData();
                break;
            case 2://修改
                getP().queryOrderDetail(context,String.valueOf(orderId),companyId);
                tvClear.setVisibility(View.GONE);//清空列表
                tvSearchGoods.setVisibility(View.GONE);
                edit_shr.setEnabled(false);
                edit_phone.setEnabled(false);
                edit_address.setEnabled(false);
                edit_remo.setEnabled(false);
                break;
            case 3://查看
                getP().queryOrderDetail(context,String.valueOf(orderId),companyId);
                tvClear.setVisibility(View.GONE);//清空列表
                tvSearchGoods.setVisibility(View.GONE);
                edit_shr.setEnabled(false);
                edit_phone.setEnabled(false);
                edit_address.setEnabled(false);
                edit_remo.setEnabled(false);
                break;
        }
    }

    /**
     * 头部
     */
    @BindView(R.id.head_left)
    View mViewLeft;
    @BindView(R.id.head_right)
    View mViewRight;
    @BindView(R.id.head_right2)
    View mViewRight2;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorBlue(context);
        mTvHeadTitle.setText("商城订单");
        mTvHeadRight2.setText("打印");
        mViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.getInstance().closeActivity(context);
            }
        });
        mViewRight2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPrintUtil.getInstance().print(context, mOrderNo, mKhNm, mBean, OrderTypeEnum.ORDER_SC.getType());
            }
        });
    }

    /**
     * 基本
     */
    private ImageView img_more;//显示隐藏（收货人-电话--地址）
    private LinearLayout ll_hide;
    private EditText edit_phone;
    private EditText edit_address;
    private EditText edit_remo;//备注
    private EditText edit_shr;//收货人
    private EditText edit_zje;//总金额
    private EditText edit_zdzk;//整单折扣
    private EditText edit_zdzk_percent;//整单折扣百分比
    private EditText edit_cjje;//交易金额
    private TextView tvClear;//清空列表
    private TextView tvSearchGoods;//搜索已选商品
    @BindView(R.id.edit_freight)
    EditText mEtFreight;
    @BindView(R.id.edit_order_amount)
    EditText mEtOrderAmount;
    //退货时，ui改变下（配送指定，整单折扣：隐藏；“收货人”改成“退货人”；“送货时间”改成“退货时间”）
    private void initBaseView() {
        //收货人-电话--地址--备注
        img_more = (ImageView) findViewById(R.id.img_more);// 收货人-电话--地址(默认隐藏)
        ll_hide = (LinearLayout) findViewById(R.id.ll_hide);//收货人-电话--地址( 默认隐藏)
        edit_shr = (EditText) findViewById(R.id.edit_shr);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        edit_address = (EditText) findViewById(R.id.edit_address);
        edit_remo = (EditText) findViewById(R.id.edit_remo);
        //总金额；整单折扣；交易金额
        edit_zje = (EditText) findViewById(R.id.edit_zje);
        edit_zdzk = (EditText) findViewById(R.id.edit_zdzk);
        edit_zdzk_percent = (EditText) findViewById(R.id.edit_zdzk_percent);
        edit_cjje = (EditText) findViewById(R.id.edit_cjje);
        //清空列表
        tvClear=(TextView) findViewById(R.id.tv_clear_cache_data);
        tvSearchGoods=(TextView) findViewById(R.id.tv_search_goods);
    }

    /**
     * 适配器--添加商品
     */
    private View line;
    private HorizontalScrollView hs;
    private int[] columns;
    private XsxjAdapterOrder xdAdapter;// 订货下单--适配器
    private void initAdapter() {
        // 添加商品
        line = (View) findViewById(R.id.line);// 线
        hs = (HorizontalScrollView) findViewById(R.id.hs);
        String[] from = new String[] { ConstantUtils.Map.STEP5_1_GOODS_NAME ,ConstantUtils.Map.STEP5_16_GOODS_GG,
                ConstantUtils.Map.STEP5_2_GOODS_XSTP, ConstantUtils.Map.STEP5_3_GOODS_COUNT,
                ConstantUtils.Map.STEP5_4_GOODS_DW, ConstantUtils.Map.STEP5_5_GOODS_DJ,
                ConstantUtils.Map.STEP5_6_GOODS_ZJ, ConstantUtils.Map.STEP5_17_GOODS_REMARK,ConstantUtils.Map.STEP5_7_GOODS_DEL };// 标题:内容--id
        columns = new int[] { R.id.column1, R.id.column8, R.id.column2, R.id.column3,R.id.column4, R.id.column5, R.id.column6,R.id.column17, R.id.column7 };
        xdAdapter = new XsxjAdapterOrder(this, Constans.xsList,  R.layout.x_head_item_supplier_order, from, columns, R.color.difColor,  R.color.difColor1);
        MyTableListView myLisView = new MyTableListView(context, hs, columns,R.id.hs, R.id.list, R.id.head, xdAdapter);
        // 标题栏颜色
        myLisView.setHeadColor(getResources().getColor(R.color.white));
    }

    /**
     * 拜访流程--订货下单的适配器
     * */
    public class XsxjAdapterOrder extends BaseAdapter {
        private String[] mFrom;
        private List<? extends Map<String, String>> mData;
        private LayoutInflater mInflater;
        int[] difColor = new int[2];

        public XsxjAdapterOrder(Context context, List<? extends Map<String, String>> data, int resource,
                                String[] from, int[] to, int colorId, int color1Id) {
            mData = data;// 总数据
            mFrom = from;
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            difColor[0] = context.getResources().getColor(colorId);
            difColor[1] = context.getResources().getColor(color1Id);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) { return 0; }

        @Override
        public View getView(final int position, View convertView,ViewGroup parent) {
            final View root = mInflater.inflate(R.layout.x_adapter_supplier_order,null);
            // 品项，规格，销售类型，数量，单位，单价，总价，备注,操作
            TextView tv1 = (TextView) root.findViewById(R.id.column1);
            TextView tv_xsType = (TextView) root.findViewById(R.id.tv_xsType);
            final TextView tv_count = (TextView) root.findViewById(R.id.tv_count);
            final TextView tv_danwei = (TextView) root.findViewById(R.id.tv_danwei);
            final TextView tv_danjia = (TextView) root.findViewById(R.id.tv_danjia);
            final TextView tv_zongjia = (TextView) root.findViewById(R.id.tv_zongjia);
            TextView tv_chaozuo = (TextView) root.findViewById(R.id.tv_chaozuo);
            TextView tv_gg = (TextView) root.findViewById(R.id.tv_gg);
            TextView tv_remark = (TextView) root.findViewById(R.id.tv_remark);

            final Map<String, String> map = mData.get(position);
            //商品重复的颜色变红
//            int repeat=repeatMap.get(map.get(ConstantUtils.Map.STEP5_0_GOODS_ID));
//            if(repeat>1){
//                tv1.setTextColor(getResources().getColor(R.color.blue));
//            }else{
//                tv1.setTextColor(getResources().getColor(R.color.gray_6));
//            }
            tv1.setText((position+1)+": "+map.get(mFrom[0]));//拼接序号+品项
            tv_gg.setText(map.get(mFrom[1]));// 规格
            tv_xsType.setText(map.get(mFrom[2]));// 销售类型
            tv_count.setText(map.get(mFrom[3]));// 数量
            tv_danwei.setText(map.get(mFrom[4]));// 单位
            tv_remark.setText(map.get(mFrom[7]));// 操作--备注

            //单价;总价：数据不变，显示保留两位小数点
            try{
                double dj=Double.parseDouble(map.get(mFrom[5]));
                double sum=Double.parseDouble(map.get(mFrom[6]));
                tv_danjia.setText(""+ MyDoubleUtils.getDecimal(dj));// 单价
                tv_zongjia.setText(""+MyDoubleUtils.getDecimal(sum));// 总价
            }catch (Exception e){
            }

            // 数量
            tv_count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupDhxd(position, map);//显示窗体：修改订货下单（数量， 单价，单位等等）
                }
            });
            // 单价
            tv_danjia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupDhxd(position, map);//显示窗体：修改订货下单（数量， 单价，单位等等）
                }
            });
            // 单位
            tv_danwei.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupDhxd(position, map);//显示窗体：修改订货下单（数量， 单价，单位等等）
                }
            });
            // 备注
            tv_remark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupRemark(position, map);//显示窗体：修改订货下单（数量， 单价，单位等等）
                }
            });

            //1:添加(有：删除)，2：修改或查看(没有：删除)
            if(orderType==1){
                tv_chaozuo.setText(map.get(mFrom[8]));// 操作--删除
                tv_chaozuo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Constans.xsList.remove(position);
                        refreshListData();
                    }
                });
            }else if(orderType==2 || orderType==3){
                tv_chaozuo.setText("");
            }

            return root;
        }
    }

    //TODO=================================点击事件处理==========================================================
    /**
     * 点击事件
     */
    @OnClick({R.id.tv_head_right,R.id.img_more,R.id.column1,R.id.tv_search_goods,R.id.tv_clear_cache_data,R.id.iv_address_more})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_right://“提交”或“修改”
                switch (orderType){
                    case 1://添加订单
                        addData();
                        break;
                    case 2://删除订单
                        dialogNormalStyleDelOrder();
                        break;
                }
                break;
            case R.id.img_more://隐藏显示：收货人，地址，电话
                showOrHideMore();
                break;
            case R.id.column1://跳转界面-选择商品
                if(orderType==1)Router.pop(context);
                break;
            case R.id.tv_search_goods://搜索已选商品
                mSearchGoodsList.clear();
                mSearchGoodsList.addAll(Constans.xsList);
                refreshAdapterSearchGoods();
                break;
            case R.id.tv_clear_cache_data://清空
                dialogNormalStyleClearList();
                break;
            case R.id.iv_address_more://
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ConstantUtils.Intent.REQUEST_STEP_5_CHOOSE_GOODS && resultCode==ConstantUtils.Intent.RESULT_CODE_CHOOSE_GOODS){
            refreshListData();
        }
    }

    /**
     * 遍历--所有商品的总价求和
     */
    private double sumMoney;//总金额
    private double cjMoney;//成交金额
    private void getSumMoney() {
        sumMoney = 0.0;//TODO 每次默认值（0）
        try{
            // 品项，销售类型，数量，单位，单价，总价，操作
            for (int i = 0; i < Constans.xsList.size(); i++) {
                Map<String, String> map = Constans.xsList.get(i);
                if (!MyUtils.isEmptyString(map.get(ConstantUtils.Map.STEP5_6_GOODS_ZJ))) {
                    sumMoney = sumMoney + Double.valueOf(map.get(ConstantUtils.Map.STEP5_6_GOODS_ZJ));
                }
            }
            edit_zje.setText(String.valueOf(MyDoubleUtils.getDecimal(sumMoney)));
            // 判断整单折扣
            String zdzk = edit_zdzk.getText().toString().trim();
            if (!MyUtils.isEmptyString(zdzk) && sumMoney>0) {// 条件得改
                double zkMoney=Double.valueOf(zdzk);
                double percent=zkMoney*100/sumMoney;
                //计算百分比
                edit_zdzk_percent.setText(String.valueOf(MyDoubleUtils.getDecimal(percent)));
                //计算成交金额
                if (sumMoney - zkMoney > 0) {
                    // 如果:"整单折扣"不能大于"总金额"
                    edit_cjje.setText(String.valueOf(MyDoubleUtils.getDecimal(sumMoney - zkMoney)));
                } else {
                    cjMoney = sumMoney;
                    edit_cjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjMoney)));
                }
            } else {
                cjMoney = sumMoney;
                edit_cjje.setText(String.valueOf(MyDoubleUtils.getDecimal(cjMoney)));
            }
        }catch (Exception e){
            ToastUtils.showCustomToast("字符串转Double错误！");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(orderType==1){
            String shrStr = edit_shr.getText().toString().trim();
            String phoneStr = edit_phone.getText().toString().trim();
            String addressStr = edit_address.getText().toString().trim();
            String remoStr = edit_remo.getText().toString().trim();
            SupplierOrderGoodsListEvent event=new SupplierOrderGoodsListEvent();
            event.setShr(shrStr);
            event.setPhone(phoneStr);
            event.setAddress(addressStr);
            event.setRemark(remoStr);
            BusProvider.getBus().post(event);
        }else{
            //退出界面：清除公用数据
            Constans.xsList.clear();
        }
    }



    //TODO********************************************窗体****************************************************

    /**
     * 修改商品信息：数量，单位，单价
     */
    private TextView tv_name;
    private EditText mEtPrice;
    private EditText mEtGg;//规格
    private EditText mEtDw;
    private EditText mEtCount;
    private LinearLayout mLlUnit;
    private Button mBtnMaxUnit;
    private Button mBtnMinUnit;
    private TextView mTvSm;
    private EasyPopup mEasyPopUpdateGoods;
    public void createPopupUpdateGoods() {
        mEasyPopUpdateGoods = new EasyPopup(context)
                .setContentView(R.layout.x_popup_dhxd_count)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                //变暗的背景颜色
                .setDimColor(Color.BLACK)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .createPopup();
        tv_name = mEasyPopUpdateGoods.getView(R.id.tv_title);// 对应的商品名称
        mEtCount = mEasyPopUpdateGoods.getView(R.id.edit_count);
        mEtDw = mEasyPopUpdateGoods.getView(R.id.edit_dw);
        mEtPrice = mEasyPopUpdateGoods.getView(R.id.edit_price);
        mEtPrice.setEnabled(false);//价格不可输入
        mEtGg = mEasyPopUpdateGoods.getView(R.id.edit_gg);
        mLlUnit = mEasyPopUpdateGoods.getView(R.id.ll_unit);
        mBtnMaxUnit = mEasyPopUpdateGoods.getView(R.id.btn_max_unit);
        mBtnMinUnit = mEasyPopUpdateGoods.getView(R.id.btn_min_unit);
        mTvSm = mEasyPopUpdateGoods.getView(R.id.tv_sm);//执行价，原价
        mTvSm.setVisibility(View.GONE);
        Button btn_queding = mEasyPopUpdateGoods.getView(R.id.btn_confirm);
        Button btn_quxiao = mEasyPopUpdateGoods.getView(R.id.btn_quxiao);
        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEasyPopUpdateGoods.dismiss();
                MyKeyboardUtil.closeKeyboard(context);//强制关闭键盘
            }
        });
        btn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyKeyboardUtil.closeKeyboard(context);//强制关闭键盘
                // 品项，规格，销售类型，数量，单位，单价，总价，备注，操作
                Map<String, String> map = Constans.xsList.get(currentPosition);
                String countStr = mEtCount.getText().toString().trim();
                String priceStr = mEtPrice.getText().toString().trim();
                String dwStr = mEtDw.getText().toString().trim();
                if (!MyUtils.isEmptyString(countStr) && !MyUtils.isEmptyString(priceStr)) {
                    map.put(ConstantUtils.Map.STEP5_3_GOODS_COUNT, countStr);// 数量
                    map.put(ConstantUtils.Map.STEP5_5_GOODS_DJ, priceStr);// 单价
                    double zjStr = Double.valueOf(countStr)* Double.valueOf(priceStr);
                    map.put(ConstantUtils.Map.STEP5_6_GOODS_ZJ, "" + zjStr);// 总价
                } else {
                    ToastUtils.showCustomToast("数量或单价不能为空");
                    return;
                }
                getSumMoney();
                // 单位
                if (!MyUtils.isEmptyString(dwStr)) {
                    map.put(ConstantUtils.Map.STEP5_4_GOODS_DW, dwStr);// 单位
                }
                xdAdapter.notifyDataSetChanged();
                mEtCount.setText("");// 清空
                mEasyPopUpdateGoods.dismiss();
            }
        });
        mEtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countStr = mEtCount.getText().toString().trim();
                if("0".equals(countStr)){
                    mEtCount.setText("");
                }
                mLlUnit.setVisibility(View.GONE);//默认关闭
            }
        });
        mEtPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String priceStr = mEtPrice.getText().toString().trim();
                if("0".equals(priceStr)){
                    mEtPrice.setText("");
                }
                mLlUnit.setVisibility(View.GONE);//默认关闭
            }
        });
        mEtDw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // c8:包装单位代码或计量单位代码 c9:包装单位代码 c10:计量单位代码 c11:包装单位
                // c12:计量单位 c13:换算数量 c14:执行价 c15:原价
                Map<String, String> map = Constans.xsList.get(currentPosition);
                if(!MyUtils.isEmptyString(map.get(ConstantUtils.Map.STEP5_11_GOODS_MAXUNIT)) && !MyUtils.isEmptyString(map.get(ConstantUtils.Map.STEP5_12_GOODS_MINUNIT))){
                    mLlUnit.setVisibility(View.VISIBLE);
                    mBtnMaxUnit.setText(map.get(ConstantUtils.Map.STEP5_11_GOODS_MAXUNIT));
                    mBtnMinUnit.setText(map.get(ConstantUtils.Map.STEP5_12_GOODS_MINUNIT));
                }
            }
        });
        //包装单位
        mBtnMaxUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // c8:包装单位代码或计量单位代码 c9:包装单位代码 c10:计量单位代码 c11:包装单位
                // c12:计量单位 c13:换算数量 c14:执行价 c15:原价
                mLlUnit.setVisibility(View.GONE);
                Map<String, String> map = Constans.xsList.get(currentPosition);
                String price=mEtPrice.getText().toString().trim();
                String dw=mEtDw.getText().toString().trim();//原来的单位
                if(!dw.equals(map.get(ConstantUtils.Map.STEP5_11_GOODS_MAXUNIT))){
                    mEtDw.setText(map.get(ConstantUtils.Map.STEP5_11_GOODS_MAXUNIT));
                    map.put(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT,map.get(ConstantUtils.Map.STEP5_9_GOODS_MAXUNIT_CODE));//修改包装单位代码或计量单位代码
                    if(MyUtils.isEmptyString(price)){
                        mEtPrice.setText("");
                    }else{
                        String hsNum=map.get(ConstantUtils.Map.STEP5_13_GOODS_HS_NUM);
                        //销售价格(大)=销售价格（小）*换算数量
                        mEtPrice.setText(String.valueOf(Double.valueOf(price)*Double.valueOf(hsNum)));
                    }
                }
            }
        });
        //计量单位
        mBtnMinUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // c8:包装单位代码或计量单位代码 c9:包装单位代码 c10:计量单位代码 c11:包装单位
                // c12:计量单位 c13:换算数量 c14:执行价 c15:原价
                mLlUnit.setVisibility(View.GONE);
                Map<String, String> map = Constans.xsList.get(currentPosition);
                String price=mEtPrice.getText().toString().trim();
                String dw=mEtDw.getText().toString().trim();//原来的单位
                if(!dw.equals(map.get(ConstantUtils.Map.STEP5_12_GOODS_MINUNIT))){//判断不是原来的单位
                    mEtDw.setText(map.get(ConstantUtils.Map.STEP5_12_GOODS_MINUNIT));
                    map.put(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT,map.get(ConstantUtils.Map.STEP5_10_GOODS_MINUNIT_CODE));//修改包装单位代码或计量单位代码
                    if(MyUtils.isEmptyString(price)){
                        mEtPrice.setText("");
                    }else{
                        String hsNum=map.get(ConstantUtils.Map.STEP5_13_GOODS_HS_NUM);
                        //销售价格(小)=销售价格（大）／换算数量
                        mEtPrice.setText(String.valueOf(Double.valueOf(price)/Double.valueOf(hsNum)));
                    }
                }
            }
        });

        //加
        mEasyPopUpdateGoods.getView(R.id.iv_jia_dhxd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String countStr=mEtCount.getText().toString().trim();
                    if(MyUtils.isEmptyString(countStr)){
                        mEtCount.setText("1");
                        mEtCount.setSelection(1);//将光标移至文字末尾
                    }else{
                        int count=Integer.valueOf(countStr);
                        count++;
                        mEtCount.setText(String.valueOf(count));
                        mEtCount.setSelection(String.valueOf(count).length());//将光标移至文字末尾
                    }
                }catch (Exception e){
                }
            }
        });
        //减
        mEasyPopUpdateGoods.getView(R.id.iv_jian_dhxd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String countStr=mEtCount.getText().toString().trim();
                    if(MyUtils.isEmptyString(countStr)){
                        mEtCount.setText("0");
                        mEtCount.setSelection(1);//将光标移至文字末尾
                    }else{
                        int count=Integer.valueOf(countStr);
                        if(count!=0){
                            count--;
                        }
                        mEtCount.setText(String.valueOf(count));
                        mEtCount.setSelection(String.valueOf(count).length());//将光标移至文字末尾
                    }
                }catch (Exception e){
                }
            }
        });
    }
    /**
     * 修改商品信息：数量，单位，单价
     */
    private TextView mTvNameRemark;
    private EditText mEtRemark;//备注
    private EasyPopup mEasyPopRemark;
    public void createPopupRemark() {
        mEasyPopRemark = new EasyPopup(context)
                .setContentView(R.layout.x_popup_dhxd_remark)
                //允许背景变暗
                .setBackgroundDimEnable(true)
                //变暗的透明度(0-1)，0为完全透明
                .setDimValue(0.4f)
                //变暗的背景颜色
                .setDimColor(Color.BLACK)
                //是否允许点击PopupWindow之外的地方消失
                .setFocusAndOutsideEnable(true)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .createPopup();
        mTvNameRemark = mEasyPopRemark.getView(R.id.tv_title);// 对应的商品名称
        mEtRemark = mEasyPopRemark.getView(R.id.edit_remark);
        Button btn_queding = mEasyPopRemark.getView(R.id.btn_confirm);
        Button btn_quxiao = mEasyPopRemark.getView(R.id.btn_quxiao);
        btn_quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEasyPopRemark.dismiss();
                MyKeyboardUtil.closeKeyboard(context);//强制关闭键盘
            }
        });
        btn_queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 品项，规格，销售类型，数量，单位，单价，总价，备注，操作
                Map<String, String> map = Constans.xsList.get(currentPosition);
                String remarkStr = mEtRemark.getText().toString().trim();
                map.put(ConstantUtils.Map.STEP5_17_GOODS_REMARK, remarkStr);// 单位
                // 备注
                if (!MyUtils.isEmptyString(remarkStr)) {
                    map.put(ConstantUtils.Map.STEP5_17_GOODS_REMARK, remarkStr);
                }else{
                    map.put(ConstantUtils.Map.STEP5_17_GOODS_REMARK, "");
                }
                xdAdapter.notifyDataSetChanged();
                mEasyPopRemark.dismiss();
                mEtRemark.setText("");// 清空
                MyKeyboardUtil.closeKeyboard(context);//强制关闭键盘
            }
        });
    }

    /**
     * 显示窗体：修改订货下单（数量， 单价，单位等等）
     */
    private int currentPosition = -1;//当前商品列表的位置
    private void showPopupDhxd(int position, Map<String, String> map) {
        // 品项，规格，销售类型，数量，单位，单价，总价，备注，操作
        try{
            currentPosition = position;
            tv_name.setText(map.get(ConstantUtils.Map.STEP5_1_GOODS_NAME));// 窗体--商品名称
            mEtCount.setText(map.get(ConstantUtils.Map.STEP5_3_GOODS_COUNT));// 数量
            double dj=Double.parseDouble(map.get(ConstantUtils.Map.STEP5_5_GOODS_DJ));
            mEtPrice.setText(""+MyDoubleUtils.getDecimal(dj)); // 价格(数据不变；显示保留两位小数的)
            mEtGg.setText(map.get(ConstantUtils.Map.STEP5_16_GOODS_GG)); // 规格
            mEtDw.setText(map.get(ConstantUtils.Map.STEP5_4_GOODS_DW)); // 单位
            if(!MyUtils.isEmptyString(map.get(ConstantUtils.Map.STEP5_14_GOODS_ZXJ)) && !MyUtils.isEmptyString(map.get(ConstantUtils.Map.STEP5_15_GOODS_YJ))){
                mTvSm.setVisibility(View.VISIBLE);
                mTvSm.setText("提示:"+"\n执行价:"+map.get(ConstantUtils.Map.STEP5_14_GOODS_ZXJ)+"\n原价:"+map.get(ConstantUtils.Map.STEP5_15_GOODS_YJ));
            }else{
                mTvSm.setVisibility(View.GONE);
            }
            mLlUnit.setVisibility(View.GONE);//默认关闭
            mEasyPopUpdateGoods.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
        }catch (Exception e){
        }
    }

    /**
     * 显示窗体：备注
     */
    private void showPopupRemark(int position, Map<String, String> map) {
        // 品项，规格，销售类型，数量，单位，单价，总价，备注，操作
        currentPosition = position;
        mTvNameRemark.setText(map.get(ConstantUtils.Map.STEP5_1_GOODS_NAME));
        mEtRemark.setText(map.get(ConstantUtils.Map.STEP5_17_GOODS_REMARK));
        mEasyPopRemark.showAtLocation(findViewById(R.id.parent), Gravity.CENTER, 0, 0);
    }

    /**
     * 搜索商品
     */
    public ArrayList<HashMap<String, String>> mSearchGoodsList= new ArrayList<HashMap<String, String>>();

    /**
     * dialog-清空列表
     */
    private void dialogNormalStyleClearList() {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.setCanceledOnTouchOutside(false);//外部点击不消失
        dialog
                .isTitleShow(true)//是否需要标题
                .style(NormalDialog.STYLE_TWO)//标题风格二(标题居中，没有线,内容水平居中)，默认风格一
                .title("温馨提示")
                .titleTextColor(Color.parseColor("#333333"))
                .titleTextSize(18)
                .content("是否要清空列表")
                .contentTextColor(Color.parseColor("#0082CE"))
                .contentTextSize(15)
                .contentGravity(Gravity.CENTER_VERTICAL)//内容显示位置（风格二时，居中）
                .bgColor(Color.parseColor("#f1f1f2"))//背景颜色
                .cornerRadius(5)//父布局的圆角
//				.dividerColor(Color.parseColor("#ff0000"))//"确定"，“取消”，“中间”三按钮分割线的颜色
                .widthScale(0.8f)//布局宽度占屏幕的比例
                .btnNum(2)//按钮数量（默认两按钮，确定"，“取消”）
                .btnText("取消","清空")
                .btnTextColor(Color.parseColor("#666666"),Color.parseColor("#666666"))
                .btnTextSize(15,15)
                .btnPressColor(Color.parseColor("#f4f4f4"))//按钮按下的背景颜色
                .show();
        //"确定"，“取消”，“中间”三按钮的点击事件
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        clearList();
                    }
                });
    }

    /**
     * 刷新搜索商品的adapter
     */
    public void refreshAdapterSearchGoods(){
//        if (mAdapterSearchGoods == null) {
//            mAdapterSearchGoods = new Step5SearchGoodsAdapter(context,mSearchGoodsList);
//            mLvSearchGoods.setAdapter(mAdapterSearchGoods);
//        } else {
//            mAdapterSearchGoods.notifyDataSetChanged();
//        }
    }

    /**
     * 获取外层列表的位置（用来修改当前位置的数据）
     */
    public int getListPosition(HashMap<String, String> mapSearch){
        int postion=-1;
        for (HashMap<String, String> map : Constans.xsList) {
            postion++;
            if(mapSearch.equals(map)){
                return postion;
            };
        }
        return postion;
    }

    /**
     * 设置标记商品的个数（重复商品颜色变）--1:选择商品后 2：删除商品 3：获取订单数据
     */
    private Map<String,Integer> repeatMap=new HashMap<>();
    public void setRepeatMap(){
        repeatMap.clear();
        for (HashMap<String, String> map : Constans.xsList) {
            String goodsId=map.get(ConstantUtils.Map.STEP5_0_GOODS_ID);
            if(repeatMap.containsKey(goodsId)){
                int qty=repeatMap.get(goodsId);
                repeatMap.put(goodsId,qty+1);
            }else{
                repeatMap.put(goodsId,1);
            }
        }
    }

    //TODO ******************************接口******************************
    /**
     * 提交或修改数据
     */
    private void addData() {
        // 品项，销售类型，数量，单位，单价，总价，操作
        List<XiaJi> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < Constans.xsList.size(); i++) {
            HashMap<String, String> hashMap = Constans.xsList.get(i);
            XiaJi xiaJi = new XiaJi();
            xiaJi.setWareId((hashMap.get(ConstantUtils.Map.STEP5_0_GOODS_ID)));
            xiaJi.setWareNum(hashMap.get(ConstantUtils.Map.STEP5_3_GOODS_COUNT));
            xiaJi.setWareDw(hashMap.get(ConstantUtils.Map.STEP5_4_GOODS_DW));
            xiaJi.setWareDj(hashMap.get(ConstantUtils.Map.STEP5_5_GOODS_DJ));
            xiaJi.setWareZj(hashMap.get(ConstantUtils.Map.STEP5_6_GOODS_ZJ));
            xiaJi.setBeUnit(hashMap.get(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT));// 包装单位代码或计量单位代码
            xiaJi.setWareGg(hashMap.get(ConstantUtils.Map.STEP5_16_GOODS_GG));//规格
            xiaJi.setRemark(hashMap.get(ConstantUtils.Map.STEP5_17_GOODS_REMARK));//备注
//            String xsTypeStr = hashMap.get(ConstantUtils.Map.STEP5_2_GOODS_XSTP);
            xiaJi.setXsTp("正常销售");
            list.add(xiaJi);
        }
        if(list!=null && list.size()>0){
        }else{
            ToastUtils.showLongCustomToast("至少选择一样商品");
            return;
        }
        String jsonStr=JSON.toJSONString(list);//拼接的json字符串
        XLog.e("jsonStr", jsonStr);
        //
        String shrStr = edit_shr.getText().toString().trim();
        String phoneStr = edit_phone.getText().toString().trim();
        String addressStr = edit_address.getText().toString().trim();
        if(MyUtils.isEmptyString(addressStr)){
            ToastUtils.showCustomToast("请输入地址");
            return;
        }
        String remoStr = edit_remo.getText().toString().trim();
        String zdzkStr = edit_zdzk.getText().toString().trim();
        String zjeStr = edit_zje.getText().toString().trim();
        String cjjeStr = edit_cjje.getText().toString().trim();
        getP().addData(context,companyId,jsonStr,shrStr,phoneStr,addressStr,remoStr,zjeStr,zdzkStr,cjjeStr);
    }


    /**
     * 下单提交数据成功
     */
    public void resultDataSuccessOrder(){
        //退出界面：清除公用数据
        Constans.xsList.clear();
        //更新“供货商”的徽章
        SupplierCacheGoodsListEvent event=new SupplierCacheGoodsListEvent();
        event.setCount(0);
        BusProvider.getBus().post(event);
        Router.pop(context);
    }

    //刷新列表数据
    private void refreshListData() {
        if (Constans.xsList.size() > 0) {// 线
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
        setRepeatMap();//设置标记商品的个数（重复商品颜色变）
        xdAdapter.notifyDataSetChanged();
        getSumMoney();//总价求和
    }



    /**
     * /显示订单信息
     */
    private QueryBforderBean mBean;
    public void resultDataOrderInfo(QueryBforderBean data){
        mBean = data;
        edit_shr.setText(data.getShr());
        edit_phone.setText(data.getTel());
        edit_address.setText(data.getAddress());
        edit_remo.setText(data.getRemo());
        edit_zje.setText(String.valueOf(data.getZje()));
        edit_cjje.setText(String.valueOf(data.getCjje()));
        mEtFreight.setText(String.valueOf(data.getFreight()));
        mEtOrderAmount.setText(String.valueOf(data.getOrderAmount()));
        orderZt =data.getOrderZt();// 审核状态

        //***********商品信息***************
        List<XiaJi> list = data.getList();
        Constans.xsList.clear();
        for (int i = 0; i < list.size(); i++) {
            XiaJi goodsInfo = list.get(i);
            // 品项，销售类型，数量，单位，单价，总价，操作
            HashMap<String, String> item = new HashMap<>();
            item.put(ConstantUtils.Map.STEP5_0_GOODS_ID, String.valueOf(goodsInfo.getWareId()));
            item.put(ConstantUtils.Map.STEP5_1_GOODS_NAME, goodsInfo.getWareNm());
            if (MyUtils.isEmptyString(goodsInfo.getXsTp())) {
                item.put(ConstantUtils.Map.STEP5_2_GOODS_XSTP, "点击选择");
            } else {
                item.put(ConstantUtils.Map.STEP5_2_GOODS_XSTP, goodsInfo.getXsTp());
            }
            item.put(ConstantUtils.Map.STEP5_3_GOODS_COUNT, String.valueOf(goodsInfo.getWareNum()));
            item.put(ConstantUtils.Map.STEP5_4_GOODS_DW, goodsInfo.getWareDw());
            item.put(ConstantUtils.Map.STEP5_5_GOODS_DJ, String.valueOf(goodsInfo.getWareDj()));
            item.put(ConstantUtils.Map.STEP5_5_GOODS_DJ_TEMP, String.valueOf(goodsInfo.getWareDj()));
            item.put(ConstantUtils.Map.STEP5_6_GOODS_ZJ, String.valueOf(goodsInfo.getWareZj()));
//            item.put(ConstantUtils.Map.STEP5_6_GOODS_ZJ, String.valueOf(goodsInfo.getWareNum()*goodsInfo.getWareDj()));
            item.put(ConstantUtils.Map.STEP5_7_GOODS_DEL, "删除");
            item.put(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT, goodsInfo.getBeUnit());
            item.put(ConstantUtils.Map.STEP5_16_GOODS_GG, goodsInfo.getWareGg());
            item.put(ConstantUtils.Map.STEP5_17_GOODS_REMARK, goodsInfo.getRemark());//备注
            Constans.xsList.add(item);
        }
        refreshListData();
    }

    //删除订单成功
    public void resultDataDelSuccess(){
        Router.pop(context);
        //退出界面：清除公用数据
        Constans.xsList.clear();
        BusProvider.getBus().post(new BaseEvent());
    }


    //TODO==============================提取的方法：开始================================================
    //清空列表
    private void clearList() {
        Constans.xsList.clear();
        refreshListData();
        edit_zdzk.setText("");
        edit_zdzk_percent.setText("");
    }

    /**
     * 隐藏显示：收货人，地址，电话
     */
    private void showOrHideMore() {
        if (ll_hide.getVisibility() == View.VISIBLE) {
            ll_hide.setVisibility(View.GONE);
            img_more.setImageResource(R.drawable.icon_jia);
        } else {
            ll_hide.setVisibility(View.VISIBLE);
            img_more.setImageResource(R.drawable.icon_jian);
        }
    }

    /**
     * dialog-删除订单
     */
    private void dialogNormalStyleDelOrder() {
        final NormalDialog dialog = new NormalDialog(context);
        dialog.setCanceledOnTouchOutside(false);//外部点击不消失
        dialog
                .isTitleShow(true)//是否需要标题
                .style(NormalDialog.STYLE_TWO)//标题风格二(标题居中，没有线,内容水平居中)，默认风格一
                .title("温馨提示")
                .titleTextColor(Color.parseColor("#333333"))
                .titleTextSize(18)
                .content("是否删除该订单？")
                .contentTextColor(Color.parseColor("#0082CE"))
                .contentTextSize(15)
                .contentGravity(Gravity.CENTER_VERTICAL)//内容显示位置（风格二时，居中）
                .bgColor(Color.parseColor("#f1f1f2"))//背景颜色
                .cornerRadius(5)//父布局的圆角
//				.dividerColor(Color.parseColor("#ff0000"))//"确定"，“取消”，“中间”三按钮分割线的颜色
                .widthScale(0.8f)//布局宽度占屏幕的比例
                .btnNum(2)//按钮数量（默认两按钮，确定"，“取消”）
                .btnText("取消","删除")
                .btnTextColor(Color.parseColor("#666666"),Color.parseColor("#666666"))
                .btnTextSize(15,15)
                .btnPressColor(Color.parseColor("#f4f4f4"))//按钮按下的背景颜色
                .show();
        //"确定"，“取消”，“中间”三按钮的点击事件
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        getP().deleteOrder(context,String.valueOf(orderId),companyId);
                    }
                });
    }

    /**
     * 显示地址信息
     */
    public void resultShowAddressInfo(ShopAddressInfo data) {
        edit_shr.setText(data.getLinkman());
        edit_phone.setText(data.getMobile());
        edit_address.setText(data.getAddress());
        shr=data.getLinkman();
        phone=data.getMobile();
        address=data.getAddress();
    }









}
