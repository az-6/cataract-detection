package com.dicoding.capstone

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.graphics.BitmapFactory
import java.io.InputStream

class HomeFragment : Fragment() {

    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        imageView = view.findViewById(R.id.imageView)  // Initialize ImageView
        return view
    }

    // Function to update the ImageView with the image from URI
    fun updateImageFromUri(uri: Uri) {
        val imageStream: InputStream? = context?.contentResolver?.openInputStream(uri)
        val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
        imageView.setImageBitmap(selectedImage)
    }
}
