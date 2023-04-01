package cn.fkj233.ui.activity.view

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.dp2px

class MIUIEditText(context: Context) : EditText(context) {
    init {
        this.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
            it.setMargins(dp2px(context, 30f), dp2px(context, 10f), dp2px(context, 30f), 0)
        }
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
        this.setTextColor(context.getColor(R.color.whiteText))
        this.gravity = Gravity.CENTER_VERTICAL
        this.setPadding(dp2px(context, 20f), dp2px(context, 15f), dp2px(context, 20f), dp2px(context, 15f))
        this.background = context.getDrawable(R.drawable.editview_background)
        this.isSingleLine = true
        this.setHintTextColor(context.getColor(R.color.hintText))
        this.visibility = View.GONE
    }
}