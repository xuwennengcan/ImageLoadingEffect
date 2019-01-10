package com.can.image.glide

import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.can.image.http.ProgressManager
import com.can.image.inter.OnProgressListener
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File

class GlideImageViewTarget<T> internal constructor(private val url: String, private val view: SubsamplingScaleImageView?, private val onProgressListener: OnProgressListener?) : SimpleTarget<T>() {

    override fun onStart() {
        ProgressManager().addListener(url, onProgressListener)
    }

    override fun onLoadFailed(errorDrawable: Drawable?) {
        val onProgressListener = ProgressManager().getProgressListener(url)
        if (onProgressListener != null) {
            onProgressListener.onProgress(true, 100, 0, 0)
            ProgressManager().removeListener(url)
        }
    }

    override fun onResourceReady(file: T, transition: Transition<in T>?) {
        val onProgressListener = ProgressManager().getProgressListener(url)
        if (onProgressListener != null) {
            onProgressListener.onProgress(true, 100, 0, 0)
            ProgressManager().removeListener(url)
        }
        if (file is File) {
            view?.setImage(ImageSource.uri(Uri.fromFile(file as File)))
        }
    }
}