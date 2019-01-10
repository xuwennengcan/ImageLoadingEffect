package com.can.image.glide;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.View;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.can.image.inter.OnProgressListener;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by CAN on 19-1-4.
 *
 */

public class GlideImageLoader {

    private String url;
    private WeakReference<View> imageViewWeakReference;
    private GlideRequest<File> glideRequest;

    static GlideImageLoader create(View imageView) {
        return new GlideImageLoader(imageView);
    }

    private GlideImageLoader(View imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
        glideRequest = GlideApp.with(getContext()).asFile();
    }

    private View getImageView() {
        if (imageViewWeakReference != null) {
            return imageViewWeakReference.get();
        }
        return null;
    }

    public Context getContext() {
        if (getImageView() != null) {
            return getImageView().getContext();
        }
        return null;
    }

    public GlideRequest<File> getGlideRequest() {
        if (glideRequest == null) {
            glideRequest = GlideApp.with(getContext()).asFile();
        }
        return glideRequest;
    }


    private GlideRequest<File> loadImage(Object obj) {
        if (obj instanceof String) {
            url = (String) obj;
        }
        return glideRequest.load(obj);
    }

    public GlideImageLoader loadImage(Object obj, @DrawableRes int placeholder,OnProgressListener onProgressListener) {
        glideRequest = loadImage(obj);
        if (placeholder != 0) {
            glideRequest = glideRequest.placeholder(placeholder);
        }
        glideRequest.into(new GlideImageViewTarget<>(url, (SubsamplingScaleImageView) getImageView(),onProgressListener));
        return this;
    }


}
