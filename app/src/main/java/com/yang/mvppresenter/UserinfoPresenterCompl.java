package com.yang.mvppresenter;

import com.orhanobut.logger.Logger;
import com.yang.App;
import com.yang.MainActivity;
import com.yang.mvpview.IUserInfoView;
import com.yang.net2request.Staff;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;
import com.yang.utils.SharedprefUtil;

import rx.Subscription;

/**
 * Created by Administrator on 2016/9/13.
 */
public class UserinfoPresenterCompl {
    IUserInfoView mView;
    MainActivity mActivity;
    boolean isAlertMessageOn;

    public UserinfoPresenterCompl(IUserInfoView v, MainActivity c) {
        mView = v;
        mActivity = c;
//        isAlertMessageOn = SharedprefUtil.getBoolean(mActivity,"isAlertMessageOn",true);
    }

    public void onEditUserinfo() {
        mView.onEditUserinfo();
    }

    public void onChangeAlert(Staff staff) {
        isAlertMessageOn = isAlertMessageOn ? false : true;
        Subscription subscription = RetrofitHttp.getInstance().editUserInfo(App.app.getLoginUser().getUid() + "",
                "",
                "",
                staff.getFirstname(),
                "",
                "",
                "",
                (short) (isAlertMessageOn ? 0 : 1))
                .compose(RxResultHelper.handleResult())
                .compose(RxResultHelper.applyIoSchedulers())
                .subscribe(editUserInfoRespBean -> {
                    Logger.e(editUserInfoRespBean.toString());
                }, throwable -> {
                    Logger.e("edituserinfo exception");
                });

        mView.onChangeAlert(isAlertMessageOn);
    }

    public void onLogout() {
        RetrofitHttp.getInstance().logout(App.app.getLoginUser().getUid() + "")
                .compose(RxResultHelper.transformer)
                .compose(RxResultHelper.handleRespbeanResult())
                .subscribe(response -> {
                            Logger.e(response.toString());
                            App.app.setLoginUser(null);
                            SharedprefUtil.getInstance(mActivity).save("logininfo", "");
                            mView.onLogout();
                        },
                        throwable -> {
                            App.app.setLoginUser(null);
                            SharedprefUtil.getInstance(mActivity).save("logininfo", "");
                            mView.onLogout();
                        });
    }
}
