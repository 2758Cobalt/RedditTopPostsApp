package com.kolomoiets.reddittoppostsapp.data

import android.os.Parcel
import android.os.Parcelable

/* stores information about the post */

data class RedditPostData(
    val id: String,
    val authorName: String,
    val title: String,
    val createdTime: Double,
    val thumbnailUrl: String,
    val commentsCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readInt(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(authorName)
        parcel.writeString(title)
        parcel.writeDouble(createdTime)
        parcel.writeString(thumbnailUrl)
        parcel.writeInt(commentsCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedditPostData> {
        override fun createFromParcel(parcel: Parcel): RedditPostData {
            return RedditPostData(parcel)
        }

        override fun newArray(size: Int): Array<RedditPostData?> {
            return arrayOfNulls(size)
        }
    }
}
