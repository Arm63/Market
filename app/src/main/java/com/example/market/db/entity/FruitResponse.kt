package com.example.market.db.entity

import com.google.gson.annotations.SerializedName

class FruitResponse{

    @SerializedName("fruits")
    lateinit var fruitList: ArrayList<Fruit>

}