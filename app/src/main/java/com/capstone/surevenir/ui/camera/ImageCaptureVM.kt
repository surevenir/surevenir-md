package com.capstone.surevenir.ui.camera

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.network.response.PredictionResponse
import com.capstone.surevenir.data.repository.PredictionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageCaptureVM@Inject constructor(
    private val predictionRepository: PredictionRepository
) : ViewModel() {

    private val _currentImageUri = mutableStateOf<Uri?>(null)
    val currentImageUri: Uri? get() = _currentImageUri.value

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _predictionResult = MutableStateFlow<Result<PredictionResponse>?>(null)
    val predictionResult: StateFlow<Result<PredictionResponse>?> = _predictionResult.asStateFlow()

    fun setImageUri(uri: Uri) {
        _currentImageUri.value = uri
    }

    fun predictImage(token: String, context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                currentImageUri?.let { uri ->
                    val response = predictionRepository.predictImage(token, uri, context)

                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            _predictionResult.value = Result.success(body)
                        } ?: run {
                            _predictionResult.value = Result.failure(Exception("Empty response body"))
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        _predictionResult.value = Result.failure(Exception(errorBody))
                    }
                } ?: run {
                    _predictionResult.value = Result.failure(Exception("No image selected"))
                }
            } catch (e: Exception) {
                _predictionResult.value = Result.failure(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
