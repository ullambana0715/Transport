package com.yang.net2request.login;

import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpService;
import com.yang.netokhttp2.OkHttpUtils;
import com.yang.netokhttp2.HttpConstants;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/8/30.
 */
public class LoginHttpService extends HttpService {
    public LoginHttpService(Object tag){
        super(tag);
    }
    private NTLoginReqBean loginBean;
    @Override
    public void enqueue() {
        super.enqueue();//调用start状态

        FormBody body = new FormBody.Builder()
                .add("username",loginBean.getUsername())
                .add("password",loginBean.getPwd())
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.login,body,mAdapter);
    }

    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("username",loginBean.getUsername())
                .add("password",loginBean.getPwd())
                .build();
        call = OkHttpUtils.synchPost(mTag,HttpConstants.login,body,mAdapter);
    }

    public void setParam(NTReqBean bean){
        loginBean = (NTLoginReqBean) bean;
    }
}
