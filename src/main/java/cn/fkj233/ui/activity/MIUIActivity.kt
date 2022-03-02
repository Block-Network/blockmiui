/*
 * BlockMIUI
 * Copyright (C) 2022 fkj@fkj233.cn
 * https://github.com/577fkj/BlockMIUI
 *
 * This software is free opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License v2.1
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by 577fkj.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * GNU Lesser General Public License v2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License v2.1
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/577fkj/BlockMIUI/blob/main/LICENSE>.
 */

@file:Suppress("DEPRECATION")

package cn.fkj233.ui.activity

import android.app.Activity
import android.app.FragmentManager
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.BaseView
import kotlin.system.exitProcess

/**
 * @version: V1.0
 * @author: 577fkj
 * @className: MIUIActivity
 * @packageName: MIUIActivity
 * @description: BaseActivity / 基本Activity
 * @data: 2022-02-05 18:30
 **/
open class MIUIActivity : Activity() {

    @Suppress("LeakingThis")
    private val activity = this

    private var callbacks: (() -> Unit)? = null

    private val dataBinding: DataBinding = DataBinding()

    private var thisName: ArrayList<String> = arrayListOf()

    private lateinit var viewData: InitView

    private val dataList: HashMap<String, InitView.ItemData> = hashMapOf()

    private lateinit var initViewData: InitView.() -> Unit

    val backButton by lazy {
        ImageView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.gravity = Gravity.CENTER_VERTICAL
                it.setMargins(0, 0, dp2px(activity, 5f),0)
            }
            background = getDrawable(R.drawable.abc_ic_ab_back_material)
            visibility = View.GONE
            setOnClickListener {
                this@MIUIActivity.onBackPressed()
            }
        }
    }

    private val menuButton by lazy {
        ImageView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL }
            background = getDrawable(R.drawable.abc_ic_menu_overflow_material)
            setPadding(0, 0, dp2px(activity, 25f),0)
            setOnClickListener {
                showFragment("Menu")
            }
        }
    }

    val titleView by lazy {
        TextView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also {
                it.gravity = Gravity.CENTER_VERTICAL
            }
            setTextColor(getColor(R.color.whiteText))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
            paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }

    private var frameLayoutId: Int = -1
    private val frameLayout by lazy {
        val mFrameLayout = FrameLayout(activity)
        frameLayoutId = View.generateViewId()
        mFrameLayout.id = frameLayoutId
        mFrameLayout
    }

    /**
     *  是否继续加载 / Continue loading
     */
    var isLoad = true

    /**
     *  返回到最后一页直接退出(不保留后台) / Return to the last page and exit directly (without retaining the background)
     */
    private var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        setContentView(LinearLayout(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            background = getDrawable(R.color.foreground)
            orientation = LinearLayout.VERTICAL
            addView(LinearLayout(activity).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                setPadding(dp2px(activity, 25f), dp2px(activity, 20f), dp2px(activity, 25f), dp2px(activity, 15f))
                orientation = LinearLayout.HORIZONTAL
                addView(backButton)
                addView(titleView)
                addView(menuButton)
            })
            addView(frameLayout)
        })
        if (savedInstanceState != null) {
            viewData = InitView(dataList).apply(initViewData)
            if (viewData.isMenu) menuButton.visibility = View.VISIBLE else menuButton.visibility = View.GONE
            val list = savedInstanceState.getStringArrayList("this")!!
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            for (name: String in list) {
                showFragment(name)
            }
            if (list.size == 1) {
                backButton.visibility = View.GONE
                if (viewData.isMenu) menuButton.visibility = View.GONE else menuButton.visibility = View.VISIBLE
            }
        } else {
            if (isLoad) {
                viewData = InitView(dataList).apply(initViewData)
                if (viewData.isMenu) menuButton.visibility = View.VISIBLE else menuButton.visibility = View.GONE
                showFragment("Main")
            }
            backButton.visibility = View.GONE
        }
    }

    fun initView(iView: InitView.() -> Unit) {
        initViewData = iView
    }

    override fun setTitle(title: CharSequence?) {
        titleView.text = title
    }

    /**
     *  获取数据绑定 / Get data-binding
     *  @param: defValue
     *  @param: CallBacks(View, Int, Any)
     *  @return: BindingData
     */
    fun getDataBinding(defValue: Any, recvCallBacks: (View, Int, Any) -> Unit): DataBinding.BindingData {
        return dataBinding.get(defValue, recvCallBacks)
    }

    /**
     *  设置 SharedPreferences / Set SharedPreferences
     *  @param: SharedPreferences
     */
    fun setSP(sharedPreferences: SharedPreferences) {
        OwnSP.ownSP = sharedPreferences
    }

    /**
     *  获取 SharedPreferences / Get SharedPreferences
     *  @return: SharedPreferences
     */
    fun getSP(): SharedPreferences {
        return OwnSP.ownSP
    }

    fun showFragment(key: String) {
        title = dataList[key]?.title
        thisName.add(key)
        fragmentManager.beginTransaction().setCustomAnimations(
            R.animator.slide_right_in,
            R.animator.slide_left_out,
            R.animator.slide_left_in,
            R.animator.slide_right_out
        ).replace(frameLayoutId, MIUIFragment()).addToBackStack(key).commit()
        if (fragmentManager.backStackEntryCount != 0) {
            backButton.visibility = View.VISIBLE
            menuButton.visibility = View.GONE
        }
    }

    fun getThisItems(): List<BaseView> {
        return dataList[thisName[thisName.lastSize()]]?.itemList ?: arrayListOf()
    }

    fun getAllCallBacks(): (() -> Unit)? {
        return callbacks
    }

    fun getDataBinding(): DataBinding {
        return dataBinding
    }

    /**
     * 设置全局返回调用 / Set global return call methods
     * @param: Unit
     */
    fun setAllCallBacks(callbacks: () -> Unit) {
        this.callbacks = callbacks
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount <= 1) {
            finish()
            if (isExit) exitProcess(0)
        } else {
            thisName.removeAt(thisName.lastSize())
            if (fragmentManager.backStackEntryCount <= 2) {
                backButton.visibility = View.GONE
                if (viewData.isMenu) menuButton.visibility = View.VISIBLE
            }
            titleView.text = dataList[fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 2).name]?.title
            fragmentManager.popBackStack()
            titleView.setPadding(0, 0, 0, 0)
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
        }
    }

    private fun ArrayList<*>.lastSize(): Int = this.size - 1

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("this", thisName)
    }

}