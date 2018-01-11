package com.yang;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.yang.net2request.Staff;
import com.yang.utils.SharedprefUtil;
import com.yang.utils.UUIDGenerator;

/**
 * Created by Administrator on 2016/8/31.
 */
public class App extends MultiDexApplication {//支持分包打包策略，继承MultiDexApplication
    //定义静态app方便整个项目引用，
    public static App app;
    //定义公共userInfo方便其他模块调用，
    private Staff mUser;
    public static String uuid;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        if (TextUtils.isEmpty(SharedprefUtil.getInstance(this).get("registrationId",""))){
            uuid = UUIDGenerator.getUUID();
            SharedprefUtil.getInstance(this).save("registrationId",uuid);
        }else {
            uuid =  SharedprefUtil.getInstance(this).get("registrationId","");
        }
//        HookUtils hookUtils = new HookUtils(this,LoginActivity.class);
//        hookUtils.hookAMS();
//        hookUtils.hookSystemHandler();

        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.initCrashHandler(getApplicationContext());
    }
    public Staff getLoginUser(){
        return mUser;
    }

    public void setLoginUser(Staff user){
        mUser = user;
    }

}
