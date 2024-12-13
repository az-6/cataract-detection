package com.dicoding.capstone.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class CameraHelper(private val activity: Activity) {

    fun checkPermissionAndOpenCamera(cameraResultLauncher: (Intent) -> Unit) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 1001)
        } else {
            openCamera(cameraResultLauncher)
        }
    }

    private fun openCamera(cameraResultLauncher: (Intent) -> Unit) {
        val photoFile = File.createTempFile("photo_", ".jpg", activity.externalCacheDir)
        val photoUri: Uri = FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider",
            photoFile
        )

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        }

        cameraResultLauncher(cameraIntent)
    }
}
