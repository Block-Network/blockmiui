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

package cn.fkj233.ui.activity.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.LayoutPair
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.fragment.MIUIFragment

class TextWithSpinnerV(private val textV: TextV, private val spinnerV: SpinnerV, private val dataBindingRecv: DataBinding.Binding.Recv? = null): BaseView {

    override fun getType(): BaseView = this

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        return LinearContainerV(
            LinearContainerV.HORIZONTAL,
            arrayOf(
                LayoutPair(textV.create(context, callBacks), LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)),
                LayoutPair(spinnerV.create(context, callBacks), LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also { it.gravity = Gravity.CENTER_VERTICAL })
            ),
            descendantFocusability = LinearContainerV.FOCUS_BLOCK_DESCENDANTS,
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        ).create(context, callBacks).also {
            dataBindingRecv?.setView(it)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onDraw(thiz: MIUIFragment, group: LinearLayout, view: View) {
        thiz.apply {
            group.apply {
                addView(view)
                setOnClickListener {}
                setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        val popup = MIUIPopup(context, view, spinnerV.currentValue, spinnerV.dropDownWidth, {
                            spinnerV.select.text = it
                            spinnerV.currentValue = it
                            callBacks?.let { it1 -> it1() }
                            spinnerV.dataBindingSend?.send(it)
                        }, SpinnerV.SpinnerData().apply(spinnerV.data).arrayList)
                        if (view.width / 2 >= motionEvent.x) {
                            popup.apply {
                                horizontalOffset = dp2px(context, 24F)
                                setDropDownGravity(Gravity.LEFT)
                            }
                        } else {
                            popup.apply {
                                horizontalOffset = - dp2px(context, 24F)
                                setDropDownGravity(Gravity.RIGHT)
                            }
                        }
                        popup.show()
                    }
                    false
                }
            }
        }
    }
}