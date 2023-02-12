package com.roynaldi19.roynaldiwallet.view.trasfer

import androidx.lifecycle.ViewModel
import com.roynaldi19.roynaldiwallet.model.UserPreference
import kotlinx.coroutines.flow.first


class TransferViewModel(private val pref: UserPreference) : ViewModel() {
    suspend fun getToken() = pref.getToken().first()

}