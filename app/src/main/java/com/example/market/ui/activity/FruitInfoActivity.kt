package com.example.market.ui.activity

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
import com.example.market.db.cursor.CursorReader
import com.example.market.db.entity.Fruit
import com.example.market.db.handler.FruitAsyncQueryHandler
import com.example.market.io.bus.BusProvider
import com.example.market.io.bus.event.ApiEvent
import com.example.market.io.service.FruitIntentService
import com.example.market.util.AppUtil
import com.example.market.util.Constant
import com.example.market.util.NetworkUtil
import com.google.common.eventbus.Subscribe
import kotlinx.android.synthetic.main.activity_fruit_info.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVR_SUCCEEDS")
class FruitInfoActivity : BaseActivity(), FruitAsyncQueryHandler.AsyncQueryListener {

    // ===========================================================
    // Constants
    // ===========================================================


    // ===========================================================
    // Constants
    // ===========================================================
    private val LOG_TAG: String = FruitInfoActivity::class.java.simpleName

    // ===========================================================
    // Fields
    // ===========================================================
    private var isStillEditing: Boolean = false
    private var mFruit: Fruit? = null
    private lateinit var mMenuEdit: MenuItem
    private lateinit var mMenuDone: MenuItem
    private lateinit var mMenuFavorite: MenuItem
    private lateinit var mIvFruitImage: ImageView
    private lateinit var mLlFruitView: LinearLayout
    private lateinit var mLlFruitEdit: LinearLayout
    private lateinit var mEtFruitName: EditText
    private lateinit var mEtFruitPrice: EditText
    private lateinit var mEtFruitDesc: EditText
    private lateinit var mFruitAQH: FruitAsyncQueryHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BusProvider.register(this)
        findView()
        setListeners()
        init()
        getData(savedInstanceState)

        customizeActionBar()
    }

    private fun setListeners() {
        mEtFruitPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s[0] == '0') {
                    var i = 0
                    while (s.length >= i + 2 && s.toString()[i] == '0') {
                        i++
                    }
                    mEtFruitPrice.removeTextChangedListener(this)
                    mEtFruitPrice.setText(s.subSequence(i, s.length))
                    mEtFruitPrice.addTextChangedListener(this)
                    if (count > 0) {
                        mEtFruitPrice.setSelection(start + count - i)
                    } else {
                        mEtFruitPrice.setSelection(start)
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    mEtFruitPrice.removeTextChangedListener(this)
                    mEtFruitPrice.setText("0")
                    mEtFruitPrice.addTextChangedListener(this)
                    mEtFruitPrice.setSelection(1)
                }
            }
        })
    }

    private fun customizeActionBar() {
        setActionBarTitle(mFruit?.name)
    }

    override fun getLayoutResource(): Int = R.layout.activity_fruit_info


    private fun findView() {
        mIvFruitImage = findViewById(R.id.iv_fruit_image)
        mLlFruitView = findViewById(R.id.ll_fruit_view)
        mLlFruitEdit = findViewById(R.id.ll_fruit_edit)
        mEtFruitName = findViewById(R.id.et_fruit_title)
        mEtFruitPrice = findViewById(R.id.et_fruit_price)
        mEtFruitDesc = findViewById(R.id.et_fruit_desc)
    }

    private fun init() {
        mFruitAQH = FruitAsyncQueryHandler(this@FruitInfoActivity, this)
    }

    private fun getData(bundle: Bundle?) {
        val productId = intent.getLongExtra(Constant.Extra.FRUIT_ID, 0)
        mFruitAQH.getFruit(productId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_fruit_item, menu)

        mMenuEdit = menu!!.findItem(R.id.menu_fruit_edit)
        mMenuDone = menu.findItem(R.id.menu_fruit_done)
        mMenuFavorite = menu.findItem(R.id.menu_fruit_favorite)

        if (mFruit?.isFavorite!!) {
            mMenuFavorite.setIcon(R.drawable.ic_favorite)
        }
        if (isStillEditing) {
            mMenuDone.isVisible = true
            mMenuEdit.isVisible = false
            mMenuFavorite.isVisible = false
        } else {
            mMenuEdit.isVisible = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_fruit_edit -> {
                isStillEditing = true
                mMenuDone.isVisible = true
                mMenuEdit.isVisible = false
                mMenuFavorite.isVisible = false
                mFruit?.let { openEditLayout(it) }
                return true
            }
            R.id.menu_fruit_done -> {
                isStillEditing = false
                mMenuDone.isVisible = false
                mMenuEdit.isVisible = true
                mMenuFavorite.isVisible = true

                updateFruit(
                    mEtFruitName.text.toString(),
                    mEtFruitPrice.text.toString().toInt(),
                    mEtFruitDesc.text.toString()
                )
                mFruit?.let { openViewLayout(it) }
                return true
            }
            R.id.menu_fruit_favorite -> {
                if (mFruit?.isFavorite!!) {
                    mMenuFavorite.setIcon(R.drawable.ic_unfavorite)
                    mFruit!!.isFavorite = false
                } else {
                    mMenuFavorite.setIcon(R.drawable.ic_favorite)
                    mFruit!!.isFavorite = true
                }
                mFruitAQH.updateFruit(mFruit!!)
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    // ===========================================================
    // Click Listeners
    // ===========================================================
//poxelem
    private fun updateFruit(name: String, price: Int, desc: String) {
        mFruit?.name = name
        mFruit?.price = price
        mFruit?.description = desc
        mFruit?.let { mFruitAQH.updateFruit(it) }
        mFruitAQH.updateFruit(mFruit!!)
    }

    override fun onSaveInstanceState(saveInstanceState: Bundle) {
        super.onSaveInstanceState(saveInstanceState)
        if (isStillEditing) {
            mFruit?.description = mEtFruitDesc.text.toString()
            mFruit?.price = mEtFruitPrice.text.toString().toInt()
            mFruit?.name = mEtFruitName.text.toString()
            saveInstanceState.putParcelable(Constant.Extra.FRUIT, mFruit)
        }
    }


    private fun openEditLayout(fruit: Fruit) {
        mLlFruitView.visibility = View.GONE
        mLlFruitEdit.visibility = View.VISIBLE
        Glide.with(this)
            .load(fruit.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mIvFruitImage)

        mEtFruitName.setText(fruit.name)
        mEtFruitPrice.setText(fruit.price.toString())
        mEtFruitDesc.setText(fruit.description)
    }

    private fun openViewLayout(fruit: Fruit) {
        AppUtil.closeKeyboard(this)

        mLlFruitView.visibility = View.VISIBLE
        mLlFruitEdit.visibility = View.GONE
        Glide.with(this)
            .load(fruit.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(mIvFruitImage)

        tv_fruit_title.text = fruit.name
        tv_fruit_price.text = fruit.price.toString()
        tv_fruit_desc.text = fruit.description
    }


    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        when (token) {
            FruitAsyncQueryHandler.QueryToken.GET_FRUIT -> {
                mFruit = cursor?.let { CursorReader.parseFruit(it) }
                Log.d(LOG_TAG, " queryComplate  mejic" + mFruit!!.description)
                openViewLayout(mFruit!!)
                loadFruit()
            }
        }
    }

    private fun loadFruit() {
        if (NetworkUtil.instance?.isConnected(this)!!) {
            FruitIntentService.start(
                this,
                Constant.API.FRUIT_ITEM + java.lang.String.valueOf(mFruit?.id) + Constant.API.FRUIT_ITEM_POSTFIX,
                Constant.RequestType.FRUIT_ITEM
            )
        }
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {
        FruitAsyncQueryHandler.QueryToken.UPDATE_FRUIT
        openViewLayout(mFruit!!)
    }

    // ===========================================================
    // Other Listeners, methods for/from Interfaces
    // ===========================================================
    @Subscribe
    fun onEventReceived(apiEvent: ApiEvent<Any?>) {
        if (apiEvent.eventType == ApiEvent.EventType.FRUIT_ITEM_LOADED) {
            if (apiEvent.isSuccess) {
                mFruit = apiEvent.eventData as Fruit
                openViewLayout(mFruit!!)
            } else {
                Toast.makeText(this, R.string.msg_some_error, Toast.LENGTH_SHORT).show()
            }
        }
    }


}