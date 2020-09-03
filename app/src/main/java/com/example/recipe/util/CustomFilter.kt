package com.example.recipe.util

import RecipeAdapter
import android.widget.Filter
import com.example.recipe.db.entity.Recipe
import java.util.*

class CustomFilter(var filterList: ArrayList<Recipe>, var adapter: RecipeAdapter) : Filter() {


    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            constraint = constraint.toString().toUpperCase()
            val filteredPlayers: ArrayList<Recipe> = ArrayList<Recipe>()
            for (i in filterList.indices) {
                if (filterList[i].name?.toUpperCase()?.contains(constraint)!!) {
                    filteredPlayers.add(filterList[i])
                }
            }
            results.count = filteredPlayers.size
            results.values = filteredPlayers
        } else {
            results.count = filterList.size
            results.values = filterList
        }
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapter.mRecipeList = results.values as ArrayList<Recipe>
        adapter.notifyDataSetChanged()
    }

}