package com.qwb.view.order.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwb.view.order.parsent.POrderDetail;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.utils.MyUtils;
import com.xmsx.cnlife.widget.MyTableListView;
import com.qwb.view.step.model.QueryBforderBean;
import com.qwb.view.step.model.XiaJi;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 创建描述：订单详情
 */
public class OrderDetailActivity extends XActivity<POrderDetail> {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_order_detail;
    }

    @Override
    public POrderDetail newP() {
        return new POrderDetail();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initIntent();
        initUI();
        getP().loadDataDhOrderInfo(context,orderId);
    }

    /**
     * 初始化Intent
     */
    private int xdType;//
    private int orderId;//订单id
    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            xdType = intent.getIntExtra(ConstantUtils.Intent.ORDER_TYPE, 1);
            orderId = intent.getIntExtra(ConstantUtils.Intent.ORDER_ID, 0);
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

    /**
     * 头部
     */
    private TextView tv_headTitle;
    private TextView tv_headRight;
    private void initHead() {
        OtherUtils.setStatusBarColor(context);//设置状态栏颜色，透明度
        findViewById(R.id.iv_head_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.pop(context);
            }
        });
        tv_headTitle = (TextView) findViewById(R.id.tv_head_title);
        tv_headRight = (TextView) findViewById(R.id.tv_head_right);
        tv_headTitle.setText("订单详情");
    }

    /**
     * 基本
     */
    private ImageView img_more;//显示隐藏（收货人-电话--地址）
    private LinearLayout ll_hide;
    private TextView tvPhone;
    private TextView tvAddress;
    private TextView tvRemo;//备注
    private TextView tvShr;//收货人
    private TextView tvShTime;//送货时间
    private TextView tvPszd;//配送类型
    private TextView tvZje;//总金额
    private TextView tvZdzk;//整单折扣
    private TextView tvCjje;//交易金额
    private void initBaseView() {
        //收货人-电话--地址--备注
        img_more = (ImageView) findViewById(R.id.img_more);// 收货人-电话--地址(默认隐藏)
        ll_hide = (LinearLayout) findViewById(R.id.ll_hide);//收货人-电话--地址( 默认隐藏)
        tvShr = (TextView) findViewById(R.id.tv_shr);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvRemo = (TextView) findViewById(R.id.tv_remo);
        //送货时间;配送指定
        tvShTime = (TextView) findViewById(R.id.tv_shTime);// 送货时间
        tvPszd = (TextView) findViewById(R.id.tv_pszd);  //配送指定（公司直送，转二批配送）
        //总金额；整单折扣；交易金额
        tvZje = (TextView) findViewById(R.id.tv_zje);
        tvZdzk = (TextView) findViewById(R.id.tv_zdzk);
        tvCjje = (TextView) findViewById(R.id.tv_cjje);
    }

    /**
     * 适配器--添加商品
     */
    private View line;
    private HorizontalScrollView hs;
    private int[] columns;
    private AdapterOrderDetail adapterOrderDetail;// 订货下单--适配器
    public static ArrayList<HashMap<String, String>> xsList= new ArrayList<HashMap<String, String>>();
    private void initAdapter() {
        // 添加商品
        line = (View) findViewById(R.id.line);// 线
        hs = (HorizontalScrollView) findViewById(R.id.hs);
        String[] from = new String[] { ConstantUtils.Map.STEP5_1_GOODS_NAME ,ConstantUtils.Map.STEP5_16_GOODS_GG,
                ConstantUtils.Map.STEP5_2_GOODS_XSTP, ConstantUtils.Map.STEP5_3_GOODS_COUNT,
                ConstantUtils.Map.STEP5_4_GOODS_DW, ConstantUtils.Map.STEP5_5_GOODS_DJ,
                ConstantUtils.Map.STEP5_6_GOODS_ZJ, ConstantUtils.Map.STEP5_17_GOODS_REMARK,ConstantUtils.Map.STEP5_7_GOODS_DEL };// 标题:内容--id
        columns = new int[] { R.id.column1, R.id.column8, R.id.column2, R.id.column3,R.id.column4, R.id.column5, R.id.column6,R.id.column17, R.id.column7 };
        adapterOrderDetail = new AdapterOrderDetail(this, xsList,  R.layout.x_head_item_xiadang, from, columns, R.color.difColor,  R.color.difColor1);
        MyTableListView myLisView = new MyTableListView(context, hs, columns,R.id.hs, R.id.list, R.id.head, adapterOrderDetail);
        // 标题栏颜色
        myLisView.setHeadColor(getResources().getColor(R.color.white));
    }

    /**
     * 拜访流程--订货下单的适配器
     * */
    public class AdapterOrderDetail extends BaseAdapter {
        private String[] mFrom;
        private List<? extends Map<String, String>> mData;
        private LayoutInflater mInflater;
        int[] difColor = new int[2];

        public AdapterOrderDetail(Context context, List<? extends Map<String, String>> data, int resource,
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
            final View root = mInflater.inflate(R.layout.x_adapter_xsxj_xiadan,null);
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
            tv1.setText((position+1)+": "+map.get(mFrom[0]));//拼接序号+品项
            tv_gg.setText(map.get(mFrom[1]));// 规格
            tv_xsType.setText(map.get(mFrom[2]));// 销售类型
            tv_count.setText(map.get(mFrom[3]));// 数量
            tv_danwei.setText(map.get(mFrom[4]));// 单位
            tv_remark.setText(map.get(mFrom[7]));// 操作--备注
            tv_chaozuo.setText(map.get(mFrom[8]));// 操作--删除
            //单价;总价：数据不变，显示保留两位小数点
            try{
                double dj=Double.parseDouble(map.get(mFrom[5]));
                double sum=Double.parseDouble(map.get(mFrom[6]));
                tv_danjia.setText(""+ MyDoubleUtils.getDecimal(dj));// 单价
                tv_zongjia.setText(""+MyDoubleUtils.getDecimal(sum));// 总价
            }catch (Exception e){
            }
            return root;
        }
    }


    /**
     * 点击事件
     */
    @OnClick({R.id.img_more})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_more://隐藏显示：收货人，地址，电话
                showOrHideMore();
                break;
        }
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
     * /显示订单信息
     */
    public void resultOrderInfo(QueryBforderBean data){
        tvShr.setText(data.getShr());
        tvPhone.setText(data.getTel());
        tvAddress.setText(data.getAddress());
        tvRemo.setText(data.getRemo());
        tvShTime.setText(""+data.getShTime());
        tvPszd.setText(""+data.getPszd());
        tvZje.setText(""+data.getZje());
        tvCjje.setText(""+data.getCjje());
        tvZdzk.setText(""+data.getZdzk());

        //***********商品信息***************
        List<XiaJi> list = data.getList();
        xsList.clear();
        for (int i = 0; i < list.size(); i++) {
            XiaJi xiaJi = list.get(i);
            // 品项，销售类型，数量，单位，单价，总价，操作
            HashMap<String, String> item = new HashMap<String, String>();
            item.put(ConstantUtils.Map.STEP5_0_GOODS_ID, String.valueOf(xiaJi.getWareId()));
            item.put(ConstantUtils.Map.STEP5_1_GOODS_NAME, xiaJi.getWareNm());
            if (MyUtils.isEmptyString(xiaJi.getXsTp())) {
                item.put(ConstantUtils.Map.STEP5_2_GOODS_XSTP, "点击选择");
            } else {
                item.put(ConstantUtils.Map.STEP5_2_GOODS_XSTP, xiaJi.getXsTp());
            }
            item.put(ConstantUtils.Map.STEP5_3_GOODS_COUNT, xiaJi.getWareNum());
            item.put(ConstantUtils.Map.STEP5_4_GOODS_DW, xiaJi.getWareDw());
            item.put(ConstantUtils.Map.STEP5_5_GOODS_DJ, xiaJi.getWareDj());
            item.put(ConstantUtils.Map.STEP5_5_GOODS_DJ_TEMP, xiaJi.getWareDj());
            item.put(ConstantUtils.Map.STEP5_6_GOODS_ZJ, xiaJi.getWareZj());
            item.put(ConstantUtils.Map.STEP5_7_GOODS_DEL, "");
//            item.put(ConstantUtils.Map.STEP5_7_GOODS_DEL, "删除");
            item.put(ConstantUtils.Map.STEP5_7_GOODS_DEL, "");
            item.put(ConstantUtils.Map.STEP5_8_GOODS_BEUNIT, xiaJi.getBeUnit());
            item.put(ConstantUtils.Map.STEP5_16_GOODS_GG, xiaJi.getWareGg());
            item.put(ConstantUtils.Map.STEP5_17_GOODS_REMARK, xiaJi.getRemark());//备注
            xsList.add(item);
        }
        refreshListData();
    }

    //刷新列表数据
    private void refreshListData() {
        if (xsList.size() > 0) {// 线
            line.setVisibility(View.VISIBLE);
        } else {
            line.setVisibility(View.GONE);
        }
        adapterOrderDetail.notifyDataSetChanged();
    }

}
