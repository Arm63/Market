package com.example.market.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.market.R
import com.example.market.db.entity.Fruit
import com.example.market.db.entity.FruitResponse
import com.example.market.ui.adapter.FruitRecyclerAdapter
import com.example.market.util.Constant.API.FRUIT_LIST
import com.google.android.material.navigation.NavigationView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : BaseActivity(), FruitRecyclerAdapter.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {


    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String? = MainActivity::class.java.simpleName

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
    // Override Methods or Methods for/from SuperClass
    // ===========================================================

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchJson()
    }

    override fun getLayoutResource(): Int = R.layout.activity_main

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    override fun onItemClick(item: Fruit, position: Int) {
        val intent = Intent(this,FruitActivity::class.java)
        Log.d("asdasdasdassdsa", item.fruitName)
        intent.putExtra("Username",item)
        startActivity(intent)
    }

    override fun onItemLongClick(item: Fruit, position: Int) {
        TODO("Not yet implemented")
    }



    // ===========================================================
    // Methods
    // ===========================================================

    private fun fetchJson() {
        val request = Request.Builder().url(FRUIT_LIST).build()
        val client = OkHttpClient();
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val items = gson.fromJson(body, FruitResponse::class.java)
                runOnUiThread {
                    initRecycleView(items)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

    private fun initRecycleView(items: FruitResponse?) {
        rv_main.layoutManager = LinearLayoutManager(this@MainActivity)
        if (items != null) {
            rv_main.adapter = FruitRecyclerAdapter(items.fruits, this@MainActivity)
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}

