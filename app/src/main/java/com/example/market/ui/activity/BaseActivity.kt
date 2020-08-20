package com.example.market.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.market.R
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.include_toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG = BaseActivity::class.java.simpleName

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
    // Methods for/from SuperClass
    // ===========================================================
    var mToolbar: Toolbar? = null
    private var mTabLayout: TabLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())
        findViews()

        if (mToolbar != null) {
            setSupportActionBar(mToolbar as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================
    // ===========================================================
    // Methods
    // ===========================================================
    open fun findViews() {
        mTabLayout = findViewById(R.id.ctl)
        mToolbar = findViewById(R.id.tb)
    }

    open fun getToolBar(): Toolbar? {
        return mToolbar
    }

    protected open fun getTabLayout(): TabLayout? {
        return mTabLayout
    }
    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================
    // ===========================================================
    // Methods
    // ===========================================================

    open fun setActionBarTitle(title: String?) {
        tv_toolbar_title.text = title
        mToolbar?.title = title
    }

    protected open fun hideActionBar() {
        supportActionBar!!.hide()
    }

    protected open fun showActionBar() {
        supportActionBar!!.show()
    }
/// dzerov poxelem dzeva sxal ashxati
    open fun setActionBarSubTitle(subtitle: String?) {
        tv_toolbar_title!!.visibility = View.VISIBLE
        tv_toolbar_subtitle!!.text = subtitle
    }

    open fun hideActionBarIcon() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    open fun showActionBarIcon() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    protected abstract fun getLayoutResource(): Int

}