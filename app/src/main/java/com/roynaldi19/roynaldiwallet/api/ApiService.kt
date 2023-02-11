package com.roynaldi19.roynaldiwallet.api

import com.roynaldi19.roynaldiwallet.model.BalanceResponse
import com.roynaldi19.roynaldiwallet.model.LoginResponse
import com.roynaldi19.roynaldiwallet.model.RegisterResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("registration")
    @FormUrlEncoded
    fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("first_name") fistName: String,
        @Field("last_name") lastName: String
    ): Call<RegisterResponse>

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("balance")
    fun getBalance(
        @Header("Authorization") token: String
    ): Call<BalanceResponse>


}