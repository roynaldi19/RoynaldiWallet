package com.roynaldi19.roynaldiwallet.view.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.roynaldi19.roynaldiwallet.R
import com.roynaldi19.roynaldiwallet.ViewModelFactory
import com.roynaldi19.roynaldiwallet.api.ApiConfig
import com.roynaldi19.roynaldiwallet.databinding.ActivityMainBinding
import com.roynaldi19.roynaldiwallet.model.BalanceResponse
import com.roynaldi19.roynaldiwallet.model.HistoryResponse
import com.roynaldi19.roynaldiwallet.model.ProfileResponse
import com.roynaldi19.roynaldiwallet.model.UserPreference
import com.roynaldi19.roynaldiwallet.view.login.LoginActivity
import com.roynaldi19.roynaldiwallet.view.topup.TopUpActivity
import com.roynaldi19.roynaldiwallet.view.trasfer.TransferActivity
import com.roynaldi19.roynaldiwallet.view.updateProfile.UpdateProfileActivity
import com.roynaldi19.roynaldiwallet.view.welcome.WelcomeActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.rvHistoryTransaction.layoutManager = LinearLayoutManager(this)
        mainBinding.rvHistoryTransaction.setHasFixedSize(true)
        mainAdapter = MainAdapter()

        mainBinding.btnTopUp.setOnClickListener {
            val intent = Intent(this@MainActivity, TopUpActivity::class.java)
            startActivity(intent)
        }

        mainBinding.btnTransfer.setOnClickListener {
            val intent = Intent(this@MainActivity, TransferActivity::class.java)
            startActivity(intent)
        }

        setupViewModel()
        setupView()
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
        supportActionBar?.title = "NuTech Wallet"
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getState().observe(this) {
            if (it) {
                loadProfile()
                loadBalance()
                loadTransactionHistory()
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
                val scope = CoroutineScope(dispatcher)
                scope.launch {
                    mainViewModel.removeToken()
                    mainViewModel.logout()
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
                return true
            }

            R.id.action_update_profile -> {
                val intent = Intent(this, UpdateProfileActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadProfile() {
        showLoading(true)
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${mainViewModel.getToken()}"
            val client = ApiConfig().getApiService().getProfile(token)
            client.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(
                    call: Call<ProfileResponse>,
                    response: Response<ProfileResponse>
                ) {
                    showLoading(false)
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            mainBinding.tvFirstName.text = responseBody.data.firstName
                            mainBinding.tvLastName.text = responseBody.data.lastName

                        }
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    Log.e("onFailure", t.message.toString())

                }
            })
        }
    }


    private fun loadBalance() {
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        scope.launch {
            val token = "Bearer ${mainViewModel.getToken()}"
            val client = ApiConfig().getApiService().getBalance(token)
            client.enqueue(object : Callback<BalanceResponse> {
                override fun onResponse(
                    call: Call<BalanceResponse>,
                    response: Response<BalanceResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            mainBinding.tvBalance.text = responseBody.data.balance.toString()

                        }
                    }
                }

                override fun onFailure(call: Call<BalanceResponse>, t: Throwable) {
                    Log.e("onFailure", t.message.toString())

                }
            })
        }
    }

    private fun loadTransactionHistory() {
        mainBinding.apply {
            val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
            val scope = CoroutineScope(dispatcher)
            scope.launch {
                val token = "Bearer ${mainViewModel.getToken()}"
                val client = ApiConfig().getApiService().getTransactionHistory(token)
                client.enqueue(object : Callback<HistoryResponse> {
                    override fun onResponse(
                        call: Call<HistoryResponse>,
                        response: Response<HistoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                mainAdapter.setData(responseBody.data)
                                mainBinding.rvHistoryTransaction.adapter = mainAdapter

                            }
                        }
                    }

                    override fun onFailure(call: Call<HistoryResponse>, t: Throwable) {
                        Log.e("onFailure", t.message.toString())

                    }
                })
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            mainBinding.progressBar.visibility = View.VISIBLE
        } else {
            mainBinding.progressBar.visibility = View.GONE

        }
    }


}