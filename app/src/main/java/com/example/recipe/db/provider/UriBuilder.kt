package com.example.recipe.db.provider

import android.net.Uri
import com.example.market.BuildConfig
import com.example.recipe.db.RecipeDB.RECIPE_TABLE

class UriBuilder {
    companion object {
        fun buildRecipeUri(id: Long): Uri =
            Uri.parse("content://" + BuildConfig.APPLICATION_ID + "/" + RECIPE_TABLE + "/" + id)

        fun buildRecipeUri(): Uri =
            Uri.parse("content://" + BuildConfig.APPLICATION_ID + "/" + RECIPE_TABLE)
    }
}