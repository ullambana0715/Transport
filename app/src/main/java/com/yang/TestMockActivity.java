package com.yang;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.yang.bean.RespBean;
import com.yang.netokhttp3.RetrofitHttp;
import com.yang.netokhttp3.RxResultHelper;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/5/8.
 */

public class TestMockActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("next");
                subscriber.onCompleted();
            }
        });

        RetrofitHttp.getInstance()
                .login("", "")
                .compose(RxResultHelper.transformer)
                .compose(RxResultHelper.handleRespbeanResult())
                .subscribe(new Action1() {
                    @Override
                    public void call(Object o) {

                    }
                });

        Subscriber subscriber = new Subscriber() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");

            }

            @Override
            public void onNext(Object o) {
                System.out.println("onNext");

            }
        };
        observable.subscribe(subscriber);


        Observable observable1 = Observable.just("hello world");
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("onNext call");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("onError Throwable");
            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("Action0 onCompleted");
            }
        });

        String[] urls = new String[]{"1", "2", "3", "4", "5", "6"};
        Observable.from(urls)
                .flatMap(new Func1<String, Observable<RespBean>>() {
                    @Override
                    public Observable<RespBean> call(String s) {
                        return RetrofitHttp.getInstance().login("", "");
                    }
                })
                .flatMap(new Func1<RespBean, Observable<String>>() {
                    @Override
                    public Observable<String> call(RespBean respBean) {
                        return Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                subscriber.onNext(respBean.toString());
                            }
                        });
                    }
                })
                .filter(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s.length() > 0;
                    }
                })
                .take(5)
                .doOnNext(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("save s to drive");
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("show msg on complete");
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println(s);
                    }
                });
        Observable.from(urls).groupBy(new Func1<String, TestInfo>() {
            @Override
            public TestInfo call(String s) {
                TestInfo testInfo = new TestInfo();
                return testInfo;
            }
        });
        Observable.from(urls)
                .distinct(new Func1<String, TestInfo>() {
                    @Override
                    public TestInfo call(String s) {
                        return new TestInfo();
                    }
                });
//        Observer observer = new Observer() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//
//            }
//        };
//        Subscriber subscriber = new Subscriber() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Object o) {
//
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//
//            @Override
//            public void setProducer(Producer p) {
//                super.setProducer(p);
//            }
//        };
//
//        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//            }
//        });
//
//        Observable observable1 = Observable.just(1,2,3);
//        Observable observable2 = Observable.from(fileList());
//        observable.subscribe(subscriber);
//        observable1.subscribe(observer);
//         Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//
//            }
//        }).subscribe(new Observer<String>() {
//             @Override
//             public void onCompleted() {
//
//             }
//
//             @Override
//             public void onError(Throwable e) {
//
//             }
//
//             @Override
//             public void onNext(String s) {
//
//             }
//         });
//         Observable.just(1,2,3,4)
//                 .map(new Func1<Integer, String>() {
//                     @Override
//                     public String call(Integer integer) {
//                         return "This is :" + integer;
//                     }
//                 })
//                 .subscribe(new Subscriber<String>() {
//                     @Override
//                     public void onCompleted() {
//
//                     }
//
//                     @Override
//                     public void onError(Throwable e) {
//
//                     }
//
//                     @Override
//                     public void onNext(String s) {
//                        System.out.println(s);
//                     }
//                 });
//         ArrayList<OKHTTPLogInterceptor.TestInfo> infos  = new ArrayList<>();
//        OKHTTPLogInterceptor.TestInfo testInfo = new OKHTTPLogInterceptor().new TestInfo();
//         infos.add(testInfo);
//         infos.add(testInfo);
//         infos.add(testInfo);
//         Observable.from(infos)
////                 .concatMap()保留顺序
//                 .flatMap(new Func1<OKHTTPLogInterceptor.TestInfo, Observable<Drawable>>() {
//                     @Override
//                     public Observable<Drawable> call(OKHTTPLogInterceptor.TestInfo testInfo) {
//
//                         return Observable.create(new Observable.OnSubscribe<Drawable>() {
//                             @Override
//                             public void call(Subscriber<? super Drawable> subscriber) {
//                                 subscriber.onNext(testInfo.icon);
//                                 subscriber.onCompleted();
//                             }
//                         });
//                     }
//                 })
//                 .subscribe(new Action1<Drawable>() {
//                     @Override
//                     public void call(Drawable drawable) {
//                         System.out.println(drawable);
//                     }
//                 });
//
//         Observable.just(1,2,3,4,5)
//                 .takeUntil(new Func1<Integer, Boolean>() {
//                     @Override
//                     public Boolean call(Integer integer) {
//                         return integer >3;
//                     }
//                 })
//                 .subscribe(new Action1<Integer>() {
//                     @Override
//                     public void call(Integer integer) {
//                         System.out.println(integer);
//                     }
//                 });
//
//         PackageManager pm = getPackageManager();
//         ArrayList<ApplicationInfo> listInfos = (ArrayList)pm.getInstalledApplications(0);
//         ArrayList<OKHTTPLogInterceptor.TestInfo> testInfos = new ArrayList<>();
//         Observable.from(listInfos)
//                 .filter(new Func1<ApplicationInfo, Boolean>() {
//                     @Override
//                     public Boolean call(ApplicationInfo applicationInfo) {
//                         return applicationInfo.flags == 0;
//                     }
//                 })
//                .map(new Func1<ApplicationInfo, OKHTTPLogInterceptor.TestInfo>() {
//                    @Override
//                    public OKHTTPLogInterceptor.TestInfo call(ApplicationInfo applicationInfo) {
//                        OKHTTPLogInterceptor.TestInfo info = new OKHTTPLogInterceptor().new TestInfo();
//                        info.icon = applicationInfo.loadIcon(pm);
//                        info.name = applicationInfo.packageName;
//                        return info;
//                    }
//                })
//                 .filter(new Func1<OKHTTPLogInterceptor.TestInfo, Boolean>() {
//                     @Override
//                     public Boolean call(OKHTTPLogInterceptor.TestInfo testInfo) {
//
//                         return testInfo.name.contains("weixin");
//                     }
//                 })
//                 .subscribeOn(Schedulers.io())
//                 .observeOn(AndroidSchedulers.mainThread())
//                 .subscribe(new Action1<OKHTTPLogInterceptor.TestInfo>() {
//                     @Override
//                     public void call(OKHTTPLogInterceptor.TestInfo testInfo) {
//                         testInfos.add(testInfo);
//                     }
//                 });

//        File file = new File("");
//        Observable.from(file.listFiles())
//                .flatMap(new Func1<File, Observable<File>>() {
//                    @Override
//                    public Observable<File> call(File file) {
//                        return Observable.from(file.listFiles());
//                    }
//                })
//                .filter(new Func1<File, Boolean>() {
//                    @Override
//                    public Boolean call(File file) {
//                        return file.getName().endsWith(".png");
//                    }
//                })
//                .map(new Func1<File, Bitmap>() {
//                    @Override
//                    public Bitmap call(File file) {
//                        return BitmapFactory.decodeFile(file.getName());
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        ImageView imageView = new ImageView(App.app);
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });

    }

    void test() {

        Observable.just(1, 2, 3, 4, 5)
//                .map(new Func1<Integer, String>() {
//                    @Override
//                    public String call(Integer integer) {
//                        return "This is : " + integer;
//                    }
//                })
                .all(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
//                        int i = Integer.parseInt(integer.substring(integer.length()-1));
                        return integer > 3;
                    }
                })
//                .map(new Func1<Boolean, String>() {
//                    @Override
//                    public String call(Boolean integer) {
//                        return "This is : " + integer;
//                    }
//                })
//                .subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean aBoolean) {
//                        System.out.println(aBoolean);
//                    }
//                })
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError");
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        System.out.println(aBoolean);
                    }
                })
        ;
//        File[] folder = new File[]{};
//        Observable.from(folder)
//                .flatMap(new Func1<File, Observable<File>>() {
//                    @Override
//                    public Observable<File> call(File file) {
//                        return Observable.from(file.listFiles());
//                    }
//                })
//                .filter(new Func1<File, Boolean>() {
//                    @Override
//                    public Boolean call(File file) {
//                        return file.getName().endsWith(".png");
//                    }
//                })
//                .map(new Func1<File, Bitmap>() {
//                    @Override
//                    public Bitmap call(File file) {
//                        return BitmapFactory.decodeFile(file.getName());
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Bitmap>() {
//                    @Override
//                    public void call(Bitmap bitmap) {
//                        ImageView imageView = new ImageView(App.app);
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
        Integer[] integ = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
        Observable observable = Observable.from(integ)
                .groupBy(new Func1<Integer, Integer>() {
                    @Override
                    public Integer call(Integer integer) {
                        return integer % 2;
                    }
                });
        Observable.concat(observable)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer o) {
                        System.out.println(o);
                    }
                });

        Observable.from(integ)
                .take(5)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        System.out.println(integer);
                    }
                });
        String[] let = new String[]{"A", "B", "C", "D", "E"};
        Observable observable1 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        return let[aLong.intValue()];
                    }
                }).take(let.length);
        Observable<Long> observable2 = Observable.interval(500, TimeUnit.MILLISECONDS).take(5);
        Observable.merge(observable1, observable2)
                .subscribe(new Subscriber<Serializable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Serializable o) {
                        System.out.println(o);
                    }
                });
        PackageManager pm = getPackageManager();

        List<ApplicationInfo> list = pm.getInstalledApplications(0);
        List<TestInfo> testInfos = new ArrayList<>();
        Observable.from(list)
                .filter(new Func1<ApplicationInfo, Boolean>() {
                    @Override
                    public Boolean call(ApplicationInfo applicationInfo) {
                        return (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0;
                    }
                })
                .map(new Func1<ApplicationInfo, TestInfo>() {
                    @Override
                    public TestInfo call(ApplicationInfo applicationInfo) {
                        TestInfo info = new TestInfo();
                        info.name = applicationInfo.packageName;
                        info.icon = applicationInfo.loadIcon(pm);
                        return info;
                    }
                })
                .subscribe(new Action1<TestInfo>() {
                    @Override
                    public void call(TestInfo testInfo) {
                        testInfos.add(testInfo);
                    }
                });
//        Observable.from(folder)
//                .flatMap(file -> Observable.from(file.listFiles()))
//                .filter(file -> file.getName().endsWith(".png"))
//                .map(file -> BitmapFactory.decodeFile(file.getName()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(bitmap -> new ImageView(App.app).setImageBitmap(bitmap));

//        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("first");
//                subscriber.onNext("second");
//                subscriber.onNext("third");
//                subscriber.onCompleted();
//            }
//        });
//        Observable observable2 = Observable.just("hi","hello","hei");
//        String[] ints = new String[]{"3","6","9"};
//        Observable observable3 = Observable.from(ints);
//        observable3.subscribe(new Action1<String>() {
//            @Override
//            public void call(String o) {
//                System.out.println(o);
//            }
//        });


//        Subscription subscription = Observable.just(1,2,3,4)
//                .all(new Func1<Integer, Boolean>() {
//                    @Override
//                    public Boolean call(Integer integer) {
//                        System.out.println(integer);
//                        return integer < 3;
//                    }
//                })
//                .subscribe(new Subscriber<Boolean>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("onError");
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        System.out.println(aBoolean);
//                    }
//                });
//
//        Observable.amb(Observable.just(1,2).delay(1,TimeUnit.SECONDS),Observable.just(3,4));
//
//        Subscriber<String> stringSubscriber = new Subscriber<String>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("onCompleted");
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                System.out.println("onError");
//            }
//
//            @Override
//            public void onNext(String s) {
//                System.out.println("onNext:" + s);
//            }
//
//            @Override
//            public void onStart() {
//                super.onStart();
//            }
//        };
//
//
////        stringSubscriber.unsubscribe();
//
//        Observable observable0 = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("onNext---");
//                subscriber.onCompleted();
//            }
//        });
//        observable0.subscribe(stringSubscriber);
//
//
//        String[] words = {"Hello", "Hi", "Aloha"};
//        Observable observable1 = Observable.from(words)
////                .repeat(5)
////                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
////            @Override
////            public Observable<?> call(Observable<? extends Void> observable) {
////                System.out.println("call:" + 5);
////                return observable.delay(5,TimeUnit.SECONDS);
////            }})
//              ;
//        if (stringSubscriber.isUnsubscribed()){
//            stringSubscriber = new Subscriber<String>() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onNext(String s) {
//                    System.out.println("New:" + s);
//                }
//            };
//        }else {
//            System.out.println("isUnsubscribed");
//        }
//        observable1.subscribe(stringSubscriber);

//        RetrofitHttp.getInstance().login("name","96e79218965eb72c92a549dd5a330112")
//                .compose(RxResultHelper.transformer)
//                .compose(RxResultHelper.handleRespbeanResult())
//                .subscribe(new Action1<RespBean>() {
//                    @Override
//                    public void call(RespBean o) {
//                        LoginRespBean loginRespBean = new LoginRespBean();
//                        Staff staff = new Gson().fromJson(o.getResp().toString(),Staff.class);
//                        loginRespBean.setStaff(staff);
//                        System.out.println(o);
//                        System.out.println(loginRespBean);
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        System.out.println(throwable);
//                    }
//                });
//                .subscribe(respons -> {
//                    RespBean bean = (RespBean)respons;
//                    System.out.println(bean);
//                },throwable -> {
//
//                });

//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(5, TimeUnit.SECONDS);
//        builder.addInterceptor(new OKHTTPLogInterceptor());
//        builder.sslSocketFactory(SSLContext.getInstance("TLS").getSocketFactory(),new TrustManagerFactory())

//        Retrofit retrofit = new Retrofit.Builder()
//                .client(builder.build())
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl("http://10.10.11.40:5639")
//                .build();

//        HttpService service = retrofit.create(HttpService.class);
//        Observable<RespBean> observable = service.login("阿斯顿法国和健康");
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
////                .compose()
//                .observeOn(AndroidSchedulers.mainThread())
////                .observeOn(Schedulers.io())
//                .subscribe(new Subscriber<RespBean>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("onCompleted");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("onError");
//                    }
//
//                    @Override
//                    public void onNext(RespBean responseBean) {
//                        System.out.println("onNext" + responseBean.toString());
//                    }
//                });
    }
}

class TestInfo {
    public String name;
    public Drawable icon;
}

interface HttpService {
    @POST("/login")
    @FormUrlEncoded
    Observable<RespBean> login(@Field(value = "name", encoded = true) String name);
}

class OKHTTPLogInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        System.out.println(request.url().url().toString());

        okhttp3.Response response = chain.proceed(chain.request());
        okhttp3.MediaType mediaType = response.body().contentType();
        RealResponseBody realResponseBody = (RealResponseBody) response.body();
        String content = realResponseBody.string();
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
