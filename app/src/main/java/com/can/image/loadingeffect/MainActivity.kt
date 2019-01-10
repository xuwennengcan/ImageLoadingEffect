package com.can.image.loadingeffect

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.can.image.glide.GlideImageView
import com.can.image.glide.GlideOptions
import com.can.image.inter.OnProgressListener
import com.can.image.widget.CircleProgressView

class MainActivity : AppCompatActivity() {

    private var mIv : GlideImageView? = null
    private var mCpv : CircleProgressView ? = null
    private val mImageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546680464160&di=e95726d0e3325cb98f72585e91b82b46&imgtype=0&src=http%3A%2F%2Fwx3.sinaimg.cn%2Forj360%2Fa2ee95c9gy1flcvtcsybsj20fp4umwqx.jpg"
    private val image = "https://static.dingtalk.com/media/lADPDgQ9qb7pRqvNC9DND8A_4032_3024.jpg"

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val options : GlideOptions = GlideOptions()
                .skipMemoryCache(true)
                .error(R.drawable.ic_launcher_foreground)

        mIv = findViewById(R.id.iv)
        mCpv = findViewById(R.id.cpv)
        mIv!!.apply(options)
        mIv!!.load(image,R.drawable.ic_launcher_foreground,object :OnProgressListener{
            override fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) {
                mCpv!!.progress = percentage
                mCpv!!.visibility = if(isComplete) View.GONE else View.VISIBLE
                Log.i("ImageLoader","isComplete = $isComplete , percentage = $percentage , bytesRead =  $bytesRead , totalBytes = $totalBytes ")
            }
        })
    }
}
