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
import cn.fkj233.ui.activity.data.Padding
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.isRtl

class TextV(val text: String? = null, private val textId: Int? = null, val textSize: Float? = null, private val colorInt: Int? = null, private val colorId: Int? = null, private val padding: Padding? = null, private val dataBindingRecv: DataBinding.Binding.Recv? = null, private val typeface: Typeface? = null, val onClickListener: (() -> Unit)? = null): BaseView {

    private var notShowMargins = false

    override fun getType(): BaseView = this

    fun notShowMargins(boolean: Boolean) {
        notShowMargins = boolean
    }

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        return TextView(context).also { view ->
            view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text?.let { view.text = it }
            view.gravity = if (isRtl(context)) Gravity.RIGHT else Gravity.LEFT
            textId?.let { view.setText(it) }
            if (textSize == null)
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
            else
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
            colorInt?.let { view.setTextColor(colorInt) }
            colorId?.let { view.setTextColor(context.getColor(colorId)) }
            if (colorId == null && colorInt == null) {
                view.setTextColor(context.getColor(R.color.whiteText))
            }
            if (typeface == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    view.paint.typeface = Typeface.create(null, 500, false)
                } else {
                    view.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    view.paint.typeface = typeface
                } else {
                    view.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
            }
            if (!notShowMargins) {
                if (isRtl(context)) view.setPadding(dp2px(context, 5f), dp2px(context, 20f), 0, dp2px(context, 20f))
                else view.setPadding(0, dp2px(context, 20f), dp2px(context, 5f), dp2px(context, 20f))
            }
            padding?.let {
                if (isRtl(context))
                    view.setPadding(it.right, it.top, it.left, it.bottom)
                else
                    view.setPadding(it.left, it.top, it.right, it.bottom)
            }
            dataBindingRecv?.setView(view)
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
