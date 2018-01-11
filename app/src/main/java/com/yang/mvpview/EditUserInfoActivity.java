package com.yang.mvpview;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.yang.App;
import com.yang.BaseActivity;
import com.yang.MainActivity;
import com.yang.glidetransform.GlideCircleTransform;
import com.yang.mvppresenter.IPresenter;
import com.yang.net2request.Staff;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.utils.MultipartUtil;
import com.yang.utils.SharedprefUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import com.yang.utils.SHA;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import okhttp3.RequestBody;
import rx.Subscription;

/**
 * Created by Administrator on 2016/9/13.
 */
public class EditUserInfoActivity extends BaseActivity {
    @BindView(com.yang.R.id.menu)
    TextView menu;
    @BindView(com.yang.R.id.user_portrait)
    ImageView userPortrait;
    @BindView(com.yang.R.id.username)
    TextView username;
    @BindView(com.yang.R.id.tv_firstname)
    TextView tvFirstname;
    @BindView(com.yang.R.id.et_firstname)
    TextView etFirstname;
    @BindView(com.yang.R.id.tv_lastname)
    TextView tvLastname;
    @BindView(com.yang.R.id.et_lastname)
    TextView etLastname;
    @BindView(com.yang.R.id.email)
    TextView email;
    @BindView(com.yang.R.id.et_email)
    TextView etEmail;
    @BindView(com.yang.R.id.tv_mobile)
    TextView tvMobile;
    @BindView(com.yang.R.id.et_mobile)
    TextView etMobile;
    @BindView(com.yang.R.id.tv_company)
    TextView tvCompany;
    @BindView(com.yang.R.id.et_company)
    TextView etCompany;
    @BindView(com.yang.R.id.tv_username)
    TextView tvUsername;
    @BindView(com.yang.R.id.et_username)
    TextView etUsername;
    @BindView(com.yang.R.id.tv_password)
    TextView tvPassword;
    @BindView(com.yang.R.id.et_password)
    EditText etPassword;
    @BindView(com.yang.R.id.tv_save)
    TextView tvSave;
    @BindView(com.yang.R.id.tv_edit)
    TextView tvEdit;
    private static final String TAG = "EditUserInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserInfo();
        initEvent();
    }

    private void initEvent() {
        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    etPassword.setText("");
                    return false;
                }
                return false;
            }
        });
    }

    void getUserInfo() {
        RetrofitHttp.getInstance()
                .getUserInfo(App.app.getLoginUser().getUid() + "")
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(getUserInfo -> {
//                    Logger.e(getUserInfo.getMsg());
//                    Staff staff = getUserInfo.getStaff();
//                    Toast.makeText(EditUserInfoActivity.this, "getUserInfo success", Toast.LENGTH_SHORT).show();
//                    username.setText(staff.getUsername());
////                    Glide.with(this).load(HttpConstants.TEST_IMAGE_URL + staff.getPhotoUrl())
////                            .error(R.drawable.user_portrait)
////                            .centerCrop()
////                            .into(userPortrait);
//                    Glide.with(this).load(HttpConstants.TEST_URL + staff.getPhotoUrl()).bitmapTransform(new GlideCircleTransform(this)).crossFade(1000).error(R.drawable.user_portrait).into(userPortrait);
//                    Log.d(TAG, "getUserInfo: " + HttpConstants.TEST_URL + staff.getPhotoUrl());
//                    etFirstname.setText(staff.getFirstname());
//                    etLastname.setText(staff.getLastname());
//                    etCompany.setText(staff.getCompanyName());
//                    etEmail.setText(staff.getEmail());
//                    etMobile.setText(staff.getPhone());
//                    etUsername.setText(staff.getUsername());
//                    StringBuffer sb = new StringBuffer();
//                    for (int i = 0; i < SharedprefUtil.getInstance(EditUserInfoActivity.this).getInt("pwdSize", 6); i++) {
//                        sb.append(" ");
//                    }
//                    etPassword.setText(sb.toString());
                }, throwable -> {
                    Toast.makeText(EditUserInfoActivity.this, "getUserInfo fail", Toast.LENGTH_SHORT).show();
                    Logger.e(throwable.getMessage());
                });
    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public int forceSetLayoutFirst() {
        return com.yang.R.layout.activity_edituserinfo;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEventUserInfo(Staff staff) {

    }

    @Override
    public void add(Subscription subscription) {
        super.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static int REQUEST_IMAGE = 1000;

    @OnClick({com.yang.R.id.tv_save, com.yang.R.id.tv_edit, com.yang.R.id.menu, com.yang.R.id.user_portrait})
    public void onClick(View view) {
        switch (view.getId()) {
            case com.yang.R.id.user_portrait:
                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
                // 是否显示调用相机拍照
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);

                // 最大图片选择数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);

                // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);

                startActivityForResult(intent, REQUEST_IMAGE);
                break;
            case com.yang.R.id.tv_save:
                if (TextUtils.isEmpty(etPassword.getText().toString())) {
                    Toast.makeText(this, com.yang.R.string.password_is_empty, Toast.LENGTH_SHORT).show();
                    return;
                } else if (etPassword.getText().toString().trim().length() < 6) {
                    Toast.makeText(EditUserInfoActivity.this, "Password unchanged or Password length is not enough", Toast.LENGTH_SHORT).show();
                    return;
                }
                Subscription subscription = RetrofitHttp.getInstance().editUserInfo(
                        App.app.getLoginUser().getUid() + "",
                        etUsername.getText().toString(),
                        SHA.encodeByMD5(etPassword.getText().toString()),
                        etFirstname.getText().toString(),
                        etLastname.getText().toString(),
                        etEmail.getText().toString(),
                        etMobile.getText().toString(),
                        (short) App.app.getLoginUser().getIsAlert())
                        .compose(RxResultHelper.handleResult())
                        .compose(RxResultHelper.applyIoSchedulers())
                        .subscribe(editUserInfoRespBean -> {
                            Logger.e(editUserInfoRespBean.toString());
                            SharedprefUtil.getInstance(EditUserInfoActivity.this).saveInt("pwdSize",etPassword.getText().toString().trim().length());
                            Intent intent1 = new Intent();
                            intent1.putExtra("userPortrait", userImage);
                            setResult(MainActivity.RESULT_OK, intent1);
                            finish();
                        }, throwable -> {
                            Intent intent1 = new Intent();
                            intent1.putExtra("userPortrait", userImage);
                            setResult(MainActivity.RESULT_OK, intent1);
                            finish();
                        });
                break;
            case com.yang.R.id.tv_edit:
                etFirstname.setEnabled(true);
                etLastname.setEnabled(true);
                etEmail.setEnabled(true);
                etMobile.setEnabled(true);
                etUsername.setEnabled(true);
                etPassword.setEnabled(true);
                etPassword.requestFocus();
                break;
            case com.yang.R.id.menu:
                Intent intent1 = new Intent();
                intent1.putExtra("userPortrait", userImage);
                setResult(MainActivity.RESULT_OK, intent1);
                finish();
                break;
        }
    }

    String userImage;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                userImage = path.get(0);
                // 处理你自己的逻辑 ....
                Map<String, RequestBody> map = MultipartUtil.getFilesBody(path.get(0), "photoFile");
//                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"{uid:"+App.app.getLoginUser().getUid()+"}");
                Subscription subscription = RetrofitHttp.getInstance()
                        .editUserImage(App.app.getLoginUser().getUid(), map)
                        .compose(RxResultHelper.handleResult())
                        .compose(RxResultHelper.applyIoSchedulers())
                        .subscribe(editUserImgRespBean -> {
//                            Toast.makeText(EditUserInfoActivity.this, "editUserImage success", Toast.LENGTH_SHORT).show();
//                            App.app.getLoginUser().setPhotoUrl(editUserImgRespBean.getPhotoUrl());
//                            SharedprefUtil.getInstance(EditUserInfoActivity.this).save("photoUrl", editUserImgRespBean.getPhotoUrl());
                        }, throwable -> {
                            Toast.makeText(EditUserInfoActivity.this, "editUserImage fail", Toast.LENGTH_SHORT).show();
                            Logger.e(throwable.getMessage());
                        });
                Glide.with(this).load(new File(path.get(0))).bitmapTransform(new GlideCircleTransform(this)).crossFade(1000).error(com.yang.R.drawable.user_portrait).into(userPortrait);
            }
        }
    }


}
