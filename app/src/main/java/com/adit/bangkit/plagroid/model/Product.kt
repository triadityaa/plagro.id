package com.adit.bangkit.plagroid.model

import android.media.Rating
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val category: String = "",
    val productName: String = "",
    val productStock: Int = 0,
    val productPrice: Int = 0,
    val productPict: String = "",
    var rating: Int = 0,
    var avgRating: Double = 0.0,
):Parcelable {
    fun setNumRatings(newNumRatings: Int): Int {
        var newRating = (rating * productStock + newNumRatings) / (productStock + 1)
        if (newRating > 5) newRating = 5
        rating = newRating
        return rating
    }


    fun setAvgRating(newAvgRating: Double): Double {
        avgRating = newAvgRating
        return avgRating
    }

    @JvmName("getAvgRating1")
    fun getAvgRating(): Double {
        return avgRating
    }

    fun getNumRatings(): Int {
        return rating
    }
}