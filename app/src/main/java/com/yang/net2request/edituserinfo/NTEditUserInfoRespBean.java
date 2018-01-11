package com.yang.net2request.edituserinfo;

import com.yang.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/9/7.
 */
public class NTEditUserInfoRespBean extends NTRespBean {
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public String photoUrl;

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }
    }

}
