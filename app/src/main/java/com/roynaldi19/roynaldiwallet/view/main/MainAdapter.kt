package com.roynaldi19.roynaldiwallet.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.roynaldi19.roynaldiwallet.databinding.ItemHistoryBinding
import com.roynaldi19.roynaldiwallet.model.DataItemHistory

class MainAdapter : RecyclerView.Adapter<MainAdapter.HistoryViewHolder>() {
    private var oldHistory = emptyList<DataItemHistory>()

    fun setData(newHistory: List<DataItemHistory>) {
        val diffUtil = DiffUtil(oldHistory, newHistory)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        oldHistory = newHistory
        diffUtilResult.dispatchUpdatesTo(this)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): HistoryViewHolder =
        HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )

    override fun onBindViewHolder(storiesViewHolder: HistoryViewHolder, position: Int) {
        storiesViewHolder.bind(oldHistory[position])
    }

    override fun getItemCount() = oldHistory.size

    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItemHistory: DataItemHistory) {
            binding.tvTransactionAmount.text = dataItemHistory.amount.toString()
            binding.tvTransactionTime.text = dataItemHistory.transactionTime
            binding.tvTransactionAmount.text = dataItemHistory.transactionType

        }
    }
}