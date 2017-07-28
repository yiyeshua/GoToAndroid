package com.pili.basemodel.http.provider.base;


import com.pili.basemodel.http.callback.HttpCallBack;
import com.pili.basemodel.http.callback.adapter.FileHttpCallBack;
import com.pili.basemodel.http.request.HttpRequest;

/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:9:49
 */
public interface IHttpProvider {

    void loadString(HttpRequest request, HttpCallBack callBack);

    void download(String downloadUrl, String savePath, FileHttpCallBack callBack);
}
