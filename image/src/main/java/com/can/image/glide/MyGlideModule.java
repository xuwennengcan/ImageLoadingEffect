package com.can.image.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.can.image.http.ProgressManager;

import java.io.InputStream;

/**
 * Created by CAN on 19-1-5.
 * 自定义GlideModule
 */
@GlideModule
public class MyGlideModule extends AppGlideModule{
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        ProgressManager progressManager = new ProgressManager();
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(progressManager.getOkHttpClient()));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
