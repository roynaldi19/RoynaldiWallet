package com.roynaldi19.roynaldiwallet.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.roynaldi19.roynaldiwallet.model.UserPreference
import kotlinx.coroutines.flow.first

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getState() = pref.getState().asLiveData()

    suspend fun getToken() = pref.getToken().first()

    suspend fun logout() = pref.logout()

    suspend fun removeToken() = pref.removeToken()
}