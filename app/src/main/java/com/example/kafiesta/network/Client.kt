package com.example.kafiesta.network

import com.example.kafiesta.utilities.helpers.UploadRequestBody
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.util.HashMap

fun toRequestBody(value: Long?): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), (value!!).toString())
}

fun toRequestBody(value: Float?): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), (value!!).toString())
}

fun toRequestBody(value: Double?): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), (value!!).toString())
}

fun toRequestBody(value: Int): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), value.toString())
}

fun toRequestBody(string: String): RequestBody {
    return RequestBody.create(MediaType.parse("text/plain"), string)
}

fun toRequestBody(jsonObject: JSONObject): RequestBody {
    return RequestBody.create(
        MediaType.parse("application/json; charset=utf-8"),
        jsonObject.toString()
    )
}

fun imageToRequestBody(file: File): RequestBody {
    return RequestBody.create(MediaType.parse("image/*"), file)
}

fun videoToRequestBody(file: File): RequestBody {
    return RequestBody.create(MediaType.parse("video/*"), file)
}

fun pdfToRequestBody(file: File): RequestBody {
    return RequestBody.create(MediaType.parse("application/pdf"), file)
}

fun fileToMultipartBody(partName: String, file: File, fileContentType: String): MultipartBody.Part {
    val requestBody = RequestBody.create(MediaType.parse(fileContentType), file)
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}

fun fileToMultipartBodyWithProgress(
    partName: String,
    file: File,
    fileContentType: String,
    callback: UploadRequestBody.UploadCallback,
    currentCount: Int,
    size: Int,
): MultipartBody.Part {
    val requestBody = UploadRequestBody(file, fileContentType, callback, currentCount, size)
    return MultipartBody.Part.createFormData(partName, file.name, requestBody)
}

private fun getFileNameFormat(label: String, file: File): String {
    return label + "\"; filename=\"" + file.name + "\" "
}


fun paramsToRequestBody(params: HashMap<String, Any>): HashMap<String, RequestBody> {
    val fieldMap = HashMap<String, RequestBody>()
    val it = params.entries.iterator()
    while (it.hasNext()) {
        val pair = it.next() as Map.Entry<*, *>

        val key = pair.key.toString()
        when (pair.value) {
            is String -> {
                fieldMap[key] = toRequestBody(pair.value.toString())
            }
            is Int -> {
                val `val` = pair.value as Int
                fieldMap[key] = toRequestBody(`val`)
            }
            is Double -> {
                val `val` = pair.value as Double
                fieldMap[key] = toRequestBody(`val`)
            }
            is Long -> {
                val `val` = pair.value as Long
                fieldMap[key] = toRequestBody(`val`)
            }
            is JSONObject -> {
                val `object` = pair.value as JSONObject
                fieldMap[key] = toRequestBody(`object`)
            }
            is File -> {
                val f = pair.value as File
                when {
                    isVideo(f) -> fieldMap[getFileNameFormat(key, f)] = videoToRequestBody(f)
                    isPdf(f) -> fieldMap[getFileNameFormat(key, f)] = pdfToRequestBody(f)
                    else -> fieldMap[getFileNameFormat(key, f)] = imageToRequestBody(f)
                }

            }
        }
        it.remove()
    }
    return fieldMap
}

private fun isVideo(file: File): Boolean {
    val name = file.name
    return (name.contains(".mp4") || name.contains(".mp3") || name.contains(".mkv")
            || name.contains("wav") || name.contains("webm"))
}

private fun isPdf(file: File): Boolean {
    return file.name.contains(".pdf")
}