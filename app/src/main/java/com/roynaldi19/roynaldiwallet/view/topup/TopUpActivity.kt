package com.roynaldi19.roynaldiwallet.view.topup

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.roynaldi19.roynaldiwallet.ViewModelFactory
import com.roynaldi19.roynaldiwallet.api.ApiConfig
import com.roynaldi19.roynaldiwallet.databinding.ActivityTopUpBinding
import com.roynaldi19.roynaldiwallet.model.TopUpResponse
import com.roynaldi19.roynaldiwallet.model.UserPreference
import com.roynaldi19.roynaldiwallet.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors

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
        activityTopUpBinding.btnTopUp.setOnClickListener {

            val amount = activityTopUpBinding.edtTopUp.text.toString().trim()
            when {

                amount.isEmpty() -> {
                    activityTopUpBinding.edtTopUp.error =
                        "Masukkan Nilai Top Up"
                }

                else -> {
                    topUp(amount.toInt())
                }
            }
        }
    }

    private fun topUp(amount: Int) {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${topUpViewModel.getToken()}"
            val client = ApiConfig().getApiService().topUp(token, amount)
            client.enqueue(object : Callback<TopUpResponse> {
                override fun onResponse(
                    call: Call<TopUpResponse>,
                    response: Response<TopUpResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Toast.makeText(
                                this@TopUpActivity,
                                "Top Up Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@TopUpActivity, MainActivity::class.java)
                            startActivity(intent)

                        }
                    }

                }

                override fun onFailure(call: Call<TopUpResponse>, t: Throwable) {
                    Toast.makeText(
                        this@TopUpActivity,
                        "Top Up Gagal",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            })
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
        supportActionBar?.title = "Top Up"
    }
}