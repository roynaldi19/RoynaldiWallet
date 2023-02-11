package com.roynaldi19.roynaldiwallet.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roynaldi19.roynaldiwallet.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    fun saveState(isLogin: Boolean) {
        viewModelScope.launch {
            pref.saveState(isLogin)
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }
}