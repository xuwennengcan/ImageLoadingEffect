@file:Suppress("DEPRECATION")

package com.can.image.loadingeffect.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.can.image.`interface`.OnProgressListener
import com.can.image.glide.GlideApp
import com.can.image.glide.GlideRequest
import com.can.image.http.ProgressManager
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File
import java.lang.ref.WeakReference

/**
 * Created by CAN on 19-1-5.
 *
 */
class GlideImageLoader<T> constructor(imageView: SubsamplingScaleImageView) {


    private var url: String? = null
    private var imageViewWeakReference: WeakReference<SubsamplingScaleImageView>? = null
    private var glideRequest: GlideRequest<T>? = null

    init {
        imageViewWeakReference = WeakReference(imageView)
        glideRequest = GlideApp.with(getView()!!.context).asFile() as GlideRequest<T>
    }

    private fun getView(): SubsamplingScaleImageView? {
        return if (imageViewWeakReference != null) {
            imageViewWeakReference!!.get()
        } else null
    }

    fun getUrl(): String {
        return if (url.isNullOrEmpty()) "" else url.toString()
    }

    private fun loadImage(obj: Any): GlideRequest<T>? {
        if (obj is String) {
            url = obj
        }
        return glideRequest!!.load(obj)
    }

    fun load(obj: Any, @DrawableRes placeholder: Int, transformation: Transformation<Bitmap>?) : GlideImageLoader<T> {
        loadImage(obj, placeholder, transformation)
        return this
    }

    fun load(url: String, @DrawableRes placeholder: Int, onProgressListener: OnProgressListener) {
        listener(url,onProgressListener).loadImage(url,placeholder,null)
    }

    private fun loadImage(obj: Any, @DrawableRes placeholder: Int, transformation: Transformation<Bitmap>?): GlideImageLoader<T> {
        glideRequest = loadImage(obj)
        if (placeholder != 0) {
            glideRequest = glideRequest!!.placeholder(placeholder)
        }

        if (transformation != null) {
            glideRequest = glideRequest!!.transform(transformation)
        }

        if (getView() != null)
            glideRequest!!.into(GlideImageViewTarget(getView()!!))
        return this
    }

    private fun listener(obj: Any, onProgressListener: OnProgressListener): GlideImageLoader<T> {
        if (obj is String) {
            url = obj
            ProgressManager().addListener(url as String, onProgressListener)
        }
        return this
    }

    private inner class GlideImageViewTarget internal constructor(private val view: SubsamplingScaleImageView) : SimpleTarget<T>() {

        override fun onLoadFailed(errorDrawable: Drawable?) {
            val onProgressListener = ProgressManager().getProgressListener(getUrl())
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0)
                ProgressManager().removeListener(getUrl())
            }
            super.onLoadFailed(errorDrawable)
        }

        override fun onResourceReady(file: T, transition: Transition<in T>?) {
            val onProgressListener = ProgressManager().getProgressListener(getUrl())
            if (onProgressListener != null) {
                onProgressListener.onProgress(true, 100, 0, 0)
                ProgressManager().removeListener(getUrl())
            }
            if(file is File){
                view.setImage(ImageSource.uri(Uri.fromFile(file as File)))
            }
        }
    }
}