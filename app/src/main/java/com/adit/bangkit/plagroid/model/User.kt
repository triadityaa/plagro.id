package com.adit.bangkit.plagroid.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val address: String = "",
    val gender: String = "",
    val profileComplete: Int = 0,
        ):Parcelable