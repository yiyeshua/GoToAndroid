package com.pili.basemodel.http;


import com.pili.basemodel.http.provider.OKHttpProvider;
import com.pili.basemodel.http.provider.base.IHttpProvider;

/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:11:19
 */
public class HttpHelper {

    private static volatile IHttpProvider mProvider;

    public static IHttpProvider getProvider() {
        if (mProvider == null) {
            synchronized (HttpHelper.class) {
                if (mProvider == null) {
                    mProvider = new OKHttpProvider();
                }
            }
        }
        return mProvider;
    }
}
