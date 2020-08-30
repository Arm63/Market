package com.example.recipe.ui.activity

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.market.R
import com.example.recipe.db.cursor.CursorReader
import com.example.recipe.db.entity.Recipe
import com.example.recipe.db.handler.RecipeAsyncQueryHandler
import com.example.recipe.io.bus.BusProvider
import com.example.recipe.io.bus.event.ApiEvent
import com.example.recipe.io.service.RecipeIntentService
import com.example.recipe.util.AppUtil
import com.example.recipe.util.Constant
import com.example.recipe.util.NetworkUtil
import com.google.common.eventbus.Subscribe
import kotlinx.android.synthetic.main.activity_recipe_info.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVR_SUCCEEDS")
class RecipeInfoActivity : BaseActivity(), RecipeAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Constants
    // ===========================================================
    private val LOG_TAG: String =
        com.example.recipe.ui.activity.RecipeInfoActivity::class.java.simpleName

    // ===========================================================
    // Fields
    // ===========================================================
    private var isStillEditing: Boolean = false
    private var mrecipe: Recipe? = null
    private lateinit var mMenuEdit: MenuItem
    private lateinit var mMenuDone: MenuItem
    private lateinit var mMenuFavorite: MenuItem
    private lateinit var mIvRecipeImage: ImageView
    private lateinit var mLlRecipeView: LinearLayout
    private lateinit var mLlRecipeEdit: LinearLayout
    private lateinit var mEtRecipeName: EditText
    private lateinit var mEtRecipePrice: EditText
    private lateinit var mEtRecipeDesc: EditText
    private lateinit var mRecipeAQH: RecipeAsyncQueryHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusProvider.register(this)
        findView()
        setListeners()
        init()
        getData()
        customizeActionBar()
    }

    private fun setListeners() {
        mEtRecipePrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s[0] == '0') {
                    var i = 0
                    while (s.length >= i + 2 && s.toString()[i] == '0') {
                        i++
                    }
                    mEtRecipePrice.removeTextChangedListener(this)
                    mEtRecipePrice.setText(s.subSequence(i, s.length))
                    mEtRecipePrice.addTextChangedListener(this)
                    if (count > 0) {
                        mEtRecipePrice.setSelection(start + count - i)
                    } else {
                        mEtRecipePrice.setSelection(start)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    mEtRecipePrice.removeTextChangedListener(this)
                    mEtRecipePrice.setText("0")
                    mEtRecipePrice.addTextChangedListener(this)
                    mEtRecipePrice.setSelection(1)
                }
            }
        })
    }

    private fun customizeActionBar() {
        setActionBarTitle(mrecipe?.name)
    }

    override fun getLayoutResource(): Int = R.layout.activity_recipe_info


    private fun findView() {
        mIvRecipeImage = findViewById(R.id.iv_recipe_image)
        mLlRecipeView = findViewById(R.id.ll_recipe_view)
        mLlRecipeEdit = findViewById(R.id.ll_recipe_edit)
        mEtRecipeName = findViewById(R.id.et_recipe_title)
        mEtRecipePrice = findViewById(R.id.et_recipe_price)
        mEtRecipeDesc = findViewById(R.id.et_recipe_desc)
    }

    private fun init() {
        mRecipeAQH = RecipeAsyncQueryHandler(this@RecipeInfoActivity, this)
    }

    private fun getData() {
        val productId = intent.getLongExtra(Constant.Extra.recipe_ID, 0)
        mRecipeAQH.getRecipe(productId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe_item, menu)

        mMenuEdit = menu!!.findItem(R.id.menu_recipe_edit)
        mMenuDone = menu.findItem(R.id.menu_recipe_done)
        mMenuFavorite = menu.findItem(R.id.menu_recipe_favorite)



        if (mrecipe?.isFavorite!!) {
            mMenuFavorite.setIcon(R.drawable.ic_favorite)
        }
        if (isStillEditing) {
            mMenuDone.isVisible = true
            mMenuEdit.isVisible = false
            mMenuFavorite.isVisible = false
        } else {
            mMenuEdit.isVisible = true
        }
        mMenuEdit.isVisible = mrecipe?.isFromUser!!

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_recipe_edit -> {
                isStillEditing = true
                mMenuDone.isVisible = true
                mMenuEdit.isVisible = false
                mMenuFavorite.isVisible = false
                mrecipe?.let { openEditLayout(it) }
                return true
            }
            R.id.menu_recipe_done -> {
                isStillEditing = false
                mMenuDone.isVisible = false
                mMenuEdit.isVisible = true
                mMenuFavorite.isVisible = true

                updateRecipe(
                    mEtRecipeName.text.toString(),
                    mEtRecipePrice.text.toString().toLong(),
                    mEtRecipeDesc.text.toString()
                )
                mrecipe?.let { openViewLayout(it) }
                return true
            }
            R.id.menu_recipe_favorite -> {
                Log.d("asdasdasdas", mrecipe?.isFavorite.toString())
                if (mrecipe?.isFavorite!!) {
                    mMenuFavorite.setIcon(R.drawable.ic_unfavorite)
                    mrecipe!!.isFavorite = false
                } else {
                    mMenuFavorite.setIcon(R.drawable.ic_favorite)
                    mrecipe!!.isFavorite = true
                }
                mRecipeAQH.updateRecipe(mrecipe!!)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================
//poxelem
    private fun updateRecipe(name: String, price: Long, desc: String) {
        mrecipe?.name = name
        mrecipe?.price = price
        mrecipe?.description = desc
        mrecipe?.let { mRecipeAQH.updateRecipe(it) }
    }

    override fun onSaveInstanceState(saveInstanceState: Bundle) {
        super.onSaveInstanceState(saveInstanceState)
        if (isStillEditing) {
            mrecipe?.description = mEtRecipeDesc.text.toString()
            mrecipe?.price = mEtRecipePrice.text.toString().toLong()
            mrecipe?.name = mEtRecipeName.text.toString()
            saveInstanceState.putParcelable(Constant.Extra.RECIPE, mrecipe)
        }
    }


    private fun openEditLayout(recipe: Recipe) {
        mLlRecipeView.visibility = View.GONE
        mLlRecipeEdit.visibility = View.VISIBLE
        Glide.with(this)
            .load(recipe.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mIvRecipeImage)

        mEtRecipeName.setText(recipe.name)
        mEtRecipePrice.setText(recipe.price.toString())
        mEtRecipeDesc.setText(recipe.description)
    }

    private fun openViewLayout(recipe: Recipe) {
        AppUtil.closeKeyboard(this)

        mLlRecipeView.visibility = View.VISIBLE
        mLlRecipeEdit.visibility = View.GONE
        Glide.with(applicationContext)
            .load(recipe.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mIvRecipeImage)

        tv_recipe_title.text = "Name: " + recipe.name
        tv_recipe_price.text = "Price: " + recipe.price.toString()
        tv_recipe_desc.text = recipe.description
    }


    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        when (token) {
            RecipeAsyncQueryHandler.QueryToken.GET_recipe -> {
                mrecipe = cursor?.let { CursorReader.parseRecipe(it) }
                if (!mrecipe!!.isFromUser!!)
                    loadRecipe()
                openViewLayout(mrecipe!!)
            }
        }
    }

    private fun loadRecipe() {
        if (NetworkUtil.instance?.isConnected(this)!!) {
            RecipeIntentService.start(
                this,
                Constant.API.recipe_ITEM + java.lang.String.valueOf(mrecipe?.id) + Constant.API.recipe_ITEM_POSTFIX,
                Constant.RequestType.recipe_ITEM
            )
        }
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
        RecipeAsyncQueryHandler.QueryToken.UPDATE_recipe
        openViewLayout(mrecipe!!)
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================
    @Subscribe
    fun onEventReceived(apiEvent: ApiEvent<Any?>) {
        if (apiEvent.eventType == ApiEvent.EventType.RECIPE_ITEM_LOADED) {
            if (apiEvent.isSuccess) {
                mrecipe = apiEvent.eventData as Recipe
                openViewLayout(mrecipe!!)
            } else {
                Toast.makeText(this, R.string.msg_some_error, Toast.LENGTH_SHORT).show()
            }
        }
    }


}