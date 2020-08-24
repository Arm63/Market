package com.example.market.db.cursor

import android.database.Cursor
import com.example.market.db.FruitDB.FRUIT_DESCRIPTION
import com.example.market.db.FruitDB.FRUIT_FAVORITE
import com.example.market.db.FruitDB.FRUIT_ID
import com.example.market.db.FruitDB.FRUIT_IMAGE
import com.example.market.db.FruitDB.FRUIT_NAME
import com.example.market.db.FruitDB.FRUIT_PRICE
import com.example.market.db.entity.Fruit
import com.example.market.util.AppUtil

class CursorReader {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Constants
    // ===========================================================
    private val LOG_TAG = CursorReader::class.java.simpleName

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
     * FRUIT
     *************************************************************/

    companion object {
        fun parseFruit(cursor: Cursor): Fruit? {
            var fruit: Fruit? = null
            if (!cursor.isClosed && cursor.moveToFirst())
                fruit = composeFruit(cursor)
            cursor.close()
            return fruit
        }

        fun parseFruits(cursor: Cursor): ArrayList<Fruit> {
            val fruits: ArrayList<Fruit> = ArrayList()
            if (!cursor.isClosed && cursor.moveToFirst()) {
                do {
                    val item: Fruit = composeFruit(cursor)
                    fruits.add(item)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return fruits
        }


        private fun composeFruit(cursor: Cursor): Fruit {
            val fruit = Fruit()
            fruit.id = cursor.getLong(cursor.getColumnIndex(FRUIT_ID))
            fruit.name = cursor.getString(cursor.getColumnIndex(FRUIT_NAME))
            fruit.price = cursor.getInt(cursor.getColumnIndex(FRUIT_PRICE))
            fruit.isFavorite =
                AppUtil.intToBoolean(cursor.getInt(cursor.getColumnIndex(FRUIT_FAVORITE)))
            fruit.description = cursor.getString(cursor.getColumnIndex(FRUIT_DESCRIPTION))
            fruit.image = cursor.getString(cursor.getColumnIndex(FRUIT_IMAGE))

            return fruit
        }
    }
}
