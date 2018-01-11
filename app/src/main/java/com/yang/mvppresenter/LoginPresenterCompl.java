package com.yang.mvppresenter;


import com.google.gson.Gson;
import com.yang.App;
import com.yang.mvpview.ILoginView;
import com.yang.mvpview.LoginActivity;
import com.yang.net2request.NTRespBean;
import com.yang.net2request.Staff;
import com.yang.net2request.login.LoginHttpService;
import com.yang.net2request.login.NTLoginRespBean;
import com.yang.netokhttp2.LoadingAdapter;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.utils.JsonUtils;
import com.yang.utils.SharedprefUtil;

import org.greenrobot.eventbus.EventBus;

import com.yang.net2request.login.NTLoginReqBean;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.utils.SHA;

/**
 * Created by Administrator on 2016/8/25.
 */
public class LoginPresenterCompl implements ILoginPresenter {
    ILoginView mView;
    LoginActivity mActivity;

    public LoginPresenterCompl(ILoginView v, LoginActivity c) {
        mView = v;
        mActivity = c;
    }

    @Override
    public void clearName() {
        mView.onClearName();
    }

    @Override
    public void clearPwd() {

    }

    @Override
    public void rememberUsername() {
        mView.onRemmeberUsername();
    }

    @Override
    public void login(String name, String pwd) {
//        retrofitLogin(name,pwd);

        RetrofitHttp.getInstance()
                .login(name,SHA.encodeByMD5(pwd))
                .compose(RxResultHelper.handleRespbeanResult())
                .compose(RxResultHelper.transformer)
                .subscribe(respBean -> {
                    NTLoginRespBean.Data bean = (NTLoginRespBean.Data) JsonUtils.transferBean(respBean,NTLoginRespBean.Data.class);
                    Staff staff = bean.getStaff();
                    App.app.setLoginUser(staff);
                    EventBus.getDefault().postSticky(staff);//post给MainActivity的onEventLogin方法
                    SharedprefUtil.getInstance(App.app).save("logininfo", new Gson().toJson(staff));
                    mView.onLogin("");
                },throwable -> {
                    throwable.printStackTrace();
                });
    }

    public void retrofitLogin(String name, String pwd){
        LoginHttpService service = new LoginHttpService("login");
        NTLoginReqBean reqBean = new NTLoginReqBean();
        reqBean.setUsername(name);
        reqBean.setPwd(SHA.encodeByMD5(pwd));
        service.setParam(reqBean);
        service.callBack(new LoadingAdapter(mActivity, NTLoginRespBean.class) {
            @Override
            public void onStart() {
                super.onStart();
                System.out.println("onStart");
            }

            @Override
            public void onLoading(Object o) {
                super.onLoading(o);
                System.out.println("onLoading");
            }

            @Override
            public void onCancle(Object o) {
                super.onCancle(o);
                System.out.println("onCancle");
            }

            @Override
            public void onFail(Object o) {
                super.onFail(o);
                if (o instanceof NTRespBean){
                    NTRespBean bean = (NTRespBean) o;
                    mView.onFail(bean);
                }else {
                    mView.onFail(null);
                }
            }

            @Override
            public void onTimeOut() {
                super.onTimeOut();
                System.out.println("onTimeOut");
                mView.onTimeOut();
            }

            @Override
            public void onFailNoPermission(Object o) {
                super.onFailNoPermission(o);
                NTRespBean ntRespBean = (NTRespBean) o;
                mView.onFailNoPermission(ntRespBean);
            }

            @Override
            public void onFailIsNull() {
                super.onFailIsNull();
                mView.onFailIsNull();
            }

            @Override
            public void onSuccess(Object o) {
                super.onSuccess(o);
                mView.onFail(null);
            }

            @Override
            public void onRespbean(Object o) {
                NTLoginRespBean bean = (NTLoginRespBean) o;
                App.app.setLoginUser(bean.getData().getStaff());

                System.out.println("bean.getStatus():" + bean.getStatus());
                Staff staff = bean.getData().getStaff();
                Gson gson = new Gson();
                EventBus.getDefault().postSticky(staff);//post给MainActivity的onEventLogin方法
                SharedprefUtil.getInstance(App.app).save("logininfo", gson.toJson(staff));
                mView.onLogin("");
            }
        });
        service.enqueue();
    }

    @Override
    public void register() {
    }

    @Override
    public void loading(int visiblity) {

    }
}
