package com.pili.basemodel.mvpframe.rx;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2016/12/31.
 */

public class RxManager {
    public RxBus mRxBus = RxBus.$();
    private Map<String, Observable<?>> mObservables = new HashMap<>();// 管理观察源
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();// 管理订阅者


    public void on(String eventName, Consumer<Object> consumer){
        Observable<?> register = mRxBus.register(eventName);
        mObservables.put(eventName,register);
        mCompositeDisposable.add(register.observeOn(AndroidSchedulers.mainThread())
        .subscribe(consumer, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        }));

    }

    /**
     * 添加订阅
     * @param d
     */
    public void add(Disposable d) {
        mCompositeDisposable.add(d);
    }

    //清除订阅
    public void clear() {
        mCompositeDisposable.clear();// 取消订阅
        for (Map.Entry<String, Observable<?>> entry : mObservables.entrySet())
            mRxBus.unregister(entry.getKey(), entry.getValue());// 移除观察
    }

    //发送事件
    public void post(Object tag, Object content) {
        mRxBus.post(tag, content);
    }
}
