package com.example.market.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.market.R
import com.example.market.db.entity.DataSource
import com.example.market.ui.adapter.BlogRecyclerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var blogAdapter: BlogRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecycleView()
        addDataSet()
    }

    private fun addDataSet(){
        val data = DataSource.createDataSet()
        blogAdapter.submitList(data)
    }

    private fun initRecycleView() {
        rv_main.apply {
            rv_main.layoutManager = LinearLayoutManager(context)
            blogAdapter = BlogRecyclerAdapter()
            rv_main.adapter = blogAdapter
        }

    }
}