package com.adit.bangkit.plagroid.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
val id: String = "",
val idCategory: String = "",
val productPict: Int = 0,
val category: String = "",
val productName: String = "",
val productStock: Int = 0,
val productPrice: Int = 0,
val rating: Int = 0,
):Parcelable