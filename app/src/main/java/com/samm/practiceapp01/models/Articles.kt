package com.samm.practiceapp01.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

//data class Articles(
//    val source: Source,
//    val author: String,
//    val title: String,
//    val description: String,
//    val url: String,
//    val urlToImage: String,
//    val publishedAt: String,
//    val content: String
//) : Serializable


data class Articles(
    val source: Source,
    val author: String,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)
//): Parcelable {
//
//    // Implement the writeToParcel method
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        // Write the properties to the parcel
//        parcel.writeString(title)
//        parcel.writeString(description)
//        parcel.writeString(url)
//        parcel.writeString(urlToImage)
//        parcel.writeString(publishedAt)
//        parcel.writeString(source.id)
//        parcel.writeString(source.name)
//        parcel.writeString(content)
//    }
//
//    // Implement the describeContents method
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    // Implement the CREATOR property
//    companion object CREATOR : Parcelable.Creator<Articles> {
//        override fun createFromParcel(parcel: Parcel): Articles {
//            // Read the properties from the parcel
//            val title = parcel.readString()
//            val description = parcel.readString()
//            val url = parcel.readString()
//            val urlToImage = parcel.readString()
//            val publishedAt = parcel.readString()
//            val author = parcel.readString()
//            val source  = Source(
//                id = parcel.readString(),
//                name = parcel.readString()
//            )
//            val content = parcel.readString()
//
//            // Return a new NewsArticle instance
//            return Articles(
//                source = source!!,
//                author = author!!,
//                title = title!!,
//                description = description!!,
//                url = url!!,
//                urlToImage = urlToImage!!,
//                publishedAt = publishedAt!!,
//                content = content!!,
//            )
//        }
//
//        override fun newArray(size: Int): Array<Articles?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
