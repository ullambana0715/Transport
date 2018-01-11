package com.yang.net2request.getAlerts;

import com.yang.net2request.Job;
import com.yang.net2request.NTRespBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTGetAlertsRespBean extends NTRespBean {
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public List<Job> jobs;

        public List<Job> getJobs() {
            return jobs;
        }

        public void setJobs(List<Job> jobs) {
            this.jobs = jobs;
        }
    }
}
