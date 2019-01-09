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
    private val image = "https://static.dingtalk.com/media/lADPDgQ9qb7pRqvNC9DND8A_4032_3024.jpg?auth_bizType=IM&auth_bizEntity=%7B%22cid%22%3A%224248001%3A284146280%22%2C%22msgId%22%3A%22769665707059%22%7D&open_id=284146280"

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val options : RequestOptions = RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)

        mIv = findViewById(R.id.iv)
        mCpv = findViewById(R.id.cpv)

        val imageLoader = GlideImageLoader<File>(mIv!!)
        //imageLoader.getGlideRequest().apply(options)
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
