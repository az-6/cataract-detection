package com.dicoding.capstone.ui.result

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.capstone.R
import com.dicoding.capstone.databinding.FragmentResultBinding
import com.dicoding.capstone.data.local.AppDatabase
import com.dicoding.capstone.data.local.ResultEntity
import com.dicoding.capstone.ui.history.HistoryFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var imageUri: Uri? = null
    private var condition: String? = null
    private var predictionScore: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Display the uploaded image
        imageUri?.let {
            binding.imgInput.setImageURI(it)
        }

        // Display the condition
        condition?.let {
            binding.tvCondition.text = it
        }

        // Display the prediction score as a string
        predictionScore?.let {
            binding.tvScore.text = it
        }

        // Logic to display message on tvMessage
        condition?.let {
            binding.tvMessage.text = when (it.lowercase()) {
                "cataract" -> "Signs of cataracts detected. Consult a doctor."
                "normal" -> "No signs of cataracts detected. Condition is normal."
                else -> "Result unclear. Try again or consult a doctor."
            }
        }


        // Save result to database when save button is clicked
        binding.btnSave.setOnClickListener {
            val imageUriString = imageUri?.toString() ?: return@setOnClickListener
            val conditionText = condition ?: "Unknown"
            val predictionText = predictionScore ?: "0"

            val result = ResultEntity(
                imageUri = imageUriString,
                condition = conditionText,
                predictionScore = predictionText
            )

            // Insert data into database
            saveResultToDatabase(result)

            // Pindah ke HistoryFragment
            val selectedFragment = HistoryFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .addToBackStack(null) // Menambahkan fragment ke backstack agar bisa kembali
                .commit()
        }

    }

    private fun saveResultToDatabase(result: ResultEntity) {
        val database = AppDatabase.getDatabase(requireContext())
        val resultDao = database.resultDao()

        // Run database operation on IO thread
        CoroutineScope(Dispatchers.IO).launch {
            resultDao.insertResult(result)
        }
    }

    companion object {
        fun newInstance(imageUri: Uri, condition: String?, predictionScore: String?): ResultFragment {
            val fragment = ResultFragment()
            fragment.imageUri = imageUri
            fragment.condition = condition
            fragment.predictionScore = predictionScore
            return fragment
        }
    }
}
