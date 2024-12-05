package com.capstone.surevenir.ui.camera

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ImageCaptureVM : ViewModel() {
    var currentImageUri by mutableStateOf<Uri?>(null)
        private set

    fun setImageUri(uri: Uri?) {
        currentImageUri = uri
    }
}
