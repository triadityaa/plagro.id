package com.plagro.id.admin.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * A data model class for Admin with required fields.
 */
@Parcelize
data class Admin(
    val id: String = "",
    val retailName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val profileCompleted: Int = 1
) : Parcelable