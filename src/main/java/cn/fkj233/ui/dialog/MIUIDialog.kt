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

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.*
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.getDisplay
import cn.fkj233.ui.activity.isRtl

class MIUIDialog(context: Context, val build: MIUIDialog.() -> Unit) : Dialog(context, R.style.CustomDialog) {
    private val title by lazy {
        TextView(context).also { textView ->
            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.setMargins(0, dp2px(context, 20f), 0, dp2px(context, 20f))
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19f)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                textView.paint.typeface = Typeface.create(null, 500,false)
            }
            textView.setTextColor(context.getColor(R.color.whiteText))
            textView.gravity = Gravity.CENTER
            textView.setPadding(0, dp2px(context, 10f), 0, 0)
        }
    }

    private val message by lazy {
        TextView(context).also { textView ->
            textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.setMargins(dp2px(context, 10f), 0, dp2px(context, 10f), dp2px(context, 5f))
            }
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            textView.setTextColor(context.getColor(R.color.whiteText))
            textView.gravity = Gravity.CENTER
            textView.visibility = View.GONE
            textView.setPadding(dp2px(context, 10f), 0, dp2px(context, 10f), 0)
        }
    }

    private val editText by lazy {
        EditText(context).also { editText ->
            editText.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 55f)).also {
                it.setMargins(dp2px(context, 30f), dp2px(context, 10f), dp2px(context, 30f), 0)
            }
            editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            editText.setTextColor(context.getColor(R.color.whiteText))
            editText.gravity = Gravity.CENTER
            editText.setPadding(dp2px(context, 8f), dp2px(context, 8f), dp2px(context, 8f), dp2px(context, 8f))
            editText.visibility = View.GONE
            editText.background = context.getDrawable(R.drawable.editview_background)
            val mHeight = dp2px(context, 55f)
            @Suppress("DEPRECATION")
            val maxHeight = getDisplay(context).height / 2
            editText.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    editText.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val params = editText.layoutParams as LinearLayout.LayoutParams
                    if (editText.lineCount <= 1) {
                        params.height = mHeight
                    } else {
                        var tempHeight = mHeight
                        for (i in 0..editText.lineCount) {
                            tempHeight += mHeight / 2 - 20
                        }
                        params.height = if (tempHeight >= maxHeight) maxHeight else tempHeight
                    }
                    editText.layoutParams = params
                }
            })
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val params = editText.layoutParams as LinearLayout.LayoutParams
                    if (editText.lineCount <= 1) {
                        params.height = mHeight
                    } else {
                        var tempHeight = mHeight
                        for (i in 0..editText.lineCount) {
                            tempHeight += mHeight / 2 - 20
                        }
                        params.height = if (tempHeight >= maxHeight) maxHeight else tempHeight
                    }
                    editText.layoutParams = params
                }
            })
        }
    }

    private val rButton by lazy {
        Button(context).also { buttonView ->
            buttonView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 52.5f), 1f).also {
                it.setMargins(dp2px(context, 25f), 0, dp2px(context, 25f), 0)
                it.gravity = Gravity.CENTER
            }
            buttonView.setTextColor(context.getColor(R.color.white))
            buttonView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.5f)
            buttonView.stateListAnimator = null
            buttonView.background = context.getDrawable(R.drawable.r_button_background)
            buttonView.visibility = View.GONE
        }
    }

    private val lButton by lazy {
        Button(context).also { buttonView ->
            buttonView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp2px(context, 52.5f), 1f).also {
                it.setMargins(dp2px(context, 25f), 0, dp2px(context, 25f), 0)
                it.gravity = Gravity.CENTER
            }
            buttonView.setTextColor(context.getColor(R.color.whiteText))
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

    fun setLButton(text: CharSequence?, enable: Boolean = true, callBacks: (View) -> Unit) {
        lButton.apply {
            this.isEnabled = enable
            visibility = View.VISIBLE
            setText(text)
            setOnClickListener {
                callBacks(it)
            }
        }
    }

    fun setLButton(textId: Int, enable: Boolean = true, callBacks: (View) -> Unit) {
        lButton.apply {
            this.isEnabled = enable
            visibility = View.VISIBLE
            setText(textId)
            setOnClickListener {
                callBacks(it)
            }
        }
    }

    fun setRButton(text: CharSequence?, enable: Boolean = true, callBacks: (View) -> Unit) {
        rButton.apply {
            setText(text)
            this.isEnabled = enable
            setOnClickListener {
                callBacks(it)
            }
            visibility = View.VISIBLE
        }
    }

    fun setRButton(textId: Int, enable: Boolean = true, callBacks: (View) -> Unit) {
        rButton.apply {
            setText(textId)
            this.isEnabled = enable
            setOnClickListener {
                callBacks(it)
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
        layoutParams.dimAmount = 0.3F
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = layoutParams
    }

    fun setMessage(textId: Int) {
        message.apply {
            setText(textId)
            visibility = View.VISIBLE
        }
    }

    fun setMessage(text: CharSequence?) {
        message.apply {
            this.text = text
            visibility = View.VISIBLE
        }
    }

    fun setEditText(text: String, hint: String, editCallBacks: ((String) -> Unit)? = null) {
        editText.apply {
            setText(text.toCharArray(), 0, text.length)
            this.hint = hint
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
        }
    }

    fun getEditText(): String = editText.text.toString()
}