package com.qwb.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyyoona7.lib.BaseCustomPopup;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;


/**
 * popup：自定义头部菜单
 */

public class MyMenuPopup extends BaseCustomPopup {


    private List<String> items = new ArrayList<>();
    private Context mContext;

    public MyMenuPopup(Context context, List<String> items) {
        super(context);
        this.mContext = context;
        this.items = items;
    }

    public MyMenuPopup(Context context, String[] items) {
        super(context);
        this.mContext = context;
        if (items != null) {
            for (String s : items) {
                this.items.add(s);
            }
        }
    }


    @Override
    protected void initAttributes() {
        setContentView(R.layout.x_popup_custom_menu, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);
    }

    private RecyclerView recyclerView;

    @Override
    protected void initViews(View view) {
        recyclerView = getView(R.id.popup_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        Adapter adapter = new Adapter();
        adapter.setNewData(items);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (listener != null) {
                    listener.setOnItemClickListener(items.get(position), position);
                }
//                dismiss();
            }
        });
    }


    public class Adapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public Adapter() {
            super(R.layout.x_adapter_popup_custom_menu);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.item_tv, item);// 客户名称
        }
    }


    public interface OnItemClickListener {
        void setOnItemClickListener(String text, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


}
