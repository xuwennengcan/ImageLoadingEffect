package com.can.image.inter

/**
 * Created by CAN on 19-1-10.
 *
 */

interface OnProgressListener {
    fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long)
}
