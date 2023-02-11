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

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            val firstName = binding.edtFirstName.text.toString()
            val lastName = binding.edtLastName.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edtEmailTextLayout.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edtPasswordTextLayout.error = "Masukkan password"
                }
                firstName.isEmpty() -> {
                    binding.edtFirstNameTextLayout.error = "Masukkan nama"
                }
                lastName.isEmpty() -> {
                    binding.edtLastNameTextLayout.error = "Masukkan nama"
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
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}