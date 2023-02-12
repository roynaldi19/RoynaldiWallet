package com.roynaldi19.roynaldiwallet.view.topup

import androidx.lifecycle.ViewModel
import com.roynaldi19.roynaldiwallet.model.UserPreference
import kotlinx.coroutines.flow.first


class TopUpViewModel(private val pref: UserPreference) : ViewModel() {
    suspend fun getToken() = pref.getToken().first()

}