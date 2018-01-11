package com.yang.mvpview;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import com.yang.App;
import com.yang.BaseActivity;
import com.yang.MainActivity;
import com.yang.R;
import com.yang.mvppresenter.ILoginPresenter;
import com.yang.mvppresenter.LoginPresenterCompl;
import com.yang.net2request.NTRespBean;
import com.yang.net2request.Staff;
import com.yang.netokhttp2.HttpConstants;
import com.yang.utils.DialogUtil;
import com.yang.utils.SharedprefUtil;

/**
 * Created by Administrator on 2016/8/25.
 */                                    //要声明本activity要用的presenter类型
public class LoginActivity extends BaseActivity<ILoginPresenter> implements ILoginView, View.OnTouchListener {
    EditText et_username;
    EditText et_pwd;
    TextView tv_token;
    TextView tv_resp;
    @BindView(R.id.register)
    Button bt_register;
    @BindView(R.id.login)
    Button bt_login;
//    @BindView(R.id.switch_ip)
//    Button switch_ip;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.remember_username_cb)
    CheckBox remember_username_cb;
    boolean isRememberedUsername;
    private DialogUtil mDialogUtilog;

    @Override
    public ILoginPresenter getPresenter() {//返回的类型也同样是该activity所用的presenter
        return new LoginPresenterCompl(this, this);
    }

    public int forceSetLayoutFirst() {
        return R.layout.activity_login;
    }

    protected void onCreate(Bundle savedInstanceState) {//可以考虑保存登录的返回数据，通过gson来读取bean
        if (!TextUtils.isEmpty(SharedprefUtil.getInstance(this).get("logininfo", ""))) {
            Gson gson = new Gson();
            Staff staff = gson.fromJson(SharedprefUtil.getInstance(this).get("logininfo", ""),Staff.class);
            App.app.setLoginUser(staff);
            EventBus.getDefault().postSticky(staff);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        initViews();
        isRememberedUsername = SharedprefUtil.getInstance(this).getBoolean("rememberusername", false);
        remember_username_cb.setChecked(isRememberedUsername);
        if (isRememberedUsername) {
            et_username.setText(SharedprefUtil.getInstance(this).get("username", ""));
        } else {
            et_username.setText("");
        }
        remember_username_cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRememberedUsername = isRememberedUsername ? false : true;
                remember_username_cb.setChecked(isRememberedUsername);
                SharedprefUtil.getInstance(LoginActivity.this).saveBoolean("rememberusername", isRememberedUsername);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                                switch (arg1){
                                    case R.id.radio190:
                                        HttpConstants.TEST_URL = HttpConstants.TEST_URL_190;
                                        break;
                                    case R.id.radiocom:
                                        HttpConstants.TEST_URL = HttpConstants.HOTLINE_URL;
                                        break;
                                }

                Log.i("TEST_URL",HttpConstants.TEST_URL);
            }
         });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void initViews() {
        et_pwd = (EditText) findViewById(R.id.userpwd);
        bt_register = (Button) findViewById(R.id.register);
        et_username = (EditText) findViewById(R.id.username);

        et_username.setOnTouchListener(this);
        et_pwd.setOnTouchListener(this);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onClearName() {
        et_username.setText("");
    }

    @Override
    public void onClearPwd() {
        et_pwd.setText("");
    }

    public void onRemmeberUsername() {

    }

    @Override
    public void onTimeOut() {
        mDialogUtilog.changeDialog("Load the timeout", "Make sure the network is available", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
    }

    @Override
    public void onFail(NTRespBean bean) {
        if (null != bean){
            mDialogUtilog.changeDialog("fail to load", bean.getMsg(), null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
        }else {
            mDialogUtilog.changeDialog("fail to load", "Net error", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
        }
    }

    @Override
    public void onFailNoPermission(NTRespBean bean) {
        if (null != bean) {
            mDialogUtilog.changeDialog("No Permission", bean.getMsg(), null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
        }else {
            mDialogUtilog.changeDialog("fail to load", "Net error", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
        }
    }

    @Override
    public void onFailIsNull() {
        mDialogUtilog.changeDialog("Login failed", "Please check the account password", null, "\t\t  OK  \t\t", false, null, null, DialogUtil.ERROR_TYPE);
    }

    @Override
    public void onLogin(String success) {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onLoading(int visiablity) {

    }

    @OnClick(R.id.login)
    void onLoginClick() {
        mPresenter.login(et_username.getText().toString().trim(), et_pwd.getText().toString().trim());
    }

    @OnClick(R.id.register)
    void onRegisterClick() {
        mPresenter.register();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        //getCompoundDrawablesRelative获取当前view的资源数组
        // 存放drawableStart，Top，End, Bottom四个图片资源对象
        // index=2 表示的是 drawableEnd 图片资源对象
        Drawable drawable = null;
        // drawable.getIntrinsicWidth() 获取drawable资源图片呈现的宽度
        // getPaddingRight() 获取完成布局后和其他控件的padding距离
        /* @说明：isInnerWidth, isInnerHeight为ture，触摸点在删除图标之内，则视为点击了删除图标
         * event.getX() 获取相对应自身左上角的X坐标,也就是相对坐标
         * event.getY() 获取相对应自身左上角的Y坐标，也就是相对坐标
         * getWidth() 获取控件的宽度
         * getHeight() 获取控件的高度
         * getTotalPaddingRight() 获取删除图标左边缘到控件右边缘的距离
         * getPaddingRight() 获取删除图标右边缘到控件右边缘的距离
         * isInnerWidth:
         *  getWidth() - getTotalPaddingRight() 计算删除图标左边缘到控件左边缘的距离
         *  getWidth() - getPaddingRight() 计算删除图标右边缘到控件左边缘的距离
         * isInnerHeight:
         *  distance 删除图标顶部边缘到控件顶部边缘的距离
         *  distance + height 删除图标底部边缘到控件顶部边缘的距离
         */
        switch (view.getId()) {
            case R.id.username:
                drawable = et_username.getCompoundDrawablesRelative()[2];
                if (null == drawable) {
                    return false;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (x > (et_username.getWidth() - et_username.getPaddingRight() - drawable.getIntrinsicWidth())) {
                        //控件自身的宽度 - 控件右侧padding - drawable的宽度。
                        mPresenter.clearName();
                    }
                }
                break;
            case R.id.userpwd:
                drawable = et_pwd.getCompoundDrawablesRelative()[2];
                if (null == drawable) {
                    return false;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (x > (et_pwd.getWidth() - et_pwd.getPaddingRight() - drawable.getIntrinsicWidth())) {
                        //控件自身的宽度 - 控件右侧padding - drawable的宽度。
                        mPresenter.clearPwd();
                    }
                }
                break;
        }
        return false;
    }

    private void initDialog() {
        if (mDialogUtilog == null) {
            mDialogUtilog = new DialogUtil(LoginActivity.this);
        }
    }
}
