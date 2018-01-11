package com.yang.net2request.getuserinfo;

import com.yang.net2request.NTRespBean;
import com.yang.net2request.Staff;

/**
 * Created by Administrator on 2016/9/7.
 */
public class NTGetUserInfoRespBean extends NTRespBean {
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public Staff staff;

        public Staff getStaff() {
            return staff;
        }

        public void setStaff(Staff staff) {
            this.staff = staff;
        }

    }

}
