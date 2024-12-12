package com.dicoding.capstone.ui.history

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.capstone.R
import com.dicoding.capstone.data.local.ResultEntity
import com.dicoding.capstone.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyList: List<ResultEntity>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemHistoryBinding.bind(view)

        fun bind(result: ResultEntity) {
            binding.imgResult.setImageURI(Uri.parse(result.imageUri))
            binding.tvCondition.text = "Condition: ${result.condition}"
            binding.tvPredictionScore.text = "Score: ${result.predictionScore}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

    override fun getItemCount(): Int = historyList.size
}
