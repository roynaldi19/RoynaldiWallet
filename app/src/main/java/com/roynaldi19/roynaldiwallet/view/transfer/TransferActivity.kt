package com.roynaldi19.roynaldiwallet.view.transfer

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
import com.roynaldi19.roynaldiwallet.databinding.ActivityTransferBinding
import com.roynaldi19.roynaldiwallet.model.TransferResponse
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

class TransferActivity : AppCompatActivity() {

    private lateinit var activityTransferBinding: ActivityTransferBinding
    private lateinit var transferViewModel: TransferViewModel

    companion object {
        const val EXTRA_TRANSFER = "extra_transfer"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTransferBinding = ActivityTransferBinding.inflate(layoutInflater)
        setContentView(activityTransferBinding.root)

        setupView()
        setupViewModel()
        setupAction()

        val maxTransfer = intent.getIntExtra(EXTRA_TRANSFER, 0)
        activityTransferBinding.tvBalanceTransfer.text = maxTransfer.toString()
    }

    private fun setupViewModel() {
        transferViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[TransferViewModel::class.java]

    }

    private fun setupAction() {
        activityTransferBinding.btnTransfer.setOnClickListener {

            val amount = activityTransferBinding.edtTransfer.text.toString().trim()
            when {

                amount.isEmpty() -> {
                    activityTransferBinding.edtTransfer.error =
                        "Masukkan Nilai Transfer"
                }

                else -> {
                    transfer(amount.toInt())
                }
            }
        }
    }

    private fun transfer(amount: Int){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${transferViewModel.getToken()}"
            val client = ApiConfig().getApiService().transfer(token, amount)
            client.enqueue(object : Callback<TransferResponse> {
                override fun onResponse(
                    call: Call<TransferResponse>,
                    response: Response<TransferResponse>
                ) {
                    if (response.isSuccessful){
                        val responseBody = response.body()
                        if(responseBody != null) {
                            Toast.makeText(
                                this@TransferActivity,
                                "Transfer Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@TransferActivity, MainActivity::class.java)
                            startActivity(intent)

                        }
                    }

                }

                override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                    Toast.makeText(
                        this@TransferActivity,
                        "Transfer Gagal",
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
        supportActionBar?.title = "Transfer"
    }
}