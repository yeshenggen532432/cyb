package com.qwb.view.checkstorage.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qwb.utils.MyDoubleUtils;
import com.qwb.utils.MyTimeUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.view.checkstorage.model.StkCheckWareBean;
import com.qwb.utils.MyStringUtil;
import com.xmsx.cnlife.view.widget.MyAfterTextWatcher;
import com.qwb.widget.MyDatePickerDialog;
import com.xmsx.qiweibao.R;
import com.xw.repo.XEditText;

import java.util.HashMap;
import java.util.Map;
/**
 * 盘点库存
 */
public class StkCheckAdapter extends BaseQuickAdapter<StkCheckWareBean, BaseViewHolder> {

    private Context context;
    private boolean addState = true;
    private int tipPosition = -1;//提示的（重复的，错误的）
    private Map<Integer, Integer> wareIdMap = new HashMap<>();//记录重复商品，key:商品id,value:商品的个数

    public StkCheckAdapter(Context context) {
        super(R.layout.x_adapter_check_storage);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final StkCheckWareBean item) {
        helper.setIsRecyclable(false);//不复用
        //点击事件
        helper.addOnClickListener(R.id.item_layout_ddd);
        helper.addOnClickListener(R.id.item_tv_name);
        helper.addOnClickListener(R.id.right);
        helper.addOnClickListener(R.id.item_tv_produce_date);

        final int position = helper.getAdapterPosition();
//        //赋值
        helper.setText(R.id.item_tv_sort, "" + (helper.getAdapterPosition() + 1));
        TextView tvSort = helper.getView(R.id.item_tv_sort);
        //重复商品：颜色区分
        Integer wareId = item.getWareId();
        if (null != wareId && 0 != wareId) {
            Integer num = wareIdMap.get(wareId);
            if (null != num && num > 1) {
                tvSort.setBackgroundColor(context.getResources().getColor(R.color.x_c_green_179));
            } else {
                tvSort.setBackgroundColor(context.getResources().getColor(R.color.x_main_color));
            }
        } else {
            tvSort.setBackgroundColor(context.getResources().getColor(R.color.x_main_color));
        }
        //提示的（重复的，错误的）
        if (tipPosition == position) {
            tvSort.setBackgroundColor(context.getResources().getColor(R.color.x_c_red_e62));
        }


        TextView tvName = helper.getView(R.id.item_tv_name);
        XEditText etMax = helper.getView(R.id.item_et_max);
        TextView tvMaxName = helper.getView(R.id.item_tv_max_name);
        XEditText etMin = helper.getView(R.id.item_et_min);
        TextView tvMinName = helper.getView(R.id.item_tv_min_name);
        View layoutMinName = helper.getView(R.id.item_layout_min_name);
        XEditText etStkQty = helper.getView(R.id.item_et_stk_qty);
        final XEditText etDisQty = helper.getView(R.id.item_et_dis_qty);
        View layout = helper.getView(R.id.item_layout_ddd);
        final TextView tvProduceDate = helper.getView(R.id.item_tv_produce_date);

        Double maxQty = item.getQty();
        Double minQty = item.getMinQty();
        tvName.setText(item.getWareNm());
        etMax.setText(MyDoubleUtils.getDoubleToStr(maxQty));
        etMin.setText(MyDoubleUtils.getDoubleToStr(minQty));
        tvProduceDate.setText(item.getProduceDate());

        String hsNum = item.getHsNum();
        String stkQty = item.getStkQty();
        String disQty = item.getDisQty();
        String maxName = item.getUnitName();
        String minName = item.getMinUnit();
        etStkQty.setText(doNumToDw(hsNum, stkQty, maxName, minName));
        etDisQty.setText(doNumToDw(hsNum, disQty, maxName, minName));

        if (MyStringUtil.isEmpty(maxName)) {
            tvMaxName.setText("盘点数量");
        } else {
            tvMaxName.setText(maxName);
        }

        if (MyStringUtil.isEmpty(minName) || "S".equals(minName)) {
            layoutMinName.setVisibility(View.GONE);
            tvMinName.setText("小单位数量");
        } else {
            layoutMinName.setVisibility(View.VISIBLE);
            tvMinName.setText(minName);
        }

        if (addState) {
            tvName.setEnabled(true);
            etMax.setEnabled(true);
            etMin.setEnabled(true);
            layout.setVisibility(View.VISIBLE);
        } else {
            tvName.setEnabled(false);
            etMax.setEnabled(false);
            etMin.setEnabled(false);
            etMax.clearFocus();
            tvName.requestFocus();
            layout.setVisibility(View.GONE);
        }

        etMax.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    StkCheckWareBean bean = getData().get(position);
                    bean.setQty(MyDoubleUtils.getStringToDouble(editable.toString()));
                    getData().set(position, bean);
                    //处理差量
                    doDisQty(bean, position, etDisQty);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        etMin.addTextChangedListener(new MyAfterTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    StkCheckWareBean bean = getData().get(position);
                    bean.setMinQty(MyDoubleUtils.getStringToDouble(editable.toString()));
                    getData().set(position, bean);
                    //处理差量
                    doDisQty(bean, position, etDisQty);
                } catch (Exception e) {
                    ToastUtils.showError(e);
                }
            }
        });
        tvProduceDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogProduceDate(tvProduceDate, position);
            }
        });
    }

    private void doDisQty(StkCheckWareBean bean, int position, XEditText etDisQty) {
        try {
            Integer wareId = bean.getWareId();
            if (null == wareId || 0 == wareId) {
                return;
            }
            double sum = 0;
            String stkQty = bean.getStkQty();
            Double qty = bean.getQty();
            Double minQty = bean.getMinQty();
            String hsNum = bean.getHsNum();
            String maxName = bean.getUnitName();
            String minName = bean.getMinUnit();
            sum += MyDoubleUtils.getDoubleToDouble(qty);
            if(!MyStringUtil.isEmpty(hsNum) && !"0".equals(hsNum)){
                sum += MyDoubleUtils.divide(MyDoubleUtils.getDoubleToDouble(minQty), MyDoubleUtils.getStringToDouble(hsNum));
            }
            String disQty = "" + (MyDoubleUtils.subtract(sum, MyDoubleUtils.getStringToDouble(stkQty)));
            bean.setDisQty(disQty);
            getData().set(position, bean);
            String s = doNumToDw(hsNum, disQty, maxName, minName);
            etDisQty.setText(s);
        } catch (Exception e) {
            ToastUtils.showError(e);
        }
    }

    /**
     * 数量拼接单位
     */
    @NonNull
    private String doNumToDw(String hsNum, String sum, String maxName, String minName) {
        String s = "";
        try {
            if (!MyStringUtil.isEmpty(sum)) {
                Double sumD = MyDoubleUtils.getStringToDouble(sum);
                if(sumD < 0){
                    s += "-";
                }
                double abs = Math.abs(sumD);//绝对值
                int max = (int) abs;
                int min = 0;
                if (!MyStringUtil.isEmpty(minName) && !"S".equals(minName) ) {
                    double minD = ((MyDoubleUtils.subtract(abs, max)) * MyDoubleUtils.getStringToDouble(hsNum));
                    min = MyDoubleUtils.intRound(minD);
                }
                s += max + maxName;
                if (min > 0) {
                    s += min + minName;
                }
            }
        } catch (Exception e) {
            ToastUtils.showError(e);
        } finally {
            return s;
        }
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isAddState() {
        return addState;
    }

    public void setAddState(boolean addState) {
        this.addState = addState;
    }

    public Map<Integer, Integer> getWareIdMap() {
        return wareIdMap;
    }

    public void setWareIdMap(Map<Integer, Integer> wareIdMap) {
        this.wareIdMap = wareIdMap;
    }

    public int getTipPosition() {
        return tipPosition;
    }

    public void setTipPosition(int tipPosition) {
        this.tipPosition = tipPosition;
    }


    //对话框：生产日期
    private void showDialogProduceDate(final TextView tvProduceDate, final int position) {
        try {
            String dateStr = tvProduceDate.getText().toString();
            if(MyStringUtil.isEmpty(dateStr)){
                dateStr = MyTimeUtils.getTodayStr();
            }
            String[] split = dateStr.split("-");
            int year = Integer.valueOf(split[0]);
            int month = Integer.valueOf(split[1]) - 1;
            int day = Integer.valueOf(split[2]);
            new MyDatePickerDialog(context, "生产日期", year, month, day, new MyDatePickerDialog.DateTimeListener() {
                @Override
                public void onSetTime(int year, int month, int day, String timeStr) {
                    tvProduceDate.setText(timeStr);
                    StkCheckWareBean bean = getData().get(position);
                    bean.setProduceDate(timeStr);
                    getData().set(position, bean);
                }

                @Override
                public void onCancel() {
                }
            }).show();
        }catch (Exception e){
        }
    }

}
