package com.roynaldi19.roynaldiwallet.view.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.roynaldi19.roynaldiwallet.api.ApiConfig
import com.roynaldi19.roynaldiwallet.databinding.ActivityRegisterBinding
import com.roynaldi19.roynaldiwallet.model.RegisterResponse
import com.roynaldi19.roynaldiwallet.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    private lateinit var activityRegisterBinding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(activityRegisterBinding.root)

        setupView()
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

    private fun setupAction() {
        activityRegisterBinding.btnRegister.setOnClickListener {
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

    private fun register(email: String, password: String, firstName:String, lastName:String){
        showLoading(true)
        val client = ApiConfig().getApiService().register(email, password, firstName, lastName)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                if (response.isSuccessful){
                    showLoading(false)
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }

            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(this@RegisterActivity, "Pendaftaran gagal", Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityRegisterBinding.progressBar.visibility = View.VISIBLE
        } else {
            activityRegisterBinding.progressBar.visibility = View.GONE
        }
    }
}