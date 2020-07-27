package com.xmsx.cnlife.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;
import com.qwb.view.tree.SimpleTreeAdapter_map;
import com.qwb.view.base.model.TreeBean;
import com.chiyong.t3.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyChooseMemberDialog extends BaseDialog<MyChooseMemberDialog> {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_ok)
    Button mBtnOk;
    @BindView(R.id.btn_reset)
    Button mBtnReset;
    @BindView(R.id.tree_member)
    ListView mMemberTree;

    private Context mContext;
    private List<TreeBean> mTreeDatas = new ArrayList<>();
    private SimpleTreeAdapter_map<TreeBean> mMemberTreeAdapter;
    public MyChooseMemberDialog(Context context, List<TreeBean> treeDatas) {
        super(context);
        this.mContext = context;
        this.mTreeDatas = treeDatas;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
//        showAnim(new Swing());
        View inflate = View.inflate(mContext, R.layout.x_dialog_tree, null);
        ButterKnife.bind(this, inflate);
        inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        try {
            mMemberTreeAdapter = new SimpleTreeAdapter_map<>(mMemberTree, mContext, mTreeDatas, 0, false);
            mMemberTree.setAdapter(mMemberTreeAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
