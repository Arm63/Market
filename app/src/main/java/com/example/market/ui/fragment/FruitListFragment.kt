package com.example.market.ui.fragment

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.market.R
import com.example.market.db.cursor.CursorReader
import com.example.market.db.entity.Fruit
import com.example.market.db.handler.FruitAsyncQueryHandler
import com.example.market.io.bus.BusProvider
import com.example.market.io.service.FruitIntentService
import com.example.market.ui.activity.AddFruitActivity
import com.example.market.ui.activity.FruitActivity
import com.example.market.ui.adapter.FruitRecyclerAdapter
import com.example.market.util.AppUtil
import com.example.market.util.Constant
import com.example.market.util.NetworkUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FruitListFragment : BaseFragment(), FruitRecyclerAdapter.OnItemClickListener,
    View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    FruitAsyncQueryHandler.AsyncQueryListener {

    companion object {
        fun newInstance(): FruitListFragment? = FruitListFragment()
        private const val REQUEST_CODE = 100
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: FruitRecyclerAdapter
    private lateinit var mFruitList: ArrayList<Fruit>
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mFtAsyncQueryHandler: FruitAsyncQueryHandler


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_fruit_list, container, false)
        BusProvider.register(this)
        findViews(view)
        init()
        setListeners()
        loadData()
        return view
    }

    override fun onResume() {
        mFtAsyncQueryHandler.getFruits()
        super.onResume()
    }

    private fun findViews(view: View?) {
        mRecyclerView = view?.findViewById<View>(R.id.rv_main) as RecyclerView
        mSwipeRefreshLayout =
            view.findViewById<View>(R.id.sw_fragment_fruit_list) as SwipeRefreshLayout
        mFloatingActionButton = view.findViewById(R.id.fl_btn_fruit_add) as FloatingActionButton
    }

    private fun init() {
        mFtAsyncQueryHandler = FruitAsyncQueryHandler(activity!!.applicationContext, this)

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        mFruitList = ArrayList()
        mRecyclerViewAdapter = FruitRecyclerAdapter(mFruitList, this)
        mRecyclerView.adapter = mRecyclerViewAdapter
        mSwipeRefreshLayout.isRefreshing = false

    }

    private fun setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mFloatingActionButton.setOnClickListener(this)
    }

    // ===========================================================
    // Constructors
    // ===========================================================

    private fun loadData() {
        if (NetworkUtil.instance!!.isConnected(activity!!)) {
            mSwipeRefreshLayout.isRefreshing = true
            FruitIntentService.start(
                activity!!,
                Constant.API.FRUIT_LIST,
                Constant.RequestType.FRUIT_LIST
            )
        } else {
            mFtAsyncQueryHandler.getFruits()
            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            val fruit: Fruit = data!!.getParcelableExtra(Constant.Extra.FRUIT)
            AppUtil.sendNotification(
                context!!,
                activity!!,
                resources.getString(R.string.app_name),
                getString(R.string.text_added_new_product),
                fruit.name
            )
            mFruitList.add(fruit)
            mRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.fl_btn_fruit_add -> {
                val intent = Intent(activity, AddFruitActivity::class.java)
                this.startActivityForResult(
                    intent,
                    REQUEST_CODE
                )
            }
        }
    }

    override fun onItemClick(item: Fruit, position: Int) {
        val intent = Intent(context, FruitActivity::class.java)
        intent.putExtra("fstyush", item)
        startActivity(intent)
    }

    override fun onItemLongClick(item: Fruit, position: Int) {
        Log.d("long preess", "sadasd")
        openDeleteFruitDialog(item, position)
    }

    private fun openDeleteFruitDialog(item: Fruit, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.delete_product)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                mFtAsyncQueryHandler.deleteFruit(item, position)
                mFruitList.removeAt(position)
                mRecyclerViewAdapter.notifyItemRemoved(position)
                dialog.cancel()
            }
            .setNegativeButton(
                R.string.no
            ) { dialog, id -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }


    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        when (token) {
            FruitAsyncQueryHandler.QueryToken.GET_FRUITS -> {
                val fruits = cursor?.let { CursorReader.parseFruits(it) }
                mFruitList.clear()
                if (fruits != null) {
                    mFruitList.addAll(fruits)
                }
                mRecyclerViewAdapter.notifyDataSetChanged()
            }

        }
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
        TODO("Not yet implemented")
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
        TODO("Not yet implemented")
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {

    }

    override fun onRefresh() {
        if (NetworkUtil.instance!!.isConnected(context!!)) {
            FruitIntentService.start(
                activity!!,
                Constant.API.FRUIT_LIST,
                Constant.RequestType.FRUIT_LIST
            )
            mSwipeRefreshLayout.isRefreshing = false
        } else {
            mFruitList.clear()
            mRecyclerViewAdapter.notifyDataSetChanged()
            mFloatingActionButton.hide()
            Toast.makeText(context, R.string.text_no_network, Toast.LENGTH_LONG).show()
            mSwipeRefreshLayout.isRefreshing = false
        }
    }


    override fun onDestroyView() {
        BusProvider.unregister(this)
        super.onDestroyView()
    }
}