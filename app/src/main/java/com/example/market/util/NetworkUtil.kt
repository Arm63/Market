package com.example.market.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.example.market.db.entity.Fruit
import com.example.market.db.entity.FruitResponse
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

@Suppress("DEPRECATION")
class NetworkUtil private constructor() {

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    companion object {
        private val LOG_TAG = NetworkUtil::class.java.simpleName
        private var sInstance: NetworkUtil? = null
        val instance: NetworkUtil?
            get() {
                if (sInstance == null) {
                    sInstance = NetworkUtil()
                }
                return sInstance
            }
    }
}
fun fetchJson() : List<Fruit>? {
    val request = Request.Builder().url(Constant.API.FRUIT_LIST).build()
    val client = OkHttpClient();
    var items: FruitResponse? = null
    client.newCall(request).enqueue(object : Callback {
        override fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            val gson = GsonBuilder().create()
            items = gson.fromJson(body, FruitResponse::class.java)
        }

        override fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request")
        }
    })
    return items?.fruits
}