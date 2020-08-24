package com.example.market.db.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit(

    @field:SerializedName("id")
    var id: Long? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("price")
    var price: Int? = null,

    var isFavorite: Boolean = false,

    @field:SerializedName("description")
    var description: String? = null,

    @field:SerializedName("image")
    var image: String? = null

) : Parcelable

