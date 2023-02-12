package com.roynaldi19.roynaldiwallet.view.updateProfile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.roynaldi19.roynaldiwallet.databinding.ActivityUpdateProfileBinding
import com.roynaldi19.roynaldiwallet.model.UpdateProfileResponse
import com.roynaldi19.roynaldiwallet.model.UserPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var activityUpdateProfileBinding: ActivityUpdateProfileBinding
    private lateinit var updateProfileViewModel: UpdateProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityUpdateProfileBinding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(activityUpdateProfileBinding.root)

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
        activityUpdateProfileBinding.btnUpdate.setOnClickListener {

            val firstName = activityUpdateProfileBinding.edtFirstName.text.toString()
            val lastName = activityUpdateProfileBinding.edtLastName.text.toString()
            when {

                firstName.isEmpty() -> {
                    activityUpdateProfileBinding.edtFirstNameTextLayout.error =
                        "Masukkan nama Depan"
                }
                lastName.isEmpty() -> {
                    activityUpdateProfileBinding.edtLastNameTextLayout.error =
                        "Masukkan nama Belakang"
                }
                else -> {
                    updateProfile(firstName, lastName)
                }
            }
        }
    }

    private fun updateProfile(firstName: String, lastName: String) {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${updateProfileViewModel.getToken()}"
            val client = ApiConfig().getApiService().updateProfile(token, firstName, lastName)
            Log.i("test1", token)
            Log.i("test2", firstName)
            //di sini data firstname masih terbaca dari editText
            client.enqueue(object : Callback<UpdateProfileResponse> {
                override fun onResponse(
                    call: Call<UpdateProfileResponse>,
                    response: Response<UpdateProfileResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            Log.i("test3", responseBody.toString())
                            //setelah di kirimkan ke api data fistname menjadi kosong.
                            Toast.makeText(
                                this@UpdateProfileActivity,
                                "Update Data Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable) {
                    Toast.makeText(
                        this@UpdateProfileActivity,
                        "Update Data gagal",
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
        supportActionBar?.title = "Update Profile"
    }
}