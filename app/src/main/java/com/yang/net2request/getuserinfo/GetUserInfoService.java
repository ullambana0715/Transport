package com.yang.net2request.getuserinfo;

import com.yang.App;
import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpService;
import com.yang.netokhttp2.OkHttpUtils;
import com.yang.netokhttp2.HttpConstants;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetUserInfoService extends HttpService {
    NTGetUserInfoReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = null;
//        if (null != App.app.getLoginUser()) {
            body = new FormBody.Builder()
                    .add("uid",reqBean.getUid()+"")
//                    .add_sub("token",App.app.getLoginUser().getToken())
                    .build();
//        }
        call = OkHttpUtils.post(mTag, HttpConstants.getUserInfo,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = null;
        if (null != App.app.getLoginUser()) {
            body = new FormBody.Builder()
                    .add("uid",reqBean.getUid()+"")
//                    .add_sub("token",App.app.getLoginUser().getToken())
                    .build();
        }

        call = OkHttpUtils.synchPost(mTag, HttpConstants.getUserInfo,body,mAdapter);
    }

    public GetUserInfoService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTGetUserInfoReqBean) bean;
    }
}
