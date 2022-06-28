package com.adit.bangkit.plagroid.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Cart item with required fields.
 */
@Parcelize
data class Cart(
    val user_id: String = "",
    val product_id: String = "",
    val title: String = "",
    val price: Int = 0,
    val image: String = "",
    var cart_quantity: Int = 0,
    var stock_quantity: Int = 0,
    var id: String = "",
) : Parcelable