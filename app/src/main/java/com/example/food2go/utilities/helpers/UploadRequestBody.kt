package com.example.food2go.utilities.helpers

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.FileInputStream

class UploadRequestBody(
    private val file: File,
    private val fileContentType: String,
    private val callback: UploadCallback,
    private val currentCount: Int,
    private val size: Int,
) : RequestBody() {

    override fun contentType() = fileContentType.toMediaTypeOrNull()

    override fun contentLength() = file.length()

    override fun writeTo(sink: BufferedSink) {
        val length = file.length()
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val fileInputStream = FileInputStream(file)
        var uploaded = 0L
        fileInputStream.use { inputStream ->
            var read: Int
            val handler = Handler(Looper.getMainLooper())
            while (inputStream.read(buffer).also { read = it } != -1) {
                val percentage = (100 * uploaded / length).toInt()
                handler.post(ProgressUpdater(percentage, file.name, currentCount + 1, size))
                uploaded += read
                sink.write(buffer, 0, read)
            }
        }
    }

    interface UploadCallback {
        fun onProgressUpdate(
            percentage: Int,
            fileName: String,
            currentFileCount: Int,
            fileSizes: Int,
        )
    }

    inner class ProgressUpdater(
        private val percentage: Int,
        private val fileName: String,
        private val currentFileCount: Int,
        private val fileSizes: Int,
    ) : Runnable {
        override fun run() {
            callback.onProgressUpdate(percentage, fileName, currentFileCount, fileSizes)
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}