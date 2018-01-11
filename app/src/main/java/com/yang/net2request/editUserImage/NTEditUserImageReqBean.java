package com.yang.net2request.editUserImage;

import com.yang.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTEditUserImageReqBean extends NTReqBean {
    int uid;
    String photoFile;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(String photoFile) {
        this.photoFile = photoFile;
    }
}
