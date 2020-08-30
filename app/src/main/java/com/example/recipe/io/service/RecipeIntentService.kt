package com.example.recipe.io.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.recipe.db.entity.Recipe
import com.example.recipe.db.entity.ResponseRecipe
import com.example.recipe.db.handler.RecipeQueryHandler
import com.example.recipe.io.bus.BusProvider
import com.example.recipe.io.bus.event.ApiEvent
import com.example.recipe.io.server.HttpRequestManager
import com.example.recipe.io.server.HttpResponseUtil
import com.example.recipe.util.Constant
import com.example.recipe.util.Preference
import com.google.gson.Gson
import java.net.HttpURLConnection

@Suppress("UNCHECKED_CAST")
class RecipeIntentService : IntentService(RecipeIntentService::class.java.simpleName) {
    // ===========================================================
    // Constants
    // ===========================================================

    private val LOG_TAG: String =
        com.example.recipe.io.service.RecipeIntentService::class.java.simpleName


    companion object {
        /**
         * @param url         - calling api url
         * @param requestType - string constant that helps us to distinguish what request it is
         * @param postEntity  - POST request entity (json string that must be sent on server)
         */
        fun start(context: Context, url: String, postEntity: String, requestType: Int) {
            val intent = Intent(context, RecipeIntentService::class.java)
            intent.putExtra(Constant.Extra.URL, url)
            intent.putExtra(Constant.Extra.REQUEST_TYPE, requestType)
            intent.putExtra(Constant.Extra.POST_ENTITY, postEntity)
            context.startService(intent)
        }

        fun start(context: Context, url: String, requestType: Int) {
            val intent = Intent(context, RecipeIntentService::class.java)
            intent.putExtra(Constant.Extra.URL, url)
            intent.putExtra(Constant.Extra.REQUEST_TYPE, requestType)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        val url = intent!!.extras!!.getString(Constant.Extra.URL)
        val data = intent.extras!!.getString(Constant.Extra.POST_ENTITY)
        val requestType = intent.extras!!.getInt(Constant.Extra.REQUEST_TYPE)

        val connection: HttpURLConnection
        when (requestType) {
            Constant.RequestType.recipe_LIST -> {
                connection = HttpRequestManager.executeRequest(
                    url,
                    Constant.RequestMethod.GET,
                    data
                )!!
                val jsonList = HttpResponseUtil.parseResponse(connection)
                val recipeResponse = Gson().fromJson<ResponseRecipe>(
                    jsonList,
                    ResponseRecipe::class.java
                )
                if (recipeResponse != null) {
                    val recipes = recipeResponse.recipes
                    for (recipe in recipes!!) {
                        if (recipe != null) {
                            if (Preference.getInstance(applicationContext)
                                    ?.getUserFavorites(recipe.id.toString())!!
                            ) {
                                recipe.isFavorite = true
                            }
                        }
                    }
//                    PlQueryHandler.updateProductsExceptFavorite(this, products)
                    RecipeQueryHandler.addRecipes(this, recipes as ArrayList<Recipe>?)

                    // post to UI
                    BusProvider.instance.post(
                        ApiEvent(
                            ApiEvent.EventType.RECIPE_LIST_LOADED,
                            true,
                            recipes
                        )
                    )
                    Log.d(LOG_TAG, "size of = " + recipes.size)

                } else
                    BusProvider.instance.post(
                        ApiEvent(
                            ApiEvent.EventType.RECIPE_LIST_LOADED,
                            false
                        )
                    )


            }
            Constant.RequestType.recipe_ITEM -> {
                connection = HttpRequestManager.executeRequest(
                    url,
                    Constant.RequestMethod.GET,
                    null
                )!!
                val jsonItem = HttpResponseUtil.parseResponse(connection)

                val recipe = Gson().fromJson<Recipe>(jsonItem, Recipe::class.java)
                if (recipe != null) {
                    RecipeQueryHandler.updateRecipeDescription(this, recipe)
                    BusProvider.instance
                        .post(ApiEvent(ApiEvent.EventType.RECIPE_ITEM_LOADED, true, recipe))

                } else {
                    BusProvider.instance
                        .post(ApiEvent(ApiEvent.EventType.RECIPE_LIST_LOADED, false))
                }
            }
        }
        Log.d(LOG_TAG, "serive ashxateluc heto")
    }
}
