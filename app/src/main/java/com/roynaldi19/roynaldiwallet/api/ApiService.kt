package com.roynaldi19.roynaldiwallet.api

import com.roynaldi19.roynaldiwallet.model.*
import okhttp3.RequestBody
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

    @GET("getProfile")
    fun getProfile(
        @Header("Authorization") token: String
    ): Call<ProfileResponse>

    @GET("balance")
    fun getBalance(
        @Header("Authorization") token: String
    ): Call<BalanceResponse>

    @GET("transactionHistory")
    fun getTransactionHistory(
        @Header("Authorization") token: String
    ): Call<HistoryResponse>

    @POST("updateProfile")
    @FormUrlEncoded
    fun updateProfile(
        @Header("Authorization") token: String,
        @Field("first_name") fistName: String,
        @Field("last_name") lastName: String
    ): Call<UpdateProfileResponse>

    @POST("topup")
    @FormUrlEncoded
    fun topUp(
        @Header("Authorization") token: String,
        @Field("amount") amount: Int,
    ): Call<TopUpResponse>

    @POST("transfer")
    @FormUrlEncoded
    fun transfer(
        @Header("Authorization") token: String,
        @Field("amount") amount: Int,
    ): Call<TransferResponse>


}