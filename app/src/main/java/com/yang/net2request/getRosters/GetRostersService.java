package com.yang.net2request.getRosters;

import com.yang.netokhttp2.OkHttpUtils;
import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpConstants;
import com.yang.netokhttp2.HttpService;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class GetRostersService extends HttpService {
    NTGetRostersReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("uid",reqBean.getUid()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.getRosterList,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("uid",reqBean.getUid()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.getRosterList,body,mAdapter);
    }

    public GetRostersService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTGetRostersReqBean) bean;
    }
}
