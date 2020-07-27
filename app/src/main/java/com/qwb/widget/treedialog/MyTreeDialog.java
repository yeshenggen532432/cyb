package com.qwb.widget.treedialog;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.utils.MyCollectionUtil;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.ToastUtils;
import com.qwb.utils.MyStringUtil;
import com.qwb.view.base.model.TreeBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyTreeDialog extends BaseDialog<MyTreeDialog> {
    private String mTitle = "温馨选择";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.layout_ok)
    View mLayoutOk;
    @BindView(R.id.layout_reset)
    View mLayoutReset;
    @BindView(R.id.layout_cancel)
    View mLayoutCancel;
    @BindView(R.id.tree_member)
    ListView mTreeListView;

    private Context mContext;
    private List<TreeBean> mTreeDatas = new ArrayList<>();
    private MyTreeAdapter<TreeBean> mTreeAdapter;
    private HashMap<Integer, Integer> mCheckMap = new HashMap<>();//第一个Integer:id；第二个Integer:0没选中,1选中,2部分选中
    private boolean multiple = true;
    public MyTreeDialog(Context context, List<TreeBean> treeDatas) {
        super(context);
        this.mContext = context;
        this.mTreeDatas = treeDatas;
    }

    public MyTreeDialog(Context context, List<TreeBean> treeDatas, boolean multiple) {
        super(context);
        this.mContext = context;
        this.mTreeDatas = treeDatas;
        this.multiple = multiple;
    }

    public MyTreeDialog(Context context, List<TreeBean> treeDatas, HashMap<Integer, Integer> checkMap) {
        super(context);
        this.mContext = context;
        this.mTreeDatas = treeDatas;
        this.mCheckMap = checkMap;
    }
    public MyTreeDialog(Context context, List<TreeBean> treeDatas, Map<Integer, Integer> checkMap, boolean multiple) {
        super(context);
        this.mContext = context;
        this.mTreeDatas = treeDatas;
        this.multiple = multiple;
        if(null != checkMap && !checkMap.isEmpty()){
            this.mCheckMap.putAll(checkMap);
        }
    }

    @Override
    public View onCreateView() {
        //宽度80%
        widthScale(0.80f);
        //外部点击不消失
        setCanceledOnTouchOutside(false);
        //动画
//        showAnim(new Tada());

        View inflate = View.inflate(mContext, R.layout.x_dialog_tree, null);
        ButterKnife.bind(this, inflate);
        //画圆角背景
        inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        try {
            mTreeAdapter = new MyTreeAdapter<>(mTreeListView, mContext, mTreeDatas, mCheckMap, 0, multiple);
            mTreeListView.setAdapter(mTreeAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return inflate;
    }

    public MyTreeDialog title(String title) {
        this.mTitle = title;
        return this;
    }

    @Override
    public void setUiBeforShow() {
        if(!MyStringUtil.isEmpty(mTitle)){
            mTvTitle.setText(mTitle);
        }
        mLayoutOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTreeAdapter.setTempMap(mTreeAdapter.getCheckMap());
                dismiss();
                if(null != listener){
                    listener.onOkListener(getContainPIds(), getNoContainPIds(), mTreeAdapter.getCheckMap());
                }
            }
        });
        mLayoutReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Integer, Integer> tempMap = new HashMap<>();
                mTreeAdapter.setCheckMap(tempMap);
                mTreeAdapter.notifyDataSetChanged();
            }
        });
        mLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTreeAdapter.setCheckMap(mTreeAdapter.getTempMap());
                mTreeAdapter.notifyDataSetChanged();
                dismiss();
            }
        });
    }

    public interface OnClickListener{
        void onOkListener(String containPIds, String noContainPIds, Map<Integer, Integer> checkMap);
    }

    private OnClickListener listener;
    public void setOnClickListener(OnClickListener listener){
        this.listener = listener;
    }


    /**
     * 获取选中的id(只包含员工id的，不包含部门id)
     */
    public String getContainPIds(){
        String ids = "";
        try {
            Map<Integer, Integer> checkMap = mTreeAdapter.getCheckMap();
            if(MyCollectionUtil.isNotEmpty(checkMap)){
                Iterator<Map.Entry<Integer, Integer>> it = checkMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, Integer> entry = it.next();
                    Integer key = entry.getKey();
                    Integer value = entry.getValue();
                    //key > ConstantUtils.TREE_ID
                    if(null != key && key > ConstantUtils.TREE_ID && null != value && value == 1){
                        if(MyStringUtil.isEmpty(ids)){
                            ids += (key - ConstantUtils.TREE_ID);
                        }else{
                            ids += "," + (key - ConstantUtils.TREE_ID);
                        }
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return ids;
    }
    /**
     * 获取选中的id(只包含员工id的，不包含部门id)
     */
    public String getNoContainPIds(){
        String ids = "";
        try {
            Map<Integer, Integer> checkMap = mTreeAdapter.getCheckMap();
            if(null != checkMap && !checkMap.isEmpty()){
                Iterator<Map.Entry<Integer, Integer>> it = checkMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Integer, Integer> entry = it.next();
                    Integer key = entry.getKey();
                    Integer value = entry.getValue();
                    if(null != key && (key > 0 && key < ConstantUtils.TREE_ID) && null != value && value == 1){
                        if(MyStringUtil.isEmpty(ids)){
                            ids += key;
                        }else{
                            ids += "," + key;
                        }
                    }
                }
            }
        }catch (Exception e){
            ToastUtils.showError(e);
        }
        return ids;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //屏蔽返回键关闭dialog
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
