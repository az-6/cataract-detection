package com.dicoding.capstone

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

class ResultFragment : Fragment() {

    private lateinit var imageViewResult: ImageView

    // Mendapatkan URI gambar dari MainActivity
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUri = it.getParcelable("image_uri") // Ambil URI gambar
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_result, container, false)

        // Menghubungkan ImageView dari layout
        imageViewResult = rootView.findViewById(R.id.imageViewResult)

        // Menampilkan gambar menggunakan Glide
        imageUri?.let {
            Glide.with(requireContext())
                .load(it) // URI gambar
                .into(imageViewResult)
        }

        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance(imageUri: Uri) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("image_uri", imageUri) // Pastikan 'image_uri' benar
                }
            }
    }

}
