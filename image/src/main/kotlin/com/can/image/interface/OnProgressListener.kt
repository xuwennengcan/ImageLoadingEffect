package com.can.image.`interface`

/**
 * Created by CAN on 19-1-5.
 * 下载图片进度监听
 */
interface OnProgressListener {
    /**
     * @param isComplete 是否完成
     * @param percentage 下载进度
     * @param bytesRead 已下载字节
     * @param totalBytes 总字节
     */
    fun onProgress(isComplete: Boolean, percentage: Int, bytesRead: Long, totalBytes: Long) {}
}