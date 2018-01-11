package com.yang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.orhanobut.logger.Logger;
import com.yang.mvppresenter.IPresenter;
import com.yang.mvpview.LoginActivity;
import com.yang.utils.DialogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/8/25.
 */
public abstract class BaseActivity<Y extends IPresenter> extends FragmentActivity {
    public Y mPresenter;//其他所有activity可能都会用到presenter，所以定义成泛型。
    public Unbinder unbinder;
    private CompositeSubscription compositeSubscription;//用的较少。
    private DialogUtil mDialogUtilog;//公共弹窗，比如踢出登录，收到重大消息。该弹窗通过reciver接收，reciver发送的地方后面会指明
    @Override
    @Subscribe
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(mKickoffReceiver,new IntentFilter(Constants.ACTION_KICKOFF));
        mPresenter = getPresenter();
        setContentView(forceSetLayoutFirst());
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (mDialogUtilog == null) {
            mDialogUtilog = new DialogUtil(BaseActivity.this);
        }
    }

    public abstract Y getPresenter();//强制子类实现父类方法，获取presenter
    public abstract int forceSetLayoutFirst();//强制子类实现父类方法，获取presenter
    @Subscribe
    public void add(Subscription subscription){
        if (null == compositeSubscription){
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);
    }

    @Override
    protected void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        unregisterReceiver(mKickoffReceiver);
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    BroadcastReceiver mKickoffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.e("kicked off");
            if (intent.getAction() == Constants.ACTION_KICKOFF){
                mDialogUtilog.changeDialog("Notice!!!",intent.getStringExtra("message"), null, "\t\t  OK  \t\t", false,null, new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                        finish();
                    }
                }, DialogUtil.ERROR_TYPE);

            }else if (intent.getAction() == Constants.ACTION_KICKOFF){

            }
        }
    };
}
