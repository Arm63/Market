package com.example.recipe.ui.fragment

import RecipeAdapter
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.market.R
import com.example.recipe.db.cursor.CursorReader
import com.example.recipe.db.entity.Recipe
import com.example.recipe.db.handler.RecipeAsyncQueryHandler
import com.example.recipe.db.handler.RecipeQueryHandler
import com.example.recipe.io.bus.BusProvider
import com.example.recipe.io.service.RecipeIntentService
import com.example.recipe.ui.activity.RecipeAddActivity
import com.example.recipe.ui.activity.RecipeInfoActivity
import com.example.recipe.util.Constant
import com.example.recipe.util.Constant.RequestCode.ADD_RECIPE_ACTIVITY
import com.example.recipe.util.NetworkUtil
import com.example.recipe.util.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RecipeListFragment : BaseFragment(), RecipeAdapter.OnItemClickListener,
    View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
    RecipeAsyncQueryHandler.AsyncQueryListener {

    companion object {
        fun newInstance(): RecipeListFragment = RecipeListFragment()
        private const val REQUEST_CODE = 100
        private val LOG_TAG: String = RecipeListFragment::class.java.simpleName
    }


    private var icon: Drawable? = null
    private var background: ColorDrawable? = null
    private var coordinatorLayout: CoordinatorLayout? = null

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mRecyclerViewAdapter: RecipeAdapter
    private lateinit var mRecipeList: ArrayList<Recipe>
    private lateinit var mFloatingActionButton: FloatingActionButton
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecipeAQH: RecipeAsyncQueryHandler


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)
        BusProvider.register(this)
        findViews(view)
        init()
        setListeners()
        onRefresh()
        enableSwipeToDeleteAndUndo()

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.d("Start", "sadasdasdsadasdasdsa")
        mRecipeAQH.getRecipes()
    }


    private fun findViews(view: View?) {
        mRecyclerView = view?.findViewById<View>(R.id.rv_main) as RecyclerView
        mSwipeRefreshLayout =
            view.findViewById<View>(R.id.sw_fragment_recipe_list) as SwipeRefreshLayout
        mFloatingActionButton = view.findViewById(R.id.fl_btn_recipe_add) as FloatingActionButton
    }

    private fun init() {
        icon = context?.let { ContextCompat.getDrawable(it, R.drawable.ic_delete_white) };
        background = ColorDrawable(Color.RED)

        mRecipeAQH = activity?.applicationContext?.let { RecipeAsyncQueryHandler(it, this) }!!

        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.itemAnimator = DefaultItemAnimator()

        mRecipeList = ArrayList()
        mRecyclerViewAdapter = context?.let { RecipeAdapter(it, mRecipeList, this) }!!
        mRecyclerView.adapter = mRecyclerViewAdapter
        mSwipeRefreshLayout.isRefreshing = false


//        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }


    private fun setListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mFloatingActionButton.setOnClickListener(this)
    }

// ===========================================================
// Constructors
// ===========================================================


// ===========================================================
// Other Listeners, methods for/from Interfaces
// ===========================================================

//    @Subscribe
//    fun onEventReceived(apiEvent: ApiEvent<Any?>) {
//        if (!apiEvent.isSuccess) {
//            mrecipeAQH.getrecipes()
//            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show()
//        } else {
////            mSwipeRefreshLayout.isRefreshing = true
//        }
//    }



    private fun enableSwipeToDeleteAndUndo() {
        val swipeToDeleteCallback: SwipeToDeleteCallback =
            object : SwipeToDeleteCallback(this@RecipeListFragment) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                    val position = viewHolder.adapterPosition
                    val item: Recipe = mRecyclerViewAdapter.getData()[position]
                    mRecipeList.removeAt(position)
                    mRecyclerViewAdapter.notifyItemRemoved(position)
                    mRecipeAQH.deleteRecipe(item, position)
                    val snackbar = mRecyclerView.let {
                        Snackbar
                            .make(
                                it,
                                "Item was removed from the list.",
                                Snackbar.LENGTH_LONG
                            )
                    }
                    snackbar.setAction("UNDO") {
                        mRecyclerViewAdapter.restoreItem(item, position)
                        mRecyclerView.scrollToPosition(position)
                    }
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(mRecyclerView)
    }

    override fun onItemClick(item: Recipe, position: Int) {
        val intent = Intent(activity, RecipeInfoActivity::class.java)
        intent.putExtra(Constant.Extra.recipe_ID, item.id)
        startActivity(intent)
    }

    override fun onItemLongClick(item: Recipe, position: Int) {
        openDeleterecipeDialog(item, position)
        Log.d("asdasdasda", " " + RecipeQueryHandler.getRecipes(context!!)?.size)
    }


    private fun openDeleterecipeDialog(recipe: Recipe, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.msg_dialog_delete_recipe)
            .setCancelable(false)
            .setPositiveButton(R.string.text_btn_dialog_yes) { dialog, _ ->
                mRecipeAQH.deleteRecipe(recipe, position)
                mRecipeList.removeAt(position)
                mRecyclerViewAdapter.notifyItemRemoved(position)
                dialog.cancel()
            }
            .setNegativeButton(
                R.string.text_btn_dialog_no
            ) { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }


    override fun onQueryComplete(token: Int, cookie: Any?, cursor: Cursor?) {
        when (token) {
            RecipeAsyncQueryHandler.QueryToken.GET_RECIPES -> {
                val recipes = cursor?.let { CursorReader.parseRecipes(it) }
                if (recipes!!.size != 0) {
                    mRecipeList.clear()
                    mRecipeList.addAll(recipes)
                    mRecyclerViewAdapter.notifyDataSetChanged()
                } else {
                    mRecipeList.clear()
                    mRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onInsertComplete(token: Int, cookie: Any?, uri: Uri?) {
    }

    override fun onUpdateComplete(token: Int, cookie: Any?, result: Int) {
    }

    override fun onDeleteComplete(token: Int, cookie: Any?, result: Int) {

    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = true
        if (NetworkUtil.instance!!.isConnected(context!!) && RecipeQueryHandler.getRecipes(
                context!!
            )?.size == 0
        )
            RecipeIntentService.start(
                activity!!,
                Constant.API.recipe_LIST,
                Constant.RequestType.recipe_LIST
            )

        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        BusProvider.unregister(this)
        super.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fl_btn_recipe_add -> {
                val intent = Intent(activity, RecipeAddActivity::class.java)
                startActivityForResult(intent, ADD_RECIPE_ACTIVITY)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ADD_RECIPE_ACTIVITY -> {
                    val recipe: Recipe = data!!.getParcelableExtra(Constant.Extra.EXTRA_recipe)
                    mRecipeList.add(recipe)
                    mRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}