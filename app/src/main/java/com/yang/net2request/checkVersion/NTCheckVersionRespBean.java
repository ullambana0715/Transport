package com.yang.net2request.checkVersion;

import com.yang.net2request.NTRespBean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/22.
 */

public class NTCheckVersionRespBean extends NTRespBean implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private int version;
        private String url;

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
