package com.capstone.surevenir.helper

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences (private val context: Context){

    companion object {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_EMAIL = stringPreferencesKey(name = "user_email")
    }

    suspend fun saveLoginState(isLoggedIn: Boolean, token: String, email: String){
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            if (token != null) preferences[USER_TOKEN] = token
            if (token != null) preferences[USER_EMAIL] = email
        }
    }

    suspend fun clearLoginData() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences.remove(USER_TOKEN)
            preferences.remove(USER_EMAIL)
        }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userToken: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_TOKEN]
    }

    val userEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_EMAIL]
    }


}