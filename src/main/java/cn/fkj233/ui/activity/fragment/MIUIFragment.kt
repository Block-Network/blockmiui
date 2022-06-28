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

@file:Suppress("DEPRECATION", "DuplicatedCode")

package cn.fkj233.ui.activity.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.Fragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.annotation.Keep
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.AsyncInit
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.view.*


@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("ValidFragment")
@Keep
class MIUIFragment() : Fragment() {
    private var key = ""
    private lateinit var scrollView: ScrollView
    private lateinit var itemView: LinearLayout
    val callBacks: (() -> Unit)? by lazy { (activity as MIUIActivity).getAllCallBacks() }
    private val async: AsyncInit? by lazy { (activity as MIUIActivity).getThisAsync(key) }
    private var dialog: Dialog? = null
    val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    constructor(keys: String) : this() {
        key = keys
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        scrollView = ScrollView(context).apply {
            isVerticalScrollBarEnabled = false
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            isFillViewport = true
            addView(LinearLayout(context).apply { // 总布局
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.VERTICAL
                background = context.getDrawable(R.color.foreground)
                itemView = this
                if (async?.skipLoadItem != true) initData()
            })
        }
        async?.let { Thread { it.onInit(this) }.start() }
        return scrollView
    }

    /**
     *  如果 skipLoadItem 为 true 则需要手动调用 / If skipLoadItem is true, it needs to be called manually
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun initData() {
        for (item: BaseView in (activity as MIUIActivity).getThisItems(key)) {
            addItem(item)
        }
    }

    /**
     *  Show loading dialog / 显示加载 Dialog
     */
    fun showLoading() {
        Log.e("showLoading", "show")
        handler.post {
            dialog = Dialog(activity, R.style.Translucent_NoTitle).apply {
                setCancelable(false)
                setContentView(LinearLayout(context).apply {
                    layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                    addView(ImageView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(dp2px(context, 60f), dp2px(context, 60f)).also { it.setMargins(dp2px(context, 20f), dp2px(context, 20f), dp2px(context, 20f), dp2px(context, 20f)) }
                        background = context.getDrawable(R.drawable.ic_loading)
                        startAnimation(AnimationSet(true).apply {
                            interpolator = LinearInterpolator()
                            addAnimation(RotateAnimation(0f, +359f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f).apply {
                                repeatCount = -1
                                startOffset = 0
                                duration = 1000
                            })
                        })
                    })
                })
            }
            dialog?.show()
        }
    }

    /**
     *  Close loading dialog / 关闭加载 Dialog
     */
    fun closeLoading() {
        handler.post { dialog?.dismiss() }
    }

    /**
     *  Add view / 添加控件
     */
    @SuppressLint("ClickableViewAccessibility")
    fun addItem(item: BaseView) {
        handler.post {
            itemView.addView(LinearLayout(context).apply { // 控件布局
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                background = context.getDrawable(R.drawable.ic_click_check)
                setPadding(dp2px(context, 30f), 0, dp2px(context, 30f), 0)
                when (item) {
                    is SeekBarV -> { // 滑动条
                        addView(LinearLayout(context).apply {
                            setPadding(dp2px(activity, 12f), 0, dp2px(activity, 12f), 0)
                            addView(item.create(context, callBacks), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                        })
                    }
                    is SeekBarWithTextV -> { // 滑动条 带文本
                        addView(item.create(context, callBacks), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
                    }
                    is TextV -> { // 文本
                        addView(item.create(context, callBacks))
                        item.onClickListener?.let { unit ->
                            setOnClickListener {
                                unit()
                                callBacks?.let { it1 -> it1() }
                            }
                        }
                    }
                    is SwitchV -> addView(item.create(context, callBacks)) // 开关
                    is TextWithSwitchV -> {
                        addView(item.create(context, callBacks)) // 带文本的开关
                        setOnTouchListener { _, motionEvent ->
                            when (motionEvent.action) {
                                MotionEvent.ACTION_DOWN -> if (item.switchV.switch.isEnabled) background = context.getDrawable(R.drawable.ic_main_down_bg)
                                MotionEvent.ACTION_UP -> {
                                    if (item.switchV.switch.isEnabled) {
                                        item.switchV.click()
                                        callBacks?.let { it1 -> it1() }
                                        background = context.getDrawable(R.drawable.ic_main_bg)
                                    }
                                }
                                else -> background = context.getDrawable(R.drawable.ic_main_bg)
                            }
                            true
                        }
                    }
                    is TitleTextV -> addView(item.create(context, callBacks)) // 标题文字
                    is LineV -> addView(item.create(context, callBacks)) // 分割线
                    is LinearContainerV -> addView(item.create(context, callBacks)) // 布局创建
                    is AuthorV -> { // 作者框
                        addView(item.create(context, callBacks))
                        item.onClick?.let { unit ->
                            setOnClickListener {
                                unit()
                                callBacks?.let { it1 -> it1() }
                            }
                        }
                    }
                    is TextSummaryV -> { // 带箭头和提示的文本框
                        addView(item.create(context, callBacks))
                        item.onClickListener?.let { unit ->
                            setOnClickListener {
                                unit()
                                callBacks?.let { it1 -> it1() }
                            }
                        }
                    }
                    is SpinnerV -> { // 下拉选择框
                        addView(item.create(context, callBacks))
                    }
                    is TextSummaryWithSpinnerV, is TextWithSpinnerV -> {
                        addView(item.create(context, callBacks))
                        setOnClickListener {}
                        val spinner = when (item) {
                            is TextSummaryWithSpinnerV -> item.spinnerV
                            is TextWithSpinnerV -> item.spinnerV
                            else -> throw IllegalAccessException("Not is TextSummaryWithSpinnerV or TextWithSpinnerV")
                        }
                        setOnTouchListener { view, motionEvent ->
                            if (motionEvent.action == MotionEvent.ACTION_UP) {
                                val popup = MIUIPopup(context, view, spinner.currentValue, {
                                    spinner.select.text = it
                                    spinner.currentValue = it
                                    callBacks?.let { it1 -> it1() }
                                    spinner.dataBindingSend?.send(it)
                                }, SpinnerV.SpinnerData().apply(spinner.data).arrayList)
                                if (view.width / 2 >= motionEvent.x) {
                                    popup.apply {
                                        horizontalOffset = dp2px(context, 24F)
                                        setDropDownGravity(Gravity.LEFT)
                                    }
                                } else {
                                    popup.apply {
                                        horizontalOffset = -dp2px(context, 24F)
                                        setDropDownGravity(Gravity.RIGHT)
                                    }
                                }
                                popup.show()
                            }
                            false
                        }
                    }
                    is TextSummaryArrowV -> {
                        addView(item.create(context, callBacks))
                        item.textSummaryV.onClickListener?.let { unit ->
                            setOnClickListener {
                                unit()
                                callBacks?.let { it1 -> it1() }
                            }
                        }
                    }
                    is TextSummaryWithSwitchV -> {
                        addView(item.create(context, callBacks))
                        setOnClickListener {
                            item.switchV.click()
                            callBacks?.let { it1 -> it1() }
                        }
                    }
                    is CustomViewV -> {
                        addView(item.create(context, callBacks))
                    }
                    is RadioViewV -> {
                        setPadding(0, 0, 0, 0)
                        addView(item.create(context, callBacks))
                    }
                }
            })
        }
    }
}
