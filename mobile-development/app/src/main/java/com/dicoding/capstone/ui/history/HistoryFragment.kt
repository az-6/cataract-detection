package com.dicoding.capstone.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.capstone.data.local.AppDatabase
import com.dicoding.capstone.data.local.ResultEntity
import com.dicoding.capstone.databinding.FragmentHistoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        loadHistory()
    }

    private fun loadHistory() {
        val database = AppDatabase.getDatabase(requireContext())
        val resultDao = database.resultDao()

        CoroutineScope(Dispatchers.IO).launch {
            val historyList: List<ResultEntity> = resultDao.getAllResults()

            withContext(Dispatchers.Main) {
                if (historyList.isNotEmpty()) {
                    binding.tvEmptyMessage.visibility = View.GONE
                    binding.rvHistory.visibility = View.VISIBLE

                    adapter = HistoryAdapter(historyList)
                    binding.rvHistory.adapter = adapter
                } else {
                    binding.tvEmptyMessage.visibility = View.VISIBLE
                    binding.rvHistory.visibility = View.GONE
                }
            }
        }
    }
}
