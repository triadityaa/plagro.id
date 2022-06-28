package com.plagro.id.admin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Sold Product with required fields.
 */
@Parcelize
data class SoldProduct(
    val user_id: String = "",
    val title: String = "",
    val price: Int = 0,
    val sold_quantity: Int = 0,
    val image: String = "",
    val order_id: String = "",
    val order_date: Long = 0L,
    val sub_total_amount: Int = 0,
    val shipping_charge: Int = 0,
    var total_amount: Int = 0,
    val address: Address = Address(),
    var id: String = "",
    val admin_id: String = "b2KuXe8oj8QzsuKAOBBsJGFXH643",
) : Parcelable