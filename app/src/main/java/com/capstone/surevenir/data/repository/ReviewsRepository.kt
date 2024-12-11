package com.capstone.surevenir.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.capstone.surevenir.data.network.ApiService
import com.capstone.surevenir.data.network.response.CategoryResponse
import com.capstone.surevenir.data.network.response.ReviewRequest
import com.capstone.surevenir.data.network.response.ReviewResponse
import com.capstone.surevenir.data.network.response.ReviewsResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewsRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
){

    suspend fun getReviews(productId: Int, token: String): Response<ReviewsResponse> {
        return apiService.getReviews(productId, token)
    }

    suspend fun postReview(
        token: String,
        rating: Int,
        comment: String,
        productId: Int,
        imageUris: List<String>
    ): Response<ReviewResponse> {
        try {
            // Ensure token format
            val authToken = "Bearer ${token.replace("Bearer ", "")}"

            // Log token for debugging
            Log.d("ReviewsRepository", "Using token: $authToken")

            // Create request bodies
            val ratingBody = rating.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val commentBody = comment.toRequestBody("text/plain".toMediaTypeOrNull())
            val productIdBody = productId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            // Process images
            val imageParts = imageUris.mapNotNull { uriString ->
                try {
                    val uri = Uri.parse(uriString)
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        // Get file name or generate one
                        val fileName = getFileNameFromUri(uri) ?: "image_${System.currentTimeMillis()}.jpg"

                        // Create temp file
                        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
                        tempFile.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }

                        // Create MultipartBody.Part
                        val requestBody = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("images", fileName, requestBody)
                    }
                } catch (e: Exception) {
                    Log.e("ReviewsRepository", "Error processing image: ${e.message}")
                    null
                }
            }

            // Log request details
            Log.d("ReviewsRepository", "Sending review with ${imageParts.size} images")

            // Make API call
            return apiService.postReview(
                token = authToken,
                rating = ratingBody,
                comment = commentBody,
                productId = productIdBody,
                images = imageParts
            )
        } catch (e: Exception) {
            Log.e("ReviewsRepository", "Error in postReview: ${e.message}")
            throw e
        }
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex("_display_name")
            if (nameIndex != -1 && cursor.moveToFirst()) {
                cursor.getString(nameIndex)
            } else null
        }
    }

}