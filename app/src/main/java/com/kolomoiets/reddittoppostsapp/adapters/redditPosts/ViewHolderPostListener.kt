package com.kolomoiets.reddittoppostsapp.adapters.redditPosts

import android.graphics.Bitmap

interface ViewHolderPostListener {
    fun onThumbnailClick(thumbnailUrl: String)
    fun actionSaveToGallery(thumbnailUrl: String)
}