package com.yang.net2request.activeJob;

import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpService;
import com.yang.netokhttp2.OkHttpUtils;
import com.yang.netokhttp2.HttpConstants;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2016/9/7.
 */
public class ActiveJobService extends HttpService {
    NTActiveJobReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        FormBody body = new FormBody.Builder()
                .add("appJobId",reqBean.getAppJobId()+"")
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.activeJob,body,mAdapter);
    }
    public void synchEnqueue(){
        FormBody body = new FormBody.Builder()
                .add("appJobId",reqBean.getAppJobId()+"")
                .build();
        call = OkHttpUtils.synchPost(mTag, HttpConstants.activeJob,body,mAdapter);
    }

    public ActiveJobService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTActiveJobReqBean) bean;
    }
}
