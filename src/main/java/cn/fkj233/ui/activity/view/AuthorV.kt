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
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.data.LayoutPair
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.isRtl

class AuthorV(private val authorHead: Drawable, private val authorName: String, private val authorTips: String? = null, private val round: Float = 30f, private val onClick: (() -> Unit)? = null, private val dataBindingRecv: DataBinding.Binding.Recv? = null): BaseView {

    override fun getType(): BaseView {
        return this
    }

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        return LinearContainerV(LinearContainerV.HORIZONTAL, arrayOf(
            LayoutPair(
                RoundCornerImageView(context, dp2px(context, round), dp2px(context, round)).also {
                    it.setPadding(0, dp2px(context, 10f), 0, dp2px(context, 10f))
                    it.background = authorHead
                },
                LinearLayout.LayoutParams(
                    dp2px(context, 60f),
                    dp2px(context, 60f)
                )
            ),
            LayoutPair(
                LinearContainerV(
                    LinearContainerV.VERTICAL, arrayOf(
                    LayoutPair(
                        TextView(context).also {
                            if (isRtl(context))
                                it.setPadding(0, dp2px(context, if (authorTips == null) 17f else 5f), dp2px(context, 15f), 0)
                            else
                                it.setPadding(dp2px(context, 15f), dp2px(context, if (authorTips == null) 17f else 5f), 0, 0)
                            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                            it.setTextColor(context.getColor(R.color.whiteText))
                            it.text = authorName
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                it.paint.typeface = Typeface.create(null, 500,false)
                            } else {
                                it.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }
                        },
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    ),
                    LayoutPair(
                        TextView(context).also {
                            if (isRtl(context))
                                it.setPadding(0, 0, dp2px(context, 16f), 0)
                            else
                                it.setPadding(dp2px(context, 16f), 0, 0, 0)
                            it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                            it.setTextColor(context.getColor(R.color.author_tips))
                            if (authorTips == null) {
                                it.visibility = View.GONE
                            } else {
                                it.text = authorTips
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                it.paint.typeface = Typeface.create(null, 400,false)
                            } else {
                                it.paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                            }
                        },
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                )).create(context, callBacks),
                LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            ),
            LayoutPair(
                ImageView(context).also {
                    it.background = context.getDrawable(R.drawable.ic_right_arrow)
                },
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                    it.gravity = Gravity.CENTER_VERTICAL
                    if (isRtl(context)) it.setMargins(0, 0, dp2px(context, 5f), 0)
                }
            )
        ), layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).also {
               it.setMargins(0, dp2px(context, 10f), 0, dp2px(context, 10f))
        }).create(context, callBacks).also {
            dataBindingRecv?.setView(it)
        }
    }

    override fun onDraw(thiz: MIUIFragment, group: LinearLayout, view: View) {
        thiz.apply {
            group.apply {
                addView(view)
                onClick?.let { unit ->
                    setOnClickListener {
                        unit()
                        callBacks?.let { it1 -> it1() }
                    }
                }
            }
        }
    }
}