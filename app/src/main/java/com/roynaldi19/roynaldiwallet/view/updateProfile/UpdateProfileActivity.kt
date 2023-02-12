package com.roynaldi19.roynaldiwallet.view.updateProfile

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.roynaldi19.roynaldiwallet.R
import com.roynaldi19.roynaldiwallet.databinding.ActivityMainBinding
import com.roynaldi19.roynaldiwallet.databinding.ActivityUpdateProfileBinding
import com.roynaldi19.roynaldiwallet.view.main.MainViewModel

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var updateProfileBinding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateProfileBinding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(updateProfileBinding.root)
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