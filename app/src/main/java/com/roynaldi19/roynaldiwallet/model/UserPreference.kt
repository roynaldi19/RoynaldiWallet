package com.roynaldi19.roynaldiwallet.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val stateKey = booleanPreferencesKey(STATE_KEY)
    private val tokenKey = stringPreferencesKey(TOKEN_KEY)

    suspend fun saveState(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[stateKey] = isLogin
        }
    }

    fun getState(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[stateKey] ?: false
        }

    suspend fun logout() {
        dataStore.edit { preferences ->
            if (preferences.contains(stateKey)) {
                preferences.remove(stateKey)
            }
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    fun getToken(): Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[tokenKey]
        }

    suspend fun removeToken() {
        dataStore.edit { preferences ->
            if (preferences.contains(tokenKey)) {
                preferences.remove(tokenKey)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null
        private const val STATE_KEY = "state_key"
        private const val TOKEN_KEY = "token_key"

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}