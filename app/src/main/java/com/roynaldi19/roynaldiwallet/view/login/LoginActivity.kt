package com.roynaldi19.roynaldiwallet.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.roynaldi19.roynaldiwallet.api.ApiConfig
import com.roynaldi19.roynaldiwallet.databinding.ActivityLoginBinding
import com.roynaldi19.roynaldiwallet.model.UserPreference
import com.roynaldi19.roynaldiwallet.ViewModelFactory
import com.roynaldi19.roynaldiwallet.model.LoginResponse
import com.roynaldi19.roynaldiwallet.view.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var activityLoginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(activityLoginBinding.root)

        setupView()
        setupViewModel()
        setupAction()
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
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

    }

    private fun setupAction() {
        activityLoginBinding.btnLogin.setOnClickListener {
            val email = activityLoginBinding.edtEmail.text.toString()
            val password = activityLoginBinding.passwordEditText.text.toString()
            when {
                email.isEmpty() -> {
                    activityLoginBinding.edtEmailTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    activityLoginBinding.edtPasswordTextLayout.error = "Masukkan password"
                }
                else -> {
                    login(email, password)

                }
            }
        }
    }

    private fun login(email: String, password: String) {
        showLoading(true)
        val client = ApiConfig().getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null) {
                        loginViewModel.saveState(true)
                        loginViewModel.saveToken(responseBody.data.token)
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityLoginBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityLoginBinding.progressBar.visibility = View.GONE
        }
    }
}