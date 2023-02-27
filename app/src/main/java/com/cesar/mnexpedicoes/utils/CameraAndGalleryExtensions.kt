package com.cesar.mnexpedicoes.utils

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import java.io.File

fun Fragment.openGallery(REQUEST_CODE_GALLERY: Int) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, REQUEST_CODE_GALLERY)
}

@RequiresApi(Build.VERSION_CODES.P)
fun Fragment.saveProfileImage(imgUri: Uri? = null, imgBitmap: Bitmap? = null, userId: String) {
    val userFolder = File(requireContext().filesDir, "users/${userId}")
    userFolder.mkdirs()
    val file = File(userFolder, "profile.jpg")

    imgUri?.let {
        val source = ImageDecoder.createSource(requireContext().contentResolver, imgUri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    imgBitmap?.let {
        file.outputStream().use {
            imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }
}

fun Fragment.getUserProfilePhoto(userId: String) : Drawable? {
    val userFolder = File(requireContext().filesDir, "users/${userId}")
    val file = File(userFolder, "profile.jpg")
    return Drawable.createFromPath(file.toString())
}