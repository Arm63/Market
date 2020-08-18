package com.example.market.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.market.R
import com.example.market.db.entity.Fruit
import com.example.market.db.entity.FruitResponse
import com.example.market.io.HttpRequestManager
import com.example.market.io.HttpResponseUtil
import com.example.market.ui.adapter.BlogRecyclerAdapter
import com.example.market.util.Constant
import com.example.market.util.NetworkUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : BaseActivity() {


    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String? = BaseActivity::class.java.simpleName

    // ===========================================================
    // Fields
    // ===========================================================

    private lateinit var blogAdapter: BlogRecyclerAdapter


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
        initRecycleView()
        loadData()
    }

    override fun getLayoutResource(): Int =
        R.layout.activity_main

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    private fun loadData() {
        var response =
            URL("https://raw.githubusercontent.com/Arm63/armen63.io/master/fruit_list/fruits.json").readText()
        var gson = Gson()
        val data = gson.fromJson(response, Array<Fruit>::class.java)

        for(x in 0 until data.size)
            println(data[x].fruitName+ "      ")



    }

    fun initRecycleView() {
        rv_main.apply {
            rv_main.layoutManager = LinearLayoutManager(context)
            blogAdapter = BlogRecyclerAdapter()
            rv_main.adapter = blogAdapter
        }
    }
}