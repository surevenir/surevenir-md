package com.capstone.surevenir.data.repository

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.PredictionResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class PredictionRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun predictImage(
        token: String,
        imageUri: Uri,
        context: Context
    ): Response<PredictionResponse> {
        val file = File(getRealPathFromUri(context, imageUri) ?: "")
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        return api.predictImage("Bearer $token", imagePart)
    }

    private fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var result: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                result = it.getString(columnIndex)
            }
        }
        return result
    }
}
