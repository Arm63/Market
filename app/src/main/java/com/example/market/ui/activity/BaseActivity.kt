package com.example.market.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
    }

    abstract fun getLayoutResource(): Int
}