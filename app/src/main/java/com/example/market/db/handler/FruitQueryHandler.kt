package com.example.market.db.handler

import android.content.Context
import com.example.market.db.FruitDB
import com.example.market.db.FruitDB.FRUIT_ID
import com.example.market.db.FruitDB.FRUIT_TABLE
import com.example.market.db.entity.Fruit
import com.example.market.db.provider.UriBuilder

class FruitQueryHandler {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Constants
    // ===========================================================
    private val LOG_TAG: String = FruitQueryHandler::class.java.simpleName

    // ===========================================================
    // Fields
    // ===========================================================

    // ===========================================================
    // Constructors
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
     * FRUIT METHODS
     *************************************************************/
    companion object {
        @Synchronized
        fun addFruit(context: Context, fruit: Fruit) {
            context.contentResolver.insert(
                UriBuilder.buildFruitUri(),
                FruitDB.composeValues(fruit, FruitDB.ContentValuesType.FRUITS)
            )
        }

        @Synchronized
        fun addFruits(context: Context, fruits: ArrayList<Fruit>) {
            context.contentResolver.bulkInsert(
                UriBuilder.buildFruitUri(),
                FruitDB.composeValuesArray(fruits, FruitDB.ContentValuesType.FRUITS)
            )
        }

        @Synchronized
        fun updateFruit(context: Context, fruit: Fruit) {
            context.contentResolver.update(
                UriBuilder.buildFruitUri(),
                FruitDB.composeValues(fruit, FRUIT_TABLE),
                "$FRUIT_ID?",
                arrayOf(java.lang.String.valueOf(fruit.id))
            )
            arrayOf((fruit.id).toString())
        }

        @Synchronized
        fun updateFruitDescription(context: Context, fruit: Fruit) {
            context.contentResolver.update(
                UriBuilder.buildFruitUri(),
                FruitDB.composeValues(fruit, FruitDB.ContentValuesType.DESCRIPTION),
                FruitDB.FRUIT_ID.toString() + "=?",
                arrayOf(java.lang.String.valueOf(fruit.id))
            )
        }
    }
}

