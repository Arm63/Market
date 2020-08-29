package com.example.market.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.market.R
import com.example.market.db.cursor.CursorReader
import com.example.market.db.entity.Fruit
import com.example.market.db.handler.FruitAsyncQueryHandler
import com.example.market.db.handler.FruitQueryHandler
import com.example.market.io.bus.BusProvider
import com.example.market.io.service.FruitIntentService
import com.example.market.ui.activity.FruitInfoActivity
import com.example.market.ui.adapter.FruitRecyclerAdapter
import com.example.market.util.Constant
import com.example.market.util.NetworkUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FruitListFragment : BaseFragment(), FruitRecyclerAdapter.OnItemClickListener,
    View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    FruitAsyncQueryHandler.AsyncQueryListener {

    companion object {
        fun newInstance(): FruitListFragment = FruitListFragment()
        private const val REQUEST_CODE = 100
        private val LOG_TAG: String = FruitListFragment::class.java.simpleName
    }

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: FruitRecyclerAdapter
    private lateinit var mFruitList: ArrayList<Fruit>
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mFruitAQH: FruitAsyncQueryHandler

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
        onRefresh()
//        mSwipeRefreshLayout.setOnRefreshListener {  }

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d("Start", "sadasdasdsadasdasdsa")
        mFruitAQH.getFruits()
    }


    private fun findViews(view: View?) {
        mRecyclerView = view?.findViewById<View>(R.id.rv_main) as RecyclerView
        mSwipeRefreshLayout =
            view.findViewById<View>(R.id.sw_fragment_fruit_list) as SwipeRefreshLayout
        mFloatingActionButton = view.findViewById(R.id.fl_btn_fruit_add) as FloatingActionButton
    }

    private fun init() {
        mFruitAQH = activity?.applicationContext?.let { FruitAsyncQueryHandler(it, this) }!!

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        mFruitList = ArrayList()
        Log.d("initi mejic size", "" + mFruitList.size)
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


    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

//    @Subscribe
//    fun onEventReceived(apiEvent: ApiEvent<Any?>) {
//        if (!apiEvent.isSuccess) {
//            mFruitAQH.getFruits()
//            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
//        } else {
////            mSwipeRefreshLayout.isRefreshing = true
//        }
//    }


    override fun onItemClick(item: Fruit, position: Int) {
        Log.d("aj yanna = ", item.description)
        val intent = Intent(activity, FruitInfoActivity::class.java)
        intent.putExtra(Constant.Extra.FRUIT_ID, item.id)
        startActivity(intent)
    }

    override fun onItemLongClick(item: Fruit, position: Int) {
        openDeleteFruitDialog(item, position)
        Log.d("asdasdasda", " " + FruitQueryHandler.getFruits(context!!)?.size )
    }


    private fun openDeleteFruitDialog(fruit: Fruit, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.delete_fruit)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { dialog, id ->
                mFruitAQH.deleteFruit(fruit, position)
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
                if (fruits!!.size != 0) {
                    mFruitList.clear()
                    mFruitList.addAll(fruits)
                    mRecyclerViewAdapter.notifyDataSetChanged()
                } else {
                    mFruitList.clear()
                    mRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {

    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = true
        if (NetworkUtil.instance!!.isConnected(context!!) && FruitQueryHandler.getFruits(context!!)?.size == 0)
            FruitIntentService.start(
                activity!!,
                Constant.API.FRUIT_LIST,
                Constant.RequestType.FRUIT_LIST
            )

        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        BusProvider.unregister(this)
        super.onDestroyView()
    }

    override fun onClick(v: View?) {

    }
}