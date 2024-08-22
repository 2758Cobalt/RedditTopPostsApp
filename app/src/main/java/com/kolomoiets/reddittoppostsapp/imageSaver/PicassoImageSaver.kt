package com.kolomoiets.reddittoppostsapp.imageSaver

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class PicassoImageSaver(private val context: Context) {

    fun saveImageToGallery(imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    saveBitmapToGallery(bitmap)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: android.graphics.drawable.Drawable?) { }
                override fun onPrepareLoad(placeHolderDrawable: android.graphics.drawable.Drawable?) { }
            })
    }

    private fun saveBitmapToGallery(bitmap: Bitmap) {
        val filename = "picture_${System.currentTimeMillis()}.jpg"

        val fos: OutputStream? =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            resolver.openOutputStream(imageUri!!)
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }
}