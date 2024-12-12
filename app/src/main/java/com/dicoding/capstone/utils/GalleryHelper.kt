package com.dicoding.capstone.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

class GalleryHelper(private val activity: Activity) {

    fun openGallery(galleryResultLauncher: (Uri) -> Unit) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, 1002)
    }
}
