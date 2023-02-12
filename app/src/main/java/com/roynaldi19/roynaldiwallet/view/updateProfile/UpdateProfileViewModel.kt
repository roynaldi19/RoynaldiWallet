package com.roynaldi19.roynaldiwallet.view.updateProfile

import androidx.lifecycle.ViewModel
import com.roynaldi19.roynaldiwallet.model.UserPreference
import kotlinx.coroutines.flow.first



class UpdateProfileViewModel(private val pref: UserPreference) : ViewModel() {
    suspend fun getToken() = pref.getToken().first()

}