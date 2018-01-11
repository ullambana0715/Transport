package com.yang.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2017/9/25.
 */

public class HookUtils {
    Context mContext;
    Class<?> proxyActivity;
    public HookUtils(Context c,Class<?> cl){
        mContext = c;
        proxyActivity = cl;
    }

    public void hookAMS(){
        try {
            Class<?> forname = Class.forName("android.app.ActivityManagerNative");
            Field defaultField = forname.getDeclaredField("gDefault");
            defaultField.setAccessible(true);
            Object defaultValue = defaultField.get(null);
            Class<?> forname2 = Class.forName("android.util.Singleton");
            Field mInstance = forname2.getDeclaredField("mInstance");
            mInstance.setAccessible(true);
            Object iActivityManager = mInstance.get(defaultValue);

            Class<?> iActivityIntercept = Class.forName("android.app.IActivityManager");
            AMSInvocacationHandler handler = new AMSInvocacationHandler(iActivityManager);
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class<?>[]{iActivityIntercept},handler);
            mInstance.set(defaultValue,proxy);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    class AMSInvocacationHandler implements InvocationHandler{
        private Object activityManager;
        public AMSInvocacationHandler(Object o){
            activityManager = o;
        }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("startActivity".contains(method.getName())){
                Intent intent = null;
                int index = 0;
                for (int i=0;i<args.length;i++){
                    if (args[i] instanceof Intent){
                        intent = (Intent)args[i];
                        index = i;
                        break;
                    }
                }
                Intent proxyIntent = new Intent();
                ComponentName componentName = new ComponentName(mContext,proxyActivity);
                proxyIntent.setComponent(componentName);
                proxyIntent.putExtra("oldIntent",intent);
                args[index] = proxyIntent;
                return method.invoke(activityManager,args);
            }
            return method.invoke(activityManager,args);
        }
    }

    public void hookSystemHandler(){
        try {
            Class<?> forname =Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThread = forname.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThread.setAccessible(true);
            Object activityThread = sCurrentActivityThread.get(null);
            Field mInstance = forname.getDeclaredField("mH");
            mInstance.setAccessible(true);
            Handler handlerObj = (Handler) mInstance.get(activityThread);
            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);
            ActivityThreadHandlerCallback callback = new ActivityThreadHandlerCallback(handlerObj);
            callbackField.set(handlerObj,callback);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public class ActivityThreadHandlerCallback implements Handler.Callback{
        private Handler handler;
        public ActivityThreadHandlerCallback(Handler h){
            super();
            handler = h;
        }
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 100){
                handlerCallbackActivity(msg);
            }
            handler.handleMessage(msg);
            return false;
        }

        private void handlerCallbackActivity(Message msg){
            Object obj = msg.obj;
            try {
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent intent = (Intent) intentField.get(obj);
                Intent proxyIntent = (Intent)intent.getParcelableExtra("oldIntent");
                if (proxyIntent != null) {
                    intent.setComponent(proxyIntent.getComponent());
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
