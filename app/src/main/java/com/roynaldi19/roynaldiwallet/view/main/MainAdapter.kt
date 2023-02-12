package com.roynaldi19.roynaldiwallet.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.roynaldi19.roynaldiwallet.databinding.ItemHistoryBinding
import com.roynaldi19.roynaldiwallet.model.DataItemHistory

class MainAdapter : RecyclerView.Adapter<MainAdapter.HistoryViewHolder>() {
    private val listHistory = ArrayList<DataItemHistory>()

    fun setData(items: ArrayList<DataItemHistory>) {
        listHistory.clear()
        listHistory.addAll(items)
        notifyDataSetChanged()

    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): HistoryViewHolder =
        HistoryViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        )

    override fun onBindViewHolder(historyViewHolder: HistoryViewHolder, position: Int) {
        historyViewHolder.bind(listHistory[position])
    }

    override fun getItemCount() = listHistory.size

    class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(dataItemHistory: DataItemHistory) {
            binding.tvTransactionAmount.text = dataItemHistory.amount.toString()
            binding.tvTransactionTime.text = dataItemHistory.transactionTime
            binding.tvTransactionType.text = dataItemHistory.transactionType

        }
    }
}