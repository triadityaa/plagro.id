package com.adit.bangkit.plagroid.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Address item with required fields.
 */
@Parcelize
data class Address(
    val user_id: String = "",
    val name: String = "",
    val mobileNumber: String = "",

    val address: String = "",
    val zipCode: String = "",
    val additionalNote: String = "",

    val type: String = "",
    val otherDetails: String = "",
    var id: String = "",

    var key: String = "",
    var latlng: Double = 0.0,
) : Parcelable