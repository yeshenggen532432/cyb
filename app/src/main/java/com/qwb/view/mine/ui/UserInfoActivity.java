package com.qwb.view.mine.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.qwb.view.mine.parsent.PUserInfo;
import com.qwb.utils.MyImageUtil;
import com.qwb.utils.ActivityManager;
import com.qwb.utils.ConstantUtils;
import com.qwb.utils.OtherUtils;
import com.qwb.utils.Constans;
import com.qwb.utils.MyPopWindowManager;
import com.qwb.utils.MyPopWindowManager.OnImageBack;
import com.qwb.utils.SPUtils;
import com.qwb.view.mine.model.UserInfoResult;
import com.qwb.utils.MyGlideUtil;
import com.qwb.utils.MyStatusBarUtil;
import com.chiyong.t3.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * 用户信息
 */
public class UserInfoActivity extends XActivity<PUserInfo> {
    private String memberHead;

    @Override
    public int getLayoutId() {
        return R.layout.x_activity_user_info;
    }

    @Override
    public PUserInfo newP() {
        return new PUserInfo();
    }


    @Override
    public void initData(Bundle savedInstanceState) {
        initUI();
        getP().getUserInfo(context);
    }

    @BindView(R.id.tv_head_title)
    TextView mTvHeadTitle;
    @BindView(R.id.iv_user_head)
    ImageView mIvUserHead;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_user_phone)
    TextView mTvUserPhone;
    @BindView(R.id.tv_email)
    TextView mTvEmail;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_address)
    TextView mTvAddress;
    @BindView(R.id.tv_company)
    TextView mTvCompany;
    @BindView(R.id.tv_branch)
    TextView mTvBranch;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
    private void initUI() {
        MyStatusBarUtil.getInstance().setColorWhite(context);
        mTvHeadTitle.setText("个人信息");
    }


    @OnClick({R.id.head_left,R.id.view_user_name, R.id.view_email, R.id.view_address, R.id.view_job, R.id.view_sign, R.id.view_sex, R.id.iv_user_head, R.id.tv_update_head})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_left:
                ActivityManager.getInstance().closeActivity(context);
                break;
            case R.id.view_user_name:
                toUserUpdateActivity("memberNm", mTvUserName.getText().toString().trim(), "姓名");
                break;
            case R.id.view_email:
                toUserUpdateActivity("email", mTvEmail.getText().toString().trim(), "邮箱");
                break;
            case R.id.view_address:
                toUserUpdateActivity("memberHometown", mTvAddress.getText().toString().trim(), "地址");
                break;
            case R.id.view_job:
                toUserUpdateActivity("memberJob", mTvJob.getText().toString().trim(), "职位");
                break;
            case R.id.view_sign:
                toUserUpdateActivity("memberDesc", mTvSign.getText().toString().trim(), "个性签名");
                break;
            case R.id.view_sex:
                showDialogSex();
                break;
            case R.id.iv_user_head:
                String[] urls = new String[1];
                urls[0] = Constans.IMGROOTHOST + memberHead;
                MyImageUtil.getInstance().zoomImage(context, urls, 0);
                break;
            case R.id.tv_update_head:
                MyPopWindowManager.getI().show(context, new OnImageBack() {
                    @Override
                    public void fromCamera() {
                        MyImageUtil.getInstance().getImageFromCamera(context);
                    }

                    @Override
                    public void fromPhotoAlbum() {
                        MyImageUtil.getInstance().getImageFromAlbum(context);
                    }
                }, "", "");
                break;
            default:
                break;
        }
    }

    public void showDialogSex(){
        final String[] items = {"男","女"};
        NormalListDialog dialog = new NormalListDialog(context,items);
        dialog.title("选择性别").show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sex = mTvSex.getText().toString().trim();
                String s = items[position];
                if(!s.equals(sex)){
                    mTvSex.setText(s);
                    String input = "1";
                    if("女".equals(s)){
                        input = "2";
                    }
                    getP().updateUserSex(context, input);
                }
            }
        });
    }

    /**
     * 显示：用户信息
     */
    public void doUserInfo(UserInfoResult bean) {
        UserInfoResult.UserInfoBean sysMember = bean.getSysMember();
        SPUtils.setValues(ConstantUtils.Sp.USER_NAME, sysMember.getMemberName());
        memberHead = sysMember.getMemberHead();
        SPUtils.setValues(ConstantUtils.Sp.USER_HEAD, memberHead);
        MyGlideUtil.getInstance().displayImageRound(Constans.IMGROOTHOST + memberHead, mIvUserHead);
        mTvUserName.setText(sysMember.getMemberNm());
        mTvUserPhone.setText(sysMember.getMemberMobile());
        mTvAddress.setText(sysMember.getMemberHometown());
        mTvCompany.setText(sysMember.getMemberCompany());
        mTvJob.setText(sysMember.getMemberJob());
        mTvSign.setText(sysMember.getMemberDesc());
        mTvEmail.setText(sysMember.getEmail());
        String sex = sysMember.getSex();
        if ("1".equals(sex)) {
            mTvSex.setText("男");
        }
        if ("2".equals(sex)) {
            mTvSex.setText("女");
        }
        mTvBranch.setText(sysMember.getBranchName());

    }

    /**
     * 跳到用户修改界面
     */
    private void toUserUpdateActivity(String key, String value, String title) {
        Router.newIntent(context)
                .putString(ConstantUtils.Intent.KEY, key)
                .putString(ConstantUtils.Intent.VALUE, value)
                .putString(ConstantUtils.Intent.TITLE, title)
                .to(UserUpdateActivity.class)
                .requestCode(0)
                .launch();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //修改用户头像
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            if (images != null) {
                for (int i = 0; i < images.size(); i++) {
                    Constans.headUrl = images.get(i).path;
                }
                //相机-记录拍照的集合（要删除拍照图片）
                if (requestCode == Constans.TAKE_PIC_XJ) {
                    for (int j = 0; j < images.size(); j++) {
                        Constans.publish_pics_xj.add(images.get(j).path);
                    }
                }
                getP().updateUserHead(context, new File(Constans.headUrl));
            }
        }

        //修改用户信息
        if (resultCode == ConstantUtils.Intent.REQUEST_CODE_UPDATE_SUCCESS) {
            getP().getUserInfo(context);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OtherUtils.resetStepStatus(context);//重置默认状态,删除图片
    }


}
