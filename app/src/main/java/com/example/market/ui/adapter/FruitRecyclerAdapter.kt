package com.example.market.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.market.R
import com.example.market.db.entity.Fruit
import kotlinx.android.synthetic.main.layout_fruit_list_item.view.*


class FruitRecyclerAdapter(
    var mFruitList: ArrayList<Fruit>,
    var onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<FruitRecyclerAdapter.FruitViewHolder>() {

    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String = FruitRecyclerAdapter::class.java.simpleName

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_fruit_list_item, parent, false)
        return FruitViewHolder(view, mFruitList, onItemClickListener)
    }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        holder.bindData(mFruitList[position])
    }

    override fun getItemCount(): Int = mFruitList.size

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================

    // ===========================================================
    // Click Listeners
    // ===========================================================

    // ===========================================================
    // Methods
    // ===========================================================

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    //------------------------------------------------------------------------------------------------

    class FruitViewHolder constructor(
        itemView: View,
        var fruitList: ArrayList<Fruit>,
        private var onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        var llItemContainer: LinearLayout? = itemView.findViewById(R.id.ll_fruit_item_container)

        fun bindData(fruit: Fruit) {
            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(fruit.image)
                .into(itemView.iv_fruit_item)

            itemView.tv_fruit_item_name.text = fruit.name
            itemView.tv_fruit_item_price.text = fruit.price.toString()
            Log.d("bindi mejic", fruit.toString())

            llItemContainer!!.setOnClickListener {
                onItemClickListener.onItemClick(fruitList[adapterPosition], adapterPosition)
                Log.d("bindi clicki mejic", fruit.toString())
            }
            llItemContainer!!.setOnLongClickListener {
                onItemClickListener.onItemLongClick(fruitList[adapterPosition], adapterPosition)
                true
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Fruit, position: Int)
        fun onItemLongClick(item: Fruit, position: Int)
    }

}


