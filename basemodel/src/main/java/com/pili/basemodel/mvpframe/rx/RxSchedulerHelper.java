package com.pili.basemodel.mvpframe.rx;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/12/31.
 * Rx线程调度帮助类
 */

public class RxSchedulerHelper {

    //线程调度，返回一个绑定了生产线程和消费线程的被观察者，其中被观察者执行在io线程，以后如果订阅了观察者，则观察者执行在主线程
    public static <T> ObservableTransformer<T, T> io_main() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }

    //线程调度，返回一个绑定了生产线程和消费线程的被观察者，其中被观察者执行在newThread线程，以后如果订阅了观察者，则观察者执行在主线程

    public static <T> ObservableTransformer<T, T> newThread_main() {

        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(io.reactivex.Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }

        };
    }
}
