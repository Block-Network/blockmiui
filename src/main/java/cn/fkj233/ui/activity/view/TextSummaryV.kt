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

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.LayoutPair
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.isRtl

class TextSummaryV(private val text: String? = null, private val textId: Int? = null, private val tips: String? = null, private val colorInt: Int? = null, private val colorId: Int? = null, private val tipsId: Int? = null, private val dataBindingRecv: DataBinding.Binding.Recv? = null, val onClickListener: (() -> Unit)? = null): BaseView {

    private var notShowMargins = false

    override fun getType(): BaseView {
        return this
    }

    fun notShowMargins(boolean: Boolean) {
        notShowMargins = boolean
    }

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        return LinearContainerV(LinearContainerV.VERTICAL, arrayOf(
            LayoutPair(
                TextView(context).also { view ->
                    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, if (text == null && textId == null) 15f else 18.25f)
                    view.gravity = if (isRtl(context)) Gravity.RIGHT else Gravity.LEFT
                    colorInt?.let { view.setTextColor(colorInt) }
                    colorId?.let { view.setTextColor(context.getColor(colorId)) }
                    if (colorId == null && colorInt == null) {
                        view.setTextColor(context.getColor(R.color.whiteText))
                    }
                    text?.let { it1 -> view.text = it1 }
                    textId?.let { it1 -> view.setText(it1) }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        view.paint.typeface = Typeface.create(null, 500,false)
                    } else {
                        view.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    }
                },
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            ),
            LayoutPair(
                TextView(context).also {
                    it.gravity = if (isRtl(context)) Gravity.RIGHT else Gravity.LEFT
                    it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13.75f)
                    it.setTextColor(context.getColor(R.color.author_tips))
                    if (tips == null && tipsId == null) {
                        it.visibility = View.GONE
                    } else {
                        tips?.let { it1 -> it.text = it1 }
                        tipsId?.let { it1 -> it.setText(it1) }
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        it.paint.typeface = Typeface.create(null, 350,false)
                    } else {
                        it.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    }
                },
                LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
            )
        ), layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).also {
            if (!notShowMargins) it.setMargins(0, dp2px(context, 15f),0, dp2px(context, 15f))
        }).create(context, callBacks).also {
            dataBindingRecv?.setView(it)
        }
    }

    override fun onDraw(thiz: MIUIFragment, group: LinearLayout, view: View) {
        thiz.apply {
            group.apply {
                addView(view)
                onClickListener?.let { unit ->
                    setOnClickListener {
                        unit()
                        callBacks?.let { it1 -> it1() }
                    }
                }
            }
        }
    }
}