package com.roynaldi19.roynaldiwallet.view.updateProfile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.roynaldi19.roynaldiwallet.ViewModelFactory
import com.roynaldi19.roynaldiwallet.databinding.ActivityUpdateProfileBinding
import com.roynaldi19.roynaldiwallet.model.UserPreference
import com.roynaldi19.roynaldiwallet.view.login.LoginViewModel
import com.roynaldi19.roynaldiwallet.view.login.dataStore


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var updateProfileBinding: ActivityUpdateProfileBinding
    private lateinit var updateProfileViewModel: UpdateProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateProfileBinding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(updateProfileBinding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        updateProfileViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[UpdateProfileViewModel::class.java]

    }

    private fun setupAction() {
        updateProfileViewModel.btnRegister.setOnClickListener {
            val email = activityRegisterBinding.edtEmail.text.toString()
            val password = activityRegisterBinding.edtPassword.text.toString()
            val firstName = activityRegisterBinding.edtFirstName.text.toString()
            val lastName = activityRegisterBinding.edtLastName.text.toString()
            when {
                email.isEmpty() -> {
                    activityRegisterBinding.edtEmailTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    activityRegisterBinding.edtPasswordTextLayout.error = "Masukkan password"
                }
                firstName.isEmpty() -> {
                    activityRegisterBinding.edtFirstNameTextLayout.error = "Masukkan nama Depan"
                }
                lastName.isEmpty() -> {
                    activityRegisterBinding.edtLastNameTextLayout.error = "Masukkan nama Belakang"
                }
                else -> {
                    register(email, password, firstName, lastName)
                }
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.title = "Update Profile"
    }
}