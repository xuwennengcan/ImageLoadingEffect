package com.can.image.http

import android.text.TextUtils
import android.util.Log
import com.can.image.inter.InternalProgressListener
import com.can.image.inter.OnProgressListener
import okhttp3.OkHttpClient
import java.util.*

/**
 * Created by CAN on 19-1-5.
 * 图片下载进度网络监听
 */

class ProgressManager{

    companion object {
        private var instance : ProgressManager?=null
        get() {
            if(field==null){
                field = ProgressManager()
            }
            return field
        }
        @Synchronized
        fun get() : ProgressManager{
            return instance!!
        }

        private var listenersMap = Collections.synchronizedMap(HashMap<String, OnProgressListener>())
        private var okHttpClient: OkHttpClient? = null
    }

    private val mProgressListener = object : InternalProgressListener {
        override fun onProgress(url: String, bytesRead: Long, totalBytes: Long) {
            super.onProgress(url, bytesRead, totalBytes)
            val onProgressListener = getProgressListener(url)
            Log.i("ImageLoader"," onProgress , url = $url ")

            if (onProgressListener != null) {
                val percentage = (bytesRead * 1f / totalBytes * 100f).toInt()
                Log.i("ImageLoader","percentage = $percentage ")
                val isComplete = percentage >= 100
                onProgressListener.onProgress(isComplete, percentage, bytesRead, totalBytes)
                if (isComplete) {
                    removeListener(url)
                }
            }
        }
    }

    fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        val request = chain.request()
                        val response = chain.proceed(request)
                        response.newBuilder()
                                .body(ProgressResponseBody(request.url().toString(), mProgressListener, response.body()!!))
                                .build()
                    }
                    .build()
        }
        return okHttpClient!!
    }

    fun addListener(url: String, listener: OnProgressListener?) {
        if (!TextUtils.isEmpty(url) && listener != null) {
            listenersMap[url] = listener
            Log.i("ImageLoader"," addListener : url = $url ")

            listener.onProgress(false, 1, 0, 0)
        }
    }

    fun removeListener(url: String) {
        if (!TextUtils.isEmpty(url)) {
            listenersMap!!.remove(url)
        }
    }

    fun getProgressListener(url: String): OnProgressListener? {
        return if (TextUtils.isEmpty(url) || listenersMap == null || listenersMap.isEmpty() ) {
            Log.i("ImageLoader"," getProgressListener : url null= $url ")
            null
        } else {
            Log.i("ImageLoader"," getProgressListener : url = $url ")
            listenersMap[url]
        }
    }

}
