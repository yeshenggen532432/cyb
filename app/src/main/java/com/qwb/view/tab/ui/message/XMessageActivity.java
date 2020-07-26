package com.qwb.view.tab.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qwb.view.tab.ui.XMessageFragment;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.MyStatusBarUtil;
import com.xmsx.qiweibao.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 消息页面
 */
public class XMessageActivity extends XActivity {

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_message;
    }

    @Override
    public Object newP() {
        return null;
    }

    public void initData(Bundle savedInstanceState) {
        initUI();
    }

    private void initUI() {
        initIntent();
        initHead();
        initFragment();
    }

    public String bankuai;
    public String title;
    private void initIntent(){
        Intent intent = context.getIntent();
        if(null != intent){
            bankuai =  intent.getStringExtra(ConstantUtils.Intent.BANKUAI);
            title =  intent.getStringExtra(ConstantUtils.Intent.TITLE);
        }
    }

    @BindView(R.id.head)
    View mHead;
    @BindView(R.id.head_left)
    View mHeadLeft;
    @BindView(R.id.tv_head_left)
    TextView mTvHeadLeft;
    @BindView(R.id.iv_head_left)
    ImageView mIvHeadLeft;
    @BindView(R.id.tv_head_right)
    TextView mTvHeadRight;
    @BindView(R.id.iv_head_right)
    ImageView mIvHeadRight;
    @BindView(R.id.tv_head_right2)
    TextView mTvHeadRight2;
    @BindView(R.id.iv_head_right2)
    ImageView mIvHeadRight2;
    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    private void initHead() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mHead.setBackgroundResource(R.color.white);
        mHeadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Router.pop(context);
            }
        });//返回
        mIvHeadLeft.setImageResource(R.mipmap.back_g);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.dp_8),(int)getResources().getDimension(R.dimen.dp_14));
        mIvHeadLeft.setLayoutParams(params);
        mTvHeadTitle.setText("消息");
        mTvHeadTitle.setTextColor(getResources().getColor(R.color.gray_3));
        if(!TextUtils.isEmpty(title)){
            mTvHeadTitle.setText(title);
        }
    }

    private XMessageFragment messageFragment;
    private void initFragment() {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (messageFragment == null) {
            messageFragment = new XMessageFragment();
            //给fragment传递参数
            Bundle bundle = new Bundle();
            bundle.putString(ConstantUtils.Intent.BANKUAI,bankuai);
            messageFragment.setArguments(bundle);
            transaction.add(R.id.fl_manager, messageFragment);
        } else {
            transaction.show(messageFragment);
        }
        transaction.commit();
    }


}
