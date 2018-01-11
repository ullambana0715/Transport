package com.yang.net2request.logout;

import com.yang.netokhttp2.OkHttpUtils;
import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpConstants;
import com.yang.netokhttp2.HttpService;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/5.
 */
public class LogoutService extends HttpService {
    public LogoutService(Object o) {
        super(o);
    }

    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder().add("uid",reqBean.getUid()+"").build();
        call = OkHttpUtils.post(mTag, HttpConstants.logout,body,mAdapter);
    }

    public void synchEnqueue() {
        FormBody body = new FormBody.Builder().add("uid",reqBean.getUid()+"").build();
        call = OkHttpUtils.synchPost(mTag,HttpConstants.logout,body,mAdapter);
    }
    NTLogoutReqBean reqBean;
    public void setParam(NTReqBean bean){
        reqBean = (NTLogoutReqBean) bean;
    }
}
