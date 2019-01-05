package com.can.image.`interface`

/**
 * Created by CAN on 19-1-5.
 *
 */
interface InternalProgressListener {
    fun onProgress(url: String, bytesRead: Long, totalBytes: Long){}
}