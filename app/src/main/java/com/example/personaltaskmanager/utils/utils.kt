package com.example.personaltaskmanager.utils


import android.content.Context
import android.net.Uri

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
fun prepareFilePart(uri: Uri, context: Context): MultipartBody.Part {
    val inputStream = context.contentResolver.openInputStream(uri)!!
    val file = File(context.cacheDir, inputStream.toString())
    file.outputStream().use { outputStream ->
        inputStream.copyTo(outputStream)
    }
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData("imageurl", file.name, requestFile)
}

fun String.toRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaTypeOrNull())
