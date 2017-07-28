package com.pili.basemodel.mvpframe.base;


import com.pili.basemodel.mvpframe.rx.RxManager;

/**
 * Created by Administrator on 2016/12/31.
 *继承此BasePresenter的Presenter拥有对model和view的引用
 */

public abstract class BasePresenter<M, V> {
    public M mModel;
    public V mView;
    public RxManager mRxManager = new RxManager();

    public void attachVM(V v, M m) {
        this.mModel = m;
        this.mView = v;

    }

    public void detachVM() {
        mRxManager.clear();
        mView = null;
        mModel = null;
    }

}
