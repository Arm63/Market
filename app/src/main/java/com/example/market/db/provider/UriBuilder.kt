package com.example.market.db.provider

import android.net.Uri
import com.example.market.BuildConfig
import com.example.market.db.FruitDB.FRUIT_TABLE

class UriBuilder {
    companion object {
        fun buildFruitUri(id: Int): Uri =
            Uri.parse("content://" + BuildConfig.APPLICATION_ID + "/" + FRUIT_TABLE + "/" + id)

        fun buildFruitUri(): Uri =
            Uri.parse("content://" + BuildConfig.APPLICATION_ID + "/" + FRUIT_TABLE)
    }
}