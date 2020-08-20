package com.example.market.db.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FruitResponse(
	@field:SerializedName("fruits")
	val fruits: ArrayList<Fruit>
) : Parcelable

