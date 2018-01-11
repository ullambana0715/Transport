package com.yang.netokhttp2;

import com.yang.Constants;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/30.
 */
public class HttpService {
    public Call call;
    public LoadingAdapter mAdapter;
    public Object mTag;//tag这里用来标记cancle的状态，后来发现很多调用都直接在方法内部调用，没有全局调用的请求，于是cancle的状态基本上就未用
    public HttpService(Object o){
        mTag = o ;
    }
    public  void enqueue(){
        LoadingHandler handler = new LoadingHandler(mAdapter);
        handler.sendEmptyMessage(Constants.NET_LOAD_START);//start的过程是从调用到发送请求之前---OKHttp.excute
    };

    public void callBack(LoadingAdapter adapter){
        mAdapter = adapter;
    }

    public void cancel(){
        OkHttpUtils.cancel(mTag,mAdapter);
    }
}
