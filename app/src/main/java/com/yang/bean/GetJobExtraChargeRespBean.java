package com.yang.bean;

import com.yang.net2request.Jobextracharge;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetJobExtraChargeRespBean extends RespBean implements Serializable {
    List<Jobextracharge> jobExtraChargeDict;

    public List<Jobextracharge> getJobextrachargeDict() {
        return jobExtraChargeDict;
    }

    public void setJobextrachargeDict(List<Jobextracharge> jobextrachargeDict) {
        this.jobExtraChargeDict = jobextrachargeDict;
    }
}