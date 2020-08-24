package com.example.market.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
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
    private lateinit var mMenuEdit: MenuItem
    private lateinit var mMenuDone: MenuItem
    private lateinit var mMenuUnfav: MenuItem
    private lateinit var mMenuFav: MenuItem
    private lateinit var mLlfruitView: LinearLayout
    private lateinit var mLlfruitEdit: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findView()
        loadData()
    }

    private fun findView() {
        mLlfruitView = findViewById(R.id.ll_fruit_view)
        mLlfruitEdit = findViewById(R.id.ll_fruit_edit)

    }

    private fun loadData() {
        mFruit = intent.getParcelableExtra("fstyush")

        Glide.with(this)
            .load(mFruit.image)
            .into(iv_fruit_image)

        tv_fruit_title.text = mFruit.name
        tv_fruit_price.text = mFruit.price.toString()
        tv_fruit_desc.text = mFruit.description
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fruit_item, menu)
        mMenuEdit = menu!!.findItem(R.id.menu_fruit_edit)
        mMenuDone = menu.findItem(R.id.menu_fruit_done)
        mMenuUnfav = menu.findItem(R.id.menu_fruit_unfav)
        mMenuFav = menu.findItem(R.id.menu_fruit_fav)
        if (mFruit.isFavorite!!) {
            mMenuFav.isVisible = true
            mMenuUnfav.isVisible = false
        } else {
            mMenuFav.isVisible = false
            mMenuUnfav.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_fruit_edit -> {
                mLlfruitView.visibility = View.GONE
                mMenuDone.isVisible = true
                mMenuEdit.isVisible = false
                mMenuFav.isVisible = false
                mMenuUnfav.isVisible = false
                openEditLayout(mFruit)
                return true
            }
            R.id.menu_fruit_done -> {

                mMenuDone.isVisible = false
                mMenuEdit.isVisible = true
                updateFRUIT(
                    et_fruit_title.text.toString()
                )
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateFRUIT(name: String) {
        tv_fruit_title.text = name
    }

    private fun openEditLayout(fruit: Fruit) {
        mLlfruitView.visibility = View.GONE
        mLlfruitEdit.visibility = View.VISIBLE

        et_fruit_title.setText(fruit.name)
        et_fruit_price.setText(fruit.price.toString())
        et_fruit_desc.setText(fruit.description)
    }

    private fun openViewLayout(fruit: Fruit) {
        mLlfruitView.visibility = View.VISIBLE
        mLlfruitEdit.visibility = View.GONE
    }

    override fun getLayoutResource(): Int = R.layout.activity_fruit
}