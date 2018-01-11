package com.yang.net2request.logout;

import com.yang.net2request.NTRespBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/5.
 */
public class NTLogoutRespBean extends NTRespBean implements Serializable {
    public Data  data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

    }

}
