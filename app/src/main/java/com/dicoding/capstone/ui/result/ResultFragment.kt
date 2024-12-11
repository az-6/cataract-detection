package com.dicoding.capstone.ui.result

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dicoding.capstone.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var imageUri: Uri? = null
    private var condition: String? = null
    private var predictionScore: String? = null  // Ubah tipe menjadi String?

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

        // Tampilkan gambar yang diupload
        imageUri?.let {
            binding.imageView.setImageURI(it)
        }

        // Tampilkan kondisi
        condition?.let {
            binding.conditionTextView.text = "Condition: $it"
        }

        // Tampilkan skor prediksi sebagai string
        predictionScore?.let {
            binding.predictionScoreTextView.text = "Prediction Score: $it"
        }
    }

    companion object {
        fun newInstance(imageUri: Uri, condition: String?, predictionScore: String?): ResultFragment {
            val fragment = ResultFragment()
            fragment.imageUri = imageUri
            fragment.condition = condition
            fragment.predictionScore = predictionScore  // Kirim predictionScore sebagai String
            return fragment
        }
    }
}
