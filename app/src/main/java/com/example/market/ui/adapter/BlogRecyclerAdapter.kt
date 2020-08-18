package com.example.market.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.market.R
import com.example.market.db.entity.BlogPost
import kotlinx.android.synthetic.main.layout_blog_list_item.view.*

class BlogRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<BlogPost> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BLogViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_blog_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BLogViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun submitList(fruitList: List<BlogPost>) {
        items = fruitList
    }

    class BLogViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(blogPost: BlogPost) {

            itemView.tv_fruit_item_name.text = blogPost.fruit_Name
            itemView.tv_fruit_item_price.text = blogPost.price.toString()

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(blogPost.image)
                .into(itemView.iv_fruit_item)
        }
    }

}


