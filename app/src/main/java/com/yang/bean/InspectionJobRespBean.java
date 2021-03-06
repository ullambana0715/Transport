package com.yang.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class InspectionJobRespBean extends RespBean implements Serializable{
    private String signUrl;

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    private int completedStatus;
    private int currentPage;
    private int jobId;
    private int node;
    private String runDate;
    private int runStatus;
    private int skipMark;

    public int getSkipMark() {
        return skipMark;
    }

    public void setSkipMark(int skipMark) {
        this.skipMark = skipMark;
    }
    public int getCompletedStatus() {
        return completedStatus;
    }

    public void setCompletedStatus(int completedStatus) {
        this.completedStatus = completedStatus;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }
}
