package com.example.market.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class FruitDBHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_NAME = "FRUIT.DB"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(FruitDB.CREATE_FRUIT_TABLE)
        Log.d("FruitDBHelper","sarqvela")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + FruitDB.FRUIT_TABLE)
        onCreate(db)
    }
}