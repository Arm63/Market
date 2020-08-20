package com.example.market.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.market.R
import com.example.market.ui.fragment.FruitListFragment
import com.example.market.util.Constant
import com.example.market.util.FragmentTransactionManager
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {


    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String? = MainActivity::class.java.simpleName

    // ===========================================================
    // Fields
    // ===========================================================
    private var mDrawerLayout: DrawerLayout? = null
    private var mNavigationView: NavigationView? = null

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
        findViews()
        setListeners()
        customizeActionBar()
        initDrawer()
        openScreen(
            FruitListFragment.newInstance()!!,
            R.id.nav_fruit_list,
            false
        )
        catchNotificationData()
    }

    override fun findViews() {
        mDrawerLayout = findViewById<View>(R.id.dl_main) as DrawerLayout
        mNavigationView = findViewById<View>(R.id.nav_main) as NavigationView
    }

    private fun customizeActionBar() {
        setActionBarTitle(getString(R.string.app_name))
    }

    override fun getLayoutResource(): Int = R.layout.activity_main

    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================

    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // ===========================================================
    // Methods
    // ===========================================================


    private fun openScreen(fragment: Fragment, item: Int, addToBackStack: Boolean) {
        mNavigationView!!.menu.findItem(item).isChecked = true
        FragmentTransactionManager.displayFragment(
            supportFragmentManager,
            fragment,
            R.id.fl_main_container,
            addToBackStack
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_fruit_list ->
                FruitListFragment.newInstance()?.let {
                    openScreen(
                        it,
                        R.id.nav_fruit_list,
                        false
                    )
                }
            R.id.nav_fv_list -> {
//                FruitListFragment.newInstance()?.let {
//                    openScreen(
//                        it,
//                        R.id.nav_fv_list,
//                        true
//                    )

                Toast.makeText(this, "hl@ chka es fragment@", Toast.LENGTH_LONG)
                    .show()
            }
        }
        mDrawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(v: View) {}


    private fun setListeners() {
        mNavigationView!!.setNavigationItemSelectedListener(this)
    }

    private fun initDrawer() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            mToolbar,
            R.string.msg_navigation_drawer_open,
            R.string.msg_navigation_drawer_close
        )
        mDrawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    private fun catchNotificationData() {
        val notificationMessage = intent.getStringExtra(Constant.Extra.NOTIFICATION_DATA)
        if (notificationMessage != null) {
            Toast.makeText(this, notificationMessage, Toast.LENGTH_LONG).show()
        }
    }
}

