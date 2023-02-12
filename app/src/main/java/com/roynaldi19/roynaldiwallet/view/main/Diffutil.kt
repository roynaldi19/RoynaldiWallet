package com.roynaldi19.roynaldiwallet.view.main

import androidx.recyclerview.widget.DiffUtil
import com.roynaldi19.roynaldiwallet.model.DataItemHistory

class DiffUtil(
    private val oldHistory: List<DataItemHistory>,
    private val newHistory: List<DataItemHistory>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldHistory.size

    override fun getNewListSize() = newHistory.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldHistory[oldItemPosition].transactionId == newHistory[newItemPosition].transactionId

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldHistory[oldItemPosition] == newHistory[newItemPosition]
}