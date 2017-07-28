package com.pili.basemodel.retrofit;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by YSL on 2016/8/3 0003.
 */
public interface NetService {
    @GET("/search")
    Observable<List<Object>> login(@Query("q") String q);

    @GET("/search")
    Observable<Object> login2(@Query("q") String q);
}
