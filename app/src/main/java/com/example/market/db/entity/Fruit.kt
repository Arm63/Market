package com.example.market.db.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit(

    @field:SerializedName("fruit_id")
    val fruitId: Int? = null,

    @field:SerializedName("fruit_name")
    val fruitName: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("image")
    val image: String? = null
) : Parcelable