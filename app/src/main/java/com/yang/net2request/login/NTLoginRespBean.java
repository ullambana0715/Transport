package com.yang.net2request.login;

import java.io.Serializable;

import com.yang.net2request.NTRespBean;
import com.yang.net2request.Staff;

/**
 * Created by Administrator on 2016/9/1.
 */
public class NTLoginRespBean extends NTRespBean implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private Staff staff;

        public Staff getStaff() {
            return staff;
        }

        public void setStaff(Staff staff) {
            this.staff = staff;
        }
    }

}
