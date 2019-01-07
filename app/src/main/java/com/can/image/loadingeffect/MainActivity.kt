package com.can.image.loadingeffect

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.can.image.`interface`.OnProgressListener
import com.can.image.glide.GlideRequest
import com.can.image.loadingeffect.glide.GlideImageLoader
import com.can.image.widget.CircleProgressView
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File

class MainActivity : AppCompatActivity() {

    private var mIv : SubsamplingScaleImageView ? = null
    private var mCpv : CircleProgressView ? = null
    private val mImageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546680464160&di=e95726d0e3325cb98f72585e91b82b46&imgtype=0&src=http%3A%2F%2Fwx3.sinaimg.cn%2Forj360%2Fa2ee95c9gy1flcvtcsybsj20fp4umwqx.jpg"
    private val image = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546680464160&di=e320a1c0f88aad70a88dff9d84cc785f&imgtype=0&src=http%3A%2F%2Fwx1.sinaimg.cn%2Forj360%2F66dc402ely1fv7ztsb871j20u046ohdt.jpg"

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val options : RequestOptions = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

        mIv = findViewById(R.id.iv)
        mCpv = findViewById(R.id.cpv)

        val imageLoader = GlideImageLoader<File>(mIv!!)
        imageLoader.getGlideRequest().apply(options)
        imageLoader.loadImage(image,0,object :OnProgressListener{
            override fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) {
                super.onProgress(isComplete, percentage, bytesRead, totalBytes)
                mCpv!!.progress = percentage
                mCpv!!.visibility = if(isComplete) View.GONE else View.VISIBLE
                Log.i("ImageLoader","isComplete = $isComplete , percentage = $percentage , bytesRead =  $bytesRead , totalBytes = $totalBytes ")
            }
        })
    }
}
