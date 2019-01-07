package com.can.image.loadingeffect.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import com.bumptech.glide.load.Transformation
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
    }

    fun getGlideRequest(): GlideRequest<T> {
        if (glideRequest == null) {
            glideRequest = GlideApp.with(getView()!!.context).asFile() as GlideRequest<T>
        }
        return glideRequest!!
    }

    private fun getView(): SubsamplingScaleImageView? {
        return if (imageViewWeakReference != null) {
            imageViewWeakReference!!.get()
        } else null
    }

    fun getUrl(): String {
        return if (url.isNullOrEmpty()) "" else url.toString()
    }

    fun loadImage(obj: Any, @DrawableRes placeholder: Int,onProgressListener: OnProgressListener?): GlideImageLoader<T> {
        if (placeholder != 0) {
            glideRequest = getGlideRequest().placeholder(placeholder)
        }

        if(onProgressListener!=null){
            if (obj is String) {
                url = obj
                ProgressManager().addListener(url as String, onProgressListener)
            }
        }

        if (getView() != null)
            getGlideRequest().load(obj).into(GlideImageViewTarget(getView()!!))
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