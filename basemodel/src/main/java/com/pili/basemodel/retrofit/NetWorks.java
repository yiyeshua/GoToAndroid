package com.pili.basemodel.retrofit;


import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by YSL on 2016/8/3 0003.
 */
public class NetWorks extends RetrofitUtils {

    protected static final NetService service = getRetrofit().create(NetService.class);

    //设缓存有效期为1天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    /**
     * 方法一：List等泛型类
     * @param q
     * @param observer
     */
    public static  void Test(String q, Observer<List<Object>> observer){
        setSubscribe(service.login(q),observer);
    }

    /**
     * 方法二：Object类型
     * @param q
     * @param observer
     */
    public static void Test2(String q, Observer<Object> observer){
        setSubscribe2(service.login2(q),observer);
    }

    /**
     * 插入观察者-泛型
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     *  插入观察者-Object
     * @param observable
     * @param observer
     */
    public static void setSubscribe2(Observable<Object> observable, Observer<Object> observer) {
        observable.subscribeOn(Schedulers.io())
                 .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}
