package com.example.market.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.market.ui.activity.BaseActivity

abstract class BaseFragment : Fragment() {
    // ===========================================================
    // Constants
    // ===========================================================
    // ===========================================================
    // Fields
    // ===========================================================
    // ===========================================================
    // Constructors
    // ===========================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================
    // ===========================================================
    // Methods for/from SuperClass
    // ===========================================================
    // ===========================================================
    // Listeners, methods for/from Interfaces
    // ===========================================================
    // ===========================================================
    // Methods
    // ===========================================================
    protected fun hideActionBarIcon() {
        (getActivity() as BaseActivity).hideActionBarIcon()
    }

    protected fun showActionBarIcon() {
        (getActivity() as BaseActivity).showActionBarIcon()
    }

    protected fun setActionBarIcon() {
        (getActivity() as BaseActivity).hideActionBarIcon()
    }

    protected fun setActionBarTitle(actionBarTitle: String?) {
        (getActivity() as BaseActivity).setActionBarTitle(actionBarTitle)
    } // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================
}