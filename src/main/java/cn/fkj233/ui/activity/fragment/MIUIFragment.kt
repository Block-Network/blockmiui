/*
 * BlockMIUI
 * Copyright (C) 2022 fkj@fkj233.cn
 * https://github.com/577fkj/BlockMIUI
 *
 * This software is free opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by 577fkj.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/577fkj/BlockMIUI/blob/main/LICENSE>.
 */

@file:Suppress("DEPRECATION")

package cn.fkj233.ui.activity.fragment

import android.annotation.SuppressLint
import android.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ScrollView
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.view.*
import cn.fkj233.ui.activity.dp2px

@SuppressLint("ValidFragment")
class MIUIFragment : Fragment() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View =
        ScrollView(context).apply { // 滑动布局
            val dataBinding: DataBinding = (activity as MIUIActivity).getDataBinding()
            val callBacks: (() -> Unit)? = (activity as MIUIActivity).getAllCallBacks()
            val itemList: List<BaseView> = (activity as MIUIActivity).getThisItems()
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
            addView(
                LinearLayout(context).apply { // 总布局
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.VERTICAL
                    background = context.getDrawable(R.color.foreground)
                    for (item: BaseView in itemList) {
                        addView(LinearLayout(context).apply { // 控件布局
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            background = context.getDrawable(R.drawable.ic_click_check)
                            setPadding(dp2px(context, 25f), 0, dp2px(context, 25f), 0)
                            when (item) {
                                is SeekBarV -> { // 滑动条
                                    addView(LinearLayout(context).apply {
                                        setPadding(dp2px(activity, 12f), 0, dp2px(activity, 12f), 0)
                                        addView(
                                            item.create(context, callBacks),
                                            LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT
                                            )
                                        )
                                    })
                                }
                                is SeekBarWithTextV -> { // 滑动条 带文本
                                    addView(
                                        item.create(context, callBacks),
                                        LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        )
                                    )
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
                                is TextWithSwitchV -> addView(item.create(context, callBacks)) // 带文本的开关
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
                                is TextWithSpinnerV -> { // 带文本的下拉选择框
                                    addView(item.create(context, callBacks))
                                    setOnClickListener {}
                                    setOnTouchListener { view, motionEvent ->
                                        if (motionEvent.action == MotionEvent.ACTION_UP) {
                                            val popup = MIUIPopup(
                                                context,
                                                view,
                                                item.spinnerV.currentValue,
                                                {
                                                    item.spinnerV.select.text = it
                                                    item.spinnerV.currentValue = it
                                                    callBacks?.let { it1 -> it1() }
                                                },
                                                item.spinnerV.arrayList
                                            )
                                            if (view.width / 2 >= motionEvent.x) {
                                                popup.apply {
                                                    horizontalOffset = dp2px(context, 24F)
                                                    setDropDownGravity(Gravity.LEFT)
                                                    show()
                                                }
                                            } else {
                                                popup.apply {
                                                    horizontalOffset = -dp2px(context, 24F)
                                                    setDropDownGravity(Gravity.RIGHT)
                                                    show()
                                                }
                                            }
                                        }
                                        false
                                    }
                                }
                            }
                        })
                    }
                }
            )
            dataBinding.initAll()
        }
}