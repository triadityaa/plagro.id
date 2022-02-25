package com.adit.bangkit.plagroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Seller(
    val id: String = "",
    val retailName: String = "",
    val email: String = "",
    val address: String = "",
    val mobile: Long = 0,
    val codepos: Int = 0,
    val image: String = "",
    val profileComplete: Int = 0,
    val userType: Int = 2
):Parcelable