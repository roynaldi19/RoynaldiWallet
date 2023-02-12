package com.roynaldi19.roynaldiwallet.view.topup

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.roynaldi19.roynaldiwallet.ViewModelFactory
import com.roynaldi19.roynaldiwallet.databinding.ActivityTopUpBinding
import com.roynaldi19.roynaldiwallet.model.UserPreference

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class TopUpActivity : AppCompatActivity() {
    private lateinit var activityTopUpBinding: ActivityTopUpBinding
    private lateinit var topUpViewModel: TopUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTopUpBinding = ActivityTopUpBinding.inflate(layoutInflater)
        setContentView(activityTopUpBinding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        topUpViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[TopUpViewModel::class.java]

    }

    private fun setupAction() {
        activityTopUpBinding.btnUpdate.setOnClickListener {

            val amount = activityTopUpBinding.edtTopUp.text.toString()
            when {

                amount.isEmpty() -> {
                    activityTopUpBinding.edtTopUp.error =
                        "Masukkan nama Nilai Top Up"
                }

                else -> {
                    topUp(amount.toInt())
                }
            }
        }
    }

    private fun topUp(amount: Int){


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
        supportActionBar?.title = "Top Up"
    }
}