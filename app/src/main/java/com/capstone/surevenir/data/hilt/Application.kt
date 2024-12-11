package com.capstone.surevenir.data.hilt

import android.app.Application
import com.capstone.surevenir.helper.UserPreferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            userPreferences.resetDataIfNeeded()
        }
    }
}