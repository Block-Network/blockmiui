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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.FragmentManager
import android.content.Context
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
import cn.fkj233.ui.activity.annotation.BMMainPage
import cn.fkj233.ui.activity.annotation.BMMenuPage
import cn.fkj233.ui.activity.annotation.BMPage
import cn.fkj233.ui.activity.data.AsyncInit
import cn.fkj233.ui.activity.data.BasePage
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.data.SafeSharedPreferences
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.BaseView

/**
 * @version: V1.0
 * @author: 577fkj
 * @className: MIUIActivity
 * @packageName: MIUIActivity
 * @description: BaseActivity / 基本Activity
 * @data: 2022-02-05 18:30
 **/
open class MIUIActivity : Activity() {
    private var callbacks: (() -> Unit)? = null

    private var thisName: ArrayList<String> = arrayListOf()

    private lateinit var viewData: InitView

    private val dataList: HashMap<String, InitView.ItemData> = hashMapOf()

    private lateinit var initViewData: InitView.() -> Unit

    companion object {
        var safeSP: SafeSharedPreferences = SafeSharedPreferences()

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var activity: MIUIActivity
    }

    private val backButton by lazy {
        ImageView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.gravity = Gravity.CENTER_VERTICAL
                if (isRtl(context))
                    it.setMargins(dp2px(activity, 5f), 0, 0, 0)
                else
                    it.setMargins(0, 0, dp2px(activity, 5f), 0)
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
            visibility = View.GONE
            if (isRtl(context))
                setPadding(dp2px(activity, 25f), 0, 0, 0)
            else
                setPadding(0, 0, dp2px(activity, 25f), 0)
            setOnClickListener {
                showFragment(if (this@MIUIActivity::initViewData.isInitialized) "Menu" else "__menu__")
            }
        }
    }

    private val titleView by lazy {
        TextView(activity).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).also {
                it.gravity = Gravity.CENTER_VERTICAL
            }
            gravity = if (isRtl(context)) Gravity.RIGHT else Gravity.LEFT
            setTextColor(getColor(R.color.whiteText))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f)
            paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        }
    }

    private var frameLayoutId: Int = -1
    private val frameLayout by lazy {
        val mFrameLayout = FrameLayout(activity).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        }
        frameLayoutId = View.generateViewId()
        mFrameLayout.id = frameLayoutId
        mFrameLayout
    }

    /**
     *  是否继续加载 / Continue loading
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isLoad = true

    /**
     *  退出时是否保留后台 / Retaining the background
     */
    @Suppress("MemberVisibilityCanBePrivate")
    var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        activity = this
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
            if (this::initViewData.isInitialized) {
                viewData = InitView(dataList).apply(initViewData)
                setMenuShow(viewData.isMenu)
                val list = savedInstanceState.getStringArrayList("this")!!
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                for (name: String in list) {
                    showFragment(name)
                }
                if (list.size == 1) {
                    setBackupShow(viewData.mainShowBack)
                }
                return
            }
            val list = savedInstanceState.getStringArrayList("this")!!
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            initAllPage()
            if (pageInfo.containsKey("__menu__")) setMenuShow(list.size == 1)
            for (name: String in list) {
                showFragment(name)
            }
        } else {
            if (isLoad) {
                if (this::initViewData.isInitialized) {
                    viewData = InitView(dataList).apply(initViewData)
                    setBackupShow(!viewData.mainShowBack)
                    setMenuShow(viewData.isMenu)
                    showFragment("Main")
                    return
                }
                initAllPage()
                showFragment("__main__")
            }
        }
        val showFragmentName = intent.getStringExtra("showFragment").toString()
        if (showFragmentName != "null" && showFragmentName.isNotEmpty()) {
            if (pageInfo.containsKey(showFragmentName)) {
                showFragment(showFragmentName)
                return
            }
        }
    }

    private val pageInfo: HashMap<String, BasePage> = hashMapOf()
    private val pageList: ArrayList<Class<out BasePage>> = arrayListOf()

    fun registerPage(basePage: Class<out BasePage>) {
        pageList.add(basePage)
    }

    fun initAllPage() {
        pageList.forEach { basePage ->
            if (basePage.getAnnotation(BMMainPage::class.java) != null) {
                val mainPage = basePage.newInstance()
                mainPage.activity = this
                pageInfo["__main__"] = mainPage
            } else if (basePage.getAnnotation(BMMenuPage::class.java) != null) {
                val menuPage = basePage.newInstance()
                menuPage.activity = this
                menuButton.visibility = View.VISIBLE
                pageInfo["__menu__"] = menuPage
            } else if (basePage.getAnnotation(BMPage::class.java) != null) {
                val menuPage = basePage.newInstance()
                menuPage.activity = this
                pageInfo[basePage.getAnnotation(BMPage::class.java)!!.key] = menuPage
            } else {
                throw Exception("Page must be annotated with BMMainPage or BMMenuPage or BMPage")
            }
        }
    }

    @Deprecated("This method is obsolete")
    fun initView(iView: InitView.() -> Unit) {
        initViewData = iView
    }

    override fun setTitle(title: CharSequence?) {
        titleView.text = title
    }

    /**
     *  设置 SharedPreferences / Set SharedPreferences
     *  @param: SharedPreferences
     */
    fun setSP(sharedPreferences: SharedPreferences) {
        safeSP.mSP = sharedPreferences
    }

    /**
     *  获取 SharedPreferences / Get SharedPreferences
     *  @return: SharedPreferences
     */
    @Suppress("unused")
    fun getSP(): SharedPreferences? {
        return safeSP.mSP
    }

    /**
     *  显示 Fragment / Show fragment
     *  @param: key 注册的key / Register key
     */
    fun showFragment(key: String) {
        if (this::initViewData.isInitialized) {
            title = dataList[key]?.title
            thisName.add(key)
            val frame = MIUIFragment(key)
            if (key != "Main" && fragmentManager.backStackEntryCount != 0) {
                fragmentManager.beginTransaction().let {
                    if (key != "Menu") {
                        if (isRtl(activity)) it.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_right_out, R.animator.slide_right_in, R.animator.slide_left_out)
                        else it.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out, R.animator.slide_left_in, R.animator.slide_right_out)
                    } else {
                        if (isRtl(activity)) it.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out, R.animator.slide_left_in, R.animator.slide_right_out)
                        else it.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_right_out, R.animator.slide_right_in, R.animator.slide_left_out)
                    }
                }.replace(frameLayoutId, frame).addToBackStack(key).commit()
                backButton.visibility = View.VISIBLE
                setMenuShow(dataList[key]?.hideMenu == false)
            } else {
                setBackupShow(viewData.mainShowBack)
                fragmentManager.beginTransaction().replace(frameLayoutId, frame).addToBackStack(key).commit()
            }
            return
        }
        if (!pageInfo.containsKey(key)) {
            throw Exception("No page found")
        }
        val thisPage = pageInfo[key]!!
        title = getPageTitle(thisPage)
        thisName.add(key)
        val frame = MIUIFragment(key)
        if (key != "__main__" && fragmentManager.backStackEntryCount != 0) {
            fragmentManager.beginTransaction().let {
                if (key != "__menu__") {
                    if (isRtl(activity)) it.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_right_out, R.animator.slide_right_in, R.animator.slide_left_out)
                    else it.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out, R.animator.slide_left_in, R.animator.slide_right_out)
                } else {
                    if (isRtl(activity)) it.setCustomAnimations(R.animator.slide_right_in, R.animator.slide_left_out, R.animator.slide_left_in, R.animator.slide_right_out)
                    else it.setCustomAnimations(R.animator.slide_left_in, R.animator.slide_right_out, R.animator.slide_right_in, R.animator.slide_left_out)
                }
            }.replace(frameLayoutId, frame).addToBackStack(key).commit()
            setBackupShow(true)
            if (key !in arrayOf("__main__", "__menu__")) setMenuShow(!getPageHideMenu(thisPage))
            if (key == "__menu__") setMenuShow(false)
        } else {
            setMenuShow(pageInfo.containsKey("__menu__"))
            setBackupShow(pageInfo["__main__"]!!.javaClass.getAnnotation(BMMainPage::class.java)!!.showBack)
            fragmentManager.beginTransaction().replace(frameLayoutId, frame).addToBackStack(key).commit()
        }
    }

    fun setMenuShow(show: Boolean) {
        if (this::initViewData.isInitialized) {
            if (!dataList.containsKey("Menu")) return
            if (show) menuButton.visibility = View.VISIBLE
            else menuButton.visibility = View.GONE
            return
        }
        if (pageInfo.containsKey("__menu__")) {
            if (show) {
                menuButton.visibility = View.VISIBLE
            } else {
                menuButton.visibility = View.GONE
            }
        }
    }

    fun setBackupShow(show: Boolean) {
        if (show) backButton.visibility = View.VISIBLE else backButton.visibility = View.GONE
    }

    private fun getPageHideMenu(basePage: BasePage): Boolean {
        return basePage.javaClass.getAnnotation(BMPage::class.java)?.hideMenu == true
    }

    private fun getPageTitle(basePage: BasePage): String {
        basePage.javaClass.getAnnotation(BMPage::class.java)?.let {
            return it.title.ifEmpty { if (it.titleId != 0) activity.getString(it.titleId) else basePage.getTitle() }
        }
        basePage.javaClass.getAnnotation(BMMainPage::class.java)?.let {
            return it.title.ifEmpty { if (it.titleId != 0) activity.getString(it.titleId) else basePage.getTitle() }
        }
        basePage.javaClass.getAnnotation(BMMenuPage::class.java)?.let {
            return it.title.ifEmpty { if (it.titleId != 0) activity.getString(it.titleId) else basePage.getTitle() }
        }
        throw Exception("No title found")
    }

    fun getTopPage(): String {
        return thisName[thisName.lastSize()]
    }

    fun getThisItems(key: String): List<BaseView> {
        if (this::initViewData.isInitialized) {
            return dataList[key]?.itemList ?: arrayListOf()
        }
        val currentPage = pageInfo[key]!!
        if (currentPage.itemList.size == 0) {
            currentPage.onCreate()
        }
        return currentPage.itemList
    }

    fun getThisAsync(key: String): AsyncInit? {
        if (this::initViewData.isInitialized) {
            return dataList[key]?.async
        }
        val currentPage = pageInfo[key]!!
        if (currentPage.itemList.size == 0) {
            currentPage.onCreate()
        }
        return object : AsyncInit {
            override val skipLoadItem: Boolean
                get() = currentPage.skipLoadItem

            override fun onInit(fragment: MIUIFragment) {
                currentPage.asyncInit(fragment)
            }
        }
    }

    fun getAllCallBacks(): (() -> Unit)? {
        return callbacks
    }

    /**
     * 设置全局返回调用 / Set global return call methods
     * @param: Unit
     */
    @Suppress("unused")
    fun setAllCallBacks(callbacks: () -> Unit) {
        this.callbacks = callbacks
    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount <= 1) {
            if (isExit) {
                finishAndRemoveTask()
            } else {
                finish()
            }
        } else {
            thisName.removeAt(thisName.lastSize())
            val name = fragmentManager.getBackStackEntryAt(fragmentManager.backStackEntryCount - 2).name
            when (name) {
                "Main" -> {
                    if (!viewData.mainShowBack) backButton.visibility = View.GONE
                    if (viewData.isMenu) menuButton.visibility = View.VISIBLE
                }

                "__main__" -> {
                    if (!pageInfo[name]!!.javaClass.getAnnotation(BMMainPage::class.java)!!.showBack) backButton.visibility = View.GONE
                    setMenuShow(pageInfo.containsKey("__menu__"))
                }

                else -> {
                    if (this::initViewData.isInitialized) {
                        setMenuShow(dataList[name]?.hideMenu == false)
                    } else {
                        setMenuShow(!getPageHideMenu(pageInfo[name]!!))
                    }
                }
            }
            title = if (this::initViewData.isInitialized) {
                dataList[name]?.title
            } else {
                getPageTitle(pageInfo[name]!!)
            }
            fragmentManager.popBackStack()
        }
    }

    private fun ArrayList<*>.lastSize(): Int = this.size - 1

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("this", thisName)
    }

}