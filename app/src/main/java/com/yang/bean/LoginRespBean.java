package com.yang.bean;

import com.yang.net2request.Staff;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class LoginRespBean extends RespBean implements Serializable{
    private Staff staff;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

}
