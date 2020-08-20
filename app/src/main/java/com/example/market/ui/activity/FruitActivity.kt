package com.example.market.ui.activity

import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.market.R
import com.example.market.db.entity.Fruit
import kotlinx.android.synthetic.main.activity_fruit.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FruitActivity : BaseActivity() {

    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Constants
    // ===========================================================
    private val LOG_TAG: String = FruitActivity::class.java.simpleName

    // ===========================================================
    // Fields
    // ===========================================================

    private lateinit var mFruit: Fruit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()
        setData()


    }

    private fun setData() {
        Glide.with(this)
            .load(mFruit.image)
            .into(iv_fruit_image)

        tv_fruit_title.text = mFruit.fruitName
        tv_fruit_price.text = mFruit.price.toString()
        tv_fruit_desc.text = mFruit.description
    }

    private fun getData() {
        mFruit = intent.getParcelableExtra("Username")
    }

    override fun getLayoutResource(): Int =
        R.layout.activity_fruit

}