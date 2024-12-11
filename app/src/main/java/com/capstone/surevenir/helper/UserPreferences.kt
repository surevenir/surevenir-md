package com.capstone.surevenir.helper

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.capstone.surevenir.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences @Inject constructor(@ApplicationContext private val context: Context){

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val RESET_DONE = booleanPreferencesKey("reset_done")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_EMAIL = stringPreferencesKey(name = "user_email")
        val USER_NAME = stringPreferencesKey("user_name")
        val HAS_SEEN_ONBOARDING = booleanPreferencesKey("has_seen_onboarding")
    }

    suspend fun resetDataIfNeeded() {
        if (BuildConfig.RESET_DATA_ON_LAUNCH) {
            val isResetDone = context.dataStore.data.map { preferences ->
                preferences[RESET_DONE] ?: false
            }.first()

            if (!isResetDone) {
                clearLoginData()
                saveOnboardingState(false)

                context.dataStore.edit { preferences ->
                    preferences[RESET_DONE] = true
                }
            }
        }
    }


    suspend fun saveLoginState(isLoggedIn: Boolean, token: String, email: String, username: String){
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            if (token != null) preferences[USER_TOKEN] = token
            if (token != null) preferences[USER_EMAIL] = email
            if (username.isNotEmpty()) preferences[USER_NAME] = username

        }
    }

    suspend fun clearLoginData() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false

            // Jangan hapus token di mode release
            if (BuildConfig.DEBUG) {
                preferences.remove(USER_TOKEN)
            }

            preferences.remove(USER_EMAIL)
            preferences.remove(USER_NAME)
        }
    }


    suspend fun saveOnboardingState(hasSeenOnboarding: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HAS_SEEN_ONBOARDING] = hasSeenOnboarding
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val hasSeenOnboarding: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[HAS_SEEN_ONBOARDING] ?: false
    }

    val userToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN]
    }

    val userName: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }
}