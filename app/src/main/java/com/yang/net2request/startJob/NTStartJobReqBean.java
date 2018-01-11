package com.yang.net2request.startJob;

import com.yang.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/12/24.
 */

public class NTStartJobReqBean extends NTReqBean {
    private int jobId;
    private int jobStarted;
    private String startTime;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getJobStarted() {
        return jobStarted;
    }

    public void setJobStarted(int jobStarted) {
        this.jobStarted = jobStarted;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
