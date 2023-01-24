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
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.isRtl

class TitleTextV(val text: String? = null, private val textId: Int? = null, private val colorInt: Int? = null, private val colorId: Int? = null, private val dataBindingRecv: DataBinding.Binding.Recv? = null, private val onClickListener: (() -> Unit)? = null) : BaseView {

    override fun getType(): BaseView {
        return this
    }

    override fun create(context: Context, callBacks: (() -> Unit)?): View {


        return TextView(context).also { view ->
            view.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            text?.let { view.text = it }
            view.gravity = if (isRtl(context)) Gravity.RIGHT else Gravity.LEFT
            textId?.let { view.setText(it) }
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
            view.setTextColor(Color.parseColor("#9399b3"))
            colorInt?.let { view.setTextColor(colorInt) }
            colorId?.let { view.setTextColor(context.getColor(colorId)) }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                view.paint.typeface = Typeface.create(null, 400, false)
            } else {
                view.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }

            view.setPadding(0, dp2px(context, 9.75f), dp2px(context, 5f), dp2px(context, 9.75f))
            onClickListener?.let { view.setOnClickListener { it() } }
            dataBindingRecv?.setView(view)
        }

    }

}