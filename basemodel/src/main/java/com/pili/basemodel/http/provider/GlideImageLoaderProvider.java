package com.pili.basemodel.http.provider;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.pili.basemodel.http.provider.base.IImageLoaderProvider;
import com.pili.basemodel.http.request.ImageRequest;
import com.pili.basemodel.utils.AppUtils;


/**
 * Created by _SOLID
 * Date:2016/5/13
 * Time:10:27
 */
public class GlideImageLoaderProvider implements IImageLoaderProvider {
    @Override
    public void loadImage(ImageRequest request) {
        Glide.with(AppUtils.getAppContext()).load(request.getUrl()).placeholder(request.getPlaceHolder()).into(request.getImageView());
    }

    @Override
    public void loadImage(Context context, ImageRequest request) {
        Glide.with(context).load(request.getUrl()).placeholder(request.getPlaceHolder()).into(request.getImageView());
    }
}
