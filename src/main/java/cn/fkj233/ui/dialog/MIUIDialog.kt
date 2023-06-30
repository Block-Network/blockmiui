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

package cn.fkj233.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.isRtl
import cn.fkj233.ui.activity.view.MIUIEditText
import kotlin.math.roundToInt


@SuppressLint("ClickableViewAccessibility")
class MIUIDialog(context: Context, private val newStyle: Boolean = true, val build: MIUIDialog.() -> Unit) : Dialog(context, R.style.CustomDialog) {

    private var finallyCallBacks: ((View) -> Unit)? = null

    private val title by lazy {
        TextView(context).also { textView ->
            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.setMargins(0, dp2px(context, 20f), 0, dp2px(context, 20f))
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                textView.paint.typeface = Typeface.create(null, 500, false)
            }
            textView.setTextColor(context.getColor(R.color.whiteText))
            textView.gravity = Gravity.CENTER
            textView.setPadding(0, dp2px(context, 10f), 0, 0)
        }
    }

    private val message by lazy {
        TextView(context).also { textView ->
            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.setMargins(dp2px(context, 20f), 0, dp2px(context, 20f), dp2px(context, 5f))
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            textView.setTextColor(context.getColor(R.color.whiteText))
            textView.gravity = Gravity.CENTER
            textView.visibility = View.GONE
            textView.setPadding(dp2px(context, 10f), 0, dp2px(context, 10f), 0)
        }
    }

    private val editText by lazy { MIUIEditText(context) }

    private val rButton by lazy {
        Button(context).also { buttonView ->
            buttonView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 48f), 1f).also {
                it.setMargins(dp2px(context, 25f), 0, dp2px(context, 25f), 0)
                it.gravity = Gravity.CENTER
            }
            buttonView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            buttonView.setTextColor(context.getColor(R.color.RButtonText))
            buttonView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.5f)
            buttonView.stateListAnimator = null
            buttonView.background = context.getDrawable(R.drawable.r_button_background)
            buttonView.visibility = View.GONE
        }
    }

    private val lButton by lazy {
        Button(context).also { buttonView ->
            buttonView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 48f), 1f).also {
                it.setMargins(dp2px(context, 25f), 0, dp2px(context, 25f), 0)
                it.gravity = Gravity.CENTER
            }
            buttonView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            buttonView.setTextColor(context.getColor(R.color.LButtonText))
            buttonView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.5f)
            buttonView.stateListAnimator = null
            buttonView.visibility = View.GONE
            buttonView.background = context.getDrawable(R.drawable.l_button_background)
        }
    }

    private val view by lazy {
        LinearLayout(context).also { linearLayout ->
            linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.addView(message)
            linearLayout.addView(editText)
        }
    }

    private val root = RelativeLayout(context).also { viewRoot ->
        viewRoot.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        viewRoot.addView(LinearLayout(context).also { viewLinearLayout ->
            viewLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            viewLinearLayout.orientation = LinearLayout.VERTICAL
            viewLinearLayout.addView(title)
            viewLinearLayout.addView(view)
            viewLinearLayout.addView(LinearLayout(context).also { linearLayout ->
                linearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                    it.gravity = Gravity.CENTER_HORIZONTAL
                }
                linearLayout.setPadding(0, dp2px(context, 16f), 0, dp2px(context, 35f))
                linearLayout.addView(lButton)
                linearLayout.addView(rButton)
            })
        })
    }

    init {
        window?.setGravity(Gravity.BOTTOM)
        setContentView(root)
        window?.setBackgroundDrawable(GradientDrawable().apply {
            if (newStyle) {
                cornerRadius = dp2px(context, 30f).toFloat()
            } else {
                val dp30 = dp2px(context, 30f).toFloat()
                cornerRadii = floatArrayOf(dp30, dp30, dp30, dp30, 0f, 0f, 0f, 0f)
            }
            setColor(context.getColor(R.color.dialog_background))
        })
    }

    fun addView(mView: View) {
        view.addView(mView)
    }

    override fun setTitle(title: CharSequence?) {
        this.title.text = title
    }

    override fun setTitle(titleId: Int) {
        this.title.setText(titleId)
    }

    fun setLButton(text: CharSequence?, enable: Boolean = true) {
        setLButton(text, enable, null)
    }

    fun setLButton(text: CharSequence?, enable: Boolean = true, callBacks: ((View) -> Unit)?) {
        lButton.apply {
            this.isEnabled = enable
            visibility = View.VISIBLE
            setText(text)
            setOnClickListener {
                callBacks?.invoke(it)
                finallyCallBacks?.invoke(it)
            }
        }
    }

    fun setLButton(textId: Int, enable: Boolean = true) {
        setLButton(textId, enable, null)
    }

    fun setLButton(textId: Int, enable: Boolean = true, callBacks: ((View) -> Unit)?) {
        lButton.apply {
            this.isEnabled = enable
            visibility = View.VISIBLE
            setText(textId)
            setOnClickListener {
                callBacks?.invoke(it)
                finallyCallBacks?.invoke(it)
            }
        }
    }

    fun setRButton(text: CharSequence?, enable: Boolean = true) {
        setRButton(text, enable, null)
    }

    fun setRButton(text: CharSequence?, enable: Boolean = true, callBacks: ((View) -> Unit)?) {
        rButton.apply {
            setText(text)
            this.isEnabled = enable
            setOnClickListener {
                callBacks?.invoke(it)
                finallyCallBacks?.invoke(it)
            }
            visibility = View.VISIBLE
        }
    }

    fun setRButton(textId: Int, enable: Boolean = true) {
        setRButton(textId, enable, null)
    }

    fun setRButton(textId: Int, enable: Boolean = true, callBacks: ((View) -> Unit)?) {
        rButton.apply {
            setText(textId)
            this.isEnabled = enable
            setOnClickListener {
                callBacks?.invoke(it)
                finallyCallBacks?.invoke(it)
            }
            visibility = View.VISIBLE
        }
    }

    fun getRButton(): TextView = rButton

    fun getLButton(): TextView = lButton

    override fun show() {
        build()
        window!!.setWindowAnimations(R.style.DialogAnim)
        if (rButton.visibility == View.VISIBLE && lButton.visibility == View.VISIBLE) {
            if (isRtl(context)) {
                (rButton.layoutParams as LinearLayout.LayoutParams).setMargins(dp2px(context, 30f), 0, dp2px(context, 5f), 0)
                (lButton.layoutParams as LinearLayout.LayoutParams).setMargins(dp2px(context, 5f), 0, dp2px(context, 30f), 0)
            } else {
                (rButton.layoutParams as LinearLayout.LayoutParams).setMargins(dp2px(context, 5f), 0, dp2px(context, 30f), 0)
                (lButton.layoutParams as LinearLayout.LayoutParams).setMargins(dp2px(context, 30f), 0, dp2px(context, 5f), 0)
            }
        }
        super.show()
        val layoutParams = window!!.attributes
        layoutParams.dimAmount = 0.5F
        if (newStyle) {
            val resources = context.resources
            val dm: DisplayMetrics = resources.displayMetrics
            val width = dm.widthPixels
            layoutParams.width = (width * 0.92f).roundToInt()
            layoutParams.y = (width * 0.045f).roundToInt()
        } else {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        }
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = layoutParams
    }

    fun setMessage(textId: Int, isCenter: Boolean = true) {
        if (isCenter) {
            message.gravity = Gravity.CENTER
        } else {
            message.gravity = Gravity.START
        }
        message.apply {
            setText(textId)
            visibility = View.VISIBLE
        }
    }

    fun setMessage(text: CharSequence, isCenter: Boolean = true) {
        if (isCenter) {
            message.gravity = Gravity.CENTER
        } else {
            message.gravity = Gravity.START
        }
        message.apply {
            this.text = text
            visibility = View.VISIBLE
        }
    }

    fun setEditText(text: String, hint: String, isSingleLine: Boolean = true, config: ((EditText) -> Unit)? = null, editCallBacks: ((String) -> Unit)? = null) {
        editText.apply {
            setText(text.toCharArray(), 0, text.length)
            this.hint = hint
            this.isSingleLine = isSingleLine
            this.maxLines = 5
            visibility = View.VISIBLE
            editCallBacks?.let {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(var1: Editable?) {
                        it(var1.toString())
                    }

                    override fun beforeTextChanged(var1: CharSequence?, var2: Int, var3: Int, var4: Int) {}
                    override fun onTextChanged(var1: CharSequence?, var2: Int, var3: Int, var4: Int) {}
                })
            }
            config?.invoke(this)
        }
    }

    fun finally(callBacks: (View) -> Unit) {
        finallyCallBacks = callBacks
    }

    fun getEditText(): String = editText.text.toString()
}