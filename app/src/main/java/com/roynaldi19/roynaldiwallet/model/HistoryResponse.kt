package com.roynaldi19.roynaldiwallet.model

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("data")
	val data: List<DataItemHistory>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
)

data class DataItemHistory(

	@field:SerializedName("transaction_id")
	val transactionId: String,

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("transaction_time")
	val transactionTime: String,

	@field:SerializedName("transaction_type")
	val transactionType: String
)
