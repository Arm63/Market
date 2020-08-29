package com.example.market.io.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.market.db.entity.Fruit
import com.example.market.db.entity.FruitResponse
import com.example.market.db.handler.FruitQueryHandler
import com.example.market.io.bus.BusProvider
import com.example.market.io.bus.event.ApiEvent
import com.example.market.io.server.HttpRequestManager
import com.example.market.io.server.HttpResponseUtil
import com.example.market.util.Constant
import com.example.market.util.Preference
import com.google.gson.Gson
import java.net.HttpURLConnection

class FruitIntentService : IntentService(FruitIntentService::class.java.simpleName) {
    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String = FruitIntentService::class.java.simpleName


    companion object {
        /**
         * @param url         - calling api url
         * @param requestType - string constant that helps us to distinguish what request it is
         * @param postEntity  - POST request entity (json string that must be sent on server)
         */
        fun start(context: Context, url: String, postEntity: String, requestType: Int) {
            val intent = Intent(context, FruitIntentService::class.java)
            intent.putExtra(Constant.Extra.URL, url)
            intent.putExtra(Constant.Extra.REQUEST_TYPE, requestType)
            intent.putExtra(Constant.Extra.POST_ENTITY, postEntity)
            context.startService(intent)
        }

        fun start(context: Context, url: String, requestType: Int) {
            val intent = Intent(context, FruitIntentService::class.java)
            intent.putExtra(Constant.Extra.URL, url)
            intent.putExtra(Constant.Extra.REQUEST_TYPE, requestType)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        val url = intent!!.extras!!.getString(Constant.Extra.URL)
        val data = intent.extras!!.getString(Constant.Extra.POST_ENTITY)
        val requestType = intent.extras!!.getInt(Constant.Extra.REQUEST_TYPE)

        val connection: HttpURLConnection
        when (requestType) {
            Constant.RequestType.FRUIT_LIST -> {
                connection = HttpRequestManager.executeRequest(
                    url,
                    Constant.RequestMethod.GET,
                    data
                )!!
                val jsonList = HttpResponseUtil.parseResponse(connection)
                val fruitResponse = Gson().fromJson<FruitResponse>(
                    jsonList,
                    FruitResponse::class.java
                )
                if (fruitResponse != null) {
                    val fruits = fruitResponse.fruits
                    for (fruit in fruits!!) {
                        Log.d("servici fori mejic  description =   ", fruit?.description)
                        if (fruit != null) {
                            if (Preference.getInstance(applicationContext)
                                    ?.getUserFavorites(fruit.id.toString())!!
                            ) {
                                fruit.isFavorite = true
                            }
                        }
                    }
//                    PlQueryHandler.updateProductsExceptFavorite(this, products)
                    FruitQueryHandler.addFruits(this, fruits as ArrayList<Fruit>?)

                    // post to UI
                    BusProvider.instance.post(
                        ApiEvent(
                            ApiEvent.EventType.FRUIT_LIST_LOADED,
                            true,
                            fruits
                        )
                    )
                    Log.d(LOG_TAG, "size of = " + fruits.size)

                } else
                    BusProvider.instance.post(ApiEvent(ApiEvent.EventType.FRUIT_LIST_LOADED, false))


            }
            Constant.RequestType.FRUIT_ITEM -> {
                connection = HttpRequestManager.executeRequest(
                    url,
                    Constant.RequestMethod.GET,
                    null
                )!!
                val jsonItem = HttpResponseUtil.parseResponse(connection)

                val fruit = Gson().fromJson<Fruit>(jsonItem, Fruit::class.java)
                if (fruit != null) {
                    FruitQueryHandler.updateFruitDescription(this, fruit)
                    BusProvider.instance
                        .post(ApiEvent(ApiEvent.EventType.FRUIT_ITEM_LOADED, true, fruit))

                } else {
                    BusProvider.instance
                        .post(ApiEvent(ApiEvent.EventType.FRUIT_LIST_LOADED, false))
                }
            }
        }
        Log.d(LOG_TAG, "serive ashxateluc heto")
    }
}
