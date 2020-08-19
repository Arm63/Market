package com.example.market.db.entity

import com.google.gson.annotations.SerializedName

data class FruitResponse(
	@field:SerializedName("fruits")
	val fruits: ArrayList<Fruit>
)

