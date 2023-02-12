package com.roynaldi19.roynaldiwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roynaldi19.roynaldiwallet.model.UserPreference
import com.roynaldi19.roynaldiwallet.view.login.LoginViewModel
import com.roynaldi19.roynaldiwallet.view.main.MainViewModel
import com.roynaldi19.roynaldiwallet.view.updateProfile.UpdateProfileViewModel

class ViewModelFactory(private val pref: UserPreference) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            modelClass.isAssignableFrom(UpdateProfileViewModel::class.java) -> {
                UpdateProfileViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}