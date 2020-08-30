package com.example.recipe.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.market.R
import com.example.recipe.db.entity.Recipe
import com.example.recipe.db.handler.RecipeAsyncQueryHandler
import com.example.recipe.util.Constant
import com.example.recipe.util.Constant.API.RECIPE_ITEM_DEFAULT_IMAGE

class RecipeAddActivity : BaseActivity(), View.OnClickListener,
    RecipeAsyncQueryHandler.AsyncQueryListener {

    private lateinit var mIvRecipeImage: ImageView
    private lateinit var mEtRecipeName: EditText
    private lateinit var mEtRecipePrice: EditText
    private lateinit var mEtRecipeDescription: EditText
    private lateinit var mBtnRecipeAdd: Button
    private lateinit var mMenuFavorite: MenuItem
    private lateinit var mRecipe: Recipe
    private lateinit var mRecipeAQH: RecipeAsyncQueryHandler

    override fun getLayoutResource(): Int = R.layout.activity_add_recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViews()
        setListeners()
        if (savedInstanceState == null)
            init()
        else
            getData(savedInstanceState)
    }


    private fun findViews() {
        mIvRecipeImage = findViewById(R.id.iv_add_recipe_logo)
        mEtRecipeName = findViewById(R.id.et_add_recipe_title)
        mEtRecipePrice = findViewById(R.id.et_add_recipe_price)
        mEtRecipeDescription = findViewById(R.id.et_add_recipe_description)
        mBtnRecipeAdd = findViewById(R.id.btn_add_recipe_add)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_recipe_item, menu)
        mMenuFavorite = menu!!.findItem(R.id.menu_recipe_favorite)
        if (mRecipe.isFavorite!!)
            mMenuFavorite.setIcon(R.drawable.ic_favorite)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_recipe_favorite -> {
                if (mRecipe.isFavorite!!) {
                    mMenuFavorite.setIcon(R.drawable.ic_unfavorite)
                    mRecipe.isFavorite = false
                } else {
                    mMenuFavorite.setIcon(R.drawable.ic_favorite)
                    mRecipe.isFavorite = true
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setListeners() {
        mBtnRecipeAdd.setOnClickListener(this)
        mIvRecipeImage.setOnClickListener(this)
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

    private fun init() {
        mRecipeAQH = RecipeAsyncQueryHandler(applicationContext, this)
        mRecipe = Recipe()
        mRecipe.id = System.currentTimeMillis()
        mRecipe.isFromUser = true
        mRecipe.image = RECIPE_ITEM_DEFAULT_IMAGE
        mEtRecipePrice.setText("0")
        Glide.with(this)
            .load(mRecipe.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mIvRecipeImage)
    }

    private fun getData(savedInstanceState: Bundle) {
        if (savedInstanceState.getParcelable<Recipe>(Constant.Extra.EXTRA_recipe) != null) {
            mRecipe = savedInstanceState.getParcelable(Constant.Extra.EXTRA_recipe)!!
            mEtRecipeName.setText(mRecipe.name)
            mEtRecipePrice.setText(mRecipe.price.toString())
            mEtRecipeDescription.setText(mRecipe.description)
            Glide.with(this)
                .load(mRecipe.image)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvRecipeImage)
        }
    }

    override fun onSaveInstanceState(saveInstanceState: Bundle) {
        super.onSaveInstanceState(saveInstanceState)
        mRecipe.name = mEtRecipeName.text.toString()
        mRecipe.price = mEtRecipePrice.text.toString().toLong()
        mRecipe.description = mEtRecipeDescription.text.toString()
        saveInstanceState.putParcelable(Constant.Extra.EXTRA_recipe, mRecipe)
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_add_recipe_add -> {
                if (mEtRecipeName.text.isEmpty()) {
                    Toast.makeText(this, R.string.msg_edt_title_error, Toast.LENGTH_SHORT).show()
                    return
                } else if (mEtRecipePrice.text.isEmpty()) {
                    Toast.makeText(this, R.string.msg_edt_price_error, Toast.LENGTH_SHORT).show()
                    return
                }
                mRecipe.name = mEtRecipeName.text.toString()
                mRecipe.price = mEtRecipePrice.text.toString().toLong()
                mRecipe.description = mEtRecipeDescription.text.toString()
                addRecipeDialog()
                return
            }
        }
    }

    private fun addRecipeDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .setMessage(R.string.msg_dialog_add_recipe)
            .setPositiveButton(R.string.text_btn_dialog_ok) { dialog, which ->
                mRecipeAQH.addRecipe(mRecipe)
            }
            .setNegativeButton(R.string.text_btn_dialog_cancel, null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
        val intent = Intent()
        intent.putExtra(Constant.Extra.EXTRA_recipe, mRecipe)
        setResult(RESULT_OK, intent)
//        AppUtil.sendNotification(
//            applicationContext,
//            MainActivity::class.java,
//            getString(R.string.app_name),
//            getString(R.string.notif_add) + " " + mrecipe.name,
//            mrecipe.name,
//            Constant.NotifyType.ADD
//        )
        finish()
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
    }


}