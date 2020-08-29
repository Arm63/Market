package com.example.market.db.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils
import com.example.market.BuildConfig
import com.example.market.db.FruitDB
import com.example.market.db.FruitDB.FRUIT_TABLE
import com.example.market.db.FruitDBHelper

class FruitProvider : ContentProvider() {

    private object Code {
        const val ALL_FRUITS = 1
        const val SINGLE_FRUIT = 2
    }

    private class ContentType {
        companion object {
            const val ALL_FRUITS = ("vnd.android.cursor.dir/vnd."
                    + BuildConfig.APPLICATION_ID + "." + FRUIT_TABLE)

            const val SINGLE_FRUIT = ("vnd.android.cursor.item/vnd."
                    + BuildConfig.APPLICATION_ID + "." + FRUIT_TABLE)
        }

    }

    private val sUriMatcher: UriMatcher? = buildUriMatcher()
    private lateinit var mDBHelper: FruitDBHelper


// ===========================================================
// Listeners, methods for/from Interfaces
// ===========================================================
// ===========================================================
// Methods
// ===========================================================


    override fun onCreate(): Boolean {
        mDBHelper = FruitDBHelper(context)
        return true
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher?.match(uri)) {
            Code.ALL_FRUITS -> {
                ContentType.ALL_FRUITS
            }
            Code.SINGLE_FRUIT -> {
                ContentType.SINGLE_FRUIT
            }
            else -> throw IllegalArgumentException("Unsupported URI$uri")
        }
    }


    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor: Cursor
        val db: SQLiteDatabase = mDBHelper.writableDatabase
        val queryBuilder = SQLiteQueryBuilder()

        when (sUriMatcher?.match(uri)) {
            Code.SINGLE_FRUIT -> {
                queryBuilder.tables = FRUIT_TABLE
                cursor = queryBuilder.query(
                    db,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            Code.ALL_FRUITS -> {
                queryBuilder.tables = FRUIT_TABLE
                cursor = queryBuilder.query(
                    db,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                )
            }
            else ->
                throw IllegalArgumentException("Unsupported URI: $uri")
        }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val contentUri: Uri
        val db = mDBHelper.writableDatabase
        val id: Long

        when (sUriMatcher?.match(uri)) {
            Code.SINGLE_FRUIT -> {
                id = db.insertWithOnConflict(
                    FRUIT_TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE
                ).toLong()
                contentUri = ContentUris.withAppendedId(UriBuilder.buildFruitUri(), id)
            }
            Code.ALL_FRUITS -> {
                id = db.insertWithOnConflict(
                    FRUIT_TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE
                ).toLong()
                contentUri = ContentUris.withAppendedId(UriBuilder.buildFruitUri(), id.toLong())
            }
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
        return contentUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val deleteCount: Int
        val db = mDBHelper.writableDatabase

        deleteCount = when (sUriMatcher?.match(uri)) {
            Code.ALL_FRUITS -> db.delete(
                FRUIT_TABLE,
                selection,
                selectionArgs
            )
            Code.SINGLE_FRUIT -> db.delete(
                FRUIT_TABLE,
                selection,
                selectionArgs
            )
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
        return deleteCount
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        var updateCount: Int
        var sss = selection
        val db = mDBHelper.writableDatabase

        updateCount = when (sUriMatcher?.match(uri)) {
            Code.ALL_FRUITS -> db.update(FRUIT_TABLE, values, selection, selectionArgs)
            Code.SINGLE_FRUIT -> {
                var id = uri.lastPathSegment
                if (TextUtils.isEmpty(selection)) {
                    sss = FruitDB.FRUIT_ID + "=" + id
                } else {
                    sss = sss + " AND " + FruitDB.FRUIT_ID + "=" + id
                }
                return  db.update(FRUIT_TABLE, values, sss, selectionArgs)
//                db.update(FRUIT_TABLE, values, selection, selectionArgs)
            }
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
        return updateCount
    }

    companion object {
        private fun buildUriMatcher(): UriMatcher? {
            val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher.addURI(
                BuildConfig.APPLICATION_ID,
                FRUIT_TABLE,
                Code.ALL_FRUITS
            )
            uriMatcher.addURI(
                BuildConfig.APPLICATION_ID,
                "$FRUIT_TABLE/#",
                Code.SINGLE_FRUIT
            )
            return uriMatcher
        }
    }
}