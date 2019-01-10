package com.can.image.http

import android.os.Handler
import android.os.Looper
import com.can.image.inter.InternalProgressListener
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * Created by CAN on 19-1-5.
 * 重写ResponseBody
 */

class ProgressResponseBody internal constructor(private val url: String,
                                                private val internalProgressListener: InternalProgressListener?,
                                                private val responseBody: ResponseBody) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead: Long = 0
            internal var lastTotalBytesRead: Long = 0

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead == (-1).toLong()) 0 else bytesRead

                if (lastTotalBytesRead != totalBytesRead) {
                    lastTotalBytesRead = totalBytesRead
                    mainThreadHandler.post { internalProgressListener?.onProgress(url, totalBytesRead, contentLength()) }
                }
                return bytesRead
            }
        }
    }

    companion object {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
    }
}
