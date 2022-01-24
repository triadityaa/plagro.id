package com.adit.bangkit.plagroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class Seller (
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val retailName: String = "",
    val email: String = "",
    val image: String = "",
    val productImage: String = "",
    val productName: String = "",
    val productCategory: String = "",
    val mobile: Long = 0,
    val address: String = "",
    val codepos: Int = 0,
    val profileComplete: Int = 0,
    val userType: Int = 2
        ):Parcelable