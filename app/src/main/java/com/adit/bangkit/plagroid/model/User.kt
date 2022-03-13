package com.adit.bangkit.plagroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val retailName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val address: String = "",
    val gender: String = "",
    val codepos: Int = 0,
    val profileComplete: Int = 0,
    val sellerProfileComplete: Int = 0,
    var userType: Int = 1
):Parcelable