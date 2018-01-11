package com.yang.netokhttp3;

import com.orhanobut.logger.Logger;
import com.yang.App;
import com.yang.netokhttp2.HttpConstants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.yang.Constants;
import com.yang.utils.NetWorkUtil;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/8/31.
 */
public class RetrofitHttp {
    private static IHttp mHttp;
    private static RetrofitHttp mRetrofitHttp;
    private static final Object WATCH = new Object();

    HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
    //新建log拦截器
    HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            System.out.println("BODY:"+message);
        }
    });

    OkHttpClient.Builder mOkHttpBuilder = new OkHttpClient.Builder()
            .readTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .connectTimeout(Constants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(new HeadInterceptor())
            .addInterceptor(loggingInterceptor.setLevel(level))
            .addInterceptor(new OKHTTPLogInterceptor());
    /*http://blog.csdn.net/xx326664162/article/details/78044877
    Application interceptors应用程序拦截器
    不需要担心比如重定向和重试的中间响应。
    总是被调用一次，即使HTTP响应结果是从缓存中获取的。
    监控应用程序的原始意图。不关心例如OkHttp注入的头部字段If-None-Match。
    允许短路，不调用Chain.proceed（）。
    允许重试并多次调用Chain.proceed（）。

    Network Interceptors网络拦截器
    能够对中间的响应进行操作比如重定向和重试。
    当发生网络短路时，不调用缓存的响应结果。
    监控数据，就像数据再网络上传输一样。
    访问承载请求的连接Connection。*/
    Retrofit.Builder mRetrofitBulder = new Retrofit.Builder();
    public RetrofitHttp(){
        mHttp = mRetrofitBulder.baseUrl(HttpConstants.TEST_URL)
                .client(mOkHttpBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(IHttp.class);
    }
    //仅限无header方法调用
    public static IHttp getInstance(){
        if (null == mHttp){
            synchronized (WATCH){
                mRetrofitHttp = new RetrofitHttp();//new的过程要等到app初始化加载完成，才能将app信息add入header中。
            }
        }
        return mHttp;
    }

    class HeadInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (null != App.app.getLoginUser()){
                Request newRequset = request.newBuilder()
                        .addHeader("token", App.app.getLoginUser().getToken())
                        .addHeader("registrationId", App.app.uuid+"")
                        .addHeader("uid", App.app.getLoginUser().getUid() + "")
                        .addHeader("version","v260")
                        .build();
                return chain.proceed(newRequset);
            }else {
                return chain.proceed(request);
            }
        }
    }

    class OKHTTPLogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            okhttp3.Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();

            String content = response.body().string();
            if (content.length() > 400){
                Logger.e(chain.request().url().url().toString()+content.substring(0,400));
                Logger.e(content.substring(400,content.length()));
            }else {
                Logger.e(chain.request().url().url().toString()+content);
            }

            if (response.body() != null) {
                // 深坑！
                // 打印body后原ResponseBody会被清空，需要重新设置body
                ResponseBody body = ResponseBody.create(mediaType, content);
                return response.newBuilder().body(body).build();
            } else {
                return response;
            }
        }
    }


    class HttpCacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetWorkUtil.isNetConnected(App.app)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();

                Logger.e("no network");

            }

            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetConnected(App.app)) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=2419200")
                        .removeHeader("Pragma")
                        .build();
            }
        }
    }
}
