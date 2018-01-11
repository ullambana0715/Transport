package com.yang.netokhttp3;


import com.yang.bean.RespBean;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 *  Created by SQ on 16/7/12.
 */
public class RxResultHelper {

    public static void handleResutl(RespBean respBean){

//        Observable.create(new Observable.OnSubscribe<RespBean>() {
//            @Override
//            public void call(Subscriber<? super RespBean> subscriber) {
//                try {
//                    subscriber.onNext(respBean);
//                    subscriber.onCompleted();
//                }catch (Exception e){
//                    subscriber.onError(e);
//                }
//            }
//        }).flatMap(new Func1<RespBean, Observable<?>>() {
//            @Override
//            public Observable<?> call(RespBean respBean) {
//                if ("000".equals(respBean.getStatus())) {
//                    return respBean;
//                }
//
//            }
//        });
    }

    public static <T> Observable.Transformer<RespBean<T>, T> handleResult() {
        return apiResponseObservable -> apiResponseObservable.flatMap(new Func1<RespBean<T>, Observable<T>>() {

            @Override
            public Observable<T> call(RespBean<T> tApiResponse) {
                    return createData(tApiResponse.getResp());
            }
        });
    }

    public static <T> Observable<T> createData(T t) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static <T> Observable<RespBean<T>> createRespTData(RespBean<T> response) {
        return Observable.create(new Observable.OnSubscribe<RespBean<T>>() {
            @Override
            public void call(Subscriber<? super RespBean<T>> subscriber) {
                try {
                    subscriber.onNext(response);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private final static Observable.Transformer ioTransformer = o -> ((Observable)o).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
//    private final static Observable.Transformer ioTransformer = o -> ((Observable)o).unsubscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread());


    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return (Observable.Transformer<T, T>) ioTransformer;
    }

    public static Observable.Transformer<Object,RespBean> transformer = new Observable.Transformer() {
        @Override
        public Observable call(Object o) {
            return ((Observable) o)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    public static  <T> Observable.Transformer<RespBean, T> handleRespbeanResult(){
        Observable.Transformer<RespBean, T> transformer = new Observable.Transformer<RespBean, T>() {
            @Override
            public Observable<T> call(Observable<RespBean> respBeanObservable) {
                return ((Observable) respBeanObservable).flatMap(new Func1<RespBean<T>, Observable<RespBean<T>>>(){
                    @Override
                    public Observable<RespBean<T>> call(RespBean<T> tRespBean) {
                        return createRespTData(tRespBean);
                    }
                });
            }
        };
        return transformer;
    }
}

