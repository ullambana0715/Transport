package com.yang.netokhttp3;

/**
 * Created by Administrator on 2017/11/15.
 */

public interface UploadProgressListener {
    public void onProgress(long totalsize,long currentsize);
}
