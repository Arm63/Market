package com.example.market.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.market.R
import com.example.market.db.entity.Fruit
import kotlinx.android.synthetic.main.layout_fruit_list_item.view.*

class FruitRecyclerAdapter(
    var mItems: ArrayList<Fruit>,
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
        return FruitViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_fruit_list_item, parent, false),
            mItems,
            onItemClickListener
        )
    }

    override fun onBindViewHolder(holder: FruitViewHolder, position: Int) {
        holder.bind(mItems[position])
    }

    override fun getItemCount(): Int = mItems.size


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
        private var fruitArrayList: ArrayList<Fruit>,
        private var onItemClickListener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(fruit: Fruit) {

            itemView.tv_fruit_item_name.text = fruit.fruitName
            itemView.tv_fruit_item_price.text = fruit.price.toString()

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(fruit.image)
                .into(itemView.iv_fruit_item)

            itemView.ll_fruit_item_container.setOnClickListener {
                onItemClickListener.onItemClick(fruitArrayList[adapterPosition], adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Fruit, position: Int)
        fun onItemLongClick(item: Fruit, position: Int)
    }

}


