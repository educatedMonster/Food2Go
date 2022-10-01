package com.example.food2go.network

import com.example.food2go.utilities.helpers.UploadRequestBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File


fun toRequestBody(value: Long?): RequestBody {
    return (value!!).toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

fun toRequestBody(value: Float?): RequestBody {
    return (value!!).toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

fun toRequestBody(value: Double?): RequestBody {
    return (value!!).toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

fun toRequestBody(value: Int): RequestBody {
    return value.toString().toRequestBody("text/plain".toMediaTypeOrNull())
}

fun toRequestBody(string: String): RequestBody {
    return string.toRequestBody("text/plain".toMediaTypeOrNull())
}

fun toRequestBody(jsonObject: JSONObject): RequestBody {
    return jsonObject.toString()
        .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
}

fun imageToRequestBody(file: File): RequestBody {
    return file.asRequestBody("image/*".toMediaTypeOrNull())
}

fun videoToRequestBody(file: File): RequestBody {
    return file.asRequestBody("video/*".toMediaTypeOrNull())
}

fun pdfToRequestBody(file: File): RequestBody {
    return file.asRequestBody("application/pdf".toMediaTypeOrNull())
}

fun fileToMultipartBody(partName: String, file: File, fileContentType: String): MultipartBody.Part {
    val requestBody = file.asRequestBody(fileContentType.toMediaTypeOrNull())
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