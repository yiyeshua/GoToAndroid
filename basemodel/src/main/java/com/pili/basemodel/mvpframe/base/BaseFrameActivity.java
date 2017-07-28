package com.pili.basemodel.mvpframe.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.pili.basemodel.base.BaseActivity;
import com.pili.basemodel.mvpframe.util.TUtil;


/**
 * Created by Administrator on 2016/12/28.
 */

public abstract class BaseFrameActivity<P extends BasePresenter, M extends BaseModel> extends BaseActivity implements BaseView{
    public P mPresenter;

    public M mModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = TUtil.getT(this, 0);
        mModel = TUtil.getT(this, 1);
        if (this instanceof BaseView) {
            mPresenter.attachVM(this, mModel);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * view生命周期结束，解除绑定
     */
    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachVM();
        super.onDestroy();
    }


}
