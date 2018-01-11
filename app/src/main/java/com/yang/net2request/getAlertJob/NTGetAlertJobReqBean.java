package com.yang.net2request.getAlertJob;

import com.yang.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTGetAlertJobReqBean extends NTReqBean {
    int jobId;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
