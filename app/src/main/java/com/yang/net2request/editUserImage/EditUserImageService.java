package com.yang.net2request.editUserImage;

import com.yang.net2request.NTReqBean;
import com.yang.netokhttp2.HttpService;
import com.yang.netokhttp2.OkHttpUtils;
import com.yang.netokhttp2.HttpConstants;

/**
 * Created by Administrator on 2016/9/7.
 */
public class EditUserImageService extends HttpService {
    NTEditUserImageReqBean reqBean;
    @Override
    public void enqueue() {
        super.enqueue();
        String url  =  HttpConstants.editUserImage+"?"+"uid="+reqBean.getUid();
        call = OkHttpUtils.uploading(mTag, url,reqBean.getPhotoFile(),mAdapter);
    }
    public void synchEnqueue(){
        String url  =  HttpConstants.editUserImage+"?"+"uid="+reqBean.getUid();
        call = OkHttpUtils.synchUploading(mTag, url,reqBean.getPhotoFile(),mAdapter);
    }

    public EditUserImageService(Object o) {
        super(o);
    }

    public void setParam(NTReqBean bean){
        reqBean = (NTEditUserImageReqBean) bean;
    }
}
