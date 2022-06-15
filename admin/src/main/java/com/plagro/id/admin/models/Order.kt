package com.plagro.id.admin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Order item with required fields.
 */
@Parcelize
data class Order(
    val user_id: String = "",
    val items: ArrayList<Cart> = ArrayList(),
    val address: Address = Address(),
    val title: String = "",
    val image: String = "",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    var total_amount: String = "",
    val order_datetime: Long = 0L,
    var id: String = "",
    val admin_id: String = "b2KuXe8oj8QzsuKAOBBsJGFXH643",
) : Parcelable