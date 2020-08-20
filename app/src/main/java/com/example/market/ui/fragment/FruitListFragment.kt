package com.example.market.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.market.R
import com.example.market.db.entity.Fruit
import com.example.market.db.entity.FruitResponse
import com.example.market.ui.adapter.FruitRecyclerAdapter
import com.example.market.util.Constant.API.FRUIT_LIST
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_fruit_list.*
import okhttp3.*
import java.io.IOException
import androidx.recyclerview.widget.RecyclerView as RecyclerView1


class FruitListFragment : BaseFragment(), FruitRecyclerAdapter.OnItemClickListener {

    companion object {
        fun newInstance(): FruitListFragment? = FruitListFragment()
    }

    private lateinit var mRecyclerView: RecyclerView1
    private lateinit var mRecyclerViewAdapter: FruitRecyclerAdapter
    private lateinit var mFruitList: ArrayList<Fruit>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_fruit_list, container, false)
        findViews(view)
        init()
        setListeners()
        fetchJson()
        return view
    }

    private fun findViews(view: View?) {
        mRecyclerView = view!!.findViewById<View>(R.id.rv_main) as RecyclerView1
    }

    private fun init() {

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        mFruitList = ArrayList()
        mRecyclerViewAdapter = FruitRecyclerAdapter(mFruitList, this)
        mRecyclerView.adapter = mRecyclerViewAdapter

    }

    private fun setListeners() {

    }

    private fun loadData() {
        TODO("Not yet implemented")
    }

    // ===========================================================
    // Constructors
    // ===========================================================

    private fun fetchJson() {
        val request = Request.Builder().url(FRUIT_LIST).build()
        val client = OkHttpClient();
        var items: FruitResponse
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                items = gson.fromJson(body, FruitResponse::class.java)
                activity?.runOnUiThread(Runnable {
                    rv_main.layoutManager = LinearLayoutManager(context)
                    if (items != null) {
                        rv_main.adapter = FruitRecyclerAdapter(items.fruits, this@FruitListFragment)
                    }
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

//    private fun initRecycleView(items: FruitResponse?) {
//        rv_main.layoutManager = LinearLayoutManager(context)
//        if (items != null) {
//            rv_main.adapter = FruitRecyclerAdapter(items.fruits, this@FruitListFragment)
//        }
//    }

    override fun onItemClick(item: Fruit, position: Int) {
        // openDeleteProductDialog(item, position)
    }

    override fun onItemLongClick(item: Fruit, position: Int) {
        //openDeleteProductDialog(item, position)
    }

//    private fun openDeleteProductDialog(item: Fruit, position: Int) {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
//        builder.setMessage(R.string.delete_product)
//            .setCancelable(false)
//            .setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
//                mFruitList.removeAt(position)
//                mRecyclerViewAdapter.notifyItemRemoved(position)
//                dialog.cancel()
//            })
//            .setNegativeButton(R.string.no,
//                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
//    }
}