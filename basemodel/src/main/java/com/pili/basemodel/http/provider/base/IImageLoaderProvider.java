package com.pili.basemodel.http.provider.base;

import android.content.Context;

import com.pili.basemodel.http.request.ImageRequest;


/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:10:25
 */
public interface IImageLoaderProvider {

    void loadImage(ImageRequest request);

    void loadImage(Context context, ImageRequest request);
}
