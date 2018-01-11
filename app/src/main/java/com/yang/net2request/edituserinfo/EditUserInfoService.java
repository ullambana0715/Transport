package com.yang.net2request.edituserinfo;

import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpConstants;
import com.yang.netokhttp2.HttpService;
import com.yang.netokhttp2.OkHttpUtils;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class EditUserInfoService extends HttpService {
    NTEditUserInfoReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("uid",reqBean.getUid()+"")
                .add("username",reqBean.getUsername())
                .add("password_hint",reqBean.getPassword())
                .add("npassword",reqBean.getNpassword())
                .add("firstname",reqBean.getFirstname())
                .add("surname",reqBean.getSurname())
                .add("email",reqBean.getEmail())
                .add("phone",reqBean.getPhone())
                .add("isAlert",reqBean.getIsAlert()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.editUserInfo,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("uid",reqBean.getUid()+"")
                .add("username",reqBean.getUsername())
                .add("password_hint",reqBean.getPassword())
                .add("npassword",reqBean.getNpassword())
                .add("firstname",reqBean.getFirstname())
                .add("surname",reqBean.getSurname())
                .add("email",reqBean.getEmail())
                .add("phone",reqBean.getPhone())
                .add("isAlert",reqBean.getIsAlert()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.editUserInfo,body,mAdapter);
    }

    public EditUserInfoService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTEditUserInfoReqBean) bean;
    }
}
