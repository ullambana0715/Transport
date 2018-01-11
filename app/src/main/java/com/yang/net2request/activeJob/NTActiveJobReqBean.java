package com.yang.net2request.activeJob;

import com.yang.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTActiveJobReqBean extends NTReqBean {
    int appJobId;//app_job的主键id


    public int getAppJobId() {
        return appJobId;
    }

    public void setAppJobId(int appJobId) {
        this.appJobId = appJobId;
    }

}
