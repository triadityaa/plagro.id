package com.adit.bangkit.plagroid.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Product with required fields.
 */
@Parcelize
data class Product(
    val user_id: String = "",
    val user_name: String = "",
    val title: String = "",
    val price: Int = 0,
    val description: String = "",
    val stock_quantity: String = "",
    val image: String = "",
    var product_id: String = "",
) : Parcelable