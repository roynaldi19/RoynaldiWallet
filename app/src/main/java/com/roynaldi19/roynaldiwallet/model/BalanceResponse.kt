package com.roynaldi19.roynaldiwallet.model

import com.google.gson.annotations.SerializedName

data class BalanceResponse(

	@field:SerializedName("data")
	val data: BalanceData,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class BalanceData(

	@field:SerializedName("balance")
	val balance: Int
)
