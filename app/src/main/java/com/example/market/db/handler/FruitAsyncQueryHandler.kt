package com.example.market.db.handler

import android.content.AsyncQueryHandler
import android.content.Context
import android.database.Cursor
import android.net.Uri
import com.example.market.db.FruitDB
import com.example.market.db.entity.Fruit
import com.example.market.db.provider.UriBuilder
import com.example.market.util.AppUtil
import java.lang.ref.WeakReference

class FruitAsyncQueryHandler(context: Context, queryListenerReference: AsyncQueryListener) :
    AsyncQueryHandler(context.contentResolver) {
    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String = FruitAsyncQueryHandler::class.java.simpleName

    object QueryToken {
        const val GET_FRUIT = 100
        const val GET_FRUITS = 101
        const val ADD_FRUIT = 102
        const val UPDATE_FRUIT = 104
        const val DELETE_FRUIT = 105
        const val DELETE_FRUITS = 106
        const val GET_FAVORITE_FRUITS = 107
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    interface AsyncQueryListener {
        fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?)
        fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?)
        fun onUpdateComplete(token: Int, cookie: Any?, result: Int)
        fun onDeleteComplete(token: Int, cookie: Any?, result: Int)
    }

    // ===========================================================
    // Fields
    // ===========================================================

    private var mQueryListenerReference
            = WeakReference(queryListenerReference)

    // ===========================================================
    // Constructors
    // ===========================================================

    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor) {
        val queryListener = mQueryListenerReference.get()
        if (queryListener != null) {
            queryListener.onQueryComplete(token, cookie, cursor)
        } else cursor.close()
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
        mQueryListenerReference.get()?.onInsertComplete(token, cookie, uri)
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
        mQueryListenerReference.get()?.onUpdateComplete(token, cookie, result)

    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
        mQueryListenerReference.get()?.onDeleteComplete(token, cookie, result)
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods systems
    // ===========================================================

    // ===========================================================
    // Methods controls
    // ===========================================================

    /**
     * FRUIT Methods
     *************************************************************/

    @Synchronized
    fun getFruit(id: Long) {
        startQuery(
            QueryToken.GET_FRUIT,
            null,
            UriBuilder.buildFruitUri(),
            FruitDB.Projection.FRUIT,
            FruitDB.FRUIT_ID + "=?",
            arrayOf(id.toString()),
            null
        )
    }

    @Synchronized
    fun getFruits() {
        startQuery(
            QueryToken.GET_FRUITS,
            null,
            UriBuilder.buildFruitUri(),
            FruitDB.Projection.FRUIT,
            null,
            null,
            null
        )
    }

    @Synchronized
    fun addFruit(fruit: Fruit) {
        startInsert(
            QueryToken.ADD_FRUIT,
            null,
            UriBuilder.buildFruitUri(),
            FruitDB.composeValues(fruit, FruitDB.ContentValuesType.FRUITS)
        )
    }

    //134Tox@ poxaca id qcaca
    @Synchronized
    fun updateFruit(fruit: Fruit) {
        startUpdate(
            QueryToken.UPDATE_FRUIT,
            null,
            fruit.id?.let { UriBuilder.buildFruitUri(it) },
            FruitDB.composeValues(fruit, FruitDB.ContentValuesType.FRUITS),
            "${FruitDB.FRUIT_ID}=?",
            arrayOf((fruit.id).toString())
        )
    }

    @Synchronized
    fun deleteFruit(fruit: Fruit, cookie: Any) {
        startDelete(
            QueryToken.DELETE_FRUIT,
            null,
            UriBuilder.buildFruitUri(),
            FruitDB.FRUIT_ID + "=?",
            arrayOf((fruit.id).toString())
        )
    }

    @Synchronized
    fun deleteFruit(fruit: Fruit) {
        startDelete(
            QueryToken.DELETE_FRUIT,
            null,
            UriBuilder.buildFruitUri(fruit.id!!),
            null,
            null
        )
    }


    @Synchronized
    fun deleteFruits() {
        startDelete(
            QueryToken.DELETE_FRUITS,
            null,
            UriBuilder.buildFruitUri(),
            null,
            null
        )
    }

    @Synchronized
    fun getAllFavoriteFruits() {
        startQuery(
            QueryToken.GET_FAVORITE_FRUITS,
            null,
            UriBuilder.buildFruitUri(),
            FruitDB.Projection.FRUIT,
            FruitDB.FRUIT_FAVORITE + "=?",
            arrayOf((AppUtil.booleanToInt(true)).toString()),
            null
        )
    }

}
