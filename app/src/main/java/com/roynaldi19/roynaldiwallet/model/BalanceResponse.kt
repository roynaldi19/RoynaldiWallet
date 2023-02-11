package com.roynaldi19.roynaldiwallet.model

import com.google.gson.annotations.SerializedName

data class BalanceResponse(

	@field:SerializedName("data")
	val data: DataBalance? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class DataBalance(

	@field:SerializedName("balance")
	val balance: Any? = null
)
