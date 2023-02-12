package com.roynaldi19.roynaldiwallet.model

import com.google.gson.annotations.SerializedName

data class TransferResponse(

	@field:SerializedName("data")
	val data: Any,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)
