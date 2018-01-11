package com.yang.net2request.checkVersion;

import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpService;
import com.yang.netokhttp2.OkHttpUtils;
import com.yang.netokhttp2.HttpConstants;

import okhttp3.FormBody;

/**
 * Created by Administrator on 2017/3/22.
 */

public class CheckVersionService extends HttpService {

    public CheckVersionService(Object tag){
        super(tag);
    }
    private NTCheckVersionReqBean checkVersionReqBean;
    @Override
    public void enqueue() {
        super.enqueue();//调用start状态

        FormBody body = new FormBody.Builder()
                .add("deviceId",checkVersionReqBean.getDevicesId())
                .build();
        call = OkHttpUtils.post(mTag, HttpConstants.checkVersion,body,mAdapter);
    }


    public void setParam(NTReqBean bean){
        checkVersionReqBean = (NTCheckVersionReqBean) bean;
    }
}
