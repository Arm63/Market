@file:Suppress("UNCHECKED_CAST")

package com.example.market.db

import android.content.ContentValues
import com.example.market.db.FruitDB.ContentValuesType.DESCRIPTION
import com.example.market.db.FruitDB.ContentValuesType.FRUITS
import com.example.market.db.entity.Fruit

object FruitDB {

    object ContentValuesType {
        const val FRUITS = "FRUITS"
        const val DESCRIPTION = "DESCRIPTION"
    }


    /**
     * TABLES
     */

    const val FRUIT_TABLE = "FRUIT_TABLE"

    const val FRUIT_PK = "_id"
    const val FRUIT_ID = "FRUIT_ID"
    const val FRUIT_NAME = "FRUIT_NAME"
    const val FRUIT_PRICE = "FRUIT_PRICE"
    const val FRUIT_IMAGE = "FRUIT_IMAGE"
    const val FRUIT_FAVORITE = "FRUIT_FAVORITE"
    const val FRUIT_DESCRIPTION = "FRUIT_DESCRIPTION"


    const val CREATE_FRUIT_TABLE = ("CREATE TABLE IF NOT EXISTS " + FRUIT_TABLE
            + " ("
            + FRUIT_PK + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FRUIT_ID + " INTEGER UNIQUE, "
            + FRUIT_NAME + " TEXT, "
            + FRUIT_PRICE + " INTEGER, "
            + FRUIT_FAVORITE + " INTEGER, "
            + FRUIT_DESCRIPTION + " TEXT, "
            + FRUIT_IMAGE + " TEXT "
            + ");")


    /**
     * PROJECTIONS
     */
    object Projection {
        var FRUIT = arrayOf(
            FRUIT_PK,
            FRUIT_ID,
            FRUIT_NAME,
            FRUIT_PRICE,
            FRUIT_FAVORITE,
            FRUIT_DESCRIPTION,
            FRUIT_IMAGE
        )
    }

// ===========================================================
//  ructors
// ===========================================================
// ===========================================================
// Getter & Setter
// ===========================================================
// ===========================================================
// Methods for/from SuperClass
// ===========================================================
// ===========================================================
// Listeners, methods for/from Interfaces
// ===========================================================
// ===========================================================
// Methods
// ===========================================================

    /**
     * VALUES
     */

    fun composeValues(item: Any, table: String?): ContentValues {
        val values = ContentValues()
        val fruit: Fruit = item as Fruit
        when (table) {
            FRUITS -> {
                values.put(FRUIT_ID, fruit.id)
                values.put(FRUIT_NAME, fruit.name)
                values.put(FRUIT_PRICE, fruit.price)
                values.put(FRUIT_FAVORITE, fruit.isFavorite)
                values.put(FRUIT_DESCRIPTION, fruit.description)
                values.put(FRUIT_IMAGE, fruit.image)
            }
            DESCRIPTION -> values.put(
                FRUIT_DESCRIPTION,
                fruit.description
            )
        }
        return values
    }

    fun composeValuesArray(objects: ArrayList<*>, table: String?): Array<ContentValues> {
        val valuesList = ArrayList<ContentValues>()
        val fruits: ArrayList<Fruit> = objects as ArrayList<Fruit>

        when (table) {
            FRUITS -> for (fruit in fruits) {
                val values = ContentValues()
                values.put(FRUIT_ID, fruit.id)
                values.put(FRUIT_NAME, fruit.name)
                values.put(FRUIT_PRICE, fruit.price)
                values.put(FRUIT_FAVORITE, fruit.isFavorite)
                values.put(FRUIT_DESCRIPTION, fruit.description)
                values.put(FRUIT_IMAGE, fruit.image)

                valuesList.add(values)
            }
        }
        return valuesList.toTypedArray()
    }

}