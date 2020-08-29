package com.example.market.db.handler

import android.content.Context
import com.example.market.db.FruitDB
import com.example.market.db.FruitDB.composeValues
import com.example.market.db.FruitDB.composeValuesArray
import com.example.market.db.cursor.CursorReader
import com.example.market.db.entity.Fruit
import com.example.market.db.provider.UriBuilder
import com.example.market.util.AppUtil


object FruitQueryHandler {
    // ===========================================================
    // Constants
    // ===========================================================
    private val LOG_TAG = FruitQueryHandler::class.java.simpleName
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
     * fruit METHODS
     */
    @Synchronized
    fun addFruit(context: Context, fruit: Fruit?) {
        context.contentResolver.insert(
            UriBuilder.buildFruitUri(),
            composeValues(fruit!!, FruitDB.ContentValuesType.FRUITS)
        )
    }

    @Synchronized
    fun addFruits(context: Context, fruits: ArrayList<Fruit>?) {
        context.contentResolver.bulkInsert(
            UriBuilder.buildFruitUri(),
            composeValuesArray(fruits!!, FruitDB.ContentValuesType.FRUITS)
        )
    }

    @Synchronized
    fun updateFruit(context: Context, fruit: Fruit) {
        context.contentResolver.update(
            UriBuilder.buildFruitUri(),
            composeValues(fruit, FruitDB.FRUIT_TABLE),
            FruitDB.FRUIT_ID + "=?", arrayOf(fruit.id.toString())
        )
    }

    @Synchronized
    fun updateFruitDescription(context: Context, fruit: Fruit) {
        context.contentResolver.update(
            UriBuilder.buildFruitUri(),
            composeValues(fruit, FruitDB.ContentValuesType.DESCRIPTION),
            FruitDB.FRUIT_ID + "=?",
            arrayOf(fruit.id.toString())
        )
    }

    @Synchronized
    fun getFruit(context: Context, id: Long): Fruit? {
        val cursor = context.contentResolver.query(
            UriBuilder.buildFruitUri(id),
            FruitDB.Projection.FRUIT,
            null,
            null,
            null
        )
        return cursor?.let { CursorReader.parseFruit(it) }
    }

    @Synchronized
    fun getFruits(context: Context): ArrayList<Fruit>? {
        val cursor = context.contentResolver.query(
            UriBuilder.buildFruitUri(),
            FruitDB.Projection.FRUIT,
            null,
            null,
            null
        )
        return cursor?.let { CursorReader.parseFruits(it) }
    }

    @Synchronized
    fun deleteFruit(context: Context, fruit: Fruit) {
        context.contentResolver.delete(
            UriBuilder.buildFruitUri(),
            FruitDB.FRUIT_ID + "=?", arrayOf(fruit.id.toString())
        )
    }

    @Synchronized
    fun deleteFruits(context: Context) {
        context.contentResolver.delete(
            UriBuilder.buildFruitUri(),
            null,
            null
        )
    }

    @Synchronized
    fun getAllFavoriteFruits(context: Context): ArrayList<Fruit>? {
        val cursor = context.contentResolver.query(
            UriBuilder.buildFruitUri(),
            FruitDB.Projection.FRUIT,
            FruitDB.FRUIT_FAVORITE + "=?", arrayOf(AppUtil.booleanToInt(true).toString()),
            null
        )
        return cursor?.let { CursorReader.parseFruits(it) }
    }

//    @Synchronized
//    fun updateProductsExceptFavorite(context: Context, fruits: ArrayList<Fruit>) {
//        for (fruit in fruits) {
//            fruit.id?.let { UriBuilder.buildFruitUri(it) }?.let {
//                context.contentResolver.update(
//                    it,
//                    FruitDB.composeValues(fruit, FruitDB.ContentValuesType.ALL_EXCEPT_FAVORITE),
//                    null,
//                    null
//                )
//            }
//        }
//    }
//

//    @Synchronized
//    fun getAllFromUserFruits(context: Context): ArrayList<Fruit> {
//        val cursor = context.contentResolver.query(
//            UriBuilder.buildFruitUri(),
//            FruitDB.Projection.FRUIT,
//            FruitDB.FRUIT_USER + "=?", arrayOf(AppUtil.booleanToInt(true).toString()),
//            null
//        )
//        return CursorReader.parseFruits(cursor)
//    } // ===========================================================
    // Inner and Anonymous Clases
    // ===========================================================
}