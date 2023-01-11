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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import cn.fkj233.ui.activity.view.BaseView


@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("ValidFragment")
@Keep
class MIUIFragment() : Fragment() {
    private var key = ""
    private lateinit var scrollView: ScrollView
    private lateinit var itemView: LinearLayout
    val callBacks: (() -> Unit)? by lazy { MIUIActivity.activity.getAllCallBacks() }
    private val async: AsyncInit? by lazy { MIUIActivity.activity.getThisAsync(key.ifEmpty { MIUIActivity.activity.getTopPage() }) }
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
        for (item: BaseView in MIUIActivity.activity.getThisItems(key.ifEmpty { MIUIActivity.activity.getTopPage() })) {
            addItem(item)
        }
    }

    /**
     *  Show loading dialog / 显示加载 Dialog
     */
    fun showLoading() {
        handler.post {
            dialog = Dialog(MIUIActivity.context, R.style.Translucent_NoTitle).apply {
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
            itemView.addView(LinearLayout(MIUIActivity.context).apply { // 控件布局
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                background = context.getDrawable(R.drawable.ic_click_check)
                setPadding(dp2px(context, 30f), 0, dp2px(context, 30f), 0)
                item.onDraw(this@MIUIFragment, this, item.create(context, callBacks))
            })
        }
    }
}
